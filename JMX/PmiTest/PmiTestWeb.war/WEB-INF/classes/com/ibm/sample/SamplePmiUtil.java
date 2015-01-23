package com.ibm.sample;

import java.util.Iterator;
import java.util.Set;

import javax.management.InstanceNotFoundException;
import javax.management.MBeanException;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.ReflectionException;

import com.ibm.websphere.management.AdminClient;
import com.ibm.websphere.management.AdminClientFactory;
import com.ibm.websphere.management.exception.ConnectorException;
import com.ibm.websphere.pmi.stat.StatDescriptor;
import com.ibm.websphere.pmi.stat.WSJDBCConnectionPoolStats;
import com.ibm.websphere.pmi.stat.WSJVMStats;
import com.ibm.websphere.pmi.stat.WSRangeStatistic;
import com.ibm.websphere.pmi.stat.WSStats;
import com.ibm.websphere.pmi.stat.WSThreadPoolStats;
import com.ibm.websphere.pmi.stat.WSTimeStatistic;

/**
 * 샘플을 수행하기에 앞서 해야 할 것들. 
 * 1. PMI 를 enable 시킬 것!!! 또한, PMI 의 각 해당 항목들도 Enable 시켜야 함. 
 * (PMI 항목들을 모두 Enable 시키지 말고, "PMI > [서버명] > 사용자 정의" 로 들어가서 조건에 부합되는 세부 항목들만
 * Enable 시킬 것!!!!
 * 
 * 2. "Application Server > [서버명] > 프로세스 정의 > JVM(Java Virtual Machine) > 사용자 정의 특성"
 * 으로 가서 다음 항목들 추가.
 * 
 * pmilog.host=localhost <= 그냥 localhost 로 할 것. 
 * pmilog.port=8882 <= 이 값은 Dmgr 이나 nodeagent 가 아니라, 현재 WAS 서버의 SOAP 포트명으로 할 것. 
 * pmilog.username=wasadm <= Dmgr 에 로긴하는 사용자명.
 * pmilog.password=wasadm <= Dmgr 에 로긴하는 패스워드. 
 * 
 * 특히, GC 관련  PMI 데이터는 PMI 항목 뿐만 아니라 WAS의 JVMPI Profiler 를 Enable 시켜야 함. Enable 방법은 아래 URL 을 참고 할 것.  
 * http://publib.boulder.ibm.com/infocenter/wasinfo/v6r1/topic/com.ibm.websphere.wsfep.multiplatform.doc/info/ae/ae/tprf_jvmpidata.html
 * 
 * 
 * 
 * @author kr050104
 *
 */
public class SamplePmiUtil {
	private AdminClient ac = null ;
	private ObjectName perfMBean = null ;
	private ObjectName jvmMBean = null ;

	public SamplePmiUtil(String host, int port, String username, String password)
	throws MalformedObjectNameException
	, ConnectorException
	{
		initAdminClient(host, port, username, password);
		initMBeans();
	}

