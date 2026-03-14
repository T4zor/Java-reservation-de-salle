package model;

public class Room {

    // ── Attributs — correspondent exactement à la table BD ──
    private int    id;
    private String nom;
    private int    capacite;
    private String equipements;

    // ── Constructeurs ─────────────────────────────────────
    public Room() {}

    public Room(String nom, int capacite, String equipements) {
        this.nom         = nom;
        this.capacite    = capacite;
        this.equipements = equipements;
    }

    // ── Getters & Setters ─────────────────────────────────
    public int getId()        { return id; }
    public void setId(int id) { this.id = id; }

    public String getNom()            { return nom; }
    public void setNom(String nom)    { this.nom = nom; }

    public int getCapacite()              { return capacite; }
    public void setCapacite(int capacite) { this.capacite = capacite; }

    public String getEquipements()                  { return equipements; }
    public void setEquipements(String equipements)  { this.equipements = equipements; }

    // ── Méthodes utilitaires ──────────────────────────────
    @Override
    public String toString() {
        return nom + " (capacité : " + capacite + ")";
    }
}