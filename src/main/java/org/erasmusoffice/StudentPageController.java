package org.erasmusoffice;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.util.ArrayList;

public class StudentPageController {

    /*@FXML
    private void switchToPrimary() throws IOException {
        App.setRoot("primary");
    }*/

    @FXML
    private Label closeButton;

    @FXML
    private Button deleteButton;

    @FXML
    private TableView<ApplicationModel> applicationTable;

    @FXML
    private TableColumn<ApplicationModel, String> appId;

    @FXML
    private TableColumn<ApplicationModel, String> studentID;

    @FXML
    private TableColumn<ApplicationModel, String> universityName;

    @FXML
    private TableColumn<ApplicationModel, String> term;

    @FXML
    private TableView<UniversityModel> universitiesTable;

    @FXML
    private TableColumn<UniversityModel, String> country;

    @FXML
    private TableColumn<UniversityModel, String> uniName;

    @FXML
    private TableColumn<UniversityModel, String> fallQuota;

    @FXML
    private TableColumn<UniversityModel, String> springQuota;

    @FXML
    private Tab universitiesTab;

    @FXML
    public void initialize(){
        appId.setCellValueFactory(new PropertyValueFactory<ApplicationModel, String>("appID"));
        studentID.setCellValueFactory(new PropertyValueFactory<ApplicationModel, String>("studentID"));
        universityName.setCellValueFactory(new PropertyValueFactory<ApplicationModel, String>("universityName"));
        term.setCellValueFactory(new PropertyValueFactory<ApplicationModel, String>("term"));

        country.setCellValueFactory(new PropertyValueFactory<UniversityModel, String>("country"));
        uniName.setCellValueFactory(new PropertyValueFactory<UniversityModel, String>("name"));
        fallQuota.setCellValueFactory(new PropertyValueFactory<UniversityModel, String>("fallQuota"));
        springQuota.setCellValueFactory(new PropertyValueFactory<UniversityModel, String>("springQuota"));

    }

    @FXML
    private void closeScreen(Event event) {
        Stage loginStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        loginStage.close();
    }

    @FXML
    private void showTable(){
//        ApplicationModel test = new ApplicationModel(1, 1, "AAA", "BBB");
//        applicationTable.getItems().add(test);
    }

    @FXML
    private void universityTabPressed(){
        universitiesTable.getItems().clear();
        ArrayList<UniversityModel> universities = Database.getUniversitiesInfo();
        universitiesTable.getItems().addAll(universities);
    }
}
