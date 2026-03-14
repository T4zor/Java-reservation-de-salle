package view;

import controller.MainController;
import model.Reservation;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class RequestProcessingView extends JFrame {

    private MainController controller;
    private DefaultTableModel tableModel;
    private JTable table;

    // =========================================================
    // CONSTRUCTEUR
    // =========================================================

    public RequestProcessingView(
            MainController controller) {
        this.controller = controller;

        setTitle("Traitement des demandes");
        setSize(850, 480);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(
            JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // ── Titre ─────────────────────────────────────────
        JPanel topBar = new JPanel(
            new BorderLayout());
        topBar.setBackground(
            new Color(243, 156, 18));
        topBar.setBorder(
            BorderFactory.createEmptyBorder(
                12, 18, 12, 18));
        JLabel titre = new JLabel(
            "Traitement des demandes");
        titre.setFont(
            new Font("Segoe UI", Font.BOLD, 16));
        titre.setForeground(Color.WHITE);
        topBar.add(titre, BorderLayout.WEST);
        add(topBar, BorderLayout.NORTH);

        // ── Tableau ───────────────────────────────────────
        String[] cols = {
            "ID", "Salle", "Demandeur",
            "Date debut", "Date fin", "Statut"
        };
        tableModel = new DefaultTableModel(
            cols, 0) {
            public boolean isCellEditable(
                    int r, int c) {
                return false;
            }
        };

        table = new JTable(tableModel);
        table.setRowHeight(26);
        table.setFont(
            new Font("Segoe UI", Font.PLAIN, 12));
        table.getTableHeader().setFont(
            new Font("Segoe UI", Font.BOLD, 12));
        table.setSelectionMode(
            ListSelectionModel.SINGLE_SELECTION);
        add(new JScrollPane(table),
            BorderLayout.CENTER);

        // ── Boutons bas ───────────────────────────────────
        JPanel bas = new JPanel(
            new FlowLayout(
                FlowLayout.CENTER, 15, 10));
        bas.setBackground(
            new Color(245, 246, 250));

        JButton btnAccepter =
            bouton("Accepter",
                new Color(39, 174, 96));
        JButton btnRefuser =
            bouton("Refuser",
                new Color(192, 57, 43));
        JButton btnRetour =
            bouton("Retour",
                new Color(52, 73, 94));

        btnAccepter.addActionListener(
            e -> traiterDemande("acceptee"));
        btnRefuser.addActionListener(
            e -> traiterDemande("refusee"));
        btnRetour.addActionListener(e -> {
            dispose();
            controller.showLoginView();
        });

        bas.add(btnAccepter);
        bas.add(btnRefuser);
        bas.add(btnRetour);
        add(bas, BorderLayout.SOUTH);

        chargerDemandes();
    }

    // =========================================================
    // CHARGER DEMANDES EN ATTENTE
    // =========================================================

    private void chargerDemandes() {
        tableModel.setRowCount(0);

        String sql =
            "SELECT r.id, s.nom AS salle_nom, "
            + "u.nom || ' ' || u.prenom "
            + "AS demandeur, "
            + "r.date_debut, r.date_fin, "
            + "r.statut "
            + "FROM Reservation r "
            + "JOIN Salle s ON r.id_salle = s.id "
            + "JOIN User u ON r.id_user = u.id "
            + "WHERE r.statut = 'en attente' "
            + "ORDER BY r.date_debut";

        try {
            java.sql.Statement st =
                model.DatabaseManager
                     .getConnection()
                     .createStatement();
            java.sql.ResultSet rs =
                st.executeQuery(sql);

            while (rs.next()) {
                tableModel.addRow(new Object[]{
                    rs.getInt("id"),
                    rs.getString("salle_nom"),
                    rs.getString("demandeur"),
                    rs.getString("date_debut"),
                    rs.getString("date_fin"),
                    rs.getString("statut")
                });
            }

            if (tableModel.getRowCount() == 0) {
                JOptionPane.showMessageDialog(
                    this,
                    "Aucune demande en attente.",
                    "Information",
                    JOptionPane
                        .INFORMATION_MESSAGE);
            }

        } catch (java.sql.SQLException e) {
            System.err.println(
                "Erreur chargement demandes : "
                + e.getMessage());
        }
    }

    // =========================================================
    // TRAITER UNE DEMANDE
    // =========================================================

    private void traiterDemande(String statut) {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this,
                "Selectionnez une demande.");
            return;
        }

        int id = (int) tableModel
            .getValueAt(row, 0);
        String action = statut.equals("acceptee")
            ? "accepter" : "refuser";

        int choix = JOptionPane
            .showConfirmDialog(this,
                "Voulez-vous " + action
                + " cette demande ?",
                "Confirmation",
                JOptionPane.YES_NO_OPTION);

        if (choix == JOptionPane.YES_OPTION) {
            String sql =
                "UPDATE Reservation "
                + "SET statut = ? "
                + "WHERE id = ?";
            try {
                java.sql.PreparedStatement ps =
                    model.DatabaseManager
                         .getConnection()
                         .prepareStatement(sql);
                ps.setString(1, statut);
                ps.setInt(2, id);

                if (ps.executeUpdate() > 0) {
                    JOptionPane
                        .showMessageDialog(this,
                            "Demande "
                            + action
                            + "e avec succes !");
                    chargerDemandes();
                }
            } catch (java.sql.SQLException e) {
                System.err.println(
                    "Erreur traitement : "
                    + e.getMessage());
            }
        }
    }

    // =========================================================
    // HELPER
    // =========================================================

    private JButton bouton(String texte,
                            Color couleur) {
        JButton b = new JButton(texte);
        b.setBackground(couleur);
        b.setForeground(Color.WHITE);
        b.setFont(
            new Font("Segoe UI", Font.BOLD, 13));
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setCursor(
            new Cursor(Cursor.HAND_CURSOR));
        b.setPreferredSize(
            new Dimension(130, 38));
        return b;
    }
}