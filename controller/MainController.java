package controller;

import model.*;
import view.*;

import java.sql.SQLException;

public class MainController {
    public void start() {
        // Test de la connexion BD
        try {
            DatabaseManager.getConnection();
            System.out.println("Connexion à la base de données réussie !");
        } catch (SQLException e) {
            System.out.println("Erreur de connexion à la BD : " + e.getMessage());
            return; // Arrête si la BD ne fonctionne pas
        }

        // Affiche la fenêtre de connexion
        LoginView loginView = new LoginView(this);
        loginView.setVisible(true);
    }

    // Méthodes pour naviguer vers les autres vues
    public void showReservationRequestView() {
        ReservationRequestView view = new ReservationRequestView();
        view.setVisible(true);
    }

    public void showReservationStatusView() {
        ReservationStatusView view = new ReservationStatusView();
        view.setVisible(true);
    }

    public void showRoomManagementView() {
        RoomManagementView view = new RoomManagementView();
        view.setVisible(true);
    }

    public void showRequestProcessingView() {
        RequestProcessingView view = new RequestProcessingView();
        view.setVisible(true);
    }
}
