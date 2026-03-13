package controller;

import view.*;

public class MainController {
    public void start() {
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
