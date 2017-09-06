package com.delaru.rabbitmq;

import com.delaru.model.VehicleStatus;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

public class VehicleStatusConsumer {

    private java.util.function.Consumer<VehicleStatus> messageConsumer;

    public VehicleStatusConsumer(java.util.function.Consumer<VehicleStatus> messageConsumer) {
        this.messageConsumer = messageConsumer;
    }

    @RabbitListener(queues = "${com.delaru.rabbitmq.consumer.vehicle.status.queue}",
            containerFactory ="vehicleFactory")
    public void recieveMessage(VehicleStatus message) {
        System.out.println("Consuming message "+message);
        messageConsumer.accept(message);
    }
}
