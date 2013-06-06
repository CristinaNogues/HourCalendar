/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hourcalendar;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.util.Vector;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author admin
 */
public class PanellHorari extends javax.swing.JPanel {
    private DisponibilitatHoraria disponibilitat;
    /**
     * Creates new form PanellHorari
     */
    public PanellHorari(DisponibilitatHoraria _disponibilitat) {
        initComponents();
        disponibilitat = _disponibilitat;
        //Cel·les multilinia
        for (int i = 0; i <= 5; ++i) {
            TaulaHorari.getColumnModel().getColumn(i).setCellRenderer(new TextAreaRenderer());
            TaulaHorari.getColumnModel().getColumn(i).setHeaderRenderer(new TextAreaRenderer());
        }
        
        TaulaHorari.getColumnModel().getColumn(0).setWidth(75);
        TaulaHorari.getColumnModel().getColumn(0).setPreferredWidth(75);
        TaulaHorari.getColumnModel().getColumn(0).setMinWidth(75);
        TaulaHorari.getColumnModel().getColumn(0).setMaxWidth(75);
        TaulaHorari.setRowMargin(5);

        Vector<DisponibilitatHoraria.DiaDeLaSetmana> dies = disponibilitat.getDisponibilitat();
        for (int idDia = 2; idDia <= 6; ++idDia) {
            DisponibilitatHoraria.DiaDeLaSetmana dia = dies.get(idDia);
            Vector<String> classes = reagrupar(dia.getOcupacio());
            for (int idClasse = 0; idClasse < classes.size(); ++idClasse)
                TaulaHorari.getModel().setValueAt(classes.get(idClasse), idClasse, idDia - 1);
        }
        
    }
    
    private Vector<String> reagrupar(Vector< Vector<String> > ocupacio) {
        Vector<String> classes = new Vector<String>();
        Vector< Vector<Integer> > ordres = new Vector< Vector<Integer> >();
        for (int ordre = 0; ordre <= 3; ++ordre) {
            Vector<String> reserves = ocupacio.get(ordre);
            for (int i = 0; i < reserves.size(); ++i) {
                String reserva = reserves.get(i);
                if (classes.indexOf(reserva) == -1) {
                    classes.add(reserva);
                    ordres.add(new Vector<Integer>());
                }
                //Afegim ordre a classe
                ordres.get(classes.indexOf(reserva)).add(ordre);
            }
        }
        //Ara ja tenim les differents classes impartides durant el dia i en els ordres en que s'imparteixen
        //Les agrupem en ordre
        Vector<ClasseAgrupada> classesAgrupades = new Vector<ClasseAgrupada>();
        for (int i = 0; i < classes.size(); ++i)
            classesAgrupades.add(new ClasseAgrupada(classes.get(i), ordres.get(i)));
        
        Vector<String> resultat = new Vector<String>();
        String newLine = System.getProperty("line.separator");
        //for (int i = 0; i < classesAgrupades.size(); ++i) {
        while (classesAgrupades.size() > 0) {
            ClasseAgrupada c = classesAgrupades.firstElement();
            if (c.disposicio == 15) {
                resultat.add(c.toString());
                classesAgrupades.removeElement(c);
                continue;
            }
            boolean agrupar = false;
            int i;
            for (i = 1; i < classesAgrupades.size(); ++i) {
                if (c.disposicio + classesAgrupades.get(i).disposicio == 15) {
                    agrupar = true;
                    break;
                }
            }
            
            if (!agrupar) {
                //No ha coincidit agrupar 2 per fusionar una hora omplint-la tota, provem sense omplir-la tota
                for (i = 1; i < classesAgrupades.size(); ++i) {
                    //TODO: No és correcte, esta fet per sortir del pas
                    if (c.disposicio != classesAgrupades.get(i).disposicio && c.disposicio + classesAgrupades.get(i).disposicio < 15) {
                        agrupar = true;
                        break;
                    }
                }
            }
            if (agrupar) {
                resultat.add(c.toString().concat(newLine).concat(classesAgrupades.get(i).toString()));
                classesAgrupades.removeElementAt(i);
                classesAgrupades.removeElement(c);
                continue;
            } else {
                //Probablement només quedi una... o no, però l'afegirem sola en una hora.
                resultat.add(c.toString());
                classesAgrupades.removeElement(c);
                continue;
            }
            //TODO: Les classes de pràctiques de grups diferents i assignatures differents es poden solapar!!!
        }
        
        return resultat;
    }
    
