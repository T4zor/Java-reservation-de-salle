package view;

import controller.MainController;
import javax.swing.*;

@SuppressWarnings("serial")
public class ReservationRequestView extends JFrame {
    private static final long serialVersionUID = 1L;

    @SuppressWarnings("unused")
    private final MainController controller;

    public ReservationRequestView(MainController controller) {
        this.controller = controller;
        setTitle("Demande de réservation");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        //icone
        try {
            setIconImage(new ImageIcon("icon/accueil.png").getImage());
        } catch (Exception ex) {
            System.out.println("Icône non trouvée ou non supportée.");
        }

        JLabel label = new JLabel(
            "Vue en cours de développement...", JLabel.CENTER);
        add(label);
    }
}
