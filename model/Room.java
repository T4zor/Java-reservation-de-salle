package model;

import java.util.List;
import java.util.ArrayList;

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

    // ── Méthodes CRUD ─────────────────────────────────────
    public boolean save() {
        // Vérifier l'unicité du nom
        if (nomExiste(nom)) {
            return false; // Nom déjà utilisé
        }

        String sql = "INSERT INTO Salle (nom, capacite, equipements) VALUES (?, ?, ?)";
        try {
            var ps = DatabaseManager.getConnection().prepareStatement(sql);
            ps.setString(1, nom);
            ps.setInt(2, capacite);
            ps.setString(3, equipements);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            System.err.println("Erreur save Room : " + e.getMessage());
            return false;
        }
    }

    public boolean update() {
        // Vérifier l'unicité du nom
        if (nomExiste(nom)) {
            return false; // Nom déjà utilisé
        }

        String sql = "UPDATE Salle SET nom = ?, capacite = ?, equipements = ? WHERE id = ?";
        try {
            var ps = DatabaseManager.getConnection().prepareStatement(sql);
            ps.setString(1, nom);
            ps.setInt(2, capacite);
            ps.setString(3, equipements);
            ps.setInt(4, id);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            System.err.println("Erreur update Room : " + e.getMessage());
            return false;
        }
    }

    public boolean delete() {
        String sql = "DELETE FROM Salle WHERE id = ?";
        try {
            var ps = DatabaseManager.getConnection().prepareStatement(sql);
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            System.err.println("Erreur delete Room : " + e.getMessage());
            return false;
        }
    }

    // ── Méthode utilitaire pour vérifier l'unicité du nom ──
    private boolean nomExiste(String nomSalle) {
        String sql = "SELECT COUNT(*) FROM Salle WHERE nom = ? AND id != ?";
        try {
            var ps = DatabaseManager.getConnection().prepareStatement(sql);
            ps.setString(1, nomSalle);
            ps.setInt(2, this.id); // Exclure cette salle si c'est une mise à jour
            var rs = ps.executeQuery();
            return rs.next() && rs.getInt(1) > 0;
        } catch (Exception e) {
            System.err.println("Erreur vérification nom salle : " + e.getMessage());
            return false;
        }
    }

    public static java.util.List<Room> findAll() {
        java.util.List<Room> rooms = new java.util.ArrayList<>();
        String sql = "SELECT * FROM Salle ORDER BY nom";
        try {
            var st = DatabaseManager.getConnection().createStatement();
            var rs = st.executeQuery(sql);
            while (rs.next()) {
                Room r = new Room();
                r.setId(rs.getInt("id"));
                r.setNom(rs.getString("nom"));
                r.setCapacite(rs.getInt("capacite"));
                r.setEquipements(rs.getString("equipements"));
                rooms.add(r);
            }
        } catch (Exception e) {
            System.err.println("Erreur findAll Room : " + e.getMessage());
        }
        return rooms;
    }

    public static Room findById(int id) {
        String sql = "SELECT * FROM Salle WHERE id = ?";
        try {
            var ps = DatabaseManager.getConnection().prepareStatement(sql);
            ps.setInt(1, id);
            var rs = ps.executeQuery();
            if (rs.next()) {
                Room r = new Room();
                r.setId(rs.getInt("id"));
                r.setNom(rs.getString("nom"));
                r.setCapacite(rs.getInt("capacite"));
                r.setEquipements(rs.getString("equipements"));
                return r;
            }
        } catch (Exception e) {
            System.err.println("Erreur findById Room : " + e.getMessage());
        }
        return null;
    }
}