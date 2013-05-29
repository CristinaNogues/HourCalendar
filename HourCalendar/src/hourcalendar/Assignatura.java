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
    public String codi;
    public TipusMateria tipus;
    public int alumnes;
    public int quadrimestre;
    public int horesTeoria;
    public int horesPractica;
    public TipusHoresPractica tipusHoresPractica;
    private boolean esValida = true;
    
    
    public Assignatura(Base _base, Grau _grau, String _nom, String _codi, TipusMateria _tipus, int _alumnes, int _quadrimestre, int _horesTeoria, int _horesPractica, TipusHoresPractica _tipusHoresPractica) {
        base = _base;
        grau = _grau;
        nom = _nom;
        codi = _codi;
        tipus = _tipus;
        alumnes = _alumnes;
        quadrimestre = _quadrimestre;
        horesTeoria = _horesTeoria;
        horesPractica = _horesPractica;
        tipusHoresPractica = _tipusHoresPractica;
        
        if (!(base.hasGrau(grau) && base.hasTipusMateria(tipus) && base.hasTipusHoresPractica(tipusHoresPractica)))
            esValida = false;
        
        if (nom.isEmpty() || codi.isEmpty())
            esValida = false;
        
        if (!(quadrimestre > 0 && quadrimestre <= 8))
            esValida = false;
        
    }
    
    public String getNom() {
        return nom;
    }
    
    public String getCodi() {
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