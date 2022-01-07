package org.erasmusoffice;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebEvent;
import javafx.scene.web.WebView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import org.w3c.dom.Element;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ManagerPageController {

    /*@FXML
    private void switchToPrimary() throws IOException {
        App.setRoot("primary");
    }*/

    @FXML
    private WebView browser;

    @FXML
    private Button updateButton;

    @FXML
    private Label closeButton;

    @FXML
    private Button queryButton;

    @FXML
    private TableView queryTable;
    private ObservableList<ObservableList> data;

    @FXML
    public void initialize() {
        WebEngine webEngine = browser.getEngine();
        webEngine.load(getClass().getResource("/ace/editor.html").toExternalForm());
    }

    @FXML
    public void showPopUp() {
        try {
            Parent root1 = App.loadFXML("UpdatePopUp");
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setTitle("Choose A Option");
            stage.setScene(new Scene(root1));
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
    private void executeQuery() throws SQLException {
        String code = (String)browser.getEngine().executeScript("editor.getValue()");
        queryTable.getItems().clear();
        queryTable.getColumns().clear();
        data = FXCollections.observableArrayList();
        try (Connection conn = Database.connectToDatabase("it_staff", "0000"); Statement stmt =
                conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(code);
            //            TableView queryTable = new TableView();
            if (rs != null) {
                for(int i=0 ; i<rs.getMetaData().getColumnCount(); i++){
                    //We are using non property style for making dynamic table
                    final int j = i;
                    TableColumn col = new TableColumn(rs.getMetaData().getColumnName(i+1));
                    col.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList,String>, ObservableValue<String>>(){
                        public ObservableValue<String> call(TableColumn.CellDataFeatures<ObservableList, String> param) {
                            return new SimpleStringProperty(param.getValue().get(j).toString());
                        }
                    });

                    queryTable.getColumns().addAll(col);
                }
            }
            while(rs.next()){
                //Iterate Row
                ObservableList<String> row = FXCollections.observableArrayList();
                for(int i=1 ; i<=rs.getMetaData().getColumnCount(); i++){
                    //Iterate Column
                    row.add(rs.getString(i));
                }
                data.add(row);

            }
            queryTable.setItems(data);

        } catch (SQLException e) {
            System.out.println("error: Could not run the query.");
            Alert alert = new Alert(Alert.AlertType.ERROR, "Invalid Query", ButtonType.OK);
            alert.showAndWait();
            e.printStackTrace();
        }
    }
}
