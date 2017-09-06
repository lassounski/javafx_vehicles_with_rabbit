package com.delaru.rabbitmq;

import com.delaru.model.Command;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

public class CommandProducer {

    @Autowired
    private AmqpTemplate amqpTemplate;

    @Value("${com.delaru.rabbitmq.exchange}")
    private String exchange;

    @Value("${com.delaru.rabbitmq.routingkey}")
    private String routingkey;

    public void produce(Command object) {
        amqpTemplate.convertAndSend(exchange, routingkey, object);
    }
}
