/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectofinalgimnasio;

/**
 *
 * @author Sebastián Melgar Marín
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class ConexionBD {
    
    //private static final String URL = "jdbc:mysql://localhost:3306/gimnasio";
    //private static final String USUARIO = "root";
    //private static final String CONTRASENA = "root";

    private static final String URL = "jdbc:mysql://sql11.freesqldatabase.com:3306/sql11688015";
    private static final String USUARIO = "sql11688015";
    private static final String CONTRASENA = "SYUzQhgjF4";

    /**
     *
     * @return
     * @throws SQLException
     */
    public static Connection obtenerConexion() throws SQLException {
        return DriverManager.getConnection(URL, USUARIO, CONTRASENA);
    }

    /**
     *
     * @param conexion
     */
    public static void cerrarConexion(Connection conexion) {
            try {
                if (conexion != null && !conexion.isClosed()) {
                    conexion.close();
                    System.out.println("Conexión cerrada correctamente."); 
                }
            } catch (SQLException e) {
                System.err.println("Error al cerrar la conexión: " + e.getMessage());
            }
        }
}
