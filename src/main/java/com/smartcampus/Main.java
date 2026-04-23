package com.smartcampus;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.jackson.JacksonFeature;

import java.net.URI;

public class Main {
    public static void main(String[] args) throws Exception {
        // Create ResourceConfig with NO ApplicationPath conflict
        ResourceConfig rc = new ResourceConfig()
            .packages("com.smartcampus.resource", 
                     "com.smartcampus.filter",
                     "com.smartcampus.exception")
            .register(JacksonFeature.class);
        
        HttpServer server = GrizzlyHttpServerFactory.createHttpServer(
            URI.create("http://localhost:8080/"),
            rc
        );
        
        System.out.println("==========================================");
        System.out.println("Server started!");
        System.out.println("Base URL: http://localhost:8080/");
        System.out.println("Test: http://localhost:8080/hello");
        System.out.println("==========================================");
        
        System.in.read();
        server.shutdown();
    }
}