package com.smartcampus.exception.mappers;

import com.smartcampus.exception.RoomNotFoundException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import java.util.Map;

@Provider
public class RoomNotFoundExceptionMapper implements ExceptionMapper<RoomNotFoundException> {
    @Override
    public Response toResponse(RoomNotFoundException exception) {
        return Response.status(422) // Unprocessable Entity
                .entity(Map.of(
                    "error", "Unprocessable Entity",
                    "message", exception.getMessage(),
                    "status", 422
                ))
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}