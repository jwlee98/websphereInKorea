package com.ibm.hanabank;

import java.io.IOException;
import java.io.PrintWriter;

import javax.management.MalformedObjectNameException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ibm.sample.SamplePmiUtil;
import com.ibm.websphere.management.exception.ConnectorException;


/**
 * Servlet implementation class for Servlet: PmiUtilTestServlet
 *
 */
 public class PmiUtilTestServlet extends javax.servlet.http.HttpServlet implements javax.servlet.Servlet {
   static final long serialVersionUID = 1L;
   private  SamplePmiUtil spu = null ;
   

    /* (non-Java-doc)
	 * @see javax.servlet.http.HttpServlet#HttpServlet()
	 */
	public PmiUtilTestServlet() {
		super();
		try {
			String host = System.getProperty("pmilog.host");
			int port = Integer.parseInt(System.getProperty("pmilog.port"));
			String username = System.getProperty("pmilog.username");
			String password = System.getProperty("pmilog.password");
			
			spu = new SamplePmiUtil(host, port, username, password);
		} catch (MalformedObjectNameException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ConnectorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}   	
	
	/* (non-Java-doc)
	 * @see javax.servlet.http.HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		test(response);
	}  	
	
	/* (non-Java-doc)
	 * @see javax.servlet.http.HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		test(response);
	}   	
	
	protected void test(HttpServletResponse res)
	{
		PrintWriter pw = null ;
		try {
			 pw = new PrintWriter(res.getOutputStream());
			pw.println("<PRE>");
			pw.println(spu.getAllLoggingInfo());
			pw.println("<PRE>");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally
		{
			if(pw!=null)	pw.close();
		}
	}
}