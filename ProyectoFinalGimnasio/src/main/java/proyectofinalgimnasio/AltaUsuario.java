/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package proyectofinalgimnasio;

import java.awt.Toolkit;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;

/**
 *
 * @author Sebastián Melgar Marín
 */
public class AltaUsuario extends javax.swing.JFrame {

    /**
     * Creates new form GimApp
     */
    public AltaUsuario() {
        initComponents();
        //setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("icono.png")));
        setTitle("Gimnasio - Altas");
    }
    
    /**
     *
     * @return
     */
    public boolean verificarDatos() {
            String regexNombre = "[A-Za-záéíóúÁÉÍÓÚñÑ\\s]+";
            String regexApellidos = "[A-Za-záéíóúÁÉÍÓÚñÑ\\s]+";
            String regexTelefono = "\\d{9}|\\d{11}";
            String regexEmail = "\\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}\\b";
            String regexDNI = "((([X-Zx-z])|([LM])){1}([-]?)((\\d){7})([-]?)([A-Za-z]))|((\\d{8})([-]?)([A-Za-z]))";

            String nombre = jTextFieldNombre.getText();
            String apellidos = jTextFieldApellidos.getText();
            String telefono = jTextFieldTelefono.getText();
            String email = jTextFieldEmail.getText();
            String fechaNacimiento = jTextFieldFechaNacimiento.getText();
            String dni = jTextFieldDNI.getText();

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

            pattern = Pattern.compile(regexDNI);
            matcher = pattern.matcher(dni);
            if (!matcher.matches()) {
                jLabelVerificacionDNI.setText("Ingrese un DNI o NIE válido");
                camposCorrectos = false;
            } else {
                jLabelVerificacionDNI.setText("");
            }

            if (!verificarFecha(fechaNacimiento)) {
                jLabelVerificacionFechaNacimiento.setText("La fecha de nacimiento no es válida");
                camposCorrectos = false;
            } else {
                jLabelVerificacionFechaNacimiento.setText("");
            }
            
             if (dniExistente(dni)) {
                JOptionPane.showMessageDialog(this, "Ya existe un usuario con este DNI", "Error", JOptionPane.ERROR_MESSAGE);
                camposCorrectos = false;
    }

            return camposCorrectos;
    }
        
