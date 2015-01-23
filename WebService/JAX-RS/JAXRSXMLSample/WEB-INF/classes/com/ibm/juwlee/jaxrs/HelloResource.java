package com.ibm.juwlee.jaxrs;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
 
@Path("/hello")
public class HelloResource {
    @GET
    public String helloGet() {
        return "[GET] Hello from JAX-RS on WebSphere Liberty Profile";
    }
 
    @POST
    public Response helloPost() {
        return Response.ok("[POST] Hello from JAX-RS on WebSphere Liberty Profile")
                       .header("X-Resource-Architect", "Jacek Laskowski")
                       .build();
    }
}