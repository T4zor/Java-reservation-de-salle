package view;

import controller.MainController;
import javax.swing.*;

public class ReservationRequestView extends JFrame {

    private MainController controller;

    public ReservationRequestView(MainController controller) {
        this.controller = controller;
        setTitle("Demande de réservation");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JLabel label = new JLabel(
            "Vue en cours de développement...", JLabel.CENTER);
        add(label);
    }
}