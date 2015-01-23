package com.ibm.juwlee.jpa;

import java.io.IOException;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class SearchJPAServlet
 */
@WebServlet("/SearchJPAServlet")
public class SearchJPAServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SearchJPAServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ServletOutputStream out = response.getOutputStream();
		response.setContentType("text/html");
		out.println("<html><head><title>Hello World</title></head>");
		out.println("<body><h1>Hello World!!!</h1><br>");
		try {
			Employee e = SearchEmployee("000010");
			out.println("Hello " + e.getFirstnme());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		out.println("</html>");		
	}

	@PersistenceUnit(unitName = "JPATest")
	private EntityManagerFactory emf;
	@Resource
	private javax.transaction.UserTransaction utx;
	
	protected Employee SearchEmployee(String EmpNo) throws Exception {
		utx.begin();
		EntityManager em = emf.createEntityManager();
		
		Employee employee = (Employee) em.find(Employee.class, EmpNo);
		if (employee == null) {
			utx.commit();
			em.close();
			throw new Exception ("Employee is not found... EmpNo is " + EmpNo);
		}
		utx.commit();
		em.close();
		return employee;
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
