package org.erasmusoffice;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.sql.SQLException;

public class LoginController {
    public static Student studentInfo;
    public static String staffRole = null;

    @FXML
    RadioButton student;
    @FXML
    RadioButton manager;
    @FXML
    Button loginButton;
    @FXML
    TextField usernameInput;
    @FXML
    PasswordField passwordInput;
    @FXML
    private Label closeButton;

    @FXML
    private static void showPage(String fxmlName) {
        try {
            Parent root = App.loadFXML(fxmlName);
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void closeScreen(Event event) {
        Stage loginStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        loginStage.close();
    }

    @FXML
    private void loginButtonPressed() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setHeaderText("Username or password incorrect!");
        alert.setContentText("Please enter your credentials again.");

        Stage loginStage = (Stage) usernameInput.getScene().getWindow();

        if (!(usernameInput.getText().isBlank() || usernameInput.getText().isEmpty() || passwordInput.getText()
                .isBlank() || passwordInput.getText().isEmpty() || (!student.isSelected() && !manager.isSelected()))) {

            if (student.isSelected()) {
                try {
                    studentInfo =
                            Database.getStudent(Integer.valueOf(usernameInput.getText()), passwordInput.getText());
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                if (studentInfo == null) {
                    alert.showAndWait();
                } else {
                    loginStage.close();
                    showPage("StudentPage");
                }
            } else if (manager.isSelected()) {
                try {
                    staffRole = Database.checkLoginInfo(usernameInput.getText(), passwordInput.getText());
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                if (staffRole == null) {
                    alert.showAndWait();
                } else {
                    loginStage.close();
                    showPage("ManagerPage");
                }
            }
        }
    }
}
