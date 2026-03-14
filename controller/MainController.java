package controller;

import database.DBConnection;
import model.User;
import view.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MainController {

    // ── Session : utilisateur connecté ────────────────────
    private User utilisateurConnecte = null;

    // =========================================================
    // DÉMARRAGE ET NAVIGATION
    // =========================================================

    public void start() {
        LoginView loginView = new LoginView(this);
        loginView.setVisible(true);
    }

    public void showLoginView() {
        LoginView loginView = new LoginView(this);
        loginView.setVisible(true);
    }

    public void showReservationRequestView() {
        ReservationRequestView view = new ReservationRequestView();
        view.setVisible(true);
    }

    public void showReservationStatusView() {
        ReservationStatusView view = new ReservationStatusView();
        view.setVisible(true);
    }

    public void showRoomManagementView() {
        RoomManagementView view = new RoomManagementView();
        view.setVisible(true);
    }

    public void showRequestProcessingView() {
        RequestProcessingView view = new RequestProcessingView();
        view.setVisible(true);
    }

    public void showUserManagementView() {
        UserManagementView view = new UserManagementView(this);
        view.setVisible(true);
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

    // =========================================================
    // GESTION DES UTILISATEURS — table `utilisateurs`
    // =========================================================

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM utilisateurs ORDER BY nom, prenom";
        try {
            Statement st = DBConnection.getConnection().createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                users.add(mapUser(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    public boolean ajouterUser(User u) {
        String sql = "INSERT INTO utilisateurs " +
                     "(nom, prenom, email, telephone, role) " +
                     "VALUES (?, ?, ?, ?, ?)";
        try {
            PreparedStatement ps =
                DBConnection.getConnection().prepareStatement(sql);
            ps.setString(1, u.getNom());
            ps.setString(2, u.getPrenom());
            ps.setString(3, u.getEmail());
            ps.setString(4, u.getTelephone());
            ps.setString(5, u.getRole());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean modifierUser(User u) {
        String sql = "UPDATE utilisateurs " +
                     "SET nom=?, prenom=?, email=?, telephone=?, role=? " +
                     "WHERE id=?";
        try {
            PreparedStatement ps =
                DBConnection.getConnection().prepareStatement(sql);
            ps.setString(1, u.getNom());
            ps.setString(2, u.getPrenom());
            ps.setString(3, u.getEmail());
            ps.setString(4, u.getTelephone());
            ps.setString(5, u.getRole());
            ps.setInt(6, u.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean supprimerUser(int id) {
        String sql = "DELETE FROM utilisateurs WHERE id = ?";
        try {
            PreparedStatement ps =
                DBConnection.getConnection().prepareStatement(sql);
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public User authentifier(String email) {
        String sql = "SELECT * FROM utilisateurs WHERE email = ?";
        try {
            PreparedStatement ps =
                DBConnection.getConnection().prepareStatement(sql);
            ps.setString(1, email.trim());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapUser(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean emailExiste(String email) {
        String sql = "SELECT COUNT(*) FROM utilisateurs WHERE email = ?";
        try {
            PreparedStatement ps =
                DBConnection.getConnection().prepareStatement(sql);
            ps.setString(1, email.trim());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1) > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // =========================================================
    // HELPER PRIVÉ
    // =========================================================

    private User mapUser(ResultSet rs) throws SQLException {
        User u = new User();
        u.setId(rs.getInt("id"));
        u.setNom(rs.getString("nom"));
        u.setPrenom(rs.getString("prenom"));
        u.setEmail(rs.getString("email"));
        u.setTelephone(rs.getString("telephone"));
        u.setRole(rs.getString("role"));
        return u;
    }
}