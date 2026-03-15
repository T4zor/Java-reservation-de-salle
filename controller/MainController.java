package controller;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import model.DatabaseManager;
import model.LogAction;
import model.Reservation;
import model.Room;
import model.User;
import view.LoginView;
import view.MenuEtudiantView;
import view.MenuResponsableView;
import view.RequestProcessingView;
import view.ReservationRequestView;
import view.ReservationStatusView;
import view.RoomManagementView;
import view.UserManagementView;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;

public class MainController {

    // Couleurs pour l'interface
    private static final Color BLEU = new Color(41, 128, 185);
    private static final Color BLEU_FONCE = new Color(31, 97, 141);
    private static final Color VERT = new Color(39, 174, 96);
    private static final Color ROUGE = new Color(192, 57, 43);
    private static final Color ORANGE = new Color(243, 156, 18);
    private static final Color VIOLET = new Color(142, 68, 173);
    private static final Color GRIS = new Color(52, 73, 94);
    private static final Color FOND = new Color(245, 246, 250);

    // ── Session : utilisateur connecte ───────────────────
    private User utilisateurConnecte = null;

    // =========================================================
    // GESTION SESSION
    // =========================================================

    public void sauvegarderSession() {
        try {
            if (utilisateurConnecte != null) {
                java.io.FileWriter fw = new java.io.FileWriter("session.txt");
                fw.write(String.valueOf(utilisateurConnecte.getId()));
                fw.close();
            }
        } catch (Exception e) {
            System.err.println("Erreur sauvegarde session : " + e.getMessage());
        }
    }

    public void chargerSession() {
        try {
            java.io.File file = new java.io.File("session.txt");
            if (file.exists()) {
                java.io.BufferedReader br = new java.io.BufferedReader(new java.io.FileReader(file));
                int userId = Integer.parseInt(br.readLine());
                br.close();
                User user = getUserById(userId);
                if (user != null) {
                    utilisateurConnecte = user;
                    System.out.println("Session restaurée pour : " + user.getNomComplet());
                    // Log de connexion automatique
                    new LogAction(user.getId(), "CONNEXION", "Connexion automatique via session").save();
                }
            }
        } catch (Exception e) {
            System.err.println("Erreur chargement session : " + e.getMessage());
        }
    }

    public void deconnecter() {
        if (utilisateurConnecte != null) {
            // Log de déconnexion
            new LogAction(utilisateurConnecte.getId(), "DECONNEXION", "Déconnexion manuelle").save();
        }
        utilisateurConnecte = null;
        new java.io.File("session.txt").delete();
        showLoginView();
    }

    // =========================================================
    // DEMARRAGE
    // =========================================================

    public void start() {
        // Charger la session précédente
        chargerSession();

        // Créer un utilisateur par défaut si aucun n'existe
        creerUtilisateurParDefaut();

        if (utilisateurConnecte != null) {
            showMenuPrincipal();
        } else {
            showLoginView();
        }
    }

    private void creerUtilisateurParDefaut() {
        try {
            // Vérifier si l'utilisateur admin correct existe
            User adminExistant = User.findByEmail("admin@exemple.com");
            if (adminExistant == null) {
                // Créer l'utilisateur admin par défaut
                User admin = new User("Admin", "System", "admin@exemple.com", "responsable", "admin123");
                admin.save();
                System.out.println("Utilisateur par défaut créé : admin@exemple.com / admin123");
            }

            // Créer des salles par défaut si aucune n'existe
            creerSallesParDefaut();
        } catch (SQLException e) {
            System.out.println("Erreur lors de la création de l'utilisateur par défaut : " + e.getMessage());
        }
    }

    private void creerSallesParDefaut() {
        try {
            // Vérifier si des salles existent déjà
            java.util.List<model.Room> sallesExistantes = model.Room.findAll();
            if (sallesExistantes.isEmpty()) {
                // Créer quelques salles par défaut
                model.Room salle1 = new model.Room("Salle A101", 30, "Projecteur, Tableau blanc");
                salle1.save();

                model.Room salle2 = new model.Room("Salle B202", 50, "Projecteur, Sonorisation, Tableau interactif");
                salle2.save();

                model.Room salle3 = new model.Room("Salle C303", 20, "Tableau blanc, Ordinateurs");
                salle3.save();

                model.Room salle4 = new model.Room("Amphithéâtre", 100, "Projecteur, Sonorisation, Microphones");
                salle4.save();

                System.out.println("Salles par défaut créées");
            }
        } catch (Exception e) {
            System.out.println("Erreur lors de la création des salles par défaut : " + e.getMessage());
        }
    }

    // =========================================================
    // NAVIGATION
    // =========================================================

    public void showLoginView() {
        new LoginView(this).setVisible(true);
    }

    public void showUserManagementView() {
        new UserManagementView(this)
            .setVisible(true);
    }

    public void showReservationRequestView() {
        new ReservationRequestView(this)
            .setVisible(true);
    }

    public void showReservationStatusView() {
        new ReservationStatusView(this)
            .setVisible(true);
    }

    public void showRoomManagementView() {
        new RoomManagementView(this)
            .setVisible(true);
    }

    public void showRequestProcessingView() {
        new RequestProcessingView(this)
            .setVisible(true);
    }

    public void showMenuResponsableView() {
        new MenuResponsableView(this).setVisible(true);
    }

    public void showMenuEtudiantView() {
        new MenuEtudiantView(this).setVisible(true);
    }

    public void showMenuPrincipal() {
        // Si pas d'utilisateur connecté, essayer de charger la session
        if (utilisateurConnecte == null) {
            chargerSession();
        }

        if (utilisateurConnecte == null) {
            showLoginView();
            return;
        }

        if (utilisateurConnecte.isResponsable()) {
            ouvrirMenuResponsable(utilisateurConnecte);
        } else {
            ouvrirMenuEtudiant(utilisateurConnecte);
        }
    }

