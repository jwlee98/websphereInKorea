package org.example.wsatwebservice1;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;



@javax.jws.WebService (endpointInterface="org.example.wsatwebservice1.WsatWebService1", targetNamespace="http://www.example.org/WsatWebService1/", serviceName="WsatWebService1", portName="WsatWebService1SOAP")
public class WsatWebService1SOAPImpl{

    public String op1(String in) {
    	
		System.out.println("begin op1");
		Connection wsatDbConnection = null;
		Statement statement = null; 
		try {
			System.out.println("begin try-catch");
			Context ctx = new InitialContext();
			DataSource wsatDatabase = (DataSource) ctx.lookup("jdbc/wsatDataSource2");

			UserTransaction userTransaction = (UserTransaction) ctx.lookup("java:comp/UserTransaction");
			System.out.println("Transaction status = "+ userTransaction.getStatus());

			System.out.println("before database call");
			wsatDbConnection = wsatDatabase.getConnection();
			statement = wsatDbConnection.createStatement();
			
			String primaryKey = "" + (System.currentTimeMillis() % 10000000000l);
			System.out.println("primary key is " + primaryKey);
			statement.execute("insert into wsatTable2 values ('" + primaryKey + "','value" + primaryKey +"')");
			System.out.println("after database call");
				
			if("rollback".equalsIgnoreCase(in)) {
				throw new RuntimeException("ROLLBACK ME");
			} 
			
			System.out.println("end try-catch");
		} catch (NamingException e) {
			System.out.println("xxx: " + e.getMessage());
			e.printStackTrace();
		} catch (SQLException e) {
			System.out.println("yyy: " + e.getMessage());
			e.printStackTrace();
		} catch (SystemException e) {
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
			
			if(wsatDbConnection != null) {
				try {
					wsatDbConnection.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		
		if ("exit".equalsIgnoreCase(in)) {
			System.exit(1);
		}
		
		System.out.println("end op1");
        return in + " echo";
    }

}