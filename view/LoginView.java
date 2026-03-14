package view;

import controller.MainController;
import model.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.nio.charset.StandardCharsets;

public class LoginView extends JFrame {

    private MainController controller;
    private JTextField txtEmail;
    private JPasswordField txtMotDePasse;
    private JLabel lblMessage;

    // =========================================================
    // CONSTRUCTEUR
    // =========================================================

    public LoginView(MainController controller) {
        this.controller = controller;

        setTitle("Connexion \u2014 R\u00e9servation de Salles");
        setSize(420, 520);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        try {
            setIconImage(
                new ImageIcon("icon/accueil.png")
                    .getImage());
        } catch (Exception e) {
            System.out.println("Icone non trouvee.");
        }

        add(construireFormulaire());
    }

    // =========================================================
    // CONSTRUCTION FORMULAIRE
    // =========================================================

    private JPanel construireFormulaire() {
        JPanel panel = new JPanel();
        panel.setLayout(
            new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setBorder(
            BorderFactory.createEmptyBorder(
                30, 40, 20, 40));

        // ── Titre ─────────────────────────────────────────
        JLabel lblTitre = new JLabel("Bienvenue !");
        lblTitre.setFont(
            new Font("Segoe UI", Font.BOLD, 24));
        lblTitre.setForeground(
            new Color(41, 128, 185));
        lblTitre.setAlignmentX(CENTER_ALIGNMENT);

        JLabel lblSousTitre = new JLabel(
            "Connectez-vous pour continuer");
        lblSousTitre.setFont(
            new Font("Segoe UI", Font.PLAIN, 13));
        lblSousTitre.setForeground(Color.GRAY);
        lblSousTitre.setAlignmentX(CENTER_ALIGNMENT);

        panel.add(lblTitre);
        panel.add(Box.createVerticalStrut(5));
        panel.add(lblSousTitre);
        panel.add(Box.createVerticalStrut(25));
        panel.add(creerSeparateur());
        panel.add(Box.createVerticalStrut(20));

        // ── Email ─────────────────────────────────────────
        panel.add(creerLabel("Adresse email"));
        panel.add(Box.createVerticalStrut(5));
        txtEmail = new JTextField();
        styliserChamp(txtEmail);
        panel.add(txtEmail);
        panel.add(Box.createVerticalStrut(15));

        // ── Mot de passe ──────────────────────────────────
        panel.add(creerLabel("Mot de passe"));
        panel.add(Box.createVerticalStrut(5));
        txtMotDePasse = new JPasswordField();
        styliserChamp(txtMotDePasse);
        panel.add(txtMotDePasse);
        panel.add(Box.createVerticalStrut(12));

        // ── Message erreur / succes ───────────────────────
        lblMessage = new JLabel(" ");
        lblMessage.setFont(
            new Font("Segoe UI", Font.ITALIC, 12));
        lblMessage.setForeground(Color.RED);
        lblMessage.setAlignmentX(CENTER_ALIGNMENT);
        panel.add(lblMessage);
        panel.add(Box.createVerticalStrut(12));

        // ── Bouton connexion ──────────────────────────────
        JButton btnConnexion = new JButton(
            "Se connecter");
        btnConnexion.setMaximumSize(
            new Dimension(Integer.MAX_VALUE, 42));
        btnConnexion.setBackground(
            new Color(41, 128, 185));
        btnConnexion.setForeground(Color.WHITE);
        btnConnexion.setFont(
            new Font("Segoe UI", Font.BOLD, 14));
        btnConnexion.setFocusPainted(false);
        btnConnexion.setBorderPainted(false);
        btnConnexion.setCursor(
            new Cursor(Cursor.HAND_CURSOR));
        btnConnexion.setAlignmentX(LEFT_ALIGNMENT);
        panel.add(btnConnexion);
        panel.add(Box.createVerticalStrut(15));
        panel.add(creerSeparateur());
        panel.add(Box.createVerticalStrut(12));

        // ── Info roles ────────────────────────────────────
        JLabel lblInfo = new JLabel(
            "Roles : etudiant | enseignant "
            + "| responsable");
        lblInfo.setFont(
            new Font("Segoe UI", Font.ITALIC, 11));
        lblInfo.setForeground(Color.GRAY);
        lblInfo.setAlignmentX(CENTER_ALIGNMENT);
        panel.add(lblInfo);

        // ── Actions ───────────────────────────────────────
        btnConnexion.addActionListener(
            e -> seConnecter());
        txtMotDePasse.addActionListener(
            e -> seConnecter());
        txtEmail.addActionListener(
            e -> txtMotDePasse.requestFocus());

        return panel;
    }

    // =========================================================
    // LOGIQUE CONNEXION
    // =========================================================

    private void seConnecter() {
        String email = txtEmail.getText().trim();
        String mdp   = new String(
            txtMotDePasse.getPassword()).trim();

        if (email.isEmpty() || mdp.isEmpty()) {
            afficherErreur(
                "Veuillez remplir tous les champs.");
            return;
        }

        User user = controller.authentifier(
            email, mdp);

        if (user != null) {
            controller.setUtilisateurConnecte(user);
            afficherSucces(
                "Connexion reussie ! Bienvenue "
                + user.getNomComplet());
            dispose();
            ouvrirMenuPrincipal(user);
        } else {
            afficherErreur(
                "Email ou mot de passe incorrect.");
            txtMotDePasse.setText("");
            txtMotDePasse.requestFocus();
        }
    }

    // =========================================================
    // MENUS SELON LE ROLE
    // =========================================================

    private void ouvrirMenuPrincipal(User user) {
        if (user.isResponsable()) {
            ouvrirMenuResponsable(user);
        } else {
            ouvrirMenuLimite(user);
        }
    }

    private void ouvrirMenuResponsable(User user) {
        JFrame menu = new JFrame(
            "Menu \u2014 "
            + user.getNomComplet()
            + " (Responsable)");
        menu.setSize(420, 420);
        menu.setLocationRelativeTo(null);
        menu.setDefaultCloseOperation(
            JFrame.EXIT_ON_CLOSE);

        JPanel panel = creerPanelMenu(
            "Bienvenue "
            + user.getNomComplet() + " !");

        ajouterBouton(panel,
            "Demande de r\u00e9servation",
            new Color(41, 128, 185), e -> {
                menu.dispose();
                controller
                    .showReservationRequestView();
            });
        ajouterBouton(panel,
            "Statut des r\u00e9servations",
            new Color(39, 174, 96), e -> {
                menu.dispose();
                controller
                    .showReservationStatusView();
            });
        ajouterBouton(panel,
            "Gestion des salles",
            new Color(142, 68, 173), e -> {
                menu.dispose();
                controller.showRoomManagementView();
            });
        ajouterBouton(panel,
            "Traitement des demandes",
            new Color(243, 156, 18), e -> {
                menu.dispose();
                controller
                    .showRequestProcessingView();
            });
        ajouterBouton(panel,
            "Gestion des utilisateurs",
            new Color(52, 73, 94), e -> {
                menu.dispose();
                controller
                    .showUserManagementView();
            });
        ajouterBouton(panel,
            "Se d\u00e9connecter",
            new Color(192, 57, 43), e -> {
                menu.dispose();
                controller
                    .setUtilisateurConnecte(null);
                controller.showLoginView();
            });

        menu.add(panel);
        menu.setVisible(true);
    }

    private void ouvrirMenuLimite(User user) {
        JFrame menu = new JFrame(
            "Menu \u2014 "
            + user.getNomComplet()
            + " (" + user.getRole() + ")");
        menu.setSize(420, 270);
        menu.setLocationRelativeTo(null);
        menu.setDefaultCloseOperation(
            JFrame.EXIT_ON_CLOSE);

        JPanel panel = creerPanelMenu(
            "Bienvenue "
            + user.getNomComplet()
            + " (" + user.getRole() + ")");

        ajouterBouton(panel,
            "Demande de r\u00e9servation",
            new Color(41, 128, 185), e -> {
                menu.dispose();
                controller
                    .showReservationRequestView();
            });
        ajouterBouton(panel,
            "Statut des r\u00e9servations",
            new Color(39, 174, 96), e -> {
                menu.dispose();
                controller
                    .showReservationStatusView();
            });
        ajouterBouton(panel,
            "Se d\u00e9connecter",
            new Color(192, 57, 43), e -> {
                menu.dispose();
                controller
                    .setUtilisateurConnecte(null);
                controller.showLoginView();
            });

        menu.add(panel);
        menu.setVisible(true);
    }

    // =========================================================
    // HELPERS PRIVES
    // =========================================================

    private JPanel creerPanelMenu(String titreTxt) {
        JPanel panel = new JPanel();
        panel.setLayout(
            new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setBorder(
            BorderFactory.createEmptyBorder(
                20, 30, 20, 30));

        JLabel titre = new JLabel(titreTxt);
        titre.setFont(
            new Font("Segoe UI", Font.BOLD, 14));
        titre.setForeground(
            new Color(41, 128, 185));
        titre.setAlignmentX(CENTER_ALIGNMENT);
        panel.add(titre);
        panel.add(Box.createVerticalStrut(20));
        return panel;
    }

    private void ajouterBouton(JPanel panel,
            String texte, Color couleur,
            ActionListener action) {
        JButton b = new JButton(texte);
        b.setMaximumSize(
            new Dimension(Integer.MAX_VALUE, 38));
        b.setBackground(couleur);
        b.setForeground(Color.WHITE);
        b.setFont(
            new Font("Segoe UI", Font.BOLD, 12));
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        b.setAlignmentX(LEFT_ALIGNMENT);
        b.addActionListener(action);
        panel.add(b);
        panel.add(Box.createVerticalStrut(8));
    }

    private JLabel creerLabel(String texte) {
        JLabel l = new JLabel(texte);
        l.setFont(
            new Font("Segoe UI", Font.BOLD, 12));
        l.setForeground(new Color(52, 73, 94));
        l.setAlignmentX(LEFT_ALIGNMENT);
        return l;
    }

    private void styliserChamp(JTextField champ) {
        champ.setMaximumSize(
            new Dimension(Integer.MAX_VALUE, 38));
        champ.setFont(
            new Font("Segoe UI", Font.PLAIN, 13));
        champ.setBorder(
            BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(
                    new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(
                    5, 10, 5, 10)));
        champ.setAlignmentX(LEFT_ALIGNMENT);
    }

    private JSeparator creerSeparateur() {
        JSeparator sep = new JSeparator();
        sep.setMaximumSize(
            new Dimension(Integer.MAX_VALUE, 1));
        sep.setForeground(new Color(220, 220, 220));
        return sep;
    }

    private void afficherErreur(String msg) {
        lblMessage.setForeground(Color.RED);
        lblMessage.setText(msg);
    }

    private void afficherSucces(String msg) {
        lblMessage.setForeground(
            new Color(39, 174, 96));
        lblMessage.setText(msg);
    }
}