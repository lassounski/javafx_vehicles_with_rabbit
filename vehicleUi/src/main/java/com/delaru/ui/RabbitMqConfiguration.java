package com.delaru.ui;

import com.delaru.model.Command;
import com.delaru.rabbitmq.Producer;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfiguration {

    @Value("${com.delaru.producer.queue.name}")
    private String QUEUE_NAME;

    @Value("${com.delaru.producer.host}")
    private String HOST;

    @Bean
    public Producer<Command> createProducerBean() {
        return new Producer<>(QUEUE_NAME, HOST);
    }
}
