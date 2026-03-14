package view;

import controller.MainController;
import javax.swing.*;
import java.awt.*;

public class LoginView extends JFrame {

    private MainController controller;
    private CardLayout cardLayout;
    private JPanel mainPanel;

    private JTextField loginEmailField;
    private JPasswordField loginPasswordField;

    private JTextField nomField, prenomField, emailField;
    private JTextField numEtudiantField, filiereField;
    private JTextField departementField, fonctionField;
    private JComboBox<String> roleCombo, niveauCombo;
    private JPasswordField passField, confirmField;
    private CardLayout roleCardLayout;
    private JPanel rolePanel;

    private static final Color BLUE        = new Color(0x18, 0x5F, 0xA5);
    private static final Color BLUE_LIGHT  = new Color(0xE6, 0xF1, 0xFB);
    private static final Color GRAY_TEXT   = new Color(0x88, 0x87, 0x80);
    private static final Color GRAY_BORDER = new Color(0xD3, 0xD1, 0xC7);
    private static final Color GRAY_NOTE   = new Color(0xB4, 0xB2, 0xA9);

    public LoginView(MainController controller) {
        this.controller = controller;

        setTitle("Reservation de Salles");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Taille fixe — ne change JAMAIS
        setSize(500, 620);
        setResizable(true);
        setLocationRelativeTo(null);

        getContentPane().setBackground(Color.WHITE);
        setLayout(new BorderLayout());

        add(buildHeader(), BorderLayout.NORTH);
        add(buildCenter(), BorderLayout.CENTER);

        try { setIconImage(new ImageIcon("icon/accueil.png").getImage()); } catch (Exception ex) {}
    }

    private JPanel buildHeader() {
        JPanel panel = new JPanel();
        panel.setBackground(Color.WHITE);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(24, 0, 8, 0));

