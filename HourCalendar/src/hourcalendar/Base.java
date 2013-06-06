/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hourcalendar;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
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

/**
 *
 * @author asus
 */
public class Base {
    private Vector<Assignatura> assignatures;
    private Vector<Grau> graus;
    public Vector<TipusHoresPractica> tipusHoresPractiques;
    public Vector<TipusMateria> tipusMateries;
    private List<Date> diesDocencia;
    private Vector setmanaOrdreQ1;
    private Vector setmanaOrdreQ2;
    private Vector<DisponibilitatHoraria> disponibilitatsHoraries;
    
    public Base() {
        loadDiesDocencia();
        setmanaOrdreQ1 = new Vector<Integer>();
        setmanaOrdreQ2 = new Vector<Integer>();
        ModsCalendari.load();
        //updateDisponibilitatHoraria(1, 2012);
        
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
    
    /** Afegeix o modifica una nova assignatura segons el codi d'assignatura.
     * @return boolean indica si s'ha afegit l'assignatura correctament, no s'afegirà si algun dels paràmetres no és l'esperat
     **/
    public boolean addAssignatura(Grau _grau, String _nom, int _codi, TipusMateria _tipus, int _alumnes, int _quadrimestre, int _horesTeoria, int _horesPractica, TipusHoresPractica _tipusHoresPractica) {
        
        Assignatura assignatura = new Assignatura(this, _grau, _nom, _codi, _tipus, _alumnes, _quadrimestre, _horesTeoria, _horesPractica, _tipusHoresPractica);
        
        if (assignatura.esValida()) {
            Assignatura aux = getAssignatura(_codi);
            if (aux != null) {
                //L'assignatura ja existia, la modifiquem
                aux = assignatura;
            } else {
                //Afegint una nova assignatura
                assignatures.add(assignatura);
            }
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
    }
    
    /** Retorna cert si existeix l'objecte grau al vector graus. **/
    public boolean hasGrau(Grau grau) {
        return graus.contains(grau);
    }
    
    /** Retorna cert si existeix l'objecte TipusHoresPractica al vector tipusHoresPractiques. **/
    public boolean hasTipusHoresPractica(TipusHoresPractica tipus) {
        return tipusHoresPractiques.contains(tipus);
    }
    
    /** Retorna cert si existeix l'objecte TipusMateria al vector tipusMateries. **/
    public boolean hasTipusMateria(TipusMateria tipus) {
        return tipusMateries.contains(tipus);
    }
    
    private void loadDiesDocencia() {
        FileInputStream fin;
        ObjectInputStream ois;
        List<Date> selectedDates;
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
            ordre = ((mod = ModsCalendari.getModOrdre(dia)) != -1) ? mod : getSetmanaOrdre(d, quadri);
            diaDeLaSetmana = ((mod = ModsCalendari.getModDia(dia)) != -1) ? mod : dia.get(Calendar.DAY_OF_WEEK);
            disponibilitat.addDia(diaDeLaSetmana, ordre);
        }
        int horesDesquadrades = 0;
        try {
            //PROP
            horesDesquadrades = disponibilitat.addReserva(30, "340380#0#0");    //TEORIA CONJUNTA
            System.out.println("RESERVA 30h [340380#0#0]: ".concat(String.valueOf(horesDesquadrades)));
            horesDesquadrades = disponibilitat.addReserva(30, "340380#5#1");    //LAB GRUP 1
            System.out.println("RESERVA 30h [340380#5#1]: ".concat(String.valueOf(horesDesquadrades)));
            horesDesquadrades = disponibilitat.addReserva(30, "340380#5#2");    //LAB GRUP 2
            System.out.println("RESERVA 30h [340380#5#2]: ".concat(String.valueOf(horesDesquadrades)));
            //XACO
            horesDesquadrades = disponibilitat.addReserva(42, "340356#0#0");    //TEORIA CONJUNTA
            System.out.println("RESERVA 42h [340356#0#0]: ".concat(String.valueOf(horesDesquadrades)));
            horesDesquadrades = disponibilitat.addReserva(18, "340356#5#1");    //LAB GRUP 1
            System.out.println("RESERVA 18h [340356#5#1]: ".concat(String.valueOf(horesDesquadrades)));
            horesDesquadrades = disponibilitat.addReserva(18, "340356#5#2");    //LAB GRUP 2
            System.out.println("RESERVA 18h [340356#5#2]: ".concat(String.valueOf(horesDesquadrades)));
            horesDesquadrades = disponibilitat.addReserva(18, "340356#5#3");    //LAB GRUP 3
            System.out.println("RESERVA 18h [340356#5#3]: ".concat(String.valueOf(horesDesquadrades)));
            //CLON DE XACO
            horesDesquadrades = disponibilitat.addReserva(42, "370251#0#0");    //TEORIA CONJUNTA
            System.out.println("RESERVA 42h [370251#0#0]: ".concat(String.valueOf(horesDesquadrades)));
            horesDesquadrades = disponibilitat.addReserva(18, "370251#5#1");    //LAB GRUP 1
            System.out.println("RESERVA 18h [370251#5#1]: ".concat(String.valueOf(horesDesquadrades)));
            horesDesquadrades = disponibilitat.addReserva(18, "370251#5#2");    //LAB GRUP 2
            System.out.println("RESERVA 18h [370251#5#2]: ".concat(String.valueOf(horesDesquadrades)));
            //CLON DE PROP 1
            horesDesquadrades = disponibilitat.addReserva(30, "390123#0#0");    //TEORIA CONJUNTA
            System.out.println("RESERVA 30h [390123#0#0]: ".concat(String.valueOf(horesDesquadrades)));
            horesDesquadrades = disponibilitat.addReserva(30, "390123#5#1");    //LAB GRUP 1
            System.out.println("RESERVA 30h [390123#5#1]: ".concat(String.valueOf(horesDesquadrades)));
            horesDesquadrades = disponibilitat.addReserva(30, "390123#5#2");    //LAB GRUP 2
            System.out.println("RESERVA 30h [390123#5#2]: ".concat(String.valueOf(horesDesquadrades)));
            //CLON DE PROP 2
            /*horesDesquadrades = disponibilitat.addReserva(30, "300000#0#0");    //TEORIA CONJUNTA
            System.out.println("RESERVA 30h [300000#0#0]: ".concat(String.valueOf(horesDesquadrades)));
            horesDesquadrades = disponibilitat.addReserva(30, "300000#5#1");    //LAB GRUP 1
            System.out.println("RESERVA 30h [300000#5#1]: ".concat(String.valueOf(horesDesquadrades)));
            horesDesquadrades = disponibilitat.addReserva(30, "300000#5#2");    //LAB GRUP 2
            System.out.println("RESERVA 30h [300000#5#2]: ".concat(String.valueOf(horesDesquadrades)));*/

        } catch (CloneNotSupportedException ex) {
            Logger.getLogger(Base.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println(disponibilitat.toString());
        disponibilitatsHoraries.add(disponibilitat);
    }
    
    public Vector<DisponibilitatHoraria> getDisponibilitatsHoraries() {
        return disponibilitatsHoraries;
    }
    
    public static class ModsCalendari {
        public static Vector< Vector<Calendar> > datesAmbDiaModificat;
        public static Vector< Vector<Calendar> > datesAmbOrdreModificat;

        public static void load() {
            datesAmbDiaModificat = new Vector< Vector<Calendar> >();
            for (int i = 0; i <= 7; ++i)
                datesAmbDiaModificat.add(new Vector<Calendar>());
            //Marquem el dijous 25 d'abril de 2013 com a dimarts
            datesAmbDiaModificat.get(3).add(new GregorianCalendar(2013, 3, 25));
            datesAmbDiaModificat.get(4).add(new GregorianCalendar(2013, 3, 3));

            datesAmbOrdreModificat = new Vector< Vector<Calendar> >();
            for (int i = 0; i <= 3; ++i)
                datesAmbOrdreModificat.add(new Vector<Calendar>());

            datesAmbOrdreModificat.get(3).add(new GregorianCalendar(2013, 5, 7));
            datesAmbOrdreModificat.get(0).add(new GregorianCalendar(2013, 5, 10));
        }
        
        /** Retorna el dia de la setmana (1 = diumenge) d'un dia específic, -1 si no és un dia modificat. **/
        public static int getModDia(Calendar dia) {
            for (int i = 0; i <= 7; ++i) {
                /*Vector<Calendar> dates = datesAmbDiaModificat.get(i);
                for (int e = 0; e < dates.size(); ++e) {
                    if (dia.compareTo(dates.get(e)) == 0) {
                        System.out.println("MOD DIA!!!".concat(dates.get(e).getTime().toLocaleString()));
                        return i;
                    }
                }*/
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
}
