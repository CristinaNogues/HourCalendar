package hourcalendar;

public class Aula implements java.io.Serializable {
    
    public int id;
    public String nom;
    public int capacitat;
    //public TipusAula tipus;
    public int idTipusAula;
    
    
    public Aula(int _id, String _nom, int _capacitat, TipusAula _tipus){
        id = _id;
        nom = _nom;
        capacitat = _capacitat;
        idTipusAula = _tipus.getID();
    }
    
    public Aula(Aula other) {
        id = other.id;
        nom = other.nom;
        capacitat = other.capacitat;
        idTipusAula = other.idTipusAula;
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
        Base base = HourCalendar.getBase();
        return base.getTipusAula(idTipusAula);
    }
}
