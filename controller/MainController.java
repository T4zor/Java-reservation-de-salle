package controller;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import model.DatabaseManager;
import model.User;
import view.LoginView;
import view.RequestProcessingView;
import view.ReservationRequestView;
import view.ReservationStatusView;
import view.RoomManagementView;
import view.UserManagementView;

public class MainController {

    // ── Session : utilisateur connecte ───────────────────
    private User utilisateurConnecte = null;

    // =========================================================
    // DEMARRAGE
    // =========================================================

    public void start() {
        showLoginView();
    }

    // =========================================================
    // NAVIGATION
    // =========================================================

    public void showLoginView() {
        new LoginView(this).setVisible(true);
    }

    public void showUserManagementView() {
        new UserManagementView(this).setVisible(true);
    }

    public void showReservationRequestView() {
        new ReservationRequestView(this).setVisible(true);
    }

    public void showReservationStatusView() {
        new ReservationStatusView(this).setVisible(true);
    }

    public void showRoomManagementView() {
        new RoomManagementView(this).setVisible(true);
    }

    public void showRequestProcessingView() {
        new RequestProcessingView(this).setVisible(true);
    }

    // =========================================================
    // SESSION
    // =========================================================

    public User getUtilisateurConnecte() {
        return utilisateurConnecte;
    }

    public void setUtilisateurConnecte(User u) {
        this.utilisateurConnecte = u;
    }

    public boolean isResponsable() {
        return utilisateurConnecte != null
            && utilisateurConnecte.isResponsable();
    }

    // =========================================================
    // GESTION DES UTILISATEURS
    // =========================================================

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM User "
                   + "ORDER BY nom, prenom";
        try {
            Statement st =
                DatabaseManager.getConnection()
                               .createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                users.add(mapUser(rs));
            }
        } catch (SQLException e) {
            System.err.println(
                "Erreur getAllUsers : "
                + e.getMessage());
        }
        return users;
    }

    public boolean ajouterUser(User u) {
        String sql = "INSERT INTO User "
                   + "(nom, prenom, email, "
                   + "role, mot_de_passe) "
                   + "VALUES (?, ?, ?, ?, ?)";
        try {
            PreparedStatement ps =
                DatabaseManager.getConnection()
                               .prepareStatement(sql);
            ps.setString(1, u.getNom());
            ps.setString(2, u.getPrenom());
            ps.setString(3, u.getEmail());
            ps.setString(4, u.getRole());
            ps.setString(5, u.getMotDePasse());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println(
                "Erreur ajouterUser : "
                + e.getMessage());
            return false;
        }
    }

    public boolean modifierUser(User u) {
        String sql = "UPDATE User "
                   + "SET nom=?, prenom=?, "
                   + "email=?, role=?, "
                   + "mot_de_passe=? "
                   + "WHERE id=?";
        try {
            PreparedStatement ps =
                DatabaseManager.getConnection()
                               .prepareStatement(sql);
            ps.setString(1, u.getNom());
            ps.setString(2, u.getPrenom());
            ps.setString(3, u.getEmail());
            ps.setString(4, u.getRole());
            ps.setString(5, u.getMotDePasse());
            ps.setInt(6, u.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println(
                "Erreur modifierUser : "
                + e.getMessage());
            return false;
        }
    }

    public boolean supprimerUser(int id) {
        String sql = "DELETE FROM User WHERE id = ?";
        try {
            PreparedStatement ps =
                DatabaseManager.getConnection()
                               .prepareStatement(sql);
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println(
                "Erreur supprimerUser : "
                + e.getMessage());
            return false;
        }
    }

    public User authentifier(String email,
                              String motDePasse) {
        String sql = "SELECT * FROM User "
                   + "WHERE email = ? "
                   + "AND mot_de_passe = ?";
        try {
            PreparedStatement ps =
                DatabaseManager.getConnection()
                               .prepareStatement(sql);
            ps.setString(1, email.trim());
            ps.setString(2, motDePasse.trim());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapUser(rs);
            }
        } catch (SQLException e) {
            System.err.println(
                "Erreur authentifier : "
                + e.getMessage());
        }
        return null;
    }

    public boolean emailExiste(String email) {
        String sql = "SELECT COUNT(*) FROM User "
                   + "WHERE email = ?";
        try {
            PreparedStatement ps =
                DatabaseManager.getConnection()
                               .prepareStatement(sql);
            ps.setString(1, email.trim());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1) > 0;
        } catch (SQLException e) {
            System.err.println(
                "Erreur emailExiste : "
                + e.getMessage());
        }
        return false;
    }

    // =========================================================
    // HELPER PRIVE
    // =========================================================

    private User mapUser(ResultSet rs)
            throws SQLException {
        User u = new User();
        u.setId(rs.getInt("id"));
        u.setNom(rs.getString("nom"));
        u.setPrenom(rs.getString("prenom"));
        u.setEmail(rs.getString("email"));
        u.setRole(rs.getString("role"));
        u.setMotDePasse(rs.getString("mot_de_passe"));
        return u;
    }
}