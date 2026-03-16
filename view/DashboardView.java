package view;

import controller.MainController;
import model.LogAction;
import model.Reservation;
import model.Room;
import model.User;
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class DashboardView extends JFrame {
    private MainController controller;

    public DashboardView(MainController controller) {
        this.controller = controller;
        StyleUtils.applyModernStyle(this);
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Dashboard - Système de Réservation de Salles");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Icône
        try {
            setIconImage(new ImageIcon("icon/accueil.png").getImage());
        } catch (Exception ex) {
            System.out.println("Icône non trouvée ou non supportée.");
        }

        setLayout(new BorderLayout());

        // Header avec titre et info utilisateur
        add(createHeader(), BorderLayout.NORTH);

        // Contenu principal
        add(createMainContent(), BorderLayout.CENTER);

        // Footer
        add(createFooter(), BorderLayout.SOUTH);
    }

    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(StyleUtils.PRIMARY_COLOR);
        header.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        // Titre
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        titlePanel.setOpaque(false);

        JLabel titleLabel = new JLabel("Système de Réservation");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        titlePanel.add(titleLabel);

        header.add(titlePanel, BorderLayout.WEST);

        // Infos utilisateur et déconnexion
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        rightPanel.setOpaque(false);

        String userName = controller.getUtilisateurConnecte().getNomComplet();
        String roleText = controller.isResponsable() ? "(Responsable)" : "(Étudiant)";

        JLabel userLabel = new JLabel("Bienvenue, " + userName + " " + roleText);
        userLabel.setFont(StyleUtils.BODY_FONT);
        userLabel.setForeground(Color.WHITE);
        rightPanel.add(userLabel);

        JButton btnLogout = StyleUtils.createSecondaryButton("Déconnexion");
        btnLogout.addActionListener(e -> {
            dispose();
            controller.deconnecter();
        });
        rightPanel.add(btnLogout);

        header.add(rightPanel, BorderLayout.EAST);

        return header;
    }

    private JPanel createMainContent() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(StyleUtils.BACKGROUND_COLOR);

        // Section métriques
        mainPanel.add(createMetricsSection());

        // Section actions rapides
        mainPanel.add(createQuickActionsSection());

        return mainPanel;
    }

    private JPanel createMetricsSection() {
        JPanel section = new JPanel();
        section.setLayout(new BoxLayout(section, BoxLayout.Y_AXIS));
        section.setBackground(StyleUtils.BACKGROUND_COLOR);
        section.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));

        // Titre de section
        JLabel sectionTitle = new JLabel("Métriques");
        sectionTitle.setFont(StyleUtils.SUBTITLE_FONT);
        sectionTitle.setForeground(StyleUtils.TEXT_PRIMARY);
        sectionTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        section.add(sectionTitle);

        // Grille de cartes métriques
        JPanel metricsGrid = new JPanel(new GridLayout(1, 4, 15, 0));
        metricsGrid.setOpaque(false);

        // Calculer les métriques réelles
        int totalUsers = getTotalUsers();
        int totalRooms = getTotalRooms();
        int totalReservations = getTotalReservations();
        int pendingReservations = getPendingReservations();

        // Métriques selon le rôle
        if (controller.isResponsable()) {
            metricsGrid.add(createMetricCard("Utilisateurs", String.valueOf(totalUsers)));
            metricsGrid.add(createMetricCard("Salles", String.valueOf(totalRooms)));
            metricsGrid.add(createMetricCard("Réservations", String.valueOf(totalReservations)));
            metricsGrid.add(createMetricCard("En attente", String.valueOf(pendingReservations)));
        } else {
            int myReservations = getMyReservations();
            int myPendingReservations = getMyPendingReservations();
            metricsGrid.add(createMetricCard("Mes réservations", String.valueOf(myReservations)));
            metricsGrid.add(createMetricCard("En attente", String.valueOf(myPendingReservations)));
        }

        section.add(metricsGrid);
        return section;
    }

    private JPanel createMetricCard(String title, String value) {
        JPanel card = StyleUtils.createCardPanel();
        card.setLayout(new BorderLayout());
        card.setPreferredSize(new Dimension(180, 100));
        card.setMaximumSize(new Dimension(180, 100));

        // Contenu centré
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setOpaque(false);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 15, 20, 15));

        // Valeur centrée
        JPanel valuePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        valuePanel.setOpaque(false);
        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        valueLabel.setForeground(StyleUtils.PRIMARY_COLOR);
        valuePanel.add(valueLabel);
        contentPanel.add(valuePanel);

        contentPanel.add(Box.createVerticalStrut(10));

        // Titre centré
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        titlePanel.setOpaque(false);
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(StyleUtils.BODY_FONT);
        titleLabel.setForeground(StyleUtils.TEXT_SECONDARY);
        titlePanel.add(titleLabel);
        contentPanel.add(titlePanel);

        card.add(contentPanel, BorderLayout.CENTER);

        return card;
    }

    private JPanel createQuickActionsSection() {
        JPanel section = new JPanel();
        section.setLayout(new BoxLayout(section, BoxLayout.Y_AXIS));
        section.setBackground(StyleUtils.BACKGROUND_COLOR);
        section.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Titre de section
        JLabel sectionTitle = new JLabel("Actions Rapides");
        sectionTitle.setFont(StyleUtils.SUBTITLE_FONT);
        sectionTitle.setForeground(StyleUtils.TEXT_PRIMARY);
        sectionTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        section.add(sectionTitle);

        // Grille d'actions
        JPanel actionsGrid = new JPanel(new GridLayout(0, 3, 15, 15));
        actionsGrid.setOpaque(false);

        if (controller.isResponsable()) {
            actionsGrid.add(createActionCard("Nouvelle Réservation", e -> {
                dispose();
                controller.showReservationRequestView();
            }));

            actionsGrid.add(createActionCard("Gérer Utilisateurs", e -> {
                dispose();
                controller.showUserManagementView();
            }));

            actionsGrid.add(createActionCard("Gérer Salles", e -> {
                dispose();
                controller.showRoomManagementView();
            }));

            actionsGrid.add(createActionCard("Traiter Demandes", e -> {
                dispose();
                controller.showRequestProcessingView();
            }));

            actionsGrid.add(createActionCard("Voir Historique", e -> {
                dispose();
                controller.showLogHistoryView();
            }));

            actionsGrid.add(createActionCard("Statistiques", e -> {
                dispose();
                controller.showStatisticsView();
            }));
        } else {
            actionsGrid.add(createActionCard("Faire une Demande", e -> {
                dispose();
                controller.showReservationRequestView();
            }));

            actionsGrid.add(createActionCard("Mes Réservations", e -> {
                dispose();
                controller.showReservationStatusView();
            }));

            actionsGrid.add(createActionCard("Aide", e -> {
                JOptionPane.showMessageDialog(this, "Bienvenue dans le système de réservation !\n\n• Utilisez 'Faire une Demande' pour réserver\n• 'Mes Réservations' pour suivre vos demandes", "Aide", JOptionPane.INFORMATION_MESSAGE);
            }));
        }

        section.add(actionsGrid);
        return section;
    }

    private JPanel createActionCard(String title, java.awt.event.ActionListener action) {
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(224, 224, 224), 1),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        card.setPreferredSize(new Dimension(160, 80));
        card.setMaximumSize(new Dimension(160, 80));
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Titre centré
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(StyleUtils.BUTTON_FONT);
        titleLabel.setForeground(StyleUtils.TEXT_PRIMARY);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        card.add(titleLabel, BorderLayout.CENTER);

        // Effet hover
        card.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                card.setBackground(new Color(245, 245, 245));
                card.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(StyleUtils.PRIMARY_COLOR, 2),
                    BorderFactory.createEmptyBorder(14, 14, 14, 14)
                ));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                card.setBackground(Color.WHITE);
                card.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(224, 224, 224), 1),
                    BorderFactory.createEmptyBorder(15, 15, 15, 15)
                ));
            }
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                action.actionPerformed(null);
            }
        });

        return card;
    }

    private JPanel createFooter() {
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.CENTER));
        footer.setBackground(StyleUtils.BACKGROUND_COLOR);
        footer.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JLabel footerLabel = new JLabel("Système de Gestion des Réservations de Salles - 2026");
        footerLabel.setFont(StyleUtils.SMALL_FONT);
        footerLabel.setForeground(StyleUtils.TEXT_SECONDARY);
        footer.add(footerLabel);

        return footer;
    }

    // Méthodes de calcul des métriques
    private int getTotalUsers() {
        try {
            return controller.getAllUsers().size();
        } catch (Exception e) {
            return 0;
        }
    }

    private int getTotalRooms() {
        try {
            return Room.findAll().size();
        } catch (Exception e) {
            return 0;
        }
    }

    private int getTotalReservations() {
        try {
            return Reservation.findAll().size();
        } catch (Exception e) {
            return 0;
        }
    }

    private int getPendingReservations() {
        try {
            return (int) Reservation.findAll().stream()
                .filter(r -> Reservation.STATUT_ATTENTE.equals(r.getStatut()))
                .count();
        } catch (Exception e) {
            return 0;
        }
    }

    private int getMyReservations() {
        try {
            return (int) Reservation.findAll().stream()
                .filter(r -> r.getIdUser() == controller.getUtilisateurConnecte().getId())
                .count();
        } catch (Exception e) {
            return 0;
        }
    }

    private int getMyPendingReservations() {
        try {
            return (int) Reservation.findAll().stream()
                .filter(r -> r.getIdUser() == controller.getUtilisateurConnecte().getId()
                        && Reservation.STATUT_ATTENTE.equals(r.getStatut()))
                .count();
        } catch (Exception e) {
            return 0;
        }
    }
}