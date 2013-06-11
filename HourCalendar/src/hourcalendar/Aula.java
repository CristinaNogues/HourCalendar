/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hourcalendar;

/**
 *
 * @author Daniel
 */
public class Aula {
    
    private Base base;
    public int id;
    public String nom;
    public int capacitat;
    public TipusAula tipus;
    
    
    public Aula(Base _base, int _id, String _nom, int _capacitat, TipusAula _tipus){
        
        base = _base;
        id = _id;
        nom = _nom;
        capacitat = _capacitat;
        tipus = _tipus;
    }
    
    
    public int getID(){
        return id;
    }
    
    public String getNom(){
        return nom;
    }
    
    public int getCapacitat(){
        return capacitat;
    }
    
    public TipusAula getTipusAula(){
        return tipus;
    }
}
