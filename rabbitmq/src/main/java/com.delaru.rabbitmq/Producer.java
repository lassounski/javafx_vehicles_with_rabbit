package com.delaru.rabbitmq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import org.apache.commons.lang.SerializationUtils;

import java.io.IOException;
import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

public class Producer<T extends Serializable> {

    private String queue_name;
    private String host;

    private Channel channel;
    private Connection connection;

    public Producer(String queue_name, String host) {
        this.queue_name = queue_name;
        this.host = host;
    }

    @PostConstruct
    private void setUp() throws IOException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(host);
        connection = factory.newConnection();
        channel = connection.createChannel();
    }

    public void send(T object) {
        try {
            channel.basicPublish("", queue_name, null, SerializationUtils.serialize(object));
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