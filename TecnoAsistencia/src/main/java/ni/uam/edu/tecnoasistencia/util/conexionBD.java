package ni.uam.edu.tecnoasistencia.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class conexionBD {

    private static final String URL =
            "jdbc:postgresql://localhost:5432/gestion_citas";

    private static final String USER =
            "postgres";

    private static final String PASSWORD =
            System.getenv("DB_PASSWORD");

    public static Connection getConnection()
            throws SQLException {

        return DriverManager.getConnection(
                URL,
                USER,
                PASSWORD
        );
    }

    public static boolean probarConexion() {

        String sql = "SELECT 1";

        try (
                Connection connection =
                        getConnection();

                Statement statement =
                        connection.createStatement()
        ) {

            statement.executeQuery(sql);

            return true;

        } catch (SQLException e) {

            System.err.println(
                    "Error de conexión: "
                            + e.getMessage()
            );

            return false;
        }
    }
}
