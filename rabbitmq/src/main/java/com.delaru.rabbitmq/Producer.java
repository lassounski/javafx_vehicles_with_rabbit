package com.delaru.rabbitmq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import org.apache.commons.lang.SerializationUtils;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;
import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

public class Producer<T extends Serializable> {

    @Value("com.delaru.producer.queue.name")
    private String QUEUE_NAME;

    @Value("com.delaru.producer.host")
    private String HOST;

    private Channel channel;
    private Connection connection;

    @PostConstruct
    private void setUp() throws IOException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(HOST);
        connection = factory.newConnection();
        channel = connection.createChannel();
    }

    public void send(T object) {
        try {
            channel.basicPublish("", QUEUE_NAME, null, SerializationUtils.serialize(object));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @PreDestroy
    private void destroy() throws IOException {
        channel.close();
        connection.close();
    }
}