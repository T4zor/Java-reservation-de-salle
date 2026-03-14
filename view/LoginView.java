package view;

import controller.MainController;
import javax.swing.*;

public class LoginView extends JFrame {
    private MainController controller;

    public LoginView(MainController controller) {
        this.controller = controller;
        setTitle("Connexion");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel label = new JLabel("Bienvenue! Navigation squelette :");
        label.setAlignmentX(CENTER_ALIGNMENT);
        panel.add(Box.createVerticalStrut(20));
        panel.add(label);
        panel.add(Box.createVerticalStrut(20));

        JButton btnReservation = new JButton("Demande de réservation");
        btnReservation.setAlignmentX(CENTER_ALIGNMENT);
        btnReservation.addActionListener(e -> controller.showReservationRequestView());
        panel.add(btnReservation);
        panel.add(Box.createVerticalStrut(10));

        JButton btnStatus = new JButton("Statut des réservations");
        btnStatus.setAlignmentX(CENTER_ALIGNMENT);
        btnStatus.addActionListener(e -> controller.showReservationStatusView());
        panel.add(btnStatus);
        panel.add(Box.createVerticalStrut(10));

        JButton btnRoom = new JButton("Gestion des salles");
        btnRoom.setAlignmentX(CENTER_ALIGNMENT);
        btnRoom.addActionListener(e -> controller.showRoomManagementView());
        panel.add(btnRoom);
        panel.add(Box.createVerticalStrut(10));

        JButton btnProcess = new JButton("Traitement des demandes");
        btnProcess.setAlignmentX(CENTER_ALIGNMENT);
        btnProcess.addActionListener(e -> controller.showRequestProcessingView());
        panel.add(btnProcess);
        panel.add(Box.createVerticalStrut(20));

        add(panel);

        // Préparation pour l'icône
        try {
            setIconImage(new ImageIcon("icon/accueil.png").getImage());
        } catch (Exception ex) {
            System.out.println("Icône non trouvée ou non supportée.");
        }
    }
}