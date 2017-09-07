package com.delaru.rabbitmq;

import com.delaru.model.Command;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

public class CommandConsumer {

    private java.util.function.Consumer<Command> messageConsumer;

    public CommandConsumer(java.util.function.Consumer<Command> messageConsumer) {
        this.messageConsumer = messageConsumer;
    }

    @RabbitListener(queues = "${com.delaru.rabbitmq.consumer.commands.queue}", containerFactory = "vehicleFactory")
    public void recieveMessage(Command object) {
        messageConsumer.accept(object);
    }
}
