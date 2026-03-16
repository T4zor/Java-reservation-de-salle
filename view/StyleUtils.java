package view;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class StyleUtils {

    // Palette de couleurs moderne et colorée
    public static final Color PRIMARY_COLOR = new Color(63, 81, 181);      // Bleu indigo principal
    public static final Color PRIMARY_LIGHT = new Color(92, 107, 192);     // Bleu plus clair
    public static final Color PRIMARY_DARK = new Color(48, 63, 159);       // Bleu plus foncé
    public static final Color SECONDARY_COLOR = new Color(156, 39, 176);   // Violet secondaire
    public static final Color ACCENT_COLOR = new Color(255, 87, 34);       // Orange accent
    public static final Color SUCCESS_COLOR = new Color(76, 175, 80);      // Vert succès
    public static final Color WARNING_COLOR = new Color(255, 152, 0);      // Orange warning
    public static final Color ERROR_COLOR = new Color(244, 67, 54);        // Rouge erreur
    public static final Color INFO_COLOR = new Color(33, 150, 243);        // Bleu info
    public static final Color BACKGROUND_COLOR = new Color(248, 249, 250); // Gris très clair
    public static final Color CARD_COLOR = new Color(255, 255, 255);       // Blanc
    public static final Color CARD_SHADOW = new Color(0, 0, 0, 10);        // Ombre légère
    public static final Color TEXT_PRIMARY = new Color(33, 33, 33);        // Noir/gris foncé
    public static final Color TEXT_SECONDARY = new Color(117, 117, 117);   // Gris
    public static final Color BORDER_COLOR = new Color(224, 224, 224);     // Gris bordure

    // Polices modernes
    public static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 24);
    public static final Font SUBTITLE_FONT = new Font("Segoe UI", Font.BOLD, 18);
    public static final Font BODY_FONT = new Font("Segoe UI", Font.PLAIN, 14);
    public static final Font BUTTON_FONT = new Font("Segoe UI", Font.BOLD, 14);
    public static final Font SMALL_FONT = new Font("Segoe UI", Font.PLAIN, 12);
    public static final Font EMOJI_FONT = new Font("Segoe UI Emoji", Font.PLAIN, 16);

    // Méthodes utilitaires pour styliser les composants
    public static void applyModernStyle(JFrame frame) {
        frame.getContentPane().setBackground(BACKGROUND_COLOR);
        frame.setBackground(BACKGROUND_COLOR);
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {
            // Ignore if Nimbus is not available
        }
    }

    public static void styleTitleLabel(JLabel label) {
        label.setFont(TITLE_FONT);
        label.setForeground(PRIMARY_COLOR);
        label.setBorder(BorderFactory.createEmptyBorder(20, 10, 10, 10));
    }

    public static void styleSubtitleLabel(JLabel label) {
        label.setFont(SUBTITLE_FONT);
        label.setForeground(TEXT_PRIMARY);
    }

    public static void styleBodyLabel(JLabel label) {
        label.setFont(BODY_FONT);
        label.setForeground(TEXT_SECONDARY);
    }

    public static JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(BUTTON_FONT);
        button.setBackground(PRIMARY_COLOR);
        button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createEmptyBorder(12, 24, 12, 24));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Effet hover
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(PRIMARY_DARK);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(PRIMARY_COLOR);
            }
        });

        return button;
    }

    public static JButton createSecondaryButton(String text) {
        JButton button = new JButton(text);
        button.setFont(BUTTON_FONT);
        button.setBackground(Color.WHITE);
        button.setForeground(PRIMARY_COLOR);
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(PRIMARY_COLOR, 1),
            BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Effet hover
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(245, 245, 245));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(Color.WHITE);
            }
        });

        return button;
    }

    public static JButton createAccentButton(String text) {
        JButton button = new JButton(text);
        button.setFont(BUTTON_FONT);
        button.setBackground(ACCENT_COLOR);
        button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createEmptyBorder(12, 24, 12, 24));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Effet hover
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(230, 74, 25));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(ACCENT_COLOR);
            }
        });

        return button;
    }

    public static JButton createSuccessButton(String text) {
        JButton button = new JButton(text);
        button.setFont(BUTTON_FONT);
        button.setBackground(SUCCESS_COLOR);
        button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createEmptyBorder(12, 24, 12, 24));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Effet hover
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(67, 160, 71));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(SUCCESS_COLOR);
            }
        });

        return button;
    }

    public static JButton createDangerButton(String text) {
        JButton button = new JButton(text);
        button.setFont(BUTTON_FONT);
        button.setBackground(ERROR_COLOR);
        button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createEmptyBorder(12, 24, 12, 24));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Effet hover
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(211, 47, 47));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(ERROR_COLOR);
            }
        });

        return button;
    }

    public static JTextField createStyledTextField() {
        JTextField field = new JTextField();
        field.setFont(BODY_FONT);
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR, 1),
            BorderFactory.createEmptyBorder(10, 12, 10, 12)
        ));
        field.setBackground(Color.WHITE);
        return field;
    }

    public static JPasswordField createStyledPasswordField() {
        JPasswordField field = new JPasswordField();
        field.setFont(BODY_FONT);
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR, 1),
            BorderFactory.createEmptyBorder(10, 12, 10, 12)
        ));
        field.setBackground(Color.WHITE);
        return field;
    }

    public static <T> JComboBox<T> createStyledComboBoxGeneric() {
        JComboBox<T> combo = new JComboBox<>();
        combo.setFont(BODY_FONT);
        combo.setBackground(Color.WHITE);
        combo.setBorder(BorderFactory.createLineBorder(BORDER_COLOR, 1));
        return combo;
    }

    public static JTextArea createStyledTextArea() {
        JTextArea area = new JTextArea();
        area.setFont(BODY_FONT);
        area.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR, 1),
            BorderFactory.createEmptyBorder(10, 12, 10, 12)
        ));
        area.setBackground(Color.WHITE);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        return area;
    }

    public static JPanel createCardPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(CARD_COLOR);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(CARD_SHADOW, 1),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        return panel;
    }

    public static JPanel createCenteredPanel(JPanel content) {
        JPanel wrapper = new JPanel(new GridBagLayout());
        wrapper.setBackground(BACKGROUND_COLOR);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.CENTER;
        wrapper.add(content, gbc);
        return wrapper;
    }

    public static JLabel createEmojiLabel(String emoji, String text) {
        JLabel label = new JLabel(emoji + " " + text);
        label.setFont(EMOJI_FONT);
        label.setForeground(TEXT_PRIMARY);
        return label;
    }

    public static JLabel createIconLabel(String emoji) {
        JLabel label = new JLabel(emoji);
        label.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 24));
        label.setHorizontalAlignment(SwingConstants.CENTER);
        return label;
    }

    public static JTable createStyledTable() {
        JTable table = new JTable();
        table.setFont(BODY_FONT);
        table.setRowHeight(30);
        table.setGridColor(new Color(224, 224, 224));
        table.setSelectionBackground(new Color(232, 240, 254));
        table.setSelectionForeground(TEXT_PRIMARY);
        table.getTableHeader().setFont(BUTTON_FONT);
        table.getTableHeader().setBackground(new Color(245, 245, 245));
        table.getTableHeader().setForeground(TEXT_PRIMARY);
        return table;
    }
}