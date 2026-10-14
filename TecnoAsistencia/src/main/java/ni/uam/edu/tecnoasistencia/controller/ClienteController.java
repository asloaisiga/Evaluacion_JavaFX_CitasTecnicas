package ni.uam.edu.tecnoasistencia.controller;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import ni.uam.edu.tecnoasistencia.dao.ClienteDAO;
import ni.uam.edu.tecnoasistencia.models.Cliente;

import java.sql.SQLException;

public class ClienteController {

    @FXML private TextField txtNombres;
    @FXML private TextField txtApellidos;
    @FXML private TextField txtTelefono;
    @FXML private TextField txtCorreo;
    @FXML private TextField txtBuscar;
    @FXML private CheckBox chkActivo;

    @FXML private TableView<Cliente> tablaClientes;
    @FXML private TableColumn<Cliente, Integer> colId;
    @FXML private TableColumn<Cliente, String> colNombres;
    @FXML private TableColumn<Cliente, String> colApellidos;
    @FXML private TableColumn<Cliente, String> colTelefono;
    @FXML private TableColumn<Cliente, String> colCorreo;
    @FXML private TableColumn<Cliente, String> colActivo;

    private final ClienteDAO clienteDAO = new ClienteDAO();
    private final ObservableList<Cliente> clientes = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        colId.setCellValueFactory(data ->
                new SimpleObjectProperty<>(data.getValue().getId()));

