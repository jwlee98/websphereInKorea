package com.ibm.juwlee.mdb;

import javax.jms.JMSException;

/**
 * Bean implementation class for Enterprise Bean: MDBTestBean
 */
public class MDBTestBeanBean
	implements
		javax.ejb.MessageDrivenBean,
		javax.jms.MessageListener {

	private javax.ejb.MessageDrivenContext fMessageDrivenCtx;

	/**
	 * getMessageDrivenContext
	 */
	public javax.ejb.MessageDrivenContext getMessageDrivenContext() {
		return fMessageDrivenCtx;
	}

	/**
	 * setMessageDrivenContext
	 */
	public void setMessageDrivenContext(javax.ejb.MessageDrivenContext ctx) {
		fMessageDrivenCtx = ctx;
	}

	/**
	 * ejbCreate
	 */
	public void ejbCreate() {
	}

	/**
	 * onMessage
	 */
	public void onMessage(javax.jms.Message msg) {
		javax.jms.TextMessage txtMsg = (javax.jms.TextMessage)msg;
		try {
			System.out.println("Message -> " +txtMsg.getText());			
		} catch (JMSException e) {
			e.printStackTrace(System.out);
		}		
	}

	/**
	 * ejbRemove
	 */
	public void ejbRemove() {
	}
}
