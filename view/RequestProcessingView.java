package view;

import controller.MainController;
import javax.swing.*;

public class RequestProcessingView extends JFrame {

    private MainController controller;

    public RequestProcessingView(MainController controller) {
        this.controller = controller;
        setTitle("Traitement des demandes");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JLabel label = new JLabel(
            "Vue en cours de développement...", JLabel.CENTER);
        add(label);
    }
}