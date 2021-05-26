package de.fherfurt.api;

//@Path("/hello-resteasy")
public class GreetingResource {

//    @GET
//    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        return "Hello RESTEasy";
    }
}