package com.delaru.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.util.Random;

@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "@id", scope = VehicleStatus
        .class)
public class VehicleStatus {

    private int speedModifier = new Random().nextInt(10) + 1;

    private String vehicleId;

    private int x, y;

    private VehicleMovement vehicleMovement = VehicleMovement.STOP;

    public VehicleStatus() {
    }

    public VehicleStatus(String vehicleId) {
        this.vehicleId = vehicleId;
    }

    public String getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(String vehicleId) {
        this.vehicleId = vehicleId;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public VehicleMovement getVehicleMovement() {
        return vehicleMovement;
    }

    public void setVehicleMovement(VehicleMovement vehicleMovement) {
        this.vehicleMovement = vehicleMovement;
    }

    public int getSpeed() {
        return speedModifier;
    }
}
