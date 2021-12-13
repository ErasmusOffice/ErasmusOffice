module org.erasmusoffice {
    requires javafx.controls;
    requires javafx.fxml;

    opens org.erasmusoffice to javafx.fxml;
    exports org.erasmusoffice;
}