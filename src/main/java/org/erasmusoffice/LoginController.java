package org.erasmusoffice;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;

public class LoginController {

    @FXML
    private Button button;

    @FXML
    private void switchToSecondary() throws IOException {
        App.setRoot("secondary");
    }

    @FXML
    private void printScreen(){
        System.out.println("AAAAAA");
    }
}
