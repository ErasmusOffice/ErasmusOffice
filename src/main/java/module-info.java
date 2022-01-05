module org.erasmusoffice {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.postgresql.jdbc;

    opens org.erasmusoffice to javafx.fxml;
    exports org.erasmusoffice;
}