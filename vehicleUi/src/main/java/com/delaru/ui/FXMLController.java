package com.delaru.ui;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.scene.text.TextBoundsType;

public class FXMLController implements Initializable {

    enum Direction {
        LEFT, RIGHT, UP, DOWN, STOP
    };

    private int vehicleIdCounter = 1;

    private Map<String, Node> vehicles = new HashMap<>();
    private Map<String, Direction> movingVehicles = new ConcurrentHashMap<>();

    private ExecutorService executorService = Executors.newFixedThreadPool(10);

    private ToggleGroup directionsGroup = new ToggleGroup();

    @FXML
    Pane world;

    @FXML
    ComboBox<String> vehicleId;

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

    @FXML
    private void createVehicleAction(ActionEvent event) {
        String lastVehicleId = Integer.toString(vehicleIdCounter);
        Node vehicle = createVehicle(lastVehicleId);

        world.getChildren().add(vehicle);
        vehicles.put(lastVehicleId, vehicle);
        movingVehicles.put(lastVehicleId, Direction.STOP);
        executorService.execute(new MovingVehicle(lastVehicleId));
        vehicleId.getItems().add(lastVehicleId);

        vehicleIdCounter++;
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
        world.getChildren().removeAll(vehicles.get(vehicleId.getValue()));
        vehicles.remove(vehicleId.getValue());
        movingVehicles.remove(vehicleId.getValue());
    }

    @FXML
    private void onVehicleChange() {
        String vehichleId = vehicleId.getValue();
        switch (movingVehicles.get(vehichleId)) {
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

    class MovingVehicle extends Thread {

        private String id;

        public MovingVehicle(String id) {
            this.id = id;
            this.setDaemon(true);
        }

        @Override
        public void run() {
            System.out.println("Vehicle running in " + Thread.currentThread().getName());
            while (movingVehicles.containsKey(id)) {
                Node vehicle = vehicles.get(id);
                switch (movingVehicles.get(id)) {
                    case UP:
                        vehicle.setTranslateY(vehicle.getTranslateY() - 1);
                        break;
                    case DOWN:
                        vehicle.setTranslateY(vehicle.getTranslateY() + 1);
                        break;
                    case LEFT:
                        vehicle.setTranslateX(vehicle.getTranslateX() - 1);
                        break;
                    case RIGHT:
                        vehicle.setTranslateX(vehicle.getTranslateX() + 1);
                        break;
                    default:
                        break;
                }
                try {
                    Thread.sleep(200);
                } catch (InterruptedException ex) {
                    Logger.getLogger(FXMLController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            System.out.println("Vehicle in " + Thread.currentThread().getName() + " is dead");
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        rightDirection.setToggleGroup(directionsGroup);
        leftDirection.setToggleGroup(directionsGroup);
        upDirection.setToggleGroup(directionsGroup);
        downDirection.setToggleGroup(directionsGroup);
        stop.setToggleGroup(directionsGroup);
        stop.setSelected(true);

        directionsGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            Direction direction = Direction.valueOf(((RadioButton) newValue).getText());
            movingVehicles.put(vehicleId.getValue(), direction);
        });
    }
}
