<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="javax.jms.MessageProducer,java.util.*"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=EUC-KR">
<title>Insert title here</title>
</head>
<body>
<%
	
	String queryDay = null;
	javax.jms.ConnectionFactory cf= null;
	javax.jms.Connection conn = null;
	
	javax.jms.Destination dest = null;
	javax.jms.Session sess =null;
	
	int day,month = 1;
	if (request.getParameter("day")!=null)
		day = Integer.parseInt(request.getParameter("day"));
	else
		day = 1;
	if (request.getParameter("month")!=null)
		month = Integer.parseInt(request.getParameter("month"));
	else
		month = 1;
	
	int maxCount = 10;
	int count = 0;
	if (request.getParameter("count")!= null)
		maxCount = Integer.parseInt(request.getParameter("count"));
	try {
		Hashtable env = new Hashtable();
		env.put(javax.naming.Context.INITIAL_CONTEXT_FACTORY, "com.ibm.websphere.naming.WsnInitialContextFactory");
		env.put(javax.naming.Context.PROVIDER_URL, "iiop://tec120:9809");

		javax.naming.Context ctx = null;
	
		ctx = new javax.naming.InitialContext(env);
		cf= (javax.jms.ConnectionFactory)ctx.lookup("jms/qcf1");
		conn = cf.createConnection();
		conn.start();
		dest = (javax.jms.Destination) ctx.lookup("jms/q1");
		sess = conn.createSession(false,javax.jms.Session.AUTO_ACKNOWLEDGE);
		
		String selectCount = "";
	
		long startTime = new java.util.Date().getTime();
		out.println("<br>max count: "+maxCount+"<br>");
		
		while(true){
			if (count++>maxCount)break;
			//String impDclNo = rs.getString(1);
			//S/tring orgNatnCd = rs.getString(2);
			//String hsGodsCls10Cd = rs.getString(3);
			
			StringBuffer sb = new StringBuffer();
			sb.append("<connector xmlns=\"http://wbe.ibm.com/6.2/Event/ImportEvent\" name=\"Import\" version=\"6.2\">");
			sb.append("<connector-bundle name=\"ImportEvent\" type=\"Event\">");
			
			
			
			sb.append("  <ImportEvent>");
			sb.append("   <IMP_DCL_NO type=\"String\"></IMP_DCL_NO>");
			sb.append("   <ORG_NATN_CD type=\"String\"></ORG_NATN_CD>");
			sb.append("   <HS_GODS_CLS10_CD type=\"String\"></HS_GODS_CLS10_CD>");
			sb.append("<HS_CD type=\"String\"></HS_CD>");
			sb.append("   </ImportEvent>");
			sb.append("</connector-bundle>");
			sb.append("<system>admin-dxzwzp9rl</system>");
			sb.append("<timestamp>2009-08-11T08:37:24Z</timestamp>");
			sb.append("<loginfo>Test values from WebSphere Business Events:Design Data</loginfo>");
			sb.append("</connector>");
			
			//javax.naming.Context ctx =null;
		
				
			MessageProducer producer = sess.createProducer(dest);
			//javax.jms.TopicSubscriber sub = sess.createSubscriber(topic);
			
			javax.jms.TextMessage test = sess.createTextMessage();
			test.setText(sb.toString());
			//Thread.sleep(200);
			//out.println("Processing ...<br>");
			out.flush();
			producer.send(test);
				
		}
		long elapsedTime = new java.util.Date().getTime() -startTime;
		out.println("Done."+elapsedTime);
		System.out.println("** elapsedTime :"+elapsedTime);
	}catch(Exception e) {
		e.printStackTrace();
	}finally {
		try {

			sess.close();
			//producer.close();
			conn.close();
		}catch(Exception e ){
			e.printStackTrace();
		}
	}
	

%>


</body>
</html>
