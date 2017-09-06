package com.delaru.vehicle;

import com.delaru.model.DestroyCommand;
import com.delaru.model.MovementCommand;
import com.delaru.model.VehicleStatus;
import com.delaru.rabbitmq.VehicleStatusProducer;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;

@Service
public class CommandService {

    private int vehicleIdCounter = 1;

    private ExecutorService executorService = Executors.newFixedThreadPool(10);

    private ConcurrentHashMap<String, VehicleStatus> movingVehicles = new ConcurrentHashMap<>(10);

    @Autowired
    private VehicleStatusProducer vehicleStatusProducer;

    public void createVehicle() {
        String lastVehicleId = Integer.toString(vehicleIdCounter);

        executorService.execute(new MovingVehicle(lastVehicleId));
        movingVehicles.put(lastVehicleId, new VehicleStatus(lastVehicleId));
        vehicleIdCounter++;
    }

    public void destroyVehicle(DestroyCommand destroyCommand) {
        movingVehicles.remove(destroyCommand.getVehicleId());
    }

    public void moveVehicle(MovementCommand movementCommand) {
        if(movingVehicles.contains(movementCommand.getVehicleId())){
            VehicleStatus vehicleStatus = movingVehicles.get(movementCommand.getVehicleId());
            vehicleStatus.setVehicleMovement(movementCommand.getVehicleMovement());
        }
    }

    class MovingVehicle extends Thread {

        private String id;

        public MovingVehicle(String id) {
            this.id = id;
            this.setDaemon(true);
        }

        @Override
        public void run() {
            System.out.println("Vehicle running in " + Thread.currentThread().getName());
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
                    Thread.sleep(5000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            System.out.println("Vehicle in " + Thread.currentThread().getName() + " is dead");
        }
    }
}
