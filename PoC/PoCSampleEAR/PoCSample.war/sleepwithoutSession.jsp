<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">

<HTML>
<HEAD>
<%@ page language="java" contentType="text/html; charset=EUC-KR" session="false" import="com.ibm.juwlee.poc.*"%>
<META http-equiv="Content-Type" content="text/html; charset=EUC-KR">
<META name="GENERATOR" content="IBM Software Development Platform">
<META http-equiv="Content-Style-Type" content="text/css">

<TITLE>sleep.jsp</TITLE>
</HEAD>
<BODY><P>This program is for Excessive Response Time Test<BR>
<BR>------------ How to use ----------------<BR>sleep.jsp?sleep=1000 (all &quot;sleep.jsp&quot; requests to this WAS instance will sleep for 1000 msec from now on)<BR>
Default sleep time = 500 ms<BR>
-------------------------------------------<BR>

<%
	String sleepTimeParam = request.getParameter("sleep") ;
	if(sleepTimeParam != null)
	{
		long sleepTime = Long.parseLong(sleepTimeParam) ;
		SleepTimeInfo.getInstance().setSleepTime(sleepTime) ;
		out.println("<BR><BR>all &quot;sleep.jsp&quot; requests to this WAS instance will sleep for" + sleepTimeParam +  " msec from now on") ;
		return ;
	}
	else
	{
		long sleepTime = SleepTimeInfo.getInstance().getSleepTime() ;
		Thread.sleep(sleepTime) ;
		out.println("<BR><BR>This page sleeped for " + sleepTime + " ms <BR>") ;
	}
%>

<BR></P>
</BODY>
</HTML>
