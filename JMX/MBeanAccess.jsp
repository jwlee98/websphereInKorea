<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">


<%@ page language="java" contentType="text/html; charset=EUC-KR"
	pageEncoding="EUC-KR" 
	import="java.util.*,com.ibm.websphere.management.*, javax.management.*,com.ibm.websphere.management.mbean.*,javax.security.auth.login.*,java.sql.*"%>

<HTML>
<HEAD>
<META http-equiv="Content-Type" content="text/html; charset=EUC-KR">
<META name="GENERATOR" content="IBM Software Development Platform">
<META http-equiv="Content-Style-Type" content="text/css">
<link rel="stylesheet" type="text/css" href="./base.css"/>
<TITLE>JMS Queue Monitering</TITLE>
<script>
	function onload(){
		window.setTimeout("reload();",60000);
	}
	
	function reload() {
	     //alert('load');
	     location.reload();
	}

	
</script>
</HEAD>
<BODY onload="onload()" style="padding:5 0 0 15"><BR>
<%!
	public static TreeMap qInfo;
%>
<%
qInfo = new TreeMap();

long startTime = System.currentTimeMillis();    
String currentTm = (new Timestamp(startTime)).toString();


	
javax.security.auth.login.LoginContext lc = null;

try {
	lc = new javax.security.auth.login.LoginContext("WSLogin",
	new com.ibm.websphere.security.auth.callback.WSCallbackHandlerImpl("op", "securityrealm", "op014"));
	
	// create a LoginContext and specify a CallbackHandler implementation
	// CallbackHandler implementation determine how authentication data is collected
	// in this case, the authentication data is "push" to the authentication mechanism
	//   implemented by the LoginModule.
} catch (javax.security.auth.login.LoginException e) {
	System.err.println("ERROR: failed to instantiate a LoginContext and the exception: " + e.getMessage());
	e.printStackTrace();
	
	// may be javax.security.auth.AuthPermission "createLoginContext" is not granted
	//   to the application, or the JAAS login configuration is not defined.
}

if (lc != null)
try {
	lc.login();  // perform login
	javax.security.auth.Subject s = lc.getSubject();
	// get the authenticated subject
	
	// Invoke a J2EE resources using the authenticated subject
	com.ibm.websphere.security.auth.WSSubject.doAs(s,
		new java.security.PrivilegedAction() {
			
			
			
			public Object run() {
				try {
				
					//com.ibm.websphere.management.AdminService adminService = com.ibm.websphere.management.AdminServiceFactory.getAdminService();
					//String jvmName = adminService.getProcessName();
					//javax.management.MBeanServer  mbeanServer = com.ibm.websphere.management.AdminServiceFactory.getMBeanFactory().getMBeanServer();
					//com.ibm.websphere.management.MBeanFactory mbeanFactory = com.ibm.websphere.management.AdminServiceFactory.getMBeanFactory();
					//System.out.println("mbeanFactory : " + mbeanFactory);
					
					
					com.ibm.websphere.management.AdminService adminService = com.ibm.websphere.management.AdminServiceFactory.getAdminService();
					String cellName = adminService.getCellName();
					//System.out.println("CellName : " + cellName);
					com.ibm.websphere.management.AdminClient adminClient = adminService.getDeploymentManagerAdminClient();
					
					//javax.management.ObjectName objectName  = new ObjectName("WebSphere:type=Server,cell=localhostCell01,*");
					javax.management.ObjectName objectName  = new ObjectName("WebSphere:type=SIBQueuePoint,*");
					Iterator jmsSet = adminClient.queryNames(objectName, null).iterator();
					//System.out.println("adminService : " + adminService);
					
					
					while(jmsSet.hasNext())
					{
					   objectName = (ObjectName)jmsSet.next();
					   String obName = objectName.toString();
					   
					   if(obName.charAt(15) != '_'){
						   //System.out.print("Object : " + obName.substring(15,obName.indexOf(",")));
						   //System.out.println("     Queue depth: " + adminClient.getAttribute(objectName, "depth"));
						   
						   String qNm =  obName.substring(15,obName.indexOf(",")); 
						   String dep = adminClient.getAttribute(objectName, "depth").toString();
						   //System.out.println("qNm : " + qNm);
						   qInfo.put(qNm, dep);
					   }
					   //out.println("conn : " + mbeanServer.invoke(objectName, "getProperties", null ,null));
					}
		
	   
	
					/*
					javax.management.MBeanServer  mbeanServer = mbeanFactory.getMBeanServer();
					System.out.println("mbeanServer : " + mbeanServer);
					
					javax.management.ObjectName objectName  = new ObjectName("WebSphere:*,type=Server,node=localhostCellManager01");
					
					Iterator jmsSet = mbeanServer.queryNames(objectName, null).iterator();
					while(jmsSet.hasNext())
					{
					   objectName = (ObjectName)jmsSet.next();
					   System.out.println("Object : " + objectName);
					   //System.out.println("aaa" + mbeanServer.getAttribute(objectName, "depth"));
					   //out.println("conn : " + mbeanServer.invoke(objectName, "getProperties", null ,null));
					   }
					
					
					//out.println("Queue : " + mbeanServer.invoke(objectName, "browse", params ,signature));
					
					*/
				} catch (Exception e) {
					System.out.println("ERROR: error while accessing EJB resource, exception: " + e.getMessage());
					e.printStackTrace();
				}
				return null;
			}
		}
	);
} catch (javax.security.auth.login.LoginException e) {
	System.err.println("ERROR: login failed with exception: " + e.getMessage());
	e.printStackTrace();

	// login failed, might want to provide relogin logic
}
out.println("<span><b>"+currentTm+"</b> 현재 큐 상황</span><br><br>");
%>
<table class="search" border="1">
  <tr align="center">
    <td><span><b>Queue Name</b></span></td>
    <td><span><b>Current Message Depth</b></span></td>
  </tr>

<% 
	Vector keys = new Vector(qInfo.keySet());
	for(int i=0;i<keys.size();i++){
 		if(i%2==1) out.print("<tr bgcolor='#E0EEE0'>");
 		else out.print("<tr bgcolor='#F0FFF0'>");
 		String qName = keys.get(i).toString();
		out.println("<td align='center'><span>"+qName+"</span></td>");
		out.println("<td align='center'><span>"+qInfo.get(qName)+"</span></td>");
		out.println("</tr>");
	}
	
%>
</table>
<BR></P>
</BODY>
</HTML>
