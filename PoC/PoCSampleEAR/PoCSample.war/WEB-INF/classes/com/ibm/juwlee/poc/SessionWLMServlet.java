package com.ibm.juwlee.poc;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.ibm.websphere.servlet.session.IBMApplicationSession;
import com.ibm.websphere.servlet.session.IBMSession;

/**
 * Servlet implementation class SessionWLMServlet
 */
public class SessionWLMServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SessionWLMServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter     out;
        String[]        p;
        String       	source  = "NO" ; 
        String          servername = null;
        String          id  = null;
        String       	newid = null ;
        String			counttxt = null ;
        int				count = 0 ;

        servername = ( "Server Name = " + (String)getServletContext().getAttribute("com.ibm.websphere.servlet.application.host") );


        p = request.getParameterValues("source"); 
        if( p != null ) source = p[0];
        
 
        if(source.equals("SVR") ) {

        	HttpSession httpSession = request.getSession();
        	IBMApplicationSession session = ((IBMSession)httpSession).getIBMApplicationSession();
            if ( session == null )
                counttxt= "No Count in Session";
            else
            {
                Integer value = (Integer)session.getAttribute("hwcount");
                if ( value == null )
                {
                    counttxt = "Count: Session exists, but count not found";
                }
                else
                {
                    value = new Integer(value.intValue() + 1);
                    session.setAttribute("hwcount", value);                    
                    counttxt = "Count: " + value + " (from existing session)";
                }
                
                 id = ("Session ID = " + session.getId() + " (You May Wish to Invalidate Session Before Proceeding)");
            }
       }
        if(source.equals("SES") ) {
            
        	HttpSession httpSession = request.getSession();
//        	IBMApplicationSession session = ((IBMSession)httpSession).getIBMApplicationSession();
        	
            if ( httpSession == null )
                counttxt= "Count: Cannot create session as expected!";
            else
            {				
                Integer value = (Integer)httpSession.getAttribute("hwcount");
                if ( value == null )
                {
                    value = new Integer(1);
                    counttxt = "Count: 1 (from new session)";
                    httpSession.setAttribute("listener", new SessionEventTest());
                }
                else
                {
                    value = new Integer(value.intValue() + 1);
                    counttxt = "Count: " + value + " (from existing session)";
                }
                httpSession.setAttribute("hwcount", value);				
            }   
           
            id = ("Session ID = " + httpSession.getId() );
          
           
        }

      
        if(source.equals("RESET") ) {
        	HttpSession httpSession = request.getSession();
        	IBMApplicationSession session = ((IBMSession)httpSession).getIBMApplicationSession();

            if(session != null)
                session.invalidate() ;
            id= "Session Object Invalidated";
            System.out.println("Invalidated");          

            Cookie[] cookies = request.getCookies() ;
            Cookie sessionCookie = null;
            if(cookies != null) {
                for(int i = 0 ; i < cookies.length; i++ ) {
                    if(cookies[i].getName().equals("JSESSIONID")) {
                        sessionCookie = cookies[i];
                        sessionCookie.setValue(newid);
                        sessionCookie.setPath("/");                        
                        sessionCookie.setMaxAge(0); 
                    }

                }   
            }
           if(sessionCookie != null)
                response.addCookie(sessionCookie);
        }



        request.setAttribute("wlmsource", source);
        request.setAttribute("wlmservername", servername);
        request.setAttribute("wlmid", id);
        request.setAttribute("wlmcount",counttxt);
        RequestDispatcher rd = getServletContext().getRequestDispatcher("/sessionWLM.jsp");
        rd.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
