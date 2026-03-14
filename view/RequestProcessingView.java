package view;

import controller.MainController;
import model.User;

import javax.swing.*;
import java.awt.*;

public class LoginView extends JFrame {

    private MainController controller;

    // Champs du formulaire
    private JTextField txtEmail;
    private JPasswordField txtMotDePasse;
    private JLabel lblMessage;

    public LoginView(MainController controller) {
        this.controller = controller;

        setTitle("Connexion — Réservation de Salles");
        setSize(420, 520);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Icone
        try {
            setIconImage(
                new ImageIcon("icon/accueil.png").getImage());
        } catch (Exception e) {
            System.out.println("Icone non trouvee.");
        }

        // Panel principal
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setBorder(
            BorderFactory.createEmptyBorder(30, 40, 20, 40));

        // ── Logo / Titre ──────────────────────────────────
        JLabel lblTitre = new JLabel("Bienvenue !");
        lblTitre.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitre.setForeground(new Color(41, 128, 185));
        lblTitre.setAlignmentX(CENTER_ALIGNMENT);
        panel.add(lblTitre);
        panel.add(Box.createVerticalStrut(5));

        JLabel lblSousTitre = new JLabel(
            "Connectez-vous pour continuer");
        lblSousTitre.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblSousTitre.setForeground(Color.GRAY);
        lblSousTitre.setAlignmentX(CENTER_ALIGNMENT);
        panel.add(lblSousTitre);
        panel.add(Box.createVerticalStrut(30));

        // ── Separateur ────────────────────────────────────
        JSeparator sep = new JSeparator();
        sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        sep.setForeground(new Color(220, 220, 220));
        panel.add(sep);
        panel.add(Box.createVerticalStrut(25));

        // ── Champ Email ───────────────────────────────────
        JLabel lblEmail = new JLabel("Adresse email");
        lblEmail.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblEmail.setForeground(new Color(52, 73, 94));
        lblEmail.setAlignmentX(LEFT_ALIGNMENT);
        panel.add(lblEmail);
        panel.add(Box.createVerticalStrut(5));

        txtEmail = new JTextField();
        txtEmail.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        txtEmail.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtEmail.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(
                new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        txtEmail.setAlignmentX(LEFT_ALIGNMENT);
        panel.add(txtEmail);
        panel.add(Box.createVerticalStrut(15));

        // ── Champ Mot de passe ────────────────────────────
        JLabel lblMdp = new JLabel("Mot de passe");
        lblMdp.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblMdp.setForeground(new Color(52, 73, 94));
        lblMdp.setAlignmentX(LEFT_ALIGNMENT);
        panel.add(lblMdp);
        panel.add(Box.createVerticalStrut(5));

        txtMotDePasse = new JPasswordField();
        txtMotDePasse.setMaximumSize(
            new Dimension(Integer.MAX_VALUE, 38));
        txtMotDePasse.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtMotDePasse.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(
                new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        txtMotDePasse.setAlignmentX(LEFT_ALIGNMENT);
        panel.add(txtMotDePasse);
        panel.add(Box.createVerticalStrut(10));

        // ── Message d'erreur ──────────────────────────────
        lblMessage = new JLabel(" ");
        lblMessage.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        lblMessage.setForeground(Color.RED);
        lblMessage.setAlignmentX(CENTER_ALIGNMENT);
        panel.add(lblMessage);
        panel.add(Box.createVerticalStrut(15));

        // ── Bouton Connexion ──────────────────────────────
        JButton btnConnexion = new JButton("Se connecter");
        btnConnexion.setMaximumSize(
            new Dimension(Integer.MAX_VALUE, 42));
        btnConnexion.setBackground(new Color(41, 128, 185));
        btnConnexion.setForeground(Color.WHITE);
        btnConnexion.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnConnexion.setFocusPainted(false);
        btnConnexion.setBorderPainted(false);
        btnConnexion.setCursor(
            new Cursor(Cursor.HAND_CURSOR));
        btnConnexion.setAlignmentX(LEFT_ALIGNMENT);
        panel.add(btnConnexion);
        panel.add(Box.createVerticalStrut(15));

        // ── Separateur ────────────────────────────────────
        JSeparator sep2 = new JSeparator();
        sep2.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        sep2.setForeground(new Color(220, 220, 220));
        panel.add(sep2);
        panel.add(Box.createVerticalStrut(15));

        // ── Info roles ────────────────────────────────────
        JLabel lblInfo = new JLabel(
            "Roles : etudiant | enseignant | responsable");
        lblInfo.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        lblInfo.setForeground(Color.GRAY);
        lblInfo.setAlignmentX(CENTER_ALIGNMENT);
        panel.add(lblInfo);

        add(panel);

        // ── Action bouton connexion ───────────────────────
        btnConnexion.addActionListener(e -> seConnecter());

        // Connexion aussi avec la touche Entree
        txtMotDePasse.addActionListener(e -> seConnecter());
        txtEmail.addActionListener(e ->
            txtMotDePasse.requestFocus());
    }

    // ── Logique de connexion ──────────────────────────────
    private void seConnecter() {
        String email    = txtEmail.getText().trim();
        String motDePasse = new String(
            txtMotDePasse.getPassword()).trim();

        // Validation des champs
        if (email.isEmpty() || motDePasse.isEmpty()) {
            lblMessage.setText(
                "Veuillez remplir tous les champs.");
            lblMessage.setForeground(Color.RED);
            return;
        }

        // Verification en BD
        User user = controller.authentifier(email, motDePasse);

        if (user != null) {
            // Connexion reussie
            controller.setUtilisateurConnecte(user);
            lblMessage.setForeground(new Color(39, 174, 96));
            lblMessage.setText(
                "Connexion reussie ! Bienvenue "
                + user.getNomComplet());

            // Fermer et ouvrir le menu principal
            dispose();
            ouvrirMenuPrincipal(user);

        } else {
            // Echec connexion
            lblMessage.setForeground(Color.RED);
            lblMessage.setText(
                "Email ou mot de passe incorrect.");
            txtMotDePasse.setText("");
            txtMotDePasse.requestFocus();
        }
    }

    // ── Redirection selon le role ─────────────────────────
    private void ouvrirMenuPrincipal(User user) {
        if (user.isResponsable()) {
            // Responsable → acces complet
            ouvrirNavigationComplete();
        } else {
            // Etudiant / Enseignant → acces limite
            ouvrirNavigationLimitee(user);
        }
    }

    private void ouvrirNavigationComplete() {
        JFrame menu = new JFrame(
            "Menu Principal — Responsable");
        menu.setSize(420, 380);
        menu.setLocationRelativeTo(null);
        menu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(
            new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setBorder(
            BorderFactory.createEmptyBorder(20, 30, 20, 30));

        JLabel lbl = new JLabel(
            "Bienvenue ! Navigation :");
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lbl.setAlignmentX(CENTER_ALIGNMENT);
        panel.add(lbl);
        panel.add(Box.createVerticalStrut(20));

        Dimension btnSize =
            new Dimension(Integer.MAX_VALUE, 38);

        JButton btnReservation =
            bouton("Demande de reservation", btnSize,
                new Color(41, 128, 185));
        JButton btnStatut =
            bouton("Statut des reservations", btnSize,
                new Color(39, 174, 96));
        JButton btnSalles =
            bouton("Gestion des salles", btnSize,
                new Color(142, 68, 173));
        JButton btnTraitement =
            bouton("Traitement des demandes", btnSize,
                new Color(243, 156, 18));
        JButton btnUsers =
            bouton("Gestion des utilisateurs", btnSize,
                new Color(52, 73, 94));
        JButton btnDeconnexion =
            bouton("Se deconnecter", btnSize,
                new Color(192, 57, 43));

        btnReservation.addActionListener(e -> {
            menu.dispose();
            controller.showReservationRequestView();
        });
        btnStatut.addActionListener(e -> {
            menu.dispose();
            controller.showReservationStatusView();
        });
        btnSalles.addActionListener(e -> {
            menu.dispose();
            controller.showRoomManagementView();
        });
        btnTraitement.addActionListener(e -> {
            menu.dispose();
            controller.showRequestProcessingView();
        });
        btnUsers.addActionListener(e -> {
            menu.dispose();
            controller.showUserManagementView();
        });
        btnDeconnexion.addActionListener(e -> {
            menu.dispose();
            controller.setUtilisateurConnecte(null);
            controller.showLoginView();
        });

        panel.add(btnReservation);
        panel.add(Box.createVerticalStrut(8));
        panel.add(btnStatut);
        panel.add(Box.createVerticalStrut(8));
        panel.add(btnSalles);
        panel.add(Box.createVerticalStrut(8));
        panel.add(btnTraitement);
        panel.add(Box.createVerticalStrut(8));
        panel.add(btnUsers);
        panel.add(Box.createVerticalStrut(8));
        panel.add(btnDeconnexion);

        menu.add(panel);
        menu.setVisible(true);
    }

    private void ouvrirNavigationLimitee(User user) {
        JFrame menu = new JFrame(
            "Menu — " + user.getNomComplet());
        menu.setSize(420, 260);
        menu.setLocationRelativeTo(null);
        menu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(
            new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setBorder(
            BorderFactory.createEmptyBorder(20, 30, 20, 30));

        JLabel lbl = new JLabel(
            "Bienvenue " + user.getNomComplet()
            + " (" + user.getRole() + ")");
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lbl.setAlignmentX(CENTER_ALIGNMENT);
        panel.add(lbl);
        panel.add(Box.createVerticalStrut(20));

        Dimension btnSize =
            new Dimension(Integer.MAX_VALUE, 38);

        JButton btnReservation =
            bouton("Demande de reservation", btnSize,
                new Color(41, 128, 185));
        JButton btnStatut =
            bouton("Statut des reservations", btnSize,
                new Color(39, 174, 96));
        JButton btnDeconnexion =
            bouton("Se deconnecter", btnSize,
                new Color(192, 57, 43));

        btnReservation.addActionListener(e -> {
            menu.dispose();
            controller.showReservationRequestView();
        });
        btnStatut.addActionListener(e -> {
            menu.dispose();
            controller.showReservationStatusView();
        });
        btnDeconnexion.addActionListener(e -> {
            menu.dispose();
            controller.setUtilisateurConnecte(null);
            controller.showLoginView();
        });

        panel.add(btnReservation);
        panel.add(Box.createVerticalStrut(8));
        panel.add(btnStatut);
        panel.add(Box.createVerticalStrut(8));
        panel.add(btnDeconnexion);

        menu.add(panel);
        menu.setVisible(true);
    }

    // ── Helper bouton ─────────────────────────────────────
    private JButton bouton(String texte,
                            Dimension taille, Color couleur) {
        JButton b = new JButton(texte);
        b.setMaximumSize(taille);
        b.setBackground(couleur);
        b.setForeground(Color.WHITE);
        b.setFont(new Font("Segoe UI", Font.BOLD, 12));
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        b.setAlignmentX(LEFT_ALIGNMENT);
        return b;
    }
}
