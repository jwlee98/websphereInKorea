<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import="java.io.*,java.util.*,javax.naming.*"%>

<html>
<head>
</head>
<body>
hahahaha
<%
try {
            out.println("haha");
            System.out.print("test");
            Hashtable table = new Hashtable();
            table.put("java.naming.factory.initial", "com.ibm.websphere.naming.WsnInitialContextFactory");
            table.put("java.naming.provider.url", "iiop://was-i1:9809");
            Context ctx = new InitialContext(table);
            //Context ctx = new InitialContext();
            Object o = ctx.lookup( "ejblocal:ejb/FramePlus/ServiceEJBLocalCMT");
            System.out.println("test1 " + o);
}catch(Exception e){
        e.printStackTrace();
}
%>
