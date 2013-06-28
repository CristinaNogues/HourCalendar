package hourcalendar;

public class Assignatura implements java.io.Serializable {
    public int idGrau;
    public String nom;
    public String sigles;
    public int codi;
    public int idTipusMateria;
    public int alumnes;
    public int quadrimestre;
    public int horesTeoria;
    public int horesPractica;
    public int idTipusHoresPractica;    //id TipusAula
    public int grups;
    public boolean esValida = true;
    
    
    public Assignatura(Grau _grau, String _nom, String _sigles, int _codi, TipusMateria _tipus, int _alumnes, int _quadrimestre, int _horesTeoria, int _horesPractica, TipusAula _tipusHoresPractica, int _grups) {
        Base base = HourCalendar.getBase();
        idGrau = _grau.getID();//base.getGrauAt(base.getIndexOfGrau(_grau)).getID();
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
        Base base = HourCalendar.getBase();
        return base.getGrau(idGrau);
    }
    
    public TipusMateria getTipus() {
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
        Base base = HourCalendar.getBase();
        return base.getTipusAula(idTipusHoresPractica);
    }
    
    public boolean esValida() {
        return esValida;
    }
    
}