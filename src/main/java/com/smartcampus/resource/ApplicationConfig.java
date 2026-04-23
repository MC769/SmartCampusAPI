package com.smartcampus.resource;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

@ApplicationPath("/api/v1")
public class ApplicationConfig extends Application {
    // JAX-RS will automatically scan for resources
}