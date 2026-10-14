
package ni.uam.edu.tecnoasistencia.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import ni.uam.edu.tecnoasistencia.dao.CitaTecnicaDAO;
import ni.uam.edu.tecnoasistencia.dao.ClienteDAO;
import ni.uam.edu.tecnoasistencia.models.CitaTecnica;
import ni.uam.edu.tecnoasistencia.models.Cliente;

import java.sql.SQLException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class CitaTecnicaController {

    @FXML private ComboBox<Cliente> cbCliente;
    @FXML private DatePicker dpFecha;
    @FXML private TextField txtHora;
    @FXML private TextField txtTipoServicio;
    @FXML private ComboBox<String> cbEstado;
    @FXML private TextArea txtDescripcion;

    @FXML private ComboBox<String> cbFiltroEstado;
    @FXML private DatePicker dpFiltroFecha;

    @FXML private TableView<CitaTecnica> tablaCitas;
    @FXML private TableColumn<CitaTecnica, Integer> colId;
    @FXML private TableColumn<CitaTecnica, String> colCliente;
    @FXML private TableColumn<CitaTecnica, String> colFecha;
    @FXML private TableColumn<CitaTecnica, String> colHora;
    @FXML private TableColumn<CitaTecnica, String> colServicio;
    @FXML private TableColumn<CitaTecnica, String> colEstado;

    private final CitaTecnicaDAO citaDAO = new CitaTecnicaDAO();
    private final ClienteDAO clienteDAO = new ClienteDAO();

    private final ObservableList<CitaTecnica> citas =
            FXCollections.observableArrayList();

    private final DateTimeFormatter formatoHora =
            DateTimeFormatter.ofPattern("HH:mm");

    @FXML
    public void initialize() {
        cbEstado.getItems().addAll(
                "Pendiente", "En proceso", "Finalizada", "Cancelada"
        );

        cbFiltroEstado.getItems().addAll(
                "Pendiente", "En proceso", "Finalizada", "Cancelada"
        );

        colId.setCellValueFactory(data ->
                new javafx.beans.property.SimpleObjectProperty<>(
                        data.getValue().getId()));

        colCliente.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(
                        data.getValue().getCliente().toString()));

        colFecha.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(
                        data.getValue().getFecha().toString()));

        colHora.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(
                        data.getValue().getHora().format(formatoHora)));

        colServicio.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(
                        data.getValue().getTipoServicio()));

        colEstado.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(
                        data.getValue().getEstado()));

        tablaCitas.setItems(citas);

        cargarClientes();
        cargarCitas();
    }

    private void cargarClientes() {
        try {
            cbCliente.setItems(FXCollections.observableArrayList(
                    clienteDAO.listar()));
        } catch (SQLException e) {
            mostrarAlerta(Alert.AlertType.ERROR,
                    "Error al cargar clientes", e.getMessage());
        }
    }

    private void cargarCitas() {
        try {
            citas.setAll(citaDAO.listar());
        } catch (SQLException e) {
            mostrarAlerta(Alert.AlertType.ERROR,
                    "Error al cargar citas", e.getMessage());
        }
    }

    private CitaTecnica obtenerDatos() {
        if (cbCliente.getValue() == null ||
                dpFecha.getValue() == null ||
                txtHora.getText().isBlank() ||
                txtTipoServicio.getText().isBlank() ||
                cbEstado.getValue() == null ||
                txtDescripcion.getText().isBlank()) {

            mostrarAlerta(Alert.AlertType.WARNING,
                    "Validación", "Todos los campos son obligatorios.");
            return null;
        }

        LocalTime hora;

        try {
            hora = LocalTime.parse(
                    txtHora.getText().trim(), formatoHora);
        } catch (DateTimeParseException e) {
            mostrarAlerta(Alert.AlertType.WARNING,
                    "Hora inválida", "Utiliza el formato HH:mm. Ejemplo: 14:30");
            return null;
        }

        String servicio = txtTipoServicio.getText().trim();
        String descripcion = txtDescripcion.getText().trim();

        if (servicio.length() > 80 || descripcion.length() > 255) {
            mostrarAlerta(Alert.AlertType.WARNING,
                    "Validación",
                    "El servicio admite 80 caracteres y la descripción 255.");
            return null;
        }

        CitaTecnica cita = new CitaTecnica();
        cita.setCliente(cbCliente.getValue());
        cita.setFecha(dpFecha.getValue());
        cita.setHora(hora);
        cita.setTipoServicio(servicio);
        cita.setDescripcion(descripcion);
        cita.setEstado(cbEstado.getValue());

        return cita;
    }

    @FXML
    private void guardarCita() {
        CitaTecnica cita = obtenerDatos();

        if (cita == null) return;

        try {
            if (citaDAO.guardar(cita)) {
                mostrarAlerta(Alert.AlertType.INFORMATION,
                        "Registro exitoso", "La cita fue guardada correctamente.");
                cargarCitas();
                limpiarFormulario();
            }
        } catch (SQLException e) {
            mostrarAlerta(Alert.AlertType.ERROR,
                    "Error al guardar", e.getMessage());
        }
    }

    @FXML
    private void actualizarCita() {
        CitaTecnica seleccionada =
                tablaCitas.getSelectionModel().getSelectedItem();

        if (seleccionada == null) {
            mostrarAlerta(Alert.AlertType.WARNING,
                    "Selección requerida", "Selecciona una cita para actualizar.");
            return;
        }

        CitaTecnica cita = obtenerDatos();

        if (cita == null) return;

        cita.setId(seleccionada.getId());

        try {
            if (citaDAO.actualizar(cita)) {
                mostrarAlerta(Alert.AlertType.INFORMATION,
                        "Actualización exitosa", "La cita fue actualizada.");
                cargarCitas();
                limpiarFormulario();
            }
        } catch (SQLException e) {
            mostrarAlerta(Alert.AlertType.ERROR,
                    "Error al actualizar", e.getMessage());
        }
    }

    @FXML
    private void eliminarCita() {
        CitaTecnica seleccionada =
                tablaCitas.getSelectionModel().getSelectedItem();

        if (seleccionada == null) {
            mostrarAlerta(Alert.AlertType.WARNING,
                    "Selección requerida", "Selecciona una cita para eliminar.");
            return;
        }

        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Confirmar eliminación");
        confirmacion.setHeaderText("Eliminar cita técnica");
        confirmacion.setContentText(
                "¿Deseas eliminar la cita seleccionada?");

        if (confirmacion.showAndWait().orElse(ButtonType.CANCEL)
                != ButtonType.OK) {
            return;
        }

        try {
            if (citaDAO.eliminar(seleccionada.getId())) {
                mostrarAlerta(Alert.AlertType.INFORMATION,
                        "Eliminación exitosa", "La cita fue eliminada.");
                cargarCitas();
                limpiarFormulario();
            }
        } catch (SQLException e) {
            mostrarAlerta(Alert.AlertType.ERROR,
                    "Error al eliminar", e.getMessage());
        }
    }

    @FXML
    private void seleccionarCita(MouseEvent event) {
        CitaTecnica cita =
                tablaCitas.getSelectionModel().getSelectedItem();

        if (cita == null) return;

        cbCliente.getItems().stream()
                .filter(cliente -> cliente.getId().equals(
                        cita.getCliente().getId()))
                .findFirst()
                .ifPresent(cbCliente::setValue);

        dpFecha.setValue(cita.getFecha());
        txtHora.setText(cita.getHora().format(formatoHora));
        txtTipoServicio.setText(cita.getTipoServicio());
        cbEstado.setValue(cita.getEstado());
        txtDescripcion.setText(cita.getDescripcion());
    }

    @FXML
    private void filtrarCitas() {
        try {
            ObservableList<CitaTecnica> filtradas =
                    FXCollections.observableArrayList();

            for (CitaTecnica cita : citaDAO.listar()) {
                boolean cumpleEstado = cbFiltroEstado.getValue() == null ||
                        cita.getEstado().equals(cbFiltroEstado.getValue());

                boolean cumpleFecha = dpFiltroFecha.getValue() == null ||
                        cita.getFecha().equals(dpFiltroFecha.getValue());

                if (cumpleEstado && cumpleFecha) {
                    filtradas.add(cita);
                }
            }

            citas.setAll(filtradas);

        } catch (SQLException e) {
            mostrarAlerta(Alert.AlertType.ERROR,
                    "Error al filtrar", e.getMessage());
        }
    }

    @FXML
    private void limpiarFiltros() {
        cbFiltroEstado.setValue(null);
        dpFiltroFecha.setValue(null);
        cargarCitas();
    }

    @FXML
    private void limpiarFormulario() {
        cbCliente.setValue(null);
        dpFecha.setValue(null);
        txtHora.clear();
        txtTipoServicio.clear();
        cbEstado.setValue(null);
        txtDescripcion.clear();
        tablaCitas.getSelectionModel().clearSelection();
    }

    private void mostrarAlerta(Alert.AlertType tipo,
                               String titulo, String mensaje) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}
