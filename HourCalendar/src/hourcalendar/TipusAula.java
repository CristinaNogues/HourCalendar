package hourcalendar;

public class TipusAula implements java.io.Serializable {
    
    public int id;
    public String nom;
    
    public TipusAula(int _id, String _nom) {
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
