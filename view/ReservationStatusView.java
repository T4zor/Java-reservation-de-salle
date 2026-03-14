package view;

import controller.MainController;
import javax.swing.*;

public class ReservationStatusView extends JFrame {

    private MainController controller;

    public ReservationStatusView(MainController controller) {
        this.controller = controller;
        setTitle("Statut des réservations");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JLabel label = new JLabel(
            "Vue en cours de développement...", JLabel.CENTER);
        add(label);
    }
}