package view;

import controller.MainController;
import javax.swing.*;

@SuppressWarnings("serial")
public class ReservationStatusView extends JFrame {
    private static final long serialVersionUID = 1L;

    @SuppressWarnings("unused")
    private final MainController controller;

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
