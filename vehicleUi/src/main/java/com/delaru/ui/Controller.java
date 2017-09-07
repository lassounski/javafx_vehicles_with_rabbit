package com.delaru.ui;

import com.delaru.model.Command;
import com.delaru.model.CommandType;
import com.delaru.model.VehicleMovement;
import com.delaru.model.VehicleStatus;
import com.delaru.rabbitmq.CommandProducer;

import de.felixroske.jfxsupport.FXMLController;

import org.springframework.beans.factory.annotation.Autowired;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.scene.text.TextBoundsType;

@FXMLController
public class Controller implements Initializable {

    private Map<String, VehicleStatus> vehicles = new HashMap<>();

    private Map<String, Node> paneVehicles = new HashMap<>();

    private ToggleGroup directionsGroup = new ToggleGroup();

    private ExecutorService vehicleExecutorService = Executors.newFixedThreadPool(10);

    @FXML
    Pane pane;

    @FXML
    ComboBox<String> vehicleIdCombo;

    @FXML
    RadioButton rightDirection;

    @FXML
    RadioButton leftDirection;

    @FXML
    RadioButton upDirection;

    @FXML
    RadioButton downDirection;

    @FXML
    RadioButton stop;

    @Autowired
    private CommandProducer commandProducer;

    @FXML
    private void createVehicleAction(ActionEvent event) {
        Command create = new Command(CommandType.CREATE);
        commandProducer.produce(create);
    }

    private Node createVehicle(String vehicleId) {
        Circle circle = new Circle(20, 20, 10, Color.BLUEVIOLET);
        circle.setOpacity(0.7);
        circle.relocate(40, 40);

        Text text = new Text(vehicleId);
        text.setBoundsType(TextBoundsType.VISUAL);

        StackPane stack = new StackPane();
        stack.getChildren().addAll(circle, text);
        stack.setId(vehicleId);

        return stack;
    }

    @FXML
    private void removeVehicle() {
        pane.getChildren().removeAll(paneVehicles.get(vehicleIdCombo.getValue()));
        paneVehicles.remove(vehicleIdCombo.getValue());

        Command removeCommand = new Command(CommandType.DESTROY, vehicleIdCombo.getValue());
        commandProducer.produce(removeCommand);
    }

    @FXML
    private void onVehicleChange() {
        String selectedVehicleId = vehicleIdCombo.getValue();
        switch (vehicles.get(selectedVehicleId).getVehicleMovement()) {
            case UP:
                upDirection.fire();
                break;
            case DOWN:
                downDirection.fire();
                break;
            case LEFT:
                leftDirection.fire();
                break;
            case RIGHT:
                rightDirection.fire();
                break;
            case STOP:
                stop.fire();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        rightDirection.setToggleGroup(directionsGroup);
        leftDirection.setToggleGroup(directionsGroup);
        upDirection.setToggleGroup(directionsGroup);
        downDirection.setToggleGroup(directionsGroup);
        stop.setToggleGroup(directionsGroup);
        stop.setSelected(true);

        directionsGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            VehicleMovement vehicleMovement = VehicleMovement.valueOf(((RadioButton) newValue).getText());
            Command movementCommand = new Command(CommandType.MOVEMENT, vehicleIdCombo.getValue(), vehicleMovement);

            commandProducer.produce(movementCommand);
        });
    }

    public void handleVehicle(VehicleStatus vehicleStatus) {
        Thread graphicsThread = new Thread(new UpdateGraphics(vehicleStatus));
        graphicsThread.setDaemon(true);
        graphicsThread.start();
    }

    private class UpdateGraphics extends Task<Void> {

        private VehicleStatus vehicleStatus;

        public UpdateGraphics(VehicleStatus vehicleStatus) {
            this.vehicleStatus = vehicleStatus;
        }

        @Override
        protected Void call() throws Exception {
            Platform.runLater(() -> {
                System.out.println("Updating graphics thread");
                String vehicleId = vehicleStatus.getVehicleId();

                if (vehicles.containsKey(vehicleId)) {
                    System.out.println(String.format("Moving vehicle %s to %d,%d", vehicleId,
                        vehicleStatus.getX(),
                        vehicleStatus.getY()));
                    Node vehicle = paneVehicles.get(vehicleId);

                    vehicle.setTranslateX(vehicleStatus.getX());
                    vehicle.setTranslateY(vehicleStatus.getY());
                } else {
                    System.out.println("Creating vehicle " + vehicleId);

                    vehicles.put(vehicleId, vehicleStatus);
                    Node vehicle = createVehicle(vehicleId);

                    pane.getChildren().add(vehicle);
                    paneVehicles.put(vehicleId, vehicle);
                    vehicleIdCombo.getItems().add(vehicleId);
                }
            });
            return null;
        }
    }
}
