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
        setSize(520, 640);
        setMinimumSize(new Dimension(420, 540));
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
        JButton btnLogin       = makeTabBtn("Connexion",   true);
        JButton btnInscription = makeTabBtn("Inscription", false);

        JPanel tabBar = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        tabBar.setBackground(Color.WHITE);
        tabBar.add(btnLogin);
        tabBar.add(btnInscription);

        cardLayout = new CardLayout();
        mainPanel  = new JPanel(cardLayout);
        mainPanel.setBackground(Color.WHITE);
        mainPanel.add(buildLoginPanel(),       "LOGIN");
        mainPanel.add(buildInscriptionPanel(), "INSCRIPTION");

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

    private JPanel buildLoginPanel() {
        JPanel outer = new JPanel(new GridBagLayout());
        outer.setBackground(Color.WHITE);

        JPanel inner = new JPanel(new GridBagLayout());
        inner.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill      = GridBagConstraints.HORIZONTAL;
        gbc.weightx   = 1.0;
        gbc.gridx     = 0;
        gbc.gridwidth = 2;
        int row = 0;

        gbc.gridy = row++; gbc.insets = new Insets(0, 0, 2, 0);
        inner.add(fieldLabel("Email institutionnel"), gbc);
        gbc.gridy = row++; gbc.insets = new Insets(0, 0, 12, 0);
        loginEmailField = textField();
        inner.add(loginEmailField, gbc);

        gbc.gridy = row++; gbc.insets = new Insets(0, 0, 2, 0);
        inner.add(fieldLabel("Mot de passe"), gbc);
        gbc.gridy = row++; gbc.insets = new Insets(0, 0, 10, 0);
        loginPasswordField = passwordField();
        inner.add(loginPasswordField, gbc);

        gbc.gridy     = row++;
        gbc.gridwidth = 1;
        gbc.weightx   = 0.5;
        gbc.insets    = new Insets(0, 0, 18, 0);
        JCheckBox remember = new JCheckBox("Se souvenir de moi");
        remember.setFont(new Font("SansSerif", Font.PLAIN, 12));
        remember.setForeground(GRAY_TEXT);
        remember.setBackground(Color.WHITE);
        gbc.gridx = 0;
        inner.add(remember, gbc);

        JLabel forgot = new JLabel("<html><u>Mot de passe oublie ?</u></html>");
        forgot.setFont(new Font("SansSerif", Font.PLAIN, 12));
        forgot.setForeground(BLUE);
        forgot.setHorizontalAlignment(SwingConstants.RIGHT);
        forgot.setCursor(new Cursor(Cursor.HAND_CURSOR));
        forgot.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                JOptionPane.showMessageDialog(LoginView.this,
                    "Un email de reinitialisation vous sera envoye.",
                    "Mot de passe oublie", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        gbc.gridx = 1;
        inner.add(forgot, gbc);

        gbc.gridx     = 0;
        gbc.gridwidth = 2;
        gbc.weightx   = 1.0;
        gbc.gridy     = row++;
        gbc.insets    = new Insets(0, 0, 10, 0);
        JButton btnLogin = primaryButton("Se connecter");
        btnLogin.addActionListener(e -> handleLogin());
        inner.add(btnLogin, gbc);

        gbc.gridy  = row++;
        gbc.insets = new Insets(0, 0, 0, 0);
        JLabel note = new JLabel("Le role est detecte automatiquement selon votre compte");
        note.setFont(new Font("SansSerif", Font.PLAIN, 11));
        note.setForeground(GRAY_NOTE);
        note.setHorizontalAlignment(SwingConstants.CENTER);
        inner.add(note, gbc);

        gbc.gridy   = row;
        gbc.weighty = 1.0;
        inner.add(new JLabel(), gbc);

        GridBagConstraints o = new GridBagConstraints();
        o.fill = GridBagConstraints.BOTH; o.weighty = 1.0;
        o.weightx = 0.15; o.gridx = 0;
        outer.add(new JPanel() {{ setBackground(Color.WHITE); }}, o);
        o.weightx = 0.7; o.gridx = 1;
        outer.add(inner, o);
        o.weightx = 0.15; o.gridx = 2;
        outer.add(new JPanel() {{ setBackground(Color.WHITE); }}, o);

        return outer;
    }

    private JPanel buildInscriptionPanel() {
        JPanel outer = new JPanel(new GridBagLayout());
        outer.setBackground(Color.WHITE);

        JPanel inner = new JPanel(new GridBagLayout());
        inner.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill      = GridBagConstraints.HORIZONTAL;
        gbc.weightx   = 1.0;
        gbc.gridx     = 0;
        gbc.gridwidth = 2;
        int row = 0;

        gbc.gridy = row++; gbc.insets = new Insets(4, 0, 2, 0);
        inner.add(fieldLabel("Type de compte"), gbc);
        roleCombo = new JComboBox<>(new String[]{"Etudiant", "Enseignant"});
        styleCombo(roleCombo);
        gbc.gridy = row++; gbc.insets = new Insets(0, 0, 12, 0);
        inner.add(roleCombo, gbc);

        gbc.gridy = row++; gbc.gridwidth = 1; gbc.weightx = 0.5;
        gbc.gridx = 0; gbc.insets = new Insets(0, 0, 2, 4);
        nomField = textField();
        inner.add(fieldLabel("Nom"), gbc);
        gbc.gridx = 1; gbc.insets = new Insets(0, 4, 2, 0);
        prenomField = textField();
        inner.add(fieldLabel("Prenom"), gbc);

        gbc.gridy = row++;
        gbc.gridx = 0; gbc.insets = new Insets(0, 0, 12, 4);
        inner.add(nomField, gbc);
        gbc.gridx = 1; gbc.insets = new Insets(0, 4, 12, 0);
        inner.add(prenomField, gbc);

        gbc.gridx = 0; gbc.gridwidth = 2; gbc.weightx = 1.0;
        gbc.gridy = row++; gbc.insets = new Insets(0, 0, 2, 0);
        inner.add(fieldLabel("Email institutionnel"), gbc);
        emailField = textField();
        gbc.gridy = row++; gbc.insets = new Insets(0, 0, 12, 0);
        inner.add(emailField, gbc);

        roleCardLayout = new CardLayout();
        rolePanel = new JPanel(roleCardLayout);
        rolePanel.setBackground(Color.WHITE);
        rolePanel.add(buildEtudiantFields(),   "ETUDIANT");
        rolePanel.add(buildEnseignantFields(), "ENSEIGNANT");
        gbc.gridy = row++; gbc.insets = new Insets(0, 0, 12, 0);
        inner.add(rolePanel, gbc);

        gbc.gridy = row++; gbc.gridwidth = 1; gbc.weightx = 0.5;
        passField = passwordField(); confirmField = passwordField();
        gbc.gridx = 0; gbc.insets = new Insets(0, 0, 2, 4);
        inner.add(fieldLabel("Mot de passe"), gbc);
        gbc.gridx = 1; gbc.insets = new Insets(0, 4, 2, 0);
        inner.add(fieldLabel("Confirmer"), gbc);

        gbc.gridy = row++;
        gbc.gridx = 0; gbc.insets = new Insets(0, 0, 18, 4);
        inner.add(passField, gbc);
        gbc.gridx = 1; gbc.insets = new Insets(0, 4, 18, 0);
        inner.add(confirmField, gbc);

        gbc.gridx = 0; gbc.gridwidth = 2; gbc.weightx = 1.0;
        gbc.gridy = row++; gbc.insets = new Insets(0, 0, 10, 0);
        JButton btnCreer = primaryButton("Creer mon compte");
        btnCreer.addActionListener(e -> handleInscription());
        inner.add(btnCreer, gbc);

        gbc.gridy = row++; gbc.insets = new Insets(0, 0, 0, 0);
        JLabel note = new JLabel("Les comptes responsables sont crees par l'administrateur");
        note.setFont(new Font("SansSerif", Font.PLAIN, 11));
        note.setForeground(GRAY_NOTE);
        note.setHorizontalAlignment(SwingConstants.CENTER);
        inner.add(note, gbc);

        gbc.gridy = row; gbc.weighty = 1.0;
        inner.add(new JLabel(), gbc);

        roleCombo.addActionListener(e -> {
            String sel = (String) roleCombo.getSelectedItem();
            roleCardLayout.show(rolePanel, "Etudiant".equals(sel) ? "ETUDIANT" : "ENSEIGNANT");
        });

        GridBagConstraints o = new GridBagConstraints();
        o.fill = GridBagConstraints.BOTH; o.weighty = 1.0;
        o.weightx = 0.1; o.gridx = 0;
        outer.add(new JPanel() {{ setBackground(Color.WHITE); }}, o);
        o.weightx = 0.8; o.gridx = 1;
        outer.add(inner, o);
        o.weightx = 0.1; o.gridx = 2;
        outer.add(new JPanel() {{ setBackground(Color.WHITE); }}, o);

        return outer;
    }

    private JPanel buildEtudiantFields() {
        JPanel p = new JPanel(new GridBagLayout());
        p.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 0.5; gbc.gridy = 0;

        filiereField = textField();
        niveauCombo  = new JComboBox<>(new String[]{"Licence 1","Licence 2","Licence 3","Master 1","Master 2"});
        styleCombo(niveauCombo);

        gbc.gridx = 0; gbc.insets = new Insets(0, 0, 2, 6);
        p.add(fieldLabel("Filiere"), gbc);
        gbc.gridx = 1; gbc.insets = new Insets(0, 6, 2, 0);
        p.add(fieldLabel("Niveau"), gbc);

        gbc.gridy = 1;
        gbc.gridx = 0; gbc.insets = new Insets(0, 0, 10, 6);
        p.add(filiereField, gbc);
        gbc.gridx = 1; gbc.insets = new Insets(0, 6, 10, 0);
        p.add(niveauCombo, gbc);

        gbc.gridy = 2; gbc.gridx = 0; gbc.gridwidth = 2; gbc.weightx = 1.0;
        gbc.insets = new Insets(0, 0, 2, 0);
        p.add(fieldLabel("Numero etudiant"), gbc);

        gbc.gridy = 3; gbc.insets = new Insets(0, 0, 0, 0);
        numEtudiantField = textField();
        p.add(numEtudiantField, gbc);
        return p;
    }

    private JPanel buildEnseignantFields() {
        JPanel p = new JPanel(new GridBagLayout());
        p.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0; gbc.gridx = 0;

        gbc.gridy = 0; gbc.insets = new Insets(0, 0, 2, 0);
        p.add(fieldLabel("Departement"), gbc);
        gbc.gridy = 1; gbc.insets = new Insets(0, 0, 10, 0);
        departementField = textField();
        p.add(departementField, gbc);

        gbc.gridy = 2; gbc.insets = new Insets(0, 0, 2, 0);
        p.add(fieldLabel("Fonction"), gbc);
        gbc.gridy = 3; gbc.insets = new Insets(0, 0, 0, 0);
        fonctionField = textField();
        p.add(fonctionField, gbc);
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

    private JTextField textField() {
        JTextField f = new JTextField();
        f.setFont(new Font("SansSerif", Font.PLAIN, 13));
        f.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(GRAY_BORDER, 1),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)));
        return f;
    }

    private JPasswordField passwordField() {
        JPasswordField f = new JPasswordField();
        f.setFont(new Font("SansSerif", Font.PLAIN, 13));
        f.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(GRAY_BORDER, 1),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)));
        return f;
    }

    private JLabel fieldLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("SansSerif", Font.PLAIN, 12));
        l.setForeground(GRAY_TEXT);
        l.setHorizontalAlignment(SwingConstants.LEFT);
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
        b.setPreferredSize(new Dimension(0, 44));
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
        if (active) { b.setBackground(BLUE); b.setForeground(Color.WHITE); }
        else        { b.setBackground(Color.WHITE); b.setForeground(BLUE); }
        return b;
    }

    private void setActiveTab(JButton active, JButton inactive) {
        active.setBackground(BLUE);          active.setForeground(Color.WHITE);
        inactive.setBackground(Color.WHITE); inactive.setForeground(BLUE);
    }

    private void styleCombo(JComboBox<?> combo) {
        combo.setFont(new Font("SansSerif", Font.PLAIN, 13));
        combo.setBorder(BorderFactory.createLineBorder(GRAY_BORDER, 1));
    }

    // TODO: enlever cette methode main quand tout sera fini
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainController controller = new MainController();
            new LoginView(controller).setVisible(true);
        });
    }
}