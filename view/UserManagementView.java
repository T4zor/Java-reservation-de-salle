package view;

import controller.MainController;
import model.User;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class UserManagementView extends JFrame {

    private MainController controller;
    private DefaultTableModel tableModel;
    private JTable table;
    private JTextField txtNom, txtPrenom,
                       txtEmail, txtMotDePasse;
    private JComboBox<String> cbRole;
    private List<User> allUsers;

    // =========================================================
    // CONSTRUCTEUR
    // =========================================================

    public UserManagementView(
            MainController controller) {
        this.controller = controller;

        setTitle("Gestion des utilisateurs");
        setSize(850, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(
            JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        add(construireTitre(),
            BorderLayout.NORTH);
        add(new JScrollPane(construireTableau()),
            BorderLayout.CENTER);
        add(construireFormulaire(),
            BorderLayout.EAST);
        add(construireBoutonsBas(),
            BorderLayout.SOUTH);

        chargerUsers();
    }

    // =========================================================
    // CONSTRUCTION INTERFACE
    // =========================================================

    private JPanel construireTitre() {
        JPanel topBar = new JPanel(
            new BorderLayout());
        topBar.setBackground(
            new Color(52, 73, 94));
        topBar.setBorder(
            BorderFactory.createEmptyBorder(
                12, 18, 12, 18));

        // Titre à gauche
        JLabel titre = new JLabel(
            "Gestion des utilisateurs");
        titre.setFont(
            new Font("Segoe UI", Font.BOLD, 16));
        titre.setForeground(Color.WHITE);
        topBar.add(titre, BorderLayout.WEST);

        // Barre de recherche à droite
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        searchPanel.setBackground(new Color(52, 73, 94));
        searchPanel.add(new JLabel("Rechercher :"));
        searchPanel.getComponent(0).setForeground(Color.WHITE);
        JTextField txtSearch = new JTextField(15);
        searchPanel.add(txtSearch);
        JButton btnSearch = new JButton("🔍");
        btnSearch.setPreferredSize(new Dimension(40, 25));
        searchPanel.add(btnSearch);
        JButton btnClear = new JButton("✕");
        btnClear.setPreferredSize(new Dimension(40, 25));
        searchPanel.add(btnClear);
        topBar.add(searchPanel, BorderLayout.EAST);

        // Événements de recherche
        btnSearch.addActionListener(e -> filterTable(txtSearch.getText()));
        btnClear.addActionListener(e -> {
            txtSearch.setText("");
            chargerUsers();
        });

        // Recherche en temps réel
        txtSearch.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                filterTable(txtSearch.getText());
            }
        });

        return topBar;
    }

    private JTable construireTableau() {
        String[] cols = {
            "ID", "Nom", "Prenom",
            "Email", "Role"
        };
        tableModel = new DefaultTableModel(
            cols, 0) {
            public boolean isCellEditable(
                    int r, int c) {
                return false;
            }
        };
        table = new JTable(tableModel);
        table.setRowHeight(25);
        table.setFont(
            new Font("Segoe UI", Font.PLAIN, 12));
        table.getTableHeader().setFont(
            new Font("Segoe UI", Font.BOLD, 12));
        table.setSelectionMode(
            ListSelectionModel.SINGLE_SELECTION);
        table.setAutoCreateRowSorter(true);
        table.getSelectionModel()
             .addListSelectionListener(
                 e -> remplirFormulaire());
        return table;
    }

    private JPanel construireFormulaire() {
        JPanel form = new JPanel();
        form.setLayout(
            new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setBorder(
            BorderFactory.createTitledBorder(
                "Details utilisateur"));
        form.setBackground(Color.WHITE);
        form.setPreferredSize(
            new Dimension(230, 0));

        txtNom        = champ("Nom :", form);
        txtPrenom     = champ("Prenom :", form);
        txtEmail      = champ("Email :", form);
        txtMotDePasse = champ(
            "Mot de passe :", form);

        form.add(new JLabel("Role :"));
        form.add(Box.createVerticalStrut(4));
        cbRole = new JComboBox<>(new String[]{
            "etudiant",
            "enseignant",
            "responsable"
        });
        cbRole.setMaximumSize(
            new Dimension(Integer.MAX_VALUE, 32));
        form.add(cbRole);
        form.add(Box.createVerticalStrut(15));

        // Indication mot de passe
        JLabel lblInfo = new JLabel(
            "<html><i>Laisser vide pour<br>"
            + "ne pas modifier<br>"
            + "le mot de passe</i></html>");
        lblInfo.setFont(
            new Font("Segoe UI", Font.ITALIC, 10));
        lblInfo.setForeground(Color.GRAY);
        form.add(lblInfo);
        form.add(Box.createVerticalStrut(10));

        JButton btnAjouter =
            bouton("Ajouter",
                new Color(39, 174, 96));
        JButton btnModifier =
            bouton("Modifier",
                new Color(41, 128, 185));
        JButton btnVider =
            bouton("Vider", Color.GRAY);

        btnAjouter.addActionListener(
            e -> ajouterUser());
        btnModifier.addActionListener(
            e -> modifierUser());
        btnVider.addActionListener(
            e -> viderFormulaire());

        form.add(btnAjouter);
        form.add(Box.createVerticalStrut(6));
        form.add(btnModifier);
        form.add(Box.createVerticalStrut(6));
        form.add(btnVider);

        return form;
    }

    private JPanel construireBoutonsBas() {
        JPanel bas = new JPanel(
            new FlowLayout(
                FlowLayout.CENTER, 12, 8));
        bas.setBackground(
            new Color(245, 246, 250));

        JButton btnSupprimer =
            bouton("Supprimer",
                new Color(192, 57, 43));
        JButton btnRetour =
            bouton("Retour",
                new Color(52, 73, 94));

        btnSupprimer.addActionListener(
            e -> supprimerUser());
        btnRetour.addActionListener(e -> {
            dispose();
            controller.showMenuPrincipal();
        });

        bas.add(btnSupprimer);
        bas.add(btnRetour);
        return bas;
    }

    // =========================================================
    // LOGIQUE BD
    // =========================================================

    private void chargerUsers() {
        tableModel.setRowCount(0);
        allUsers = controller.getAllUsers();
        loadTableData(allUsers);
    }

    private void loadTableData(List<User> users) {
        tableModel.setRowCount(0);
        for (User u : users) {
            tableModel.addRow(new Object[]{
                u.getId(),
                u.getNom(),
                u.getPrenom(),
                u.getEmail(),
                u.getRole()
            });
        }
    }

    private void filterTable(String searchText) {
        if (searchText == null || searchText.trim().isEmpty()) {
            loadTableData(allUsers);
            return;
        }

        List<User> filtered = allUsers.stream()
            .filter(u -> u.getNom().toLowerCase().contains(searchText.toLowerCase()) ||
                        u.getPrenom().toLowerCase().contains(searchText.toLowerCase()) ||
                        u.getEmail().toLowerCase().contains(searchText.toLowerCase()) ||
                        u.getRole().toLowerCase().contains(searchText.toLowerCase()))
            .toList();

        loadTableData(filtered);
    }

    private void remplirFormulaire() {
        int row = table.getSelectedRow();
        if (row < 0) return;
        txtNom.setText(
            (String) tableModel
                .getValueAt(row, 1));
        txtPrenom.setText(
            (String) tableModel
                .getValueAt(row, 2));
        txtEmail.setText(
            (String) tableModel
                .getValueAt(row, 3));
        cbRole.setSelectedItem(
            tableModel.getValueAt(row, 4));
        // Mot de passe vide par securite
        txtMotDePasse.setText("");
    }

    private void ajouterUser() {
        if (txtNom.getText().trim().isEmpty()
            || txtEmail.getText().trim().isEmpty()
            || txtMotDePasse.getText()
                .trim().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Nom, Email et Mot de passe "
                + "sont obligatoires.");
            return;
        }
        if (controller.emailExiste(
                txtEmail.getText().trim())) {
            JOptionPane.showMessageDialog(this,
                "Cet email existe deja.");
            return;
        }
        User u = new User(
            txtNom.getText().trim(),
            txtPrenom.getText().trim(),
            txtEmail.getText().trim(),
            (String) cbRole.getSelectedItem(),
            txtMotDePasse.getText().trim()
        );
        if (controller.ajouterUser(u)) {
            JOptionPane.showMessageDialog(this,
                "Utilisateur ajoute !");
            viderFormulaire();
            chargerUsers();
        } else {
            JOptionPane.showMessageDialog(this,
                "Erreur lors de l'ajout.");
        }
    }

    private void modifierUser() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this,
                "Selectionnez un utilisateur.");
            return;
        }

        int id = (int) tableModel
            .getValueAt(row, 0);

        // Si mot de passe vide → garder l'ancien
        String mdp = txtMotDePasse
            .getText().trim();
        if (mdp.isEmpty()) {
            mdp = controller
                .getMotDePasseUser(id);
        }

        User u = new User(
            txtNom.getText().trim(),
            txtPrenom.getText().trim(),
            txtEmail.getText().trim(),
            (String) cbRole.getSelectedItem(),
            mdp
        );
        u.setId(id);

        if (controller.modifierUser(u)) {
            JOptionPane.showMessageDialog(this,
                "Utilisateur modifie !");
            chargerUsers();
        } else {
            JOptionPane.showMessageDialog(this,
                "Erreur modification.");
        }
    }

    private void supprimerUser() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this,
                "Selectionnez un utilisateur.");
            return;
        }
        String nom =
            tableModel.getValueAt(row, 1)
            + " "
            + tableModel.getValueAt(row, 2);
        int choix = JOptionPane
            .showConfirmDialog(this,
                "Supprimer " + nom + " ?",
                "Confirmation",
                JOptionPane.YES_NO_OPTION);
        if (choix == JOptionPane.YES_OPTION) {
            int id = (int) tableModel
                .getValueAt(row, 0);
            if (controller.supprimerUser(id)) {
                JOptionPane.showMessageDialog(
                    this,
                    "Utilisateur supprime !");
                viderFormulaire();
                chargerUsers();
            }
        }
    }

    private void viderFormulaire() {
        txtNom.setText("");
        txtPrenom.setText("");
        txtEmail.setText("");
        txtMotDePasse.setText("");
        cbRole.setSelectedIndex(0);
        table.clearSelection();
    }

    // =========================================================
    // HELPERS
    // =========================================================

    private JTextField champ(String label,
                              JPanel parent) {
        parent.add(new JLabel(label));
        parent.add(Box.createVerticalStrut(4));
        JTextField tf = new JTextField();
        tf.setMaximumSize(
            new Dimension(
                Integer.MAX_VALUE, 32));
        tf.setFont(
            new Font("Segoe UI", Font.PLAIN, 12));
        parent.add(tf);
        parent.add(Box.createVerticalStrut(10));
        return tf;
    }

    private JButton bouton(String texte,
                            Color couleur) {
        JButton b = new JButton(texte);
        b.setMaximumSize(
            new Dimension(
                Integer.MAX_VALUE, 34));
        b.setBackground(couleur);
        b.setForeground(Color.WHITE);
        b.setFont(
            new Font("Segoe UI", Font.BOLD, 12));
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setCursor(
            new Cursor(Cursor.HAND_CURSOR));
        return b;
    }
}