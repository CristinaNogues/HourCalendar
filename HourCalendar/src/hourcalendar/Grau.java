/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hourcalendar;

import java.awt.Color;

public class Grau implements java.io.Serializable {
    public String nom;
    public String codi;
    public Color color;

    public Grau(String _nom, String _codi) {
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
}