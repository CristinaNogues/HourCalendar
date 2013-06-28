package hourcalendar;

import hourcalendar.Base.Regles;
import java.util.Collections;
import java.util.Date;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingWorker;

public class Generador extends SwingWorker<String, Object> {
    private DisponibilitatHoraria disponibilitat;
    private Vector<Assignatura> assignatures;
    private Vector< Vector<Item> > itemsGlobal;
    private DisponibilitatHoraria millorDisponibilitat;
    public boolean finalitzat;
    //multi thread
    public int ponderacio = -204800;
    public int millorHoresQuadrades = -1;
    public int iteracions = Regles.ITERACIONS_GENERADOR.getInt();
    private int numThreads = Runtime.getRuntime().availableProcessors();
    private boolean utilitzarThreads = Regles.THREADS_EN_PARALEL.get();
    
    public Generador() {
        finalitzat = false;
    }
    
    public Generador(DisponibilitatHoraria disponibilitat, Vector<Assignatura> assignatures) {
        itemsGlobal = new Vector< Vector<Item> >();
        this.disponibilitat = disponibilitat;
        this.assignatures = assignatures;
        finalitzat = false;
        
        int numItemsGlobal = (utilitzarThreads) ? numThreads : 1;
        //TODO: clonar items del primer per a la resta
        for (int thread = 0; thread < numItemsGlobal; ++thread) {
            Vector<Item> items = new Vector<Item>();
            for (int i = assignatures.size(); --i >= 0;) {
                Assignatura a = assignatures.get(i);
                String codi = String.valueOf(a.codi).concat("#0#0");
                Item docencia = new Item(a.horesTeoria, codi);
                items.add(docencia);
                for (int grup = 1; grup <= a.grups; ++grup) {
                    codi = String.valueOf(a.codi).concat("#").concat(String.valueOf(a.getTipusHoresPractica().getID()).concat("#").concat(String.valueOf(grup)));
                    docencia = new Item(a.horesPractica, codi);
                    items.add(docencia);
                }
            }
            itemsGlobal.add(items);
        }
    }

