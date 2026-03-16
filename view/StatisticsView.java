package view;

import controller.MainController;
import model.Reservation;
import model.Room;
import model.User;
import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class StatisticsView extends JFrame {
    private MainController controller;

    public StatisticsView(MainController controller) {
        this.controller = controller;
        StyleUtils.applyModernStyle(this);
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Statistiques - Système de Réservation de Salles");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Icône
        try {
            setIconImage(new ImageIcon("icon/accueil.png").getImage());
        } catch (Exception ex) {
            System.out.println("Icône non trouvée ou non supportée.");
        }

        setLayout(new BorderLayout());

        // Header
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

        JLabel titleLabel = new JLabel("Statistiques du Système");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        titlePanel.add(titleLabel);

        header.add(titlePanel, BorderLayout.WEST);

        // Bouton retour
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        rightPanel.setOpaque(false);

        JButton btnBack = StyleUtils.createSecondaryButton("Retour au menu");
        btnBack.addActionListener(e -> {
            dispose();
            controller.showDashboardView();
        });
        rightPanel.add(btnBack);

        header.add(rightPanel, BorderLayout.EAST);

        return header;
    }

    private JPanel createMainContent() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(StyleUtils.BACKGROUND_COLOR);

        // Section statistiques générales
        mainPanel.add(createGeneralStatsSection());

        // Section graphiques
        mainPanel.add(createChartsSection());

        return mainPanel;
    }

    private JPanel createGeneralStatsSection() {
        JPanel section = new JPanel();
        section.setLayout(new BoxLayout(section, BoxLayout.Y_AXIS));
        section.setBackground(StyleUtils.BACKGROUND_COLOR);
        section.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));

        // Titre de section
        JLabel sectionTitle = new JLabel("Statistiques Générales");
        sectionTitle.setFont(StyleUtils.SUBTITLE_FONT);
        sectionTitle.setForeground(StyleUtils.TEXT_PRIMARY);
        sectionTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        section.add(sectionTitle);

        // Grille de statistiques
        JPanel statsGrid = new JPanel(new GridLayout(2, 3, 15, 15));
        statsGrid.setOpaque(false);

        // Calculer les statistiques
        int totalUsers = getTotalUsers();
        int totalRooms = getTotalRooms();
        int totalReservations = getTotalReservations();
        int approvedReservations = getApprovedReservations();
        int pendingReservations = getPendingReservations();
        int rejectedReservations = getRejectedReservations();

        statsGrid.add(createStatCard("Total Utilisateurs", String.valueOf(totalUsers), StyleUtils.PRIMARY_COLOR));
        statsGrid.add(createStatCard("Total Salles", String.valueOf(totalRooms), StyleUtils.SUCCESS_COLOR));
        statsGrid.add(createStatCard("Total Réservations", String.valueOf(totalReservations), StyleUtils.INFO_COLOR));
        statsGrid.add(createStatCard("Approuvées", String.valueOf(approvedReservations), StyleUtils.SUCCESS_COLOR));
        statsGrid.add(createStatCard("En Attente", String.valueOf(pendingReservations), StyleUtils.WARNING_COLOR));
        statsGrid.add(createStatCard("Rejetées", String.valueOf(rejectedReservations), StyleUtils.ERROR_COLOR));

        section.add(statsGrid);
        return section;
    }

    private JPanel createChartsSection() {
        JPanel section = new JPanel();
        section.setLayout(new BoxLayout(section, BoxLayout.Y_AXIS));
        section.setBackground(StyleUtils.BACKGROUND_COLOR);
        section.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Titre de section
        JLabel sectionTitle = new JLabel("Analyse Détaillée");
        sectionTitle.setFont(StyleUtils.SUBTITLE_FONT);
        sectionTitle.setForeground(StyleUtils.TEXT_PRIMARY);
        sectionTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        section.add(sectionTitle);

        // Grille de graphiques
        JPanel chartsGrid = new JPanel(new GridLayout(1, 2, 15, 0));
        chartsGrid.setOpaque(false);

        chartsGrid.add(createChartCard("Répartition par Salle", createRoomUsageChart()));
        chartsGrid.add(createChartCard("Évolution Mensuelle", createMonthlyTrendChart()));

        section.add(chartsGrid);
        return section;
    }

    private JPanel createStatCard(String title, String value, Color color) {
        JPanel card = StyleUtils.createCardPanel();
        card.setLayout(new BorderLayout());
        card.setPreferredSize(new Dimension(200, 100));
        card.setMaximumSize(new Dimension(200, 100));

        // Contenu centré
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setOpaque(false);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Valeur centrée
        JPanel valuePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        valuePanel.setOpaque(false);
        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        valueLabel.setForeground(color);
        valuePanel.add(valueLabel);
        contentPanel.add(valuePanel);

        contentPanel.add(Box.createVerticalStrut(5));

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

    private JPanel createChartCard(String title, JPanel chart) {
        JPanel card = StyleUtils.createCardPanel();
        card.setLayout(new BorderLayout());
        card.setPreferredSize(new Dimension(350, 250));

        // Titre
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        titlePanel.setOpaque(false);
        titlePanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 5, 15));
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(StyleUtils.SUBTITLE_FONT);
        titleLabel.setForeground(StyleUtils.TEXT_PRIMARY);
        titlePanel.add(titleLabel);
        card.add(titlePanel, BorderLayout.NORTH);

        // Contenu du graphique
        JPanel chartPanel = new JPanel(new BorderLayout());
        chartPanel.setOpaque(false);
        chartPanel.setBorder(BorderFactory.createEmptyBorder(5, 15, 15, 15));
        chartPanel.add(chart, BorderLayout.CENTER);
        card.add(chartPanel, BorderLayout.CENTER);

        return card;
    }

    private JPanel createRoomUsageChart() {
        JPanel chartPanel = new JPanel();
        chartPanel.setLayout(new BoxLayout(chartPanel, BoxLayout.Y_AXIS));
        chartPanel.setOpaque(false);

        try {
            List<Room> rooms = Room.findAll();
            Map<String, Long> roomUsage = Reservation.findAll().stream()
                .collect(Collectors.groupingBy(r -> {
                    Room room = rooms.stream()
                        .filter(rm -> rm.getId() == r.getIdSalle())
                        .findFirst().orElse(null);
                    return room != null ? room.getNom() : "Salle inconnue";
                }, Collectors.counting()));

            // Trier par utilisation
            roomUsage.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .limit(5)
                .forEach(entry -> {
                    JPanel barPanel = new JPanel(new BorderLayout());
                    barPanel.setOpaque(false);
                    barPanel.setBorder(BorderFactory.createEmptyBorder(2, 0, 2, 0));

                    // Nom de la salle
                    JLabel roomLabel = new JLabel(entry.getKey());
                    roomLabel.setFont(StyleUtils.BODY_FONT);
                    roomLabel.setPreferredSize(new Dimension(120, 20));
                    barPanel.add(roomLabel, BorderLayout.WEST);

                    // Barre de progression
                    JProgressBar bar = new JProgressBar(0, getTotalReservations() > 0 ? getTotalReservations() : 1);
                    bar.setValue(entry.getValue().intValue());
                    bar.setStringPainted(true);
                    bar.setString(entry.getValue().toString());
                    bar.setForeground(StyleUtils.PRIMARY_COLOR);
                    barPanel.add(bar, BorderLayout.CENTER);

                    chartPanel.add(barPanel);
                });

        } catch (Exception e) {
            JLabel errorLabel = new JLabel("Erreur lors du chargement des données");
            errorLabel.setForeground(StyleUtils.ERROR_COLOR);
            chartPanel.add(errorLabel);
        }

        return chartPanel;
    }

    private JPanel createMonthlyTrendChart() {
        JPanel chartPanel = new JPanel();
        chartPanel.setLayout(new BoxLayout(chartPanel, BoxLayout.Y_AXIS));
        chartPanel.setOpaque(false);

        try {
            // Simulation de données mensuelles (en production, calculer depuis la DB)
            String[] months = {"Jan", "Fév", "Mar", "Avr", "Mai", "Jun"};
            int[] values = {12, 19, 15, 25, 22, 18};

            int maxValue = 30; // Maximum pour l'échelle

            for (int i = 0; i < months.length; i++) {
                JPanel barPanel = new JPanel(new BorderLayout());
                barPanel.setOpaque(false);
                barPanel.setBorder(BorderFactory.createEmptyBorder(2, 0, 2, 0));

                // Nom du mois
                JLabel monthLabel = new JLabel(months[i]);
                monthLabel.setFont(StyleUtils.BODY_FONT);
                monthLabel.setPreferredSize(new Dimension(40, 20));
                barPanel.add(monthLabel, BorderLayout.WEST);

                // Barre de progression
                JProgressBar bar = new JProgressBar(0, maxValue);
                bar.setValue(values[i]);
                bar.setStringPainted(true);
                bar.setString(String.valueOf(values[i]));
                bar.setForeground(StyleUtils.SUCCESS_COLOR);
                barPanel.add(bar, BorderLayout.CENTER);

                chartPanel.add(barPanel);
            }

        } catch (Exception e) {
            JLabel errorLabel = new JLabel("Erreur lors du chargement des données");
            errorLabel.setForeground(StyleUtils.ERROR_COLOR);
            chartPanel.add(errorLabel);
        }

        return chartPanel;
    }

    private JPanel createFooter() {
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.CENTER));
        footer.setBackground(StyleUtils.BACKGROUND_COLOR);
        footer.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JLabel footerLabel = new JLabel("Système de Gestion des Réservations de Salles - Analyse Statistique");
        footerLabel.setFont(StyleUtils.SMALL_FONT);
        footerLabel.setForeground(StyleUtils.TEXT_SECONDARY);
        footer.add(footerLabel);

        return footer;
    }

    // Méthodes de calcul des statistiques
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

    private int getApprovedReservations() {
        try {
            return (int) Reservation.findAll().stream()
                .filter(r -> Reservation.STATUT_ACCEPTEE.equals(r.getStatut()))
                .count();
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

    private int getRejectedReservations() {
        try {
            return (int) Reservation.findAll().stream()
                .filter(r -> Reservation.STATUT_REFUSEE.equals(r.getStatut()))
                .count();
        } catch (Exception e) {
            return 0;
        }
    }
}