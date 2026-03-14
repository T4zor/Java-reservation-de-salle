package view;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;

public class RequestProcessingView extends JFrame {

    // ========== COULEURS ==========
    private static final Color BLUE         = new Color(0x18, 0x5F, 0xA5);
    private static final Color BLUE_LIGHT   = new Color(0xE6, 0xF1, 0xFB);
    private static final Color GRAY_BG      = new Color(0xF5, 0xF5, 0xF3);
    private static final Color GRAY_TEXT    = new Color(0x88, 0x87, 0x80);
    private static final Color GRAY_BORDER  = new Color(0xD3, 0xD1, 0xC7);
    private static final Color GREEN        = new Color(0x0F, 0x6E, 0x56);
    private static final Color GREEN_LIGHT  = new Color(0xEA, 0xF3, 0xDE);
    private static final Color ORANGE       = new Color(0xBA, 0x75, 0x17);
    private static final Color ORANGE_LIGHT = new Color(0xFA, 0xEE, 0xDA);
    private static final Color RED          = new Color(0xA3, 0x2D, 0x2D);
    private static final Color RED_LIGHT    = new Color(0xFC, 0xEB, 0xEB);

    private JTabbedPane tabbedPane;

    public RequestProcessingView() {
        setTitle("Réservation de salles — Responsable");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(900, 600));
        setSize(1100, 700);
        setLocationRelativeTo(null);
        getContentPane().setBackground(GRAY_BG);

