
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
public class AltaAbono extends javax.swing.JFrame implements UsuarioSeleccionadoListener {
    private String dniUsuarioSeleccionado;
    
    /**
     *
     */
    public AltaAbono() {
        initComponents();
        //setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("icono.png")));
        setTitle("Gimnasio - Abonos");
        jButtonGuardarAbono.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
             
            }
        });

        jComboBoxPrecioMensual.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                calcularPrecioTotal();
            }
        });

        jComboBoxDescuento.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                calcularPrecioTotal();
            }
        });

        jCheckBox1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                calcularPrecioTotal();
            }
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
    
    /**
     *
     * @param nombre
     * @param apellidos
     * @param dni
     */
    @Override
    public void onUsuarioSeleccionado(String nombre, String apellidos, String dni) {
        mostrarUsuarioSeleccionado(nombre, apellidos, dni);
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
        if (dia < 1 || dia > diasPorMes[mes - 1]) {
            return false;
        }

        return true;
    }

    /**
     *
     */
    public void verificarFechaInicio() {
        String fechaInicioStr = jTextFieldFechaInicio.getText();

        if (!verificarFecha(fechaInicioStr)) {
            jLabelVerificadorFechaInicio.setText("Fecha incorrecta");
            return;
        }

        jLabelVerificadorFechaInicio.setText("");
        calcularFechaFin(); // Llamar a calcularFechaFin después de verificar la fecha
    }

    /**
     *
     */
    public void calcularFechaFin() {
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
     */
    public void calcularPrecioTotal() {
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

            jLabelPrecioTotal.setText("Precio Total: " + String.format("%.2f", total) + "€");
        }
    }

    /**
     *
     */
    public void verificarDatos() {
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

        jLabelPrecioTotal.setText(String.format("%.2f", total) + "€");

        if (camposCorrectos) {
            //Verificamos si el usuario ya tiene un abono existente
            if (existeAbonoParaUsuario(dniUsuarioSeleccionado)) {
                JOptionPane.showMessageDialog(this, "El usuario ya tiene un abono existente", "Error", JOptionPane.ERROR_MESSAGE);
                return; // No intentar insertar un nuevo abono si ya existe uno
            }

            //Si el usuario no tiene un abono existente, se inserta
            String fechaFin = jTextFieldFechaFin.getText();
            // Ahora pasamos 'total' directamente a insertarAbonoEnBD
            if (insertarAbonoEnBD(fechaInicio, fechaFin, total)) {
                JOptionPane.showMessageDialog(this, "Abono guardado con éxito", "Info", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Error al guardar el abono", "Error", JOptionPane.ERROR);
            }
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
    
    /**
     *
     * @param nombre
     * @param apellidos
     * @param dni
     */
    public void mostrarUsuarioSeleccionado(String nombre, String apellidos, String dni) {
        jTextFieldUsuario.setText(nombre + " " + apellidos);

        // Almacenar el DNI del usuario seleccionado
        dniUsuarioSeleccionado = dni;

        // Añade esta línea para depurar y verificar que el DNI se está capturando correctamente
        System.out.println("DNI Usuario Seleccionado: " + dniUsuarioSeleccionado);
    }
    
    private boolean insertarAbonoEnBD(String fechaInicio, String fechaFin, double precioTotal) {
        // Convertir las fechas de inicio y fin al formato esperado por MySQL (YYYY-MM-DD)
        DateTimeFormatter formatterEntrada = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter formatterSalida = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        LocalDate fechaInicioLocalDate = LocalDate.parse(fechaInicio, formatterEntrada);
        String fechaInicioFormatoSQL = fechaInicioLocalDate.format(formatterSalida);

        LocalDate fechaFinLocalDate = LocalDate.parse(fechaFin, formatterEntrada);
        String fechaFinFormatoSQL = fechaFinLocalDate.format(formatterSalida);

        // Ahora puedes usar fechaInicioFormatoSQL y fechaFinFormatoSQL en tu consulta SQL
        String insertQuery = "INSERT INTO abonos (usuario_dni, fecha_inicio_contrato, fecha_fin_contrato, precio_total, duracion_meses, precio_mensual, premium) VALUES (?, ?, ?, ?, ?, ?, ?)";
        int duracionMeses = Integer.parseInt(jTextFieldMeses.getText());

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement pstmt = conexion.prepareStatement(insertQuery)) {

            pstmt.setString(1, dniUsuarioSeleccionado);
            pstmt.setString(2, fechaInicioFormatoSQL);
            pstmt.setString(3, fechaFinFormatoSQL);
            pstmt.setDouble(4, precioTotal);
            pstmt.setInt(5, duracionMeses);
            pstmt.setDouble(6, calcularPrecioMensual(precioTotal, duracionMeses));
            pstmt.setBoolean(7, jCheckBox1.isSelected());

            int filasAfectadas = pstmt.executeUpdate();
            return filasAfectadas > 0;

        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
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
    
    /**
     *
     */
    public class FechaFormato {

        /**
         *
         * @param args
         */
        public static void main(String[] args) {
            // Fecha en formato DD/MM/YYYY
            String fechaInicioStr = "01/03/2024";

            // Convertir de DD/MM/YYYY a LocalDate
            DateTimeFormatter formatterEntrada = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate fecha = LocalDate.parse(fechaInicioStr, formatterEntrada);

            // Convertir de LocalDate a YYYY-MM-DD
            DateTimeFormatter formatterSalida = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String fechaInicioFormatoSQL = fecha.format(formatterSalida);

            System.out.println(fechaInicioFormatoSQL); // Salida: 2024-03-01
        }
    }
    
    /**
     *
     * @param dni
     * @return
     */
    public boolean existeAbonoParaUsuario(String dni) {
        String consulta = "SELECT COUNT(*) FROM abonos WHERE usuario_dni = ?";

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement pstmt = conexion.prepareStatement(consulta)) {

            pstmt.setString(1, dni);
            ResultSet resultado = pstmt.executeQuery();
            resultado.next(); // Mover al primer resultado
            int cantidadAbonos = resultado.getInt(1);

            return cantidadAbonos > 0; // Si hay algún abono para el usuario, retorna true
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false; // Si ocurre algún error, retorna false
        }
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
        jTextFieldUsuario = new javax.swing.JTextField();
        jTextFieldMeses = new javax.swing.JTextField();
        jTextFieldFechaInicio = new javax.swing.JTextField();
        jTextFieldFechaFin = new javax.swing.JTextField();
        jButtonBuscarUsuario = new javax.swing.JButton();
        jButtonGuardarAbono = new javax.swing.JButton();
        jButtonCancelar = new javax.swing.JButton();
        jComboBoxPrecioMensual = new javax.swing.JComboBox<>();
        jComboBoxDescuento = new javax.swing.JComboBox<>();
        jCheckBox1 = new javax.swing.JCheckBox();

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

        jButtonInicio.setBackground(new java.awt.Color(255, 102, 102));
        jButtonInicio.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        jButtonInicio.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/LogoGymApp.png"))); // NOI18N
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
        jLabelTituloVentana.setText("NUEVO ABONO");

        jLabelUsuario.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabelUsuario.setText("Usuario");

        jLabelMeses.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabelMeses.setText("Número de meses");

        jLabelPrecioMensual.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabelPrecioMensual.setText("Precio Mensual");

        jLabelDescuento.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabelDescuento.setText("Descuento");

        jLabelFechaInicio.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabelFechaInicio.setText("Fecha de inicio");

        jLabelFechaFin.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabelFechaFin.setText("Fecha de fin");

        jLabel1.setText("ABONO PREMIUM (10€ mensuales más)");

        jLabelPrecioTotal.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabelPrecioTotal.setText("Precio TOTAL:");

        jLabelVerificadorFechaInicio.setForeground(new java.awt.Color(255, 51, 0));
        jLabelVerificadorFechaInicio.setToolTipText("");

        jLabelVerificadorMeses.setForeground(new java.awt.Color(255, 51, 0));
        jLabelVerificadorMeses.setToolTipText("");

        jTextFieldUsuario.setEditable(false);
        jTextFieldUsuario.setToolTipText("Introducir nombre (Solo letras y espacios)");
        jTextFieldUsuario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldUsuarioActionPerformed(evt);
            }
        });

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

        jButtonBuscarUsuario.setText("Buscar...");
        jButtonBuscarUsuario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonBuscarUsuarioActionPerformed(evt);
            }
        });

        jButtonGuardarAbono.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButtonGuardarAbono.setText("CONFIRMAR NUEVO ABONO");
        jButtonGuardarAbono.setToolTipText("");
        jButtonGuardarAbono.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonGuardarAbonoActionPerformed(evt);
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
                        .addComponent(jLabelTituloVentana)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(85, 85, 85)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabelVerificadorMeses, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                        .addComponent(jLabelUsuario)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jTextFieldUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 218, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jButtonBuscarUsuario)))
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabelFechaInicio)
                                            .addComponent(jLabelFechaFin)
                                            .addComponent(jLabelDescuento)
                                            .addComponent(jLabelPrecioMensual)
                                            .addComponent(jLabelMeses))
                                        .addGap(88, 88, 88)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(layout.createSequentialGroup()
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                    .addComponent(jTextFieldFechaInicio, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addComponent(jTextFieldFechaFin, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jLabelVerificadorFechaInicio, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addComponent(jComboBoxPrecioMensual, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jComboBoxDescuento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jTextFieldMeses, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addComponent(jButtonCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabelPrecioTotal)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel1)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jCheckBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(jButtonGuardarAbono, javax.swing.GroupLayout.PREFERRED_SIZE, 243, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(398, Short.MAX_VALUE))))))
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
                        .addGap(38, 38, 38)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabelUsuario)
                                    .addComponent(jTextFieldUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabelVerificadorMeses, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabelMeses)
                                        .addComponent(jTextFieldMeses, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabelPrecioMensual)
                                    .addComponent(jComboBoxPrecioMensual, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabelDescuento)
                                    .addComponent(jComboBoxDescuento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(1, 1, 1)
                                .addComponent(jButtonBuscarUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextFieldFechaInicio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabelFechaInicio)
                                .addComponent(jLabelVerificadorFechaInicio, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabelFechaFin)
                            .addComponent(jTextFieldFechaFin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jCheckBox1))
                        .addGap(33, 33, 33)
                        .addComponent(jLabelPrecioTotal)
                        .addGap(38, 38, 38)
                        .addComponent(jButtonGuardarAbono, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButtonCancelar))))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonNuevoUsuarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonNuevoUsuarioActionPerformed
        AltaUsuario nuevoFrame = new AltaUsuario();
        nuevoFrame.setLocation(this.getLocation()); // Establece la ubicación del nuevo JFrame igual a la del actual
        nuevoFrame.setVisible(true);
        this.dispose(); // Cierra el JFrame actual
    }//GEN-LAST:event_jButtonNuevoUsuarioActionPerformed

    private void jTextFieldUsuarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldUsuarioActionPerformed
        
    }//GEN-LAST:event_jTextFieldUsuarioActionPerformed

    private void jTextFieldFechaInicioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldFechaInicioActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldFechaInicioActionPerformed

    private void jButtonGuardarAbonoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonGuardarAbonoActionPerformed
            verificarDatos();
            GimApp nuevoFrame = new GimApp();
            nuevoFrame.setLocation(this.getLocation());
            nuevoFrame.setVisible(true);
            this.dispose();
    }//GEN-LAST:event_jButtonGuardarAbonoActionPerformed

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
            Logger.getLogger(AltaAbono.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        dialogoBuscarUsuarios.setVisible(true);
    }//GEN-LAST:event_jButtonBuscarUsuarioActionPerformed

    private void jButtonNuevoAbonoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonNuevoAbonoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButtonNuevoAbonoActionPerformed

    private void jButtonGestionUsuariosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonGestionUsuariosActionPerformed
        GestionUsuarios nuevoFrame = new GestionUsuarios();
        nuevoFrame.setLocation(this.getLocation()); // Establece la ubicación del nuevo JFrame igual a la del actual
        nuevoFrame.setVisible(true);
        this.dispose(); // Cierra el JFrame actual
    }//GEN-LAST:event_jButtonGestionUsuariosActionPerformed

    private void jButtonInicioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonInicioActionPerformed
        GimApp nuevoFrame = new GimApp();
        nuevoFrame.setLocation(this.getLocation());
        nuevoFrame.setVisible(true);
        this.dispose(); // Cierra el JFrame actual
    }//GEN-LAST:event_jButtonInicioActionPerformed



    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonBuscarUsuario;
    private javax.swing.JButton jButtonCancelar;
    private javax.swing.JButton jButtonGestionUsuarios;
    private javax.swing.JButton jButtonGuardarAbono;
    private javax.swing.JButton jButtonInicio;
    private javax.swing.JButton jButtonNuevoAbono;
    private javax.swing.JButton jButtonNuevoUsuario;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JComboBox<String> jComboBoxDescuento;
    private javax.swing.JComboBox<String> jComboBoxPrecioMensual;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabelDescuento;
    private javax.swing.JLabel jLabelFechaFin;
    private javax.swing.JLabel jLabelFechaInicio;
    private javax.swing.JLabel jLabelMeses;
    private javax.swing.JLabel jLabelPrecioMensual;
    private javax.swing.JLabel jLabelPrecioTotal;
    private javax.swing.JLabel jLabelTituloVentana;
    private javax.swing.JLabel jLabelUsuario;
    private javax.swing.JLabel jLabelVerificadorFechaInicio;
    private javax.swing.JLabel jLabelVerificadorMeses;
    private javax.swing.JPanel jPanelHeader;
    private javax.swing.JPanel jPanelLateral;
    private javax.swing.JPanel jPanelLogo;
    private javax.swing.JTextField jTextFieldFechaFin;
    private javax.swing.JTextField jTextFieldFechaInicio;
    private javax.swing.JTextField jTextFieldMeses;
    private javax.swing.JTextField jTextFieldUsuario;
    // End of variables declaration//GEN-END:variables
}
