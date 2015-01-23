package com.ibm.juwlee;

import java.io.IOException;
import java.io.PrintWriter;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class DB2CallTest
 */
public class DB2CallTest2 extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	@EJB(name="ejb/DB2CallTest", beanInterface=DB2CallTestRemote.class)
	private DB2CallTestRemote ejb3DB2Call;	
	
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DB2CallTest2() {
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
		ejb3DB2Call.insertDB();
		out.println("DB2 Insert Call Complete.");					
		
	}

}
