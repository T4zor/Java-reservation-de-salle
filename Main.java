import javax.swing.*;

public class Main {
    public static void main(String[] args) {

        // Forcer encodage UTF-8
        System.setProperty("file.encoding", "UTF-8");

        // Look and feel systeme
        try {
            UIManager.setLookAndFeel(
                UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        // Lancer l'application
        SwingUtilities.invokeLater(() -> {
            new controller.MainController().start();
        });
    }
}