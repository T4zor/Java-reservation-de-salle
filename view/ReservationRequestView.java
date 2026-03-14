package view;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;

public class ReservationRequestView extends JFrame {

    private CardLayout cardLayout;
    private JPanel mainPanel;
    private JButton btnAccueil, btnDemande, btnNotifs, btnProfil;

    private static final Color BLUE        = new Color(0x18, 0x5F, 0xA5);
    private static final Color BLUE_LIGHT  = new Color(0xE6, 0xF1, 0xFB);
    private static final Color GRAY_BG     = new Color(0xF5, 0xF5, 0xF3);
    private static final Color GRAY_TEXT   = new Color(0x88, 0x87, 0x80);
    private static final Color GRAY_BORDER = new Color(0xD3, 0xD1, 0xC7);
    private static final Color GREEN       = new Color(0x0F, 0x6E, 0x56);
    private static final Color GREEN_LIGHT = new Color(0xEA, 0xF3, 0xDE);
    private static final Color ORANGE      = new Color(0xBA, 0x75, 0x17);
    private static final Color ORANGE_LIGHT= new Color(0xFA, 0xEE, 0xDA);
    private static final Color RED         = new Color(0xA3, 0x2D, 0x2D);
    private static final Color RED_LIGHT   = new Color(0xFC, 0xEB, 0xEB);

    public ReservationRequestView() {
        setTitle("Dashboard Etudiant");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 620);
        setMinimumSize(new Dimension(750, 500));
        setResizable(true);
        setLocationRelativeTo(null);
        getContentPane().setBackground(GRAY_BG);
        setLayout(new BorderLayout());

        add(buildNavbar(), BorderLayout.NORTH);

        cardLayout = new CardLayout();
        mainPanel  = new JPanel(cardLayout);
        mainPanel.setBackground(GRAY_BG);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        mainPanel.add(buildAccueil(),        "ACCUEIL");
        mainPanel.add(buildNouvelleDemande(), "DEMANDE");
        mainPanel.add(buildNotifications(),   "NOTIFS");
        mainPanel.add(buildProfil(),          "PROFIL");

        add(mainPanel, BorderLayout.CENTER);

