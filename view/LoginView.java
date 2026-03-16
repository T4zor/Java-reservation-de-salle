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

    // =========================================================
    // CONSTRUCTEUR
    // =========================================================

    public LoginView(MainController controller) {
        this.controller = controller;
        // Appliquer le thème moderne
        StyleUtils.applyModernStyle(this);

        setTitle("Connexion — Réservation de Salles");
        setSize(440, 560);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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

        add(construireHeader(),
            BorderLayout.NORTH);
        add(StyleUtils.createCenteredPanel(construireFormulaire()),
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
        header.setBackground(StyleUtils.PRIMARY_COLOR);
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
        panel.setLayout(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(25, 35, 20, 35));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER;

        // Email
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 1;
        panel.add(creerLabel("ADRESSE EMAIL"), gbc);

        gbc.gridy = 1;
        txtEmail = StyleUtils.createStyledTextField();
        txtEmail.setPreferredSize(new Dimension(300, 35));
        txtEmail.setMaximumSize(new Dimension(300, 35));
        panel.add(txtEmail, gbc);

        // Mot de passe
        gbc.gridy = 2;
        panel.add(creerLabel("MOT DE PASSE"), gbc);

        gbc.gridy = 3;
        txtMotDePasse = StyleUtils.createStyledPasswordField();
        txtMotDePasse.setPreferredSize(new Dimension(300, 35));
        txtMotDePasse.setMaximumSize(new Dimension(300, 35));
        panel.add(txtMotDePasse, gbc);

        // Message erreur/succes
        gbc.gridy = 4;
        lblMessage = new JLabel(" ");
        lblMessage.setFont(StyleUtils.SMALL_FONT);
        lblMessage.setForeground(StyleUtils.ERROR_COLOR);
        lblMessage.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(lblMessage, gbc);

        // Bouton connexion
        gbc.gridy = 5;
        JButton btnConnexion = StyleUtils.createStyledButton("Se connecter");
        btnConnexion.setPreferredSize(new Dimension(300, 44));
        btnConnexion.setMaximumSize(new Dimension(300, 44));
        panel.add(btnConnexion, gbc);

        // Separateur
        gbc.gridy = 6;
        JSeparator sep = new JSeparator();
        sep.setPreferredSize(new Dimension(300, 1));
        sep.setForeground(new Color(220, 220, 220));
        panel.add(sep, gbc);

        // Roles disponibles
        gbc.gridy = 7;
        JLabel lblRoles = new JLabel("Roles disponibles :");
        lblRoles.setFont(new Font("Segoe UI", Font.BOLD, 11));
        lblRoles.setForeground(new Color(100, 100, 100));
        lblRoles.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(lblRoles, gbc);

        // Badges des roles
        gbc.gridy = 8;
        JPanel badges = new JPanel(new FlowLayout(FlowLayout.CENTER, 6, 0));
        badges.setOpaque(false);
        badges.setPreferredSize(new Dimension(300, 30));
        badges.add(badge("Etudiant", new Color(230, 241, 251), new Color(12, 68, 124)));
        badges.add(badge("Enseignant", new Color(234, 243, 222), new Color(39, 80, 10)));
        badges.add(badge("Responsable", new Color(250, 238, 218), new Color(99, 56, 6)));
        panel.add(badges, gbc);

        // Actions
        btnConnexion.addActionListener(e -> seConnecter());
        txtMotDePasse.addActionListener(e -> seConnecter());
        txtEmail.addActionListener(e -> txtMotDePasse.requestFocus());

        return panel;
    }

    // =========================================================
    // FOOTER
    // =========================================================

    private JPanel construireFooter() {
        JPanel footer = new JPanel(
            new FlowLayout(
                FlowLayout.CENTER));
        footer.setBackground(StyleUtils.BACKGROUND_COLOR);
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
            controller.showMenuResponsableView();
        } else {
            controller.showMenuEtudiantView();
        }
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
        lblMessage.setForeground(StyleUtils.ERROR_COLOR);
        lblMessage.setText(msg);
    }

    private void afficherSucces(
            String msg) {
        lblMessage.setForeground(StyleUtils.SUCCESS_COLOR);
        lblMessage.setText(msg);
    }
}