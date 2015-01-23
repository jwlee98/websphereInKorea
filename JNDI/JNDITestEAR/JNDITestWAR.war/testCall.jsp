<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import="java.io.*,java.util.*,javax.naming.*,com.ibm.juwlee.ejbTest.*,javax.rmi.*,javax.ejb.*,com.ibm.websphere.naming.*"%>

<html>
<head>
</head>
<body>
Test JSP start
<%
        Hashtable env = new Hashtable();
        env.put(Context.INITIAL_CONTEXT_FACTORY,"com.ibm.websphere.naming.WsnInitialContextFactory");
        env.put(Context.PROVIDER_URL, "corbaloc:iiop:localhost:2815");
        
        Context ctx;
        Object o=null;
		try {
			ctx = new InitialContext(env);
			o = ctx.lookup("java:comp/env/ejb/SessionBeanCall");
		} catch (NamingException e) {
			e.printStackTrace();
		}        
		SessionBeanCallHome home=(SessionBeanCallHome)PortableRemoteObject.narrow(o,SessionBeanCallHome.class);
		SessionBeanCall sbc = null;

		try {
			System.out.println("create start");
			sbc = home.create();
		} catch (CreateException e) {
			e.printStackTrace();
		}				
        System.out.println("Number : " + sbc.getNum());
        sbc.increaseNum();   
        System.out.println("Finish");        
%>
Test JSP complete
