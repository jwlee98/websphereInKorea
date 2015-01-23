package com.ibm.juwlee.jaxrs;

import java.io.File;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/multipart")
public class MultipartResource {
    @GET
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response handleMultipart(@FormParam("fileid") int theFileid,
                                    @FormParam("description") String theDescription,
                                    @FormParam("thefile") File theFile) {
        System.out.printf("theFileid: %s, theDescription: %s, theFile: %s%n", theFileid, theDescription, theFile);
        return Response.ok().build();
    }
 
    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response completeHandleMultipart(@FormParam("xml") String theXml,
                                            @FormParam("file") List<File> files) {
        final String response = "theXML: " + theXml + " and " + files.size() + " file(s) received";
        System.out.println(response);
        return Response.ok(response).build();
    }
 
}