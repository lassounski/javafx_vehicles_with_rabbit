package com.delaru.model;


import org.immutables.value.Value;

@Value.Immutable
public abstract class Vehicle {
    public abstract String getId();

    public abstract int getX();

    public abstract int getY();

    public abstract int getSpeedFactor();

    public abstract VehicleMovement getVehicleMovement();
}
