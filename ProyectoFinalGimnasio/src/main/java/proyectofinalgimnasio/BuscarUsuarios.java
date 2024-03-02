/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package proyectofinalgimnasio;

import java.awt.Container;
import java.awt.Frame;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableModel;


/**
 *
 * @author Sebastián Melgar Marín
 */
public class BuscarUsuarios extends javax.swing.JDialog {
    private String dniUsuarioSeleccionado;
    /**
     * Creates new form BuscarUsuarios
     */
    public BuscarUsuarios(java.awt.Frame parent, boolean modal) throws SQLException {
        super(parent, modal);
        initComponents();
        mostrarUltimosRegistros();
    }

    public void mostrarUltimosRegistros() throws SQLException {
            Connection conexion = ConexionBD.obtenerConexion();

            //Consulta SQL para obtener los últimos registros
            String consulta = "SELECT nombre, apellidos, DNI, telefono, email "
                            + "FROM usuarios "
                            + "ORDER BY fecha_nacimiento DESC "
                            + "LIMIT 10"; // Mostrar los últimos 4 registros

            try {
                PreparedStatement ps = conexion.prepareStatement(consulta);
                ResultSet rs = ps.executeQuery();

                // Limpiar la tabla antes de agregar nuevos datos
                DefaultTableModel modelo = (DefaultTableModel) jTableResultados.getModel();
                modelo.setRowCount(0);

                //Llenar la tabla con los resultados de la consulta
                while (rs.next()) {
                    Object[] fila = {
                        rs.getString("nombre"),
                        rs.getString("apellidos"),
                        rs.getString("DNI"),
                        rs.getString("telefono"),
                        rs.getString("email")
                    };
                    modelo.addRow(fila);
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            } finally {
                ConexionBD.cerrarConexion(conexion);
            }
        }
         
    public void buscarUsuarios(String criterio, String busqueda) throws SQLException {
        //Obtener la conexión a la base de datos
        Connection conexion = ConexionBD.obtenerConexion();

        //Consulta SQL para buscar usuarios según el criterio seleccionado
        String consulta = "SELECT nombre, apellidos, DNI, telefono, email "
                        + "FROM usuarios "
                        + "WHERE " + criterio + " LIKE ?";

        try {
            PreparedStatement ps = conexion.prepareStatement(consulta);
            ps.setString(1, "%" + busqueda + "%");
            ResultSet rs = ps.executeQuery();

            //Limpiar la tabla antes de agregar nuevos datos
            DefaultTableModel modelo = (DefaultTableModel) jTableResultados.getModel();
            modelo.setRowCount(0);

            //Llenar la tabla con los resultados de la búsqueda
            while (rs.next()) {
                Object[] fila = {
                    rs.getString("nombre"),
                    rs.getString("apellidos"),
                    rs.getString("DNI"),
                    rs.getString("telefono"),
                    rs.getString("email")
                };
                modelo.addRow(fila);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            // Cerrar la conexión
            ConexionBD.cerrarConexion(conexion);
        }
    }
    
    public String getDNIUsuarioSeleccionado() {
        return dniUsuarioSeleccionado; // Suponiendo que 'dniUsuarioSeleccionado' es el DNI del usuario seleccionado
    }
    
    private void jTableResultadosMouseClicked(java.awt.event.MouseEvent evt) {                                              
        int filaSeleccionada = jTableResultados.getSelectedRow();
        if (filaSeleccionada != -1) {
            dniUsuarioSeleccionado = (String) jTableResultados.getValueAt(filaSeleccionada, 2); // El DNI está en la tercera columna (índice 2)
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

        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTableResultados = new javax.swing.JTable();
        jComboBoxCriterioBusqueda = new javax.swing.JComboBox<>();
        jTextFieldBusqueda = new javax.swing.JTextField();
        jLabelCriterio = new javax.swing.JLabel();
        jButtonBuscar = new javax.swing.JButton();
        jButtonBuscarUltimasAltas = new javax.swing.JButton();
        jLabelEtiquetaBusqueda = new javax.swing.JLabel();
        jButtonSeleccionar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel1.setText("BÚSQUEDA DE USUARIOS");

        jTableResultados.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Nombre", "Apellidos", "DNI", "TELEFONO", "EMAIL"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, true, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(jTableResultados);

        jComboBoxCriterioBusqueda.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "DNI", "Telefono", "Email" }));

        jTextFieldBusqueda.setToolTipText("campo a rellenar con el criterio de búsqueda");

        jLabelCriterio.setText("CRITERIO DE BÚSQUEDA:");

        jButtonBuscar.setText("Buscar");
        jButtonBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonBuscarActionPerformed(evt);
            }
        });

        jButtonBuscarUltimasAltas.setText("Mostrar últimas altas");
        jButtonBuscarUltimasAltas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonBuscarUltimasAltasActionPerformed(evt);
            }
        });

        jLabelEtiquetaBusqueda.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabelEtiquetaBusqueda.setText("ULTIMAS ALTAS:");

        jButtonSeleccionar.setText("SELECCIONAR");
        jButtonSeleccionar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSeleccionarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabelEtiquetaBusqueda)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jTextFieldBusqueda, javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabelCriterio)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jComboBoxCriterioBusqueda, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonBuscar))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(jLabel1)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButtonBuscarUltimasAltas))
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 711, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jButtonSeleccionar, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(39, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(16, 16, 16)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabelCriterio)
                            .addComponent(jComboBoxCriterioBusqueda, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jButtonBuscarUltimasAltas, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFieldBusqueda, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonBuscar))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabelEtiquetaBusqueda)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 328, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButtonSeleccionar)
                .addContainerGap(20, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonBuscarActionPerformed
    String criterio = jComboBoxCriterioBusqueda.getSelectedItem().toString();
    String busqueda = jTextFieldBusqueda.getText().trim();
        try {
            buscarUsuarios(criterio, busqueda);
        } catch (SQLException ex) {
            Logger.getLogger(BuscarUsuarios.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButtonBuscarActionPerformed

    private void jButtonBuscarUltimasAltasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonBuscarUltimasAltasActionPerformed
        try {
            mostrarUltimosRegistros();
        } catch (SQLException ex) {
            Logger.getLogger(BuscarUsuarios.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButtonBuscarUltimasAltasActionPerformed

    private void jButtonSeleccionarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSeleccionarActionPerformed
    // Obtener la instancia del padre
      Container parent = getParent();
      UsuarioSeleccionadoListener listener = null;

      // Verificar si el padre implementa UsuarioSeleccionadoListener
      if (parent instanceof UsuarioSeleccionadoListener) {
          listener = (UsuarioSeleccionadoListener) parent;
      }

      // Asegúrate de que hay una fila seleccionada
      int filaSeleccionada = jTableResultados.getSelectedRow();
      if (filaSeleccionada != -1 && listener != null) {
          // Obtener el nombre, apellidos y DNI del usuario seleccionado
          String nombre = (String) jTableResultados.getValueAt(filaSeleccionada, 0); // Nombre en la primera columna
          String apellidos = (String) jTableResultados.getValueAt(filaSeleccionada, 1); // Apellidos en la segunda columna
          String dni = (String) jTableResultados.getValueAt(filaSeleccionada, 2); // DNI en la tercera columna

          // Llamar al método correspondiente del listener para manejar la selección del usuario
          listener.onUsuarioSeleccionado(nombre, apellidos, dni);
      } 
      dispose();
    }//GEN-LAST:event_jButtonSeleccionarActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(BuscarUsuarios.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(BuscarUsuarios.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(BuscarUsuarios.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(BuscarUsuarios.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                BuscarUsuarios dialog = null;
                try {
                    dialog = new BuscarUsuarios(new javax.swing.JFrame(), true);
                } catch (SQLException ex) {
                    Logger.getLogger(BuscarUsuarios.class.getName()).log(Level.SEVERE, null, ex);
                }
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonBuscar;
    private javax.swing.JButton jButtonBuscarUltimasAltas;
    private javax.swing.JButton jButtonSeleccionar;
    private javax.swing.JComboBox<String> jComboBoxCriterioBusqueda;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabelCriterio;
    private javax.swing.JLabel jLabelEtiquetaBusqueda;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTableResultados;
    private javax.swing.JTextField jTextFieldBusqueda;
    // End of variables declaration//GEN-END:variables
}
