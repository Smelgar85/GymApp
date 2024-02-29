/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package proyectofinalgimnasio;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author Sebastián Melgar Marín
 */
public class AltaAbono extends javax.swing.JFrame {

  public AltaAbono() {
        initComponents();
        jButtonGuardarAbono.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                verificarDatos();
            }
        });

        // Agregar escuchadores de eventos a los campos relacionados con el importe
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
  
    private void verificarFechaInicio() {
        String fechaInicioStr = jTextFieldFechaInicio.getText();
        
        // Validar formato de la fecha de inicio
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        try {
            LocalDate.parse(fechaInicioStr, formatter);
            jLabelVerificadorFechaInicio.setText("");
        } catch (Exception e) {
            jLabelVerificadorFechaInicio.setText("Fecha de inicio inválida");
        }
    }

    private void calcularFechaFin() {
        String fechaInicioStr = jTextFieldFechaInicio.getText();
        String numeroMesesStr = jTextFieldMeses.getText();
        
        // Validar formato de la fecha de inicio
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate fechaInicio = null;
        try {
            fechaInicio = LocalDate.parse(fechaInicioStr, formatter);
        } catch (Exception e) {
            jTextFieldFechaFin.setText("Fecha de inicio inválida");
            return;
        }

        // Validar número de meses
        Pattern pattern = Pattern.compile("^(?:[1-9]|1[0-2])$");
        Matcher matcher = pattern.matcher(numeroMesesStr);
        if (!matcher.matches()) {
            jTextFieldFechaFin.setText("Número de meses inválido");
            return;
        }
        // Calcular fecha de fin y actualizar el campo correspondiente
        int numeroMeses = Integer.parseInt(numeroMesesStr);
        LocalDate fechaFin = fechaInicio.plusMonths(numeroMeses);
        String fechaFinStr = fechaFin.format(formatter);
        jTextFieldFechaFin.setText(fechaFinStr);
    }

 
  private void calcularPrecioTotal() {
        String precioMensual = (String) jComboBoxPrecioMensual.getSelectedItem();
        String descuento = (String) jComboBoxDescuento.getSelectedItem();
        String numeroMeses = jTextFieldMeses.getText();
        boolean camposCorrectos = true;
        
        // Validar el número de meses ingresado
        Pattern pattern = Pattern.compile("^(?:[1-9]|1[0-2])$");
        Matcher matcher = pattern.matcher(numeroMeses);
        
        if (!matcher.matches()) {
            jLabelPrecioTotal.setText("Número de meses inválido");
            camposCorrectos = false;
        }
        
        // Calcular el precio total si todos los campos son correctos
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
        jPanelAyuda = new javax.swing.JPanel();
        jButtonAyuda = new javax.swing.JButton();
        jPanelHeader = new javax.swing.JPanel();
        jPanelLogo = new javax.swing.JPanel();
        jLabelTituloVentana = new javax.swing.JLabel();
        jLabelUsuario = new javax.swing.JLabel();
        jLabelMeses = new javax.swing.JLabel();
        jLabelPrecioMensual = new javax.swing.JLabel();
        jLabelDescuento = new javax.swing.JLabel();
        jLabelFechaInicio = new javax.swing.JLabel();
        jTextFieldUsuario = new javax.swing.JTextField();
        jTextFieldMeses = new javax.swing.JTextField();
        jTextFieldFechaInicio = new javax.swing.JTextField();
        jButtonCancelar = new javax.swing.JButton();
        jButtonGuardarAbono = new javax.swing.JButton();
        jLabelVerificacionNombre = new javax.swing.JLabel();
        jLabelVerificacionApellido = new javax.swing.JLabel();
        jLabelVerificacionTelefono = new javax.swing.JLabel();
        jLabelVerificacionEmail = new javax.swing.JLabel();
        jLabelVerificacionFechaNacimiento = new javax.swing.JLabel();
        jLabelFechaFin = new javax.swing.JLabel();
        jTextFieldFechaFin = new javax.swing.JTextField();
        jComboBoxPrecioMensual = new javax.swing.JComboBox<>();
        jComboBoxDescuento = new javax.swing.JComboBox<>();
        jButtonBuscarUsuario = new javax.swing.JButton();
        jCheckBox1 = new javax.swing.JCheckBox();
        jLabel1 = new javax.swing.JLabel();
        jLabelPrecioTotal = new javax.swing.JLabel();
        jLabelVerificadorFechaInicio = new javax.swing.JLabel();

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

        jButtonNuevoAbono.setText("NUEVO ABONO");

        jPanelAyuda.setBackground(new java.awt.Color(255, 102, 102));
        jPanelAyuda.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));

        jButtonAyuda.setText("AYUDA");
        jButtonAyuda.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAyudaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanelAyudaLayout = new javax.swing.GroupLayout(jPanelAyuda);
        jPanelAyuda.setLayout(jPanelAyudaLayout);
        jPanelAyudaLayout.setHorizontalGroup(
            jPanelAyudaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelAyudaLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButtonAyuda, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanelAyudaLayout.setVerticalGroup(
            jPanelAyudaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelAyudaLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButtonAyuda, javax.swing.GroupLayout.DEFAULT_SIZE, 33, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanelLateralLayout = new javax.swing.GroupLayout(jPanelLateral);
        jPanelLateral.setLayout(jPanelLateralLayout);
        jPanelLateralLayout.setHorizontalGroup(
            jPanelLateralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelLateralLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelLateralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jButtonNuevoAbono, javax.swing.GroupLayout.DEFAULT_SIZE, 170, Short.MAX_VALUE)
                    .addComponent(jButtonGestionUsuarios, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButtonNuevoUsuario, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
            .addComponent(jPanelAyuda, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanelLateralLayout.setVerticalGroup(
            jPanelLateralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelLateralLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButtonNuevoUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonGestionUsuarios, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonNuevoAbono, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 277, Short.MAX_VALUE)
                .addComponent(jPanelAyuda, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
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
            .addGap(0, 62, Short.MAX_VALUE)
        );

        jPanelLogo.setBackground(new java.awt.Color(255, 102, 102));
        jPanelLogo.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        javax.swing.GroupLayout jPanelLogoLayout = new javax.swing.GroupLayout(jPanelLogo);
        jPanelLogo.setLayout(jPanelLogoLayout);
        jPanelLogoLayout.setHorizontalGroup(
            jPanelLogoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanelLogoLayout.setVerticalGroup(
            jPanelLogoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
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

        jButtonCancelar.setText("Cancelar");
        jButtonCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCancelarActionPerformed(evt);
            }
        });

        jButtonGuardarAbono.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButtonGuardarAbono.setText("ACEPTAR NUEVO ABONO");
        jButtonGuardarAbono.setToolTipText("");
        jButtonGuardarAbono.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonGuardarAbonoActionPerformed(evt);
            }
        });

        jLabelVerificacionNombre.setForeground(new java.awt.Color(255, 51, 51));

        jLabelVerificacionApellido.setForeground(new java.awt.Color(255, 0, 0));

        jLabelVerificacionTelefono.setForeground(new java.awt.Color(255, 0, 0));
        jLabelVerificacionTelefono.setToolTipText("");

        jLabelVerificacionEmail.setForeground(new java.awt.Color(255, 0, 0));

        jLabelVerificacionFechaNacimiento.setForeground(new java.awt.Color(255, 0, 0));

        jLabelFechaFin.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabelFechaFin.setText("Fecha de fin");

        jTextFieldFechaFin.setEditable(false);
        jTextFieldFechaFin.setToolTipText("Introduce fecha de nacimiento válida");
        jTextFieldFechaFin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldFechaFinActionPerformed(evt);
            }
        });

        jComboBoxPrecioMensual.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "20", "30", "40" }));
        jComboBoxPrecioMensual.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxPrecioMensualActionPerformed(evt);
            }
        });

        jComboBoxDescuento.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "0", "5", "15", "25", "50", "100" }));

        jButtonBuscarUsuario.setText("Buscar...");

        jLabel1.setText("ABONO PREMIUM (10€ mensuales más)");

        jLabelPrecioTotal.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabelPrecioTotal.setText("Precio TOTAL:");

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
                            .addComponent(jButtonCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabelUsuario)
                                    .addComponent(jLabelMeses)
                                    .addComponent(jLabelPrecioMensual)
                                    .addComponent(jLabelDescuento)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabelFechaInicio)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jTextFieldFechaInicio, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabelVerificadorFechaInicio))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel1)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jCheckBox1)))
                                .addGap(12, 12, 12)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jComboBoxPrecioMensual, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(117, 117, 117)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabelVerificacionEmail)
                                            .addComponent(jLabelVerificacionTelefono)))
                                    .addComponent(jTextFieldMeses, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jComboBoxDescuento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jTextFieldUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 197, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jButtonBuscarUsuario))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(layout.createSequentialGroup()
                                                .addGap(37, 37, 37)
                                                .addComponent(jLabelPrecioTotal)
                                                .addGap(118, 118, 118))
                                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                .addComponent(jLabelFechaFin)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jTextFieldFechaFin, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                                        .addComponent(jLabelVerificacionFechaNacimiento))))
                            .addComponent(jButtonGuardarAbono, javax.swing.GroupLayout.PREFERRED_SIZE, 209, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(373, 373, 373)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabelVerificacionApellido)
                            .addComponent(jLabelVerificacionNombre))
                        .addContainerGap())))
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
                            .addComponent(jLabelUsuario)
                            .addComponent(jTextFieldUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabelVerificacionNombre)
                            .addComponent(jButtonBuscarUsuario))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabelMeses)
                            .addComponent(jTextFieldMeses, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabelVerificacionApellido))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabelPrecioMensual)
                            .addComponent(jLabelVerificacionTelefono)
                            .addComponent(jComboBoxPrecioMensual, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabelDescuento)
                            .addComponent(jLabelVerificacionEmail)
                            .addComponent(jComboBoxDescuento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabelFechaInicio)
                            .addComponent(jTextFieldFechaInicio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabelVerificacionFechaNacimiento)
                            .addComponent(jLabelFechaFin)
                            .addComponent(jTextFieldFechaFin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabelVerificadorFechaInicio))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jCheckBox1)
                            .addComponent(jLabel1))
                        .addGap(34, 34, 34)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButtonGuardarAbono, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabelPrecioTotal))
                        .addGap(18, 18, 18)
                        .addComponent(jButtonCancelar)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonNuevoUsuarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonNuevoUsuarioActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButtonNuevoUsuarioActionPerformed

    private void jButtonAyudaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAyudaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButtonAyudaActionPerformed

    private void jTextFieldUsuarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldUsuarioActionPerformed
        
    }//GEN-LAST:event_jTextFieldUsuarioActionPerformed

    private void jTextFieldFechaInicioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldFechaInicioActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldFechaInicioActionPerformed

    private void jButtonGuardarAbonoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonGuardarAbonoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButtonGuardarAbonoActionPerformed

    private void jButtonCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCancelarActionPerformed
        // TODO add your handling code here:
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

 private void verificarDatos() {
        String regexNumeroDeMeses = "^(?:[1-9]|1[0-2])$";
        String precioMensual = (String) jComboBoxPrecioMensual.getSelectedItem();
        String descuento = (String) jComboBoxDescuento.getSelectedItem();
        String numeroMeses = jTextFieldMeses.getText();
        String fechaInicio = jTextFieldFechaInicio.getText();
        boolean camposCorrectos = true;
        
        Pattern pattern = Pattern.compile(regexNumeroDeMeses);
        Matcher matcher = pattern.matcher(numeroMeses);
        
        if (!matcher.matches()) {
            jLabelVerificacionApellido.setText("Mínimo 1 mes. Máximo 12 meses.");
            camposCorrectos = false;
        } else {
            jLabelVerificacionApellido.setText("");
        }
        
        LocalDate fechaActual = LocalDate.now();
        LocalDate fechaInicioParsed = LocalDate.parse(fechaInicio, DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        
        if (fechaInicioParsed.isBefore(fechaActual)) {
            jLabelVerificacionFechaNacimiento.setText("La fecha de inicio debe ser actual o futura");
            camposCorrectos = false;
        } else {
            jLabelVerificacionFechaNacimiento.setText("");
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
            System.out.println("Los datos son correctos. Guardando abono...");
        }
    }

    private boolean esAnioBisiesto(int anio) {
        return (anio % 4 == 0 && anio % 100 != 0) || (anio % 400 == 0);
    }

 public static void main(String args[]) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(AltaAbono.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new AltaAbono().setVisible(true);
            }
        });
    }



    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonAyuda;
    private javax.swing.JButton jButtonBuscarUsuario;
    private javax.swing.JButton jButtonCancelar;
    private javax.swing.JButton jButtonGestionUsuarios;
    private javax.swing.JButton jButtonGuardarAbono;
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
    private javax.swing.JLabel jLabelVerificacionApellido;
    private javax.swing.JLabel jLabelVerificacionEmail;
    private javax.swing.JLabel jLabelVerificacionFechaNacimiento;
    private javax.swing.JLabel jLabelVerificacionNombre;
    private javax.swing.JLabel jLabelVerificacionTelefono;
    private javax.swing.JLabel jLabelVerificadorFechaInicio;
    private javax.swing.JPanel jPanelAyuda;
    private javax.swing.JPanel jPanelHeader;
    private javax.swing.JPanel jPanelLateral;
    private javax.swing.JPanel jPanelLogo;
    private javax.swing.JTextField jTextFieldFechaFin;
    private javax.swing.JTextField jTextFieldFechaInicio;
    private javax.swing.JTextField jTextFieldMeses;
    private javax.swing.JTextField jTextFieldUsuario;
    // End of variables declaration//GEN-END:variables
}