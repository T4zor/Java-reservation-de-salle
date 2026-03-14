package view;

import controller.MainController;
import model.User;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;

public class LoginView extends JFrame {

    private MainController controller;
    private JTextField txtEmail;
    private JPasswordField txtMotDePasse;
    private JLabel lblMessage;

    // Couleurs
    private static final Color BLEU
        = new Color(41, 128, 185);
    private static final Color BLEU_FONCE
        = new Color(31, 97, 141);
    private static final Color VERT
        = new Color(39, 174, 96);
    private static final Color ROUGE
        = new Color(192, 57, 43);
    private static final Color ORANGE
        = new Color(243, 156, 18);
    private static final Color VIOLET
        = new Color(142, 68, 173);
    private static final Color GRIS
        = new Color(52, 73, 94);
    private static final Color FOND
        = new Color(245, 246, 250);

    // =========================================================
    // CONSTRUCTEUR
    // =========================================================

    public LoginView(MainController controller) {
        this.controller = controller;

        setTitle("Connexion \u2014 "
            + "R\u00e9servation de Salles");
        setSize(440, 560);
        setDefaultCloseOperation(
            JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        try {
            setIconImage(
                new ImageIcon("icon/accueil.png")
                    .getImage());
        } catch (Exception e) {
            System.out.println(
                "Icone non trouvee.");
        }

        getContentPane()
            .setBackground(FOND);
        setLayout(new BorderLayout());
        add(construireHeader(),
            BorderLayout.NORTH);
        add(construireFormulaire(),
            BorderLayout.CENTER);
        add(construireFooter(),
            BorderLayout.SOUTH);
    }

    // =========================================================
    // HEADER BLEU
    // =========================================================

    private JPanel construireHeader() {
        JPanel header = new JPanel();
        header.setLayout(
            new BoxLayout(header,
                BoxLayout.Y_AXIS));
        header.setBackground(BLEU);
        header.setBorder(
            new EmptyBorder(25, 30, 20, 30));

        // Icone utilisateur
        JLabel icone = new JLabel() {
            @Override
            protected void paintComponent(
                    Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 =
                    (Graphics2D) g;
                g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints
                        .VALUE_ANTIALIAS_ON);
                g2.setColor(
                    new Color(255,255,255,50));
                g2.fillOval(0, 0, 50, 50);
                g2.setColor(Color.WHITE);
                g2.fillOval(17, 8, 16, 16);
                g2.fillOval(10, 28, 30, 20);
            }
        };
        icone.setPreferredSize(
            new Dimension(50, 50));
        icone.setMaximumSize(
            new Dimension(50, 50));
        icone.setAlignmentX(
            CENTER_ALIGNMENT);

        JLabel lblTitre = new JLabel(
            "Bienvenue !");
        lblTitre.setFont(
            new Font("Segoe UI",
                Font.BOLD, 24));
        lblTitre.setForeground(Color.WHITE);
        lblTitre.setAlignmentX(
            CENTER_ALIGNMENT);

        JLabel lblSous = new JLabel(
            "Connectez-vous pour continuer");
        lblSous.setFont(
            new Font("Segoe UI",
                Font.PLAIN, 13));
        lblSous.setForeground(
            new Color(200, 225, 255));
        lblSous.setAlignmentX(
            CENTER_ALIGNMENT);

        header.add(icone);
        header.add(Box.createVerticalStrut(12));
        header.add(lblTitre);
        header.add(Box.createVerticalStrut(4));
        header.add(lblSous);

        return header;
    }

    // =========================================================
    // FORMULAIRE
    // =========================================================

