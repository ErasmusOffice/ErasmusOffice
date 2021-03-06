package org.erasmusoffice;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
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
    private TableColumn<ApplicationModel, Integer> appId;
    @FXML
    private TableColumn<ApplicationModel, Integer> studentID;
    @FXML
    private TableColumn<ApplicationModel, String> universityName;
    @FXML
    private TableColumn<ApplicationModel, String> term;
    @FXML
    private TableColumn<ApplicationModel, String> result;

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
    private Label studentNameLabel;

    @FXML
    private AnchorPane anchorPane;

    private Stage stage;

    private double xOffset;
    private double yOffset;

    @FXML
    public void initialize() {
        Database.dbPassword = "0000";
        Database.dbAdmin = "student";

        appId.setCellValueFactory(new PropertyValueFactory<ApplicationModel, Integer>("appID"));
        studentID.setCellValueFactory(new PropertyValueFactory<ApplicationModel, Integer>("studentID"));
        universityName.setCellValueFactory(new PropertyValueFactory<ApplicationModel, String>("universityName"));
        term.setCellValueFactory(new PropertyValueFactory<ApplicationModel, String>("term"));
        result.setCellValueFactory(new PropertyValueFactory<ApplicationModel, String>("resultString"));

        country.setCellValueFactory(new PropertyValueFactory<UniversityModel, String>("country"));
        uniName.setCellValueFactory(new PropertyValueFactory<UniversityModel, String>("name"));
        fallQuota.setCellValueFactory(new PropertyValueFactory<UniversityModel, Integer>("fallQuota"));
        springQuota.setCellValueFactory(new PropertyValueFactory<UniversityModel, Integer>("springQuota"));
        studentNameLabel.setText(LoginController.studentInfo.getFname() + " " + LoginController.studentInfo.getLname());


        idStudent.setText(String.valueOf(LoginController.studentInfo.getStdID()));
        fname.setText(LoginController.studentInfo.getFname());
        lname.setText(LoginController.studentInfo.getLname());
        gpa.setText(String.valueOf(LoginController.studentInfo.getGPA()));
        examResult.setText(String.valueOf(LoginController.studentInfo.getExamResult()));

        Stage stage = App.primaryStage;
        Parent root = studentNameLabel.getParent().getParent();
        root.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                xOffset = stage.getX() - event.getScreenX();
                yOffset = stage.getY() - event.getScreenY();
            }
        });
        root.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                stage.setX(event.getScreenX() + xOffset);
                stage.setY(event.getScreenY() + yOffset);
            }
        });
    }

    @FXML
    public void applyButtonPressed(Event event) {
        String term = "";

        if (termFall.isSelected() && termSpring.isSelected()) {
            term = "full_year";
        } else if (termFall.isSelected() && !termSpring.isSelected()) {
            term = "fall";
        } else if (!termFall.isSelected() && termSpring.isSelected()) {
            term = "spring";
        }

        String uniName = universityList.getValue();
        int uni_id = Database.getUniversityId(uniName);

        ApplicationModel newApp = new ApplicationModel();
        newApp.setUniversityId(uni_id);
        newApp.setStudentID(LoginController.studentInfo.getStdID());
        newApp.setTerm(term);
        newApp.setPriority(Database.getNextPriority(LoginController.studentInfo.getStdID()));
        Database.insertApplication(newApp);


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
        ArrayList<ApplicationModel> applications =
                Database.getApplicationsOfStudent(LoginController.studentInfo.getStdID());
        applicationTable.getItems().addAll(applications);
    }

    @FXML
    private void deleteApplication(){
        int goner = applicationTable.getSelectionModel().getSelectedItem().getAppID();
        Database.deleteApplication(goner);
        applicationTable.getItems().clear();
        ArrayList<ApplicationModel> applications = Database.getApplicationsOfStudent(LoginController.studentInfo.getStdID());
        applicationTable.getItems().addAll(applications);
    }

    public void panepressed(MouseEvent me)
    {
        stage = (Stage)((Node)me.getSource()).getScene().getWindow();
        xOffset= stage.getX()- me.getScreenX();
        yOffset= stage.getY()- me.getScreenY();


    }
    public void panedraged(MouseEvent me)
    {
        stage = (Stage)((Node)me.getSource()).getScene().getWindow();
        stage.setX(xOffset+me.getScreenX());
        stage.setY(yOffset+me.getScreenY());


    }
}
