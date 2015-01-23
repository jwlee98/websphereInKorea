package com.ibm.juwlee;

import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.rmi.PortableRemoteObject;
import javax.transaction.NotSupportedException;
import javax.transaction.SystemException;

/**
 * Session Bean implementation class OracleDB2Call
 */
@TransactionManagement(TransactionManagementType.BEAN)
@Stateless
public class OracleDB2Call implements OracleDB2CallLocal {

	private SessionContext sctx;

	@Resource
	private void setSctx(SessionContext sctx) {
	  this.sctx = sctx;
	}	
	
    /**
     * Default constructor. 
     */
    public OracleDB2Call() {
        // TODO Auto-generated constructor stub
    }
    
    public void insertDB2() {
    	
      Context ctx;
      Object o=null;
		try {
			ctx = new InitialContext();
			o = ctx.lookup("cell/nodes/was1Node01/servers/OracleServer/com.ibm.juwlee.OracleCallTestRemote");
			if (o == null)
			{
				System.out.println("Null");
			}
		} catch (NamingException e) {
			// TODO 자동 생성된 catch 블록
			e.printStackTrace();
		}			
		//OracleCallTestRemote ora =(OracleCallTestRemote)PortableRemoteObject.narrow(o,OracleCallTestRemote.class);
		OracleCallTestRemote ora =(OracleCallTestRemote)o;	
		
		try {
			ctx = new InitialContext();
			o = ctx.lookup("cell/nodes/was2Node01/servers/DB2Server/com.ibm.juwlee.DB2CallTestRemote");
			if (o == null)
			{
				System.out.println("Null");
			}
		} catch (NamingException e) {
			// TODO 자동 생성된 catch 블록
			e.printStackTrace();
		}			
		DB2CallTestRemote db2 =(DB2CallTestRemote)PortableRemoteObject.narrow(o,DB2CallTestRemote.class);
//		DB2CallTestRemote db2 =(DB2CallTestRemote)o;	
		
		try {
			sctx.getUserTransaction().begin();
			ora.insertDB();		
			db2.insertDB();		
			sctx.getUserTransaction().commit(); 
		} catch (IllegalStateException e) {
			// TODO 자동 생성된 catch 블록
			e.printStackTrace();
		} catch (NotSupportedException e) {
			// TODO 자동 생성된 catch 블록
			e.printStackTrace();
		} catch (Exception e) {
			// TODO 자동 생성된 catch 블록
			e.printStackTrace();
		}	
	
	

    }

}
