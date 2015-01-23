package com.ibm.developerworks.wsat;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Enumeration;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import org.example.wsatwebservice1.WsatWebService1SOAPProxy;

/**
 * Servlet implementation class WsatWebServiceClientTestServlet
 */
public class WsatWebServiceClientTestServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public WsatWebServiceClientTestServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		dispatch(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		dispatch(request, response);
	}

	protected void dispatch(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		System.out.println("begin dispatch");
		Connection wsatDbConnection = null;
		Statement statement = null;
		Statement statement2 = null;
		try {
			System.out.println("begin try-catch");
			Context ctx = new InitialContext();
			UserTransaction userTransaction = (UserTransaction) ctx.lookup("java:comp/UserTransaction");
			DataSource wsatDatabase = (DataSource) ctx.lookup("jdbc/wsatDataSource1");

			userTransaction.begin();
			System.out.println("Transaction status = "+ userTransaction.getStatus());

			System.out.println("before database call");
			wsatDbConnection = wsatDatabase.getConnection();
			statement = wsatDbConnection.createStatement();
			
			System.out.println("******************************************");
			for (Enumeration e = request.getParameterNames(); e.hasMoreElements(); ) {
				String key = (String)e.nextElement();
				System.out.println("key=" + key  + "  value= " + request.getParameter(key));
			}
			System.out.println("******************************************");

			String primaryKey = "" + (System.currentTimeMillis() % 10000000000l);
			System.out.println("primary key is " + primaryKey);
			statement.execute("insert into wsatTable1 values ('" + primaryKey + "','value" + primaryKey +"')");
			System.out.println("after database call, before webservice call");
			
			if("rollback".equalsIgnoreCase(request.getParameter("transaction01"))) {
				throw new RuntimeException("ROLLBACK ME");
			} 
	
			WsatWebService1SOAPProxy proxy = new WsatWebService1SOAPProxy();
			proxy._getDescriptor().setEndpoint("http://localhost:9089/WsatWebService1/WsatWebService1");
			String requestInput = "commit";
			
			if(request.getParameter("transaction02") != null) {
				requestInput = request.getParameter("transaction02");
			}			

			proxy.op1(requestInput);
			System.out.println("after webservice call");
			
			if("rollback".equalsIgnoreCase(request.getParameter("transactionEnd"))) {
				System.out.println("after webservice call: rollback");
				userTransaction.rollback();
			} else {
				System.out.println("after webservice call: commit");
				userTransaction.commit();
			}
			
			System.out.println("end try-catch");
		} catch (NamingException e) {
			e.printStackTrace();
		} catch (NotSupportedException e) {
			e.printStackTrace();
		} catch (SystemException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (HeuristicMixedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (HeuristicRollbackException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RollbackException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if(statement != null) {
				try {
					statement.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

			if(statement2 != null) {
				try {
					statement2.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

			if(wsatDbConnection != null) {
				try {
					wsatDbConnection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		
		System.out.println("end dispatch");

	}
}
