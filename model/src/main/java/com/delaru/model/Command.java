package com.delaru.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "@id", scope = Command.class)
public class Command {

    private CommandType commandType;

    private String vehicleId;

    private VehicleMovement vehicleMovement;

    public Command() {
    }

    public Command(CommandType commandType) {
        this.commandType = commandType;
    }

    public Command(CommandType commandType, String vehicleId) {
        this.commandType = commandType;
        this.vehicleId = vehicleId;
    }

    public Command(CommandType commandType, String vehicleId, VehicleMovement vehicleMovement) {
        this.commandType = commandType;
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

    public CommandType getCommandType() {
        return commandType;
    }

    public void setCommandType(CommandType commandType) {
        this.commandType = commandType;
    }
}
