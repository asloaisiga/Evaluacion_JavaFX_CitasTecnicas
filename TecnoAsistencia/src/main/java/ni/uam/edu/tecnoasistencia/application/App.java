package ni.uam.edu.tecnoasistencia.application;
import ni.uam.edu.tecnoasistencia.dao.ClienteDAO;
import ni.uam.edu.tecnoasistencia.models.Cliente;

import java.sql.SQLException;
import java.util.List;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import ni.uam.edu.tecnoasistencia.util.conexionBD;

public class App extends Application {
    @Override
    public void start(Stage primaryStage) {
        Label label = new Label("¡Hola, JavaFX!");
        StackPane root = new StackPane(label);

        Scene scene = new Scene(root, 400, 200);


        primaryStage.setTitle("Mi aplicación JavaFX");
        primaryStage.setScene(scene);
        primaryStage.show();

        if (conexionBD.probarConexion()) {

            System.out.println(
                    "Conexión exitosa con PostgreSQL."
            );

        } else {

            System.out.println(
                    "No se pudo conectar con PostgreSQL."
            );
        }
    }

    public static void app(String[] args) {
        launch(args);
    }




}
