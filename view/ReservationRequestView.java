package view;

import javax.swing.*;

public class ReservationRequestView extends JFrame {
    public ReservationRequestView() {
        setTitle("Demande de réservation");
        setSize(500, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        // Ajouter ici les composants (formulaire de demande...)

        //icone
        try {
            setIconImage(new ImageIcon("icon/accueil.png").getImage());
        } catch (Exception ex) {
            System.out.println("Icône non trouvée ou non supportée.");
        }
    }
}
