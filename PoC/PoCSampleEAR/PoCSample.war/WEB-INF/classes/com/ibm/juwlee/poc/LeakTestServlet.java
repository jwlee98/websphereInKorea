package com.ibm.juwlee.poc;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import example.ExampleFactory;
import example.IExample;

/**
 * Servlet implementation class LeakTestServlet
 */
public class LeakTestServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private static IExample example1;
    private static IExample example2;			
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LeakTestServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	       boolean b_count = true;
	        int count = 0;
			example1 = ExampleFactory.newInstance();
		    
		    while (b_count) {
		        example2 = ExampleFactory.newInstance().copy(example2);
	        
		        count++;
		    
		        if (count > 10000)
		        	b_count = false;
		        
		        Thread.currentThread();
//				try {
//					Thread.sleep(10);
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
		      }
	        System.out.println("1) " +example1.message() + " = " + example1.plusPlus());

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);	
	}

}
