package com.smartcampus.model;

import java.util.ArrayList;
import java.util.List;

public class Room {
    private String id;
    private String name;
    private String building;
    private int floor;
    private List<String> sensorIds;
    
    public Room() {
        this.sensorIds = new ArrayList<>();
    }
    
    public Room(String name, String building, int floor) {
        this();
        this.name = name;
        this.building = building;
        this.floor = floor;
    }
    
    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getBuilding() { return building; }
    public void setBuilding(String building) { this.building = building; }
    
    public int getFloor() { return floor; }
    public void setFloor(int floor) { this.floor = floor; }
    
    public List<String> getSensorIds() { return sensorIds; }
    public void setSensorIds(List<String> sensorIds) { this.sensorIds = sensorIds; }
    
    public void addSensorId(String sensorId) {
        this.sensorIds.add(sensorId);
    }
    
    public void removeSensorId(String sensorId) {
        this.sensorIds.remove(sensorId);
    }
}