package com.delaru.vehicle;

import com.delaru.model.CommandType;
import com.delaru.rabbitmq.CommandConsumer;
import com.delaru.rabbitmq.VehicleStatusProducer;

import org.apache.log4j.Logger;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.amqp.SimpleRabbitListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfiguration {

    private final static Logger LOGGER = Logger.getLogger(RabbitMqConfiguration.class);

    @Value("${com.delaru.rabbitmq.producer.vehicle.queue}")
    private String producerVehicleQueueName;

    @Value("${com.delaru.rabbitmq.exchange}")
    private String exchange;

    @Value("${com.delaru.rabbitmq.producer.vehicle.routingkey}")
    private String producerVehicleRoutingkey;

    @Bean
    public Queue queue() {
        return new Queue(producerVehicleQueueName, false);
    }

    @Bean
    public DirectExchange exchange() {
        return new DirectExchange(exchange);
    }

    @Bean
    public Binding binding(Queue queue, DirectExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(producerVehicleRoutingkey);
    }

    public AmqpTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        return rabbitTemplate;
    }

    @Bean
    public VehicleStatusProducer getVehicleStatusProducer() {
        return new VehicleStatusProducer();
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Autowired
    private CommandService commandService;

    @Bean
    public SimpleRabbitListenerContainerFactory vehicleFactory(ConnectionFactory connectionFactory,
                                                               SimpleRabbitListenerContainerFactoryConfigurer configurer) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        configurer.configure(factory, connectionFactory);
        factory.setMessageConverter(jsonMessageConverter());
        return factory;
    }

    @Bean
    public CommandConsumer getCommandConsumer() {
        return new CommandConsumer(command -> {
            LOGGER.debug("Received request to: " + command.getCommandType());
            if (command.getCommandType().equals(CommandType.DESTROY)) {
                commandService.destroyVehicle(command);
            } else if (command.getCommandType().equals(CommandType.MOVEMENT)) {
                commandService.moveVehicle(command);
            } else {
                commandService.createVehicle();
            }
        });
    }
}
