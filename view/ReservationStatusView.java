package view;

import controller.MainController;
import model.Reservation;
import model.User;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ReservationStatusView extends JFrame {
    private MainController controller;
    private JTable table;
    private DefaultTableModel tableModel;
    private List<Reservation> allReservations;
    private JTextField txtSearch;

    public ReservationStatusView(MainController controller) {
        this.controller = controller;
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Statut des réservations");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Layout
        setLayout(new BorderLayout());

        // Titre et recherche
        JPanel topPanel = new JPanel(new BorderLayout());
        JLabel lblTitre = new JLabel("Statut des réservations", JLabel.CENTER);
        lblTitre.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 18));
        topPanel.add(lblTitre, BorderLayout.NORTH);

        // Barre de recherche
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.add(new JLabel("Rechercher :"));
        txtSearch = new JTextField(20);
        searchPanel.add(txtSearch);
        JButton btnSearch = new JButton("Rechercher");
        searchPanel.add(btnSearch);
        JButton btnClear = new JButton("Effacer");
        searchPanel.add(btnClear);
        topPanel.add(searchPanel, BorderLayout.SOUTH);

        add(topPanel, BorderLayout.NORTH);

        // Contenu (table des réservations)
        allReservations = Reservation.findAll();
        if (!controller.isResponsable()) {
            allReservations = allReservations.stream()
                .filter(r -> r.getIdUser() == controller.getUtilisateurConnecte().getId())
                .toList();
        }

        String[] columnNames = {"ID", "Utilisateur", "Salle", "Début", "Fin", "Statut", "Motif"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        loadTableData(allReservations);

        table = new JTable(tableModel);
        table.setFillsViewportHeight(true);
        table.setAutoCreateRowSorter(true);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // Événements de recherche
        btnSearch.addActionListener(e -> filterTable(txtSearch.getText()));
        btnClear.addActionListener(e -> {
            txtSearch.setText("");
            loadTableData(allReservations);
        });

        // Recherche en temps réel
        txtSearch.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                filterTable(txtSearch.getText());
            }
        });

        // Boutons
        JPanel panelBoutons = new JPanel();
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
        panelBoutons.add(btnRetour);
        panelBoutons.add(btnDeconnexion);
        add(panelBoutons, BorderLayout.SOUTH);

        // Icône
        try {
            setIconImage(new ImageIcon("icon/accueil.png").getImage());
        } catch (Exception ex) {
            System.out.println("Icône non trouvée ou non supportée.");
        }
    }

    private void loadTableData(List<Reservation> reservations) {
        tableModel.setRowCount(0);
        for (Reservation r : reservations) {
            Object[] row = {
                r.getId(),
                r.getNomUser(),
                r.getNomSalle(),
                r.getDateDebutFormatee(),
                r.getDateFinFormatee(),
                r.getStatut(),
                r.getMotif()
            };
            tableModel.addRow(row);
        }
    }

    private void filterTable(String searchText) {
        if (searchText == null || searchText.trim().isEmpty()) {
            loadTableData(allReservations);
            return;
        }

        List<Reservation> filtered = allReservations.stream()
            .filter(r -> r.getNomUser().toLowerCase().contains(searchText.toLowerCase()) ||
                        r.getNomSalle().toLowerCase().contains(searchText.toLowerCase()) ||
                        r.getStatut().toLowerCase().contains(searchText.toLowerCase()) ||
                        (r.getMotif() != null && r.getMotif().toLowerCase().contains(searchText.toLowerCase())))
            .toList();

        loadTableData(filtered);
    }
}
