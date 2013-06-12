/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hourcalendar;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import hourcalendar.Base;



/**
 *
 * @author admin
 */
public class DisponibilitatHoraria implements Cloneable {
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
    
    public void imprimeixPonderacio() {
        int ponderacio = 0;
        String str = "[ ";
        
        for (int i = 2; i <= 6; ++i) {
            int temp = disponibilitat.get(i).avalua();
            str = str.concat(String.valueOf(temp)).concat(" ");
            ponderacio += temp;
        }
        
        str = str.concat("] SUM: ").concat(String.valueOf(ponderacio));
        System.out.println(str);
    }
    
    public int avalua() {
        int ponderacio = 0;
        
        for (int i = 2; i <= 6; ++i)
            ponderacio += disponibilitat.get(i).avalua();
        
        return ponderacio;
    }
    
    public Vector<DiaDeLaSetmana> getDisponibilitat() {
        return disponibilitat;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        DisponibilitatHoraria clon = (DisponibilitatHoraria) super.clone();
        //clon.disponibilitat = (Vector<DiaDeLaSetmana>) disponibilitat.clone();
        //clon.disponibilitat.clear();
        clon.disponibilitat = new Vector<DiaDeLaSetmana>();
        for (int i = 0; i <= 6; ++i)
            clon.disponibilitat.add((DiaDeLaSetmana) disponibilitat.get(i).clone());
        
        return clon;    //super.clone();
    }

    public void addDia(int diaDeLaSetmana, int ordre) {
        disponibilitat.get(diaDeLaSetmana).addDia(ordre);
    }

    public int addReserva(int hores, String codi) throws CloneNotSupportedException {
        Base.dbg2("RESERVA - ".concat(codi).concat(": ").concat(String.valueOf(hores)));
        int millorPonderacio = -20480;
        //int diaAmbMillorPonderacio = 0;
        int horesMillorPonderacio = 0;
        Vector<DiaDeLaSetmana> millorDisponibilitat = new Vector<DiaDeLaSetmana>();
        for (int i = 6; i >= 2; --i) {
            DisponibilitatHoraria disponibilitatHorariaActual = (DisponibilitatHoraria) this.clone();
            int horesActual = hores;

            horesActual = disponibilitatHorariaActual.addReserva(horesActual, codi, i);
            Base.dbg2("\taddReservaElDia(".concat(codi).concat(")[").concat(String.valueOf(i)).concat("]: ").concat(String.valueOf(horesActual)).concat(" / ").concat(String.valueOf(hores)).concat(" TOTAL RESERVES SETMANA: ").concat(String.valueOf(disponibilitatHorariaActual.getReserves())));
            if (horesActual > 0) {
                //Si encara queden hores per col·locar, iterem recursivament
                horesActual = disponibilitatHorariaActual.addReserva(horesActual, codi);
            }
            
            //TODO: AVALUA SEGONS HORES ACTUAL I PONDERACIÓ HORARI GENERAT
            int ponderacio = disponibilitatHorariaActual.avalua();  //valor <= 0
            Base.dbg2(codi.concat(": [").concat(String.valueOf(ponderacio)).concat(" - ").concat(String.valueOf(horesActual)).concat("] ").concat(String.valueOf(ponderacio - horesActual)).concat(" > ").concat(String.valueOf(millorPonderacio)).concat(" ???"));
            if (ponderacio - horesActual > millorPonderacio) {
                Base.dbg2("\t".concat("TRUE!").concat(": [").concat(String.valueOf(ponderacio)).concat(" - ").concat(String.valueOf(horesActual)).concat("] ").concat(String.valueOf(ponderacio - horesActual)).concat(String.valueOf(millorPonderacio)));
                millorPonderacio = ponderacio - horesActual;
                
                //diaAmbMillorPonderacio = i;
                horesMillorPonderacio = horesActual;
                //CLON millorDisponibilitat!
                //millorDisponibilitat = (Vector<DiaDeLaSetmana>) disponibilitatHorariaActual.disponibilitat.clone();
                millorDisponibilitat = new Vector<DiaDeLaSetmana>();
                for (int u = 0; u <= 6; ++u)
                    millorDisponibilitat.add((DiaDeLaSetmana) disponibilitatHorariaActual.disponibilitat.get(u).clone());
            }
            
            disponibilitatHorariaActual = null;
        }
        
        this.disponibilitat = millorDisponibilitat;
        Base.dbg2("FALLBACK! ".concat(String.valueOf(horesMillorPonderacio)));
        return horesMillorPonderacio;
    }

