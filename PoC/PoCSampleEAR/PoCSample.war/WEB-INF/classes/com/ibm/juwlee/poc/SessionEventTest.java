
package com.ibm.juwlee.poc;

import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;

/**
 * @author coder4ever
 *
 * 이 생성된 유형 주석에 대한 템플리트를 변경하려면 다음으로 이동하십시오.
 * 창&gt;환경설정&gt;Java&gt;코드 생성&gt;코드 및 주석
 */
public class SessionEventTest implements HttpSessionBindingListener {

	/* (비Javadoc)
	 * @see javax.servlet.http.HttpSessionBindingListener#valueBound(javax.servlet.http.HttpSessionBindingEvent)
	 */
	public void valueBound(HttpSessionBindingEvent arg0) {
		// TODO 자동 생성된 메소드 스텁
		System.out.println("Create Session!!!");
	}

	/* (비Javadoc)
	 * @see javax.servlet.http.HttpSessionBindingListener#valueUnbound(javax.servlet.http.HttpSessionBindingEvent)
	 */
	public void valueUnbound(HttpSessionBindingEvent arg0) {
		// TODO 자동 생성된 메소드 스텁
		System.out.println("Destroy Session!!!");

	}

}
