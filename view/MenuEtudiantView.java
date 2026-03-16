package view;

import controller.MainController;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

@SuppressWarnings("serial")
public class MenuEtudiantView extends JFrame {
    private static final long serialVersionUID = 1L;

    private final MainController controller;

    public MenuEtudiantView(MainController controller) {
        this.controller = controller;
        // Appliquer le thème moderne
        StyleUtils.applyModernStyle(this);
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Menu Étudiant - Système de Réservation de Salles");
        setSize(550, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Icône
        try {
            setIconImage(new ImageIcon("icon/accueil.png").getImage());
        } catch (Exception ex) {
            System.out.println("Icône non trouvée ou non supportée.");
        }

        // Layout principal
        setLayout(new BorderLayout());

        // Titre
        JLabel lblTitre = new JLabel("Menu Étudiant", JLabel.CENTER);
        StyleUtils.styleTitleLabel(lblTitre);
        add(lblTitre, BorderLayout.NORTH);

        // Panel central avec les boutons
        JPanel panelCentral = StyleUtils.createCardPanel();
        panelCentral.setLayout(new GridLayout(0, 1, 15, 15));

        // Informations utilisateur
        String userInfo = "Connecté en tant que : " + controller.getUtilisateurConnecte().getNomComplet();
        JLabel lblUser = new JLabel(userInfo, JLabel.CENTER);
        StyleUtils.styleBodyLabel(lblUser);
        lblUser.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        panelCentral.add(lblUser);

        // Séparateur
        panelCentral.add(new JSeparator());

        // Boutons pour les étudiants
        JButton btnFaireReservation = StyleUtils.createStyledButton("Faire une demande de réservation");
        btnFaireReservation.addActionListener(e -> {
            dispose();
            controller.showReservationRequestView();
        });
        panelCentral.add(btnFaireReservation);

        JButton btnVoirReservations = StyleUtils.createStyledButton("Voir mes réservations");
        btnVoirReservations.addActionListener(e -> {
            dispose();
            controller.showReservationStatusView();
        });
        panelCentral.add(btnVoirReservations);

        add(panelCentral, BorderLayout.CENTER);

        // Panel inférieur avec bouton déconnexion
        JPanel panelBas = new JPanel();
        panelBas.setBackground(StyleUtils.BACKGROUND_COLOR);
        JButton btnDeconnexion = StyleUtils.createSecondaryButton("Déconnexion");
        btnDeconnexion.addActionListener(e -> {
            dispose();
            controller.deconnecter();
        });
        panelBas.add(btnDeconnexion);
        add(panelBas, BorderLayout.SOUTH);
    }

    private JButton createStyledButton(String text) {
        return StyleUtils.createStyledButton(text);
    }
}