	public String getAllLoggingInfo()
	{
		StringBuffer res = new StringBuffer();
		try {
			String jdbcInfo = getJDBCInfo(new String[]{"Derby JDBC Provider"});
			String threadInfo = getThreadInfo(new String[]{"WebContainer","ORB.thread.pool"});
			String jvmInfo = getJVMInfo();
			res.append(jdbcInfo)
				.append("\n")
				.append(threadInfo)
				.append("\n")
				.append(jvmInfo)
				.append("\n");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return res.toString() ;
	}

	/**
	 * 현재 WAS 인스턴스의 JVM 으로부터 GC Time 정보를 출력함.
	 * 
	 * @return
	 * 
	 * @throws MalformedObjectNameException
	 * @throws ConnectorException
	 * @throws MBeanException
	 * @throws ReflectionException
	 * @throws InstanceNotFoundException
	 */
	public String getJVMInfo()
	throws MalformedObjectNameException
	, ConnectorException
	, MBeanException
	, ReflectionException
	, InstanceNotFoundException
	{
		StringBuffer res = new StringBuffer();
		WSStats stat = getStats(jvmMBean);
		
//		GC 관련  PMI 데이터는 WAS의 JVMPI Profiler 를 Enable 시켜야 함. Enable 방법은 아래 URL 을 참고 할 것.  
//		http://publib.boulder.ibm.com/infocenter/wasinfo/v6r1/topic/com.ibm.websphere.wsfep.multiplatform.doc/info/ae/ae/tprf_jvmpidata.html
		WSStats [] subStats = stat.getSubStats();
		double gcTime = 0 ;
		if(subStats == null)
			gcTime = -1 ;
		for(int j = 0 ; j < subStats.length ; j++)
		{
			if(subStats[j].getName().equals("GC"))
			{
				WSTimeStatistic gcTimeStat = (WSTimeStatistic)subStats[j].getStatistic(WSJVMStats.GCTime);
	
				if(gcTimeStat==null)
					gcTime = -1 ;
				else
					gcTime = gcTimeStat.getMean();
				break ;
			}
		}
		res.append("AvgGCDuration=" + gcTime + " ms");
		return res.toString() ;
	}
	
	/**
	 * 스레드 풀 이름을 주면 그 속에 포함된 모든 DataSource 에 대해 hung thread 갯수를 출력함.
	 * 
	 * 스레드 풀 이름은 Admin console 에서 "Application Server > [서버명] > 스레드 풀" 에 나옴.
	 * 
	 * @param threadPoolNames 데이터를 얻고자 하는 스레드 풀의 이름들을 나열하세요.
	 * @return
	 * @throws MBeanException
	 * @throws ReflectionException
	 * @throws InstanceNotFoundException
	 * @throws ConnectorException
	 */
	public String getThreadInfo(String [] threadPoolNames)
	throws MBeanException
	, ReflectionException
	, InstanceNotFoundException
	, ConnectorException
	{
		StringBuffer res = new StringBuffer();
		StatDescriptor [] statDescriptors = new StatDescriptor[threadPoolNames.length];
		for(int i = 0 ; i < threadPoolNames.length ; i++)
		{
			statDescriptors[i] = new StatDescriptor (new String[] {WSThreadPoolStats.NAME,threadPoolNames[i]});
		}
		
		WSStats [] stats = getStatsArray(statDescriptors);

		for(int i = 0 ; i < stats.length ; i++)
		{
//			System.out.println(stats[i]);
			WSRangeStatistic hungThreads = (WSRangeStatistic)stats[i].getStatistic(WSThreadPoolStats.ConcurrentHungThreadCount);
			long hungThreadCount = 0L ;
			if(hungThreads==null)
			{
				//아직 컴포넌트가 한 번도 호출되지 않아 데이터가 초기화 되지 않았거나, PMI 에 해당 데이터가 Enable 되어 있지 않음. 
				hungThreadCount = -1 ;
			}
			else
			{
				hungThreadCount = hungThreads.getCurrent();
			}
			res.append("ThreadPoolName=" + stats[i].getName() + ",hungThreadCount=" + hungThreadCount + "\n");
		}
		
		return res.toString() ;
	}
	
	/**
	 * JDBC Provider name 을 주면 그 속에 포함된 모든 DataSource 에 대해 Used Connection 갯수를 출력함.
	 * 
	 * JDBC Provider name 은 Admin console 에서 "자원 > JDBC > JDBC 프로바이더 " 에 나옴.
	 * 
	 * @param jdbcProviderNames 데이터를 얻고자 하는 JDBC Provider name 들을 나열하세요.
	 * 
	 * @return
	 * @throws MBeanException
	 * @throws ReflectionException
	 * @throws InstanceNotFoundException
	 * @throws ConnectorException
	 */
	public String getJDBCInfo(String [] jdbcProviderNames)
	throws MBeanException
	, ReflectionException
	, InstanceNotFoundException
	, ConnectorException
	{
		StringBuffer res = new StringBuffer();
		StatDescriptor [] statDescriptors = new StatDescriptor[jdbcProviderNames.length];
		for(int i = 0 ; i < jdbcProviderNames.length ; i++)
		{
			statDescriptors[i] = new StatDescriptor (new String[] {WSJDBCConnectionPoolStats.NAME,jdbcProviderNames[i]});
		}
		
		WSStats [] stats = getStatsArray(statDescriptors);
		for(int i = 0 ; i < stats.length ; i++)
		{
			WSStats [] subStats = stats[i].getSubStats();

			if(subStats == null)
				continue ;
			for(int j = 0 ; j < subStats.length ; j++)
			{
//				System.out.println(subStats[j]);
				WSRangeStatistic poolSizeStatistic = (WSRangeStatistic)subStats[j].getStatistic(WSJDBCConnectionPoolStats.PoolSize);
				long poolSize = poolSizeStatistic.getCurrent();
				WSRangeStatistic freePoolSizeStatistic = (WSRangeStatistic)subStats[j].getStatistic(WSJDBCConnectionPoolStats.FreePoolSize);
				long freePoolSize = freePoolSizeStatistic.getCurrent();
				long usedPoolCount = poolSize - freePoolSize ;
				res.append("DataSourceName=" + subStats[i].getName() + ",usedPoolCount=" + usedPoolCount);
			}
		}
		
		return res.toString() ;
	}
	
	public synchronized void initMBeans()
	throws MalformedObjectNameException
	, ConnectorException
	{
		if(perfMBean == null)
			perfMBean = getMBean("WebSphere:*,type=Perf");
		
		if(jvmMBean==null)
			jvmMBean = getMBean("WebSphere:*,name=JVM");
	}
	
	public AdminClient initAdminClient(String host, int port, String username, String password)
	throws ConnectorException
	{
		if(ac == null)
		{
	        java.util.Properties props = new java.util.Properties();
	        props.put(AdminClient.CONNECTOR_TYPE, "SOAP");
	        //그냥 localhost 로 놔 둘 것.
	        props.put(AdminClient.CONNECTOR_HOST, host);
	        //WAS 인스턴스의 SOAP 포트
	        props.put(AdminClient.CONNECTOR_PORT, port);
	        //로긴 유저명
	        if(username!=null)
	        	props.put(AdminClient.USERNAME, "wasadm");
	        //로긴 패스워드
	        if(password != null)
	        	props.put(AdminClient.PASSWORD, "wasadm");

            ac = AdminClientFactory.createAdminClient(props);
		}
		return ac ;
	}
	
	public ObjectName getMBean(String queryStr)
	throws ConnectorException
	, MalformedObjectNameException
	{
		Set oSet = ac.queryNames(new ObjectName(queryStr), null);
		Iterator it= oSet.iterator();
		if(it.hasNext())
			return (ObjectName)it.next();
		return null ;
	}
	
	public WSStats getStats(ObjectName targetMBean)
	throws MBeanException
	, ReflectionException
	, InstanceNotFoundException
	, ConnectorException
	{
        String [] signature = new String[] {"javax.management.ObjectName","java.lang.Boolean"};
        Object [] params = new Object[] {targetMBean, new Boolean(true)};            
        WSStats stats = (WSStats) ac.invoke(perfMBean, "getStatsObject", params, signature);
        return stats ;
	}
	
	public WSStats [] getStatsArray(StatDescriptor [] statDescriptors)
	throws MBeanException
	, ReflectionException
	, InstanceNotFoundException
	, ConnectorException
	{
		String [] signature = new String[]{"[Lcom.ibm.websphere.pmi.stat.StatDescriptor;","java.lang.Boolean"};
		Object [] params = new Object[] {statDescriptors, new Boolean(true)};

		WSStats [] wsStats = (com.ibm.websphere.pmi.stat.WSStats[])
		ac.invoke(perfMBean, "getStatsArray", params, signature);   
		return wsStats;	
	}
}
