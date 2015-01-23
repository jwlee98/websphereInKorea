package com.ibm.juwlee.poc ;import java.util.Vector;
public class MemLeakCache extends Vector{	private static final long serialVersionUID = 1L;		private static MemLeakCache _this = null ;	
	private MemLeakCache()	{		super() ;	}	public synchronized static MemLeakCache getInstance() 	{		if(_this == null)		  _this = new MemLeakCache() ;	  return _this ;	}	}
	
