package com.ibm.juwlee.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.ws.WebServiceRef;

import com.ibm.juwlee.jaxws.client.TestVO;
import com.ibm.juwlee.jaxws.client.VarargsJAXWSCall;
import com.ibm.juwlee.jaxws.client.VarargsJAXWSCallService;


@WebServlet("/VarargsCall")
public class VarargsCall extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	 @WebServiceRef(wsdlLocation="http://localhost:9080/VarargsJAXWSSample/VarargsJAXWSCallService?wsdl")
	 private static VarargsJAXWSCallService service;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public VarargsCall() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doWork(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doWork(request, response);
	}
	
	protected void doWork(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
	    PrintWriter out = response.getWriter();  
	    
	    out.println("<body>");
	    out.println("<html>");
	    out.println("<head><title>JAX-WS Test</title></head><body>");
	    out.println("Test JAX-WS");
	    
	    VarargsJAXWSCall proxy = service.getVarargsJAXWSCallPort();
	    
	    int a = 1;
	    int b = 2;
    
	    List<Object> list = new ArrayList<Object>();
	    list.add(a);
	    list.add(b);    

	    Object returnObj = proxy.callIt(list);
	    System.out.println("rerutn = " +  returnObj);
	    
	    TestVO tv = new TestVO();
	    list.add(tv);
	    
	    Object returnObj2 = proxy.callIt(list);
	    System.out.println("rerutn = " +  returnObj2);

	}

}
