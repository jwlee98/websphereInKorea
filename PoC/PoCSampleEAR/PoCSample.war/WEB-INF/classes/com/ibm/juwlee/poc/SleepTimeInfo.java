/*
 * Created on 2007-04-06
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.ibm.juwlee.poc;

/**
 * @author kr050104
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class SleepTimeInfo {
	
	private long _sleepTime = 500L ; 
	private static SleepTimeInfo _this = null ;

	private SleepTimeInfo()
	{
		super() ;
	}

	/**
	 * @return Returns the _sleepTime.
	 */
	public synchronized long getSleepTime() {
		return _sleepTime;
	}
	/**
	 * @param count The _sleepTime to set.
	 */
	public synchronized void setSleepTime(long count) {
		_sleepTime = count;
	}
	public synchronized static SleepTimeInfo getInstance() 
	{
		if(_this == null)
		  _this = new SleepTimeInfo() ;
	  return _this ;
	}
}
