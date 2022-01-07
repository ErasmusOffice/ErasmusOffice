package org.erasmusoffice;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

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
    public void initialize(){
        appId.setCellValueFactory(new PropertyValueFactory<ApplicationModel, String>("appID"));
        studentID.setCellValueFactory(new PropertyValueFactory<ApplicationModel, String>("studentID"));
        universityName.setCellValueFactory(new PropertyValueFactory<ApplicationModel, String>("universityName"));
        term.setCellValueFactory(new PropertyValueFactory<ApplicationModel, String>("term"));

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
}
