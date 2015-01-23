package com.ibm.juwlee.jaxrs;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import com.ibm.json.java.JSONObject;

@Path("/hello")
public class HelloResource {
    @GET
    public String helloGet() {
        return "[GET] Hello from JAX-RS on WebSphere Liberty Profile";
    }
    
	@GET
	@Path("/json")
    @Produces(value="application/json")
    public JSONObject getList2() {  
		JSONObject listJSONObject = new JSONObject();
		listJSONObject.put("AAAA", "Hello Earth!");
		listJSONObject.put("BBBB", "Hello Mars!");
		return listJSONObject;           
    }
    
    @POST
    public Response helloPost() {
        return Response.ok("[POST] Hello from JAX-RS on WebSphere Liberty Profile")
                       .header("X-Resource-Architect", "Jacek Laskowski")
                       .build();
    }
}