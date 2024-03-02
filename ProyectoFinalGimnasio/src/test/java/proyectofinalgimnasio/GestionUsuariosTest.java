package proyectofinalgimnasio;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class GestionUsuariosTest {

    private GestionUsuarios gestionUsuarios;
    private String nombreInput;
    private String apellidosInput;
    private String telefonoInput;
    private String emailInput;
    private String fechaNacimientoInput;
    private boolean expectedResult;

    @Before
    public void setUp() {
        gestionUsuarios = new GestionUsuarios();

        //Vamos a inicializar manualmente los campos JTextField necesarios para las pruebas
        try {
            Field nombreField = GestionUsuarios.class.getDeclaredField("jTextFieldNombre");
            nombreField.setAccessible(true);
            javax.swing.JTextField jTextFieldNombre = new javax.swing.JTextField();
            nombreField.set(gestionUsuarios, jTextFieldNombre);

            Field apellidosField = GestionUsuarios.class.getDeclaredField("jTextFieldApellidos");
            apellidosField.setAccessible(true);
            javax.swing.JTextField jTextFieldApellidos = new javax.swing.JTextField();
            apellidosField.set(gestionUsuarios, jTextFieldApellidos);

            Field telefonoField = GestionUsuarios.class.getDeclaredField("jTextFieldTelefono");
            telefonoField.setAccessible(true);
            javax.swing.JTextField jTextFieldTelefono = new javax.swing.JTextField();
            telefonoField.set(gestionUsuarios, jTextFieldTelefono);

            Field emailField = GestionUsuarios.class.getDeclaredField("jTextFieldEmail");
            emailField.setAccessible(true);
            javax.swing.JTextField jTextFieldEmail = new javax.swing.JTextField();
            emailField.set(gestionUsuarios, jTextFieldEmail);

            Field fechaNacimientoField = GestionUsuarios.class.getDeclaredField("jTextFieldFechaNacimiento");
            fechaNacimientoField.setAccessible(true);
            javax.swing.JTextField jTextFieldFechaNacimiento = new javax.swing.JTextField();
            fechaNacimientoField.set(gestionUsuarios, jTextFieldFechaNacimiento);
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException ex) {
            ex.printStackTrace();
        }
    }

    // Constructor para inicializar los datos de entrada y salida esperada
    public GestionUsuariosTest(String nombreInput, String apellidosInput, String telefonoInput, String emailInput, String fechaNacimientoInput, boolean expectedResult) {
        this.nombreInput = nombreInput;
        this.apellidosInput = apellidosInput;
        this.telefonoInput = telefonoInput;
        this.emailInput = emailInput;
        this.fechaNacimientoInput = fechaNacimientoInput;
        this.expectedResult = expectedResult;
    }

    // Método que proporciona los datos para las pruebas
    @Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
            {"Juan", "Perez", "123456789", "juan@example.com", "01/01/1990", true},
            {"Maria", "Lopez", "12345", "maria@example.com", "01/01/1990", false},
            {"123", "Gomez", "123456789", "gomez@example.com", "01/01/1990", false},
            {"Pedro", "123", "123456789", "pedro@example.com", "01/01/1990", false},
            {"Ana", "Garcia", "123456780", "ana@", "01/01/1990", false},
            {"Luis", "Hernandez", "123456789", "luis@example.com", "29/02/2024", true},
            {"Luis", "Hernandez", "123456789", "luis@example.com", "29/02/2023", false},
            {"Elena", "Martinez", "", "elena@example.com", "01/01/1990", false},
            {"Carlos", "Fernandez", "123456789", "carlos@example.com", "32/01/1990", false}
        });
    }

    @Test
    public void testVerificarDatosUsuario() {
            gestionUsuarios.setNombreTextField(nombreInput);
            gestionUsuarios.setApellidosTextField(apellidosInput);
            gestionUsuarios.setTelefonoTextField(telefonoInput);
            gestionUsuarios.setEmailTextField(emailInput);
            gestionUsuarios.setFechaNacimientoTextField(fechaNacimientoInput);

        boolean actualResult = gestionUsuarios.verificarDatosUsuario();

        // Verificar si el resultado obtenido coincide con el esperado
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testVerificarFecha() {
        // Realizar la prueba con una fecha válida
        assertTrue(verificarFecha("01/01/2000"));

        // Realizar la prueba con una fecha inválida
        assertFalse(verificarFecha("32/01/2000"));
    }

    // Método auxiliar para verificar la fecha
    private boolean verificarFecha(String fecha) {
        String regexFecha = "\\d{2}/\\d{2}/\\d{4}";
        Pattern pattern = Pattern.compile(regexFecha);
        Matcher matcher = pattern.matcher(fecha);
        if (matcher.matches()) {
            int dia = Integer.parseInt(fecha.substring(0, 2));
            int mes = Integer.parseInt(fecha.substring(3, 5));
            int anio = Integer.parseInt(fecha.substring(6, 10));
            return dia > 0 && dia <= 31 && mes > 0 && mes <= 12 && anio >= 1900;
        } else {
            return false;
        }
    }

    @Test
    public void testCalcularPrecioMensual() {
        // Realizar la prueba con un precio total y duración de meses conocidos
        double precioTotal = 120.0;
        int duracionMeses = 6;
        double precioMensual = gestionUsuarios.calcularPrecioMensual(precioTotal, duracionMeses);
        assertEquals(20.0, precioMensual, 0.0);
    }

    @Test
    public void testEsAnioBisiesto() {
        // Realizar la prueba para un año bisiesto conocido
        assertTrue(gestionUsuarios.esAnioBisiesto(2024));
        
        // Realizar la prueba para un año no bisiesto conocido
        assertFalse(gestionUsuarios.esAnioBisiesto(2023));
    }
}
