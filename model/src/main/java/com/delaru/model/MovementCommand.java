package com.delaru.model;

public class MovementCommand extends Command {

    private VehicleMovement vehicleMovement;

    public MovementCommand(CommandType commandType, VehicleMovement vehicleMovement) {
        super(commandType);
        this.vehicleMovement = vehicleMovement;
    }

    public VehicleMovement getVehicleMovement() {
        return vehicleMovement;
    }
}
