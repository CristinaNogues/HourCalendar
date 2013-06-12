/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hourcalendar;

import com.sun.java.swing.plaf.windows.WindowsTabbedPaneUI;
//import com.sun.java.swing.plaf.windows.WindowsTableHeaderUI;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.util.Vector;
import javax.swing.Box;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
//import javax.swing.plaf.basic.BasicTabbedPaneUI;

/**
 *
 * @author admin
 */
public class MainFrame extends javax.swing.JFrame {
    static javax.swing.JFrame frameCalendari;
    public JPanel ContentPane;
    public ControlProgres controlProgres;
    public boolean showTabsHeader = false;
    public FormulariOpcions formulariOpcions;
    /**
     * Creates new form MainFrame
     */
    public MainFrame() {
        initComponents();
        setExtendedState( getExtendedState()|JFrame.MAXIMIZED_BOTH );
        
        formulariOpcions = new FormulariOpcions();
        controlProgres = new ControlProgres(this);
        controlProgres.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        controlProgres.setVisible(false);

        ContentPane = new JPanel();
        ContentPane.setLayout(new GridBagLayout());
        ContentPane.setPreferredSize(new Dimension(Contenidor.getWidth(), Contenidor.getHeight()));
        //ContentPane.setBackground(Color.YELLOW);
        JLabel label = new JLabel("");
        ContentPane.add(label);
        Contenidor.setViewportView(ContentPane);
        //Contenidor.setBackground(Color.YELLOW);
        //Contenidor.setVisible(false);
        
        TabbedPane.setUI(new WindowsTabbedPaneUI() {
			@Override
			protected int calculateTabAreaHeight(int tabPlacement, int horizRunCount, int maxTabHeight) {
                HourCalendar.getBase().dbgUI("CALCULATETABAREAHEIGHT".concat(String.valueOf(tabPlacement)).concat(" - ").concat(String.valueOf(horizRunCount)));
				if (showTabsHeader) {
					return super.calculateTabAreaHeight(tabPlacement, horizRunCount, maxTabHeight);
				} else {
					return 0;
				}
			}
            @Override
            protected void paintTab(Graphics g, int tabPlacement, Rectangle[] rects, int tabIndex, Rectangle iconRect, Rectangle textRect) {
                if (showTabsHeader) {
                    super.paintTab(g, tabPlacement, rects, tabIndex, iconRect, textRect);
                }
            }

		});

        
        //Contenidor.setLayout(new GridLayout(3,1));
        updateAssignatures();
    }
    
