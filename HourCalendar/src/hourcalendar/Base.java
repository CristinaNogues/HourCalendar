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
    
    public Base() {
        loadDiesDocencia();
        setmanaOrdreQ1 = new Vector<Integer>();
        setmanaOrdreQ2 = new Vector<Integer>();
        updateDisponibilitatHoraria(1, 2012);
        updateDisponibilitatHoraria(2, 2013);
    }
    
    public int getNumAssignatures() {
        return assignatures.size();
    }
    
    /** Retorna una assignatura donada la seva posició dins el vector. **/
    public Assignatura getAssignatura(int idAssignatura) {
        return assignatures.get(idAssignatura);
    }
    
    /** Retorna una assignatura donat el seu codi, null si no existeix. */
    public Assignatura getAssignatura(String codi) {
        
        int numAssignatures = getNumAssignatures();
        for (int i = 0; i < numAssignatures; ++i) {
            Assignatura assignatura = assignatures.get(i);
            if (assignatura.getCodi().equalsIgnoreCase(codi)) {
                return assignatura;
            }
        }
        return null;
    }
    
    /** Afegeix o modifica una nova assignatura segons el codi d'assignatura.
     * @return boolean indica si s'ha afegit l'assignatura correctament, no s'afegirà si algun dels paràmetres no és l'esperat
     **/
    public boolean addAssignatura(Grau _grau, String _nom, String _codi, TipusMateria _tipus, int _alumnes, int _quadrimestre, int _horesTeoria, int _horesPractica, TipusHoresPractica _tipusHoresPractica) {
        
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
        DisponibilitatHoraria disponibilitat = new DisponibilitatHoraria();
        Calendar dia = new GregorianCalendar();
        int ordre = 0;
        int diaDeLaSetmana = 0;

        Iterator<Date> it = dies.listIterator();
        while (it.hasNext()) {
            //System.out.println(iterator.next());
            Date d = it.next();
            dia.setTime(d);
            ordre = getSetmanaOrdre(d, quadri);
            diaDeLaSetmana = dia.get(Calendar.DAY_OF_WEEK);
            disponibilitat.addDia(diaDeLaSetmana, ordre);
        }
        
        System.out.println(disponibilitat.toString());
    }
    
    private class DisponibilitatHoraria {
        private Vector<DiaDeLaSetmana> disponibilitat;
        
        public DisponibilitatHoraria() {
            disponibilitat = new Vector<DiaDeLaSetmana>();
            disponibilitat.add(new DiaDeLaSetmana(0));  //no utilitzat
            disponibilitat.add(new DiaDeLaSetmana(1));  //diumenge
            disponibilitat.add(new DiaDeLaSetmana(2));
            disponibilitat.add(new DiaDeLaSetmana(3));
            disponibilitat.add(new DiaDeLaSetmana(4));
            disponibilitat.add(new DiaDeLaSetmana(5));
            disponibilitat.add(new DiaDeLaSetmana(6));  //divendres
        }
        
        public void addDia(int diaDeLaSetmana, int ordre) {
            disponibilitat.get(diaDeLaSetmana).addDia(ordre);
        }
        
        public int addReserva(int hores) {
            
            int numReserves, dies;
            while (hores > 0) {
                for (int i = 6; i >= 2; --i) {
                    numReserves = disponibilitat.get(i).getReserves();
                    if (i == 2 || disponibilitat.get(i - 1).getReserves() > numReserves) {
                        //ocupem aquest dia
                        dies = disponibilitat.get(i).getDies();
                        if (hores >= dies * 2) {
                            //Podem ocupar una hora d'aquest dia per a tots els ordres (11,21,12 i 22)
                            hores -= dies * 2;
                            disponibilitat.get(i).addReserva(0);    //11
                            disponibilitat.get(i).addReserva(1);    //21
                            disponibilitat.get(i).addReserva(2);    //12
                            disponibilitat.get(i).addReserva(3);    //22
                        } else {
                            //No podem ocupar una hora sencera d'aquest dia, l'ocupem per a ordre 1x o 2x
                            int reserves1x = disponibilitat.get(i).getReserves(0) + disponibilitat.get(i).getReserves(2);
                            int reserves2x = disponibilitat.get(i).getReserves(1) + disponibilitat.get(i).getReserves(3);
                            boolean ocupatPerSetmanaX = true;
                            if (reserves1x <= reserves2x) {
                                //provem d'ocupar ordre 1x
                                dies = disponibilitat.get(i).getDies(0) + disponibilitat.get(i).getDies(2);
                                if (hores >= dies * 2) {
                                    //ocupem ordre 1x
                                    hores -= dies * 2;
                                    disponibilitat.get(i).addReserva(0);
                                    disponibilitat.get(i).addReserva(2);
                                } else {
                                    //NO OCUPAT PER ORDRE 1x
                                    ocupatPerSetmanaX = false;
                                }
                            } else {
                                //provem d'ocuparlo en ordre 2x
                                dies = disponibilitat.get(i).getDies(1) + disponibilitat.get(i).getDies(3);
                                if (hores >= dies * 2) {
                                    //ocupem ordre 2x
                                    hores -= dies * 2;
                                    disponibilitat.get(i).addReserva(1);
                                    disponibilitat.get(i).addReserva(3);
                                } else {
                                    //NO OCUPAT PER ORDRE 2x
                                    ocupatPerSetmanaX = false;
                                }
                            }
                            if (!ocupatPerSetmanaX) {
                                //L'ocupem per a un sol ordre
                                int ordreMenysReservat = 0;
                                int ocupacioMinima = 99;
                                boolean ordreAnteriorQuadrava = false;
                                for (int e = 0; e < 4; ++e) {
                                    int aux = disponibilitat.get(i).getReserves(e);
                                    boolean ordreQuadra = (hores >= disponibilitat.get(i).getDies(e) * 2) ? true : false;
                                    if (aux < ocupacioMinima || (ordreQuadra && !ordreAnteriorQuadrava)) {  //TODO: ARREGLAR!
                                        ocupacioMinima = aux;
                                        ordreMenysReservat = e;
                                    }
                                    ordreAnteriorQuadrava = ordreQuadra;
                                }
                                //Finalment, ocupem l'ordre escollit
                                hores -= disponibilitat.get(i).getDies(ordreMenysReservat);
                                disponibilitat.get(i).addReserva(ordreMenysReservat);
                            }

                        }
                    }
                }
            }
            
            return hores;
        }
        
        @Override
        public String toString() {
            StringBuilder result = new StringBuilder();
            String newLine = System.getProperty("line.separator");
            result.append( this.getClass().getName() );
            result.append( " {" );
            result.append(newLine);

            for (int i = 0; i < 7; ++i) {
                result.append(disponibilitat.get(i).toString());
                result.append(newLine);
            }
            
            result.append("}");

            return result.toString();
        }
        
        private class DiaDeLaSetmana {
            private int index;  //1-7: 1 = diumenge, 2 = dilluns, ...
            private Vector<Integer> numero;   //numero[0] = dies amb ordre 11, numero[1] = dies amb ordre 21, etc.
            public Vector<Integer> ocupacio;    //Quantitat de classes ja assignades en aquest dia
            
            public DiaDeLaSetmana(int _index) {
                index = _index;
                numero = new Vector<Integer>();
                numero.add(0,0);
                numero.add(1,0);
                numero.add(2,0);
                numero.add(3,0);
                ocupacio = new Vector<Integer>();
                ocupacio.add(0,0);
                ocupacio.add(1,0);
                ocupacio.add(2,0);
                ocupacio.add(3,0);
            }
            
            @Override
            public String toString() {
                StringBuilder result = new StringBuilder();
                //String newLine = System.getProperty("line.separator");
                result.append( this.getClass().getName() );
                result.append( " " );
                result.append( Integer.toString(index) );
                result.append( " {" );
                
                result.append(numero.get(0));
                result.append( " : " );
                result.append(ocupacio.get(0));
                result.append( ", " );
                
                result.append(numero.get(1));
                result.append( " : " );
                result.append(ocupacio.get(1));
                result.append( ", " );
                
                result.append(numero.get(2));
                result.append( " : " );
                result.append(ocupacio.get(2));
                result.append( ", " );
                
                result.append(numero.get(3));
                result.append( " : " );
                result.append(ocupacio.get(3));
                
                result.append("}");

                return result.toString();
            }
            
            /** Augmenta el número de dies disponibles donat un ordre. **/
            public void addDia(int ordre) {
                numero.set(ordre, numero.get(ordre) + 1);
            }
            
            /** Retorna el número de dies disponibles donat un ordre. **/
            public int getDies(int ordre) {
                return numero.get(ordre);
            }
            
            /** Retorna el número de dies disponibles independentment de l'ordre. **/
            public int getDies() {
                return numero.get(0) + numero.get(1) + numero.get(2) + numero.get(3);
            }
            
            /** Augmenta l'ocupació d'aquest dia. **/
            public void addReserva(int ordre) {
                ocupacio.set(ordre, ocupacio.get(ordre) + 1);
            }
            
            /** Retorna el número de classes que ocupen aquest dia. **/
            public int getReserves(int ordre) {
                return ocupacio.get(ordre);
            }
            
            /** Retorna el número de classes que ocupen aquest dia independentment de l'ordre. **/
            public int getReserves() {
                return ocupacio.get(0) + ocupacio.get(1) + ocupacio.get(2) + ocupacio.get(3);
            }
            
            /** Retorna el index que correspon al dia de la setmana [0-6]. **/
            public int getIndex() {
                return index;
            }
        }
    }
}


