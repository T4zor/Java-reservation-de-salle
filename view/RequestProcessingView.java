package view;

import javax.swing.*;

public class RequestProcessingView extends JFrame {
    public RequestProcessingView() {
        setTitle("Traitement des demandes");
        setSize(500, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        // Ajouter ici les composants (accepter/refuser...)

        // icone
        try {
            setIconImage(new ImageIcon("icon/accueil.png").getImage());
        } catch (Exception ex) {
            System.out.println("Icône non trouvée ou non supportée.");
        }
    }
}
