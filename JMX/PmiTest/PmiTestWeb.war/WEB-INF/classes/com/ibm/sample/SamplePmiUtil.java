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
 * ������ �����ϱ⿡ �ռ� �ؾ� �� �͵�. 
 * 1. PMI �� enable ��ų ��!!! ����, PMI �� �� �ش� �׸�鵵 Enable ���Ѿ� ��. 
 * (PMI �׸���� ��� Enable ��Ű�� ����, "PMI > [������] > ����� ����" �� ���� ���ǿ� ���յǴ� ���� �׸�鸸
 * Enable ��ų ��!!!!
 * 
 * 2. "Application Server > [������] > ���μ��� ���� > JVM(Java Virtual Machine) > ����� ���� Ư��"
 * ���� ���� ���� �׸�� �߰�.
 * 
 * pmilog.host=localhost <= �׳� localhost �� �� ��. 
 * pmilog.port=8882 <= �� ���� Dmgr �̳� nodeagent �� �ƴ϶�, ���� WAS ������ SOAP ��Ʈ������ �� ��. 
 * pmilog.username=wasadm <= Dmgr �� �α��ϴ� ����ڸ�.
 * pmilog.password=wasadm <= Dmgr �� �α��ϴ� �н�����. 
 * 
 * Ư��, GC ����  PMI �����ʹ� PMI �׸� �Ӹ� �ƴ϶� WAS�� JVMPI Profiler �� Enable ���Ѿ� ��. Enable ����� �Ʒ� URL �� ���� �� ��.  
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
	 * ���� WAS �ν��Ͻ��� JVM ���κ��� GC Time ������ �����.
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
		
//		GC ����  PMI �����ʹ� WAS�� JVMPI Profiler �� Enable ���Ѿ� ��. Enable ����� �Ʒ� URL �� ���� �� ��.  
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
	 * ������ Ǯ �̸��� �ָ� �� �ӿ� ���Ե� ��� DataSource �� ���� hung thread ������ �����.
	 * 
	 * ������ Ǯ �̸��� Admin console ���� "Application Server > [������] > ������ Ǯ" �� ����.
	 * 
	 * @param threadPoolNames �����͸� ����� �ϴ� ������ Ǯ�� �̸����� �����ϼ���.
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
				//���� ������Ʈ�� �� ���� ȣ����� �ʾ� �����Ͱ� �ʱ�ȭ ���� �ʾҰų�, PMI �� �ش� �����Ͱ� Enable �Ǿ� ���� ����. 
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
	 * JDBC Provider name �� �ָ� �� �ӿ� ���Ե� ��� DataSource �� ���� Used Connection ������ �����.
	 * 
	 * JDBC Provider name �� Admin console ���� "�ڿ� > JDBC > JDBC ���ι��̴� " �� ����.
	 * 
	 * @param jdbcProviderNames �����͸� ����� �ϴ� JDBC Provider name ���� �����ϼ���.
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
	        //�׳� localhost �� �� �� ��.
	        props.put(AdminClient.CONNECTOR_HOST, host);
	        //WAS �ν��Ͻ��� SOAP ��Ʈ
	        props.put(AdminClient.CONNECTOR_PORT, port);
	        //�α� ������
	        if(username!=null)
	        	props.put(AdminClient.USERNAME, "wasadm");
	        //�α� �н�����
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