        try { setIconImage(new ImageIcon("icon/accueil.png").getImage()); } catch (Exception ex) {}
    }

    // ─── NAVBAR ───────────────────────────────────────────
    private JPanel buildNavbar() {
        JPanel nav = new JPanel(new BorderLayout());
        nav.setBackground(Color.WHITE);
        nav.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, GRAY_BORDER),
            BorderFactory.createEmptyBorder(8, 16, 8, 16)));

        JLabel logo = new JLabel("Reservation de salles");
        logo.setFont(new Font("SansSerif", Font.BOLD, 15));

        JPanel navBtns = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
        navBtns.setBackground(Color.WHITE);

        btnAccueil = navBtn("Accueil", true);
        btnDemande = navBtn("Nouvelle demande", false);
        btnNotifs  = navBtn("Notifications", false);
        btnProfil  = navBtn("Profil", false);

        // Badge notification
        JPanel notifWrapper = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        notifWrapper.setBackground(Color.WHITE);
        notifWrapper.add(btnNotifs);
        JLabel badge = new JLabel("3");
        badge.setFont(new Font("SansSerif", Font.BOLD, 10));
        badge.setForeground(Color.WHITE);
        badge.setBackground(RED);
        badge.setOpaque(true);
        badge.setBorder(BorderFactory.createEmptyBorder(1, 5, 1, 5));
        notifWrapper.add(badge);

        navBtns.add(btnAccueil);
        navBtns.add(btnDemande);
        navBtns.add(notifWrapper);
        navBtns.add(btnProfil);

        // Avatar utilisateur
        JPanel userPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        userPanel.setBackground(Color.WHITE);
        JPanel avatar = new JPanel() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(BLUE_LIGHT);
                g2.fillOval(0, 0, 32, 32);
                g2.setColor(BLUE);
                g2.setFont(new Font("SansSerif", Font.BOLD, 11));
                FontMetrics fm = g2.getFontMetrics();
                String txt = "ES";
                g2.drawString(txt, (32 - fm.stringWidth(txt)) / 2, (32 + fm.getAscent()) / 2 - 2);
            }
        };
        avatar.setPreferredSize(new Dimension(32, 32));
        avatar.setBackground(Color.WHITE);
        JLabel userName = new JLabel("Essom · Etudiant");
        userName.setFont(new Font("SansSerif", Font.PLAIN, 12));
        userName.setForeground(GRAY_TEXT);
        userPanel.add(avatar);
        userPanel.add(userName);

        nav.add(logo,      BorderLayout.WEST);
        nav.add(navBtns,   BorderLayout.CENTER);
        nav.add(userPanel, BorderLayout.EAST);

        btnAccueil.addActionListener(e -> showPanel("ACCUEIL", btnAccueil));
        btnDemande.addActionListener(e -> showPanel("DEMANDE", btnDemande));
        btnNotifs.addActionListener(e  -> showPanel("NOTIFS",  btnNotifs));
        btnProfil.addActionListener(e  -> showPanel("PROFIL",  btnProfil));

        return nav;
    }

    // ─── ACCUEIL ──────────────────────────────────────────
    private JPanel buildAccueil() {
        JPanel p = new JPanel(new BorderLayout(0, 14));
        p.setBackground(GRAY_BG);

        // Stats
        JPanel stats = new JPanel(new GridLayout(1, 4, 12, 0));
        stats.setBackground(GRAY_BG);
        stats.add(statCard("Total demandes", "6",  Color.BLACK));
        stats.add(statCard("Acceptees",      "3",  GREEN));
        stats.add(statCard("En attente",     "2",  ORANGE));
        stats.add(statCard("Refusees",       "1",  RED));
        p.add(stats, BorderLayout.NORTH);

        // Tableau
        JPanel tableCard = card();
        tableCard.setLayout(new BorderLayout(0, 10));

        JPanel tableHeader = new JPanel(new BorderLayout());
        tableHeader.setBackground(Color.WHITE);
        JLabel tableTitle = new JLabel("Mes reservations");
        tableTitle.setFont(new Font("SansSerif", Font.BOLD, 14));
        JButton btnNouvelle = primaryBtn("+ Nouvelle demande");
        btnNouvelle.addActionListener(e -> showPanel("DEMANDE", btnDemande));
        tableHeader.add(tableTitle,  BorderLayout.WEST);
        tableHeader.add(btnNouvelle, BorderLayout.EAST);

        String[] cols = {"Salle", "Date", "Horaire", "Motif", "Statut", "Actions"};
        Object[][] data = {
            {"Salle A", "14 Mars 2026", "08h-10h", "TP Reseaux",    "Accepte",   ""},
            {"Salle B", "15 Mars 2026", "10h-12h", "Projet BD",     "En attente",""},
            {"Salle C", "16 Mars 2026", "14h-16h", "Expose",        "Refuse",    ""},
            {"Salle A", "17 Mars 2026", "09h-11h", "Cours rattrape","En attente",""},
        };

        DefaultTableModel model = new DefaultTableModel(data, cols) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable table = new JTable(model);
        table.setRowHeight(36);
        table.setFont(new Font("SansSerif", Font.PLAIN, 12));
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 12));
        table.getTableHeader().setBackground(GRAY_BG);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setSelectionBackground(BLUE_LIGHT);

        // Renderer pour colonne Statut
        table.getColumnModel().getColumn(4).setCellRenderer(new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(JTable t, Object v,
                    boolean sel, boolean foc, int r, int c) {
                JLabel l = new JLabel(v.toString());
                l.setFont(new Font("SansSerif", Font.BOLD, 11));
                l.setOpaque(true);
                l.setHorizontalAlignment(SwingConstants.CENTER);
                String s = v.toString();
                if      (s.equals("Accepte"))    { l.setBackground(GREEN_LIGHT);  l.setForeground(GREEN); }
                else if (s.equals("En attente")) { l.setBackground(ORANGE_LIGHT); l.setForeground(ORANGE); }
                else                             { l.setBackground(RED_LIGHT);    l.setForeground(RED); }
                l.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));
                return l;
            }
        });

        // Renderer pour colonne Actions
        table.getColumnModel().getColumn(5).setCellRenderer(new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(JTable t, Object v,
                    boolean sel, boolean foc, int r, int c) {
                JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 4, 4));
                panel.setBackground(Color.WHITE);
                String statut = t.getValueAt(r, 4).toString();
                JButton btnDetail = smallBtn("Detail");
                panel.add(btnDetail);
                if (statut.equals("En attente")) {
                    JButton btnAnnuler = smallBtnRed("Annuler");
                    panel.add(btnAnnuler);
                }
                if (statut.equals("Refuse")) {
                    JButton btnMotif = smallBtn("Voir motif");
                    panel.add(btnMotif);
                }
                return panel;
            }
        });

        // Alternance couleurs lignes
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(JTable t, Object v,
                    boolean sel, boolean foc, int r, int c) {
                Component comp = super.getTableCellRendererComponent(t, v, sel, foc, r, c);
                if (c == 4) return table.getColumnModel().getColumn(4)
                    .getCellRenderer().getTableCellRendererComponent(t, v, sel, foc, r, c);
                if (c == 5) return table.getColumnModel().getColumn(5)
                    .getCellRenderer().getTableCellRendererComponent(t, v, sel, foc, r, c);
                comp.setBackground(r % 2 == 0 ? Color.WHITE : GRAY_BG);
                comp.setFont(new Font("SansSerif", Font.PLAIN, 12));
                ((JLabel) comp).setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 8));
                return comp;
            }
        });

        table.getColumnModel().getColumn(0).setPreferredWidth(80);
        table.getColumnModel().getColumn(1).setPreferredWidth(110);
        table.getColumnModel().getColumn(2).setPreferredWidth(80);
        table.getColumnModel().getColumn(3).setPreferredWidth(120);
        table.getColumnModel().getColumn(4).setPreferredWidth(90);
        table.getColumnModel().getColumn(5).setPreferredWidth(140);

        tableCard.add(tableHeader,           BorderLayout.NORTH);
        tableCard.add(new JScrollPane(table), BorderLayout.CENTER);
        p.add(tableCard, BorderLayout.CENTER);
        return p;
    }

    // ─── NOUVELLE DEMANDE ─────────────────────────────────
    private JPanel buildNouvelleDemande() {
        JPanel p = new JPanel(new GridLayout(1, 2, 16, 0));
        p.setBackground(GRAY_BG);

        // Formulaire
        JPanel formCard = card();
        formCard.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0; gbc.gridx = 0; gbc.gridwidth = 2;
        int row = 0;

        JLabel formTitle = new JLabel("Nouvelle demande");
        formTitle.setFont(new Font("SansSerif", Font.BOLD, 14));
        gbc.gridy = row++; gbc.insets = new Insets(0,0,14,0);
        formCard.add(formTitle, gbc);

        gbc.gridy = row++; gbc.insets = new Insets(0,0,3,0);
        formCard.add(fieldLabel("Salle"), gbc);
        JComboBox<String> salleCombo = new JComboBox<>(new String[]{"Salle A (30 places)","Salle B (50 places)","Salle C (20 places)"});
        salleCombo.setFont(new Font("SansSerif", Font.PLAIN, 13));
        gbc.gridy = row++; gbc.insets = new Insets(0,0,10,0);
        formCard.add(salleCombo, gbc);

        gbc.gridy = row++; gbc.insets = new Insets(0,0,3,0);
        formCard.add(fieldLabel("Date"), gbc);
        JTextField dateField = textField();
        dateField.setText("JJ/MM/AAAA");
        gbc.gridy = row++; gbc.insets = new Insets(0,0,10,0);
        formCard.add(dateField, gbc);

        gbc.gridwidth = 1; gbc.weightx = 0.5;
        gbc.gridy = row++;
        gbc.gridx = 0; gbc.insets = new Insets(0,0,3,6);
        formCard.add(fieldLabel("Heure debut"), gbc);
        gbc.gridx = 1; gbc.insets = new Insets(0,6,3,0);
        formCard.add(fieldLabel("Heure fin"), gbc);

        gbc.gridy = row++;
        JTextField heureDebut = textField(); heureDebut.setText("08:00");
        JTextField heureFin   = textField(); heureFin.setText("10:00");
        gbc.gridx = 0; gbc.insets = new Insets(0,0,10,6);
        formCard.add(heureDebut, gbc);
        gbc.gridx = 1; gbc.insets = new Insets(0,6,10,0);
        formCard.add(heureFin, gbc);

        // Indicateur disponibilité
        gbc.gridx = 0; gbc.gridwidth = 2; gbc.weightx = 1.0;
        gbc.gridy = row++; gbc.insets = new Insets(0,0,10,0);
        JPanel dispoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 4));
        dispoPanel.setBackground(GREEN_LIGHT);
        dispoPanel.setBorder(BorderFactory.createLineBorder(new Color(0x9F,0xE1,0xCB), 1));
        JLabel dispoLabel = new JLabel("Creneau disponible");
        dispoLabel.setFont(new Font("SansSerif", Font.BOLD, 12));
        dispoLabel.setForeground(GREEN);
        dispoPanel.add(dispoLabel);
        formCard.add(dispoPanel, gbc);

        gbc.gridy = row++; gbc.insets = new Insets(0,0,3,0);
        formCard.add(fieldLabel("Motif"), gbc);
        JTextArea motifArea = new JTextArea(3, 20);
        motifArea.setFont(new Font("SansSerif", Font.PLAIN, 13));
        motifArea.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(GRAY_BORDER, 1),
            BorderFactory.createEmptyBorder(6, 10, 6, 10)));
        motifArea.setLineWrap(true);
        gbc.gridy = row++; gbc.insets = new Insets(0,0,14,0);
        formCard.add(new JScrollPane(motifArea), gbc);

        gbc.gridy = row++; gbc.insets = new Insets(0,0,0,0);
        JButton btnEnvoyer = primaryBtn("Envoyer la demande");
        btnEnvoyer.addActionListener(e ->
            JOptionPane.showMessageDialog(this, "Demande envoyee avec succes !", "Succes", JOptionPane.INFORMATION_MESSAGE));
        formCard.add(btnEnvoyer, gbc);

        gbc.gridy = row; gbc.weighty = 1.0;
        formCard.add(new JLabel(), gbc);

        // Calendrier disponibilité
        JPanel calCard = card();
        calCard.setLayout(new BorderLayout(0, 10));
        JLabel calTitle = new JLabel("Disponibilite - Salle A - Mars 2026");
        calTitle.setFont(new Font("SansSerif", Font.BOLD, 14));
        calCard.add(calTitle, BorderLayout.NORTH);
        calCard.add(buildCalendar(), BorderLayout.CENTER);

        p.add(formCard);
        p.add(calCard);
        return p;
    }

    private JPanel buildCalendar() {
        JPanel cal = new JPanel(new GridLayout(7, 7, 3, 3));
        cal.setBackground(Color.WHITE);
        String[] jours = {"L","M","M","J","V","S","D"};
        for (String j : jours) {
            JLabel l = new JLabel(j, SwingConstants.CENTER);
            l.setFont(new Font("SansSerif", Font.BOLD, 11));
            l.setForeground(GRAY_TEXT);
            cal.add(l);
        }
        // Jours vides au début
        for (int i = 0; i < 4; i++) cal.add(new JLabel());

        int[] occupes = {5, 8, 13, 15, 21, 27};
        int[] miens   = {14};
        for (int d = 1; d <= 31; d++) {
            JLabel cell = new JLabel(String.valueOf(d), SwingConstants.CENTER);
            cell.setFont(new Font("SansSerif", Font.PLAIN, 11));
            cell.setOpaque(true);
            boolean occupe = false, mien = false;
            for (int o : occupes) if (o == d) { occupe = true; break; }
            for (int m : miens)   if (m == d) { mien   = true; break; }
            if      (mien)   { cell.setBackground(BLUE_LIGHT);   cell.setForeground(BLUE); }
            else if (occupe) { cell.setBackground(RED_LIGHT);    cell.setForeground(RED); }
            else             { cell.setBackground(GREEN_LIGHT);  cell.setForeground(GREEN); }
            cal.add(cell);
        }

        JPanel wrapper = new JPanel(new BorderLayout(0, 8));
        wrapper.setBackground(Color.WHITE);
        wrapper.add(cal, BorderLayout.CENTER);

        JPanel legend = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        legend.setBackground(Color.WHITE);
        legend.add(legendItem(GREEN_LIGHT, GREEN,   "Disponible"));
        legend.add(legendItem(RED_LIGHT,   RED,     "Occupe"));
        legend.add(legendItem(BLUE_LIGHT,  BLUE,    "Ma reservation"));
        wrapper.add(legend, BorderLayout.SOUTH);
        return wrapper;
    }

    private JPanel legendItem(Color bg, Color fg, String text) {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
        p.setBackground(Color.WHITE);
        JPanel dot = new JPanel();
        dot.setPreferredSize(new Dimension(12, 12));
        dot.setBackground(bg);
        dot.setBorder(BorderFactory.createLineBorder(fg, 1));
        JLabel l = new JLabel(text);
        l.setFont(new Font("SansSerif", Font.PLAIN, 11));
        l.setForeground(GRAY_TEXT);
        p.add(dot); p.add(l);
        return p;
    }

    // ─── NOTIFICATIONS ────────────────────────────────────
    private JPanel buildNotifications() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(GRAY_BG);

        JPanel notifCard = card();
        notifCard.setLayout(new BorderLayout(0, 12));

        JLabel title = new JLabel("Notifications");
        title.setFont(new Font("SansSerif", Font.BOLD, 14));
        notifCard.add(title, BorderLayout.NORTH);

        JPanel list = new JPanel();
        list.setLayout(new BoxLayout(list, BoxLayout.Y_AXIS));
        list.setBackground(Color.WHITE);

        list.add(notifItem(GREEN, GREEN_LIGHT, "Demande acceptee - Salle A",
            "14 Mars 2026 · 08h00 - 10h00", "Il y a 2 heures"));
        list.add(Box.createVerticalStrut(8));
        list.add(notifItem(RED, RED_LIGHT, "Demande refusee - Salle C",
            "Motif : Creneau deja attribue a un autre groupe", "Hier a 15h30"));
        list.add(Box.createVerticalStrut(8));
        list.add(notifItem(ORANGE, ORANGE_LIGHT, "Demande en cours d'examen - Salle B",
            "15 Mars 2026 · 10h00 - 12h00", "Hier a 09h00"));

        notifCard.add(list, BorderLayout.CENTER);

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(GRAY_BG);
        wrapper.add(notifCard, BorderLayout.NORTH);
        p.add(wrapper, BorderLayout.CENTER);
        return p;
    }

    private JPanel notifItem(Color fg, Color bg, String titre, String desc, String time) {
        JPanel p = new JPanel(new GridBagLayout());
        p.setBackground(bg);
        p.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 3, 0, 0, fg),
            BorderFactory.createEmptyBorder(10, 12, 10, 12)));
        p.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0; gbc.gridx = 0;

        JLabel lTitre = new JLabel(titre);
        lTitre.setFont(new Font("SansSerif", Font.BOLD, 13));
        lTitre.setForeground(fg);
        gbc.gridy = 0; gbc.insets = new Insets(0,0,2,0);
        p.add(lTitre, gbc);

        JLabel lDesc = new JLabel(desc);
        lDesc.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lDesc.setForeground(fg);
        gbc.gridy = 1; gbc.insets = new Insets(0,0,2,0);
        p.add(lDesc, gbc);

        JLabel lTime = new JLabel(time);
        lTime.setFont(new Font("SansSerif", Font.PLAIN, 11));
        lTime.setForeground(GRAY_TEXT);
        gbc.gridy = 2; gbc.insets = new Insets(0,0,0,0);
        p.add(lTime, gbc);

        return p;
    }

    // ─── PROFIL ───────────────────────────────────────────
    private JPanel buildProfil() {
        JPanel p = new JPanel(new GridBagLayout());
        p.setBackground(GRAY_BG);

        JPanel wrapper = new JPanel(new GridLayout(1, 2, 16, 0));
        wrapper.setBackground(GRAY_BG);
        wrapper.setPreferredSize(new Dimension(700, 300));

        // Carte avatar
        JPanel avatarCard = card();
        avatarCard.setLayout(new BoxLayout(avatarCard, BoxLayout.Y_AXIS));

        JPanel avatar = new JPanel() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(BLUE_LIGHT);
                g2.fillOval(0, 0, 56, 56);
                g2.setColor(BLUE);
                g2.setFont(new Font("SansSerif", Font.BOLD, 16));
                FontMetrics fm = g2.getFontMetrics();
                String txt = "ES";
                g2.drawString(txt, (56 - fm.stringWidth(txt)) / 2, (56 + fm.getAscent()) / 2 - 3);
            }
        };
        avatar.setPreferredSize(new Dimension(56, 56));
        avatar.setMaximumSize(new Dimension(56, 56));
        avatar.setBackground(Color.WHITE);
        avatar.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel name = new JLabel("Essom Marcel");
        name.setFont(new Font("SansSerif", Font.BOLD, 15));
        name.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel role = new JLabel("Etudiant · Informatique");
        role.setFont(new Font("SansSerif", Font.PLAIN, 12));
        role.setForeground(GRAY_TEXT);
        role.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel niveau = new JLabel("Licence 3");
        niveau.setFont(new Font("SansSerif", Font.BOLD, 11));
        niveau.setForeground(BLUE);
        niveau.setBackground(BLUE_LIGHT);
        niveau.setOpaque(true);
        niveau.setBorder(BorderFactory.createEmptyBorder(3, 10, 3, 10));
        niveau.setAlignmentX(Component.CENTER_ALIGNMENT);

        avatarCard.add(Box.createVerticalStrut(20));
        avatarCard.add(avatar);
        avatarCard.add(Box.createVerticalStrut(12));
        avatarCard.add(name);
        avatarCard.add(Box.createVerticalStrut(4));
        avatarCard.add(role);
        avatarCard.add(Box.createVerticalStrut(10));
        avatarCard.add(niveau);

        // Carte infos
        JPanel infoCard = card();
        infoCard.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0; gbc.gridx = 0;

        JLabel infoTitle = new JLabel("Mes informations");
        infoTitle.setFont(new Font("SansSerif", Font.BOLD, 14));
        gbc.gridy = 0; gbc.insets = new Insets(0,0,14,0);
        infoCard.add(infoTitle, gbc);

        String[][] infos = {
            {"Email",            "essom@univ.cm"},
            {"Numero etudiant",  "20240123"},
            {"Filiere",          "Informatique"},
            {"Niveau",           "Licence 3"},
        };
        for (int i = 0; i < infos.length; i++) {
            JPanel row = new JPanel(new BorderLayout());
            row.setBackground(Color.WHITE);
            row.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, GRAY_BORDER));
            JLabel key = new JLabel(infos[i][0]);
            key.setFont(new Font("SansSerif", Font.PLAIN, 12));
            key.setForeground(GRAY_TEXT);
            JLabel val = new JLabel(infos[i][1]);
            val.setFont(new Font("SansSerif", Font.PLAIN, 12));
            val.setHorizontalAlignment(SwingConstants.RIGHT);
            row.add(key, BorderLayout.WEST);
            row.add(val, BorderLayout.EAST);
            row.setPreferredSize(new Dimension(0, 32));
            gbc.gridy = i + 1; gbc.insets = new Insets(0,0,0,0);
            infoCard.add(row, gbc);
        }

        gbc.gridy = infos.length + 1; gbc.insets = new Insets(14,0,0,0);
        JButton btnMdp = new JButton("Modifier le mot de passe");
        btnMdp.setFont(new Font("SansSerif", Font.PLAIN, 13));
        btnMdp.setFocusPainted(false);
        btnMdp.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnMdp.addActionListener(e -> {
            JPasswordField np = new JPasswordField();
            JPasswordField cp = new JPasswordField();
            JPanel panel = new JPanel(new GridLayout(2, 2, 6, 6));
            panel.add(new JLabel("Nouveau mot de passe :"));
            panel.add(np);
            panel.add(new JLabel("Confirmer :"));
            panel.add(cp);
            int result = JOptionPane.showConfirmDialog(this, panel,
                "Modifier le mot de passe", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION)
                JOptionPane.showMessageDialog(this, "Mot de passe modifie !");
        });
        infoCard.add(btnMdp, gbc);

        gbc.gridy = infos.length + 2; gbc.weighty = 1.0;
        infoCard.add(new JLabel(), gbc);

        wrapper.add(avatarCard);
        wrapper.add(infoCard);

        p.add(wrapper);
        return p;
    }

    // ─── UTILITAIRES ──────────────────────────────────────
    private void showPanel(String name, JButton active) {
        cardLayout.show(mainPanel, name);
        for (JButton b : new JButton[]{btnAccueil, btnDemande, btnNotifs, btnProfil}) {
            b.setBackground(Color.WHITE);
            b.setForeground(new Color(0x5F, 0x5E, 0x5A));
        }
        active.setBackground(BLUE_LIGHT);
        active.setForeground(BLUE);
    }

    private JPanel card() {
        JPanel p = new JPanel();
        p.setBackground(Color.WHITE);
        p.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(GRAY_BORDER, 1),
            BorderFactory.createEmptyBorder(16, 16, 16, 16)));
        return p;
    }

    private JPanel statCard(String label, String value, Color valueColor) {
        JPanel p = new JPanel();
        p.setBackground(GRAY_BG);
        p.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(GRAY_BORDER, 1),
            BorderFactory.createEmptyBorder(12, 14, 12, 14)));
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lbl.setForeground(GRAY_TEXT);
        JLabel val = new JLabel(value);
        val.setFont(new Font("SansSerif", Font.BOLD, 24));
        val.setForeground(valueColor);
        p.add(lbl);
        p.add(Box.createVerticalStrut(4));
        p.add(val);
        return p;
    }

    private JButton navBtn(String text, boolean active) {
        JButton b = new JButton(text);
        b.setFont(new Font("SansSerif", Font.PLAIN, 13));
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        b.setOpaque(true);
        if (active) { b.setBackground(BLUE_LIGHT); b.setForeground(BLUE); }
        else        { b.setBackground(Color.WHITE); b.setForeground(new Color(0x5F,0x5E,0x5A)); }
        b.setBorder(BorderFactory.createEmptyBorder(6, 12, 6, 12));
        return b;
    }

    private JButton primaryBtn(String text) {
        JButton b = new JButton(text);
        b.setFont(new Font("SansSerif", Font.BOLD, 13));
        b.setBackground(BLUE);
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setOpaque(true);
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        b.setBorder(BorderFactory.createEmptyBorder(8, 14, 8, 14));
        b.setPreferredSize(new Dimension(0, 38));
        return b;
    }

    private JButton smallBtn(String text) {
        JButton b = new JButton(text);
        b.setFont(new Font("SansSerif", Font.PLAIN, 11));
        b.setFocusPainted(false);
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        b.setBorder(BorderFactory.createEmptyBorder(3, 8, 3, 8));
        return b;
    }

    private JButton smallBtnRed(String text) {
        JButton b = new JButton(text);
        b.setFont(new Font("SansSerif", Font.PLAIN, 11));
        b.setBackground(RED_LIGHT);
        b.setForeground(RED);
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setOpaque(true);
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        b.setBorder(BorderFactory.createEmptyBorder(3, 8, 3, 8));
        return b;
    }

    private JTextField textField() {
        JTextField f = new JTextField();
        f.setFont(new Font("SansSerif", Font.PLAIN, 13));
        f.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(GRAY_BORDER, 1),
            BorderFactory.createEmptyBorder(7, 10, 7, 10)));
        return f;
    }

    private JLabel fieldLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("SansSerif", Font.PLAIN, 12));
        l.setForeground(GRAY_TEXT);
        return l;
    }

    // TODO: enlever ce main quand tout sera fini
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ReservationRequestView().setVisible(true));
    }
}