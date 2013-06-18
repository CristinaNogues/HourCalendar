/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hourcalendar;

/**
 *
 * @author Daniel
 */
public class Aula implements java.io.Serializable {
    
    public int id;
    public String nom;
    public int capacitat;
    public TipusAula tipus;
    
    
    public Aula(int _id, String _nom, int _capacitat, TipusAula _tipus){
        
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
