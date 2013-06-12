/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hourcalendar;

/**
 *
 * @author asus
 */
public class Assignatura {
    private Base base;
    public Grau grau;
    public String nom;
    public String sigles;
    public int codi;
    public TipusMateria tipus;
    public int alumnes;
    public int quadrimestre;
    public int horesTeoria;
    public int horesPractica;
    public TipusHoresPractica tipusHoresPractica;
    public int grups;
    public String inicial;  //Lletra inicial que representa la titulació (grau), exemples: I = Informàtica, T = Telecos, M = Mecànica; Poden estar repetits
    public boolean esValida = true;
    
    
    public Assignatura(Base _base, Grau _grau, String _nom, String _sigles, int _codi, TipusMateria _tipus, int _alumnes, int _quadrimestre, int _horesTeoria, int _horesPractica, TipusHoresPractica _tipusHoresPractica, int _grups, String _inicial) {
        base = _base;
        grau = _grau;
        nom = _nom;
        sigles = _sigles;
        codi = _codi;
        tipus = _tipus;
        alumnes = _alumnes;
        quadrimestre = _quadrimestre;
        horesTeoria = _horesTeoria;
        horesPractica = _horesPractica;
        tipusHoresPractica = _tipusHoresPractica;
        grups = _grups;
        inicial = _inicial;
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
    
    public TipusMateria getTipus() {
        return tipus;
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
    
    public TipusHoresPractica getTipusHoresPractica() {
        return tipusHoresPractica;
    }
    
    public boolean esValida() {
        return esValida;
    }
    
}