        colNombres.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getNombres()));

        colApellidos.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getApellidos()));

        colTelefono.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getTelefono()));

        colCorreo.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getCorreo()));

        colActivo.setCellValueFactory(data ->
                new SimpleStringProperty(
                        data.getValue().isActivo() ? "Sí" : "No"));

        tablaClientes.setItems(clientes);
        cargarClientes();
    }

    private void cargarClientes() {
        try {
            clientes.setAll(clienteDAO.listar());
        } catch (SQLException e) {
            mostrarAlerta(Alert.AlertType.ERROR,
                    "Error al cargar clientes", e.getMessage());
        }
    }

    private Cliente obtenerDatos() {
        String nombres = txtNombres.getText().trim();
        String apellidos = txtApellidos.getText().trim();
        String telefono = txtTelefono.getText().trim();
        String correo = txtCorreo.getText().trim();

        if (nombres.isEmpty() || apellidos.isEmpty() ||
                telefono.isEmpty() || correo.isEmpty()) {
            mostrarAlerta(Alert.AlertType.WARNING,
                    "Validación", "Todos los campos son obligatorios.");
            return null;
        }

        if (nombres.length() > 80 || apellidos.length() > 80 ||
                telefono.length() > 20 || correo.length() > 120) {
            mostrarAlerta(Alert.AlertType.WARNING,
                    "Validación", "Uno de los campos supera la longitud permitida.");
            return null;
        }

        if (!correo.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
            mostrarAlerta(Alert.AlertType.WARNING,
                    "Correo inválido", "Ingresa un correo electrónico válido.");
            return null;
        }

        Cliente cliente = new Cliente();
        cliente.setNombres(nombres);
        cliente.setApellidos(apellidos);
        cliente.setTelefono(telefono);
        cliente.setCorreo(correo);
        cliente.setActivo(chkActivo.isSelected());

        return cliente;
    }

    @FXML
    private void guardarCliente() {
        Cliente cliente = obtenerDatos();

        if (cliente == null) return;

        try {
            if (clienteDAO.existeCorreo(cliente.getCorreo())) {
                mostrarAlerta(Alert.AlertType.WARNING,
                        "Correo duplicado", "Este correo ya está registrado.");
                return;
            }

            if (clienteDAO.guardar(cliente)) {
                mostrarAlerta(Alert.AlertType.INFORMATION,
                        "Registro exitoso", "El cliente fue registrado correctamente.");
                cargarClientes();
                limpiarFormulario();
            }

        } catch (SQLException e) {
            mostrarAlerta(Alert.AlertType.ERROR,
                    "Error al guardar", e.getMessage());
        }
    }

    @FXML
    private void actualizarCliente() {
        Cliente seleccionado = tablaClientes.getSelectionModel().getSelectedItem();

        if (seleccionado == null) {
            mostrarAlerta(Alert.AlertType.WARNING,
                    "Selección requerida", "Selecciona un cliente para actualizar.");
            return;
        }

        Cliente cliente = obtenerDatos();

        if (cliente == null) return;

        cliente.setId(seleccionado.getId());

        try {
            if (clienteDAO.existeCorreoEnOtroCliente(
                    cliente.getCorreo(), cliente.getId())) {
                mostrarAlerta(Alert.AlertType.WARNING,
                        "Correo duplicado", "Este correo pertenece a otro cliente.");
                return;
            }

            if (clienteDAO.actualizar(cliente)) {
                mostrarAlerta(Alert.AlertType.INFORMATION,
                        "Actualización exitosa", "El cliente fue actualizado.");
                cargarClientes();
                limpiarFormulario();
            }

        } catch (SQLException e) {
            mostrarAlerta(Alert.AlertType.ERROR,
                    "Error al actualizar", e.getMessage());
        }
    }

    @FXML
    private void eliminarCliente() {
        Cliente seleccionado = tablaClientes.getSelectionModel().getSelectedItem();

        if (seleccionado == null) {
            mostrarAlerta(Alert.AlertType.WARNING,
                    "Selección requerida", "Selecciona un cliente para eliminar.");
            return;
        }

        try {
            if (clienteDAO.tieneCitasAsociadas(seleccionado.getId())) {
                mostrarAlerta(Alert.AlertType.WARNING,
                        "Eliminación restringida",
                        "No puedes eliminar este cliente porque tiene citas asociadas.");
                return;
            }

            Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
            confirmacion.setTitle("Confirmar eliminación");
            confirmacion.setHeaderText("Eliminar cliente");
            confirmacion.setContentText("¿Deseas eliminar el cliente seleccionado?");

            if (confirmacion.showAndWait().orElse(ButtonType.CANCEL)
                    != ButtonType.OK) {
                return;
            }

            if (clienteDAO.eliminar(seleccionado.getId())) {
                mostrarAlerta(Alert.AlertType.INFORMATION,
                        "Eliminación exitosa", "El cliente fue eliminado.");
                cargarClientes();
                limpiarFormulario();
            }

        } catch (SQLException e) {
            mostrarAlerta(Alert.AlertType.ERROR,
                    "Error al eliminar", e.getMessage());
        }
    }

    @FXML
    private void seleccionarCliente(MouseEvent event) {
        Cliente cliente = tablaClientes.getSelectionModel().getSelectedItem();

        if (cliente == null) return;

        txtNombres.setText(cliente.getNombres());
        txtApellidos.setText(cliente.getApellidos());
        txtTelefono.setText(cliente.getTelefono());
        txtCorreo.setText(cliente.getCorreo());
        chkActivo.setSelected(cliente.isActivo());
    }

    @FXML
    private void buscarClientes() {
        String texto = txtBuscar.getText().trim().toLowerCase();

        if (texto.isEmpty()) {
            cargarClientes();
            return;
        }

        try {
            ObservableList<Cliente> filtrados = FXCollections.observableArrayList();

            for (Cliente cliente : clienteDAO.listar()) {
                if (cliente.getNombres().toLowerCase().contains(texto) ||
                        cliente.getApellidos().toLowerCase().contains(texto) ||
                        cliente.getCorreo().toLowerCase().contains(texto)) {
                    filtrados.add(cliente);
                }
            }

            clientes.setAll(filtrados);

        } catch (SQLException e) {
            mostrarAlerta(Alert.AlertType.ERROR,
                    "Error al buscar", e.getMessage());
        }
    }

    @FXML
    private void limpiarBusqueda() {
        txtBuscar.clear();
        cargarClientes();
    }

    @FXML
    private void limpiarFormulario() {
        txtNombres.clear();
        txtApellidos.clear();
        txtTelefono.clear();
        txtCorreo.clear();
        chkActivo.setSelected(true);
        tablaClientes.getSelectionModel().clearSelection();
    }

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensaje) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}