    private void ouvrirMenuResponsable(User user) {
        JFrame menu = creerFenetreMenu("Menu Responsable — " + user.getNomComplet(), 420, 430);
        JPanel panel = creerPanelMenu(user, "Responsable", ORANGE);
        ajouterBouton(panel, "Demande de réservation", BLEU, e -> {
            menu.dispose();
            showReservationRequestView();
        });
        ajouterBouton(panel, "Statut des réservations", VERT, e -> {
            menu.dispose();
            showReservationStatusView();
        });
        ajouterBouton(panel, "Gestion des salles", VIOLET, e -> {
            menu.dispose();
            showRoomManagementView();
        });
        ajouterBouton(panel, "Traitement des demandes", ORANGE, e -> {
            menu.dispose();
            showRequestProcessingView();
        });
        ajouterBouton(panel, "Gestion des utilisateurs", GRIS, e -> {
            menu.dispose();
            showUserManagementView();
        });
        ajouterBouton(panel, "Se déconnecter", ROUGE, e -> {
            menu.dispose();
            setUtilisateurConnecte(null);
            showLoginView();
        });
        menu.add(panel);
        menu.setVisible(true);
    }

    private void ouvrirMenuEtudiant(User user) {
        JFrame menu = creerFenetreMenu("Menu Etudiant — " + user.getNomComplet(), 420, 290);
        JPanel panel = creerPanelMenu(user, "Etudiant", BLEU);
        ajouterBouton(panel, "Demande de réservation", BLEU, e -> {
            menu.dispose();
            showReservationRequestView();
        });
        ajouterBouton(panel, "Statut de mes réservations", VERT, e -> {
            menu.dispose();
            showReservationStatusView();
        });
        ajouterBouton(panel, "Se déconnecter", ROUGE, e -> {
            menu.dispose();
            setUtilisateurConnecte(null);
            showLoginView();
        });
        menu.add(panel);
        menu.setVisible(true);
    }

    // =========================================================
    // SESSION
    // =========================================================

    public User getUtilisateurConnecte() {
        return utilisateurConnecte;
    }

    public void setUtilisateurConnecte(User u) {
        this.utilisateurConnecte = u;
        sauvegarderSession();
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
            boolean success = ps.executeUpdate() > 0;
            if (success && utilisateurConnecte != null) {
                new LogAction(utilisateurConnecte.getId(), "AJOUT_UTILISATEUR",
                    "Ajout de l'utilisateur " + u.getNomComplet() + " (" + u.getEmail() + ")").save();
            }
            return success;
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
        String sql = "DELETE FROM User "
                   + "WHERE id = ?";
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
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println(
                "Erreur emailExiste : "
                + e.getMessage());
        }
        return false;
    }

    public User getUserById(int id) {
        String sql = "SELECT * FROM User WHERE id = ?";
        try {
            PreparedStatement ps = DatabaseManager.getConnection().prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapUser(rs);
            }
        } catch (SQLException e) {
            System.err.println("Erreur getUserById : " + e.getMessage());
        }
        return null;
    }

    public String getMotDePasseUser(int id) {
        String sql = "SELECT mot_de_passe "
                   + "FROM User WHERE id = ?";
        try {
            PreparedStatement ps =
                DatabaseManager.getConnection()
                               .prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString(
                    "mot_de_passe");
            }
        } catch (SQLException e) {
            System.err.println(
                "Erreur getMotDePasseUser : "
                + e.getMessage());
        }
        return "";
    }

    private User mapUser(ResultSet rs)
            throws SQLException {
        User u = new User();
        u.setId(rs.getInt("id"));
        u.setNom(rs.getString("nom"));
        u.setPrenom(rs.getString("prenom"));
        u.setEmail(rs.getString("email"));
        u.setRole(rs.getString("role"));
        u.setMotDePasse(
            rs.getString("mot_de_passe"));
        return u;
    }

    // =========================================================
    // HELPERS INTERFACE
    // =========================================================

    private JFrame creerFenetreMenu(String titre, int w, int h) {
        JFrame menu = new JFrame(titre);
        menu.setSize(w, h);
        menu.setLocationRelativeTo(null);
        menu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        return menu;
    }

    private JPanel creerPanelMenu(User user, String role, Color couleurRole) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(20, 30, 20, 30));
        JLabel lblNom = new JLabel("Bienvenue, " + user.getNomComplet());
        lblNom.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblNom.setForeground(new Color(52, 73, 94));
        lblNom.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel lblRole = new JLabel(role);
        lblRole.setFont(new Font("Segoe UI", Font.BOLD, 11));
        lblRole.setForeground(couleurRole);
        lblRole.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(lblNom);
        panel.add(Box.createVerticalStrut(4));
        panel.add(lblRole);
        panel.add(Box.createVerticalStrut(18));
        JSeparator sep = new JSeparator();
        sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        sep.setForeground(new Color(220, 220, 220));
        panel.add(sep);
        panel.add(Box.createVerticalStrut(15));
        return panel;
    }

    private void ajouterBouton(JPanel panel, String texte, Color couleur, ActionListener action) {
        JButton b = new JButton(texte);
        b.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        b.setBackground(couleur);
        b.setForeground(Color.WHITE);
        b.setFont(new Font("Segoe UI", Font.BOLD, 12));
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        b.setAlignmentX(Component.LEFT_ALIGNMENT);
        b.addActionListener(action);
        panel.add(b);
        panel.add(Box.createVerticalStrut(8));
    }
}