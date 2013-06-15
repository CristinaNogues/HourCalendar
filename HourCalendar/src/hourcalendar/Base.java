/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hourcalendar;

import java.awt.BorderLayout;
import java.awt.Container;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 *
 * @author asus
 */
public class Base {
    public Vector<Assignatura> assignatures;
    private Vector<Grau> graus;
    public Vector<TipusHoresPractica> tipusHoresPractiques;
    public Vector<TipusMateria> tipusMateries;
    private List<Date> diesDocencia;
    private Vector setmanaOrdreQ1;
    private Vector setmanaOrdreQ2;
    public Vector<DisponibilitatHoraria> disponibilitatsHoraries;
    public int progres;
    public String nomProgres = "Inicialitzant...";
    public Generador generador;
    public String informe = "";
    
    public Base() {
        loadDiesDocencia();
        setmanaOrdreQ1 = new Vector<Integer>();
        setmanaOrdreQ2 = new Vector<Integer>();
        ModsCalendari.load();
        assignatures = new Vector<Assignatura>();
        graus = new Vector<Grau>();
        loadState();
        //updateDisponibilitatHoraria(1, 2012);
        //addGrau("Informàtica", "01");
        //addGrau("Enginyeria Mecànica", "02");
        //addGrau("Disseny Industrial", "03");
        addAssignatura(getGrau(0), "PROJECTE DE PROGRAMACIÓ", "PROP", 340380, new TipusMateria(1, "ABC"),
                25, 4, 30, 30, new TipusHoresPractica(5, "Laboratori Informàtica"), 2, "I");
        addAssignatura(getGrau(0), "XARXES DE COMPUTADORS", "XACO", 340356, new TipusMateria(1, "ABC"),
                25, 4, 42, 18, new TipusHoresPractica(5, "Laboratori Informàtica"), 3, "I");
        addAssignatura(getGrau(0), "ESTRUCTURA DE LA INFORMACIÓ", "ESIN", 300000, new TipusMateria(1, "ABC"),
                25, 2, 30, 30, new TipusHoresPractica(5, "Laboratori Informàtica"), 8, "I");
        addAssignatura(getGrau(0), "XARXES MULTIMÈDIA", "XAMU", 370251, new TipusMateria(1, "ABC"),
                25, 2, 42, 18, new TipusHoresPractica(5, "Laboratori Informàtica"), 3, "I");
        addAssignatura(getGrau(0), "EMPRESA", "EMPR", 670200, new TipusMateria(1, "ABC"),
                25, 2, 60, 0, new TipusHoresPractica(5, "Laboratori Informàtica"), 0, "I");
        addAssignatura(getGrau(1), "SOSTENIBILITAT", "SOST", 370001, new TipusMateria(1, "ABC"),
                25, 2, 42, 18, new TipusHoresPractica(5, "Laboratori Informàtica"), 3, "N");
        addAssignatura(getGrau(1), "PROJECTE DE TECNOLOGIES DE LA INFORMACIÓ", "PTIN", 555555, new TipusMateria(1, "ABC"),
                25, 2, 42, 18, new TipusHoresPractica(5, "Laboratori Informàtica"), 3, "N");
        addAssignatura(getGrau(2), "GESTIÓ DE PROJECTES", "GEPR", 340037, new TipusMateria(1, "ABC"),
                25, 4, 30, 30, new TipusHoresPractica(5, "Laboratori Informàtica"), 3, "N");
        addAssignatura(getGrau(2), "DISSENY I REPRESENTACIÓ TÈCNICA", "DIRT", 340075, new TipusMateria(1, "ABC"),
                25, 4, 42, 18, new TipusHoresPractica(5, "Laboratori Informàtica"), 3, "N");
        
    }
    
    public int getNumAssignatures() {
        return assignatures.size();
    }
    
    /** Retorna una assignatura donada la seva posició dins el vector. **/
    public Assignatura getAssignaturaAt(int idAssignatura) {
        return assignatures.get(idAssignatura);
    }
    
