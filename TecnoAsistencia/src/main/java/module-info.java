module ni.uam.edu.tecnoasistencia {

    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.postgresql.jdbc;

    opens ni.uam.edu.tecnoasistencia.application to javafx.fxml;

    exports ni.uam.edu.tecnoasistencia.application;
}