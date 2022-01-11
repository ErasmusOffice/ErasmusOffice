package org.erasmusoffice;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
    private static final String dbUrl = "jdbc:postgresql://localhost:5432/erasmus_db";
    private static final String dbAdmin = "student";
    private static final String dbPassword = "0000";

    @FXML
    private Label closeButton;
    @FXML
    private Button deleteButton;

    @FXML
    private TableView<ApplicationModel> applicationTable;
    @FXML
    private TableColumn<ApplicationModel, Integer> appId;
    @FXML
    private TableColumn<ApplicationModel, Integer> studentID;
    @FXML
    private TableColumn<ApplicationModel, String> universityName;
    @FXML
    private TableColumn<ApplicationModel, String> term;

    @FXML
    private TextField idStudent;
    @FXML
    private TextField fname;
    @FXML
    private TextField lname;
    @FXML
    private TextField gpa;
    @FXML
    private TextField examResult;
    @FXML
    private CheckBox termFall;
    @FXML
    private CheckBox termSpring;
    @FXML
    private ComboBox<String> universityList;
    @FXML
    private Button apply;

    @FXML
    private TableView<UniversityModel> universitiesTable;
    @FXML
    private TableColumn<UniversityModel, String> country;
    @FXML
    private TableColumn<UniversityModel, String> uniName;
    @FXML
    private TableColumn<UniversityModel, Integer> fallQuota;
    @FXML
    private TableColumn<UniversityModel, Integer> springQuota;
    @FXML
    private Tab universitiesTab;

    @FXML
    private Tab applicationsTab;

    @FXML
    public void initialize() {
        appId.setCellValueFactory(new PropertyValueFactory<ApplicationModel, Integer>("appID"));
        studentID.setCellValueFactory(new PropertyValueFactory<ApplicationModel, Integer>("studentID"));

        universityName.setCellValueFactory(new PropertyValueFactory<ApplicationModel, String>("universityName"));
        term.setCellValueFactory(new PropertyValueFactory<ApplicationModel, String>("term"));

        country.setCellValueFactory(new PropertyValueFactory<UniversityModel, String>("country"));
        uniName.setCellValueFactory(new PropertyValueFactory<UniversityModel, String>("name"));
        fallQuota.setCellValueFactory(new PropertyValueFactory<UniversityModel, Integer>("fallQuota"));
        springQuota.setCellValueFactory(new PropertyValueFactory<UniversityModel, Integer>("springQuota"));
    }

    @FXML
    public void application(Event event) {
        String term;

        if (termFall.isSelected() && termSpring.isSelected()) {
            term = "full_year";
        } else if (termFall.isSelected() && !termSpring.isSelected()) {
            term = "fall";
        } else if (!termFall.isSelected() && termSpring.isSelected()) {
            term = "spring";
        }

        String uniName = universityList.getValue();
        System.out.println(uniName);
    }


    @FXML
    public void universityCombo(Event event) {
        ObservableList<String> observableNames = FXCollections.observableList(Database.getUniversities());
        universityList.setItems(observableNames);
    }


    @FXML
    private void closeScreen(Event event) {
        Stage loginStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        loginStage.close();
    }

    @FXML
    private void showTable() {
        //        ApplicationModel test = new ApplicationModel(1, 1, "AAA", "BBB");
        //        applicationTable.getItems().add(test);
    }

    @FXML
    private void universityTabPressed() {
        universitiesTable.getItems().clear();
        ArrayList<UniversityModel> universities = Database.getUniversitiesInfo();
        universitiesTable.getItems().addAll(universities);
    }

    @FXML
    private void applicationsTabPressed(){
        applicationTable.getItems().clear();
        ArrayList<ApplicationModel> applications = Database.getApplicationsInfo();
        applicationTable.getItems().addAll(applications);
    }

    @FXML
    private void deleteApplication(){
        int goner = applicationTable.getSelectionModel().getSelectedItem().getAppID();
        Database.deleteApplication(goner);
        applicationTable.getItems().clear();
        ArrayList<ApplicationModel> applications = Database.getApplicationsInfo();
        applicationTable.getItems().addAll(applications);
    }
}
