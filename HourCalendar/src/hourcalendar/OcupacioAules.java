/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hourcalendar;

import hourcalendar.HoresRepresentables.ClasseAgrupada;
import java.util.Vector;

/**
 *
 * @author admin
 */
public class OcupacioAules {
    private Vector<Dia> dies;   //els dies de la setmana
    
    public OcupacioAules() {
        dies = new Vector<Dia>();
        for (int i = 0; i <= 7; ++i) {
            dies.add(new Dia(i));
        }
    }
    
    /** Donat un dia, una hora i una classe, n'ocupa una aula i retorna l'aula ocupada.
     *  Retorna null si no s'ha trobat una aula disponible per a les reserves que conformen la classe.
     */
    public Aula assigna(int _dia, int hora, ClasseAgrupada classe) {
        Base base = HourCalendar.getBase();
        Dia dia = dies.get(_dia);
        boolean disponible = false;
        //int numAulesDisponiblesPrimerOrdre = dia.horesClasse.get(classe.ordres.get(0)).get(hora).getNumAules(base.getTipusAula(classe.tipusAula));
        Vector<Aula> aulesDisponiblesPrimerOrdre = dia.horesClasse.get(classe.ordres.get(0)).get(hora).getAulesDisponibles(base.getTipusAula(classe.tipusAula));
        int numAulesDisponiblesPrimerOrdre = aulesDisponiblesPrimerOrdre.size();
        int indexAula = 0;
        for (indexAula = 0; indexAula < numAulesDisponiblesPrimerOrdre; ++indexAula) {
            disponible = true;
            Aula aula = aulesDisponiblesPrimerOrdre.get(indexAula);
            //mirem si l'aula escollida està disponible per als altres ordres
            for (int indexOrdre = 1; indexOrdre < classe.ordres.size(); ++indexOrdre) {
                if (!dia.horesClasse.get(classe.ordres.get(indexOrdre)).get(hora).esDisponible(aula.getNom())) {
                    //No està disponible aquesta aula per aquest odre, passem a la seguent aula disponible del primer ordre
                    disponible = false;
                    break;
                } else {
                    //Si que està disponible, ho comprovem al seguent ordre
                    continue;
                }
            }
            if (disponible) break;
        }
        if (disponible) {
            //s'ha trobat una aula disponible per aquesta classe, l'ocupem per a tots els ordres
            Aula aula = aulesDisponiblesPrimerOrdre.get(indexAula);
            for (int indexOrdre = 0; indexOrdre < classe.ordres.size(); ++indexOrdre) {
                dia.horesClasse.get(classe.ordres.get(indexOrdre)).get(hora).ocupa(aula.getNom());
            }
            return aula;
        } else {
            //No s'ha trobat cap aula disponible per aquesta classe
            return null;
        }
    }

    public class Dia {
        public Vector< Vector<DisponibilitatAules> > horesClasse; //Representa les disponibilitats d'aules per a les 6 hores de classe diàries i els 4 setmanaOrdre.
                                                                  //horesClasse[0][2] és la disponibilitat d'aules per a :s11 a tercera hora (10:30-12:30)
        private int index;  //1-7: 1 = diumenge, 2 = dilluns, ...
        
        public Dia(int index) {
            Base base = HourCalendar.getBase();
            this.index = index;
            horesClasse = new Vector< Vector<DisponibilitatAules> >();

            //per als 4 setmanaOrdre (s11, s21, s12, s22):
            //Inicialitzem les disponibilitats d'aules per a totes les hores representables del dia
            for (int ordre = 0; ordre < 4; ++ordre) {
                horesClasse.add(new Vector<DisponibilitatAules>());
                for (int i = 0; i <= 5; ++i) {
                    if (index >= 2 && index <= 6) {
                        //de dilluns a divendres, inicialitzem la disponibilitat d'aules amb aules
                        horesClasse.get(ordre).add(new DisponibilitatAules(base.aules));
                    } else {
                        //els altres dies sense aules disponibles
                        horesClasse.get(ordre).add(new DisponibilitatAules());
                    }
                }
            }
        }
    }
}
