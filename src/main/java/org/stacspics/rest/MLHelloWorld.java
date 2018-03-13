package org.stacspics.rest;

import javax.ws.rs.GET;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.Path;

@Path("/mlhelloworld")
public class MLHelloWorld {

  @GET
  @Path("{lang}")
  @Produces("text/plain")
  public String getMessage(@PathParam("lang") String lang) {
    switch (lang.toLowerCase()) {
      case "en": return "Hello World!";
      case "de": return "Hallo Welt!";
      case "cn": return "你好世界!";
      case "ru": return "Privet Mir";
      default: return "language "+lang+" not supported.";
    }
  }
}
