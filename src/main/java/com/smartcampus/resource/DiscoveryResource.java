package com.smartcampus.resource;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.Map;

@Path("/")
public class DiscoveryResource {
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getApiInfo() {
        Map<String, Object> response = Map.of(
            "api_name", "Smart Campus API",
            "version", "1.0",
            "admin_contact", "campus-it@westminster.ac.uk",
            "description", "RESTful API for managing campus rooms and sensors",
            "resources", Map.of(
                "rooms", "/api/v1/rooms",
                "sensors", "/api/v1/sensors",
                "discovery", "/api/v1/"
            ),
            "hypermedia_links", Map.of(
                "rooms_endpoint", "http://localhost:8080/api/v1/rooms",
                "sensors_endpoint", "http://localhost:8080/api/v1/sensors"
            )
        );
        return Response.ok(response).build();
    }
}