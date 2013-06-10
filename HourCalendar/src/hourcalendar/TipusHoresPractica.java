/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hourcalendar;

public class TipusHoresPractica {
    public int id;
    public String nom;
    
    public TipusHoresPractica(int _id, String _nom) {
        id = _id;
        nom = _nom;
    }
    
    public int getId() {
        return id;
    }
       
    public String getNom() {
        return nom;
    }
}