        JPanel icon = new JPanel() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(BLUE_LIGHT);
                g2.fillRoundRect(0, 0, 48, 48, 12, 12);
                g2.setColor(BLUE);
                g2.setStroke(new BasicStroke(2.2f));
                g2.drawRoundRect(10, 10, 28, 28, 4, 4);
                g2.drawLine(16, 24, 30, 24);
                g2.drawLine(24, 16, 24, 32);
            }
        };
        icon.setPreferredSize(new Dimension(48, 48));
        icon.setMaximumSize(new Dimension(48, 48));
        icon.setBackground(Color.WHITE);
        icon.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel title = new JLabel("Reservation de salles");
        title.setFont(new Font("SansSerif", Font.BOLD, 18));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitle = new JLabel("Connectez-vous a votre compte");
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 13));
        subtitle.setForeground(GRAY_TEXT);
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(icon);
        panel.add(Box.createVerticalStrut(10));
        panel.add(title);
        panel.add(Box.createVerticalStrut(4));
        panel.add(subtitle);
        return panel;
    }

    private JPanel buildCenter() {
        // Onglets — taille fixe pour les deux boutons
        JButton btnLogin       = makeTabBtn("Connexion",   true);
        JButton btnInscription = makeTabBtn("Inscription", false);

        JPanel tabBar = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        tabBar.setBackground(Color.WHITE);
        tabBar.add(btnLogin);
        tabBar.add(btnInscription);

        // CardLayout — les deux panneaux ont la MEME taille
        cardLayout = new CardLayout();
        mainPanel  = new JPanel(cardLayout);
        mainPanel.setBackground(Color.WHITE);

        // Les deux panneaux sont dans un JScrollPane
        // pour que le contenu ne déborde jamais
        JScrollPane scrollLogin = scrollWrap(buildLoginPanel());
        JScrollPane scrollInsc  = scrollWrap(buildInscriptionPanel());

        mainPanel.add(scrollLogin, "LOGIN");
        mainPanel.add(scrollInsc,  "INSCRIPTION");

        // Les onglets NE changent PAS la taille de la fenetre
        btnLogin.addActionListener(e -> {
            cardLayout.show(mainPanel, "LOGIN");
            setActiveTab(btnLogin, btnInscription);
        });
        btnInscription.addActionListener(e -> {
            cardLayout.show(mainPanel, "INSCRIPTION");
            setActiveTab(btnInscription, btnLogin);
        });

        JPanel center = new JPanel(new BorderLayout());
        center.setBackground(Color.WHITE);
        center.add(tabBar,    BorderLayout.NORTH);
        center.add(mainPanel, BorderLayout.CENTER);
        return center;
    }

    // Wrapping dans ScrollPane sans bordure visible
    private JScrollPane scrollWrap(JPanel content) {
        content.setBorder(BorderFactory.createEmptyBorder(4, 40, 20, 40));
        JScrollPane sp = new JScrollPane(content);
        sp.setBorder(null);
        sp.getVerticalScrollBar().setUnitIncrement(14);
        sp.setBackground(Color.WHITE);
        sp.getViewport().setBackground(Color.WHITE);
        sp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        return sp;
    }

    private JPanel buildLoginPanel() {
        JPanel p = vbox();

        p.add(fieldLabel("Email institutionnel"));
        p.add(Box.createVerticalStrut(5));
        loginEmailField = textField();
        p.add(loginEmailField);
        p.add(Box.createVerticalStrut(14));

        p.add(fieldLabel("Mot de passe"));
        p.add(Box.createVerticalStrut(5));
        loginPasswordField = passwordField();
        p.add(loginPasswordField);
        p.add(Box.createVerticalStrut(12));

        JPanel row = new JPanel(new BorderLayout());
        row.setBackground(Color.WHITE);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 28));

        JCheckBox remember = new JCheckBox("Se souvenir de moi");
        remember.setFont(new Font("SansSerif", Font.PLAIN, 12));
        remember.setForeground(GRAY_TEXT);
        remember.setBackground(Color.WHITE);

        JLabel forgot = new JLabel("<html><u>Mot de passe oublie ?</u></html>");
        forgot.setFont(new Font("SansSerif", Font.PLAIN, 12));
        forgot.setForeground(BLUE);
        forgot.setCursor(new Cursor(Cursor.HAND_CURSOR));
        forgot.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                JOptionPane.showMessageDialog(LoginView.this,
                    "Un email de reinitialisation vous sera envoye.",
                    "Mot de passe oublie", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        row.add(remember, BorderLayout.WEST);
        row.add(forgot,   BorderLayout.EAST);
        p.add(row);
        p.add(Box.createVerticalStrut(22));

        JButton btn = primaryButton("Se connecter");
        btn.addActionListener(e -> handleLogin());
        p.add(btn);
        p.add(Box.createVerticalStrut(12));

        JLabel note = new JLabel("Le role est detecte automatiquement selon votre compte");
        note.setFont(new Font("SansSerif", Font.PLAIN, 11));
        note.setForeground(GRAY_NOTE);
        note.setAlignmentX(Component.CENTER_ALIGNMENT);
        p.add(note);
        return p;
    }

    private JPanel buildInscriptionPanel() {
        JPanel p = vbox();

        p.add(fieldLabel("Type de compte"));
        p.add(Box.createVerticalStrut(5));
        roleCombo = new JComboBox<>(new String[]{"Etudiant", "Enseignant"});
        styleCombo(roleCombo);
        p.add(roleCombo);
        p.add(Box.createVerticalStrut(14));

        nomField    = textField();
        prenomField = textField();
        p.add(row2(labeled("Nom", nomField), labeled("Prenom", prenomField)));
        p.add(Box.createVerticalStrut(14));

        p.add(fieldLabel("Email institutionnel"));
        p.add(Box.createVerticalStrut(5));
        emailField = textField();
        p.add(emailField);
        p.add(Box.createVerticalStrut(14));

        roleCardLayout = new CardLayout();
        rolePanel = new JPanel(roleCardLayout);
        rolePanel.setBackground(Color.WHITE);
        rolePanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));
        rolePanel.add(buildEtudiantFields(),   "ETUDIANT");
        rolePanel.add(buildEnseignantFields(), "ENSEIGNANT");
        p.add(rolePanel);
        p.add(Box.createVerticalStrut(14));

        passField    = passwordField();
        confirmField = passwordField();
        p.add(row2(labeled("Mot de passe", passField), labeled("Confirmer", confirmField)));
        p.add(Box.createVerticalStrut(22));

        JButton btn = primaryButton("Creer mon compte");
        btn.addActionListener(e -> handleInscription());
        p.add(btn);
        p.add(Box.createVerticalStrut(10));

        JLabel note = new JLabel("Les comptes responsables sont crees par l'administrateur");
        note.setFont(new Font("SansSerif", Font.PLAIN, 11));
        note.setForeground(GRAY_NOTE);
        note.setAlignmentX(Component.CENTER_ALIGNMENT);
        p.add(note);

        roleCombo.addActionListener(e -> {
            String sel = (String) roleCombo.getSelectedItem();
            roleCardLayout.show(rolePanel, "Etudiant".equals(sel) ? "ETUDIANT" : "ENSEIGNANT");
        });
        return p;
    }

    private JPanel buildEtudiantFields() {
        JPanel p = vbox();
        filiereField = textField();
        niveauCombo  = new JComboBox<>(new String[]{"Licence 1","Licence 2","Licence 3","Master 1","Master 2"});
        styleCombo(niveauCombo);
        p.add(row2(labeled("Filiere", filiereField), labeled("Niveau", niveauCombo)));
        p.add(Box.createVerticalStrut(14));
        p.add(fieldLabel("Numero etudiant"));
        p.add(Box.createVerticalStrut(5));
        numEtudiantField = textField();
        p.add(numEtudiantField);
        return p;
    }

    private JPanel buildEnseignantFields() {
        JPanel p = vbox();
        p.add(fieldLabel("Departement"));
        p.add(Box.createVerticalStrut(5));
        departementField = textField();
        p.add(departementField);
        p.add(Box.createVerticalStrut(14));
        p.add(fieldLabel("Fonction"));
        p.add(Box.createVerticalStrut(5));
        fonctionField = textField();
        p.add(fonctionField);
        return p;
    }

    private void handleLogin() {
        String email = loginEmailField.getText().trim();
        String pass  = new String(loginPasswordField.getPassword()).trim();
        if (email.isEmpty() || pass.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Veuillez remplir tous les champs.",
                "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }
        dispose();
        controller.showReservationRequestView();
    }

    private void handleInscription() {
        String nom    = nomField.getText().trim();
        String prenom = prenomField.getText().trim();
        String email  = emailField.getText().trim();
        String pass   = new String(passField.getPassword());
        String confirm = new String(confirmField.getPassword());
        if (nom.isEmpty() || prenom.isEmpty() || email.isEmpty() || pass.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Veuillez remplir tous les champs.",
                "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (!pass.equals(confirm)) {
            JOptionPane.showMessageDialog(this, "Les mots de passe ne correspondent pas.",
                "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }
        JOptionPane.showMessageDialog(this, "Compte cree avec succes !",
            "Succes", JOptionPane.INFORMATION_MESSAGE);
    }

    // ─── UTILITAIRES ──────────────────────────────────────────────────
    private JPanel vbox() {
        JPanel p = new JPanel();
        p.setBackground(Color.WHITE);
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        return p;
    }

    private JTextField textField() {
        JTextField f = new JTextField();
        f.setFont(new Font("SansSerif", Font.PLAIN, 13));
        f.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(GRAY_BORDER, 1),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)));
        f.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        return f;
    }

    private JPasswordField passwordField() {
        JPasswordField f = new JPasswordField();
        f.setFont(new Font("SansSerif", Font.PLAIN, 13));
        f.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(GRAY_BORDER, 1),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)));
        f.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        return f;
    }

    private JLabel fieldLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("SansSerif", Font.PLAIN, 12));
        l.setForeground(GRAY_TEXT);
        return l;
    }

    private JButton primaryButton(String text) {
        JButton b = new JButton(text);
        b.setFont(new Font("SansSerif", Font.BOLD, 14));
        b.setBackground(BLUE);
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setOpaque(true);
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        b.setBorder(BorderFactory.createEmptyBorder(12, 0, 12, 0));
        b.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        b.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) { b.setBackground(new Color(0x0C,0x44,0x7C)); }
            public void mouseExited(java.awt.event.MouseEvent e)  { b.setBackground(BLUE); }
        });
        return b;
    }

    private JButton makeTabBtn(String text, boolean active) {
        JButton b = new JButton(text);
        b.setFont(new Font("SansSerif", Font.PLAIN, 13));
        b.setFocusPainted(false);
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        b.setPreferredSize(new Dimension(140, 36));
        b.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BLUE, 1, true),
            BorderFactory.createEmptyBorder(6, 20, 6, 20)));
        b.setOpaque(true);
        if (active) { b.setBackground(BLUE);       b.setForeground(Color.WHITE); }
        else         { b.setBackground(Color.WHITE); b.setForeground(BLUE); }
        return b;
    }

    private void setActiveTab(JButton active, JButton inactive) {
        active.setBackground(BLUE);        active.setForeground(Color.WHITE);
        inactive.setBackground(Color.WHITE); inactive.setForeground(BLUE);
    }

    private void styleCombo(JComboBox<?> combo) {
        combo.setFont(new Font("SansSerif", Font.PLAIN, 13));
        combo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
    }

    private JPanel labeled(String label, JComponent field) {
        JPanel p = vbox();
        p.add(fieldLabel(label));
        p.add(Box.createVerticalStrut(5));
        p.add(field);
        return p;
    }

    private JPanel row2(JPanel left, JPanel right) {
        JPanel row = new JPanel(new GridLayout(1, 2, 10, 0));
        row.setBackground(Color.WHITE);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 65));
        row.add(left);
        row.add(right);
        return row;
    }
}