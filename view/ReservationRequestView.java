package view;

import controller.MainController;
import model.Room;
import model.Reservation;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@SuppressWarnings("serial")
public class ReservationRequestView extends JFrame {
    private static final long serialVersionUID = 1L;

    private final MainController controller;
    private JComboBox<Room> cbSalles;
    private JSpinner spDateDebut, spDateFin;
    private JTextArea taMotif;
    private JLabel lblCapacite, lblEquipements;

    public ReservationRequestView(MainController controller) {
        this.controller = controller;
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Demande de réservation");
        setSize(600, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Icône
        try {
            setIconImage(new ImageIcon("icon/accueil.png").getImage());
        } catch (Exception ex) {
            System.out.println("Icône non trouvée ou non supportée.");
        }

        // Layout
        setLayout(new BorderLayout());

        // Titre
        JLabel lblTitre = new JLabel("Nouvelle demande de réservation", JLabel.CENTER);
        lblTitre.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitre.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(lblTitre, BorderLayout.NORTH);

        // Panel formulaire
        JPanel panelForm = new JPanel(new GridBagLayout());
        panelForm.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // Salle
        gbc.gridx = 0; gbc.gridy = 0;
        panelForm.add(new JLabel("Salle :"), gbc);
        gbc.gridx = 1;
        cbSalles = new JComboBox<>();
        chargerSalles();
        cbSalles.setPreferredSize(new Dimension(200, 25));
        cbSalles.addActionListener(e -> afficherDetailsSalle());
        panelForm.add(cbSalles, gbc);

        // Détails salle
        gbc.gridx = 0; gbc.gridy = 1;
        gbc.gridwidth = 2;
        JPanel panelDetails = new JPanel(new GridLayout(2, 1));
        lblCapacite = new JLabel("Capacité : ");
        lblEquipements = new JLabel("Équipements : ");
        panelDetails.add(lblCapacite);
        panelDetails.add(lblEquipements);
        panelForm.add(panelDetails, gbc);
        gbc.gridwidth = 1;

        // Date début
        gbc.gridx = 0; gbc.gridy = 2;
        panelForm.add(new JLabel("Date et heure de début :"), gbc);
        gbc.gridx = 1;
        spDateDebut = new JSpinner(new SpinnerDateModel());
        spDateDebut.setEditor(new JSpinner.DateEditor(spDateDebut, "dd/MM/yyyy HH:mm"));
        spDateDebut.setPreferredSize(new Dimension(200, 25));
        panelForm.add(spDateDebut, gbc);

        // Date fin
        gbc.gridx = 0; gbc.gridy = 3;
        panelForm.add(new JLabel("Date et heure de fin :"), gbc);
        gbc.gridx = 1;
        spDateFin = new JSpinner(new SpinnerDateModel());
        spDateFin.setEditor(new JSpinner.DateEditor(spDateFin, "dd/MM/yyyy HH:mm"));
        spDateFin.setPreferredSize(new Dimension(200, 25));
        panelForm.add(spDateFin, gbc);

        // Motif
        gbc.gridx = 0; gbc.gridy = 4;
        panelForm.add(new JLabel("Motif :"), gbc);
        gbc.gridx = 1;
        taMotif = new JTextArea(3, 20);
        taMotif.setLineWrap(true);
        taMotif.setWrapStyleWord(true);
        JScrollPane scrollMotif = new JScrollPane(taMotif);
        scrollMotif.setPreferredSize(new Dimension(200, 60));
        panelForm.add(scrollMotif, gbc);

        add(panelForm, BorderLayout.CENTER);

        // Boutons
        JPanel panelBoutons = new JPanel();
        JButton btnSoumettre = new JButton("Soumettre la demande");
        btnSoumettre.addActionListener(e -> soumettreDemande());
        JButton btnRetour = new JButton("Retour au menu");
        btnRetour.addActionListener(e -> {
            dispose();
            controller.showMenuPrincipal();
        });
        JButton btnDeconnexion = new JButton("Déconnexion");
        btnDeconnexion.addActionListener(e -> {
            dispose();
            controller.deconnecter();
        });
        panelBoutons.add(btnSoumettre);
        panelBoutons.add(btnRetour);
        panelBoutons.add(btnDeconnexion);
        add(panelBoutons, BorderLayout.SOUTH);
    }

    private void chargerSalles() {
        try {
            List<Room> salles = Room.findAll();
            for (Room salle : salles) {
                cbSalles.addItem(salle);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erreur lors du chargement des salles : " + e.getMessage(),
                                        "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void afficherDetailsSalle() {
        Room salle = (Room) cbSalles.getSelectedItem();
        if (salle != null) {
            lblCapacite.setText("Capacité : " + salle.getCapacite());
            lblEquipements.setText("Équipements : " + salle.getEquipements());
        } else {
            lblCapacite.setText("Capacité : ");
            lblEquipements.setText("Équipements : ");
        }
    }

    private void soumettreDemande() {
        try {
            Room salleSelectionnee = (Room) cbSalles.getSelectedItem();
            if (salleSelectionnee == null) {
                JOptionPane.showMessageDialog(this, "Veuillez sélectionner une salle.",
                                            "Erreur", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Date dateDebut = (Date) spDateDebut.getValue();
            Date dateFin = (Date) spDateFin.getValue();
            String motif = taMotif.getText().trim();

            if (dateDebut.after(dateFin)) {
                JOptionPane.showMessageDialog(this, "La date de fin doit être après la date de début.",
                                            "Erreur", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (motif.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Veuillez saisir un motif pour la réservation.",
                                            "Erreur", JOptionPane.WARNING_MESSAGE);
                return;
            }

            LocalDateTime dateDebutLDT = LocalDateTime.ofInstant(dateDebut.toInstant(), ZoneId.systemDefault());
            LocalDateTime dateFinLDT = LocalDateTime.ofInstant(dateFin.toInstant(), ZoneId.systemDefault());

            // Vérifier les conflits
            if (Reservation.verifierConflit(salleSelectionnee.getId(), dateDebutLDT, dateFinLDT)) {
                JOptionPane.showMessageDialog(this, "Conflit détecté : la salle est déjà réservée à cette période.",
                                            "Conflit", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Créer la réservation avec statut "en_attente"
            Reservation reservation = new Reservation(
                controller.getUtilisateurConnecte().getId(),
                salleSelectionnee.getId(),
                dateDebutLDT,
                dateFinLDT,
                motif
            );

            if (reservation.save()) {
                JOptionPane.showMessageDialog(this,
                    "Demande de réservation soumise avec succès !\n" +
                    "Salle : " + salleSelectionnee.getNom() + "\n" +
                    "Du : " + reservation.getDateDebutFormatee() + "\n" +
                    "Au : " + reservation.getDateFinFormatee() + "\n" +
                    "Motif : " + motif,
                    "Demande soumise", JOptionPane.INFORMATION_MESSAGE);

                // Retour au menu
                dispose();
                controller.showMenuPrincipal();
            } else {
                JOptionPane.showMessageDialog(this, "Erreur lors de la sauvegarde de la réservation.",
                                            "Erreur", JOptionPane.ERROR_MESSAGE);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erreur lors de la soumission : " + e.getMessage(),
                                        "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }
}
