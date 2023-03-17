package com.example.triforium;

import javafx.animation.AnimationTimer;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HelloController {
    private worldLogic mainSim;

    @FXML
    private Stage stage;
    private Scene scene;

    @FXML
    private  StackPane stackPane;
    @FXML
    private ImageView[] imageList = new ImageView[10];
    @FXML
    private Pane pane = new Pane();
    @FXML
    private Button startButton;


    @FXML
    protected void onStartButtonClick(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("simulationScreen.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    @FXML protected void assignFarmerClick(ActionEvent event) throws IOException{

    }
    @FXML protected void assignMinerClick(ActionEvent event) throws IOException{

    }
    @FXML protected void assignSmithClick(ActionEvent event) throws IOException{

    }

    @FXML
    protected void startSimClick(ActionEvent event) throws  IOException { //start the  simulation
        //prevent players from starting multiple simulations by removing the button when clicked
        startButton.setVisible(false);
        mainSim = new worldLogic();
        //add text displays to the pane
        pane.getChildren().addAll(mainSim.foodInfo,mainSim.ironInfo,mainSim.toolInfo, mainSim.population);
        Thread mainSimulation = new Thread(mainSim);

        for (int i = 0;i<mainSim.getPopList().size();i++){
            //set coordinate of the pop images to 0 so they will spawn in the church
            mainSim.getPopList().get(i).portrait.setX(0);
            mainSim.getPopList().get(i).portrait.setY(0);

            //this code would be used for name tags
            //mainSim.getPopList().get(i).textDisplay.setX(mainSim.getPopList().get(i).portrait.getX());
            //mainSim.getPopList().get(i).textDisplay.setY(mainSim.getPopList().get(i).portrait.getY());

            //add the image to the background pane
            pane.getChildren().add(mainSim.getPopList().get(i).portrait);
            pane.getChildren().add(mainSim.getPopList().get(i).textDisplay);
        }

        ExecutorService es = Executors.newCachedThreadPool();
        es.submit(() -> {
            mainSimulation.start();
        });

    }

}