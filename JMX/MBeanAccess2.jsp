<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">

<HTML>
<HEAD>
<%@ page language="java" contentType="text/html; charset=EUC-KR"
	pageEncoding="EUC-KR" import="java.util.*,com.ibm.websphere.management.*, javax.management.*,com.ibm.websphere.management.mbean.*,javax.security.auth.login.*"%>
<META http-equiv="Content-Type" content="text/html; charset=EUC-KR">
<META name="GENERATOR" content="IBM Software Development Platform">
<META http-equiv="Content-Style-Type" content="text/css">
<LINK href="theme/Master.css"
	rel="stylesheet" type="text/css">
<TITLE>sleep.jsp</TITLE>
</HEAD>
<BODY><BR>
<%

javax.security.auth.login.LoginContext lc = null;

try {
lc = new javax.security.auth.login.LoginContext("WSLogin",
new com.ibm.websphere.security.auth.callback.WSCallbackHandlerImpl("wasadmin", "securityrealm", "q1w2e3r4"));

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

com.ibm.websphere.management.AdminService adminService = com.ibm.websphere.management.AdminServiceFactory.getAdminService();
javax.management.MBeanServer  mbeanServer = com.ibm.websphere.management.AdminServiceFactory.getMBeanFactory().getMBeanServer();

String jvmName = adminService.getProcessName();

javax.management.ObjectName objectName  = new ObjectName("WebSphere:*,type=JVM,j2eeType=JVM");

Iterator jmsSet = mbeanServer.queryNames(objectName, null).iterator();
while(jmsSet.hasNext())
{
   objectName = (ObjectName)jmsSet.next();
   System.out.println("aaa" + mbeanServer.getAttribute(objectName, "heapSize") +"<p>");
   System.out.println("aaa" + mbeanServer.getAttribute(objectName, "javaVersion") +"<p>");   
   //out.println("conn : " + mbeanServer.invoke(objectName, "getProperties", null ,null));
   }


//out.println("Queue : " + mbeanServer.invoke(objectName, "browse", params ,signature));


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
%>

<BR></P>
</BODY>
</HTML>
