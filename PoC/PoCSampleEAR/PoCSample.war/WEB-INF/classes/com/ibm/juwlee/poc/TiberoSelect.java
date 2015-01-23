package com.ibm.juwlee.poc;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

/**
 * Servlet implementation class TiberoSelect
 */
public class TiberoSelect extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private DataSource tibero2DS = null;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public TiberoSelect() {
        super();      
      Context ctx;
		try {
			ctx = new InitialContext();
			tibero2DS = (DataSource)ctx.lookup("jdbc/tibero");
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = response.getWriter();
//		String select_sql = "select * from TEST";
		String select_sql = "select NAME from student where s_num=?";
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			conn = tibero2DS.getConnection();
			pstmt = conn.prepareStatement(select_sql);
			pstmt.setInt(1, 1);
			rs = pstmt.executeQuery();
//			while(rs.next())
//			{
//				int Id = rs.getInt("ID");
//				String Name = rs.getString("NAME");
//				out.println("ID : " + Id + " NAME : " + Name + "<br>");
//			}
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			rs.next();
			out.println("Name = " + rs.getString(1));
			
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
