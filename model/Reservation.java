package model;

import java.sql.*;

public class Reservation {
    private int id;
    private int idUser;
    private int idSalle;
    private String dateDebut; // Format YYYY-MM-DD HH:MM
    private String dateFin;
    private String statut; // 'en attente', 'acceptee', 'refusee'

    // Constructeurs
    public Reservation() {}

    public Reservation(int idUser, int idSalle, String dateDebut, String dateFin) {
        this.idUser = idUser;
        this.idSalle = idSalle;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.statut = "en attente";
    }

    // Getters et Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getIdUser() { return idUser; }
    public void setIdUser(int idUser) { this.idUser = idUser; }
    public int getIdSalle() { return idSalle; }
    public void setIdSalle(int idSalle) { this.idSalle = idSalle; }
    public String getDateDebut() { return dateDebut; }
    public void setDateDebut(String dateDebut) { this.dateDebut = dateDebut; }
    public String getDateFin() { return dateFin; }
    public void setDateFin(String dateFin) { this.dateFin = dateFin; }
    public String getStatut() { return statut; }
    public void setStatut(String statut) { this.statut = statut; }

    // Méthodes CRUD
    public void save() throws SQLException {
        String sql = "INSERT INTO Reservation (id_user, id_salle, date_debut, date_fin, statut) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = DatabaseManager.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, idUser);
            stmt.setInt(2, idSalle);
            stmt.setString(3, dateDebut);
            stmt.setString(4, dateFin);
            stmt.setString(5, statut);
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                this.id = rs.getInt(1);
            }
        }
    }

    public static Reservation findById(int id) throws SQLException {
        String sql = "SELECT * FROM Reservation WHERE id = ?";
        try (PreparedStatement stmt = DatabaseManager.getConnection().prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Reservation res = new Reservation();
                res.setId(rs.getInt("id"));
                res.setIdUser(rs.getInt("id_user"));
                res.setIdSalle(rs.getInt("id_salle"));
                res.setDateDebut(rs.getString("date_debut"));
                res.setDateFin(rs.getString("date_fin"));
                res.setStatut(rs.getString("statut"));
                return res;
            }
        }
        return null;
    }

    public void update() throws SQLException {
        String sql = "UPDATE Reservation SET id_user = ?, id_salle = ?, date_debut = ?, date_fin = ?, statut = ? WHERE id = ?";
        try (PreparedStatement stmt = DatabaseManager.getConnection().prepareStatement(sql)) {
            stmt.setInt(1, idUser);
            stmt.setInt(2, idSalle);
            stmt.setString(3, dateDebut);
            stmt.setString(4, dateFin);
            stmt.setString(5, statut);
            stmt.setInt(6, id);
            stmt.executeUpdate();
        }
    }

    public void delete() throws SQLException {
        String sql = "DELETE FROM Reservation WHERE id = ?";
        try (PreparedStatement stmt = DatabaseManager.getConnection().prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
}
