package com.smartcampus.service;

import com.smartcampus.model.Sensor;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class SensorService {
    private static final ConcurrentHashMap<String, Sensor> sensors = new ConcurrentHashMap<>();
    private static final AtomicInteger idGenerator = new AtomicInteger(1);
    
    public static ConcurrentHashMap<String, Sensor> getAllSensors() {
        return sensors;
    }
    
    public static Sensor addSensor(Sensor sensor) {
        String id = String.valueOf(idGenerator.getAndIncrement());
        sensor.setId(id);
        sensors.put(id, sensor);
        return sensor;
    }
    
    public static Sensor getSensor(String id) {
        return sensors.get(id);
    }
    
    public static boolean deleteSensor(String id) {
        Sensor sensor = sensors.remove(id);
        if (sensor != null) {
            RoomService.removeSensorFromRoom(sensor.getRoomId(), id);
        }
        return sensor != null;
    }
    
    public static List<Sensor> getSensorsByRoom(String roomId) {
        return sensors.values().stream()
                .filter(s -> s.getRoomId().equals(roomId))
                .collect(Collectors.toList());
    }
    
    public static boolean hasSensorsInRoom(String roomId) {
        return sensors.values().stream()
                .anyMatch(s -> s.getRoomId().equals(roomId));
    }
    
    public static List<Sensor> filterByStatus(String status) {
        if (status == null) return sensors.values().stream().collect(Collectors.toList());
        return sensors.values().stream()
                .filter(s -> s.getStatus().equalsIgnoreCase(status))
                .collect(Collectors.toList());
    }
}