        buildUI();
        setVisible(true);
    }

    private void buildUI() {
        setLayout(new BorderLayout());

        // Header
        add(buildHeader(), BorderLayout.NORTH);

        // Onglets
        tabbedPane = new JTabbedPane();
        tabbedPane.setBackground(GRAY_BG);
        tabbedPane.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tabbedPane.addTab("Demandes",     buildDemandesPanel());
        tabbedPane.addTab("Calendrier",   buildCalendrierPanel());
        tabbedPane.addTab("Salles",       buildSallesPanel());
        tabbedPane.addTab("Utilisateurs", buildUtilisateursPanel());

        add(tabbedPane, BorderLayout.CENTER);
    }

    // ========== HEADER ==========
    private JPanel buildHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);
        header.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, GRAY_BORDER),
            BorderFactory.createEmptyBorder(12, 20, 12, 20)
        ));

        JLabel title = new JLabel("Réservation de salles");
        title.setFont(new Font("Segoe UI", Font.BOLD, 16));
        title.setForeground(new Color(0x1A, 0x1A, 0x1A));

        // Badge responsable
        JPanel badge = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        badge.setOpaque(false);
        JLabel initiales = new JLabel("RB");
        initiales.setFont(new Font("Segoe UI", Font.BOLD, 12));
        initiales.setForeground(Color.WHITE);
        initiales.setOpaque(true);
        initiales.setBackground(BLUE);
        initiales.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));
        JLabel role = new JLabel("Responsable");
        role.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        role.setForeground(GRAY_TEXT);
        badge.add(initiales);
        badge.add(role);

        header.add(title, BorderLayout.WEST);
        header.add(badge, BorderLayout.EAST);
        return header;
    }

    // ========== ONGLET 1 : DEMANDES ==========
    private JPanel buildDemandesPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(GRAY_BG);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(0, 0, 16, 0);

        // Stats
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.weightx = 1; gbc.weighty = 0;
        panel.add(buildStatsBar(), gbc);

        // Filtres
        gbc.gridy = 1;
        panel.add(buildFiltresBar(), gbc);

        // Tableau
        gbc.gridy = 2; gbc.weighty = 1;
        panel.add(buildTableauDemandes(), gbc);

        return panel;
    }

    private JPanel buildStatsBar() {
        JPanel bar = new JPanel(new GridLayout(1, 4, 12, 0));
        bar.setOpaque(false);
        bar.add(buildStatCard("Total demandes", "12", Color.BLACK));
        bar.add(buildStatCard("En attente",     "5",  ORANGE));
        bar.add(buildStatCard("Acceptées",      "5",  GREEN));
        bar.add(buildStatCard("Refusées",       "2",  RED));
        return bar;
    }

    private JPanel buildStatCard(String label, String value, Color valueColor) {
        JPanel card = new JPanel(new GridBagLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(GRAY_BORDER),
            BorderFactory.createEmptyBorder(14, 18, 14, 18)
        ));
        GridBagConstraints g = new GridBagConstraints();
        g.gridx = 0; g.gridy = 0; g.anchor = GridBagConstraints.WEST;
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lbl.setForeground(GRAY_TEXT);
        card.add(lbl, g);
        g.gridy = 1;
        JLabel val = new JLabel(value);
        val.setFont(new Font("Segoe UI", Font.BOLD, 26));
        val.setForeground(valueColor);
        card.add(val, g);
        return card;
    }

    private JPanel buildFiltresBar() {
        JPanel bar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        bar.setOpaque(false);

        JLabel lbl = new JLabel("Filtrer :");
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        String[] salles   = {"Toutes les salles", "Salle A", "Salle B", "Salle C"};
        String[] statuts  = {"Tous les statuts", "En attente", "Acceptée", "Refusée"};

        JComboBox<String> cbSalle  = styledCombo(salles);
        JComboBox<String> cbStatut = styledCombo(statuts);

        JTextField tfDate = new JTextField("jj/mm/aaaa", 10);
        tfDate.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tfDate.setForeground(GRAY_TEXT);

        bar.add(lbl);
        bar.add(cbSalle);
        bar.add(cbStatut);
        bar.add(tfDate);
        return bar;
    }

    private JScrollPane buildTableauDemandes() {
        String[] cols = {"Demandeur", "Salle", "Date", "Horaire", "Motif", "Statut", "Actions"};
        Object[][] data = {
            {"Essom M.",  "Salle B", "15 Mars", "10h-12h", "Cours Java",    "En attente", ""},
            {"Marie K.",  "Salle A", "16 Mars", "08h-10h", "TD Réseau",     "En attente", ""},
            {"Paul N.",   "Salle C", "17 Mars", "14h-16h", "Projet groupe", "En attente", ""},
            {"Anna R.",   "Salle A", "18 Mars", "09h-11h", "Examen",        "Acceptée",   ""},
            {"Jean T.",   "Salle B", "18 Mars", "13h-15h", "Réunion",       "Refusée",    ""},
        };

        DefaultTableModel model = new DefaultTableModel(data, cols) {
            public boolean isCellEditable(int r, int c) { return false; }
        };

        JTable table = new JTable(model);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setRowHeight(40);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        table.getTableHeader().setBackground(GRAY_BG);
        table.setShowHorizontalLines(true);
        table.setGridColor(GRAY_BORDER);
        table.setSelectionBackground(BLUE_LIGHT);

        // Rendu couleurs statut
        table.getColumnModel().getColumn(5).setCellRenderer(new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(JTable t, Object v,
                    boolean sel, boolean foc, int r, int c) {
                JLabel lbl = new JLabel(v.toString(), SwingConstants.CENTER);
                lbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lbl.setOpaque(true);
                switch (v.toString()) {
                    case "En attente": lbl.setBackground(ORANGE_LIGHT); lbl.setForeground(ORANGE); break;
                    case "Acceptée":   lbl.setBackground(GREEN_LIGHT);  lbl.setForeground(GREEN);  break;
                    case "Refusée":    lbl.setBackground(RED_LIGHT);    lbl.setForeground(RED);    break;
                }
                return lbl;
            }
        });

        // Colonne Actions avec boutons Acc. / Ref.
        table.getColumnModel().getColumn(6).setCellRenderer(new TableCellRenderer() {
            public Component getTableCellRendererComponent(JTable t, Object v,
                    boolean sel, boolean foc, int r, int c) {
                return buildActionButtons();
            }
        });
        table.getColumnModel().getColumn(6).setCellEditor(new DefaultCellEditor(new JCheckBox()) {
            public Component getTableCellEditorComponent(JTable t, Object v,
                    boolean sel, int r, int c) {
                JPanel p = buildActionButtons();
                // Acc.
                ((JButton)p.getComponent(0)).addActionListener(e -> {
                    model.setValueAt("Acceptée", r, 5);
                    fireEditingStopped();
                });
                // Ref.
                ((JButton)p.getComponent(1)).addActionListener(e -> {
                    String motif = JOptionPane.showInputDialog(null,
                        "Motif du refus :", "Refuser la demande", JOptionPane.PLAIN_MESSAGE);
                    if (motif != null && !motif.trim().isEmpty()) {
                        model.setValueAt("Refusée", r, 5);
                    }
                    fireEditingStopped();
                });
                return p;
            }
        });

        table.getColumnModel().getColumn(6).setPreferredWidth(120);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createLineBorder(GRAY_BORDER));
        return scroll;
    }

    private JPanel buildActionButtons() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER, 4, 4));
        p.setBackground(Color.WHITE);
        JButton acc = new JButton("Acc."); 
        acc.setBackground(GREEN_LIGHT); 
        acc.setForeground(GREEN);
        acc.setFont(new Font("Segoe UI", Font.BOLD, 11));
        acc.setBorderPainted(false); 
        acc.setFocusPainted(false);
        JButton ref = new JButton("Ref."); 
        ref.setBackground(RED_LIGHT);  
        ref.setForeground(RED);
        ref.setFont(new Font("Segoe UI", Font.BOLD, 11));
        ref.setBorderPainted(false); 
        ref.setFocusPainted(false);
        p.add(acc); p.add(ref);
        return p;
    }

    // ========== ONGLET 2 : CALENDRIER ==========
    private JPanel buildCalendrierPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 16));
        panel.setBackground(GRAY_BG);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Contrôles
        JPanel controls = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        controls.setOpaque(false);
        String[] salles = {"Toutes les salles", "Salle A", "Salle B", "Salle C"};
        controls.add(new JLabel("Salle :"));
        controls.add(styledCombo(salles));
        JButton prev = new JButton("◀"); JButton next = new JButton("▶");
        prev.setFocusPainted(false); next.setFocusPainted(false);
        JLabel semaine = new JLabel("  Semaine du 10 au 16 Mars 2026  ");
        semaine.setFont(new Font("Segoe UI", Font.BOLD, 13));
        controls.add(prev); controls.add(semaine); controls.add(next);
        panel.add(controls, BorderLayout.NORTH);

        // Grille calendrier
        panel.add(buildCalendrierGrid(), BorderLayout.CENTER);
        return panel;
    }

    private JPanel buildCalendrierGrid() {
        String[] jours   = {"", "Lun 10", "Mar 11", "Mer 12", "Jeu 13", "Ven 14", "Sam 15"};
        String[] horaires = {"08h-10h", "10h-12h", "12h-14h", "14h-16h", "16h-18h"};

        JPanel grid = new JPanel(new GridLayout(horaires.length + 1, jours.length, 2, 2));
        grid.setBackground(GRAY_BG);

        // En-têtes jours
        for (String j : jours) {
            JLabel lbl = new JLabel(j, SwingConstants.CENTER);
            lbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
            lbl.setOpaque(true);
            lbl.setBackground(Color.WHITE);
            lbl.setBorder(BorderFactory.createLineBorder(GRAY_BORDER));
            grid.add(lbl);
        }

        // Données fictives
        String[][] occupations = {
            {"", "Essom M.\nSalle A", "",            "Marie K.\nSalle B", "",            ""},
            {"", "",                  "Paul N.\nSalle C", "",             "Anna R.\nSalle A", ""},
            {"", "",                  "",            "",                  "",            ""},
            {"", "Jean T.\nSalle B",  "",            "",                  "Cours\nSalle C", ""},
            {"", "",                  "",            "Réunion\nSalle A",  "",            ""},
        };

        Color[][] couleurs = {
            {null, GREEN_LIGHT,  null,        ORANGE_LIGHT, null,        null},
            {null, null,         GREEN_LIGHT, null,         GREEN_LIGHT, null},
            {null, null,         null,        null,         null,        null},
            {null, RED_LIGHT,    null,        null,         ORANGE_LIGHT,null},
            {null, null,         null,        GREEN_LIGHT,  null,        null},
        };

        for (int h = 0; h < horaires.length; h++) {
            // Colonne horaire
            JLabel hl = new JLabel(horaires[h], SwingConstants.CENTER);
            hl.setFont(new Font("Segoe UI", Font.PLAIN, 11));
            hl.setOpaque(true);
            hl.setBackground(Color.WHITE);
            hl.setBorder(BorderFactory.createLineBorder(GRAY_BORDER));
            grid.add(hl);

            for (int j = 1; j < jours.length; j++) {
                String txt = occupations[h][j-1];
                JLabel cell = new JLabel(txt.isEmpty() ? "" : "<html><center>" +
                    txt.replace("\n", "<br>") + "</center></html>", SwingConstants.CENTER);
                cell.setFont(new Font("Segoe UI", Font.PLAIN, 11));
                cell.setOpaque(true);
                cell.setBackground(txt.isEmpty() ? Color.WHITE : couleurs[h][j-1]);
                cell.setBorder(BorderFactory.createLineBorder(GRAY_BORDER));
                grid.add(cell);
            }
        }
        return grid;
    }

    // ========== ONGLET 3 : SALLES ==========
    private JPanel buildSallesPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(GRAY_BG);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1;

        // Bouton ajouter
        gbc.gridy = 0; gbc.weighty = 0;
        gbc.insets = new Insets(0, 0, 12, 0);
        JPanel topBar = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        topBar.setOpaque(false);
        topBar.add(buildBlueButton("+ Ajouter une salle"));
        panel.add(topBar, gbc);

        // Liste des salles
        gbc.gridy = 1; gbc.weighty = 1;
        gbc.insets = new Insets(0, 0, 0, 0);
        panel.add(buildListeSalles(), gbc);

        return panel;
    }

    private JPanel buildListeSalles() {
        JPanel liste = new JPanel();
        liste.setLayout(new BoxLayout(liste, BoxLayout.Y_AXIS));
        liste.setOpaque(false);

        String[][] salles = {
            {"Salle A", "30 places", "Vidéoprojecteur, Tableau"},
            {"Salle B", "50 places", "Vidéoprojecteur, Climatisation"},
            {"Salle C", "20 places", "Tableau blanc"},
        };

        for (String[] s : salles) {
            JPanel card = new JPanel(new BorderLayout());
            card.setBackground(Color.WHITE);
            card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(GRAY_BORDER),
                BorderFactory.createEmptyBorder(14, 18, 14, 18)
            ));
            card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));

            JPanel info = new JPanel(new GridLayout(2, 1));
            info.setOpaque(false);
            JLabel nom = new JLabel(s[0]);
            nom.setFont(new Font("Segoe UI", Font.BOLD, 14));
            JLabel details = new JLabel(s[1] + " — " + s[2]);
            details.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            details.setForeground(GRAY_TEXT);
            info.add(nom); info.add(details);

            JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
            actions.setOpaque(false);
            JButton mod  = new JButton("Modifier");
            JButton bloc = new JButton("Bloquer");
            JButton sup  = new JButton("Supprimer");
            mod.setForeground(BLUE);   mod.setBorderPainted(false);  mod.setBackground(BLUE_LIGHT);
            bloc.setForeground(ORANGE); bloc.setBorderPainted(false); bloc.setBackground(ORANGE_LIGHT);
            sup.setForeground(RED);    sup.setBorderPainted(false);   sup.setBackground(RED_LIGHT);
            for (JButton b : new JButton[]{mod, bloc, sup}) {
                b.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                b.setFocusPainted(false);
            }
            actions.add(mod); actions.add(bloc); actions.add(sup);

            card.add(info, BorderLayout.WEST);
            card.add(actions, BorderLayout.EAST);
            liste.add(card);
            liste.add(Box.createVerticalStrut(8));
        }
        return liste;
    }

    // ========== ONGLET 4 : UTILISATEURS ==========
    private JPanel buildUtilisateursPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(GRAY_BG);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1;

        // Barre recherche
        gbc.gridy = 0; gbc.weighty = 0;
        gbc.insets = new Insets(0, 0, 12, 0);
        JPanel searchBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        searchBar.setOpaque(false);
        JTextField search = new JTextField(20);
        search.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        search.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(GRAY_BORDER),
            BorderFactory.createEmptyBorder(6, 10, 6, 10)
        ));
        searchBar.add(new JLabel("🔍 Rechercher :"));
        searchBar.add(search);
        panel.add(searchBar, gbc);

        // Tableau utilisateurs
        gbc.gridy = 1; gbc.weighty = 1;
        gbc.insets = new Insets(0, 0, 0, 0);
        panel.add(buildTableauUtilisateurs(), gbc);

        return panel;
    }

    private JScrollPane buildTableauUtilisateurs() {
        String[] cols = {"Nom", "Prénom", "Email", "Rôle", "Filière/Dept", "Statut", "Action"};
        Object[][] data = {
            {"Essom",  "Marcel", "essom@essom.cm",  "Étudiant",    "Informatique L2", "Actif",    ""},
            {"Marie",  "Kouam",  "marie@essom.cm",  "Étudiant",    "Réseau L3",       "Actif",    ""},
            {"Paul",   "Nguema", "paul@essom.cm",   "Étudiant",    "Génie Civil L1",  "Inactif",  ""},
            {"Robert", "Bella",  "robert@essom.cm", "Responsable", "Informatique",    "Actif",    ""},
        };

        DefaultTableModel model = new DefaultTableModel(data, cols) {
            public boolean isCellEditable(int r, int c) { return c == 6; }
        };

        JTable table = new JTable(model);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setRowHeight(38);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        table.getTableHeader().setBackground(GRAY_BG);
        table.setShowHorizontalLines(true);
        table.setGridColor(GRAY_BORDER);

        // Rendu statut
        table.getColumnModel().getColumn(5).setCellRenderer(new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(JTable t, Object v,
                    boolean sel, boolean foc, int r, int c) {
                JLabel lbl = new JLabel(v.toString(), SwingConstants.CENTER);
                lbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lbl.setOpaque(true);
                if ("Actif".equals(v.toString())) {
                    lbl.setBackground(GREEN_LIGHT); lbl.setForeground(GREEN);
                } else {
                    lbl.setBackground(RED_LIGHT); lbl.setForeground(RED);
                }
                return lbl;
            }
        });

        // Bouton Activer/Désactiver
        table.getColumnModel().getColumn(6).setCellRenderer(new TableCellRenderer() {
            public Component getTableCellRendererComponent(JTable t, Object v,
                    boolean sel, boolean foc, int r, int c) {
                String statut = (String) model.getValueAt(r, 5);
                JButton btn = new JButton("Actif".equals(statut) ? "Désactiver" : "Activer");
                btn.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                btn.setBackground("Actif".equals(statut) ? RED_LIGHT : GREEN_LIGHT);
                btn.setForeground("Actif".equals(statut) ? RED : GREEN);
                btn.setBorderPainted(false); btn.setFocusPainted(false);
                return btn;
            }
        });
        table.getColumnModel().getColumn(6).setCellEditor(new DefaultCellEditor(new JCheckBox()) {
            public Component getTableCellEditorComponent(JTable t, Object v,
                    boolean sel, int r, int c) {
                String statut = (String) model.getValueAt(r, 5);
                JButton btn = new JButton("Actif".equals(statut) ? "Désactiver" : "Activer");
                btn.addActionListener(e -> {
                    model.setValueAt("Actif".equals(statut) ? "Inactif" : "Actif", r, 5);
                    fireEditingStopped();
                });
                return btn;
            }
        });

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createLineBorder(GRAY_BORDER));
        return scroll;
    }

    // ========== UTILITAIRES ==========
    private JComboBox<String> styledCombo(String[] items) {
        JComboBox<String> cb = new JComboBox<>(items);
        cb.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        cb.setBackground(Color.WHITE);
        return cb;
    }

    private JButton buildBlueButton(String text) {
        JButton btn = new JButton(text);
        btn.setBackground(BLUE);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        return btn;
    }

    // ========== MAIN TEMPORAIRE ==========
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); }
            catch (Exception e) { e.printStackTrace(); }
            new RequestProcessingView();
        });
    }
}