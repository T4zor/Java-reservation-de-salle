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
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Menu Étudiant - Système de Réservation de Salles");
        setSize(500, 350);
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
        lblTitre.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitre.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        add(lblTitre, BorderLayout.NORTH);

        // Panel central avec les boutons
        JPanel panelCentral = new JPanel();
        panelCentral.setLayout(new GridLayout(0, 1, 10, 10));
        panelCentral.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        // Informations utilisateur
        String userInfo = "Connecté en tant que : " + controller.getUtilisateurConnecte().getNomComplet();
        JLabel lblUser = new JLabel(userInfo, JLabel.CENTER);
        lblUser.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        panelCentral.add(lblUser);

        // Séparateur
        panelCentral.add(new JSeparator());

        // Boutons pour les étudiants
        JButton btnFaireReservation = createStyledButton("Faire une demande de réservation");
        btnFaireReservation.addActionListener(e -> {
            dispose();
            controller.showReservationRequestView();
        });
        panelCentral.add(btnFaireReservation);

        JButton btnVoirReservations = createStyledButton("Voir mes réservations");
        btnVoirReservations.addActionListener(e -> {
            dispose();
            controller.showReservationStatusView();
        });
        panelCentral.add(btnVoirReservations);

        add(panelCentral, BorderLayout.CENTER);

        // Panel inférieur avec bouton déconnexion
        JPanel panelBas = new JPanel();
        JButton btnDeconnexion = new JButton("Déconnexion");
        btnDeconnexion.addActionListener(e -> {
            dispose();
            controller.deconnecter();
        });
        panelBas.add(btnDeconnexion);
        add(panelBas, BorderLayout.SOUTH);
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        button.setPreferredSize(new Dimension(300, 40));
        button.setFocusPainted(false);
        return button;
    }
}