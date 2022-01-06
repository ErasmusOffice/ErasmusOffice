package org.erasmusoffice;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class UpdatePopUpController {

    /*@FXML
    private void switchToPrimary() throws IOException {
        App.setRoot("primary");
    }*/

    @FXML
    private Label closeButton;

    @FXML
    private void closeScreen(Event event){
        Stage loginStage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        loginStage.close();
    }
}