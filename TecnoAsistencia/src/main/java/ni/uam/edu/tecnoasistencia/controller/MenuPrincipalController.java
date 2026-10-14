
package ni.uam.edu.tecnoasistencia.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MenuPrincipalController {

    @FXML
    private void abrirClientes() {
        abrirVentana("/fxml/cliente-view.fxml", "Gestión de Clientes");
    }

    @FXML
    private void abrirCitas() {
        abrirVentana("/fxml/cita-view.fxml", "Gestión de Citas Técnicas");
    }

    private void abrirVentana(String ruta, String titulo) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(ruta));
            Scene scene = new Scene(loader.load());

            Stage stage = new Stage();
            stage.setTitle(titulo);
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            javafx.scene.control.Alert alerta = new javafx.scene.control.Alert(
                    javafx.scene.control.Alert.AlertType.ERROR
            );
            alerta.setTitle("Error");
            alerta.setHeaderText("No se pudo abrir la ventana");
            alerta.setContentText(e.getMessage());
            alerta.showAndWait();
        }
    }
}
