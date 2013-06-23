/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hourcalendar;

import java.awt.Color;

public class Grau implements java.io.Serializable {
    public int id;
    public String nom;
    public String codi;
    public Color color;

    public Grau(int id, String _nom, String _codi) {
        this.id = id;
        nom = _nom;
        codi = _codi;
        color = javax.swing.UIManager.getDefaults().getColor("PasswordField.selectionBackground");
    }
    
    public String getNom() {
        return nom;
    }
       
    public String getCodi() {
        return codi;
    }
    
    public int getID() {
        return id;
    }
    
}