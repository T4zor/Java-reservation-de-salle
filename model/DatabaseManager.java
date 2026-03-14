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
                FOREIGN KEY (id_user) REFERENCES User(id),
                FOREIGN KEY (id_salle) REFERENCES Salle(id)
            );
        """;

        try (Statement stmt = connection.createStatement()) {
            stmt.execute(createUserTable);
            stmt.execute(createSalleTable);
            stmt.execute(createReservationTable);
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
