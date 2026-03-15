package view;

import controller.MainController;
import model.Reservation;
import model.User;
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ReservationStatusView extends JFrame {
    private MainController controller;

    public ReservationStatusView(MainController controller) {
        this.controller = controller;
        setTitle("Statut des réservations");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Layout
        setLayout(new BorderLayout());

        // Titre
        JLabel lblTitre = new JLabel("Statut des réservations", JLabel.CENTER);
        lblTitre.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 18));
        add(lblTitre, BorderLayout.NORTH);

        // Contenu (table des réservations)
        List<Reservation> reservations = Reservation.findAll();
        if (!controller.isResponsable()) {
            reservations = reservations.stream()
                .filter(r -> r.getIdUser() == controller.getUtilisateurConnecte().getId())
                .toList();
        }

        String[] columnNames = {"ID", "Utilisateur", "Salle", "Début", "Fin", "Statut", "Motif"};
        Object[][] data = new Object[reservations.size()][7];
        for (int i = 0; i < reservations.size(); i++) {
            Reservation r = reservations.get(i);
            data[i][0] = r.getId();
            data[i][1] = r.getNomUser();
            data[i][2] = r.getNomSalle();
            data[i][3] = r.getDateDebutFormatee();
            data[i][4] = r.getDateFinFormatee();
            data[i][5] = r.getStatut();
            data[i][6] = r.getMotif();
        }

        JTable table = new JTable(data, columnNames);
        table.setFillsViewportHeight(true);
        table.setAutoCreateRowSorter(true);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

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
}
