package com.delaru.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "@id", scope =
        MovementCommand.class)
public class MovementCommand extends Command {

    private String vehicleId;

    private VehicleMovement vehicleMovement;

    public MovementCommand() {
        super(CommandType.MOVEMENT);
    }

    public MovementCommand(VehicleMovement vehicleMovement, String vehicleId) {
        super(CommandType.MOVEMENT);
        this.vehicleId = vehicleId;
        this.vehicleMovement = vehicleMovement;
    }

    public VehicleMovement getVehicleMovement() {
        return vehicleMovement;
    }

    public void setVehicleMovement(VehicleMovement vehicleMovement) {
        this.vehicleMovement = vehicleMovement;
    }

    public String getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(String vehicleId) {
        this.vehicleId = vehicleId;
    }

    @Override
    public String toString() {
        return "Command [commandType=" + this.getCommandType() + ", " + "vehicleMovement=" + getVehicleMovement() + "]";
    }
}