    private void guardarDatos() {
            String nombre = jTextFieldNombre.getText();
            String apellidos = jTextFieldApellidos.getText();
            String dni = jTextFieldDNI.getText();
            String telefono = jTextFieldTelefono.getText();
            String email = jTextFieldEmail.getText();
            String fechaNacimiento = jTextFieldFechaNacimiento.getText();

            //Se formatear la fecha al formato YYYY-MM-DD para que MySQL lo acepte
            String[] partesFecha = fechaNacimiento.split("[-/]");
            String fechaFormateada = partesFecha[2] + "-" + partesFecha[1] + "-" + partesFecha[0];

            try {
                Connection conexion = ConexionBD.obtenerConexion();
                String consulta = "INSERT INTO usuarios (DNI, nombre, apellidos, fecha_nacimiento, telefono, email) VALUES (?, ?, ?, ?, ?, ?)";
                PreparedStatement statement = conexion.prepareStatement(consulta);
                statement.setString(1, dni);
                statement.setString(2, nombre);
                statement.setString(3, apellidos);
                statement.setString(4, fechaFormateada); //Utilizamos la fecha formateada
                statement.setString(5, telefono);
                statement.setString(6, email);

                int filasInsertadas = statement.executeUpdate();

                statement.close();
                conexion.close();

                if (filasInsertadas > 0) {
                    System.out.println("Los datos se han insertado correctamente en la base de datos.");
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }

    /**
     *
     * @param fecha
     * @return
     */
    public boolean verificarFecha(String fecha) {
        //Se verifica el formato de la fecha usando una expresión regular
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
     * @param anio
     * @return
     */
    public boolean esAnioBisiesto(int anio) {
        return (anio % 4 == 0 && anio % 100 != 0) || (anio % 400 == 0);
    }
    
    private boolean dniExistente(String dni) {
        try {
            Connection conexion = ConexionBD.obtenerConexion();
            String consulta = "SELECT COUNT(*) FROM usuarios WHERE DNI = ?";
            PreparedStatement statement = conexion.prepareStatement(consulta);
            statement.setString(1, dni);
            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                int count = rs.getInt(1);
                return count > 0;
            }

            statement.close();
            conexion.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return false;
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
        jLabelNombre = new javax.swing.JLabel();
        jLabelApellidos = new javax.swing.JLabel();
        jLabelDNI = new javax.swing.JLabel();
        jLabelTelefono = new javax.swing.JLabel();
        jLabelEmail = new javax.swing.JLabel();
        jLabelFechaNacimiento = new javax.swing.JLabel();
        jTextFieldNombre = new javax.swing.JTextField();
        jTextFieldApellidos = new javax.swing.JTextField();
        jTextFieldDNI = new javax.swing.JTextField();
        jTextFieldEmail = new javax.swing.JTextField();
        jTextFieldTelefono = new javax.swing.JTextField();
        jTextFieldFechaNacimiento = new javax.swing.JTextField();
        jButtonCancelar = new javax.swing.JButton();
        jButtonGuardarUsuario = new javax.swing.JButton();
        jLabelVerificacionNombre = new javax.swing.JLabel();
        jLabelVerificacionApellido = new javax.swing.JLabel();
        jLabelVerificacionDNI = new javax.swing.JLabel();
        jLabelVerificacionTelefono = new javax.swing.JLabel();
        jLabelVerificacionEmail = new javax.swing.JLabel();
        jLabelVerificacionFechaNacimiento = new javax.swing.JLabel();

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
        jLabelTituloVentana.setText("ALTA DE NUEVO USUARIO");

        jLabelNombre.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabelNombre.setText("Nombre");

        jLabelApellidos.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabelApellidos.setText("Apellidos");

        jLabelDNI.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabelDNI.setText("DNI");

        jLabelTelefono.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabelTelefono.setText("Telefono");

        jLabelEmail.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabelEmail.setText("Email");

        jLabelFechaNacimiento.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabelFechaNacimiento.setText("F.Nacimiento");

        jTextFieldNombre.setToolTipText("Introducir nombre (Solo letras y espacios)");
        jTextFieldNombre.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldNombreActionPerformed(evt);
            }
        });

        jTextFieldApellidos.setToolTipText("Introducir apellidos (Solo letras y espacios)");
        jTextFieldApellidos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldApellidosActionPerformed(evt);
            }
        });

        jTextFieldDNI.setToolTipText("Introducir apellidos (Solo letras y espacios)");
        jTextFieldDNI.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldDNIActionPerformed(evt);
            }
        });

        jTextFieldEmail.setToolTipText("Introducir email");
        jTextFieldEmail.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldEmailActionPerformed(evt);
            }
        });

        jTextFieldTelefono.setToolTipText("Introducir teléfono (9 u 11 dígitos)");
        jTextFieldTelefono.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldTelefonoActionPerformed(evt);
            }
        });

        jTextFieldFechaNacimiento.setToolTipText("Introduce fecha de nacimiento válida");
        jTextFieldFechaNacimiento.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldFechaNacimientoActionPerformed(evt);
            }
        });

        jButtonCancelar.setText("Cancelar");
        jButtonCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCancelarActionPerformed(evt);
            }
        });

        jButtonGuardarUsuario.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButtonGuardarUsuario.setText("GUARDAR USUARIO");
        jButtonGuardarUsuario.setToolTipText("");
        jButtonGuardarUsuario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonGuardarUsuarioActionPerformed(evt);
            }
        });

        jLabelVerificacionNombre.setForeground(new java.awt.Color(255, 51, 51));

        jLabelVerificacionApellido.setForeground(new java.awt.Color(255, 0, 0));
        jLabelVerificacionApellido.setToolTipText("");

        jLabelVerificacionDNI.setForeground(new java.awt.Color(255, 0, 0));
        jLabelVerificacionDNI.setToolTipText("");

        jLabelVerificacionTelefono.setForeground(new java.awt.Color(255, 0, 0));
        jLabelVerificacionTelefono.setToolTipText("");

        jLabelVerificacionEmail.setForeground(new java.awt.Color(255, 0, 0));

        jLabelVerificacionFechaNacimiento.setForeground(new java.awt.Color(255, 0, 0));

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
                        .addGap(85, 85, 85)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButtonGuardarUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 209, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButtonCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabelNombre)
                                    .addComponent(jLabelApellidos)
                                    .addComponent(jLabelDNI))
                                .addGap(69, 69, 69)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jTextFieldDNI, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabelVerificacionDNI))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(jTextFieldNombre, javax.swing.GroupLayout.DEFAULT_SIZE, 223, Short.MAX_VALUE)
                                            .addComponent(jTextFieldApellidos))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabelVerificacionApellido)
                                            .addComponent(jLabelVerificacionNombre)))))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabelFechaNacimiento)
                                    .addComponent(jLabelTelefono)
                                    .addComponent(jLabelEmail))
                                .addGap(45, 45, 45)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(jTextFieldEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 197, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jTextFieldTelefono, javax.swing.GroupLayout.PREFERRED_SIZE, 197, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabelVerificacionTelefono)
                                            .addComponent(jLabelVerificacionEmail)))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jTextFieldFechaNacimiento, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabelVerificacionFechaNacimiento)))))
                        .addContainerGap(402, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(33, 33, 33)
                        .addComponent(jLabelTituloVentana)
                        .addGap(0, 0, Short.MAX_VALUE))))
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
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabelNombre)
                            .addComponent(jTextFieldNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabelVerificacionNombre))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabelApellidos)
                            .addComponent(jTextFieldApellidos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabelVerificacionApellido))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextFieldDNI, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabelDNI)
                            .addComponent(jLabelVerificacionDNI))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabelFechaNacimiento)
                            .addComponent(jTextFieldFechaNacimiento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabelVerificacionFechaNacimiento))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabelTelefono)
                            .addComponent(jTextFieldTelefono, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabelVerificacionTelefono))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextFieldEmail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabelEmail)
                            .addComponent(jLabelVerificacionEmail))
                        .addGap(77, 77, 77)
                        .addComponent(jButtonGuardarUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButtonCancelar))))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonNuevoUsuarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonNuevoUsuarioActionPerformed
        //TODO add your handling code here:
    }//GEN-LAST:event_jButtonNuevoUsuarioActionPerformed

    private void jTextFieldNombreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldNombreActionPerformed
        
    }//GEN-LAST:event_jTextFieldNombreActionPerformed

    private void jTextFieldApellidosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldApellidosActionPerformed
        //TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldApellidosActionPerformed

    private void jTextFieldEmailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldEmailActionPerformed
        //TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldEmailActionPerformed

    private void jTextFieldTelefonoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldTelefonoActionPerformed
        //TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldTelefonoActionPerformed

    private void jTextFieldFechaNacimientoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldFechaNacimientoActionPerformed
        //TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldFechaNacimientoActionPerformed

    private void jButtonGuardarUsuarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonGuardarUsuarioActionPerformed
        if (verificarDatos()) {
            guardarDatos();
            JOptionPane.showMessageDialog(this, "Usuario creado correctamente", "Info", JOptionPane.INFORMATION_MESSAGE);
            GimApp nuevoFrame = new GimApp();
            nuevoFrame.setLocation(this.getLocation());
            nuevoFrame.setVisible(true);
            this.dispose();
        } 
    }//GEN-LAST:event_jButtonGuardarUsuarioActionPerformed

    private void jButtonCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCancelarActionPerformed
        GimApp nuevoFrame = new GimApp();
        nuevoFrame.setLocation(this.getLocation()); //Establece la ubicación del nuevo JFrame igual a la del actual
        nuevoFrame.setVisible(true);
        this.dispose(); 
    }//GEN-LAST:event_jButtonCancelarActionPerformed

    private void jTextFieldDNIActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldDNIActionPerformed
        //TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldDNIActionPerformed

    private void jButtonGestionUsuariosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonGestionUsuariosActionPerformed
        GestionUsuarios nuevoFrame = new GestionUsuarios();
        nuevoFrame.setLocation(this.getLocation()); //Establece la ubicación del nuevo JFrame igual a la del actual
        nuevoFrame.setVisible(true);
        this.dispose(); 
    }//GEN-LAST:event_jButtonGestionUsuariosActionPerformed

    private void jButtonNuevoAbonoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonNuevoAbonoActionPerformed
        AltaAbono nuevoFrame = new AltaAbono();
        nuevoFrame.setLocation(this.getLocation()); //Establece la ubicación del nuevo JFrame igual a la del actual
        nuevoFrame.setVisible(true);
        this.dispose(); 
    }//GEN-LAST:event_jButtonNuevoAbonoActionPerformed

    private void jButtonInicioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonInicioActionPerformed
        GimApp nuevoFrame = new GimApp();
        nuevoFrame.setLocation(this.getLocation()); //Establece la ubicación del nuevo JFrame igual a la del actual
        nuevoFrame.setVisible(true);
        this.dispose(); 
    }//GEN-LAST:event_jButtonInicioActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonCancelar;
    private javax.swing.JButton jButtonGestionUsuarios;
    private javax.swing.JButton jButtonGuardarUsuario;
    private javax.swing.JButton jButtonInicio;
    private javax.swing.JButton jButtonNuevoAbono;
    private javax.swing.JButton jButtonNuevoUsuario;
    private javax.swing.JLabel jLabelApellidos;
    private javax.swing.JLabel jLabelDNI;
    private javax.swing.JLabel jLabelEmail;
    private javax.swing.JLabel jLabelFechaNacimiento;
    private javax.swing.JLabel jLabelNombre;
    private javax.swing.JLabel jLabelTelefono;
    private javax.swing.JLabel jLabelTituloVentana;
    private javax.swing.JLabel jLabelVerificacionApellido;
    private javax.swing.JLabel jLabelVerificacionDNI;
    private javax.swing.JLabel jLabelVerificacionEmail;
    private javax.swing.JLabel jLabelVerificacionFechaNacimiento;
    private javax.swing.JLabel jLabelVerificacionNombre;
    private javax.swing.JLabel jLabelVerificacionTelefono;
    private javax.swing.JPanel jPanelHeader;
    private javax.swing.JPanel jPanelLateral;
    private javax.swing.JPanel jPanelLogo;
    private javax.swing.JTextField jTextFieldApellidos;
    private javax.swing.JTextField jTextFieldDNI;
    private javax.swing.JTextField jTextFieldEmail;
    private javax.swing.JTextField jTextFieldFechaNacimiento;
    private javax.swing.JTextField jTextFieldNombre;
    private javax.swing.JTextField jTextFieldTelefono;
    // End of variables declaration//GEN-END:variables
}