/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hourcalendar;

import java.util.Vector;

/**
 *
 * @author asus
 */
public class Base {
    private Vector<Assignatura> assignatures;
    private Vector<Grau> graus;
    public Vector<TipusHoresPractica> tipusHoresPractica;
    public Vector<TipusMateria> tipusMateria;
    
    public int getNumAssignatures() {
        return assignatures.size();
    }
    
    public Assignatura getAssignatura(int idAssignatura) {
        return assignatures.get(idAssignatura);
    }
    
    public void addAssignatura() {
        
    }
    
    public int getNumGraus() {
        return graus.size();
    }
    
    public Grau getGrau(int idGrau) {
        return graus.get(idGrau);
    }
    
    public void addGrau(String nom, String codi) {
        Grau grau = new Grau(nom, codi);
        graus.add(grau);
    }
}
