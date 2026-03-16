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
        StyleUtils.applyModernStyle(this);
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Statut des réservations");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Layout
        setLayout(new BorderLayout());

        // Titre avec icône
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        titlePanel.setBackground(StyleUtils.BACKGROUND_COLOR);
        titlePanel.setBorder(BorderFactory.createEmptyBorder(20, 10, 10, 10));

        JLabel iconLabel = StyleUtils.createIconLabel("📋");
        titlePanel.add(iconLabel);

        JLabel lblTitre = new JLabel("Statut des réservations");
        StyleUtils.styleTitleLabel(lblTitre);
        lblTitre.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
        titlePanel.add(lblTitre);

        // Titre et recherche
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(titlePanel, BorderLayout.NORTH);

        // Barre de recherche stylisée
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        searchPanel.setBackground(StyleUtils.BACKGROUND_COLOR);
        searchPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        searchPanel.add(new JLabel("Rechercher :"));
        txtSearch = StyleUtils.createStyledTextField();
        txtSearch.setPreferredSize(new Dimension(200, 30));
        searchPanel.add(txtSearch);

        JButton btnSearch = StyleUtils.createStyledButton("Rechercher");
        searchPanel.add(btnSearch);

        JButton btnClear = StyleUtils.createSecondaryButton("Effacer");
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

        // Boutons stylisés
        JPanel panelBoutons = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        panelBoutons.setBackground(StyleUtils.BACKGROUND_COLOR);
        panelBoutons.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));

        JButton btnRetour = StyleUtils.createSecondaryButton("Retour au menu");
        btnRetour.addActionListener(e -> {
            dispose();
            controller.showMenuPrincipal();
        });

        JButton btnDeconnexion = StyleUtils.createDangerButton("Déconnexion");
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
