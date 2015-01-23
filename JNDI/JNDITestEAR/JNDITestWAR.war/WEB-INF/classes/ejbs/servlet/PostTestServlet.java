package ejbs.servlet;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class PostTestServlet
 */
public class PostTestServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public PostTestServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		Enumeration enu = request.getHeaderNames();
	   	//Enumeration enu2 = request.getParameterNames();
	   	byte[] readbyte = new byte[1024];
	   	String str = null;
   		System.out.println("Step1");
	   	BufferedInputStream bis = null;
	   	int cnt = 0;
	   	try {
	   		bis = new BufferedInputStream(request.getInputStream());
	   		System.out.println("Step2");
	   			while ((cnt = bis.read(readbyte)) != -1){
	   				String temp = new String(readbyte,0,cnt);
	   				str += temp;
	   			}
		   	System.out.println("Step3" + str);	   			
	   			} catch (Exception e) {
	   				e.printStackTrace();
	   			}
		
		
	}

}
