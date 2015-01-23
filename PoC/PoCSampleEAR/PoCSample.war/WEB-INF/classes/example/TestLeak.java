package example;

public class TestLeak {

    private static IExample example1;
    private static IExample example2;	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
        boolean b_count = true;
        int count = 0;
		example1 = ExampleFactory.newInstance();
	    
	    while (b_count) {
	        example2 = ExampleFactory.newInstance().copy(example2);
	        
	        System.out.println("1) " +
	            example1.message() + " = " + example1.plusPlus());
	        System.out.println("2) " +
	            example2.message() + " = " + example2.plusPlus());
	        System.out.println();
	        
	        count++;
	    
	        if (count > 100)
	        	b_count = false;
	        
	        Thread.currentThread();
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	      }
	    		

	}

}
