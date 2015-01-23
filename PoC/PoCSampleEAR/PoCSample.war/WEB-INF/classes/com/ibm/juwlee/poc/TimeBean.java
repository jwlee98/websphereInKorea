package com.ibm.juwlee.poc;
// TimeBean.java
//
// Set tabs to 3 spaces when viewing this file
// by Tom Alcott, WebSphere WW Techical Sales

import java.util.*;
import java.io.Serializable;

public class TimeBean implements Serializable
	{
	private static final long serialVersionUID = 1L;
	
	Date beanTimeDate;
	
	public Date getbeanTimeDate()
		{
		beanTimeDate = new Date() ;  	
			
		return beanTimeDate;
	}
		
}