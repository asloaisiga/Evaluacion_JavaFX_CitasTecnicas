
package ni.uam.edu.tecnoasistencia.application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ni.uam.edu.tecnoasistencia.util.conexionBD;

import java.io.IOException;

public class App extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/fxml/menu-principal.fxml")
        );

        Scene scene = new Scene(loader.load());

        stage.setTitle("TecnoAsistencia - Gestión de Citas Técnicas");
        stage.setScene(scene);
        stage.show();

        if (conexionBD.probarConexion()) {
            System.out.println("Conexión exitosa con PostgreSQL.");
        } else {
            System.out.println("No se pudo conectar con PostgreSQL.");
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
