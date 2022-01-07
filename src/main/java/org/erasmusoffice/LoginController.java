package org.erasmusoffice;

import java.io.IOException;
import java.util.concurrent.atomic.LongAccumulator;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class LoginController {

    @FXML
    private Label closeButton;

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
    private void switchToSecondary() throws IOException {
        App.setRoot("secondary");
    }

    @FXML
    private void closeScreen(Event event) {
        Stage loginStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        loginStage.close();
    }

    @FXML
    private void loginButtonPressed() {
        if (!(usernameInput.getText().isBlank() || usernameInput.getText().isEmpty() || passwordInput.getText()
                .isBlank() || passwordInput.getText().isEmpty() || !student.isSelected() || !manager.isSelected())) {
            if (student.isSelected()) {

            }
        }

    }
}
