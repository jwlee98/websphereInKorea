package com.ibm.juwlee.jaxrs;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

@Path("/xml/user")
public class XMLService {
 
	@GET
	@Path("/get")
	@Produces("application/xml")
	public User getUserInXML() {
 
		User user = new User();
		user.setUsername("juwlee");
		user.setPassword("password");
		user.setPin(123456);
 
		return user; 
 
	}
 
}