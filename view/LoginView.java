package view;

import controller.MainController;
import java.awt.*;
import javax.swing.*;
import model.User;

public class LoginView extends JFrame {

    private MainController controller;

    public LoginView(MainController controller) {
        this.controller = controller;
        setTitle("Connexion");
        setSize(420, 480);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        // ── Icône ────────────────────────────────────────
        try {
            setIconImage(new ImageIcon("icon/accueil.png").getImage());
        } catch (Exception ex) {
            System.out.println("Icône non trouvée.");
        }

        // ── Titre ─────────────────────────────────────────
        JLabel titre = new JLabel("Bienvenue!");
        titre.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titre.setForeground(new Color(41, 128, 185));
        titre.setAlignmentX(CENTER_ALIGNMENT);
        panel.add(titre);
        panel.add(Box.createVerticalStrut(5));

        JLabel sousTitre = new JLabel("Système de réservation de salles");
        sousTitre.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        sousTitre.setForeground(Color.GRAY);
        sousTitre.setAlignmentX(CENTER_ALIGNMENT);
        panel.add(sousTitre);
        panel.add(Box.createVerticalStrut(20));

        // ── Séparateur ────────────────────────────────────
        JSeparator sep1 = new JSeparator();
        sep1.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        panel.add(sep1);
        panel.add(Box.createVerticalStrut(15));

        // ── Formulaire de connexion ───────────────────────
        JLabel lblEmail = new JLabel("Adresse email :");
        lblEmail.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblEmail.setAlignmentX(LEFT_ALIGNMENT);
        panel.add(lblEmail);
        panel.add(Box.createVerticalStrut(4));

        JTextField txtEmail = new JTextField();
        txtEmail.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        txtEmail.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtEmail.setAlignmentX(LEFT_ALIGNMENT);
        panel.add(txtEmail);
        panel.add(Box.createVerticalStrut(10));

        JLabel lblMsg = new JLabel(" ");
        lblMsg.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        lblMsg.setForeground(Color.RED);
        lblMsg.setAlignmentX(CENTER_ALIGNMENT);

        JButton btnConnexion = new JButton("Se connecter");
        btnConnexion.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        btnConnexion.setBackground(new Color(41, 128, 185));
        btnConnexion.setForeground(Color.WHITE);
        btnConnexion.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnConnexion.setFocusPainted(false);
        btnConnexion.setBorderPainted(false);
        btnConnexion.setAlignmentX(LEFT_ALIGNMENT);
        btnConnexion.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Action connexion → vérifie en BD
        btnConnexion.addActionListener(e -> {
            String email = txtEmail.getText().trim();
            if (email.isEmpty()) {
                lblMsg.setText("Veuillez entrer votre email.");
                return;
            }
            User user = controller.authentifier(email);
            if (user != null) {
                controller.setUtilisateurConnecte(user);
                lblMsg.setForeground(new Color(39, 174, 96));
                lblMsg.setText("Connecté : " + user.getNomComplet()
                               + " (" + user.getRole() + ")");
            } else {
                lblMsg.setForeground(Color.RED);
                lblMsg.setText("Email introuvable. Vérifiez vos informations.");
            }
        });

        panel.add(btnConnexion);
        panel.add(Box.createVerticalStrut(6));
        panel.add(lblMsg);
        panel.add(Box.createVerticalStrut(15));

        // ── Séparateur ────────────────────────────────────
        JSeparator sep2 = new JSeparator();
        sep2.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        panel.add(sep2);
        panel.add(Box.createVerticalStrut(15));

        // ── Navigation squelette (code de ton camarade) ───
        JLabel lblNav = new JLabel("Navigation squelette :");
        lblNav.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblNav.setForeground(Color.GRAY);
        lblNav.setAlignmentX(CENTER_ALIGNMENT);
        panel.add(lblNav);
        panel.add(Box.createVerticalStrut(10));

        Dimension btnSize = new Dimension(Integer.MAX_VALUE, 35);

        JButton btnReservation = new JButton("Demande de réservation");
        btnReservation.setMaximumSize(btnSize);
        btnReservation.setAlignmentX(CENTER_ALIGNMENT);
        btnReservation.addActionListener(
            e -> controller.showReservationRequestView());
        panel.add(btnReservation);
        panel.add(Box.createVerticalStrut(6));

        JButton btnStatus = new JButton("Statut des réservations");
        btnStatus.setMaximumSize(btnSize);
        btnStatus.setAlignmentX(CENTER_ALIGNMENT);
        btnStatus.addActionListener(
            e -> controller.showReservationStatusView());
        panel.add(btnStatus);
        panel.add(Box.createVerticalStrut(6));

        JButton btnRoom = new JButton("Gestion des salles");
        btnRoom.setMaximumSize(btnSize);
        btnRoom.setAlignmentX(CENTER_ALIGNMENT);
        btnRoom.addActionListener(
            e -> controller.showRoomManagementView());
        panel.add(btnRoom);
        panel.add(Box.createVerticalStrut(6));

        JButton btnProcess = new JButton("Traitement des demandes");
        btnProcess.setMaximumSize(btnSize);
        btnProcess.setAlignmentX(CENTER_ALIGNMENT);
        btnProcess.addActionListener(
            e -> controller.showRequestProcessingView());
        panel.add(btnProcess);
        panel.add(Box.createVerticalStrut(6));

        // ── Bouton gestion utilisateurs (NOUVEAU) ─────────
        JButton btnUsers = new JButton("Gestion des utilisateurs");
        btnUsers.setMaximumSize(btnSize);
        btnUsers.setAlignmentX(CENTER_ALIGNMENT);
        btnUsers.setBackground(new Color(142, 68, 173));
        btnUsers.setForeground(Color.WHITE);
        btnUsers.setFocusPainted(false);
        btnUsers.setBorderPainted(false);
        btnUsers.addActionListener(
            e -> controller.showUserManagementView());
        panel.add(btnUsers);

        add(new JScrollPane(panel));
    }
}