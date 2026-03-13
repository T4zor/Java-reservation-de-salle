package view;

import javax.swing.*;

public class ReservationStatusView extends JFrame {
    public ReservationStatusView() {
        setTitle("Statut des réservations");
        setSize(500, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        // Ajouter ici les composants (liste des statuts...)

         //icone
        try {
            setIconImage(new ImageIcon("icon/accueil.png").getImage());
        } catch (Exception ex) {
            System.out.println("Icône non trouvée ou non supportée.");
        }
    }
}
