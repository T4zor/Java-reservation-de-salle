package view;

import controller.MainController;
import model.Room;
import model.Reservation;
import model.LogAction;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@SuppressWarnings("serial")
public class ReservationRequestView extends JFrame {
    private static final long serialVersionUID = 1L;

    private final MainController controller;
    private JComboBox<Room> cbSalles;
    private JCalendar calendar;
    private JComboBox<String> cbReservationType;
    private JSpinner spHeureDebut, spHeureFin;
    private JSpinner spDureeJours;
    private JSpinner spDateReservation;
    private JTextArea taMotif;
    private JLabel lblCapacite, lblEquipements;
    private JLabel lblHoraires, lblHeureDebut, lblHeureFin, lblPrivatisation;
    private JLabel lblDateDebutPrivatisation, lblDateFinPrivatisation;
    private JLabel lblDateReservation;
    private JButton btnVerifierConflits;
    private JLabel lblConflitsStatus;
    // Nouveaux champs pour la privatisation
    private JSpinner spDateDebutPrivatisation, spDateFinPrivatisation;

    // Contraintes horaires
    private static final LocalTime HEURE_DEBUT_COURS = LocalTime.of(7, 30);
    private static final LocalTime HEURE_FIN_COURS = LocalTime.of(21, 0);
    private static final int MAX_DUREE_ETUDIANT_HEURES = 24; // 1 journée max

    public ReservationRequestView(MainController controller) {
        this.controller = controller;
        StyleUtils.applyModernStyle(this);
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Demande de réservation - " + (controller.isResponsable() ? "Responsable" : "Étudiant"));
        setSize(900, 700);
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

        // Footer avec boutons
        add(createFooter(), BorderLayout.SOUTH);

        // Appliquer les contraintes selon le rôle
        appliquerContraintesRole();

        // Synchroniser les heures avec la date sélectionnée
        synchroniserHeuresAvecDate();
    }

    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(StyleUtils.PRIMARY_COLOR);
        header.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        // Titre
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        titlePanel.setOpaque(false);

        JLabel titleLabel = new JLabel("Demande de Réservation");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        titlePanel.add(titleLabel);

        header.add(titlePanel, BorderLayout.WEST);

        // Type d'utilisateur
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        rightPanel.setOpaque(false);

        String roleText = controller.isResponsable() ? "Mode Responsable" : "Mode Étudiant";
        JLabel roleLabel = new JLabel(roleText);
        roleLabel.setFont(StyleUtils.BODY_FONT);
        roleLabel.setForeground(Color.WHITE);
        rightPanel.add(roleLabel);

        header.add(rightPanel, BorderLayout.EAST);

