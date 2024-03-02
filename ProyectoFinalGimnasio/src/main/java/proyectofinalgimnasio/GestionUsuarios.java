
package proyectofinalgimnasio;

import java.awt.Toolkit;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author Sebastián Melgar Marín
 */
public class GestionUsuarios extends javax.swing.JFrame implements UsuarioSeleccionadoListener {
    private double precioTotal;
    
    /**
     *
     * @param texto
     */
    public void setNombreTextField(String texto) {
        jTextFieldNombre.setText(texto);
    }

    /**
     *
     * @param texto
     */
    public void setApellidosTextField(String texto) {
        jTextFieldApellidos.setText(texto);
    }

    /**
     *
     * @param texto
     */
    public void setTelefonoTextField(String texto) {
        jTextFieldTelefono.setText(texto);
    }

    /**
     *
     * @param texto
     */
    public void setEmailTextField(String texto) {
        jTextFieldEmail.setText(texto);
    }

    /**
     *
     * @param texto
     */
    public void setFechaNacimientoTextField(String texto) {
        jTextFieldFechaNacimiento.setText(texto);
    }

    /**
     *
     */
    public GestionUsuarios() {
        initComponents();
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("icono.png")));
        setTitle("Gimnasio - Gestión");
        jButtonRenovarAbono.addActionListener((ActionEvent e) -> {
            verificarDatosAbono();
        });

        jComboBoxPrecioMensual.addActionListener((ActionEvent e) -> {
            calcularPrecioTotal();
        });

        jComboBoxDescuento.addActionListener((ActionEvent e) -> {
            calcularPrecioTotal();
        });

        jCheckBox1.addActionListener((ActionEvent e) -> {
            calcularPrecioTotal();
        });

        jTextFieldMeses.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                calcularPrecioTotal();
            }
        });

        jTextFieldFechaInicio.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                verificarFechaInicio();
            }
        });
    }
    
    private void verificarDatosAbono() {
        String precioMensual = (String) jComboBoxPrecioMensual.getSelectedItem();
        String descuento = (String) jComboBoxDescuento.getSelectedItem();
        String numeroMeses = jTextFieldMeses.getText();
        String fechaInicio = jTextFieldFechaInicio.getText();
        boolean camposCorrectos = true;

        Pattern pattern = Pattern.compile("^(?:[1-9]|1[0-2])$");
        Matcher matcher = pattern.matcher(numeroMeses);

        if (!matcher.matches()) {
            jLabelVerificadorMeses.setText("Mínimo 1 mes. Máximo 12 meses.");
            camposCorrectos = false;
        } else {
            jLabelVerificadorMeses.setText("");
        }

        LocalDate fechaActual = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        try {
            LocalDate fechaInicioParsed = LocalDate.parse(fechaInicio, formatter);
            if (fechaInicioParsed.isBefore(fechaActual)) {
                jLabelVerificadorFechaInicio.setText("Fecha incorrecta");
                camposCorrectos = false;
            } else {
                jLabelVerificadorFechaInicio.setText("");
            }
        } catch (Exception e) {
            jLabelVerificadorFechaInicio.setText("Fecha incorrecta");
            camposCorrectos = false;
        }

        double precioMensualDouble = Double.parseDouble(precioMensual);
        int numeroMesesInt = Integer.parseInt(numeroMeses);
        double total = precioMensualDouble * numeroMesesInt;

        if (jCheckBox1.isSelected()) {
            total += 10 * numeroMesesInt;
        }

        double descuentoDouble = Double.parseDouble(descuento);
        total *= (1 - descuentoDouble / 100);

        jLabelPrecioTotal.setText("Precio total: " + String.format("%.2f", total) + "€");
        
        if (camposCorrectos) {
            String fechaFin = jTextFieldFechaFin.getText();
            if (actualizarAbonoEnBD(jTextFieldDNI.getText(), fechaInicio, fechaFin, total)) {
                System.out.println("Abono actualizado correctamente.");
            } else {
                System.out.println("Error al actualizar el abono en la base de datos.");
            }
        }
    }
    
    /**
     *
     * @return
     */
    public boolean verificarDatosUsuario() {
            String regexNombre = "[A-Za-záéíóúÁÉÍÓÚñÑ\\s]+";
            String regexApellidos = "[A-Za-záéíóúÁÉÍÓÚñÑ\\s]+";
            String regexTelefono = "\\d{9}|\\d{11}";
            String regexEmail = "\\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}\\b";
            
            String nombre = jTextFieldNombre.getText();
            String apellidos = jTextFieldApellidos.getText();
            String telefono = jTextFieldTelefono.getText();
            String email = jTextFieldEmail.getText();
            String fechaNacimiento = jTextFieldFechaNacimiento.getText();
            
            Pattern pattern;
            Matcher matcher;
            boolean camposCorrectos = true;

            pattern = Pattern.compile(regexNombre);
            matcher = pattern.matcher(nombre);
            if (!matcher.matches()) {
                jLabelVerificacionNombre.setText("El nombre debe contener solo letras y espacios");
                camposCorrectos = false;
            } else {
                jLabelVerificacionNombre.setText("");
            }

            pattern = Pattern.compile(regexApellidos);
            matcher = pattern.matcher(apellidos);
            if (!matcher.matches()) {
                jLabelVerificacionApellido.setText("Los apellidos deben contener solo letras y espacios");
                camposCorrectos = false;
            } else {
                jLabelVerificacionApellido.setText("");
            }

            pattern = Pattern.compile(regexTelefono);
            matcher = pattern.matcher(telefono);
            if (!matcher.matches()) {
                jLabelVerificacionTelefono.setText("El teléfono debe contener 9 u 11 dígitos");
                camposCorrectos = false;
            } else {
                jLabelVerificacionTelefono.setText("");
            }

            pattern = Pattern.compile(regexEmail, Pattern.CASE_INSENSITIVE);
            matcher = pattern.matcher(email);
            if (!matcher.matches()) {
                jLabelVerificacionEmail.setText("El email no es válido");
                camposCorrectos = false;
            } else {
                jLabelVerificacionEmail.setText("");
            }

            if (!verificarFecha(fechaNacimiento)) {
                jLabelVerificacionFechaNacimiento.setText("La fecha de nacimiento no es válida");
                camposCorrectos = false;
            } else {
                jLabelVerificacionFechaNacimiento.setText("");
            }

            return camposCorrectos;
    }
    
    /**
     *
     */
    public class FechaFormato {

        /**
         *
         * @param fechaStr
         * @param formatoEntrada
         * @param formatoSalida
         * @return
         */
        public static String convertirFecha(String fechaStr, String formatoEntrada, String formatoSalida) {
            // Convertir de formato de entrada a LocalDate
            DateTimeFormatter formatterEntrada = DateTimeFormatter.ofPattern(formatoEntrada);
            LocalDate fecha = LocalDate.parse(fechaStr, formatterEntrada);

            // Convertir de LocalDate a formato de salida
            DateTimeFormatter formatterSalida = DateTimeFormatter.ofPattern(formatoSalida);
            return fecha.format(formatterSalida);
        }

        /**
         *
         * @param fechaStr
         * @return
         */
        public static String convertirFechaMySQL(String fechaStr) {
            // Convertir de "dd/MM/yyyy" a "yyyy-MM-dd" (formato MySQL)
            return convertirFecha(fechaStr, "dd/MM/yyyy", "yyyy-MM-dd");
        }
    }
    
    /**
     *
     * @param fecha
     * @return
     */
    public boolean verificarFecha(String fecha) {
        // Verificar el formato de la fecha usando una expresión regular
        String regexFecha = "(0[1-9]|[12]\\d|3[01])[-/](0[1-9]|1[0-2])[-/]\\d{4}";
        if (!fecha.matches(regexFecha)) {
            return false;
        }

        String[] partesFecha = fecha.split("[-/]");
        int dia = Integer.parseInt(partesFecha[0]);
        int mes = Integer.parseInt(partesFecha[1]);
        int anio = Integer.parseInt(partesFecha[2]);

        if (mes < 1 || mes > 12) {
            return false;
        }

        int[] diasPorMes = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
        if (mes == 2 && esAnioBisiesto(anio)) {
            diasPorMes[1] = 29;
        }
        return !(dia < 1 || dia > diasPorMes[mes - 1]);
    }
    
    private void verificarFechaInicio() {
        String fechaInicioStr = jTextFieldFechaInicio.getText();

        if (!verificarFecha(fechaInicioStr)) {
            jLabelVerificadorFechaInicio.setText("Fecha incorrecta");
            return;
        }

        jLabelVerificadorFechaInicio.setText("");
        calcularFechaFin(); // Llamar a calcularFechaFin después de verificar la fecha
    }
    
    private void calcularFechaFin() {
            if (!jLabelVerificadorFechaInicio.getText().isEmpty()) {
                return;
            }

            String fechaInicioStr = jTextFieldFechaInicio.getText();
            String numeroMesesStr = jTextFieldMeses.getText();

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate fechaInicio = null;
            try {
                fechaInicio = LocalDate.parse(fechaInicioStr, formatter);
            } catch (Exception e) {
                jLabelVerificadorFechaInicio.setText("Fecha incorrecta");
                return;
            }

            Pattern pattern = Pattern.compile("^(?:[1-9]|1[0-2])$");
            Matcher matcher = pattern.matcher(numeroMesesStr);
            if (!matcher.matches()) {
                jLabelVerificadorMeses.setText("Mínimo 1 mes, máximo 12");
                return;
            }

            int numeroMeses = Integer.parseInt(numeroMesesStr);
            LocalDate fechaFin = fechaInicio.plusMonths(numeroMeses);
            String fechaFinStr = fechaFin.format(formatter);
            jTextFieldFechaFin.setText(fechaFinStr);
        }
    
    /**
     *
     * @param precioTotal
     * @param duracionMeses
     * @return
     */
    public double calcularPrecioMensual(double precioTotal, int duracionMeses) {
        return precioTotal / duracionMeses;
    }

    private void calcularPrecioTotal() {
        String precioMensual = (String) jComboBoxPrecioMensual.getSelectedItem();
        String descuento = (String) jComboBoxDescuento.getSelectedItem();
        String numeroMeses = jTextFieldMeses.getText();
        boolean camposCorrectos = true;
        
        Pattern pattern = Pattern.compile("^(?:[1-9]|1[0-2])$");
        Matcher matcher = pattern.matcher(numeroMeses);
        
        if (!matcher.matches()) {
            jLabelVerificadorMeses.setText("Mínimo 1 mes, máximo 12");
            camposCorrectos = false;
        } else {
            jLabelVerificadorMeses.setText("");
        }
        
        if (camposCorrectos) {
            double precioMensualDouble = Double.parseDouble(precioMensual);
            int numeroMesesInt = Integer.parseInt(numeroMeses);
            double total = precioMensualDouble * numeroMesesInt;

            if (jCheckBox1.isSelected()) {
                total += 10 * numeroMesesInt;
            }

            double descuentoDouble = Double.parseDouble(descuento);
            total *= (1 - descuentoDouble / 100);

            jLabelPrecioTotal.setText(String.format("%.2f", total) + "€");
        }
    }
    
    /**
     *
     * @param anio
     * @return
     */
    public boolean esAnioBisiesto(int anio) {
        return (anio % 4 == 0 && anio % 100 != 0) || (anio % 400 == 0);
    }
    
            
    private String[] obtenerDatosUsuario() {
        String[] datosUsuario = new String[6]; // Arreglo para almacenar los datos del usuario

        datosUsuario[0] = jTextFieldNombre.getText();
        datosUsuario[1] = jTextFieldApellidos.getText();
        datosUsuario[2] = jTextFieldEmail.getText();
        datosUsuario[3] = jTextFieldTelefono.getText();
        datosUsuario[4] = jTextFieldFechaNacimiento.getText();
        datosUsuario[5] = jTextFieldDNI.getText();

    return datosUsuario;
}
   
    private void rellenarDatosUsuario(String dni) {
        try {
            // Define la consulta SQL para obtener los datos del usuario
            try ( // Realiza la conexión a la base de datos
                    Connection conexion = ConexionBD.obtenerConexion()) {
                // Define la consulta SQL para obtener los datos del usuario
                String query = "SELECT * FROM usuarios WHERE DNI = ?";
                
                // Establece el DNI como parámetro en la consulta
                try ( // Prepara la consulta
                        PreparedStatement pstmt = conexion.prepareStatement(query)) {
                    // Establece el DNI como parámetro en la consulta
                    pstmt.setString(1, dni);
                    
                    // Verifica si se encontraron resultados
                    try ( // Ejecuta la consulta
                            ResultSet rs = pstmt.executeQuery()) {
                        // Verifica si se encontraron resultados
                        if (rs.next()) {
                            // Obtiene los datos del usuario de la consulta
                            String nombre = rs.getString("nombre");
                            String apellidos = rs.getString("apellidos");
                            // Convertir la fecha de nacimiento al formato dd/mm/yyyy
                            String fechaNacimientoSQL = rs.getString("fecha_nacimiento");
                            LocalDate fechaNacimiento = LocalDate.parse(fechaNacimientoSQL);
                            String fechaNacimientoFormateada = fechaNacimiento.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                            String telefono = rs.getString("telefono");
                            String email = rs.getString("email");
                            
                            // Rellena los campos de la interfaz de usuario con los datos obtenidos
                            jTextFieldDNI.setText(dni);
                            jTextFieldNombre.setText(nombre);
                            jTextFieldApellidos.setText(apellidos);
                            jTextFieldFechaNacimiento.setText(fechaNacimientoFormateada);
                            jTextFieldTelefono.setText(telefono);
                            jTextFieldEmail.setText(email);
                        }
                        // Cierra los recursos de la base de datos
                    }
                }
            }
        } catch (SQLException ex) {
            // Si ocurre algún error durante la consulta, muestra un mensaje de error
            JOptionPane.showMessageDialog(this, "Error al consultar la base de datos.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void rellenarDatosAbono(String dni) {
        try (Connection conexion = ConexionBD.obtenerConexion()) {
            // Definimos la consulta SQL para obtener el abono del usuario
            String query = "SELECT * FROM abonos WHERE usuario_dni = ?";

            try ( // Preparamos la consulta
                    PreparedStatement pstmt = conexion.prepareStatement(query)) {
                pstmt.setString(1, dni); // Establecemos el DNI como parámetro en la consulta
                // Verificamos si se encontraron resultados
                try ( // Ejecutamos la consulta
                        ResultSet rs = pstmt.executeQuery()) {
                    // Verificamos si se encontraron resultados
                    if (rs.next()) {
                        // Mensaje de depuración para verificar si se encontraron resultados
                        
                        // Obtenemos los datos del abono de la consulta
                        String fechaInicioContratoSQL = rs.getString("fecha_inicio_contrato");
                        LocalDate fechaInicioContrato = LocalDate.parse(fechaInicioContratoSQL);
                        String fechaInicioContratoFormateada = fechaInicioContrato.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                        
                        String fechaFinContratoSQL = rs.getString("fecha_fin_contrato");
                        LocalDate fechaFinContrato = LocalDate.parse(fechaFinContratoSQL);
                        String fechaFinContratoFormateada = fechaFinContrato.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                        
                        double precioMensual = rs.getDouble("precio_mensual");
                        int duracionMeses = rs.getInt("duracion_meses");
                        precioTotal = rs.getDouble("precio_total");
                        boolean premium = rs.getBoolean("premium");
                        
                        //Rellenamos los campos de la interfaz de usuario con los datos obtenidos
                        jTextFieldFechaInicio.setText(fechaInicioContratoFormateada);
                        jTextFieldFechaFin.setText(fechaFinContratoFormateada);
                        jComboBoxPrecioMensual.setSelectedItem(String.valueOf(precioMensual));
                        jTextFieldMeses.setText(String.valueOf(duracionMeses));
                        jLabelPrecioTotal.setText(String.valueOf(precioTotal));
                        jCheckBox1.setSelected(premium);
                    } else {
                        
                        JOptionPane.showMessageDialog(this, "No se encontraron abonos para este usuario", "Info", JOptionPane.INFORMATION_MESSAGE);
                        // Si no se encuentra un abono para el usuario, se pueden limpiar los campos
                        jTextFieldFechaInicio.setText("");
                        jTextFieldFechaFin.setText("");
                        jComboBoxPrecioMensual.setSelectedIndex(0);
                        jTextFieldMeses.setText("");
                        jLabelPrecioTotal.setText("");
                        jCheckBox1.setSelected(false);
                    }
                }
            } // Establecemos el DNI como parámetro en la consulta // Establecemos el DNI como parámetro en la consulta
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al obtener el abono de la base de datos.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
  
    /**
     *
     * @param dniUsuario
     * @param fechaInicio
     * @param fechaFin
     * @param precioTotal
     * @return
     */
    public boolean actualizarAbonoEnBD(String dniUsuario, String fechaInicio, String fechaFin, double precioTotal) {
        //Con esto convertimos las fechas de inicio y fin al formato que espera MySQL (YYYY-MM-DD)
        DateTimeFormatter formatterEntrada = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter formatterSalida = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        LocalDate fechaInicioLocalDate = LocalDate.parse(fechaInicio, formatterEntrada);
        String fechaInicioFormatoSQL = fechaInicioLocalDate.format(formatterSalida);

        LocalDate fechaFinLocalDate = LocalDate.parse(fechaFin, formatterEntrada);
        String fechaFinFormatoSQL = fechaFinLocalDate.format(formatterSalida);

        //Aquí preparamos la consulta SQL para actualizar, no para insertar
        String updateQuery = "UPDATE abonos SET fecha_inicio_contrato = ?, fecha_fin_contrato = ?, precio_total = ?, duracion_meses = ?, precio_mensual = ?, premium = ? WHERE usuario_dni = ?";
        int duracionMeses = Integer.parseInt(jTextFieldMeses.getText());

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement pstmt = conexion.prepareStatement(updateQuery)) {

            //Aquí establecemos los valores para la consulta de actualización, asegúrate de que coincidan con los campos en tu base de datos
            pstmt.setString(1, fechaInicioFormatoSQL);
            pstmt.setString(2, fechaFinFormatoSQL);
            pstmt.setDouble(3, precioTotal);
            pstmt.setInt(4, duracionMeses);
            pstmt.setDouble(5, calcularPrecioMensual(precioTotal, duracionMeses));
            pstmt.setBoolean(6, jCheckBox1.isSelected());
            pstmt.setString(7, dniUsuario);

            int filasAfectadas = pstmt.executeUpdate();
            return filasAfectadas > 0;

        } catch (SQLException ex) {
            return false;
        }
    }

    private void modificarUsuarioEnBD(String[] datosUsuario) {
        String nombre = datosUsuario[0];
        String apellidos = datosUsuario[1];
        String email = datosUsuario[2];
        String telefono = datosUsuario[3];
        // Convertir la fecha de nacimiento al formato yyyy-mm-dd
        String fechaNacimientoFormateada = datosUsuario[4];
        LocalDate fechaNacimiento = LocalDate.parse(fechaNacimientoFormateada, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        String fechaNacimientoSQL = fechaNacimiento.format(DateTimeFormatter.ISO_DATE);
        String dni = datosUsuario[5];

        int opcion = JOptionPane.showConfirmDialog(this, "¿Estás seguro de que deseas modificar los datos del usuario?", "Confirmar modificación", JOptionPane.YES_NO_OPTION);
        if (opcion == JOptionPane.YES_OPTION) {
            try (Connection conexion = ConexionBD.obtenerConexion()) {
                String query = "UPDATE usuarios SET nombre = ?, apellidos = ?, email = ?, telefono = ?, fecha_nacimiento = ? WHERE DNI = ?";
                PreparedStatement pstmt = conexion.prepareStatement(query);
                pstmt.setString(1, nombre);
                pstmt.setString(2, apellidos);
                pstmt.setString(3, email);
                pstmt.setString(4, telefono);
                pstmt.setString(5, fechaNacimientoSQL);
                pstmt.setString(6, dni);
                int filasAfectadas = pstmt.executeUpdate();
                if (filasAfectadas > 0) {
                    JOptionPane.showMessageDialog(this, "Usuario modificado correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "No se pudo modificar el usuario.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error al modificar el usuario en la base de datos.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void borrarUsuarioEnBD(String dni) {
        try (Connection conexion = ConexionBD.obtenerConexion()) {
            // Primero, borramos el registro correspondiente al usuario en la tabla 'abonos'
            String queryAbonos = "DELETE FROM abonos WHERE usuario_dni = ?";
            PreparedStatement pstmtAbonos = conexion.prepareStatement(queryAbonos);
            pstmtAbonos.setString(1, dni);
            pstmtAbonos.executeUpdate();

            // Luego, borramos el registro del usuario en la tabla 'usuarios'
            String queryUsuarios = "DELETE FROM usuarios WHERE DNI = ?";
            PreparedStatement pstmtUsuarios = conexion.prepareStatement(queryUsuarios);
            pstmtUsuarios.setString(1, dni);
            int filasAfectadas = pstmtUsuarios.executeUpdate();

            if (filasAfectadas > 0) {
                JOptionPane.showMessageDialog(this, "Usuario y abono asociado borrados correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                // Limpiar los campos de la interfaz de usuario
                jTextFieldNombre.setText("");
                jTextFieldApellidos.setText("");
                jTextFieldEmail.setText("");
                jTextFieldTelefono.setText("");
                jTextFieldFechaNacimiento.setText("");
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo borrar el usuario.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al borrar el usuario de la base de datos.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void borrarAbonoEnBD(String dni) {
        try (Connection conexion = ConexionBD.obtenerConexion()) {
            String queryAbonos = "DELETE FROM abonos WHERE usuario_dni = ?";
            PreparedStatement pstmtAbonos = conexion.prepareStatement(queryAbonos);
            pstmtAbonos.setString(1, dni);
            int filasAfectadas = pstmtAbonos.executeUpdate();

            if (filasAfectadas > 0) {
                JOptionPane.showMessageDialog(this, "Abono borrado correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                // Limpiar los campos de la interfaz de usuario
                jTextFieldFechaInicio.setText("");
                jTextFieldFechaFin.setText("");
                jTextFieldMeses.setText("");
                jComboBoxDescuento.setSelectedIndex(0);
                jComboBoxPrecioMensual.setSelectedIndex(0);
                jCheckBox1.setSelected(false);
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo borrar el abono.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al borrar el abono de la base de datos.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     *
     * @param nombre
     * @param apellidos
     * @param dni
     */
    @Override
    public void onUsuarioSeleccionado(String nombre, String apellidos, String dni) {
        rellenarDatosUsuario(dni);
    }

    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanelLateral = new javax.swing.JPanel();
        jButtonNuevoUsuario = new javax.swing.JButton();
        jButtonGestionUsuarios = new javax.swing.JButton();
        jButtonNuevoAbono = new javax.swing.JButton();
        jPanelHeader = new javax.swing.JPanel();
        jPanelLogo = new javax.swing.JPanel();
        jButtonInicio = new javax.swing.JButton();
        jLabelTituloVentana = new javax.swing.JLabel();
        jLabelUsuario = new javax.swing.JLabel();
        jLabelMeses = new javax.swing.JLabel();
        jLabelPrecioMensual = new javax.swing.JLabel();
        jLabelDescuento = new javax.swing.JLabel();
        jLabelFechaInicio = new javax.swing.JLabel();
        jLabelFechaFin = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabelPrecioTotal = new javax.swing.JLabel();
        jLabelVerificadorFechaInicio = new javax.swing.JLabel();
        jLabelVerificadorMeses = new javax.swing.JLabel();
        jTextFieldMeses = new javax.swing.JTextField();
        jTextFieldFechaInicio = new javax.swing.JTextField();
        jTextFieldFechaFin = new javax.swing.JTextField();
        jButtonBuscarUsuario = new javax.swing.JButton();
        jButtonRenovarAbono = new javax.swing.JButton();
        jButtonCancelar = new javax.swing.JButton();
        jComboBoxPrecioMensual = new javax.swing.JComboBox<>();
        jComboBoxDescuento = new javax.swing.JComboBox<>();
        jCheckBox1 = new javax.swing.JCheckBox();
        jButtonEliminarAbono = new javax.swing.JButton();
        jTextFieldApellidos = new javax.swing.JTextField();
        jTextFieldEmail = new javax.swing.JTextField();
        jTextFieldDNI = new javax.swing.JTextField();
        jTextFieldNombre = new javax.swing.JTextField();
        jTextFieldFechaNacimiento = new javax.swing.JTextField();
        jTextFieldTelefono = new javax.swing.JTextField();
        jLabelFechaNacimiento = new javax.swing.JLabel();
        jLabelApellidos = new javax.swing.JLabel();
        jLabelDNI = new javax.swing.JLabel();
        jLabelEmail = new javax.swing.JLabel();
        jLabelTelefono = new javax.swing.JLabel();
        jLabelVerificacionEmail = new javax.swing.JLabel();
        jLabelNombre = new javax.swing.JLabel();
        jLabelVerificacionApellido = new javax.swing.JLabel();
        jLabelVerificacionNombre = new javax.swing.JLabel();
        jLabelVerificacionFechaNacimiento = new javax.swing.JLabel();
        jLabelVerificacionTelefono = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jButtonEliminarUsuario = new javax.swing.JButton();
        jButtonGuardarCambios = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);

        jPanelLateral.setBackground(new java.awt.Color(255, 102, 102));
        jPanelLateral.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jButtonNuevoUsuario.setText("NUEVO USUARIO");
        jButtonNuevoUsuario.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButtonNuevoUsuario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonNuevoUsuarioActionPerformed(evt);
            }
        });

        jButtonGestionUsuarios.setText("GESTION DE USUARIOS");
        jButtonGestionUsuarios.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonGestionUsuariosActionPerformed(evt);
            }
        });

        jButtonNuevoAbono.setText("NUEVO ABONO");
        jButtonNuevoAbono.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonNuevoAbonoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanelLateralLayout = new javax.swing.GroupLayout(jPanelLateral);
        jPanelLateral.setLayout(jPanelLateralLayout);
        jPanelLateralLayout.setHorizontalGroup(
            jPanelLateralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelLateralLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelLateralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jButtonNuevoAbono, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButtonGestionUsuarios, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButtonNuevoUsuario, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanelLateralLayout.setVerticalGroup(
            jPanelLateralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelLateralLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButtonNuevoUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButtonNuevoAbono, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButtonGestionUsuarios, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(314, Short.MAX_VALUE))
        );

        jPanelHeader.setBackground(new java.awt.Color(255, 102, 102));
        jPanelHeader.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        javax.swing.GroupLayout jPanelHeaderLayout = new javax.swing.GroupLayout(jPanelHeader);
        jPanelHeader.setLayout(jPanelHeaderLayout);
        jPanelHeaderLayout.setHorizontalGroup(
            jPanelHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanelHeaderLayout.setVerticalGroup(
            jPanelHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        jPanelLogo.setBackground(new java.awt.Color(255, 102, 102));
        jPanelLogo.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jButtonInicio.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/LogoGymApp.png"))); // NOI18N
        jButtonInicio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonInicioActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanelLogoLayout = new javax.swing.GroupLayout(jPanelLogo);
        jPanelLogo.setLayout(jPanelLogoLayout);
        jPanelLogoLayout.setHorizontalGroup(
            jPanelLogoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jButtonInicio, javax.swing.GroupLayout.PREFERRED_SIZE, 182, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        jPanelLogoLayout.setVerticalGroup(
            jPanelLogoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jButtonInicio, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        jLabelTituloVentana.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabelTituloVentana.setText("GESTION USUARIOS Y ABONOS");

        jLabelUsuario.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabelUsuario.setText("Usuario");

        jLabelMeses.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabelMeses.setText("Número de meses");

        jLabelPrecioMensual.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabelPrecioMensual.setText("Precio Mensual");

        jLabelDescuento.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabelDescuento.setText("Descuento");

        jLabelFechaInicio.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabelFechaInicio.setText("Nueva fecha de inicio");

        jLabelFechaFin.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabelFechaFin.setText("Fecha de fin");

        jLabel1.setText("ABONO PREMIUM (10€ mensuales más)");

        jLabelPrecioTotal.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabelPrecioTotal.setText("Precio TOTAL:");

        jLabelVerificadorFechaInicio.setForeground(new java.awt.Color(255, 51, 0));
        jLabelVerificadorFechaInicio.setToolTipText("");

        jLabelVerificadorMeses.setForeground(new java.awt.Color(255, 51, 0));
        jLabelVerificadorMeses.setToolTipText("");

        jTextFieldMeses.setToolTipText("Introducir apellidos (Solo letras y espacios)");
        jTextFieldMeses.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldMesesActionPerformed(evt);
            }
        });

        jTextFieldFechaInicio.setToolTipText("Introduce fecha de nacimiento válida");
        jTextFieldFechaInicio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldFechaInicioActionPerformed(evt);
            }
        });

        jTextFieldFechaFin.setEditable(false);
        jTextFieldFechaFin.setToolTipText("Introduce fecha de nacimiento válida");
        jTextFieldFechaFin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldFechaFinActionPerformed(evt);
            }
        });

        jButtonBuscarUsuario.setText("Buscar usuario...");
        jButtonBuscarUsuario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonBuscarUsuarioActionPerformed(evt);
            }
        });

        jButtonRenovarAbono.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButtonRenovarAbono.setText("RENOVAR ABONO");
        jButtonRenovarAbono.setToolTipText("");
        jButtonRenovarAbono.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonRenovarAbonoActionPerformed(evt);
            }
        });

        jButtonCancelar.setText("Cancelar");
        jButtonCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCancelarActionPerformed(evt);
            }
        });

        jComboBoxPrecioMensual.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "20", "30", "40" }));
        jComboBoxPrecioMensual.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxPrecioMensualActionPerformed(evt);
            }
        });

        jComboBoxDescuento.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "0", "5", "15", "25", "50", "100" }));

        jButtonEliminarAbono.setFont(new java.awt.Font("Segoe UI", 1, 10)); // NOI18N
        jButtonEliminarAbono.setForeground(new java.awt.Color(255, 0, 0));
        jButtonEliminarAbono.setText("ELIMINAR ABONO");
        jButtonEliminarAbono.setToolTipText("");
        jButtonEliminarAbono.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonEliminarAbonoActionPerformed(evt);
            }
        });

        jTextFieldApellidos.setToolTipText("Introducir apellidos (Solo letras y espacios)");
        jTextFieldApellidos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldApellidosActionPerformed(evt);
            }
        });

        jTextFieldEmail.setToolTipText("Introducir email");
        jTextFieldEmail.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldEmailActionPerformed(evt);
            }
        });

        jTextFieldDNI.setEditable(false);
        jTextFieldDNI.setToolTipText("Introducir apellidos (Solo letras y espacios)");
        jTextFieldDNI.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldDNIActionPerformed(evt);
            }
        });

        jTextFieldNombre.setToolTipText("Introducir nombre (Solo letras y espacios)");
        jTextFieldNombre.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldNombreActionPerformed(evt);
            }
        });

        jTextFieldFechaNacimiento.setToolTipText("Introduce fecha de nacimiento válida");
        jTextFieldFechaNacimiento.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldFechaNacimientoActionPerformed(evt);
            }
        });

        jTextFieldTelefono.setToolTipText("Introducir teléfono (9 u 11 dígitos)");
        jTextFieldTelefono.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldTelefonoActionPerformed(evt);
            }
        });

        jLabelFechaNacimiento.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabelFechaNacimiento.setText("F.Nacimiento");

        jLabelApellidos.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabelApellidos.setText("Apellidos");

        jLabelDNI.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabelDNI.setText("DNI");

        jLabelEmail.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabelEmail.setText("Email");

        jLabelTelefono.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabelTelefono.setText("Telefono");

        jLabelVerificacionEmail.setForeground(new java.awt.Color(255, 0, 0));

        jLabelNombre.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabelNombre.setText("Nombre");

        jLabelVerificacionApellido.setForeground(new java.awt.Color(255, 0, 0));
        jLabelVerificacionApellido.setToolTipText("");

        jLabelVerificacionNombre.setForeground(new java.awt.Color(255, 51, 51));

        jLabelVerificacionFechaNacimiento.setForeground(new java.awt.Color(255, 0, 0));

        jLabelVerificacionTelefono.setForeground(new java.awt.Color(255, 0, 0));
        jLabelVerificacionTelefono.setToolTipText("");

        jButtonEliminarUsuario.setFont(new java.awt.Font("Segoe UI", 1, 10)); // NOI18N
        jButtonEliminarUsuario.setForeground(new java.awt.Color(255, 0, 0));
        jButtonEliminarUsuario.setText("ELIMINAR USUARIO");
        jButtonEliminarUsuario.setToolTipText("");
        jButtonEliminarUsuario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonEliminarUsuarioActionPerformed(evt);
            }
        });

        jButtonGuardarCambios.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButtonGuardarCambios.setText("GUARDAR CAMBIOS");
        jButtonGuardarCambios.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonGuardarCambiosActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jPanelLogo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanelLateral, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanelHeader, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(33, 33, 33)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jButtonCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabelPrecioTotal)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel1)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jCheckBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(jButtonRenovarAbono, javax.swing.GroupLayout.PREFERRED_SIZE, 243, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(176, 176, 176)
                                .addComponent(jButtonEliminarAbono, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addGroup(layout.createSequentialGroup()
                                            .addComponent(jLabelUsuario)
                                            .addGap(310, 310, 310))
                                        .addComponent(jLabelTituloVentana)
                                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                            .addComponent(jLabelEmail)
                                            .addGap(36, 36, 36)
                                            .addComponent(jTextFieldEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 197, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(jLabelVerificacionEmail))
                                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                            .addComponent(jLabelTelefono)
                                            .addGap(18, 18, 18)
                                            .addComponent(jTextFieldTelefono, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(jLabelVerificacionTelefono)))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(68, 68, 68)
                                        .addComponent(jTextFieldApellidos, javax.swing.GroupLayout.PREFERRED_SIZE, 223, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabelVerificacionApellido))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(jButtonBuscarUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 226, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                    .addComponent(jLabelApellidos)
                                                    .addComponent(jLabelNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addGap(12, 12, 12)
                                                .addComponent(jTextFieldNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 223, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabelVerificacionNombre)))
                                .addGap(58, 58, 58)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabelDNI)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jTextFieldDNI, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabelVerificacionFechaNacimiento))
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(jButtonEliminarUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jButtonGuardarCambios, javax.swing.GroupLayout.PREFERRED_SIZE, 182, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGroup(layout.createSequentialGroup()
                                            .addComponent(jLabelFechaNacimiento)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(jTextFieldFechaNacimiento, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                            .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 775, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabelFechaFin)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jTextFieldFechaFin, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabelFechaInicio)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jTextFieldFechaInicio, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabelVerificadorFechaInicio, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabelPrecioMensual)
                                            .addComponent(jLabelDescuento))
                                        .addGap(24, 24, 24))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                        .addComponent(jLabelMeses)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jComboBoxPrecioMensual, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jComboBoxDescuento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jTextFieldMeses, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabelVerificadorMeses, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap(32, Short.MAX_VALUE))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanelHeader, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanelLogo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanelLateral, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addComponent(jLabelTituloVentana)
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabelDNI)
                                    .addComponent(jTextFieldDNI, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabelVerificacionFechaNacimiento)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jTextFieldFechaNacimiento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabelFechaNacimiento, javax.swing.GroupLayout.Alignment.TRAILING))
                                .addGap(12, 12, 12)
                                .addComponent(jButtonGuardarCambios, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabelUsuario)
                                    .addComponent(jButtonBuscarUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jTextFieldNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabelVerificacionNombre)
                                    .addComponent(jLabelNombre))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabelApellidos)
                                    .addComponent(jTextFieldApellidos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabelVerificacionApellido))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jTextFieldEmail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabelEmail)
                                    .addComponent(jLabelVerificacionEmail))))
                        .addGap(20, 20, 20)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabelVerificacionTelefono)
                            .addComponent(jButtonEliminarUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextFieldTelefono, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabelTelefono))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabelFechaInicio)
                                        .addComponent(jTextFieldFechaInicio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(jLabelVerificadorFechaInicio, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(17, 17, 17)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabelFechaFin)
                                    .addComponent(jTextFieldFechaFin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jCheckBox1)
                                    .addComponent(jLabel1))
                                .addGap(14, 14, 14)
                                .addComponent(jLabelPrecioTotal)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jButtonRenovarAbono, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabelMeses, javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(jTextFieldMeses, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabelPrecioMensual)
                                            .addComponent(jComboBoxPrecioMensual, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addComponent(jLabelVerificadorMeses, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabelDescuento)
                                    .addComponent(jComboBoxDescuento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButtonCancelar)
                            .addComponent(jButtonEliminarAbono, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)))))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonNuevoUsuarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonNuevoUsuarioActionPerformed
        AltaUsuario nuevoFrame = new AltaUsuario();
        nuevoFrame.setLocation(this.getLocation()); // Establece la ubicación del nuevo JFrame igual a la del actual
        nuevoFrame.setVisible(true);
        this.dispose(); // Cierra el JFrame actual
    }//GEN-LAST:event_jButtonNuevoUsuarioActionPerformed

    private void jTextFieldFechaInicioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldFechaInicioActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldFechaInicioActionPerformed

    private void jButtonRenovarAbonoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonRenovarAbonoActionPerformed
    int confirmacion = JOptionPane.showConfirmDialog(this, "¿Estás seguro de que deseas modificar el abono para este usuario?", "Confirmar modificación de abono", JOptionPane.YES_NO_OPTION);
        if (confirmacion == JOptionPane.YES_OPTION) {
        verificarDatosAbono();
        }
    }//GEN-LAST:event_jButtonRenovarAbonoActionPerformed

    private void jButtonCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCancelarActionPerformed
        GimApp nuevoFrame = new GimApp();
        nuevoFrame.setLocation(this.getLocation()); // Establece la ubicación del nuevo JFrame igual a la del actual
        nuevoFrame.setVisible(true);
        this.dispose(); // Cierra el JFrame actual
    }//GEN-LAST:event_jButtonCancelarActionPerformed

    private void jTextFieldFechaFinActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldFechaFinActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldFechaFinActionPerformed

    private void jTextFieldMesesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldMesesActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldMesesActionPerformed

    private void jComboBoxPrecioMensualActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxPrecioMensualActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBoxPrecioMensualActionPerformed

    private void jButtonBuscarUsuarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonBuscarUsuarioActionPerformed
        BuscarUsuarios dialogoBuscarUsuarios = null;
          try {
              dialogoBuscarUsuarios = new BuscarUsuarios(this, true);
          } catch (SQLException ex) {
              Logger.getLogger(GestionUsuarios.class.getName()).log(Level.SEVERE, null, ex);
          }

          // Mostrar el diálogo de búsqueda de usuarios
          dialogoBuscarUsuarios.setVisible(true);

          // Obtener el DNI del usuario seleccionado
          String dniUsuarioSeleccionado = dialogoBuscarUsuarios.getDNIUsuarioSeleccionado();

          // Rellenar los datos del usuario en la interfaz de usuario utilizando el DNI obtenido
          rellenarDatosUsuario(dniUsuarioSeleccionado);
          rellenarDatosAbono(jTextFieldDNI.getText());
    }//GEN-LAST:event_jButtonBuscarUsuarioActionPerformed

    private void jButtonNuevoAbonoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonNuevoAbonoActionPerformed
        AltaAbono nuevoFrame = new AltaAbono();
        nuevoFrame.setLocation(this.getLocation()); // Establece la ubicación del nuevo JFrame igual a la del actual
        nuevoFrame.setVisible(true);
        this.dispose(); // Cierra el JFrame actual
    }//GEN-LAST:event_jButtonNuevoAbonoActionPerformed

    private void jButtonGestionUsuariosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonGestionUsuariosActionPerformed
        GestionUsuarios nuevoFrame = new GestionUsuarios();
        nuevoFrame.setLocation(this.getLocation()); // Establece la ubicación del nuevo JFrame igual a la del actual
        nuevoFrame.setVisible(true);
        this.dispose(); // Cierra el JFrame actual
    }//GEN-LAST:event_jButtonGestionUsuariosActionPerformed

    private void jButtonInicioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonInicioActionPerformed
        GimApp nuevoFrame = new GimApp();
        nuevoFrame.setLocation(this.getLocation()); // Establece la ubicación del nuevo JFrame igual a la del actual
        nuevoFrame.setVisible(true);
        this.dispose(); // Cierra el JFrame actual
    }//GEN-LAST:event_jButtonInicioActionPerformed

    private void jButtonEliminarAbonoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonEliminarAbonoActionPerformed
    int confirmacion = JOptionPane.showConfirmDialog(this, "¿Estás seguro de que deseas eliminar el abono para este usuario?", "Confirmar eliminación de abono", JOptionPane.YES_NO_OPTION);
        if (confirmacion == JOptionPane.YES_OPTION) {
            borrarAbonoEnBD(jTextFieldDNI.getText());
            JOptionPane.showMessageDialog(this, "Abono borrado con éxito", "Info", JOptionPane.INFORMATION_MESSAGE);
    }
    }//GEN-LAST:event_jButtonEliminarAbonoActionPerformed

    private void jTextFieldApellidosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldApellidosActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldApellidosActionPerformed

    private void jTextFieldEmailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldEmailActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldEmailActionPerformed

    private void jTextFieldDNIActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldDNIActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldDNIActionPerformed

    private void jTextFieldNombreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldNombreActionPerformed

    }//GEN-LAST:event_jTextFieldNombreActionPerformed

    private void jTextFieldFechaNacimientoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldFechaNacimientoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldFechaNacimientoActionPerformed

    private void jTextFieldTelefonoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldTelefonoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldTelefonoActionPerformed

    private void jButtonEliminarUsuarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonEliminarUsuarioActionPerformed
        int opcion = JOptionPane.showConfirmDialog(this, "¿Estás seguro de que deseas borrar este usuario y su abono asociado?", "Confirmar borrado", JOptionPane.YES_NO_OPTION);
        if (opcion == JOptionPane.YES_OPTION) {
            String dni = jTextFieldDNI.getText();
            borrarUsuarioEnBD(dni);
            JOptionPane.showMessageDialog(this, "Usuario y abono eliminados correctamente", "Info", JOptionPane.INFORMATION_MESSAGE);
    }
    }//GEN-LAST:event_jButtonEliminarUsuarioActionPerformed

    private void jButtonGuardarCambiosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonGuardarCambiosActionPerformed
         if (verificarDatosUsuario()){
        String[] datosUsuario = obtenerDatosUsuario();
        modificarUsuarioEnBD(datosUsuario);
        JOptionPane.showMessageDialog(this, "Cambios realizados con éxito", "Info", JOptionPane.INFORMATION_MESSAGE);
        }
    }//GEN-LAST:event_jButtonGuardarCambiosActionPerformed

    /**
     *
     * @param args
     */
    public static void main(String args[]) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(GestionUsuarios.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        java.awt.EventQueue.invokeLater(() -> {
            new GestionUsuarios().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonBuscarUsuario;
    private javax.swing.JButton jButtonCancelar;
    private javax.swing.JButton jButtonEliminarAbono;
    private javax.swing.JButton jButtonEliminarUsuario;
    private javax.swing.JButton jButtonGestionUsuarios;
    private javax.swing.JButton jButtonGuardarCambios;
    private javax.swing.JButton jButtonInicio;
    private javax.swing.JButton jButtonNuevoAbono;
    private javax.swing.JButton jButtonNuevoUsuario;
    private javax.swing.JButton jButtonRenovarAbono;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JComboBox<String> jComboBoxDescuento;
    private javax.swing.JComboBox<String> jComboBoxPrecioMensual;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabelApellidos;
    private javax.swing.JLabel jLabelDNI;
    private javax.swing.JLabel jLabelDescuento;
    private javax.swing.JLabel jLabelEmail;
    private javax.swing.JLabel jLabelFechaFin;
    private javax.swing.JLabel jLabelFechaInicio;
    private javax.swing.JLabel jLabelFechaNacimiento;
    private javax.swing.JLabel jLabelMeses;
    private javax.swing.JLabel jLabelNombre;
    private javax.swing.JLabel jLabelPrecioMensual;
    private javax.swing.JLabel jLabelPrecioTotal;
    private javax.swing.JLabel jLabelTelefono;
    private javax.swing.JLabel jLabelTituloVentana;
    private javax.swing.JLabel jLabelUsuario;
    private javax.swing.JLabel jLabelVerificacionApellido;
    private javax.swing.JLabel jLabelVerificacionEmail;
    private javax.swing.JLabel jLabelVerificacionFechaNacimiento;
    private javax.swing.JLabel jLabelVerificacionNombre;
    private javax.swing.JLabel jLabelVerificacionTelefono;
    private javax.swing.JLabel jLabelVerificadorFechaInicio;
    private javax.swing.JLabel jLabelVerificadorMeses;
    private javax.swing.JPanel jPanelHeader;
    private javax.swing.JPanel jPanelLateral;
    private javax.swing.JPanel jPanelLogo;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTextField jTextFieldApellidos;
    private javax.swing.JTextField jTextFieldDNI;
    private javax.swing.JTextField jTextFieldEmail;
    private javax.swing.JTextField jTextFieldFechaFin;
    private javax.swing.JTextField jTextFieldFechaInicio;
    private javax.swing.JTextField jTextFieldFechaNacimiento;
    private javax.swing.JTextField jTextFieldMeses;
    private javax.swing.JTextField jTextFieldNombre;
    private javax.swing.JTextField jTextFieldTelefono;
    // End of variables declaration//GEN-END:variables
}
