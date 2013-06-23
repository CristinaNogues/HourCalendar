/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hourcalendar;

/**
 *
 * @author asus
 */
public class Assignatura implements java.io.Serializable {
    //public Grau grau;
    public int idGrau;
    public String nom;
    public String sigles;
    public int codi;
    //public TipusMateria tipus;
    public int idTipusMateria;
    public int alumnes;
    public int quadrimestre;
    public int horesTeoria;
    public int horesPractica;
    //public TipusHoresPractica tipusHoresPractica;
    public int idTipusHoresPractica;    //id TipusAula
    public int grups;
    public boolean esValida = true;
    
    
    public Assignatura(Grau _grau, String _nom, String _sigles, int _codi, TipusMateria _tipus, int _alumnes, int _quadrimestre, int _horesTeoria, int _horesPractica, TipusAula _tipusHoresPractica, int _grups) {
        Base base = HourCalendar.getBase();
        idGrau = _grau.getID();//base.getGrauAt(base.getIndexOfGrau(_grau)).getID();
        //grau = _grau;
        nom = _nom;
        sigles = _sigles;
        codi = _codi;
        //tipus = _tipus;
        idTipusMateria = _tipus.getID();
        alumnes = _alumnes;
        quadrimestre = _quadrimestre;
        horesTeoria = _horesTeoria;
        horesPractica = _horesPractica;
        //tipusHoresPractica = _tipusHoresPractica;
        idTipusHoresPractica = _tipusHoresPractica.getID();
        grups = _grups;
        /*
        if (!(base.hasGrau(grau) && base.hasTipusMateria(tipus) && base.hasTipusHoresPractica(tipusHoresPractica)))
            esValida = false;
        
        if (nom.isEmpty() || !(codi > 0))
            esValida = false;
        
        if (!(quadrimestre > 0 && quadrimestre <= 8))
            esValida = false;
        */
    }
    
    public String getNom() {
        return nom;
    }
    
    public String getSigles() {
        return sigles;
    }
    
    public int getCodi() {
        return codi;
    }
    
    public int getGrups() {
        return grups;
    }
    
    public Grau getGrau() {
        //return grau;
        Base base = HourCalendar.getBase();
        return base.getGrau(idGrau);
    }
    
    public TipusMateria getTipus() {
        //return tipus;
        Base base = HourCalendar.getBase();
        return base.getTipusMateria(idTipusMateria);
    }
    
    public int getAlumnes() {
        return alumnes;
    }
    
    public int getQuadrimestre() {
        return quadrimestre;
    }
    
    public int getHoresTeoria() {
        return horesTeoria;
    }
    
    public int getHoresPractica() {
        return horesPractica;
    }
    
    public TipusAula getTipusHoresPractica() {
        //return tipusHoresPractica;
        Base base = HourCalendar.getBase();
        return base.getTipusAula(idTipusHoresPractica);
    }
    
    public boolean esValida() {
        return esValida;
    }
    
}