    /** Reserva una classe al dia especificat per a l'assignatura (codi).
     * 
     * Primer intenta reservar una classe per als 4 ordres diferents (Setmana 11, 21, 12 i 22).
     * Si la reserva supera les hores restants a col·locar de l'assignatura
     * 
     * @param hores Hores restants per a col·locar
     * @param codi Codi de l'assignatura amb identificador de tipus d'aula i grup, ex: 340380#5#0 on 340380 = PROP, 5 = Laboratori Informàtica, 0 = grup comú (1 seria el grup de práctiques 1, etc)
     * @param diaDeLaSetmana Index corresponent al dia de la setmana on 2 = dilluns, 3 = dimarts, ..., 6 = divendres
     * @return Número d'hores restants una vegada feta la reserva
     */
    public int addReserva(int hores, String codi, int diaDeLaSetmana) {
        int i = diaDeLaSetmana;
        //ocupem aquest dia
        int dies = disponibilitat.get(i).getDies();
        if (hores >= dies * 2) {
            //Podem ocupar una hora d'aquest dia per a tots els ordres (11,21,12 i 22)
            hores -= dies * 2;
            disponibilitat.get(i).addReserva(0, codi);    //11
            disponibilitat.get(i).addReserva(1, codi);    //21
            disponibilitat.get(i).addReserva(2, codi);    //12
            disponibilitat.get(i).addReserva(3, codi);    //22
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
                    disponibilitat.get(i).addReserva(0, codi);
                    disponibilitat.get(i).addReserva(2, codi);
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
                    disponibilitat.get(i).addReserva(1, codi);
                    disponibilitat.get(i).addReserva(3, codi);
                } else {
                    //NO OCUPAT PER ORDRE 2x
                    ocupatPerSetmanaX = false;
                }
            }
            if (!ocupatPerSetmanaX) {
                int millorOrdre = 0;
                int ponderacioMillorOrdre = 250;
                for (int e = 0; e < 4; ++e) {
                    int ponderacioActual = avaluaOcupacioOrdreUnic(i, e, hores);
                    if (ponderacioMillorOrdre > ponderacioActual) {
                        ponderacioMillorOrdre = ponderacioActual;
                        millorOrdre = e;
                    }
                }
                //Finalment, ocupem l'ordre escollit
                hores -= disponibilitat.get(i).getDies(millorOrdre) * 2;
                disponibilitat.get(i).addReserva(millorOrdre, codi);
            }
        }
        return hores;
    }
    
    /** Retorna la quantitat de reserves a tots els dies. **/
    private int getReserves() {
        int total = 0;
        
        for (int dia = 2; dia <= 6; ++dia) {
            total += disponibilitat.get(dia).getReserves();
        }
        return total;
    }

    /** Simula la reserva d'un dia segons ordre donades unes hores i en retorna la ponderació, quant més petita millor.
     *
     * TODO: Un professor no pot estar a la vegada impartint dues classes en una mateixa hora!!
     */
    private int avaluaOcupacioOrdreUnic(int dia, int ordre, int hores) {
        int ocupacioActual = disponibilitat.get(dia).getReserves(ordre) + 1;
        int horesOrdreActual = hores - disponibilitat.get(dia).getDies(ordre) * 2;

        return (horesOrdreActual >= 0) ? horesOrdreActual + ocupacioActual : ocupacioActual + (horesOrdreActual * -1);
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

    public class DiaDeLaSetmana implements Cloneable {
        private int index;  //1-7: 1 = diumenge, 2 = dilluns, ...
        private Vector<Integer> numero;   //numero[0] = dies amb ordre 11, numero[1] = dies amb ordre 21, etc.
        private Vector< Vector<String> > ocupacio;    //Codis de les assignatures i quantitat de classes ja assignades en aquest dia segons ordre
        
        public DiaDeLaSetmana(int _index) {
            index = _index;
            numero = new Vector<Integer>();
            numero.add(0,0);
            numero.add(1,0);
            numero.add(2,0);
            numero.add(3,0);
            ocupacio = new Vector<>();
            ocupacio.add(0, new Vector<String>());
            ocupacio.add(1, new Vector<String>());
            ocupacio.add(2, new Vector<String>());
            ocupacio.add(3, new Vector<String>());
        }
        
        /** Retorna un objecte tipus HoresRepresentables. **/
        public HoresRepresentables getHoresRepresentables() { return new HoresRepresentables(ocupacio); }
        
        @Override
        protected Object clone() throws CloneNotSupportedException {
            DiaDeLaSetmana clon = (DiaDeLaSetmana) super.clone();
            
            clon.numero = (Vector<Integer>) numero.clone();
            
            clon.ocupacio = new Vector<>();
            for (int i = 0; i <= 3; ++i)
                clon.ocupacio.add((Vector<String>) ocupacio.get(i).clone());

            return clon;    //super.clone();
        }
        
        public Vector< Vector<String> > getOcupacio() {
            return ocupacio;
        }
        
        /** Funció d'avaluació per a un dia de la setmana, retorna ponderació inferior o igual a 0, quant més elevada millor. **/
        public int avalua() {
            HoresRepresentables horesRepresentables = new HoresRepresentables(ocupacio);
            int numClassesOcupades = 0; //No es aconsellable més de 3 classes al dia, doncs tocaria fer classe per la tarda
            int numClassesRepresentables = horesRepresentables.size();
            int numClassesDuplicades = 0;
            int numClassesDuplicadesExactes = 0;
            for (int ordre = 0; ordre <= 3; ++ordre) {
                Vector<String> reserves = ocupacio.get(ordre);
                Vector<Integer> duplicitat = new Vector<Integer>(); //No es aconsellable fer més d'una classe al dia de la mateixa assignatura, doncs es molt pesat
                Vector<String> duplicitatExacte = new Vector<String>();
                if (reserves.size() > numClassesOcupades)
                    numClassesOcupades = reserves.size();
                
                for (int i = 0; i < reserves.size(); ++i) {
                    int codi = Reserva.getCodi(reserves.get(i));
                    if (duplicitat.indexOf(codi) == -1) {
                        duplicitat.add(codi);
                    } else {
                        ++numClassesDuplicades;
                    }
                    if (duplicitatExacte.indexOf(reserves.get(i)) == -1) {
                        duplicitatExacte.add(reserves.get(i));
                    } else {
                        ++numClassesDuplicadesExactes;
                        //System.out.println("\t\tDuplicades exactes!!");
                    }
                }
                
            }
            //La implementació de la ponderació és bastant pobre
            
            if (numClassesRepresentables > 3)
                numClassesRepresentables *= (5 + (Math.pow(3, (numClassesRepresentables - 4))));
            
            numClassesRepresentables *= 5;
            
            numClassesRepresentables *= -1;
            numClassesRepresentables += numClassesOcupades;
            if (numClassesRepresentables > 0) numClassesRepresentables = 0;
            
            return numClassesRepresentables - ((300 * numClassesDuplicadesExactes) + (10 * numClassesDuplicades));
        }

        @Override
        public String toString() {
            StringBuilder result = new StringBuilder();
            String newLine = System.getProperty("line.separator");
            result.append( this.getClass().getName() );
            result.append( " " );
            result.append( Integer.toString(index) );
            result.append( " {" );
            result.append( newLine );

            int numClassesOcupades = 0;
            for (int ordre = 0; ordre <= 3; ++ordre) {
                Vector<String> reserves = ocupacio.get(ordre);
                if (reserves.size() > numClassesOcupades)
                    numClassesOcupades = reserves.size();
                
                for (int i = 0; i < reserves.size(); ++i) {
                    //int codi = Reserva.getCodi(reserves.get(i));
                    result.append("\t".concat(reserves.get(i)).concat(" S").concat(String.valueOf(ordre)));
                    result.append( newLine );
                }
                
            }
            /*
            result.append(numero.get(0));
            result.append( " : " );
            result.append(ocupacio.get(0).size());
            result.append( ", " );

            result.append(numero.get(1));
            result.append( " : " );
            result.append(ocupacio.get(1).size());
            result.append( ", " );

            result.append(numero.get(2));
            result.append( " : " );
            result.append(ocupacio.get(2).size());
            result.append( ", " );

            result.append(numero.get(3));
            result.append( " : " );
            result.append(ocupacio.get(3).size());
            */
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
        public void addReserva(int ordre, String codi) {
            //ocupacio.set(ordre, ocupacio.get(ordre) + 1);
            ocupacio.get(ordre).add(codi);
        }

        /** Retorna el número de classes que ocupen aquest dia. **/
        public int getReserves(int ordre) {
            return ocupacio.get(ordre).size();
        }

        /** Retorna el número de classes que ocupen aquest dia independentment de l'ordre. **/
        public int getReserves() {
            return ocupacio.get(0).size() + ocupacio.get(1).size() + ocupacio.get(2).size() + ocupacio.get(3).size();
        }

        /** Retorna el index que correspon al dia de la setmana [0-6]. **/
        public int getIndex() {
            return index;
        }
        
    }
    
    /** Representa una Reserva, obtinguda a partir del codi (String) utilitzat en la construcció de la disponibilitat horària. **/
    public static class Reserva {
        public int codi;            //350380
        public int tipusAula;       //5 = Laboratori Informàtica
        public int grup;            //4
        public String grupAsText;   //I4511
        public String nom;          //PROP
        private static String[] digitsGrup = {"22", "11", "12", "21"};  //new String[]

        public Reserva(String codiCompacte) {
            
            String[] parts = codiCompacte.split("#");
            this.codi = Integer.parseInt(parts[0]);
            this.tipusAula = Integer.parseInt(parts[1]);
            //System.out.println("WAKAKA".concat(digitsGrup[3]));
            this.grup = Integer.parseInt(parts[2]);
            //set groupAsText
            Assignatura assignatura = HourCalendar.getBase().getAssignatura(this.codi);
            this.grupAsText = assignatura.inicial.concat(String.valueOf(assignatura.getQuadrimestre()));
            this.grupAsText = this.grupAsText.concat(String.valueOf((int) 5 + ((int) this.grup / 5)));
            if (this.grup != 0) this.grupAsText = this.grupAsText.concat(digitsGrup[this.grup % 4]);    //concat("1").concat(String.valueOf(this.grup));
            //set nom
            this.nom = assignatura.getSigles();  //getNom();
        }

        public static int getCodi(String codiCompacte) {
            return Integer.parseInt(codiCompacte.split("#")[0]);
        }
        
        public static int getTipusAula(String codiCompacte) {
            return Integer.parseInt(codiCompacte.split("#")[1]);
        }
        
        public static int getGrup(String codiCompacte) {
            return Integer.parseInt(codiCompacte.split("#")[2]);
        }
        
        public String toString() {
            return String.valueOf(codi).concat("#").concat(String.valueOf(tipusAula)).concat("#").concat(String.valueOf(grup));
        }
    }
}
