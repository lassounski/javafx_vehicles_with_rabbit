package com.delaru.vehicle;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.stream.IntStream;

public class Sender {

    private final static String QUEUE_NAME = "commands";

    public static void main(String[] args) throws IOException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        String message = "Hello World!";
        IntStream.range(1,1000)
            .forEach(num -> {
                try {
                    channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

        System.out.println(" [x] Sent '" + message + "'");

        channel.close();
        connection.close();
    }
}
