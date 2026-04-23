package com.smartcampus.model;

import java.util.ArrayList;
import java.util.List;

public class Sensor {
    private String id;
    private String type;
    private String status; // "ACTIVE", "MAINTENANCE"
    private String roomId;
    private List<Reading> readings;
    
    public Sensor() {
        this.readings = new ArrayList<>();
        this.status = "ACTIVE";
    }
    
    public Sensor(String type, String roomId) {
        this();
        this.type = type;
        this.roomId = roomId;
    }
    
    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public String getRoomId() { return roomId; }
    public void setRoomId(String roomId) { this.roomId = roomId; }
    
    public List<Reading> getReadings() { return readings; }
    public void setReadings(List<Reading> readings) { this.readings = readings; }
    
    public void addReading(Reading reading) {
        this.readings.add(reading);
    }
}