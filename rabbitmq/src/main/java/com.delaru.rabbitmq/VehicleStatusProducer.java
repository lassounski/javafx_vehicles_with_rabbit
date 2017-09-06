package com.delaru.rabbitmq;

import com.delaru.model.VehicleStatus;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

public class VehicleStatusProducer {

    @Autowired
    private AmqpTemplate amqpTemplate;

    @Value("${com.delaru.rabbitmq.exchange}")
    private String exchange;

    @Value("${com.delaru.rabbitmq.producer.vehicle.routingkey}")
    private String routingkey;

    public void produce(VehicleStatus object) {
        amqpTemplate.convertAndSend(exchange, routingkey, object);
    }
}
