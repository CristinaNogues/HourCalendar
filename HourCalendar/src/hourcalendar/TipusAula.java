/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hourcalendar;

/**
 *
 * @author Daniel
 */
public class TipusAula {
    
    private Base base;
    public int id;
    public String nom;
    
    public TipusAula(Base _base, int _id, String _nom) {
        
        base = _base;
        id = _id;
        nom = _nom;
    }
    
    
    public int getID(){
        return id;
    }
    
    
    public String getNom(){
        return nom;
    }
    
}
