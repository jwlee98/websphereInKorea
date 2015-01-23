<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@page import="java.util.Date"%>
<html>
<head>
<%
	if (request.getParameter("DATA")!= null){
		session.setAttribute("DATA",request.getParameter("DATA"));
	}
 %>
<title>Session Tester</title>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<meta name="GENERATOR" content="Rational Software Architect">
</head>
<body><p>Your session details:</p>
<table border="1">
	<tbody>
		<tr>
			<td>Session Id:</td>
			<td><%=session.getId() %></td>
		</tr>
		<tr>
			<td>Session creation date:</td>
			<td><%=new Date(session.getCreationTime())%></td>
		</tr>
		<tr>
			<td>Last time session accessed</td>
			<td><%=new Date(session.getLastAccessedTime())%></td>
		</tr>
		<tr>
			<td>Server Name</td>
			<td><%= com.ibm.websphere.runtime.ServerName.getFullName()  %></td>
		</tr>		
		<tr>
			<td>Any data</td>
			<td><%= (session.getAttribute("DATA")!=null)?session.getAttribute("DATA"):"&nbsp;" %></td>
		</tr>
	</tbody>
</table>
<form action="SessionTest.jsp">Enter some data to store in the session:<input type="text" name="DATA" size="20"><br>
<input type="submit" name="Submit" value="Submit">
</form>
</body>
</html>
