package com.smartcampus;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import java.io.IOException;
import java.net.URI;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {
    private static final Logger logger = Logger.getLogger(Main.class.getName());
    private static final String BASE_URI = "http://localhost:8080/";
    
    public static void main(String[] args) {
        try {
            // Configure ResourceConfig to scan all packages
            ResourceConfig resourceConfig = new ResourceConfig()
                .packages("com.smartcampus.resource",
                         "com.smartcampus.exception.mappers",
                         "com.smartcampus.filter");
            
            // Create and start HTTP server
            HttpServer server = GrizzlyHttpServerFactory.createHttpServer(
                URI.create(BASE_URI),
                resourceConfig
            );
            
            logger.info("==========================================");
            logger.info("Smart Campus API Server Started!");
            logger.info("==========================================");
            logger.info("Base URL: " + BASE_URI);
            logger.info("API Entry Point: " + BASE_URI + "api/v1/");
            logger.info("==========================================");
            logger.info("Available endpoints:");
            logger.info("  GET  /api/v1/                 - Discovery");
            logger.info("  GET  /api/v1/rooms            - List all rooms");
            logger.info("  POST /api/v1/rooms            - Create room");
            logger.info("  GET  /api/v1/rooms/{id}       - Get room details");
            logger.info("  DELETE /api/v1/rooms/{id}     - Delete room");
            logger.info("  GET  /api/v1/sensors          - List sensors (filterable)");
            logger.info("  POST /api/v1/sensors          - Create sensor");
            logger.info("  GET  /api/v1/sensors/{id}     - Get sensor details");
            logger.info("  DELETE /api/v1/sensors/{id}   - Delete sensor");
            logger.info("  POST /api/v1/sensors/{id}/readings - Add reading");
            logger.info("  GET  /api/v1/sensors/{id}/readings - Get readings");
            logger.info("==========================================");
            logger.info("Press ENTER to stop the server...");
            
            System.in.read();
            server.shutdown();
            logger.info("Server stopped.");
            
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Failed to start server", e);
        }
    }
}