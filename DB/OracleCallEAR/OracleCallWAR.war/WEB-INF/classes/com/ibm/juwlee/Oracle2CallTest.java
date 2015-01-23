package com.ibm.juwlee;

import java.io.IOException;
import java.io.PrintWriter;

import javax.ejb.EJB;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.rmi.PortableRemoteObject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class OracleCallTest
 */
public class Oracle2CallTest extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	@EJB(name="ejb/Oracle2CallTest", beanInterface=OracleCallTestRemote.class)
	private OracleCallTestRemote ejb3Oracle2Call;		
	
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Oracle2CallTest() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub	
		PrintWriter out = response.getWriter();
		ejb3Oracle2Call.insertDB();
		out.println("Oracle Insert Call Complete.");		
	}

}