    public void inicia() {
        Base base = HourCalendar.getBase();
        //int numThreads = 4;//Thread.getAllStackTraces().size();
        Vector<Thread> threads = new Vector<Thread>();
        //Inicialitzem el vector de threads
        for (int i = 0; i < numThreads; ++i) {
            threads.add(new Thread());
        }

        boolean waiting = false;

        for (int iteracio = 0; iteracio < iteracions; ++iteracio) {
            waiting = true;
            
            while (waiting) {
                //System.out.println("While (waiting)");
                for (int i = 0; i < numThreads; ++i) {
                    if (!threads.get(i).isAlive()) {
                        //System.out.println("Arranquem el thread: ".concat(String.valueOf(i)));
                        threads.set(i, new Thread(new GeneradorThread(i)));
                        //threads.get(i).setParent(this);
                        threads.get(i).start();
                        waiting = false;
                        base.progres++;
                        
                        break;
                    }
                }
                //si tots estan treballant i no s'ha iniciat cap aquesta iteració, adormim el procés
                if (waiting) {
                    try {
                        //System.out.println("wait");
                        synchronized (this) {
                            this.wait(50);
                        }
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Generador.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
        //esperem a que els últims threads finalitzin
        for (int i = 0; i < numThreads; ++i) {
            while (threads.get(i).isAlive()) {
                synchronized (this) {
                    //System.out.println("esperem a que els últims threads finalitzin");
                    try {
                        synchronized (this) {
                            this.wait(50);
                        }
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Generador.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
        
        //base.informar(millorDisponibilitat.toHTML());
        
    }
    
    public void iniciaSenseThreads() {
        Base base = HourCalendar.getBase();
        Vector<Item> items = itemsGlobal.get(0);
        for (int iteracio = 0; iteracio < iteracions; ++iteracio) {
            base.progres++;
            DisponibilitatHoraria disponibilitatHorariaActual;
            int horesActual = 0;
            int horesQuadrades = 0;
            try {
                disponibilitatHorariaActual = (DisponibilitatHoraria) disponibilitat.clone();
            
                //Generem horari per a la combinació actual d'items
                for (int idItem = items.size(); --idItem >= 0;) {
                    //Base.dbg("BEFORE addReserva CALL FOR item ".concat(String.valueOf(idItem)));
                    int hores = disponibilitatHorariaActual.addReserva(items.get(idItem).hores, items.get(idItem).codi);
                    //Base.dbgAUX("AFTER addReserva CALL FOR item ".concat(String.valueOf(idItem)).concat(", hores: ").concat(String.valueOf(hores)));
                    if (hores == 0) ++horesQuadrades;
                    horesActual += hores;
                }
                //Base.dbg("END addReserva FOR ALL ITEMS");
                int ponderacioActual = disponibilitatHorariaActual.avalua();
                //base.dbg("TEST PONDERACIO");
                if (Regles.PRIORITZAR_QUADRAR_HORES.get()) {
                    if (horesQuadrades > millorHoresQuadrades) {
                        //base.dbgAUX("\tMILLOR HORES QUADRADES, ponderacio actual = ".concat(String.valueOf(ponderacioActual)));
                        millorDisponibilitat = (DisponibilitatHoraria) disponibilitatHorariaActual.clone();
                        ponderacio = ponderacioActual;
                        millorHoresQuadrades = horesQuadrades;
                    } else if (horesQuadrades == millorHoresQuadrades) {
                        if (ponderacioActual > ponderacio) {
                            //base.dbgAUX("\tMILLOR HORES QUADRADES AMB PONDERACIO ACTUAL > PONDERACIO: actual = ".concat(String.valueOf(ponderacioActual)));
                            millorDisponibilitat = (DisponibilitatHoraria) disponibilitatHorariaActual.clone();
                            ponderacio = ponderacioActual;
                            millorHoresQuadrades = horesQuadrades;
                        }
                    }
                } else {
                    if (ponderacioActual > ponderacio) {
                        //base.dbgAUX("\tPONDERACIO ACTUAL > PONDERACIO: actual = ".concat(String.valueOf(ponderacioActual)));
                        millorDisponibilitat = (DisponibilitatHoraria) disponibilitatHorariaActual.clone();
                        ponderacio = ponderacioActual;

                    }
                }
                disponibilitatHorariaActual = null;
                //base.dbgAUX("\thoresActual = ".concat(String.valueOf(horesActual)));
                //base.dbgAUX("\thoresQuadrades = ".concat(String.valueOf(horesQuadrades)));
                //Reordenem el vector d'items
                Collections.shuffle(items);
            } catch (CloneNotSupportedException ex) {
                Logger.getLogger(Generador.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        //finalitzat = true;
    }
    
    public DisponibilitatHoraria getHorari() {
        return millorDisponibilitat;
    }

    @Override
    protected String doInBackground() throws Exception {
        //long inici_ms = new Date().getTime();
        if (utilitzarThreads) {
            inicia();
        } else {
            iniciaSenseThreads();
        }
        //System.out.println("GENERATOR TIME: ".concat(String.valueOf((new Date().getTime()) - inici_ms)));
        //System.out.println("die!");
        //assignatures.get(900).getNom();
        finalitzat = true;
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
    
    private class GeneradorThread implements Runnable {
        private int idThread;
        
        public void setThreadID(int id) {
            idThread = id;
        }
        
        public GeneradorThread(int id) {
            super();
            setThreadID(id);
        }
        
        @Override
        public void run() {
            //Reordenem el vector d'items
            Vector<Item> items = itemsGlobal.get(idThread);
            Collections.shuffle(items);
            DisponibilitatHoraria disponibilitatHorariaActual;
            int horesActual = 0;
            int horesQuadrades = 0;
            try {
                disponibilitatHorariaActual = (DisponibilitatHoraria) disponibilitat.clone();
            
                //Generem horari per a la combinació actual d'items
                for (int idItem = items.size(); --idItem >= 0;) {
                    //Base.dbg("BEFORE addReserva CALL FOR item ".concat(String.valueOf(idItem)));
                    int hores = disponibilitatHorariaActual.addReserva(items.get(idItem).hores, items.get(idItem).codi);
                    //Base.dbgAUX("AFTER addReserva CALL FOR item ".concat(String.valueOf(idItem)).concat(", hores: ").concat(String.valueOf(hores)));
                    if (hores == 0) ++horesQuadrades;
                    horesActual += hores;
                }
                //Base.dbg("END addReserva FOR ALL ITEMS");
                int ponderacioActual = disponibilitatHorariaActual.avalua();
                //base.dbg("TEST PONDERACIO");
                synchronized (this) {   //Només un thread a la vegada pot comparar-se amb els altres
                    if (Regles.PRIORITZAR_QUADRAR_HORES.get()) {
                        if (horesQuadrades > millorHoresQuadrades) {
                            //base.dbgAUX("\tMILLOR HORES QUADRADES, ponderacio actual = ".concat(String.valueOf(ponderacioActual)));
                            millorDisponibilitat = (DisponibilitatHoraria) disponibilitatHorariaActual.clone();
                            ponderacio = ponderacioActual;
                            millorHoresQuadrades = horesQuadrades;
                        } else if (horesQuadrades == millorHoresQuadrades) {
                            if (ponderacioActual > ponderacio) {
                                //base.dbgAUX("\tMILLOR HORES QUADRADES AMB PONDERACIO ACTUAL > PONDERACIO: actual = ".concat(String.valueOf(ponderacioActual)));
                                millorDisponibilitat = (DisponibilitatHoraria) disponibilitatHorariaActual.clone();
                                ponderacio = ponderacioActual;
                                millorHoresQuadrades = horesQuadrades;
                            }
                        }
                    } else {
                        if (ponderacioActual > ponderacio) {
                            //base.dbgAUX("\tPONDERACIO ACTUAL > PONDERACIO: actual = ".concat(String.valueOf(ponderacioActual)));
                            millorDisponibilitat = (DisponibilitatHoraria) disponibilitatHorariaActual.clone();
                            ponderacio = ponderacioActual;

                        }
                    }
                }
                disponibilitatHorariaActual = null;
                //base.dbgAUX("\thoresActual = ".concat(String.valueOf(horesActual)));
                //base.dbgAUX("\thoresQuadrades = ".concat(String.valueOf(horesQuadrades)));
                
            } catch (CloneNotSupportedException ex) {
                Logger.getLogger(Generador.class.getName()).log(Level.SEVERE, null, ex);
            }
            //System.out.println("thread finalitzat!");
            //this.parent.notify();
        }
        
    }
    
}
