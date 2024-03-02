/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit4TestClass.java to edit this template
 */
package proyectofinalgimnasio;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.lang.reflect.Field;

public class AltaUsuarioTest {

    private AltaUsuario altaUsuario;

    @Before
    public void setUp() {
        altaUsuario = new AltaUsuario();

        try {
            Field nombreField = AltaUsuario.class.getDeclaredField("jTextFieldNombre");
            nombreField.setAccessible(true);
            javax.swing.JTextField jTextFieldNombre = new javax.swing.JTextField();
            nombreField.set(altaUsuario, jTextFieldNombre);

            Field apellidosField = AltaUsuario.class.getDeclaredField("jTextFieldApellidos");
            apellidosField.setAccessible(true);
            javax.swing.JTextField jTextFieldApellidos = new javax.swing.JTextField();
            apellidosField.set(altaUsuario, jTextFieldApellidos);

            Field telefonoField = AltaUsuario.class.getDeclaredField("jTextFieldTelefono");
            telefonoField.setAccessible(true);
            javax.swing.JTextField jTextFieldTelefono = new javax.swing.JTextField();
            telefonoField.set(altaUsuario, jTextFieldTelefono);

            Field emailField = AltaUsuario.class.getDeclaredField("jTextFieldEmail");
            emailField.setAccessible(true);
            javax.swing.JTextField jTextFieldEmail = new javax.swing.JTextField();
            emailField.set(altaUsuario, jTextFieldEmail);

            Field fechaNacimientoField = AltaUsuario.class.getDeclaredField("jTextFieldFechaNacimiento");
            fechaNacimientoField.setAccessible(true);
            javax.swing.JTextField jTextFieldFechaNacimiento = new javax.swing.JTextField();
            fechaNacimientoField.set(altaUsuario, jTextFieldFechaNacimiento);

            Field dniField = AltaUsuario.class.getDeclaredField("jTextFieldDNI");
            dniField.setAccessible(true);
            javax.swing.JTextField jTextFieldDNI = new javax.swing.JTextField();
            dniField.set(altaUsuario, jTextFieldDNI);
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException ex) {
            ex.printStackTrace();
        }
    }

    @Test
    public void testVerificarDatos() throws Exception {
        Field nombreField = AltaUsuario.class.getDeclaredField("jTextFieldNombre");
        nombreField.setAccessible(true);

        javax.swing.JTextField jTextFieldNombre = (javax.swing.JTextField) nombreField.get(altaUsuario);
        jTextFieldNombre.setText("Juan");

        Field apellidosField = AltaUsuario.class.getDeclaredField("jTextFieldApellidos");
        apellidosField.setAccessible(true);

        javax.swing.JTextField jTextFieldApellidos = (javax.swing.JTextField) apellidosField.get(altaUsuario);
        jTextFieldApellidos.setText("González");

        Field telefonoField = AltaUsuario.class.getDeclaredField("jTextFieldTelefono");
        telefonoField.setAccessible(true);

        javax.swing.JTextField jTextFieldTelefono = (javax.swing.JTextField) telefonoField.get(altaUsuario);
        jTextFieldTelefono.setText("123456789");

        // Acceder al campo privado jTextFieldEmail
        Field emailField = AltaUsuario.class.getDeclaredField("jTextFieldEmail");
        emailField.setAccessible(true);

        javax.swing.JTextField jTextFieldEmail = (javax.swing.JTextField) emailField.get(altaUsuario);
        jTextFieldEmail.setText("juan@example.com");

        Field fechaNacimientoField = AltaUsuario.class.getDeclaredField("jTextFieldFechaNacimiento");
        fechaNacimientoField.setAccessible(true);

        javax.swing.JTextField jTextFieldFechaNacimiento = (javax.swing.JTextField) fechaNacimientoField.get(altaUsuario);
        jTextFieldFechaNacimiento.setText("01/01/2000");

        Field dniField = AltaUsuario.class.getDeclaredField("jTextFieldDNI");
        dniField.setAccessible(true);

        javax.swing.JTextField jTextFieldDNI = (javax.swing.JTextField) dniField.get(altaUsuario);
        jTextFieldDNI.setText("12345678A");

        assertTrue(altaUsuario.verificarDatos());
    }
    
    @Test
    public void testVerificarDatos_DatosIncorrectos() throws Exception {
        
        Field nombreField = AltaUsuario.class.getDeclaredField("jTextFieldNombre");
        nombreField.setAccessible(true);
        
        javax.swing.JTextField jTextFieldNombre = (javax.swing.JTextField) nombreField.get(altaUsuario);
        jTextFieldNombre.setText("12345"); // Nombre no válido

        Field apellidosField = AltaUsuario.class.getDeclaredField("jTextFieldApellidos");
        apellidosField.setAccessible(true);

        javax.swing.JTextField jTextFieldApellidos = (javax.swing.JTextField) apellidosField.get(altaUsuario);
        jTextFieldApellidos.setText("12345"); // Apellidos no válidos

        Field telefonoField = AltaUsuario.class.getDeclaredField("jTextFieldTelefono");
        telefonoField.setAccessible(true);

        javax.swing.JTextField jTextFieldTelefono = (javax.swing.JTextField) telefonoField.get(altaUsuario);
        jTextFieldTelefono.setText("abc"); // Teléfono no válido

        Field emailField = AltaUsuario.class.getDeclaredField("jTextFieldEmail");
        emailField.setAccessible(true);

        javax.swing.JTextField jTextFieldEmail = (javax.swing.JTextField) emailField.get(altaUsuario);
        jTextFieldEmail.setText("correo.invalido"); 

        Field fechaNacimientoField = AltaUsuario.class.getDeclaredField("jTextFieldFechaNacimiento");
        fechaNacimientoField.setAccessible(true);

        javax.swing.JTextField jTextFieldFechaNacimiento = (javax.swing.JTextField) fechaNacimientoField.get(altaUsuario);
        jTextFieldFechaNacimiento.setText("31/02/2000"); // Fecha de nacimiento no válida

        Field dniField = AltaUsuario.class.getDeclaredField("jTextFieldDNI");
        dniField.setAccessible(true);

        javax.swing.JTextField jTextFieldDNI = (javax.swing.JTextField) dniField.get(altaUsuario);
        jTextFieldDNI.setText("1234567XX");

        assertFalse(altaUsuario.verificarDatos());
    }


    @Test
    public void testVerificarFecha() {
        //Realiza la prueba para una fecha de nacimiento válida
        assertTrue(altaUsuario.verificarFecha("01/01/2000"));

        //Realiza la prueba para una fecha de nacimiento incorrecta
        assertFalse(altaUsuario.verificarFecha("32/13/2000"));
    }

    @Test
    public void testEsAnioBisiesto() {
        // Realiza la prueba para un año bisiesto conocido
        assertTrue(altaUsuario.esAnioBisiesto(2024));

        // Realiza la prueba para un año no bisiesto conocido
        assertFalse(altaUsuario.esAnioBisiesto(2023));
    }
}
