package com.smartcampus.service;

import com.smartcampus.model.Room;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class RoomService {
    private static final ConcurrentHashMap<String, Room> rooms = new ConcurrentHashMap<>();
    private static final AtomicInteger idGenerator = new AtomicInteger(1);
    
    public static ConcurrentHashMap<String, Room> getAllRooms() {
        return rooms;
    }
    
    public static Room addRoom(Room room) {
        String id = String.valueOf(idGenerator.getAndIncrement());
        room.setId(id);
        rooms.put(id, room);
        return room;
    }
    
    public static Room getRoom(String id) {
        return rooms.get(id);
    }
    
    public static boolean deleteRoom(String id) {
        return rooms.remove(id) != null;
    }
    
    public static boolean roomExists(String id) {
        return rooms.containsKey(id);
    }
    
    public static void addSensorToRoom(String roomId, String sensorId) {
        Room room = rooms.get(roomId);
        if (room != null) {
            room.addSensorId(sensorId);
        }
    }
    
    public static void removeSensorFromRoom(String roomId, String sensorId) {
        Room room = rooms.get(roomId);
        if (room != null) {
            room.removeSensorId(sensorId);
        }
    }
    
    public static int getSensorCountInRoom(String roomId) {
        Room room = rooms.get(roomId);
        return room != null ? room.getSensorIds().size() : 0;
    }
}