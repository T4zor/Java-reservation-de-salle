package model;

import java.sql.*;

public class DatabaseManager {
    private static final String URL = "jdbc:sqlite:reservation.db";
    private static Connection connection;

    // Méthode pour établir la connexion
    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(URL);
            createTables();
        }
        return connection;
    }

    // Création des tables si elles n'existent pas
    private static void createTables() {
        String createUserTable = """
            CREATE TABLE IF NOT EXISTS User (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                nom TEXT NOT NULL,
                prenom TEXT NOT NULL,
                email TEXT UNIQUE NOT NULL,
                role TEXT NOT NULL,  -- 'enseignant', 'etudiant', 'responsable'
                mot_de_passe TEXT NOT NULL
            );
        """;

        String createSalleTable = """
            CREATE TABLE IF NOT EXISTS Salle (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                nom TEXT NOT NULL,
                capacite INTEGER NOT NULL,
                equipements TEXT
            );
        """;

        String createReservationTable = """
            CREATE TABLE IF NOT EXISTS Reservation (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                id_user INTEGER NOT NULL,
                id_salle INTEGER NOT NULL,
                date_debut TEXT NOT NULL,  -- Format YYYY-MM-DD HH:MM
                date_fin TEXT NOT NULL,
                statut TEXT NOT NULL DEFAULT 'en attente',  -- 'en attente', 'acceptee', 'refusee'
                motif TEXT,
                FOREIGN KEY (id_user) REFERENCES User(id),
                FOREIGN KEY (id_salle) REFERENCES Salle(id)
            );
        """;

        try (Statement stmt = connection.createStatement()) {
            stmt.execute(createUserTable);
            stmt.execute(createSalleTable);
            stmt.execute(createReservationTable);

            // Ajouter la colonne motif si elle n'existe pas (pour compatibilité)
            try {
                stmt.execute("ALTER TABLE Reservation ADD COLUMN motif TEXT;");
            } catch (SQLException e) {
                // Colonne existe déjà, ignorer l'erreur
                if (!e.getMessage().contains("duplicate column name")) {
                    System.out.println("Erreur lors de l'ajout de la colonne motif : " + e.getMessage());
                }
            }

            // Insérer un utilisateur par défaut si la table est vide
            String insertDefaultUser = """
                INSERT OR IGNORE INTO User (nom, prenom, email, role, mot_de_passe)
                VALUES ('Admin', 'Admin', 'admin@example.com', 'responsable', 'admin');
            """;
            stmt.execute(insertDefaultUser);
        } catch (SQLException e) {
            System.out.println("Erreur lors de la création des tables : " + e.getMessage());
        }
    }

    // Fermer la connexion
    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                System.out.println("Erreur lors de la fermeture : " + e.getMessage());
            }
        }
    }
}
