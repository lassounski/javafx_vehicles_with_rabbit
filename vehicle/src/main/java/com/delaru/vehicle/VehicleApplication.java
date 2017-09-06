package com.delaru.vehicle;

import com.delaru.model.Command;
import com.delaru.rabbitmq.Consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;


@SpringBootApplication
public class VehicleApplication {

    public static void main(String[] args) {
        SpringApplication.run(VehicleApplication.class, args);
    }

    @Autowired
    private Consumer<Command> commandConsumer;

    @PostConstruct
    public void start(){
    }
}
