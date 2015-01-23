<%@ page import="com.ibm.juwlee.poc.*"%>
<%	out.println("----- How to use ----- <BR>") ;
	out.println("memleak.jsp : Show current memory status<BR>");	out.println("memleak.jsp?mem=10  : Add 10% of max memory to MemLeakCache <BR>") ;
	out.println("memleak.jsp?target_mem=80  : Make total used memory up to 80% of max memory <BR>") ;	out.println("memleak.jsp?clear=ok  : Clear Leak Cache <BR>") ;
	out.println("You can use float type number (ex - 0.01) as mem or target_mem parameter value<BR>") ;
	out.println("This program has 1 or 2 second sleep time. this is enforced mechanism by sleep() method which is used in code.<BR>");	out.println("----- Made by euijung cha / ejcha@kr.ibm.com / Apr.2007 ----- <BR> ") ;%>
<%	String strMem = request.getParameter("mem") ;
	String strTargetMem = request.getParameter("target_mem") ;	String clear = request.getParameter("clear") ;
	if(clear != null && clear.equalsIgnoreCase("ok"))	{	  MemLeakCache.getInstance().clear() ;	  out.println("<BR><BR>MemLeakCache is cleared!! <BR>") ;	  return ;	}
	Runtime runtime = Runtime.getRuntime() ;

	runtime.gc() ;
	Thread.sleep(1000) ;
	
	long maxMemory = runtime.maxMemory() ;
	long totalMemory = runtime.totalMemory() ;
	long freeMemory = runtime.freeMemory() ;
	long usedMemory = totalMemory - freeMemory ;
	long additionalMem = 0L;

	if(strMem==null && strTargetMem == null  && clear == null)
	{
		out.println("<BR>-------Current heap memory status ----------<BR>") ;
		out.println("maxMemory = " + maxMemory/1000000L + "MB <BR>") ;
		out.println("totalMemory = " + totalMemory/1000000L + "MB <BR>") ;
	//	out.println("freeMemory = " + freeMemory/1000000L + "MB (" + (int)((double)freeMemory/(double)totalMemory*100) + "% of max Memory) <BR>") ;
		out.println("usedMemory = " + usedMemory/1000000L + "MB (" + (int)((double)usedMemory/(double)maxMemory*100) + "% of max Memory) <BR>") ;
		out.println("----------------------------------") ;			
		return;
	}
	else
	{
		out.println("<BR>-------Previous heap memory status ----------<BR>") ;	
		out.println("maxMemory = " + maxMemory/1000000L + "MB <BR>") ;
		out.println("totalMemory = " + totalMemory/1000000L + "MB <BR>") ;
	//	out.println("freeMemory = " + freeMemory/1000000L + "MB (" + (int)((double)freeMemory/(double)totalMemory*100) + "% of max Memory) <BR>") ;
		out.println("usedMemory = " + usedMemory/1000000L + "MB (" + (int)((double)usedMemory/(double)maxMemory*100) + "% of max Memory) <BR>") ;
		out.println("----------------------------------") ;	
	}
	if (strMem != null)
	{
		float mem = Float.parseFloat(strMem) ;
		additionalMem = (int)((double)maxMemory * (double)mem / 100d) ;
	}
	else if (strTargetMem != null)
	{
		float targetMem = Float.parseFloat(strTargetMem) ;
		additionalMem = (int)((double) maxMemory * (double)targetMem/100d) - usedMemory ;
	}

	int realAddedMem = 0 ;

	  if(additionalMem >0 )	  {
	  		int loopCount = (int)((double)additionalMem / 10000d) ; //leak per 10k			//Making byte array
			for(int i = 0 ; i < loopCount ; i++)
			{				byte[] bytes = new byte[10000] ;				//Byte byteObj = new Byte(bytes) ;					  //Store byte array to MemLeakCache (Singleton vector) object					MemLeakCache.getInstance().add(bytes) ;
				realAddedMem += 1000 ;
			}		}
	out.println("<BR><BR>Successfully added " + realAddedMem + " bytes to MemLeakCache <BR>") ;

	runtime.gc() ;
	Thread.sleep(1000) ;

	totalMemory = runtime.totalMemory() ;
	freeMemory = runtime.freeMemory() ;
	usedMemory = totalMemory - freeMemory ;
	
	out.println("<BR>-------Current heap memory status ----------<BR>") ;	
	out.println("maxMemory = " + maxMemory/1000000L + "MB <BR>") ;
	out.println("totalMemory = " + totalMemory/1000000L + "MB <BR>") ;
//	out.println("freeMemory = " + freeMemory/1000000L + "MB (" + (int)((double)freeMemory/(double)totalMemory*100) + "% of max Memory) <BR>") ;
	out.println("usedMemory = " + usedMemory/1000000L + "MB (" + (int)((double)usedMemory/(double)maxMemory*100) + "% of max Memory) <BR>") ;
	out.println("----------------------------------") ;

%>
