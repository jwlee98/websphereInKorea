package example;

public class Leak implements ILeak {
	  private ILeak leak;
	  
	  public Leak(ILeak leak) {
	    this.leak = leak;
	  }	

}
