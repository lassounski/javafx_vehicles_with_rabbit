package com.delaru.vehicle;

import com.delaru.model.Command;
import com.delaru.model.VehicleStatus;
import com.delaru.rabbitmq.VehicleStatusProducer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Service responsible for calculating the position of the vehicles, each vehicle runs on a thread.
 */
@Service
public class CommandService {

    private final static org.apache.log4j.Logger LOGGER = org.apache.log4j.Logger.getLogger(Controller.class);

    private int vehicleIdCounter = 1;

    private ExecutorService executorService = Executors.newFixedThreadPool(10);

    private ConcurrentHashMap<String, VehicleStatus> movingVehicles = new ConcurrentHashMap<>(10);

    @Autowired
    private VehicleStatusProducer vehicleStatusProducer;

    /**
     * Creates a new vehicle
     */
    public void createVehicle() {
        String lastVehicleId = Integer.toString(vehicleIdCounter);

        executorService.execute(new MovingVehicle(lastVehicleId));
        movingVehicles.put(lastVehicleId, new VehicleStatus(lastVehicleId));
        vehicleIdCounter++;
    }

    /**
     * Destroys a vehicle
     *
     * @param command for destruction
     */
    public void destroyVehicle(Command command) {
        LOGGER.debug("Destroying vehicle " + command.getVehicleId());
        movingVehicles.remove(command.getVehicleId());
    }

    /**
     * Changes the vehicle position
     *
     * @param command for movement
     */
    public void moveVehicle(Command command) {
        if (movingVehicles.containsKey(command.getVehicleId())) {
            LOGGER.debug(String.format("Moving vehicle %s to %s", command.getVehicleId(), command
                .getVehicleMovement()));
            VehicleStatus vehicleStatus = movingVehicles.get(command.getVehicleId());
            vehicleStatus.setVehicleMovement(command.getVehicleMovement());
        }
    }

    /**
     * Thread that calculates the vehicle position and puts a message in
     * the movement queue for the front-end
     */
    class MovingVehicle extends Thread {

        private String id;

        public MovingVehicle(String id) {
            this.id = id;
            this.setDaemon(true);
        }

        @Override
        public void run() {
            LOGGER.debug("Vehicle running in " + Thread.currentThread().getName());
            while (movingVehicles.containsKey(id)) {
                VehicleStatus movingVehicle = movingVehicles.get(id);
                switch (movingVehicles.get(id).getVehicleMovement()) {
                    case UP:
                        movingVehicle.setY(movingVehicle.getY() - movingVehicle.getSpeed());
                        break;
                    case DOWN:
                        movingVehicle.setY(movingVehicle.getY() + movingVehicle.getSpeed());
                        break;
                    case LEFT:
                        movingVehicle.setX(movingVehicle.getX() - movingVehicle.getSpeed());
                        break;
                    case RIGHT:
                        movingVehicle.setX(movingVehicle.getX() + movingVehicle.getSpeed());
                        break;
                    default:
                        break;
                }
                vehicleStatusProducer.produce(movingVehicle);
                try {
                    Thread.sleep(500);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            LOGGER.debug("Vehicle in " + Thread.currentThread().getName() + " is dead");
        }
    }
}
