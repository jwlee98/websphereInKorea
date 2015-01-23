package example;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

public class ExampleFactory {

  public static IExample newInstance() {
	    try {
	      return newInstanceWithThrows();
	    } catch (InstantiationException e) {
	      throw new RuntimeException(e.getCause());
	    } catch (IllegalAccessException e) {
	      throw new RuntimeException(e);
	    } catch (ClassNotFoundException e) {
	      throw new RuntimeException(e);
	    }
	  }
	  
	  private static IExample newInstanceWithThrows() throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		  
	    URLClassLoader tmp = 
	      new URLClassLoader(new URL[] {getClassPath()}) {
	      public Class<?> loadClass(String name)
	        throws ClassNotFoundException {
	        if ("example.Example".equals(name) || "example.Leak".equals(name))
	          return findClass(name);
	        return super.loadClass(name);
	      }
	    };
	    
//        IExample ie = (IExample)tmp.loadClass("example.Example").newInstance();
	    tmp.loadClass("example.Example").newInstance();
	    
	    IExample ie = new Example();

	    return ie;
	  }

	  private static URL getClassPath() {
	    String resName = ExampleFactory.class.getName().replace('.', '/') + ".class";
	    String loc = ExampleFactory.class.getClassLoader().getResource(resName).toExternalForm();    
	    String dir = loc.substring(0, loc.length() - resName.length());	    
	    try {
	      return new URL(dir);
	    } catch (MalformedURLException e) {
	      throw new RuntimeException(e);
	    }
	  }
}
