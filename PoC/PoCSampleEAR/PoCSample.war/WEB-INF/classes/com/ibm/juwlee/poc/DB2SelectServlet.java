package com.ibm.juwlee.poc;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

/**
 * Servlet implementation class DB2SelectServlet
 */
public class DB2SelectServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private DataSource db2DS = null;
       
	//@Resource(name="DB2Datasource", type=javax.sql.DataSource.class)
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DB2SelectServlet() {
        super();
        
//        Hashtable table = new Hashtable();
//        table.put("java.naming.factory.initial", "com.ibm.websphere.naming.WsnInitialContextFactory");
//        table.put("java.naming.provider.url", "iiop://localhost:9809");
        Context ctx;
		try {
			ctx = new InitialContext();
			db2DS = (DataSource)ctx.lookup("jdbc/DB2Datasource");
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = response.getWriter();
//		String select_sql = "select * from TEST";
		String select_sql = "select FIRSTNME from Employee where empno=?";
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			conn = db2DS.getConnection();
			pstmt = conn.prepareStatement(select_sql);
			pstmt.setString(1, "000010");
			rs = pstmt.executeQuery();
			
//			while(rs.next())
//			{
//				int Id = rs.getInt("ID");
//				String Name = rs.getString("NAME");
//				out.println("ID : " + Id + " NAME : " + Name + "<br>");
//			}
			rs.next();
			out.println("First Name = " + rs.getString(1));
			
		} catch (SQLException e){
			e.printStackTrace(out);
		} finally {
			try {
				if (pstmt != null)	
					pstmt.close();				
			} catch (SQLException e) {
				// TODO 자동 생성된 catch 블록
				e.printStackTrace(out);
			}
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				// TODO 자동 생성된 catch 블록
				e.printStackTrace(out);
			}
		}			
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
