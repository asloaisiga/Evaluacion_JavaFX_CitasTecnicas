package ni.uam.edu.tecnoasistencia.dao;

import ni.uam.edu.tecnoasistencia.models.Cliente;
import ni.uam.edu.tecnoasistencia.util.conexionBD;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAO {

    public boolean guardar(Cliente cliente)
            throws SQLException {

        String sql = """
                INSERT INTO cliente
                (
                    nombres,
                    apellidos,
                    telefono,
                    correo,
                    activo
                )
                VALUES (?, ?, ?, ?, ?)
                """;

        try (
                Connection conexion =
                        conexionBD.getConnection();

                PreparedStatement ps =
                        conexion.prepareStatement(
                                sql,
                                Statement.RETURN_GENERATED_KEYS
                        )
        ) {

            ps.setString(
                    1,
                    cliente.getNombres()
            );

            ps.setString(
                    2,
                    cliente.getApellidos()
            );

            ps.setString(
                    3,
                    cliente.getTelefono()
            );

            ps.setString(
                    4,
                    cliente.getCorreo()
            );

            ps.setBoolean(
                    5,
                    cliente.isActivo()
            );

            int filas =
                    ps.executeUpdate();

            if (filas > 0) {

                try (
                        ResultSet rs =
                                ps.getGeneratedKeys()
                ) {

                    if (rs.next()) {
                        cliente.setId(
                                rs.getInt(1)
                        );
                    }
                }

                return true;
            }
        }

        return false;
    }


    public List<Cliente> listar()
            throws SQLException {

        List<Cliente> clientes =
                new ArrayList<>();

        String sql = """
                SELECT
                    id,
                    nombres,
                    apellidos,
                    telefono,
                    correo,
                    activo
                FROM cliente
                ORDER BY id
                """;

        try (
                Connection conexion =
                        conexionBD.getConnection();

                PreparedStatement ps =
                        conexion.prepareStatement(sql);

                ResultSet rs =
                        ps.executeQuery()
        ) {

            while (rs.next()) {

                Cliente cliente =
                        new Cliente();

                cliente.setId(
                        rs.getInt("id")
                );

                cliente.setNombres(
                        rs.getString("nombres")
                );

                cliente.setApellidos(
                        rs.getString("apellidos")
                );

                cliente.setTelefono(
                        rs.getString("telefono")
                );

                cliente.setCorreo(
                        rs.getString("correo")
                );

                cliente.setActivo(
                        rs.getBoolean("activo")
                );

                clientes.add(
                        cliente
                );
            }
        }

        return clientes;
    }


    public boolean actualizar(Cliente cliente)
            throws SQLException {

        String sql = """
                UPDATE cliente
                SET nombres = ?,
                    apellidos = ?,
                    telefono = ?,
                    correo = ?,
                    activo = ?
                WHERE id = ?
                """;

        try (
                Connection conexion =
                        conexionBD.getConnection();

                PreparedStatement ps =
                        conexion.prepareStatement(sql)
        ) {

            ps.setString(
                    1,
                    cliente.getNombres()
            );

            ps.setString(
                    2,
                    cliente.getApellidos()
            );

            ps.setString(
                    3,
                    cliente.getTelefono()
            );

            ps.setString(
                    4,
                    cliente.getCorreo()
            );

            ps.setBoolean(
                    5,
                    cliente.isActivo()
            );

            ps.setInt(
                    6,
                    cliente.getId()
            );

            return ps.executeUpdate() > 0;
        }
    }


    public boolean eliminar(int id)
            throws SQLException {

        String sql = """
                DELETE FROM cliente
                WHERE id = ?
                """;

        try (
                Connection conexion =
                        conexionBD.getConnection();

                PreparedStatement ps =
                        conexion.prepareStatement(sql)
        ) {

            ps.setInt(
                    1,
                    id
            );

            return ps.executeUpdate() > 0;
        }
    }


    public boolean existeCorreo(String correo)
            throws SQLException {

        String sql = """
                SELECT COUNT(*)
                FROM cliente
                WHERE LOWER(correo) = LOWER(?)
                """;

        try (
                Connection conexion =
                        conexionBD.getConnection();

                PreparedStatement ps =
                        conexion.prepareStatement(sql)
        ) {

            ps.setString(
                    1,
                    correo
            );

            try (
                    ResultSet rs =
                            ps.executeQuery()
            ) {

                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }

        return false;
    }

    public List<Cliente> buscarPorNombre(String texto) throws SQLException {

        List<Cliente> clientes = new ArrayList<>();

        String sql = """
            SELECT id, nombres, apellidos, telefono, correo, activo
            FROM cliente
            WHERE nombres ILIKE ?
               OR apellidos ILIKE ?
               OR CONCAT(nombres, ' ', apellidos) ILIKE ?
            ORDER BY nombres, apellidos
            LIMIT 20
            """;

        try (Connection connection = conexionBD.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            String busqueda = "%" + texto.trim() + "%";

            ps.setString(1, busqueda);
            ps.setString(2, busqueda);
            ps.setString(3, busqueda);

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    Cliente cliente = new Cliente();
                    cliente.setId(rs.getInt("id"));
                    cliente.setNombres(rs.getString("nombres"));
                    cliente.setApellidos(rs.getString("apellidos"));
                    cliente.setTelefono(rs.getString("telefono"));
                    cliente.setCorreo(rs.getString("correo"));
                    cliente.setActivo(rs.getBoolean("activo"));
                    clientes.add(cliente);
                }
            }
        }

        return clientes;
    }


    public boolean existeCorreoEnOtroCliente(String correo, int id) throws SQLException {

        String sql = """
            SELECT COUNT(*)
            FROM cliente
            WHERE LOWER(correo) = LOWER(?)
            AND id <> ?
            """;

        try (Connection conexion = conexionBD.getConnection();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, correo);
            ps.setInt(2, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }

        return false;
    }


    public boolean tieneCitasAsociadas(int id) throws SQLException {

        String sql = """
            SELECT COUNT(*)
            FROM cita_tecnica
            WHERE cliente_id = ?
            """;

        try (Connection conexion = conexionBD.getConnection();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }

        return false;
    }

}