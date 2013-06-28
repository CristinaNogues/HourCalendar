package hourcalendar;

import java.util.Vector;

public class DisponibilitatAules {
    private Vector<Integer> numero;  //Nº d'aules de cada tipus, ex: numero[2] == 8 representa que hi ha 8 aules amb index 2 del vector tipusAula de base. (p.e.: 8 aules d'informàtica)
    private Vector<Aula> aules;

    public DisponibilitatAules() {
        inicialitzaVectors();
    }
    
    public DisponibilitatAules(Vector<Aula> aules) {
        inicialitzaVectors();
        inicialitzaAules(aules);
    }
    
    public DisponibilitatAules(DisponibilitatAules other) {
        inicialitzaVectors();
        inicialitzaAules(other.aules);
    }
    
    private void inicialitzaAules(Vector<Aula> aules) {
        Base base = HourCalendar.getBase();
        //Clonem manualment el vector d'aules
        this.aules = new Vector<Aula>();
        for (int i = 0; i < aules.size(); ++i) {
            Aula aula = new Aula(aules.get(i));
            this.aules.add(aula);
            //Incrementem el número d'aules d'aquest tipus d'aula.
            int index = base.getIndexOfTipusAula(aula.getTipusAula());
            numero.set(index, numero.get(index) + 1);
        }
    }
    
    private void inicialitzaVectors() {
        Base base = HourCalendar.getBase();
        numero = new Vector<Integer>();
        
        int numTipusAules = base.getNumTipusAules();
        for (int i = 0; i < numTipusAules; ++i) {
            numero.add(0);
        }
    }

    public int getNumAules(TipusAula tipusAula) {
        Base base = HourCalendar.getBase();
        return numero.get(base.getIndexOfTipusAula(tipusAula));
    }
    
    /** Retorna el vector d'aules de tots els tipus d'aula disponibles. **/
    public Vector<Aula> getAulesDisponibles() {
        return aules;
    }
    
    /** Retorna un nou vector d'aules amb les aules disponibles del tipus especificat. **/
    public Vector<Aula> getAulesDisponibles(TipusAula tipusAula) {
        Base base = HourCalendar.getBase();
        //Inicialitzem el vector a retornar que contindrà les aules del tipus donat.
        Vector<Aula> _aules = new Vector<Aula>();
        int idTipusAula = tipusAula.getID();
        //omplim el vector d'aules del mateix tipus d'aula
        for (int i = 0; i < aules.size(); ++i) {
            Aula aula = aules.get(i);
            if (aula.idTipusAula == idTipusAula) {
                _aules.add(aula);
            }
        }
        return _aules;
    }
    
    /** Retorna true si l'aula amb nom especificat està disponible. **/
    public boolean esDisponible(String nomAula) {
        for (int i = 0; i < aules.size(); ++i) {
            if (aules.get(i).getNom().equals(nomAula)) {
                return true;
            }
        }
        return false;
    }
    
    /** Elimina l'aula amb nom especificat de la llista d'aules disponibles. **/
    public void ocupa(String nomAula) {
        Base base = HourCalendar.getBase();
        for (int i = 0; i < aules.size(); ++i) {
            if (aules.get(i).getNom().equals(nomAula)) {
                Aula aula = aules.get(i);
                //Decrementem el número d'aules d'aquest tipus d'aula.
                int index = base.getIndexOfTipusAula(aula.getTipusAula());
                numero.set(index, numero.get(index) - 1);
                //treiem l'aula del vector d'aules disponibles
                aules.remove(i);
                break;
            }
        }
    }
 }
