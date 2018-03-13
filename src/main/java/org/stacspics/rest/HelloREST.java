package org.stacspics.rest;

import javax.ws.rs.GET;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.Path;

@Path("/helloworld")
public class HelloREST {
  @GET	
  @Produces("text/plain")
  public String getMessage() {		
    return "Hello World!";		
  }
}
