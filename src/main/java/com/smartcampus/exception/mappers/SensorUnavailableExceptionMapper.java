package com.smartcampus.exception.mappers;

import com.smartcampus.exception.SensorUnavailableException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import java.util.Map;

@Provider
public class SensorUnavailableExceptionMapper implements ExceptionMapper<SensorUnavailableException> {
    @Override
    public Response toResponse(SensorUnavailableException exception) {
        return Response.status(Response.Status.FORBIDDEN)
                .entity(Map.of(
                    "error", "Forbidden - Sensor Unavailable",
                    "message", exception.getMessage(),
                    "status", 403
                ))
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}