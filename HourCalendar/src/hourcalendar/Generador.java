/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hourcalendar;

import hourcalendar.Base.Regles;
import java.util.Collections;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JProgressBar;
import javax.swing.SwingWorker;

/**
 *
 * @author admin
 */
public class Generador extends SwingWorker<String, Object> {
    private DisponibilitatHoraria disponibilitat;
    private Vector<Assignatura> assignatures;
    private Vector<Item> items;
    private DisponibilitatHoraria millorDisponibilitat;
    public boolean finalitzat;
    //private JProgressBar progress;
    
    public Generador() {
        finalitzat = false;
    }
    
    public Generador(DisponibilitatHoraria disponibilitat, Vector<Assignatura> assignatures) {
        this.disponibilitat = disponibilitat;
        this.assignatures = assignatures;
        finalitzat = false;
        
        items = new Vector<Item>();
        for (int i = assignatures.size(); --i >= 0;) {
            Assignatura a = assignatures.get(i);
            String codi = String.valueOf(a.codi).concat("#0#0");
            Item docencia = new Item(a.horesTeoria, codi);
            items.add(docencia);
            for (int grup = 1; grup <= a.grups; ++grup) {
                codi = String.valueOf(a.codi).concat("#").concat(String.valueOf(a.tipusHoresPractica.id).concat("#").concat(String.valueOf(grup)));
                docencia = new Item(a.horesPractica, codi);
                items.add(docencia);
            }
        }
    }

    public void inicia() {
        int ponderacio = -2048;
        int millorHoresQuadrades = -1;
        int iteracions = Regles.ITERACIONS_GENERADOR.getInt();
        Base base = HourCalendar.getBase();
        for (int iteracio = 0; iteracio < iteracions; ++iteracio) {
            base.progres++;
            //this.progress.setValue((iteracio / iteracions) * 100);
            //this.progress.update(this.progress.getGraphics());
            DisponibilitatHoraria disponibilitatHorariaActual;
            int horesActual = 0;
            int horesQuadrades = 0;
            try {
                disponibilitatHorariaActual = (DisponibilitatHoraria) disponibilitat.clone();
            
                //Generem horari per a la combinació actual d'items
                for (int idItem = items.size(); --idItem >= 0;) {
                    Base.dbg("BEFORE addReserva CALL FOR item ".concat(String.valueOf(idItem)));
                    int hores = disponibilitatHorariaActual.addReserva(items.get(idItem).hores, items.get(idItem).codi);
                    Base.dbg("AFTER addReserva CALL FOR item ".concat(String.valueOf(idItem)));
                    if (hores == 0) ++horesQuadrades;
                    horesActual += hores;
                }
                Base.dbg("END addReserva FOR ALL ITEMS");
                int ponderacioActual = disponibilitatHorariaActual.avalua();
                System.out.println("TEST PONDERACIO");
                if (Regles.PRIORITZAR_QUADRAR_HORES.get()) {
                    if (horesQuadrades > millorHoresQuadrades) {
                        System.out.println("\tMILLOR HORES QUADRADES, ponderacio actual = ".concat(String.valueOf(ponderacioActual)));
                        millorDisponibilitat = (DisponibilitatHoraria) disponibilitatHorariaActual.clone();
                        ponderacio = ponderacioActual;
                        millorHoresQuadrades = horesQuadrades;
                    } else if (horesQuadrades == millorHoresQuadrades) {
                        if (ponderacioActual > ponderacio) {
                            System.out.println("\tMILLOR HORES QUADRADES AMB PONDERACIO ACTUAL > PONDERACIO: actual = ".concat(String.valueOf(ponderacioActual)));
                            millorDisponibilitat = (DisponibilitatHoraria) disponibilitatHorariaActual.clone();
                            ponderacio = ponderacioActual;
                            millorHoresQuadrades = horesQuadrades;
                        }
                    }
                } else {
                    if (ponderacioActual > ponderacio) {
                        System.out.println("\tPONDERACIO ACTUAL > PONDERACIO: actual = ".concat(String.valueOf(ponderacioActual)));
                        millorDisponibilitat = (DisponibilitatHoraria) disponibilitatHorariaActual.clone();
                        ponderacio = ponderacioActual;

                    }
                }
                disponibilitatHorariaActual.imprimeixPonderacio();
                disponibilitatHorariaActual = null;
                System.out.println("\thoresActual = ".concat(String.valueOf(horesActual)));
                System.out.println("\thoresQuadrades = ".concat(String.valueOf(horesQuadrades)));
                //Reordenem el vector d'items
                Collections.shuffle(items);
            } catch (CloneNotSupportedException ex) {
                Logger.getLogger(Generador.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        base.informar(millorDisponibilitat.toHTML());
        finalitzat = true;
    }
    
    public DisponibilitatHoraria getHorari() {
        return millorDisponibilitat;
    }

    /*@Override
    protected void doInBackground() throws Exception {
        //throw new UnsupportedOperationException("Not supported yet.");
        inicia();
    }*/
    
    /*public void setProgressBar(JProgressBar progress) {
        this.progress = progress;
    }*/

    @Override
    protected String doInBackground() throws Exception {
        //throw new UnsupportedOperationException("Not supported yet.");
        inicia();
        return "";
    }
    
    
    /** No tinc cap nom millor, un item son les classes de docencia d'una assignatura
     * per a grup 0, un altre son les classes de pràctica per a grup 1, un altre per a grup 2, etc.
     */
    private class Item {
        public int hores;
        public String codi;
        
        public Item(int hores, String codi) {
            this.hores = hores;
            this.codi = codi;
        }
    }
    
}
