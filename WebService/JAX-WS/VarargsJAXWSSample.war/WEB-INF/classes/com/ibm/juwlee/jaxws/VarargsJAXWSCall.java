package com.ibm.juwlee.jaxws;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;

@WebService(serviceName = "VarargsJAXWSCallService")
@XmlSeeAlso({com.ibm.juwlee.jaxws.TestVO.class})
public class VarargsJAXWSCall implements VarargsJAXWSInterface{
	
	@WebMethod
	public Object CallIt(Object...objects) {
		int sum = 0;
		
		for (Object o:objects){
			System.out.println("Obj = " + o);
			try {
				sum += (int)o;
			}catch(ClassCastException e) {
				int aaa = ((TestVO)o).getA();
				System.out.println("AAA = " + aaa);
				sum += aaa;
			}
		}
		System.out.println("Sum = " + sum);

		return sum;
	}

}
