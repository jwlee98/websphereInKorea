<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META name="GENERATOR" content="IBM WebSphere Studio">
<TITLE>sessionWLM.jsp</TITLE>
</HEAD>
<BODY>
<H2>Session WLM SERVLET</H2>
<FORM METHOD=GET ACTION="SessionWLMServlet">
<INPUT TYPE=SUBMIT VALUE="SessionWLMServlet">
</BR>
<B>Select a Method of Execution:</B>
</BR>
<%@page session="true"%>
<%
    response.setHeader("Pragma", "No-cache");
    response.setHeader("Cache-Control", "no-cache");
    response.setDateHeader("Expires",0);
    String source = (String) request.getAttribute("wlmsource");
    if (source == null) source = "SVR";
    String servername = (String) request.getAttribute("wlmservername");
    if (servername == null) servername = "";
    String sessid = (String) request.getAttribute("wlmid");
    if (sessid == null) sessid = "No Session requested";
    String count = (String) request.getAttribute("wlmcount");
    if (count == null) count = "No Count in Session";
%>
<INPUT TYPE=RADIO NAME=source VALUE=SVR
<%= source.equals("SVR") ? " CHECKED" : "" %>>Display Server Name Only</BR>
<INPUT TYPE=RADIO NAME=source VALUE=SES
<%= source.equals("SES") ? " CHECKED" : "" %>>Display Server Name & Establish Session Affinity</BR>
<INPUT TYPE=RADIO NAME=source VALUE=RESET
<%= source.equals("RESET") ? " CHECKED" : "" %>>Invalidate Session ID and Affinity</BR>
</BR>
</FORM>
<H3><%=servername%></H3>
<H3><%=count%></H3>
<H3><%=sessid%></H3>
</INSERT>
</BODY>
</HTML>
