/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit4TestClass.java to edit this template
 */
package proyectofinalgimnasio;

import static org.junit.Assert.*;
import org.junit.Test;
import java.sql.SQLException;


/**
 *
 * @author Sebastián Melgar Marín
 */
public class BuscarUsuariosTest {
    
   @Test
    public void testMostrarUltimosRegistros() throws SQLException {
        BuscarUsuarios buscarUsuarios = new BuscarUsuarios(null, false);
        try {
            buscarUsuarios.mostrarUltimosRegistros();
            //Si no se produce una excepción, el test pasa
            assertTrue(true);
        } catch (SQLException ex) {
            //Si se produce una excepción, el test falla
            fail("Error al mostrar últimos registros: " + ex.getMessage());
        }
    }

    @Test
    public void testBuscarUsuarios() throws SQLException {
        BuscarUsuarios buscarUsuarios = new BuscarUsuarios(null, false);
        try {
            //Probamos la búsqueda por nombre
            buscarUsuarios.buscarUsuarios("nombre", "Juan");
            //Si no se produce una excepción, el test pasa
            assertTrue(true);
        } catch (SQLException ex) {
            //Si se produce una excepción, el test falla
            fail("Error al buscar usuarios: " + ex.getMessage());
        }
    }
    
}
