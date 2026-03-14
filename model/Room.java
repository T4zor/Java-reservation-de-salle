package model;

import java.sql.*;

public class Room {
    private int id;
    private String nom;
    private int capacite;
    private String equipements;

    // Constructeurs
    public Room() {}

    public Room(String nom, int capacite, String equipements) {
        this.nom = nom;
        this.capacite = capacite;
        this.equipements = equipements;
    }

    // Getters et Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public int getCapacite() { return capacite; }
    public void setCapacite(int capacite) { this.capacite = capacite; }
    public String getEquipements() { return equipements; }
    public void setEquipements(String equipements) { this.equipements = equipements; }

    // Méthodes CRUD
    public void save() throws SQLException {
        String sql = "INSERT INTO Salle (nom, capacite, equipements) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = DatabaseManager.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, nom);
            stmt.setInt(2, capacite);
            stmt.setString(3, equipements);
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                this.id = rs.getInt(1);
            }
        }
    }

    public static Room findById(int id) throws SQLException {
        String sql = "SELECT * FROM Salle WHERE id = ?";
        try (PreparedStatement stmt = DatabaseManager.getConnection().prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Room room = new Room();
                room.setId(rs.getInt("id"));
                room.setNom(rs.getString("nom"));
                room.setCapacite(rs.getInt("capacite"));
                room.setEquipements(rs.getString("equipements"));
                return room;
            }
        }
        return null;
    }

    public void update() throws SQLException {
        String sql = "UPDATE Salle SET nom = ?, capacite = ?, equipements = ? WHERE id = ?";
        try (PreparedStatement stmt = DatabaseManager.getConnection().prepareStatement(sql)) {
            stmt.setString(1, nom);
            stmt.setInt(2, capacite);
            stmt.setString(3, equipements);
            stmt.setInt(4, id);
            stmt.executeUpdate();
        }
    }

    public void delete() throws SQLException {
        String sql = "DELETE FROM Salle WHERE id = ?";
        try (PreparedStatement stmt = DatabaseManager.getConnection().prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
}
