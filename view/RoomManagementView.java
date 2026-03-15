package view;

import controller.MainController;
import model.Room;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class RoomManagementView extends JFrame {
    private static final long serialVersionUID = 1L;

    private MainController controller;
    private DefaultTableModel tableModel;
    private JTable table;
    private JTextField txtNom, txtCapacite, txtEquipements;
    private Room salleSelectionnee;
    private List<Room> allSalles;

    public RoomManagementView(MainController controller) {
        this.controller = controller;
        setTitle("Gestion des salles");
        setSize(850, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        add(construireTitre(), BorderLayout.NORTH);
        add(new JScrollPane(construireTableau()), BorderLayout.CENTER);
        add(construireFormulaire(), BorderLayout.EAST);
        add(construireBoutonsBas(), BorderLayout.SOUTH);

        chargerSalles();
    }

    private JPanel construireTitre() {
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(new Color(52, 73, 94));
        topBar.setBorder(BorderFactory.createEmptyBorder(12, 18, 12, 18));

        // Titre à gauche
        JLabel titre = new JLabel("Gestion des salles");
        titre.setFont(new Font("Segoe UI", Font.BOLD, 16));
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
            chargerSalles();
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
        String[] cols = {"ID", "Nom", "Capacité", "Équipements"};
        tableModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        table = new JTable(tableModel);
        table.setRowHeight(25);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setAutoCreateRowSorter(true);
        table.getSelectionModel().addListSelectionListener(e -> remplirFormulaire());
        return table;
    }

    private JPanel construireFormulaire() {
        JPanel form = new JPanel();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setBorder(BorderFactory.createTitledBorder("Détails salle"));
        form.setBackground(Color.WHITE);
        form.setPreferredSize(new Dimension(250, 0));

        form.add(new JLabel("Nom :"));
        txtNom = new JTextField();
        form.add(txtNom);

        form.add(Box.createVerticalStrut(10));
        form.add(new JLabel("Capacité :"));
        txtCapacite = new JTextField();
        form.add(txtCapacite);

        form.add(Box.createVerticalStrut(10));
        form.add(new JLabel("Équipements :"));
        txtEquipements = new JTextField();
        form.add(txtEquipements);

        form.add(Box.createVerticalStrut(20));
        JButton btnAjouter = new JButton("Ajouter");
        btnAjouter.addActionListener(e -> ajouterSalle());
        form.add(btnAjouter);

        JButton btnModifier = new JButton("Modifier");
        btnModifier.addActionListener(e -> modifierSalle());
        form.add(btnModifier);

        JButton btnSupprimer = new JButton("Supprimer");
        btnSupprimer.addActionListener(e -> supprimerSalle());
        form.add(btnSupprimer);

        return form;
    }

    private JPanel construireBoutonsBas() {
        JPanel bas = new JPanel();
        JButton btnRetour = new JButton("Retour au menu");
        btnRetour.addActionListener(e -> {
            dispose();
            controller.showMenuPrincipal();
        });
        JButton btnDeconnexion = new JButton("Déconnexion");
        btnDeconnexion.addActionListener(e -> {
            dispose();
            controller.deconnecter();
        });
        bas.add(btnRetour);
        bas.add(btnDeconnexion);
        return bas;
    }

    private void chargerSalles() {
        tableModel.setRowCount(0);
        try {
            allSalles = Room.findAll();
            loadTableData(allSalles);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erreur lors du chargement des salles : " + e.getMessage(),
                                        "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadTableData(List<Room> salles) {
        tableModel.setRowCount(0);
        for (Room salle : salles) {
            tableModel.addRow(new Object[]{
                salle.getId(),
                salle.getNom(),
                salle.getCapacite(),
                salle.getEquipements()
            });
        }
    }

    private void filterTable(String searchText) {
        if (searchText == null || searchText.trim().isEmpty()) {
            loadTableData(allSalles);
            return;
        }

        List<Room> filtered = allSalles.stream()
            .filter(s -> s.getNom().toLowerCase().contains(searchText.toLowerCase()) ||
                        String.valueOf(s.getCapacite()).contains(searchText) ||
                        (s.getEquipements() != null && s.getEquipements().toLowerCase().contains(searchText.toLowerCase())))
            .toList();

        loadTableData(filtered);
    }

    private void remplirFormulaire() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow >= 0) {
            int id = (Integer) tableModel.getValueAt(selectedRow, 0);
            try {
                // Find the room by id
                List<Room> salles = Room.findAll();
                for (Room salle : salles) {
                    if (salle.getId() == id) {
                        salleSelectionnee = salle;
                        txtNom.setText(salle.getNom());
                        txtCapacite.setText(String.valueOf(salle.getCapacite()));
                        txtEquipements.setText(salle.getEquipements());
                        break;
                    }
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Erreur : " + e.getMessage());
            }
        }
    }

    private void ajouterSalle() {
        try {
            String nom = txtNom.getText().trim();
            int capacite = Integer.parseInt(txtCapacite.getText().trim());
            String equipements = txtEquipements.getText().trim();

            if (nom.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Le nom est obligatoire.");
                return;
            }

            Room salle = new Room(nom, capacite, equipements);
            if (salle.save()) {
                JOptionPane.showMessageDialog(this, "Salle ajoutée avec succès.");
                chargerSalles();
                viderFormulaire();
            } else {
                JOptionPane.showMessageDialog(this, "Erreur lors de l'ajout.");
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Capacité doit être un nombre.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erreur : " + e.getMessage());
        }
    }

    private void modifierSalle() {
        if (salleSelectionnee == null) {
            JOptionPane.showMessageDialog(this, "Sélectionnez une salle à modifier.");
            return;
        }
        try {
            String nom = txtNom.getText().trim();
            int capacite = Integer.parseInt(txtCapacite.getText().trim());
            String equipements = txtEquipements.getText().trim();

            if (nom.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Le nom est obligatoire.");
                return;
            }

            salleSelectionnee.setNom(nom);
            salleSelectionnee.setCapacite(capacite);
            salleSelectionnee.setEquipements(equipements);

            if (salleSelectionnee.update()) {
                JOptionPane.showMessageDialog(this, "Salle modifiée avec succès.");
                chargerSalles();
            } else {
                JOptionPane.showMessageDialog(this, "Erreur lors de la modification.");
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Capacité doit être un nombre.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erreur : " + e.getMessage());
        }
    }

    private void supprimerSalle() {
        if (salleSelectionnee == null) {
            JOptionPane.showMessageDialog(this, "Sélectionnez une salle à supprimer.");
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this, "Confirmer la suppression de " + salleSelectionnee.getNom() + " ?",
                                                    "Confirmation", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            if (salleSelectionnee.delete()) {
                JOptionPane.showMessageDialog(this, "Salle supprimée avec succès.");
                chargerSalles();
                viderFormulaire();
            } else {
                JOptionPane.showMessageDialog(this, "Erreur lors de la suppression.");
            }
        }
    }

    private void viderFormulaire() {
        txtNom.setText("");
        txtCapacite.setText("");
        txtEquipements.setText("");
        salleSelectionnee = null;
        table.clearSelection();
    }
}
