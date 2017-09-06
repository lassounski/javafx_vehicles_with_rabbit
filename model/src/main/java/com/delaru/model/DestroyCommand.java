package com.delaru.model;

public class DestroyCommand extends Command {

    private String vehicleId;

    public DestroyCommand() {
        super(CommandType.DESTROY);
    }

    public DestroyCommand(String vehicleId) {
        super(CommandType.DESTROY);
        this.vehicleId = vehicleId;
    }

    public String getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(String vehicleId) {
        this.vehicleId = vehicleId;
    }
}
