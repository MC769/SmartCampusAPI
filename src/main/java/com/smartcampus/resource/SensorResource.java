package com.smartcampus.resource;

import com.smartcampus.exception.RoomNotFoundException;
import com.smartcampus.exception.SensorNotFoundException;
import com.smartcampus.exception.SensorUnavailableException;
import com.smartcampus.model.Reading;
import com.smartcampus.model.Sensor;
import com.smartcampus.service.RoomService;
import com.smartcampus.service.SensorService;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import java.net.URI;
import java.util.List;
import java.util.Map;

@Path("/api/v1/sensors")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SensorResource {
    
    @GET
    public Response getAllSensors(@QueryParam("status") String status,
                                  @QueryParam("roomId") String roomId) {
        var sensors = SensorService.getAllSensors().values().stream();
        
        if (status != null && !status.isEmpty()) {
            sensors = sensors.filter(s -> s.getStatus().equalsIgnoreCase(status));
        }
        
        if (roomId != null && !roomId.isEmpty()) {
            sensors = sensors.filter(s -> s.getRoomId().equals(roomId));
        }
        
        List<Sensor> result = sensors.toList();
        return Response.ok(Map.of(
            "total", result.size(),
            "filters_applied", Map.of(
                "status", status != null ? status : "none",
                "roomId", roomId != null ? roomId : "none"
            ),
            "sensors", result
        )).build();
    }
    
    @POST
    public Response createSensor(Sensor sensor, @Context UriInfo uriInfo) {
        // Validate room exists
        if (!RoomService.roomExists(sensor.getRoomId())) {
            throw new RoomNotFoundException(
                "Room with ID '" + sensor.getRoomId() + "' does not exist. " +
                "Cannot create sensor without a valid room."
            );
        }
        
        Sensor created = SensorService.addSensor(sensor);
        RoomService.addSensorToRoom(created.getRoomId(), created.getId());
        
        URI location = uriInfo.getAbsolutePathBuilder().path(created.getId()).build();
        return Response.created(location).entity(created).build();
    }
    
    @GET
    @Path("/{sensorId}")
    public Response getSensor(@PathParam("sensorId") String id) {
        Sensor sensor = SensorService.getSensor(id);
        if (sensor == null) {
            throw new SensorNotFoundException("Sensor not found with ID: " + id);
        }
        return Response.ok(sensor).build();
    }
    
    @DELETE
    @Path("/{sensorId}")
    public Response deleteSensor(@PathParam("sensorId") String id) {
        Sensor sensor = SensorService.getSensor(id);
        if (sensor == null) {
            throw new SensorNotFoundException("Sensor not found with ID: " + id);
        }
        
        SensorService.deleteSensor(id);
        return Response.noContent().build();
    }
    
    @PUT
    @Path("/{sensorId}/status")
    public Response updateSensorStatus(@PathParam("sensorId") String id, 
                                       Map<String, String> statusUpdate) {
        Sensor sensor = SensorService.getSensor(id);
        if (sensor == null) {
            throw new SensorNotFoundException("Sensor not found with ID: " + id);
        }
        
        String newStatus = statusUpdate.get("status");
        if (newStatus == null || (!"ACTIVE".equals(newStatus) && !"MAINTENANCE".equals(newStatus))) {
            return Response.status(400)
                .entity(Map.of("error", "Invalid status. Must be 'ACTIVE' or 'MAINTENANCE'"))
                .build();
        }
        
        sensor.setStatus(newStatus);
        return Response.ok(Map.of(
            "message", "Sensor status updated",
            "sensor_id", id,
            "new_status", newStatus
        )).build();
    }
    
    // Sub-resource for readings
    @Path("/{sensorId}/readings")
    public SensorReadingsSubResource getReadingsSubResource(@PathParam("sensorId") String sensorId) {
        Sensor sensor = SensorService.getSensor(sensorId);
        if (sensor == null) {
            throw new SensorNotFoundException("Sensor not found with ID: " + sensorId);
        }
        return new SensorReadingsSubResource(sensor);
    }
    
    // Inner class for sub-resource
    public static class SensorReadingsSubResource {
        private final Sensor sensor;
        
        public SensorReadingsSubResource(Sensor sensor) {
            this.sensor = sensor;
        }
        
        @POST
        @Consumes(MediaType.APPLICATION_JSON)
        public Response addReading(Reading reading) {
            if ("MAINTENANCE".equals(sensor.getStatus())) {
                throw new SensorUnavailableException(
                    "Sensor '" + sensor.getId() + "' is in MAINTENANCE mode. " +
                    "Cannot accept new readings until status changes to ACTIVE."
                );
            }
            
            if (reading.getTimestamp() == 0) {
                reading.setTimestamp(System.currentTimeMillis());
            }
            
            sensor.addReading(reading);
            return Response.status(Response.Status.CREATED)
                .entity(Map.of(
                    "message", "Reading added successfully",
                    "sensor_id", sensor.getId(),
                    "reading_value", reading.getValue(),
                    "timestamp", reading.getTimestamp()
                ))
                .build();
        }
        
        @GET
        public Response getAllReadings() {
            return Response.ok(Map.of(
                "sensor_id", sensor.getId(),
                "sensor_type", sensor.getType(),
                "sensor_status", sensor.getStatus(),
                "total_readings", sensor.getReadings().size(),
                "readings", sensor.getReadings()
            )).build();
        }
    }
}