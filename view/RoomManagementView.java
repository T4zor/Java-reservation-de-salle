package view;

import javax.swing.*;

public class RoomManagementView extends JFrame {
    public RoomManagementView() {
        setTitle("Gestion des salles");
        setSize(500, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        // Ajouter ici les composants (ajout, modification, suppression...)
         //icone
        try {
            setIconImage(new ImageIcon("icon/accueil.png").getImage());
        } catch (Exception ex) {
            System.out.println("Icône non trouvée ou non supportée.");
        }
    }
}