        return header;
    }

    private JPanel createMainContent() {
        JPanel mainPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        mainPanel.setBackground(StyleUtils.BACKGROUND_COLOR);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Panneau gauche : Formulaire
        mainPanel.add(createFormPanel());

        // Panneau droit : Calendrier
        mainPanel.add(createCalendarPanel());

        return mainPanel;
    }

    private JPanel createFormPanel() {
        JPanel formPanel = StyleUtils.createCardPanel();
        formPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;

        int row = 0;

        // Type de réservation
        gbc.gridx = 0; gbc.gridy = row;
        gbc.gridwidth = 2;
        JLabel lblType = new JLabel("Type de réservation :");
        StyleUtils.styleBodyLabel(lblType);
        formPanel.add(lblType, gbc);

        gbc.gridy = ++row;
        cbReservationType = new JComboBox<>(new String[]{"Réservation normale", "Privatisation de salle"});
        cbReservationType.setPreferredSize(new Dimension(300, 35));
        cbReservationType.addActionListener(e -> toggleReservationMode());
        formPanel.add(cbReservationType, gbc);
        gbc.gridwidth = 1;

        // Salle
        gbc.gridx = 0; gbc.gridy = ++row;
        JLabel lblSalle = new JLabel("Salle :");
        StyleUtils.styleBodyLabel(lblSalle);
        formPanel.add(lblSalle, gbc);

        gbc.gridx = 1;
        cbSalles = StyleUtils.createStyledComboBoxGeneric();
        cbSalles.setPreferredSize(new Dimension(250, 35));
        cbSalles.addActionListener(e -> afficherDetailsSalle());
        formPanel.add(cbSalles, gbc);

        // Détails salle
        gbc.gridx = 0; gbc.gridy = ++row;
        gbc.gridwidth = 2;
        JPanel panelDetails = new JPanel(new GridLayout(2, 1, 0, 2));
        lblCapacite = new JLabel("Capacité : ");
        lblEquipements = new JLabel("Équipements : ");
        panelDetails.add(lblCapacite);
        panelDetails.add(lblEquipements);
        formPanel.add(panelDetails, gbc);
        gbc.gridwidth = 1;

        // Section horaires (pour réservation normale)
        gbc.gridx = 0; gbc.gridy = ++row;
        gbc.gridwidth = 2;
        lblHoraires = new JLabel("Horaires de réservation :");
        StyleUtils.styleBodyLabel(lblHoraires);
        lblHoraires.setName("lblHoraires");
        formPanel.add(lblHoraires, gbc);

        // Date de réservation
        gbc.gridy = ++row;
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        lblDateReservation = new JLabel("Date de réservation :");
        StyleUtils.styleBodyLabel(lblDateReservation);
        lblDateReservation.setName("lblDateReservation");
        formPanel.add(lblDateReservation, gbc);

        gbc.gridx = 1;
        spDateReservation = new JSpinner(new SpinnerDateModel());
        spDateReservation.setEditor(new JSpinner.DateEditor(spDateReservation, "dd/MM/yyyy"));
        spDateReservation.setPreferredSize(new Dimension(120, 35));
        spDateReservation.setValue(java.util.Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));
        spDateReservation.setName("spDateReservation");
        // Synchroniser les heures quand la date change
        spDateReservation.addChangeListener(e -> {
            synchroniserHeuresAvecDate();
            validerDatesTempsReel();
        });
        formPanel.add(spDateReservation, gbc);

        // Heure début
        gbc.gridy = ++row;
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        lblHeureDebut = new JLabel("Heure de début :");
        StyleUtils.styleBodyLabel(lblHeureDebut);
        lblHeureDebut.setName("lblHeureDebut");
        formPanel.add(lblHeureDebut, gbc);

        gbc.gridx = 1;
        spHeureDebut = new JSpinner(new SpinnerDateModel());
        spHeureDebut.setEditor(new JSpinner.DateEditor(spHeureDebut, "HH:mm"));
        spHeureDebut.setPreferredSize(new Dimension(100, 35));
        // Initialiser avec l'heure actuelle (minimum 8h)
        LocalDateTime now = LocalDateTime.now();
        if (now.getHour() < 8) {
            now = LocalDateTime.of(now.toLocalDate(), LocalTime.of(8, 0));
        }
        spHeureDebut.setValue(java.util.Date.from(now.atZone(ZoneId.systemDefault()).toInstant()));
        spHeureDebut.addChangeListener(e -> {
            if ("Réservation normale".equals(getCbReservationType().getSelectedItem()) && calendar != null) {
                // Le calendrier est maintenant en mode affichage seulement
                // Pas besoin de mettre à jour la sélection
            }
            validerDatesTempsReel();
        });
        spHeureDebut.setName("spHeureDebut");
        formPanel.add(spHeureDebut, gbc);
        gbc.gridx = 0; gbc.gridy = ++row;
        lblHeureFin = new JLabel("Heure de fin :");
        StyleUtils.styleBodyLabel(lblHeureFin);
        lblHeureFin.setName("lblHeureFin");
        formPanel.add(lblHeureFin, gbc);

        gbc.gridx = 1;
        spHeureFin = new JSpinner(new SpinnerDateModel());
        spHeureFin.setEditor(new JSpinner.DateEditor(spHeureFin, "HH:mm"));
        spHeureFin.setPreferredSize(new Dimension(100, 35));
        // Initialiser avec 1 heure après le début
        LocalDateTime endTime = LocalDateTime.now();
        if (endTime.getHour() < 9) {
            endTime = LocalDateTime.of(endTime.toLocalDate(), LocalTime.of(9, 0));
        } else {
            endTime = endTime.plusHours(1);
        }
        spHeureFin.setValue(java.util.Date.from(endTime.atZone(ZoneId.systemDefault()).toInstant()));
        spHeureFin.setName("spHeureFin");
        spHeureFin.addChangeListener(e -> validerDatesTempsReel());
        formPanel.add(spHeureFin, gbc);

        // Nouveaux champs pour les dates de privatisation
        gbc.gridx = 0; gbc.gridy = ++row;
        gbc.gridwidth = 1;
        lblDateDebutPrivatisation = new JLabel("Date début privatisation :");
        StyleUtils.styleBodyLabel(lblDateDebutPrivatisation);
        lblDateDebutPrivatisation.setName("lblDateDebutPrivatisation");
        lblDateDebutPrivatisation.setVisible(false);
        formPanel.add(lblDateDebutPrivatisation, gbc);

        gbc.gridx = 1;
        spDateDebutPrivatisation = new JSpinner(new SpinnerDateModel());
        spDateDebutPrivatisation.setEditor(new JSpinner.DateEditor(spDateDebutPrivatisation, "dd/MM/yyyy"));
        spDateDebutPrivatisation.setPreferredSize(new Dimension(120, 35));
        spDateDebutPrivatisation.setValue(java.util.Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));
        spDateDebutPrivatisation.setName("spDateDebutPrivatisation");
        spDateDebutPrivatisation.setVisible(false);
        spDateDebutPrivatisation.addChangeListener(e -> validerDatesTempsReel());
        formPanel.add(spDateDebutPrivatisation, gbc);

        gbc.gridx = 0; gbc.gridy = ++row;
        lblDateFinPrivatisation = new JLabel("Date fin privatisation :");
        StyleUtils.styleBodyLabel(lblDateFinPrivatisation);
        lblDateFinPrivatisation.setName("lblDateFinPrivatisation");
        lblDateFinPrivatisation.setVisible(false);
        formPanel.add(lblDateFinPrivatisation, gbc);

        gbc.gridx = 1;
        spDateFinPrivatisation = new JSpinner(new SpinnerDateModel());
        spDateFinPrivatisation.setEditor(new JSpinner.DateEditor(spDateFinPrivatisation, "dd/MM/yyyy"));
        spDateFinPrivatisation.setPreferredSize(new Dimension(120, 35));
        spDateFinPrivatisation.setValue(java.util.Date.from(LocalDateTime.now().plusDays(1).atZone(ZoneId.systemDefault()).toInstant()));
        spDateFinPrivatisation.setName("spDateFinPrivatisation");
        spDateFinPrivatisation.setVisible(false);
        spDateFinPrivatisation.addChangeListener(e -> validerDatesTempsReel());
        formPanel.add(spDateFinPrivatisation, gbc);

        // Motif
        gbc.gridx = 0; gbc.gridy = ++row;
        gbc.gridwidth = 2;
        JLabel lblMotif = new JLabel("Motif de la réservation :");
        StyleUtils.styleBodyLabel(lblMotif);
        formPanel.add(lblMotif, gbc);

        gbc.gridy = ++row;
        taMotif = StyleUtils.createStyledTextArea();
        taMotif.setRows(3);
        JScrollPane scrollMotif = new JScrollPane(taMotif);
        scrollMotif.setPreferredSize(new Dimension(350, 80));
        formPanel.add(scrollMotif, gbc);

        // Vérification des conflits
        gbc.gridy = ++row;
        gbc.gridwidth = 2;
        JPanel conflictPanel = new JPanel(new BorderLayout());
        btnVerifierConflits = new JButton("Vérifier les conflits");
        btnVerifierConflits.addActionListener(e -> verifierConflits());
        conflictPanel.add(btnVerifierConflits, BorderLayout.WEST);

        lblConflitsStatus = new JLabel("Cliquez pour vérifier les conflits");
        lblConflitsStatus.setForeground(StyleUtils.TEXT_SECONDARY);
        conflictPanel.add(lblConflitsStatus, BorderLayout.CENTER);

        formPanel.add(conflictPanel, gbc);

        // Charger les salles après l'initialisation complète
        chargerSalles();

        return formPanel;
    }

    private JPanel createCalendarPanel() {
        JPanel calendarPanel = StyleUtils.createCardPanel();
        calendarPanel.setLayout(new BorderLayout());

        // Titre
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        titlePanel.setOpaque(false);
        titlePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 10));
        JLabel calendarTitle = new JLabel("Calendrier des réservations");
        StyleUtils.styleBodyLabel(calendarTitle);
        titlePanel.add(calendarTitle);
        calendarPanel.add(titlePanel, BorderLayout.NORTH);

        // Calendrier (simplifié pour l'instant - on utilisera un vrai calendrier plus tard)
        calendar = new JCalendar(controller, this);
        calendarPanel.add(calendar, BorderLayout.CENTER);

        // Légende
        JPanel legendPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));
        legendPanel.setOpaque(false);
        legendPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 10));

        JPanel legendItem1 = createLegendItem(StyleUtils.SUCCESS_COLOR, "Réservé");
        JPanel legendItem2 = createLegendItem(StyleUtils.WARNING_COLOR, "Votre sélection");

        legendPanel.add(legendItem1);
        legendPanel.add(legendItem2);

        calendarPanel.add(legendPanel, BorderLayout.SOUTH);

        return calendarPanel;
    }

    private JPanel createLegendItem(Color color, String text) {
        JPanel item = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        item.setOpaque(false);

        JPanel colorBox = new JPanel();
        colorBox.setBackground(color);
        colorBox.setPreferredSize(new Dimension(15, 15));
        colorBox.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));

        JLabel label = new JLabel(text);
        label.setFont(StyleUtils.SMALL_FONT);

        item.add(colorBox);
        item.add(label);

        return item;
    }

    private JPanel createFooter() {
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        footer.setBackground(StyleUtils.BACKGROUND_COLOR);
        footer.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JButton btnSoumettre = StyleUtils.createSuccessButton("Soumettre la demande");
        btnSoumettre.addActionListener(e -> soumettreDemande());

        JButton btnRetour = StyleUtils.createSecondaryButton("Retour au menu");
        btnRetour.addActionListener(e -> {
            dispose();
            controller.showDashboardView();
        });

        footer.add(btnSoumettre);
        footer.add(btnRetour);

        return footer;
    }

    private void appliquerContraintesRole() {
        if (!controller.isResponsable()) {
            // Contraintes pour étudiants
            cbReservationType.setSelectedItem("Réservation normale");
            cbReservationType.setEnabled(false); // Étudiants ne peuvent pas privatiser

            // Limiter les heures aux heures de cours
            setHeureMinMax(spHeureDebut, HEURE_DEBUT_COURS, HEURE_FIN_COURS);
            setHeureMinMax(spHeureFin, HEURE_DEBUT_COURS, HEURE_FIN_COURS);
        }
    }

    private void setHeureMinMax(JSpinner spinner, LocalTime min, LocalTime max) {
        // Cette méthode configure les contraintes horaires
        // Pour l'instant, on laisse les spinners libres mais on validera lors de la soumission
    }

    private void toggleReservationMode() {
        String selectedMode = (String) cbReservationType.getSelectedItem();
        boolean isNormalReservation = "Réservation normale".equals(selectedMode);

        // Masquer/afficher les composants selon le mode
        if (lblHoraires != null) lblHoraires.setVisible(isNormalReservation);
        if (lblDateReservation != null) lblDateReservation.setVisible(isNormalReservation);
        if (spDateReservation != null) spDateReservation.setVisible(isNormalReservation);
        if (lblHeureDebut != null) lblHeureDebut.setVisible(isNormalReservation);
        if (spHeureDebut != null) spHeureDebut.setVisible(isNormalReservation);
        if (lblHeureFin != null) lblHeureFin.setVisible(isNormalReservation);
        if (spHeureFin != null) spHeureFin.setVisible(isNormalReservation);

        // Nouveaux champs de date pour la privatisation
        if (lblDateDebutPrivatisation != null) lblDateDebutPrivatisation.setVisible(!isNormalReservation);
        if (spDateDebutPrivatisation != null) spDateDebutPrivatisation.setVisible(!isNormalReservation);
        if (lblDateFinPrivatisation != null) lblDateFinPrivatisation.setVisible(!isNormalReservation);
        if (spDateFinPrivatisation != null) spDateFinPrivatisation.setVisible(!isNormalReservation);

        // Réinitialiser les sélections du calendrier selon le mode
        if (calendar != null) {
            if (isNormalReservation) {
                // Mode normal : pas de sélection spécifique
                calendar.clearSelection();
            } else {
                // Mode privatisation : réinitialiser la sélection
                calendar.clearSelection();
            }
        }
    }

    private void synchroniserHeuresAvecDate() {
        if (spDateReservation == null || spHeureDebut == null || spHeureFin == null) return;

        try {
            // Récupérer la date sélectionnée
            Date selectedDate = (Date) spDateReservation.getValue();
            LocalDate date = LocalDateTime.ofInstant(selectedDate.toInstant(), ZoneId.systemDefault()).toLocalDate();

            // Mettre à jour l'heure de début avec la nouvelle date
            Date currentHeureDebut = (Date) spHeureDebut.getValue();
            LocalTime heureDebut = LocalDateTime.ofInstant(currentHeureDebut.toInstant(), ZoneId.systemDefault()).toLocalTime();
            LocalDateTime newHeureDebut = LocalDateTime.of(date, heureDebut);
            spHeureDebut.setValue(java.util.Date.from(newHeureDebut.atZone(ZoneId.systemDefault()).toInstant()));

            // Mettre à jour l'heure de fin avec la nouvelle date
            Date currentHeureFin = (Date) spHeureFin.getValue();
            LocalTime heureFin = LocalDateTime.ofInstant(currentHeureFin.toInstant(), ZoneId.systemDefault()).toLocalTime();
            LocalDateTime newHeureFin = LocalDateTime.of(date, heureFin);
            spHeureFin.setValue(java.util.Date.from(newHeureFin.atZone(ZoneId.systemDefault()).toInstant()));

        } catch (Exception e) {
            System.err.println("Erreur lors de la synchronisation des heures : " + e.getMessage());
        }
    }

    private void validerDatesTempsReel() {
        try {
            LocalDateTime maintenant = LocalDateTime.now();
            LocalDateTime debut = getDateTimeDebut();
            LocalDateTime fin = getDateTimeFin();

            // Réinitialiser la couleur de fond des spinners
            if (spDateReservation != null) spDateReservation.setBackground(Color.WHITE);
            if (spHeureDebut != null) spHeureDebut.setBackground(Color.WHITE);
            if (spHeureFin != null) spHeureFin.setBackground(Color.WHITE);
            if (spDateDebutPrivatisation != null) spDateDebutPrivatisation.setBackground(Color.WHITE);
            if (spDateFinPrivatisation != null) spDateFinPrivatisation.setBackground(Color.WHITE);

            boolean hasError = false;

            // Vérifier que la date de début n'est pas dans le passé
            if (debut != null && debut.isBefore(maintenant)) {
                hasError = true;
                if ("Privatisation de salle".equals(cbReservationType.getSelectedItem())) {
                    if (spDateDebutPrivatisation != null) spDateDebutPrivatisation.setBackground(StyleUtils.ERROR_COLOR);
                } else {
                    if (spDateReservation != null) spDateReservation.setBackground(StyleUtils.ERROR_COLOR);
                }
            }

            // Vérifier que la date de fin n'est pas antérieure à la date de début
            if (debut != null && fin != null && (fin.isBefore(debut) || fin.isEqual(debut))) {
                hasError = true;
                if ("Privatisation de salle".equals(cbReservationType.getSelectedItem())) {
                    if (spDateFinPrivatisation != null) spDateFinPrivatisation.setBackground(StyleUtils.ERROR_COLOR);
                } else {
                    if (spHeureFin != null) spHeureFin.setBackground(StyleUtils.ERROR_COLOR);
                }
            }

            // Mettre à jour le statut de validation
            if (hasError) {
                lblConflitsStatus.setText("Erreurs dans les dates sélectionnées");
                lblConflitsStatus.setForeground(StyleUtils.ERROR_COLOR);
            } else {
                // Remettre le statut par défaut
                lblConflitsStatus.setText("Cliquez pour vérifier les conflits");
                lblConflitsStatus.setForeground(StyleUtils.TEXT_SECONDARY);
            }

        } catch (Exception e) {
            System.err.println("Erreur lors de la validation temps réel : " + e.getMessage());
        }
    }

    private void verifierConflits() {
        try {
            // Vérifier qu'une salle est sélectionnée
            Room salleSelectionnee = (Room) cbSalles.getSelectedItem();
            if (salleSelectionnee == null) {
                lblConflitsStatus.setText("Veuillez sélectionner une salle");
                lblConflitsStatus.setForeground(StyleUtils.WARNING_COLOR);
                return;
            }

            LocalDateTime debut = getDateTimeDebut();
            LocalDateTime fin = getDateTimeFin();

            if (debut == null || fin == null) {
                lblConflitsStatus.setText("Veuillez sélectionner des dates valides");
                lblConflitsStatus.setForeground(StyleUtils.WARNING_COLOR);
                return;
            }

            // Récupérer seulement les réservations validées pour cette salle
            List<Reservation> conflits = Reservation.findAll().stream()
                .filter(r -> Reservation.STATUT_ACCEPTEE.equals(r.getStatut())) // Uniquement les réservations acceptées
                .filter(r -> r.getIdSalle() == salleSelectionnee.getId()) // Même salle
                .filter(r -> reservationsSeChevauchent(debut, fin, r)) // Chevauchement temporel
                .collect(Collectors.toList());

            if (conflits.isEmpty()) {
                lblConflitsStatus.setText("✓ Aucun conflit détecté");
                lblConflitsStatus.setForeground(StyleUtils.SUCCESS_COLOR);
            } else {
                lblConflitsStatus.setText("⚠ " + conflits.size() + " conflit(s) détecté(s)");
                lblConflitsStatus.setForeground(StyleUtils.ERROR_COLOR);
            }

        } catch (Exception e) {
            lblConflitsStatus.setText("Erreur lors de la vérification");
            lblConflitsStatus.setForeground(StyleUtils.ERROR_COLOR);
        }
    }

    private boolean reservationsSeChevauchent(LocalDateTime debutNouvelle, LocalDateTime finNouvelle, Reservation existante) {
        // Vérifier si les réservations se chevauchent
        LocalDateTime debutExistante = existante.getDateDebut();
        LocalDateTime finExistante = existante.getDateFin();

        return debutNouvelle.isBefore(finExistante) && finNouvelle.isAfter(debutExistante);
    }

    private LocalDateTime getDateTimeDebut() {
        if ("Privatisation de salle".equals(cbReservationType.getSelectedItem())) {
            // Pour la privatisation, utiliser les spinners de date directs
            Date dateDebut = (Date) spDateDebutPrivatisation.getValue();
            return LocalDateTime.ofInstant(dateDebut.toInstant(), ZoneId.systemDefault()).with(LocalTime.MIDNIGHT);
        } else {
            // Pour réservation normale, combiner la date sélectionnée + heure
            Date dateReservation = (Date) spDateReservation.getValue();
            LocalDate selectedDate = LocalDateTime.ofInstant(dateReservation.toInstant(), ZoneId.systemDefault()).toLocalDate();

            Date heureDebut = (Date) spHeureDebut.getValue();
            LocalTime selectedTime = LocalDateTime.ofInstant(heureDebut.toInstant(), ZoneId.systemDefault()).toLocalTime();

            return LocalDateTime.of(selectedDate, selectedTime);
        }
    }

    private LocalDateTime getDateTimeFin() {
        if ("Privatisation de salle".equals(cbReservationType.getSelectedItem())) {
            // Pour la privatisation, utiliser les spinners de date directs
            Date dateFin = (Date) spDateFinPrivatisation.getValue();
            return LocalDateTime.ofInstant(dateFin.toInstant(), ZoneId.systemDefault()).with(LocalTime.of(23, 59));
        } else {
            // Pour réservation normale, combiner la date sélectionnée + heure
            Date dateReservation = (Date) spDateReservation.getValue();
            LocalDate selectedDate = LocalDateTime.ofInstant(dateReservation.toInstant(), ZoneId.systemDefault()).toLocalDate();

            Date heureFin = (Date) spHeureFin.getValue();
            LocalTime selectedTime = LocalDateTime.ofInstant(heureFin.toInstant(), ZoneId.systemDefault()).toLocalTime();

            return LocalDateTime.of(selectedDate, selectedTime);
        }
    }

    private void soumettreDemande() {
        try {
            // Validation des données
            if (!validerDonnees()) {
                return;
            }

            // Vérifier les conflits seulement si la vérification a été faite
            if (btnVerifierConflits.getText().contains("Vérifier")) {
                verifierConflits();
            }

            // Vérifier s'il y a des conflits réels
            String statusText = lblConflitsStatus.getText();
            boolean hasConflicts = statusText.contains("conflit") && !statusText.contains("Aucun conflit");

            if (hasConflicts) {
                int result = JOptionPane.showConfirmDialog(this,
                    "Des conflits ont été détectés. Voulez-vous quand même soumettre la demande ?",
                    "Confirmation", JOptionPane.YES_NO_OPTION);
                if (result != JOptionPane.YES_OPTION) {
                    return;
                }
            }

            // Créer la réservation
            Reservation reservation = new Reservation(
                controller.getUtilisateurConnecte().getId(),
                ((Room) cbSalles.getSelectedItem()).getId(),
                getDateTimeDebut(),
                getDateTimeFin(),
                taMotif.getText().trim()
            );

            if (reservation.save()) {
                // Logger l'action
                LogAction log = new LogAction(
                    controller.getUtilisateurConnecte().getId(),
                    "DEMANDE_RESERVATION",
                    "Nouvelle demande de réservation pour la salle " + ((Room) cbSalles.getSelectedItem()).getNom()
                );
                log.save();

                JOptionPane.showMessageDialog(this,
                    "Votre demande de réservation a été soumise avec succès !",
                    "Succès", JOptionPane.INFORMATION_MESSAGE);

                // Rafraîchir le calendrier pour montrer la nouvelle réservation
                if (calendar != null) {
                    calendar.refreshReservations();
                }

                dispose();
                controller.showDashboardView();
            } else {
                JOptionPane.showMessageDialog(this,
                    "Erreur lors de la soumission de la demande.",
                    "Erreur", JOptionPane.ERROR_MESSAGE);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Erreur : " + e.getMessage(),
                "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean validerDonnees() {
        // Validation de base
        if (cbSalles.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner une salle.");
            return false;
        }

        if (taMotif.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Veuillez saisir un motif.");
            return false;
        }

        // Validation des dates
        LocalDateTime maintenant = LocalDateTime.now();
        LocalDateTime debut = getDateTimeDebut();
        LocalDateTime fin = getDateTimeFin();

        if (debut != null) {
            // Vérifier que la date de début n'est pas dans le passé
            if (debut.isBefore(maintenant)) {
                JOptionPane.showMessageDialog(this,
                    "La date et heure de début ne peuvent pas être dans le passé.");
                return false;
            }
        }

        if (debut != null && fin != null) {
            // Vérifier que la date de fin n'est pas antérieure à la date de début
            if (fin.isBefore(debut) || fin.isEqual(debut)) {
                JOptionPane.showMessageDialog(this,
                    "La date et heure de fin doivent être postérieures à la date et heure de début.");
                return false;
            }
        }

        // Validation des horaires (stricte pour tous les utilisateurs)

        if (debut != null && fin != null) {
            LocalTime heureDebut = debut.toLocalTime();
            LocalTime heureFin = fin.toLocalTime();

            // Vérifier si c'est une privatisation complète (minuit à minuit)
            boolean isPrivatisationComplete = "Privatisation de salle".equals(cbReservationType.getSelectedItem()) &&
                heureDebut.equals(LocalTime.MIDNIGHT) &&
                (heureFin.equals(LocalTime.of(23, 59)) || heureFin.equals(LocalTime.MIDNIGHT));

            if (!isPrivatisationComplete) {
                // Pour tous les autres cas : horaires strictement entre 7h30 et 21h00
                if (heureDebut.isBefore(HEURE_DEBUT_COURS) || heureFin.isAfter(HEURE_FIN_COURS)) {
                    JOptionPane.showMessageDialog(this,
                        "Les horaires doivent être compris entre 7h30 et 21h00.\n" +
                        "Pour privatiser une salle pour la journée complète, utilisez le mode 'Privatisation de salle'.");
                    return false;
                }
            }
        }

        // Validation selon le rôle et le type (pour les étudiants uniquement)
        if (!controller.isResponsable()) {
            // Contraintes supplémentaires pour étudiants
            if (debut != null && fin != null) {
                // Vérifier la durée maximale (24h pour étudiants)
                long heures = java.time.Duration.between(debut, fin).toHours();
                if (heures > MAX_DUREE_ETUDIANT_HEURES) {
                    JOptionPane.showMessageDialog(this,
                        "Les réservations étudiants sont limitées à 24 heures maximum.");
                    return false;
                }
            }
        }

        return true;
    }

    private void chargerSalles() {
        try {
            List<Room> salles = Room.findAll();
            for (Room salle : salles) {
                cbSalles.addItem(salle);
            }
            if (!salles.isEmpty()) {
                cbSalles.setSelectedIndex(0);
                afficherDetailsSalle();
            }
        } catch (Exception e) {
            System.err.println("Erreur chargement salles : " + e.getMessage());
        }
    }

    private void afficherDetailsSalle() {
        Room salle = (Room) cbSalles.getSelectedItem();
        if (salle != null) {
            lblCapacite.setText("Capacité : " + salle.getCapacite() + " personnes");
            lblEquipements.setText("Équipements : " + salle.getEquipements());
        }

        // Mettre à jour le calendrier pour refléter les réservations de la salle sélectionnée
        if (calendar != null) {
            calendar.updateCalendar();
        }
    }

    // Getters pour accès depuis JCalendar
    public JComboBox<Room> getCbSalles() {
        return cbSalles;
    }

    public JComboBox<String> getCbReservationType() {
        return cbReservationType;
    }

    public JSpinner getSpHeureDebut() {
        return spHeureDebut;
    }

    public JSpinner getSpHeureFin() {
        return spHeureFin;
    }

    public JSpinner getSpDateReservation() {
        return spDateReservation;
    }

    public JSpinner getSpDateDebutPrivatisation() {
        return spDateDebutPrivatisation;
    }

    public JSpinner getSpDateFinPrivatisation() {
        return spDateFinPrivatisation;
    }
}

// Classe JCalendar simplifiée pour affichage seulement
class JCalendar extends JPanel {
    private MainController controller;
    private int currentMonth;
    private int currentYear;
    private JButton[][] dayButtons;
    private JLabel monthYearLabel;
    private JButton btnPrev, btnNext;
    private Map<LocalDate, List<Reservation>> reservationsParDate;
    private ReservationRequestView parentView;
    private LocalDate selectedDate;

    public JCalendar() {
        this(null, null);
    }

    public JCalendar(MainController controller, ReservationRequestView parentView) {
        this.controller = controller;
        this.parentView = parentView;
        this.reservationsParDate = new java.util.HashMap<>();
        initializeCalendar();
        loadReservations();
    }

    private void initializeCalendar() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

        // Header avec navigation
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 10));

        btnPrev = new JButton("◀");
        btnPrev.addActionListener(e -> navigateMonth(-1));
        headerPanel.add(btnPrev, BorderLayout.WEST);

        monthYearLabel = new JLabel("", JLabel.CENTER);
        monthYearLabel.setFont(new Font("Arial", Font.BOLD, 16));
        headerPanel.add(monthYearLabel, BorderLayout.CENTER);

        btnNext = new JButton("▶");
        btnNext.addActionListener(e -> navigateMonth(1));
        headerPanel.add(btnNext, BorderLayout.EAST);

        add(headerPanel, BorderLayout.NORTH);

        // Grille des jours
        JPanel daysPanel = new JPanel(new GridLayout(7, 7, 1, 1));
        daysPanel.setBackground(Color.WHITE);

        // En-têtes des jours
        String[] joursSemaine = {"Dim", "Lun", "Mar", "Mer", "Jeu", "Ven", "Sam"};
        for (String jour : joursSemaine) {
            JLabel label = new JLabel(jour, JLabel.CENTER);
            label.setFont(new Font("Arial", Font.BOLD, 12));
            label.setForeground(Color.GRAY);
            daysPanel.add(label);
        }

        // Boutons des jours
        dayButtons = new JButton[6][7];
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 7; j++) {
                JButton btn = new JButton();
                btn.setFont(new Font("Arial", Font.PLAIN, 12));
                btn.setFocusPainted(false);
                btn.setBorderPainted(false);
                btn.setContentAreaFilled(false);
                btn.setOpaque(true);
                btn.setBackground(Color.WHITE);
                btn.setPreferredSize(new Dimension(35, 35));

                // Clic simple pour voir les détails des réservations
                btn.addActionListener(e -> showDayReservations(btn));

                dayButtons[i][j] = btn;
                daysPanel.add(btn);
            }
        }

        add(daysPanel, BorderLayout.CENTER);

        // Initialiser au mois actuel
        LocalDate today = LocalDate.now();
        currentMonth = today.getMonthValue() - 1; // 0-based
        currentYear = today.getYear();
        updateCalendar();
    }

    private void navigateMonth(int delta) {
        currentMonth += delta;
        if (currentMonth < 0) {
            currentMonth = 11;
            currentYear--;
        } else if (currentMonth > 11) {
            currentMonth = 0;
            currentYear++;
        }
        updateCalendar();
        // La sélection est préservée automatiquement car updateCalendar() utilise les variables de sélection
    }

    public void updateCalendar() {
        // Mettre à jour le label du mois/année
        java.time.Month month = java.time.Month.of(currentMonth + 1);
        monthYearLabel.setText(month.getDisplayName(java.time.format.TextStyle.FULL, java.util.Locale.FRENCH) + " " + currentYear);

        // Effacer tous les boutons
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 7; j++) {
                dayButtons[i][j].setText("");
                dayButtons[i][j].setBackground(Color.WHITE);
                dayButtons[i][j].setForeground(Color.BLACK);
                dayButtons[i][j].setEnabled(true);
            }
        }

        // Calculer le premier jour du mois
        LocalDate firstDayOfMonth = LocalDate.of(currentYear, currentMonth + 1, 1);
        int firstDayOfWeek = firstDayOfMonth.getDayOfWeek().getValue() % 7; // 0 = dimanche
        int daysInMonth = firstDayOfMonth.lengthOfMonth();

        // Remplir les jours
        int day = 1;
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 7; j++) {
                if ((i == 0 && j < firstDayOfWeek) || day > daysInMonth) {
                    // Jour du mois précédent ou suivant
                    dayButtons[i][j].setEnabled(false);
                    dayButtons[i][j].setBackground(new Color(245, 245, 245));
                } else {
                    dayButtons[i][j].setText(String.valueOf(day));
                    dayButtons[i][j].setEnabled(true);

                    // Créer la date pour ce jour
                    LocalDate currentDate = LocalDate.of(currentYear, currentMonth + 1, day);

                    // Colorer selon les réservations
                    colorDayButton(dayButtons[i][j], currentDate);

                    day++;
                }
            }
        }
    }

    private void colorDayButton(JButton button, LocalDate date) {
        List<Reservation> reservations = reservationsParDate.get(date);

        if (reservations != null && !reservations.isEmpty()) {
            // Filtrer par salle sélectionnée si elle existe
            Room salleSelectionnee = parentView != null ? (Room) parentView.getCbSalles().getSelectedItem() : null;
            if (salleSelectionnee != null) {
                reservations = reservations.stream()
                    .filter(r -> r.getIdSalle() == salleSelectionnee.getId())
                    .collect(java.util.stream.Collectors.toList());
            }

            if (!reservations.isEmpty()) {
                // Il y a des réservations ce jour-là pour cette salle
                long approvedCount = reservations.stream()
                    .filter(r -> Reservation.STATUT_ACCEPTEE.equals(r.getStatut()))
                    .count();

                if (approvedCount > 0) {
                    // Réservations approuvées
                    button.setBackground(StyleUtils.SUCCESS_COLOR);
                    button.setForeground(Color.WHITE);
                } else {
                    // Réservations en attente
                    button.setBackground(StyleUtils.WARNING_COLOR);
                    button.setForeground(Color.BLACK);
                }
            } else {
                // Aucune réservation pour cette salle ce jour-là
                button.setBackground(Color.WHITE);
                button.setForeground(Color.BLACK);
            }
        } else {
            // Jour libre
            button.setBackground(Color.WHITE);
            button.setForeground(Color.BLACK);
        }

        // Pas de sélection spéciale
        button.setBorder(BorderFactory.createEmptyBorder());
    }

    private void showDayReservations(JButton button) {
        if (button.getText().isEmpty()) return;

        int day = Integer.parseInt(button.getText());
        LocalDate clickedDate = LocalDate.of(currentYear, currentMonth + 1, day);

        // Récupérer les réservations pour cette date et la salle sélectionnée
        List<Reservation> reservations = reservationsParDate.get(clickedDate);
        if (reservations == null) {
            reservations = new java.util.ArrayList<>();
        }

        // Filtrer par salle si une salle est sélectionnée
        Room salleSelectionnee = parentView != null ? (Room) parentView.getCbSalles().getSelectedItem() : null;
        if (salleSelectionnee != null) {
            reservations = reservations.stream()
                .filter(r -> r.getIdSalle() == salleSelectionnee.getId())
                .collect(java.util.stream.Collectors.toList());
        }

        // Afficher les détails
        if (reservations.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Aucune réservation pour le " + clickedDate.format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")) +
                (salleSelectionnee != null ? " dans la salle " + salleSelectionnee.getNom() : ""),
                "Réservations du jour", JOptionPane.INFORMATION_MESSAGE);
        } else {
            StringBuilder message = new StringBuilder();
            message.append("Réservations pour le ").append(clickedDate.format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")))
                   .append(salleSelectionnee != null ? " - Salle " + salleSelectionnee.getNom() : "").append(" :\n\n");

            for (Reservation res : reservations) {
                message.append("• ").append(res.getDateDebut().format(java.time.format.DateTimeFormatter.ofPattern("HH:mm")))
                       .append(" - ").append(res.getDateFin().format(java.time.format.DateTimeFormatter.ofPattern("HH:mm")))
                       .append(" (").append(res.getStatut()).append(")\n");
                if (res.getMotif() != null && !res.getMotif().trim().isEmpty()) {
                    message.append("  Motif: ").append(res.getMotif()).append("\n");
                }
                message.append("\n");
            }

            JOptionPane.showMessageDialog(this, message.toString(), "Réservations du jour", JOptionPane.INFORMATION_MESSAGE);
        }
    }







    private void loadReservations() {
        try {
            List<Reservation> allReservations = Reservation.findAll();
            reservationsParDate.clear();

            for (Reservation res : allReservations) {
                if (res.getDateDebut() != null) {
                    LocalDate dateDebut = res.getDateDebut().toLocalDate();
                    LocalDate dateFin = res.getDateFin().toLocalDate();

                    // Ajouter la réservation à toutes les dates concernées
                    LocalDate current = dateDebut;
                    while (!current.isAfter(dateFin)) {
                        reservationsParDate.computeIfAbsent(current, k -> new java.util.ArrayList<>()).add(res);
                        current = current.plusDays(1);
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Erreur chargement réservations calendrier: " + e.getMessage());
        }
    }

    public void refreshReservations() {
        loadReservations();
        updateCalendar();
    }

    public LocalDate getSelectedDate() {
        return selectedDate;
    }

    public void clearSelection() {
        this.selectedDate = null;
        updateCalendar();
    }
}
