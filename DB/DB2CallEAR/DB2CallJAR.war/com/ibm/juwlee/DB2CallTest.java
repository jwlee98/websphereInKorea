package com.ibm.juwlee;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import javax.annotation.Resource;
import javax.ejb.Stateless;

/**
 * Session Bean implementation class DB2CallTest
 */
@Stateless
public class DB2CallTest implements DB2CallTestRemote {
	
	@Resource(name="DS_DB2_DS", type=javax.sql.DataSource.class, shareable=false)
	private javax.sql.DataSource dsDB2;
	
    /**
     * Default constructor. 
     */
    public DB2CallTest() {
        // TODO Auto-generated constructor stub
    }
    
    public void insertDB() {
       	String insert_sql = "insert into test(ID) values('haha4')";    	
    	Statement stmt = null;
    	Connection conn = null;
    	
    	try {
	   		conn = dsDB2.getConnection();
	   	    //conn.setAutoCommit(false);
    		stmt = conn.createStatement();
    		stmt.executeUpdate(insert_sql);  
    		stmt.close();
    		conn.close();
			
		} catch (SQLException e) {
			// TODO 자동 생성된 catch 블록
			e.printStackTrace();
		} catch (Exception e) {
			// TODO 자동 생성된 catch 블록
			e.printStackTrace();
		}finally {
    		try {
    			if (stmt != null)
				    stmt.close();
			} catch (SQLException e) {
				// TODO 자동 생성된 catch 블록
				e.printStackTrace();
			}
    		try {
    			if (conn != null)    			
				      conn.close();
			} catch (SQLException e) {
				// TODO 자동 생성된 catch 블록
				e.printStackTrace();
			}
		}    	
    }

}