    private class ClasseAgrupada {
        public String codi;
        public int disposicio;  //15 = Sempre, 5 = :s1, 10 = :s2, 1 = :s11, 2 = :s21, ...
                                //2 ^ ordre = 1 2 4 8
        public ClasseAgrupada (String _codi, Vector<Integer> ordres) {
            disposicio = 0;
            codi = _codi;
            for (int i = 0; i < ordres.size(); ++i) {
                disposicio += Math.pow(2, ordres.get(i));
            }
        }
        
        public String toString() {
            String s = codi.concat(" :");
            if (disposicio == 15) s = codi;
            else if (disposicio == 5) s = s.concat("s1");
            else if (disposicio == 10) s = s.concat("s2");
            else if (disposicio == 1) s = s.concat("s11");
            else if (disposicio == 2) s = s.concat("s21");
            else if (disposicio == 4) s = s.concat("s12");
            else if (disposicio == 8) s = s.concat("s22");
            else {
                if (disposicio == 3) s = s.concat("s11, ").concat("s21");
                if (disposicio == 6) s = s.concat("s21, ").concat("s12");
                if (disposicio == 12) s = s.concat("s12, ").concat("s22");
                if (disposicio == 9) s = s.concat("s11, ").concat("s22");
                else {
                    if (disposicio == 11) s = s.concat("s11, ").concat("s21, ").concat("s22");
                    if (disposicio == 7) s = s.concat("s11, ").concat("s21, ").concat("s12");
                    if (disposicio == 13) s = s.concat("s11, ").concat("s12, ").concat("s22");
                    if (disposicio == 14) s = s.concat("s21, ").concat("s12, ").concat("s22");
                }
            }
            return s;
        }
    }
    
    public class TextAreaRenderer extends JTextArea implements TableCellRenderer {

        public TextAreaRenderer() {
            setLineWrap(true);
            setWrapStyleWord(true);
        }

        public Component getTableCellRendererComponent(JTable jTable, Object obj, boolean isSelected, boolean hasFocus, int row, int column) {
            String text = (String)obj;
            setText(text);
            setMargin(new Insets(3, 0, 3, 0));
            if (row == -1) {
                setBackground(new Color(52, 136, 187));
                setForeground(new Color(255, 255, 255));
                //Font font = getFont();
                //font.getF
                setFont(new Font("Arial", Font.BOLD, 14));
            } else if (text != null && !text.isEmpty() && column != 0) {
                setBackground(new Color(238, 245, 255));
                setForeground(new Color(88, 87, 146));
            } else {
                setBackground(Color.WHITE);
                setForeground(new Color(88, 87, 146));
                //setText(getSize().toString());
            }
            return this;
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

        jScrollPane1 = new javax.swing.JScrollPane();
        TaulaHorari = new javax.swing.JTable();

        TaulaHorari.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"8:30-10:30", null, null, null, null, null},
                {"10:30-12:30", null, null, null, null, null},
                {"12:30-14:30", null, null, null, null, null},
                {"15:00-17:00", null, null, null, null, null},
                {"17:00-19:00", null, null, null, null, null},
                {"19:00-21:00", null, null, null, null, null}
            },
            new String [] {
                " Hores", " Dilluns", " Dimarts", " Dimecres", " Dijous", " Divendres"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        TaulaHorari.setFillsViewportHeight(true);
        TaulaHorari.setRowHeight(48);
        jScrollPane1.setViewportView(TaulaHorari);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable TaulaHorari;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
}
