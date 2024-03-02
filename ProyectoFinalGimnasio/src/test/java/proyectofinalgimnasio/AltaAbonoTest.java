/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit4TestClass.java to edit this template
 */
package proyectofinalgimnasio;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class AltaAbonoTest {

    private AltaAbono altaAbono;

    @Before
    public void setUp() {
        altaAbono = new AltaAbono();
    }

    @Test
    public void testVerificarFecha_FechaCorrecta() {
        assertTrue(altaAbono.verificarFecha("01/03/2024"));
    }

    @Test
    public void testVerificarFecha_FechaIncorrecta() {
        assertFalse(altaAbono.verificarFecha("32/03/2024"));
    }


    @Test
    public void testEsAnioBisiesto_AnioBisiesto() {
        assertTrue(altaAbono.esAnioBisiesto(2024));
    }

    @Test
    public void testEsAnioBisiesto_AnioNoBisiesto() {
        assertFalse(altaAbono.esAnioBisiesto(2023));
    }
   
}
