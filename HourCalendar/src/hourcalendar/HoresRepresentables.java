/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hourcalendar;

import hourcalendar.Base.Regles;
import hourcalendar.Base;
import hourcalendar.DisponibilitatHoraria.Reserva;
import java.util.Vector;

/**
 *
 * @author admin
 */
public class HoresRepresentables {
    //public Vector<Reserva> reserves;
    private Vector<ClasseAgrupada> classesAgrupades; //Al finalitzar la combinació d'hores aquest vector estarà buit
    private Vector< Vector<ClasseAgrupada> > hores;
    
    public HoresRepresentables() {
        classesAgrupades = new Vector<ClasseAgrupada>();
        hores = new Vector< Vector<ClasseAgrupada> >();
    }
    
    public HoresRepresentables(Vector< Vector<String> > ocupacio) {
        Base.dbg("HoresRepresentables()!");
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
        classesAgrupades = new Vector<ClasseAgrupada>();
        for (int i = 0; i < classes.size(); ++i)
            classesAgrupades.add(new ClasseAgrupada(classes.get(i), ordres.get(i)));
        
        //Inicialitzem el vector d'hores representables
        hores = new Vector< Vector<ClasseAgrupada> >();
        combina();
    }
    
    public void combina() {
        while (classesAgrupades.size() > 0) {
            Vector<ClasseAgrupada> classes = new Vector<ClasseAgrupada>();
            //Afegim la primera classe agrupada al vector de classes representables en aquesta hora
            classes.add(classesAgrupades.firstElement());
            Base.dbg("INICIEM NOU SOLAPAMENT A PARTIR DE: ".concat(classesAgrupades.firstElement().toString()));
            classesAgrupades.removeElementAt(0);
            //Solapem les classes agrupades solapables amb l'actual
            boolean reiterar = true;
            while (reiterar && classesAgrupades.size() > 0) {
                reiterar = false;
                for (int i = classesAgrupades.size(); --i >= 0;) {
                    ClasseAgrupada c = classesAgrupades.firstElement();
                    Base.dbg("\tSOLAPABLE?: ".concat(c.toString()).concat(" POS: ").concat(String.valueOf(i)));
                    if (esSolapable(classes, c)) {
                        reiterar = true;
                        classes.add(c);
                        Base.dbg("\t\tAFEGIM A CLASSES SOLAPADES: ".concat(c.toString()).concat(" POS: ").concat(String.valueOf(i)));
                        classesAgrupades.removeElement(c);
                        break;
                    }
                }
                
            }
            //Afegim el vector de classes que s'han pogut solapar per a una hora al vector d'hores representables
            this.hores.add(classes);
        }
    }
    
    public Vector<ClasseAgrupada> get(int index) { return hores.get(index); }
    public int size() { return hores.size(); }
    
    private boolean esSolapable(Vector<ClasseAgrupada> classes, ClasseAgrupada nova) {
        boolean solapable = true;
        ClasseAgrupada classe;
        boolean solaparHoresPractica = Regles.SOLAPAR_HORES_PRACTICA.get();
        
        for (int i = classes.size(); --i >= 0;) {
            classe = classes.get(i);
            if (!classe.esSolapable(nova, solaparHoresPractica)) {
                solapable = false;
                break;
            }
        }
        return solapable;
    }
    
    //##########################################################################
    
    public class ClasseAgrupada {
        public String codi;             //234223#4#1
        public int codiAssignatura;     //234223
        public int tipusAula;           //4
        public int grup;                //1
        public Vector<Integer> ordres;  //{0, 2}
        public int disposicio;  //15 = Sempre, 5 = :s1, 10 = :s2, 1 = :s11, 2 = :s21, ...
                                //2 ^ ordre = 1 2 4 8
        public ClasseAgrupada (String _codi, Vector<Integer> ordres) {
            disposicio = 0;
            codi = _codi;
            codiAssignatura = Reserva.getCodi(codi);
            tipusAula = Reserva.getTipusAula(codi);
            grup = Reserva.getGrup(codi);
            this.ordres = new Vector<Integer>();
            for (int i = 0; i < ordres.size(); ++i) {
                this.ordres.add(ordres.get(i));
                disposicio += Math.pow(2, ordres.get(i));
            }
        }
        
        public boolean esSolapable(ClasseAgrupada other) {
            return esSolapable(other, Regles.SOLAPAR_HORES_PRACTICA.get());
        }
        
        public boolean esSolapable(ClasseAgrupada other, boolean solaparHoresPractica) {
            if (!comparteixOrdre(other)) {
                return true;
            } else {
                if (solaparHoresPractica) {
                    if (codiAssignatura != other.codiAssignatura) {
                        if (grup != other.grup && grup != 0 && other.grup != 0) {
                            //Es possible el solapament de diferents grups de pràctiques d'assignatures diferents
                            return true;
                        } else {
                            //Els grups coincideixen, no es pot solapar
                            return false;
                        }
                    } else {
                        //Una mateixa assignatura no pot solapar diferents grups de practiques
                        return false;
                    }
                } else {
                    //No es poden solapar hores de practica de diferents assignatures i grups
                    return false;
                }
            }
        }
        
        public boolean comparteixOrdre(ClasseAgrupada other) {
            
            for (int i = other.ordres.size(); --i >= 0;) {
                if (this.ordres.contains(other.ordres.get(i))) {
                    return true;
                }
            }
            
            return false;
        }
        
        public String toString() {
            String nom = HourCalendar.getBase().getAssignatura(codiAssignatura).getNom();
            String grup = "I".concat(String.valueOf(HourCalendar.getBase().getAssignatura(codiAssignatura).getQuadrimestre()));
            grup = grup.concat("5");
            if (this.grup != 0) grup = grup.concat("1").concat(String.valueOf(this.grup));
            String aula = (this.tipusAula == 0) ? "AULA" : "LAB";
            return nom.concat(" ").concat(aula).concat(" ").concat(grup).concat(" ").concat(getOrdreAsString());
        }
        
        public String getOrdreAsString() {
            String s = ":";
            if (disposicio == 15) s = "";
            else if (disposicio == 5) s = s.concat("s1");
            else if (disposicio == 10) s = s.concat("s2");
            else if (disposicio == 1) s = s.concat("s11");
            else if (disposicio == 2) s = s.concat("s21");
            else if (disposicio == 4) s = s.concat("s12");
            else if (disposicio == 8) s = s.concat("s22");
            else {
                if (disposicio == 3) s = s.concat("s11, ").concat("s21");
                else if (disposicio == 6) s = s.concat("s21, ").concat("s12");
                else if (disposicio == 12) s = s.concat("s12, ").concat("s22");
                else if (disposicio == 9) s = s.concat("s11, ").concat("s22");
                else {
                    if (disposicio == 11) s = s.concat("s11, ").concat("s21, ").concat("s22");
                    else if (disposicio == 7) s = s.concat("s11, ").concat("s21, ").concat("s12");
                    else if (disposicio == 13) s = s.concat("s11, ").concat("s12, ").concat("s22");
                    else if (disposicio == 14) s = s.concat("s21, ").concat("s12, ").concat("s22");
                    else {
                        s = ":FAIL!!!!!!!!!!!!!!!!!";
                        //System.out.println("\t\t\t\t\tDISPOSICIO FAIL AT ".concat(String.valueOf(disposicio)));
                    }
                }
            }
            return s;
        }
    }
}
