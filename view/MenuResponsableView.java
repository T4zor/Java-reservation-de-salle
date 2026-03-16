package view;

import controller.MainController;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

@SuppressWarnings("serial")
public class MenuResponsableView extends JFrame {
    private static final long serialVersionUID = 1L;

    private final MainController controller;

    public MenuResponsableView(MainController controller) {
        this.controller = controller;
        // Appliquer le thème moderne
        StyleUtils.applyModernStyle(this);
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Menu Responsable - Système de Réservation de Salles");
        setSize(550, 500);
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
        JLabel lblTitre = new JLabel("Menu Responsable", JLabel.CENTER);
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

        // Boutons du menu responsable
        JButton btnGestionUtilisateurs = StyleUtils.createStyledButton("Gestion des Utilisateurs");
        btnGestionUtilisateurs.addActionListener(e -> {
            dispose();
            controller.showUserManagementView();
        });
        panelCentral.add(btnGestionUtilisateurs);

        JButton btnGestionSalles = StyleUtils.createStyledButton("Gestion des Salles");
        btnGestionSalles.addActionListener(e -> {
            dispose();
            controller.showRoomManagementView();
        });
        panelCentral.add(btnGestionSalles);

        JButton btnTraiterDemandes = StyleUtils.createStyledButton("Traiter les Demandes");
        btnTraiterDemandes.addActionListener(e -> {
            dispose();
            controller.showRequestProcessingView();
        });
        panelCentral.add(btnTraiterDemandes);

        JButton btnVoirReservations = StyleUtils.createStyledButton("Voir Toutes les Réservations");
        btnVoirReservations.addActionListener(e -> {
            dispose();
            controller.showReservationStatusView();
        });
        panelCentral.add(btnVoirReservations);

        JButton btnVoirHistorique = StyleUtils.createStyledButton("Voir l'Historique des Actions");
        btnVoirHistorique.addActionListener(e -> {
            dispose();
            controller.showLogHistoryView();
        });
        panelCentral.add(btnVoirHistorique);

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