    /** Retorna una assignatura donat el seu codi, null si no existeix. */
    public Assignatura getAssignatura(int codi) {
        
        int numAssignatures = getNumAssignatures();
        for (int i = 0; i < numAssignatures; ++i) {
            Assignatura assignatura = assignatures.get(i);
            if (assignatura.getCodi() == codi) {
                return assignatura;
            }
        }
        return null;
    }
    
    public void removeAssignatura(Assignatura assignatura) {
        assignatures.removeElement(assignatura);
    }
    
    /** Afegeix o modifica una nova assignatura segons el codi d'assignatura.
     * @return boolean indica si s'ha afegit l'assignatura correctament, no s'afegirà si algun dels paràmetres no és l'esperat
     **/
    public boolean addAssignatura(Grau _grau, String _nom, String _sigles, int _codi, TipusMateria _tipus, int _alumnes, int _quadrimestre, int _horesTeoria, int _horesPractica, TipusHoresPractica _tipusHoresPractica, int _grups, String _inicial) {
        
        Assignatura assignatura = new Assignatura(this, _grau, _nom, _sigles, _codi, _tipus, _alumnes, _quadrimestre, _horesTeoria, _horesPractica, _tipusHoresPractica, _grups, _inicial);
        
        if (assignatura.esValida()) {
            Assignatura aux = getAssignatura(_codi);
            if (aux != null) {
                //L'assignatura ja existia, la modifiquem
                aux = assignatura;
            } else {
                //Afegint una nova assignatura
                assignatures.add(assignatura);
            }
            saveState();
            return true;
        } else {
            return false;
        }
    }
    
    
    public int getNumGraus() {
        return graus.size();
    }
    
    public Grau getGrau(int idGrau) {
        return graus.get(idGrau);
    }
    
    public void addGrau(String nom, String codi) {
        Grau grau = new Grau(nom, codi);
        graus.add(grau);
        saveState();
    }
    
    /** Retorna cert si existeix l'objecte grau al vector graus. **/
    public boolean hasGrau(Grau grau) {
        return graus.contains(grau);
    }
    
    public int getIndexOfGrau(Grau grau) {
        return graus.indexOf(grau);
    }
    
    /** Retorna cert si existeix l'objecte TipusHoresPractica al vector tipusHoresPractiques. **/
    public boolean hasTipusHoresPractica(TipusHoresPractica tipus) {
        return tipusHoresPractiques.contains(tipus);
    }
    
    /** Retorna cert si existeix l'objecte TipusMateria al vector tipusMateries. **/
    public boolean hasTipusMateria(TipusMateria tipus) {
        return tipusMateries.contains(tipus);
    }
    
