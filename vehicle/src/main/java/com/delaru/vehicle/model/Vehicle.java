package com.delaru.vehicle.model;


import org.immutables.value.Value;

@Value.Immutable
public abstract class Vehicle {

    private String id;
    private int x, y;
    private boolean isMoving;
    private VehicleMovement movingVehicleMovement;
}
