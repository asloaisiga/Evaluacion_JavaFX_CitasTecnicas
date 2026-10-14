
package ni.uam.edu.tecnoasistencia.dao;

import ni.uam.edu.tecnoasistencia.models.CitaTecnica;
import ni.uam.edu.tecnoasistencia.models.Cliente;
import ni.uam.edu.tecnoasistencia.util.conexionBD;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CitaTecnicaDAO {

    public boolean guardar(CitaTecnica cita) throws SQLException {

        String sql = """
                INSERT INTO cita_tecnica
                (cliente_id, fecha, hora, tipo_servicio, descripcion, estado)
                VALUES (?, ?, ?, ?, ?, ?)
                """;

        try (Connection conexion = conexionBD.getConnection();
             PreparedStatement ps = conexion.prepareStatement(
                     sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, cita.getCliente().getId());
            ps.setDate(2, Date.valueOf(cita.getFecha()));
            ps.setTime(3, Time.valueOf(cita.getHora()));
            ps.setString(4, cita.getTipoServicio());
            ps.setString(5, cita.getDescripcion());
            ps.setString(6, cita.getEstado());

            int filas = ps.executeUpdate();

            if (filas > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        cita.setId(rs.getInt(1));
                    }
                }
                return true;
            }

            return false;
        }
    }

    public List<CitaTecnica> listar() throws SQLException {

        List<CitaTecnica> citas = new ArrayList<>();

        String sql = """
                SELECT
                    ct.id,
                    ct.fecha,
                    ct.hora,
                    ct.tipo_servicio,
                    ct.descripcion,
                    ct.estado,
                    c.id AS cliente_id,
                    c.nombres,
                    c.apellidos,
                    c.telefono,
                    c.correo,
                    c.activo
                FROM cita_tecnica ct
                INNER JOIN cliente c ON ct.cliente_id = c.id
                ORDER BY ct.id
                """;

        try (Connection conexion = conexionBD.getConnection();
             PreparedStatement ps = conexion.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                Cliente cliente = new Cliente();

                cliente.setId(rs.getInt("cliente_id"));
                cliente.setNombres(rs.getString("nombres"));
                cliente.setApellidos(rs.getString("apellidos"));
                cliente.setTelefono(rs.getString("telefono"));
                cliente.setCorreo(rs.getString("correo"));
                cliente.setActivo(rs.getBoolean("activo"));

                CitaTecnica cita = new CitaTecnica();

                cita.setId(rs.getInt("id"));
                cita.setCliente(cliente);
                cita.setFecha(rs.getDate("fecha").toLocalDate());
                cita.setHora(rs.getTime("hora").toLocalTime());
                cita.setTipoServicio(rs.getString("tipo_servicio"));
                cita.setDescripcion(rs.getString("descripcion"));
                cita.setEstado(rs.getString("estado"));

                citas.add(cita);
            }
        }

        return citas;
    }

    public boolean actualizar(CitaTecnica cita) throws SQLException {

        String sql = """
                UPDATE cita_tecnica
                SET cliente_id = ?,
                    fecha = ?,
                    hora = ?,
                    tipo_servicio = ?,
                    descripcion = ?,
                    estado = ?
                WHERE id = ?
                """;

        try (Connection conexion = conexionBD.getConnection();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, cita.getCliente().getId());
            ps.setDate(2, Date.valueOf(cita.getFecha()));
            ps.setTime(3, Time.valueOf(cita.getHora()));
            ps.setString(4, cita.getTipoServicio());
            ps.setString(5, cita.getDescripcion());
            ps.setString(6, cita.getEstado());
            ps.setInt(7, cita.getId());

            return ps.executeUpdate() > 0;
        }
    }

    public boolean eliminar(int id) throws SQLException {

        String sql = """
                DELETE FROM cita_tecnica
                WHERE id = ?
                """;

        try (Connection conexion = conexionBD.getConnection();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, id);

            return ps.executeUpdate() > 0;
        }
    }
}
