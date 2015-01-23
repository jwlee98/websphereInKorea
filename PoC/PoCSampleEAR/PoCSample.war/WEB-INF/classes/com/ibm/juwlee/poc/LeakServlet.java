package com.ibm.juwlee.poc;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ibm.juwlee.servlet.MyCounter;
import com.ibm.juwlee.servlet.MyThreadLocal;

/**
 * Servlet implementation class LeakServlet
 */
public class LeakServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static MyThreadLocal myThreadLocal = new MyThreadLocal();
	public static final int LENGTH = 1048576;
	private static final byte[] BUFFER = new byte[1048576];
	private static MyCounter[] COUNTER = new MyCounter[1048576];       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LeakServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    PrintWriter out = response.getWriter();

	    MyCounter counter = (MyCounter)myThreadLocal.get();

	    if (counter == null) {
	      counter = new MyCounter();
	      myThreadLocal.set(counter);
	    }

	    Random r = new Random(100L);
	    r.nextBytes(BUFFER);

	    for (int i = 0; i < 1048576; ++i) {
	      COUNTER[i] = new MyCounter(true);
	    }

	    long heapSize = Runtime.getRuntime().totalMemory();
	    long maxHeapSize = Runtime.getRuntime().maxMemory();

	    out.println("Heap Size (KB): " + (heapSize / 1024L));
	    out.println("Max Heap Size (KB): " + (maxHeapSize / 1024L));

	    response.getWriter().println(Thread.currentThread().getName() + " served this servlet " + counter.getCount() + " times");
	    counter.increment();

	    System.out.println("Heap Size (KB): " + (heapSize / 1024L));
	    System.out.println("Max Heap Size (KB): " + (maxHeapSize / 1024L));	
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