    public void updateAssignatures() {
        PanellAssignatures.removeAll();
        Base base = HourCalendar.getBase();
        for (int i = base.getNumAssignatures(); --i >= 0;) {
            AssignaturaPane a = new AssignaturaPane(base.getAssignaturaAt(i));  
            a.setSize(300,50);  
            a.setVisible(true);  
            PanellAssignatures.add(a);
            PanellAssignatures.add(Box.createRigidArea(new Dimension(0,3)));
        }
        PanellAssignatures.revalidate();
        PanellAssignatures.repaint();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        PopupCrear = new javax.swing.JPopupMenu();
        CrearAssignatura = new javax.swing.JMenuItem();
        CrearGrau = new javax.swing.JMenuItem();
        CrearTipusAula = new javax.swing.JMenuItem();
        CrearTipusMateria = new javax.swing.JMenuItem();
        CrearAula = new javax.swing.JMenuItem();
        TabbedPane = new javax.swing.JTabbedPane();
        TabAssignatures = new javax.swing.JScrollPane();
        jPanel4 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        BotoCrear = new javax.swing.JButton();
        BotoGenerarHorari = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        PanellAssignatures = new javax.swing.JPanel();
        Contenidor = new javax.swing.JScrollPane();
        TabInforme = new javax.swing.JScrollPane();
        Informe = new javax.swing.JEditorPane();

        PopupCrear.setBackground(new java.awt.Color(255, 51, 51));
        PopupCrear.setInvoker(BotoCrear);
        PopupCrear.setLabel("asdasd");
        PopupCrear.setMinimumSize(new java.awt.Dimension(100, 50));

        CrearAssignatura.setText("Assignatura");
        CrearAssignatura.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CrearAssignaturaActionPerformed(evt);
            }
        });
        PopupCrear.add(CrearAssignatura);

        CrearGrau.setText("Grau");
        CrearGrau.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CrearGrauActionPerformed(evt);
            }
        });
        PopupCrear.add(CrearGrau);

        CrearTipusAula.setText("Tipus d'aula");
        CrearTipusAula.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CrearTipusAulaActionPerformed(evt);
            }
        });
        PopupCrear.add(CrearTipusAula);

        CrearTipusMateria.setText("Tipus de matèria");
        CrearTipusMateria.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CrearTipusMateriaActionPerformed(evt);
            }
        });
        PopupCrear.add(CrearTipusMateria);

        CrearAula.setText("Aula");
        CrearAula.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CrearAulaActionPerformed(evt);
            }
        });
        PopupCrear.add(CrearAula);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("HourCalendar");
        getContentPane().setLayout(new java.awt.GridBagLayout());

        TabbedPane.setFont(new java.awt.Font("Verdana", 0, 11)); // NOI18N

        TabAssignatures.setBackground(new java.awt.Color(255, 255, 255));
        TabAssignatures.setBorder(null);
        TabAssignatures.setPreferredSize(new java.awt.Dimension(400, 300));

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));

        jPanel5.setOpaque(false);

        BotoCrear.setText("Crear");
        BotoCrear.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                BotoCrearMouseClicked(evt);
            }
        });

        BotoGenerarHorari.setText("Generar horari");
        BotoGenerarHorari.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                BotoGenerarHorariMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap(242, Short.MAX_VALUE)
                .addComponent(BotoCrear, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(BotoGenerarHorari))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(BotoCrear)
                .addComponent(BotoGenerarHorari))
        );

        jLabel1.setFont(new java.awt.Font("Verdana", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(102, 102, 102));
        jLabel1.setText("Assignatures:");

        PanellAssignatures.setBackground(new java.awt.Color(255, 255, 204));
        PanellAssignatures.setOpaque(false);
        PanellAssignatures.setLayout(new javax.swing.BoxLayout(PanellAssignatures, javax.swing.BoxLayout.PAGE_AXIS));

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(PanellAssignatures, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(PanellAssignatures, javax.swing.GroupLayout.DEFAULT_SIZE, 466, Short.MAX_VALUE)
                .addContainerGap())
        );

        TabAssignatures.setViewportView(jPanel4);

        TabbedPane.addTab("Assignatures", TabAssignatures);

        Contenidor.setBackground(new java.awt.Color(255, 255, 255));
        Contenidor.setBorder(null);
        TabbedPane.addTab("Horari generat", Contenidor);

        TabInforme.setBackground(new java.awt.Color(255, 255, 255));
        TabInforme.setBorder(null);

        Informe.setEditable(false);
        Informe.setBorder(null);
        Informe.setContentType("text/html"); // NOI18N
        Informe.setFont(new java.awt.Font("Verdana", 0, 11)); // NOI18N
        TabInforme.setViewportView(Informe);

        TabbedPane.addTab("Informe de resultats", TabInforme);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        getContentPane().add(TabbedPane, gridBagConstraints);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void CrearAssignaturaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CrearAssignaturaActionPerformed
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FormulariAssignatura().setVisible(true);
            }
        });
    }//GEN-LAST:event_CrearAssignaturaActionPerformed

    private void BotoCrearMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BotoCrearMouseClicked
        // TODO add your handling code here:
        PopupCrear.show(BotoCrear, 0, BotoCrear.getHeight());
    }//GEN-LAST:event_BotoCrearMouseClicked

    private void CrearGrauActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CrearGrauActionPerformed
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FormulariGrau().setVisible(true);
            }
        });
    }//GEN-LAST:event_CrearGrauActionPerformed

    private void CrearTipusAulaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CrearTipusAulaActionPerformed
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FormulariTipusAula().setVisible(true);
            }
        });
    }//GEN-LAST:event_CrearTipusAulaActionPerformed

    private void CrearTipusMateriaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CrearTipusMateriaActionPerformed
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FormulariTipusMateria().setVisible(true);
            }
        });
    }//GEN-LAST:event_CrearTipusMateriaActionPerformed

    private void CrearAulaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CrearAulaActionPerformed
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FormulariAula().setVisible(true);
            }
        });
    }//GEN-LAST:event_CrearAulaActionPerformed

    private void BotoGenerarHorariMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BotoGenerarHorariMouseClicked
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                formulariOpcions.setVisible(true);
            }
        });
    }//GEN-LAST:event_BotoGenerarHorariMouseClicked

    /**
     * @param args the command line arguments
     */
    /*public static void main(String args[]) {
        //Set the Nimbus look and feel
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">

        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>


        
    }*/
    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JButton BotoCrear;
    private javax.swing.JButton BotoGenerarHorari;
    public javax.swing.JScrollPane Contenidor;
    private javax.swing.JMenuItem CrearAssignatura;
    private javax.swing.JMenuItem CrearAula;
    private javax.swing.JMenuItem CrearGrau;
    private javax.swing.JMenuItem CrearTipusAula;
    private javax.swing.JMenuItem CrearTipusMateria;
    public javax.swing.JEditorPane Informe;
    public javax.swing.JPanel PanellAssignatures;
    public javax.swing.JPopupMenu PopupCrear;
    public javax.swing.JScrollPane TabAssignatures;
    private javax.swing.JScrollPane TabInforme;
    public javax.swing.JTabbedPane TabbedPane;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    // End of variables declaration//GEN-END:variables
}