    public void loadDiesDocencia() {
        FileInputStream fin;
        ObjectInputStream ois;
        List<Date> selectedDates;
        //diesDocencia = new List<Date>();
        try {
            fin = new FileInputStream("selectedDates.ser");
            try {
                ois = new ObjectInputStream(fin);
                try {
                    diesDocencia = (List<Date>) ois.readObject();
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(SelectorDiesDocencia.class.getName()).log(Level.SEVERE, null, ex);
                }
            } catch (IOException ex) {
                Logger.getLogger(SelectorDiesDocencia.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(SelectorDiesDocencia.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /** Retorna una List<Date> que conté tots els dies marcats com a docents donat un quadrimestre (1 o 2) i l'any. **/
    public List<Date> getDiesDocencia(int quadri, int any) {
        Date q1_inici = new GregorianCalendar(any, 7, 1).getTime();
        Date q1_final = new GregorianCalendar(any + 1, 1, 1).getTime();
        Date q2_inici = new GregorianCalendar(any, 1, 2).getTime();
        Date q2_final = new GregorianCalendar(any, 6, 25).getTime();
        
        Calendar temp = new GregorianCalendar();
        if (quadri == 1) setmanaOrdreQ1.clear();
        if (quadri == 2) setmanaOrdreQ2.clear();
        
        List<Date> dies = new ArrayList<Date>();
        Iterator<Date> it = diesDocencia.listIterator();
        while (it.hasNext()) {
            //System.out.println(iterator.next());
            Date d = it.next();
            temp.setTime(d);
            int weekOfYear = temp.get(Calendar.WEEK_OF_YEAR);
            if (weekOfYear <= 5) weekOfYear +=53;
            if (quadri == 1) {
                if (d.after(q1_inici) && d.before(q1_final)) {
                    dies.add(d);
                    if (setmanaOrdreQ1.indexOf(weekOfYear) == -1)
                        setmanaOrdreQ1.addElement(new Integer(weekOfYear));
                }
            } else if (quadri == 2) {
                if (d.after(q2_inici) && d.before(q2_final)) {
                    dies.add(d);
                    if (setmanaOrdreQ2.indexOf(weekOfYear) == -1)
                        setmanaOrdreQ2.addElement(new Integer(weekOfYear));
                }
            }
        }
        if (quadri == 1) Collections.sort(setmanaOrdreQ1);
        if (quadri == 2) {
            Collections.sort(setmanaOrdreQ2);
            for (int i = 0; i < setmanaOrdreQ2.size(); ++i) {
                System.out.println(Integer.toString(i).concat(" = ").concat(Integer.toString((Integer)setmanaOrdreQ2.get(i))));
            }
        }
        //Collections.copy(dies, diesDocencia);
        
        return dies;
    }
    
    /** retorna un valor del 0 al 3, corresponents a setmana 11, 21, 12 i 22 donada una data i un quadrimestre (1 o 2). Retorna -1 si quadrimestre != 1 o 2. **/
    public int getSetmanaOrdre(Date date, int quadri) {
        if (quadri == 1 && setmanaOrdreQ1.isEmpty()) getDiesDocencia(quadri, 2012);
        if (quadri == 2 && setmanaOrdreQ2.isEmpty()) getDiesDocencia(quadri, 2013);
        Calendar dia = new GregorianCalendar();
        dia.setTime(date);
        int weekOfYear = dia.get(Calendar.WEEK_OF_YEAR);
        if (weekOfYear <= 5) weekOfYear +=53;
        if (quadri == 1) {
            return setmanaOrdreQ1.indexOf(weekOfYear) % 4;
        } else if (quadri == 2) {
            return setmanaOrdreQ2.indexOf(weekOfYear) % 4;
        }
        
        return -1;
    }
    
    public void updateDisponibilitatHoraria() {
        int any = Regles.CONVOCATORIA.getInt() + 2010 + Regles.QUADRIMESTRE.getInt();
        updateDisponibilitatHoraria(Regles.QUADRIMESTRE.getInt(), any);
        
    }
    
    public void updateDisponibilitatHoraria(int quadri, int any) {
        List<Date> dies = getDiesDocencia(quadri, any);
        disponibilitatsHoraries = new Vector<DisponibilitatHoraria>();
        DisponibilitatHoraria disponibilitat = new DisponibilitatHoraria();
        Calendar dia = new GregorianCalendar();
        int ordre = 0, mod;
        int diaDeLaSetmana = 0;

        Iterator<Date> it = dies.listIterator();
        while (it.hasNext()) {
            //System.out.println(iterator.next());
            Date d = it.next();
            dia.setTime(d);
            //if ((mod = ModsCalendari.getModOrdre(dia)) != -1)
            if (Regles.APLICAR_MODS_CALENDARI.get()) {
                ordre = ((mod = ModsCalendari.getModOrdre(dia)) != -1) ? mod : getSetmanaOrdre(d, quadri);
                diaDeLaSetmana = ((mod = ModsCalendari.getModDia(dia)) != -1) ? mod : dia.get(Calendar.DAY_OF_WEEK);
            } else {
                //No apliquem classe ModsCalendari
                ordre = getSetmanaOrdre(d, quadri);
                diaDeLaSetmana = dia.get(Calendar.DAY_OF_WEEK);
            }
            disponibilitat.addDia(diaDeLaSetmana, ordre);
            
        }
        disponibilitatsHoraries.add(disponibilitat);
        //int horesDesquadrades = 0;
        //try {
            
            //addAssignatura(new Grau("Informatica", "01"), "FISI", 205555, new TipusMateria(1, "ABC"),
            //        25, 2, 20, 40, new TipusHoresPractica(5, "Laboratori Informàtica"), 1);
            /*//PROP
            horesDesquadrades = disponibilitat.addReserva(30, "340380#0#0");    //TEORIA CONJUNTA
            System.out.println("RESERVA 30h [340380#0#0]: ".concat(String.valueOf(horesDesquadrades)));
            disponibilitat.imprimeixPonderacio();
            horesDesquadrades = disponibilitat.addReserva(30, "340380#5#1");    //LAB GRUP 1
            System.out.println("RESERVA 30h [340380#5#1]: ".concat(String.valueOf(horesDesquadrades)));
            disponibilitat.imprimeixPonderacio();
            horesDesquadrades = disponibilitat.addReserva(30, "340380#5#2");    //LAB GRUP 2
            System.out.println("RESERVA 30h [340380#5#2]: ".concat(String.valueOf(horesDesquadrades)));
            disponibilitat.imprimeixPonderacio();
            //XACO
            horesDesquadrades = disponibilitat.addReserva(42, "340356#0#0");    //TEORIA CONJUNTA
            System.out.println("RESERVA 42h [340356#0#0]: ".concat(String.valueOf(horesDesquadrades)));
            disponibilitat.imprimeixPonderacio();
            horesDesquadrades = disponibilitat.addReserva(18, "340356#5#1");    //LAB GRUP 1
            System.out.println("RESERVA 18h [340356#5#1]: ".concat(String.valueOf(horesDesquadrades)));
            disponibilitat.imprimeixPonderacio();
            horesDesquadrades = disponibilitat.addReserva(18, "340356#5#2");    //LAB GRUP 2
            System.out.println("RESERVA 18h [340356#5#2]: ".concat(String.valueOf(horesDesquadrades)));
            disponibilitat.imprimeixPonderacio();
            horesDesquadrades = disponibilitat.addReserva(18, "340356#5#3");    //LAB GRUP 3
            System.out.println("RESERVA 18h [340356#5#3]: ".concat(String.valueOf(horesDesquadrades)));
            disponibilitat.imprimeixPonderacio();
            //CLON DE XACO
            horesDesquadrades = disponibilitat.addReserva(42, "370251#0#0");    //TEORIA CONJUNTA
            System.out.println("RESERVA 42h [370251#0#0]: ".concat(String.valueOf(horesDesquadrades)));
            disponibilitat.imprimeixPonderacio();
            horesDesquadrades = disponibilitat.addReserva(18, "370251#5#1");    //LAB GRUP 1
            System.out.println("RESERVA 18h [370251#5#1]: ".concat(String.valueOf(horesDesquadrades)));
            disponibilitat.imprimeixPonderacio();
            horesDesquadrades = disponibilitat.addReserva(18, "370251#5#2");    //LAB GRUP 2
            System.out.println("RESERVA 18h [370251#5#2]: ".concat(String.valueOf(horesDesquadrades)));
            disponibilitat.imprimeixPonderacio();
            //CLON DE PROP 1
            horesDesquadrades = disponibilitat.addReserva(30, "390123#0#0");    //TEORIA CONJUNTA
            System.out.println("RESERVA 30h [390123#0#0]: ".concat(String.valueOf(horesDesquadrades)));
            disponibilitat.imprimeixPonderacio();
            horesDesquadrades = disponibilitat.addReserva(30, "390123#5#1");    //LAB GRUP 1
            System.out.println("RESERVA 30h [390123#5#1]: ".concat(String.valueOf(horesDesquadrades)));
            disponibilitat.imprimeixPonderacio();
            Regles.DEBUG2_ENABLED.set(true);
            horesDesquadrades = disponibilitat.addReserva(30, "390123#5#2");    //LAB GRUP 2
            System.out.println("RESERVA 30h [390123#5#2]: ".concat(String.valueOf(horesDesquadrades)));
            disponibilitat.imprimeixPonderacio();
            Regles.DEBUG2_ENABLED.set(false);
            //CLON DE PROP 2
            horesDesquadrades = disponibilitat.addReserva(30, "300000#0#0");    //TEORIA CONJUNTA
            System.out.println("RESERVA 30h [300000#0#0]: ".concat(String.valueOf(horesDesquadrades)));
            disponibilitat.imprimeixPonderacio();
            horesDesquadrades = disponibilitat.addReserva(30, "300000#5#1");    //LAB GRUP 1
            System.out.println("RESERVA 30h [300000#5#1]: ".concat(String.valueOf(horesDesquadrades)));
            disponibilitat.imprimeixPonderacio();
            horesDesquadrades = disponibilitat.addReserva(30, "300000#5#2");    //LAB GRUP 2
            System.out.println("RESERVA 30h [300000#5#2]: ".concat(String.valueOf(horesDesquadrades)));
            disponibilitat.imprimeixPonderacio();
            
            

        } catch (CloneNotSupportedException ex) {
            Logger.getLogger(Base.class.getName()).log(Level.SEVERE, null, ex);
        }
        * */
        
        progres = 0;
        /*WaitDialog dialog = new WaitDialog(new javax.swing.JFrame(), true);
        dialog.addWindowListener(new java.awt.event.WindowAdapter() {

        });
        SwingWorker worker = new SwingWorker() {

            @Override
            protected Object doInBackground() throws Exception {
                register();
                return 0;
            }

            @Override
            protected void done() {
                dialog.dispose();
            }
        };
        worker.execute();
        dialog.setVisible(true);^*/
        //SwingUtilities.invokeAndWait(null);

        
        
    }
    
    public Vector<DisponibilitatHoraria> getDisponibilitatsHoraries() {
        return disponibilitatsHoraries;
    }
    
    /** Afegeix el missatge com a una nova línea de l'informe de resultats. **/
    public void informar(String msg) {
        String newLine = "<br>";
        informe = informe.concat(msg).concat(newLine);
    }
    
    public void saveState() {
        FileOutputStream fout;
        //FileOutputStream fout2;
        ObjectOutputStream oos;
        try {
            fout = new FileOutputStream("graus.ser");
            //fout2 = new FileOutputStream("assignatures.ser");
            try {
                oos = new ObjectOutputStream(fout);
                oos.writeObject(graus);
                //oos = new ObjectOutputStream(fout2);
                //oos.writeObject(assignatures);
            } catch (IOException ex) {
                Logger.getLogger(SelectorDiesDocencia.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(SelectorDiesDocencia.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void loadState() {
        
        FileInputStream fin;
        ObjectInputStream ois;
        try {
            fin = new FileInputStream("graus.ser");
            try {
                ois = new ObjectInputStream(fin);
                try {
                    graus = (Vector<Grau>) ois.readObject();
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(SelectorDiesDocencia.class.getName()).log(Level.SEVERE, null, ex);
                }
            } catch (IOException ex) {
                Logger.getLogger(SelectorDiesDocencia.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(SelectorDiesDocencia.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static class ModsCalendari {
        public static Vector< Vector<Calendar> > datesAmbDiaModificat;
        public static Vector< Vector<Calendar> > datesAmbOrdreModificat;

        public static void load() {
            datesAmbDiaModificat = new Vector< Vector<Calendar> >();
            for (int i = 0; i <= 7; ++i)
                datesAmbDiaModificat.add(new Vector<Calendar>());
            //MODS Q2 2012-2013
            //Marquem el dijous 25 d'abril de 2013 com a dimarts
            datesAmbDiaModificat.get(3).add(new GregorianCalendar(2013, 3, 25));
            datesAmbDiaModificat.get(4).add(new GregorianCalendar(2013, 4, 3)); //3 de maig com dimecres

            datesAmbOrdreModificat = new Vector< Vector<Calendar> >();
            for (int i = 0; i <= 3; ++i)
                datesAmbOrdreModificat.add(new Vector<Calendar>());

            datesAmbOrdreModificat.get(3).add(new GregorianCalendar(2013, 5, 7));
            datesAmbOrdreModificat.get(0).add(new GregorianCalendar(2013, 5, 10));
            //MODS Q1 2012-2013
            datesAmbOrdreModificat.get(3).add(new GregorianCalendar(2012, 10, 29)); //29 nov 2012 com setmana 2 2
            datesAmbDiaModificat.get(5).add(new GregorianCalendar(2013, 0, 14));    //14 gener 2013 com dijous
            datesAmbDiaModificat.get(6).add(new GregorianCalendar(2013, 0, 15));    //15 gener 2013 com divendres
            
        }
        
        /** Retorna el dia de la setmana (1 = diumenge) d'un dia específic, -1 si no és un dia modificat. **/
        public static int getModDia(Calendar dia) {
            for (int i = 0; i <= 7; ++i) {
                if (datesAmbDiaModificat.get(i).indexOf(dia) != -1) {
                    System.out.println("MOD DIA!!!");
                    return i;
                }
            }
            return -1;
        }
        
        /** Retorna l'ordre d'un dia específic, -1 si no és un dia amb ordre modificat. 
         *
         * @return Integer entre 0 i 3 corresponents a setmana 11, 21, 12 i 22 respectivament.
         */
        public static int getModOrdre(Calendar dia) {
            for (int i = 0; i <= 3; ++i) {
                if (datesAmbOrdreModificat.get(i).indexOf(dia) != -1) {
                    System.out.println("MOD ORDRE!!!");
                    return i;
                }
            }
            return -1;
        }
    }
    
    /*public static class Regles {
        public static boolean solaparHoresPractica = true;
        public static String solaparHoresPracticaMissatge = "Solapar les hores de pràctica d'assignatures i grups diferents.";
        
    }*/
    
    public enum Regles {
        SOLAPAR_HORES_PRACTICA (1, "<html>Solapar les hores de pràctica d'assignatures i grups diferents.</html>"),
        ASSIGNAR_HORES_RESTANTS (1, "<html>Sobrepassar les hores assignades a les assignatures si no <br>existeix combinació possible que les quadri al calendari.</html>"),
        PRIORITZAR_QUADRAR_HORES (0, "<html>Donar prioritat a quadrar les hores de les assignatures enlloc <br>d'obtenir un calendari més ben repartit.</html>"),
        ITERACIONS_GENERADOR (1, "<html>Número d'iteracions que realitza el generador <br>d'horaris per a trobar la millor combinació.</html>"),
        QUADRIMESTRE (1, "<html>Quadrimestre per al que generar els horaris.</html>"),
        CONVOCATORIA (1, "<html>Any de convocatòria.</html>"),
        APLICAR_MODS_CALENDARI (1, "<html>Utilitzar modificacions del calendari. Exemple: Dilluns 14 de gener passa a ser dijous.</html>"),
        //REGLES INTERNES (NO MODIFICABLES A TRAVÉS DEL FORMULARI D'OPCIONS
        DEBUG_ENABLED (0, "Mostrar informació per consola de les operacions que es van realitzant (DEBUG MODE)."),
        DEBUG2_ENABLED (0, "Mostrar informació per consola d'altres operacions que es van realitzant (DEBUG2 MODE)."),
        DEBUG_UI_ENABLED (1, "Mostrar informació per consola d'operacions gràfiques que es van realitzant (DEBUG UI MODE)."),
        UTILITZA_ANTIC_ALGORISME (0, "Utilitzar l'antic algorisme per a generar els horaris. No soporta solapament d'hores de pràctica.");

        private int valor;
        private String missatge;
        
        Regles(int valor, String missatge) {
            this.valor = valor;
            this.missatge = missatge;
        }
        public boolean get() { return (valor == 0) ? false : true; }
        public int getInt() { return valor; }
        public void set(int valor) { this.valor = valor; }
        public void set(boolean valor) { this.valor = (valor) ? 1 : 0; }
        public String getMissatge() { return missatge; }
        
    }
    
    public static void dbg(String msg) {
        if (Regles.DEBUG_ENABLED.get()) {
            System.out.println(msg);
        }
    }
    
    public static void dbg2(String msg) {
        if (Regles.DEBUG2_ENABLED.get()) {
            System.out.println(msg);
        }
    }
    
    public static void dbgUI(String msg) {
        if (Regles.DEBUG_UI_ENABLED.get()) {
            System.out.println("[dbgUI] ".concat(msg));
        }
    }
}
