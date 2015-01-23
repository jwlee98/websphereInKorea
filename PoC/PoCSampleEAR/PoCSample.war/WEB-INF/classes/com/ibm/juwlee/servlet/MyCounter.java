package com.ibm.juwlee.servlet;

public class MyCounter {
	  private int count = 0;
	
	  public MyCounter()
	  {
	    Thread.dumpStack();
	  }
	
	  public MyCounter(boolean b)
	  {
	  }
	
	  public void increment()
	  {
	    this.count += 1;
	  }
	
	  public int getCount() {
	    return this.count;
	  }
}
