package org.erasmusoffice;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.util.ArrayList;

public class UpdatePopUpController {

    /*@FXML
    private void switchToPrimary() throws IOException {
        App.setRoot("primary");
    }*/

    @FXML
    private Label closeButton;

    private int applicationId;

    @FXML
    private Label appID;

    @FXML
    private Button deleteButton;

    @FXML
    private Button updateButton;

    @FXML
    private CheckBox termFall;
    @FXML
    private CheckBox termSpring;

    @FXML
    private ComboBox<String> universityList;

    @FXML
    private void closeScreen(Event event) {
        Stage loginStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        loginStage.close();
    }

    @FXML
    private void initialize(){
        this.applicationId = ManagerPageController.applicationId;
        appID.setText(String.valueOf(applicationId));
    }

    @FXML
    private void deleteApplication(){
        int goner = applicationId;
        Database.deleteApplication(goner);
        Stage loginStage = (Stage) appID.getScene().getWindow();
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Successfully deleted the application", ButtonType.OK);
        alert.showAndWait();
        loginStage.close();
    }

    @FXML
    private void updateApplication(){
        String term = "";
        if (termFall.isSelected() && termSpring.isSelected()) {
            term = "full_year";
        } else if (termFall.isSelected() && !termSpring.isSelected()) {
            term = "fall";
        } else if (!termFall.isSelected() && termSpring.isSelected()) {
            term = "spring";
        } else{
            Alert alert = new Alert(Alert.AlertType.ERROR, "Please select a term", ButtonType.OK);
            alert.showAndWait();
        }
        if(universityList.getValue() == null){
            Alert alert = new Alert(Alert.AlertType.ERROR, "Please select a university", ButtonType.OK);
            alert.showAndWait();
            return;
        }

        Database.updateApplication(applicationId, term, universityList.getValue());
    }

    @FXML
    public void universityCombo(Event event) {
        ObservableList<String> observableNames = FXCollections.observableList(Database.getUniversities());
        universityList.setItems(observableNames);
    }

}
