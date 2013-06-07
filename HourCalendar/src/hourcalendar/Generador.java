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

/**
 *
 * @author admin
 */
public class Generador implements Runnable {
    private DisponibilitatHoraria disponibilitat;
    private Vector<Assignatura> assignatures;
    private Vector<Item> items;
    private DisponibilitatHoraria millorDisponibilitat;
    public boolean finalitzat;
    
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

    @Override
    public void run() {
        int ponderacio = -2048;
        int millorHoresQuadrades = -1;
        int iteracions = Regles.ITERACIONS_GENERADOR.getInt();
        for (int iteracio = 0; iteracio < iteracions; ++iteracio) {
            DisponibilitatHoraria disponibilitatHorariaActual;
            int horesActual = 0;
            int horesQuadrades = 0;
            try {
                disponibilitatHorariaActual = (DisponibilitatHoraria) disponibilitat.clone();
            
                //Generem horari per a la combinació actual d'items
                for (int idItem = items.size(); --idItem >= 0;) {
                    int hores = disponibilitatHorariaActual.addReserva(items.get(idItem).hores, items.get(idItem).codi);
                    if (hores == 0) ++horesQuadrades;
                    horesActual += hores;
                }
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
        finalitzat = true;
    }
    
    public DisponibilitatHoraria get() {
        return millorDisponibilitat;
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
