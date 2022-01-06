package org.erasmusoffice;

import java.io.IOException;
import java.util.concurrent.atomic.LongAccumulator;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class LoginController {

    @FXML
    private Button button;

    @FXML
    private Button button2;

    @FXML
    private Label closeButton;

    @FXML
    private void switchToSecondary() throws IOException {
        App.setRoot("secondary");
    }

    @FXML
    private void printScreen(){
        System.out.println("AAAAAA");
    }

    @FXML
    private void printScreen2(){
        System.out.println("BBBBBBB");
    }

    @FXML
    private void closeScreen(Event event){
        Stage loginStage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        loginStage.close();
    }
}
