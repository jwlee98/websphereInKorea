package ejbs.servlet;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.ejb.CreateException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.rmi.PortableRemoteObject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ibm.juwlee.ejbTest.SessionBeanCall;
import com.ibm.juwlee.ejbTest.SessionBeanCallHome;
import com.ibm.websphere.naming.PROPS;

/**
 * Servlet implementation class SessionCallServlet
 */
public class SessionCallServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SessionCallServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doPost(request, response);		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		System.out.println("Start Servlet");		
        Hashtable env = new Hashtable();
        env.put(Context.INITIAL_CONTEXT_FACTORY,"com.ibm.websphere.naming.WsnInitialContextFactory");
        env.put(Context.PROVIDER_URL, "corbaloc:iiop:localhost:2809");
//        env.put(Context.PROVIDER_URL, "corbaloc:iiop:1.2@localhost:2809/NameServiceCellRoot");
//        env.put(PROPS.NAME_SPACE_ROOT, PROPS.NAME_SPACE_ROOT_CELL_PERSISTENT);    
//        env.put(PROPS.NAME_SPACE_ROOT, PROPS.NAME_SPACE_ROOT_CELL);
        
        Context ctx;
        Object o=null;
		try {
			ctx = new InitialContext();
			System.out.println("Initialcontext end");

//			Enumeration e =ctx.list("/clusters/DC_testCluster02/ejb/com/ibm/juwlee/ejbTest");
//			Enumeration e =ctx.list("cell/clusters");
//			while(e.hasMoreElements()){
//				System.out.println(e.nextElement());
//			}
			
//			o = ctx.lookup("java:comp/env/ejb/SessionBeanCall");	
			o = ctx.lookup("cell/clusters/DC_testCluster02/ejb/com/ibm/juwlee/ejbTest/SessionBeanCallHome");
			if (o == null)
			{
				System.out.println("Null");
			}
		} catch (NamingException e) {
			// TODO 자동 생성된 catch 블록
			e.printStackTrace();
		}
        
		SessionBeanCallHome home=(SessionBeanCallHome)PortableRemoteObject.narrow(o,SessionBeanCallHome.class);
		SessionBeanCall sbc = null;
		try {
			System.out.println("create start");
			sbc = home.create();
		} catch (CreateException e) {
			// TODO 자동 생성된 catch 블록
			e.printStackTrace();
		}
		
		
        sbc.increaseNum();
        System.out.println("Increased Number : " + sbc.getNum());
        System.out.println("Finish Servlet");

	}

}
