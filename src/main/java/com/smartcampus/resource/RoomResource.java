package com.smartcampus.resource;

import com.smartcampus.exception.RoomNotEmptyException;
import com.smartcampus.exception.RoomNotFoundException;
import com.smartcampus.model.Room;
import com.smartcampus.service.RoomService;
import com.smartcampus.service.SensorService;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import java.net.URI;
import java.util.List;
import java.util.Map;

@Path("/rooms")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RoomResource {
    
    @GET
    public Response getAllRooms(@QueryParam("full") @DefaultValue("false") boolean full) {
        var rooms = RoomService.getAllRooms().values();
        
        if (!full) {
            // Return only IDs and basic info
            List<Map<String, String>> simplified = rooms.stream()
                .map(room -> Map.of(
                    "id", room.getId(),
                    "name", room.getName(),
                    "uri", "/api/v1/rooms/" + room.getId()
                ))
                .toList();
            return Response.ok(simplified).build();
        }
        
        return Response.ok(rooms).build();
    }
    
    @POST
    public Response createRoom(Room room, @Context UriInfo uriInfo) {
        Room created = RoomService.addRoom(room);
        URI location = uriInfo.getAbsolutePathBuilder().path(created.getId()).build();
        return Response.created(location).entity(created).build();
    }
    
    @GET
    @Path("/{roomId}")
    public Response getRoom(@PathParam("roomId") String id) {
        Room room = RoomService.getRoom(id);
        if (room == null) {
            throw new NotFoundException("Room not found with ID: " + id);
        }
        return Response.ok(room).build();
    }
    
    @DELETE
    @Path("/{roomId}")
    public Response deleteRoom(@PathParam("roomId") String id) {
        Room room = RoomService.getRoom(id);
        if (room == null) {
            throw new NotFoundException("Room not found with ID: " + id);
        }
        
        // Business logic: Cannot delete room with active sensors
        if (SensorService.hasSensorsInRoom(id)) {
            throw new RoomNotEmptyException(
                "Cannot delete room '" + id + "' because it contains " + 
                RoomService.getSensorCountInRoom(id) + " active sensor(s). " +
                "Please remove or reassign all sensors first."
            );
        }
        
        RoomService.deleteRoom(id);
        return Response.noContent().build();
    }
    
    @GET
    @Path("/{roomId}/sensors")
    public Response getSensorsInRoom(@PathParam("roomId") String roomId) {
        if (!RoomService.roomExists(roomId)) {
            throw new RoomNotFoundException("Room not found: " + roomId);
        }
        
        var sensors = SensorService.getSensorsByRoom(roomId);
        return Response.ok(Map.of(
            "room_id", roomId,
            "sensor_count", sensors.size(),
            "sensors", sensors
        )).build();
    }
}