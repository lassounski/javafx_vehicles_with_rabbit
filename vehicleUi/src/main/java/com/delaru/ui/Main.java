package com.delaru.ui;

import de.felixroske.jfxsupport.AbstractJavaFxApplicationSupport;

import org.springframework.boot.autoconfigure.SpringBootApplication;

import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

@SpringBootApplication
public class Main extends AbstractJavaFxApplicationSupport {

    public static void main(String[] args) {
        launchApp(Main.class, MainapplicationView.class, args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        super.start(stage);

        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent we) {
                System.exit(0);
            }
        });
    }
}
