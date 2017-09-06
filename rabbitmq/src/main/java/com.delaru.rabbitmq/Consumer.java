package com.delaru.rabbitmq;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import org.apache.commons.lang.SerializationUtils;

import java.io.IOException;
import java.io.Serializable;

import javax.annotation.PostConstruct;

public class Consumer<T extends Serializable> {

    private String queue_name;
    private String host;

    private Channel channel;
    private Connection connection;

    public Consumer(String queue_name, String host) {
        this.queue_name = queue_name;
        this.host = host;
    }

    @PostConstruct
    private void setUp() throws IOException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(host);
        connection = factory.newConnection();
        channel = connection.createChannel();
        channel.queueDeclare(queue_name, false, false, false, null);
        channel.basicQos(1);
        consume();
    }

    private void consume() throws IOException {
        com.rabbitmq.client.Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
                                       byte[] body) throws IOException {
                T message = (T) SerializationUtils.deserialize(body);

                System.out.println(" [x] Received '" + message + "' in t : " + Thread.currentThread());
                try {
                    System.out.println("Processing message");
                    //doWork(message);
                } finally {
                    System.out.println(" [x] Done");
                    channel.basicAck(envelope.getDeliveryTag(), false);
                }
            }
        };
        channel.basicConsume(queue_name, false, consumer);
    }
}
