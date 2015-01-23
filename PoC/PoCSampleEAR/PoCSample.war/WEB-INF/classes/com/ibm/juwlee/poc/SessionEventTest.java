
package com.ibm.juwlee.poc;

import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;

/**
 * @author coder4ever
 *
 * �� ������ ���� �ּ��� ���� ���ø�Ʈ�� �����Ϸ��� �������� �̵��Ͻʽÿ�.
 * â&gt;ȯ�漳��&gt;Java&gt;�ڵ� ����&gt;�ڵ� �� �ּ�
 */
public class SessionEventTest implements HttpSessionBindingListener {

	/* (��Javadoc)
	 * @see javax.servlet.http.HttpSessionBindingListener#valueBound(javax.servlet.http.HttpSessionBindingEvent)
	 */
	public void valueBound(HttpSessionBindingEvent arg0) {
		// TODO �ڵ� ������ �޼ҵ� ����
		System.out.println("Create Session!!!");
	}

	/* (��Javadoc)
	 * @see javax.servlet.http.HttpSessionBindingListener#valueUnbound(javax.servlet.http.HttpSessionBindingEvent)
	 */
	public void valueUnbound(HttpSessionBindingEvent arg0) {
		// TODO �ڵ� ������ �޼ҵ� ����
		System.out.println("Destroy Session!!!");

	}

}
