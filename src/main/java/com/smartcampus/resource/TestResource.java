package com.smartcampus.resource;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;

@Path("/api/v1/hello")
public class TestResource {
    
    @GET
    public Response sayHello() {
        return Response.ok("{\"message\": \"Hello World!\"}").build();
    }
}