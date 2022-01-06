package org.erasmusoffice;

import javafx.fxml.FXML;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

public class ManagerPageController {

    /*@FXML
    private void switchToPrimary() throws IOException {
        App.setRoot("primary");
    }*/

    @FXML
    WebView browser;

    @FXML
    public void initialize(){
        WebEngine webEngine = browser.getEngine();
        webEngine.load(getClass().getResource("/ace/editor.html").toExternalForm());
        //File file = new File("../../main/resources/ace/editor.html");
        /*try {
            URL url = file.toURI().toURL();
            webEngine.load(url.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }*/

        //webEngine.load("https://www.google.com");
    }

}