    private JPanel construireFormulaire() {
        JPanel panel = new JPanel();
        panel.setLayout(
            new BoxLayout(panel,
                BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setBorder(
            new EmptyBorder(25, 35, 20, 35));

        // Email
        panel.add(creerLabel(
            "ADRESSE EMAIL"));
        panel.add(Box.createVerticalStrut(6));
        txtEmail = new JTextField();
        styliserChamp(txtEmail,
            "votre@email.com");
        panel.add(txtEmail);
        panel.add(Box.createVerticalStrut(16));

        // Mot de passe
        panel.add(creerLabel("MOT DE PASSE"));
        panel.add(Box.createVerticalStrut(6));
        txtMotDePasse = new JPasswordField();
        styliserChamp(txtMotDePasse, null);
        panel.add(txtMotDePasse);
        panel.add(Box.createVerticalStrut(12));

        // Message erreur/succes
        lblMessage = new JLabel(" ");
        lblMessage.setFont(
            new Font("Segoe UI",
                Font.ITALIC, 12));
        lblMessage.setForeground(ROUGE);
        lblMessage.setAlignmentX(
            CENTER_ALIGNMENT);
        panel.add(lblMessage);
        panel.add(Box.createVerticalStrut(12));

        // Bouton connexion
        JButton btnConnexion =
            new JButton("Se connecter");
        btnConnexion.setMaximumSize(
            new Dimension(
                Integer.MAX_VALUE, 44));
        btnConnexion.setBackground(BLEU);
        btnConnexion.setForeground(
            Color.WHITE);
        btnConnexion.setFont(
            new Font("Segoe UI",
                Font.BOLD, 14));
        btnConnexion.setFocusPainted(false);
        btnConnexion.setBorderPainted(false);
        btnConnexion.setCursor(
            new Cursor(Cursor.HAND_CURSOR));
        btnConnexion.setAlignmentX(
            LEFT_ALIGNMENT);

        // Hover effect
        btnConnexion.addMouseListener(
            new java.awt.event.MouseAdapter() {
                public void mouseEntered(
                        java.awt.event.MouseEvent e) {
                    btnConnexion
                        .setBackground(
                            BLEU_FONCE);
                }
                public void mouseExited(
                        java.awt.event.MouseEvent e) {
                    btnConnexion
                        .setBackground(BLEU);
                }
            });

        panel.add(btnConnexion);
        panel.add(Box.createVerticalStrut(20));

        // Separateur
        JSeparator sep = new JSeparator();
        sep.setMaximumSize(
            new Dimension(
                Integer.MAX_VALUE, 1));
        sep.setForeground(
            new Color(220, 220, 220));
        panel.add(sep);
        panel.add(Box.createVerticalStrut(15));

        // Roles disponibles
        JLabel lblRoles = new JLabel(
            "Roles disponibles :");
        lblRoles.setFont(
            new Font("Segoe UI",
                Font.BOLD, 11));
        lblRoles.setForeground(
            new Color(100, 100, 100));
        lblRoles.setAlignmentX(
            CENTER_ALIGNMENT);
        panel.add(lblRoles);
        panel.add(Box.createVerticalStrut(10));

        // Badges des roles
        JPanel badges = new JPanel(
            new FlowLayout(
                FlowLayout.CENTER, 6, 0));
        badges.setOpaque(false);
        badges.setMaximumSize(
            new Dimension(
                Integer.MAX_VALUE, 30));
        badges.add(badge(
            "Etudiant",
            new Color(230, 241, 251),
            new Color(12, 68, 124)));
        badges.add(badge(
            "Enseignant",
            new Color(234, 243, 222),
            new Color(39, 80, 10)));
        badges.add(badge(
            "Responsable",
            new Color(250, 238, 218),
            new Color(99, 56, 6)));
        panel.add(badges);

        // Actions
        btnConnexion.addActionListener(
            e -> seConnecter());
        txtMotDePasse.addActionListener(
            e -> seConnecter());
        txtEmail.addActionListener(
            e -> txtMotDePasse
                .requestFocus());

        return panel;
    }

    // =========================================================
    // FOOTER
    // =========================================================

    private JPanel construireFooter() {
        JPanel footer = new JPanel(
            new FlowLayout(
                FlowLayout.CENTER));
        footer.setBackground(FOND);
        footer.setBorder(
            new EmptyBorder(8, 0, 8, 0));

        JLabel lbl = new JLabel(
            "Acces selon votre role"
            + " apres connexion");
        lbl.setFont(
            new Font("Segoe UI",
                Font.ITALIC, 11));
        lbl.setForeground(
            new Color(150, 150, 150));
        footer.add(lbl);
        return footer;
    }

    // =========================================================
    // LOGIQUE CONNEXION
    // =========================================================

    private void seConnecter() {
        String email =
            txtEmail.getText().trim();
        String mdp = new String(
            txtMotDePasse.getPassword())
            .trim();

        if (email.isEmpty()
                || mdp.isEmpty()) {
            afficherErreur(
                "Veuillez remplir "
                + "tous les champs.");
            return;
        }

        User user =
            controller.authentifier(
                email, mdp);

        if (user != null) {
            controller
                .setUtilisateurConnecte(user);
            afficherSucces(
                "Connexion reussie ! "
                + "Bienvenue "
                + user.getNomComplet());
            dispose();
            ouvrirMenuSelonRole(user);
        } else {
            afficherErreur(
                "Email ou mot de passe "
                + "incorrect.");
            txtMotDePasse.setText("");
            txtMotDePasse.requestFocus();
        }
    }

    // =========================================================
    // MENUS SELON LE ROLE
    // =========================================================

    private void ouvrirMenuSelonRole(
            User user) {
        if (user.isResponsable()) {
            ouvrirMenuResponsable(user);
        } else if (user.isEnseignant()) {
            ouvrirMenuEnseignant(user);
        } else {
            ouvrirMenuEtudiant(user);
        }
    }

    private void ouvrirMenuResponsable(
            User user) {
        JFrame menu = creerFenetreMenu(
            "Menu Responsable \u2014 "
            + user.getNomComplet(),
            420, 430);

        JPanel panel = creerPanelMenu(
            user, "Responsable", ORANGE);

        ajouterBouton(panel,
            "Demande de r\u00e9servation",
            BLEU, e -> {
                menu.dispose();
                controller
                    .showReservationRequestView();
            });
        ajouterBouton(panel,
            "Statut des r\u00e9servations",
            VERT, e -> {
                menu.dispose();
                controller
                    .showReservationStatusView();
            });
        ajouterBouton(panel,
            "Gestion des salles",
            VIOLET, e -> {
                menu.dispose();
                controller
                    .showRoomManagementView();
            });
        ajouterBouton(panel,
            "Traitement des demandes",
            ORANGE, e -> {
                menu.dispose();
                controller
                    .showRequestProcessingView();
            });
        ajouterBouton(panel,
            "Gestion des utilisateurs",
            GRIS, e -> {
                menu.dispose();
                controller
                    .showUserManagementView();
            });
        ajouterBouton(panel,
            "Se d\u00e9connecter",
            ROUGE, e -> {
                menu.dispose();
                controller
                    .setUtilisateurConnecte(
                        null);
                controller.showLoginView();
            });

        menu.add(panel);
        menu.setVisible(true);
    }

    private void ouvrirMenuEnseignant(
            User user) {
        JFrame menu = creerFenetreMenu(
            "Menu Enseignant \u2014 "
            + user.getNomComplet(),
            420, 290);

        JPanel panel = creerPanelMenu(
            user, "Enseignant", VERT);

        ajouterBouton(panel,
            "Demande de r\u00e9servation",
            BLEU, e -> {
                menu.dispose();
                controller
                    .showReservationRequestView();
            });
        ajouterBouton(panel,
            "Statut des r\u00e9servations",
            VERT, e -> {
                menu.dispose();
                controller
                    .showReservationStatusView();
            });
        ajouterBouton(panel,
            "Se d\u00e9connecter",
            ROUGE, e -> {
                menu.dispose();
                controller
                    .setUtilisateurConnecte(
                        null);
                controller.showLoginView();
            });

        menu.add(panel);
        menu.setVisible(true);
    }

    private void ouvrirMenuEtudiant(
            User user) {
        JFrame menu = creerFenetreMenu(
            "Menu Etudiant \u2014 "
            + user.getNomComplet(),
            420, 290);

        JPanel panel = creerPanelMenu(
            user, "Etudiant", BLEU);

        ajouterBouton(panel,
            "Demande de r\u00e9servation",
            BLEU, e -> {
                menu.dispose();
                controller
                    .showReservationRequestView();
            });
        ajouterBouton(panel,
            "Statut de mes r\u00e9servations",
            VERT, e -> {
                menu.dispose();
                controller
                    .showReservationStatusView();
            });
        ajouterBouton(panel,
            "Se d\u00e9connecter",
            ROUGE, e -> {
                menu.dispose();
                controller
                    .setUtilisateurConnecte(
                        null);
                controller.showLoginView();
            });

        menu.add(panel);
        menu.setVisible(true);
    }

    // =========================================================
    // HELPERS INTERFACE
    // =========================================================

    private JFrame creerFenetreMenu(
            String titre, int w, int h) {
        JFrame menu = new JFrame(titre);
        menu.setSize(w, h);
        menu.setLocationRelativeTo(null);
        menu.setDefaultCloseOperation(
            JFrame.EXIT_ON_CLOSE);
        return menu;
    }

    private JPanel creerPanelMenu(
            User user, String role,
            Color couleurRole) {
        JPanel panel = new JPanel();
        panel.setLayout(
            new BoxLayout(panel,
                BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setBorder(
            new EmptyBorder(20, 30, 20, 30));

        JLabel lblNom = new JLabel(
            "Bienvenue, "
            + user.getNomComplet());
        lblNom.setFont(
            new Font("Segoe UI",
                Font.BOLD, 15));
        lblNom.setForeground(
            new Color(52, 73, 94));
        lblNom.setAlignmentX(
            CENTER_ALIGNMENT);

        JLabel lblRole = new JLabel(role);
        lblRole.setFont(
            new Font("Segoe UI",
                Font.BOLD, 11));
        lblRole.setForeground(couleurRole);
        lblRole.setAlignmentX(
            CENTER_ALIGNMENT);

        panel.add(lblNom);
        panel.add(Box.createVerticalStrut(4));
        panel.add(lblRole);
        panel.add(Box.createVerticalStrut(18));

        JSeparator sep = new JSeparator();
        sep.setMaximumSize(
            new Dimension(
                Integer.MAX_VALUE, 1));
        sep.setForeground(
            new Color(220, 220, 220));
        panel.add(sep);
        panel.add(Box.createVerticalStrut(15));

        return panel;
    }

    private void ajouterBouton(
            JPanel panel, String texte,
            Color couleur,
            ActionListener action) {
        JButton b = new JButton(texte);
        b.setMaximumSize(
            new Dimension(
                Integer.MAX_VALUE, 38));
        b.setBackground(couleur);
        b.setForeground(Color.WHITE);
        b.setFont(
            new Font("Segoe UI",
                Font.BOLD, 12));
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setCursor(
            new Cursor(Cursor.HAND_CURSOR));
        b.setAlignmentX(LEFT_ALIGNMENT);
        b.addActionListener(action);
        panel.add(b);
        panel.add(
            Box.createVerticalStrut(8));
    }

    private JLabel creerLabel(
            String texte) {
        JLabel l = new JLabel(texte);
        l.setFont(
            new Font("Segoe UI",
                Font.BOLD, 11));
        l.setForeground(
            new Color(100, 100, 100));
        l.setAlignmentX(LEFT_ALIGNMENT);
        return l;
    }

    private void styliserChamp(
            JTextField champ,
            String placeholder) {
        champ.setMaximumSize(
            new Dimension(
                Integer.MAX_VALUE, 40));
        champ.setFont(
            new Font("Segoe UI",
                Font.PLAIN, 13));
        champ.setBorder(
            BorderFactory.createCompoundBorder(
                BorderFactory
                    .createLineBorder(
                        new Color(
                            200, 200, 200), 1),
                BorderFactory
                    .createEmptyBorder(
                        6, 12, 6, 12)));
        champ.setAlignmentX(LEFT_ALIGNMENT);
        champ.setBackground(
            new Color(250, 251, 252));
    }

    private JLabel badge(
            String texte, Color fond,
            Color texteColor) {
        JLabel l = new JLabel(texte) {
            @Override
            protected void paintComponent(
                    Graphics g) {
                Graphics2D g2 =
                    (Graphics2D) g;
                g2.setRenderingHint(
                    RenderingHints
                        .KEY_ANTIALIASING,
                    RenderingHints
                        .VALUE_ANTIALIAS_ON);
                g2.setColor(fond);
                g2.fillRoundRect(
                    0, 0,
                    getWidth(),
                    getHeight(),
                    20, 20);
                super.paintComponent(g);
            }
        };
        l.setFont(
            new Font("Segoe UI",
                Font.BOLD, 11));
        l.setForeground(texteColor);
        l.setOpaque(false);
        l.setBorder(
            new EmptyBorder(4, 12, 4, 12));
        return l;
    }

    private void afficherErreur(
            String msg) {
        lblMessage.setForeground(ROUGE);
        lblMessage.setText(msg);
    }

    private void afficherSucces(
            String msg) {
        lblMessage.setForeground(VERT);
        lblMessage.setText(msg);
    }
}