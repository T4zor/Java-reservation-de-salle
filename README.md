# Java Room Reservation Management System

Projet académique - ICT-205

## Description
Application Java Swing pour la gestion des réservations de salles avec base de données SQLite.
Permet la gestion des utilisateurs, salles, et réservations avec validation des conflits.

## Fonctionnalités
- ✅ Authentification utilisateurs (Admin/Étudiant)
- ✅ Gestion des utilisateurs (CRUD)
- ✅ Gestion des salles (CRUD)
- ✅ Demandes de réservation avec validation des conflits
- ✅ Approbation/Rejet des réservations
- ✅ Consultation des statuts de réservation
- ✅ Tables triables
- ✅ Persistance des sessions

## Architecture
- **MVC** : Modèle-Vue-Contrôleur
- **Base de données** : SQLite avec JDBC
- **Interface** : Java Swing

## Compilation et Exécution

### Prérequis
- Java 8+
- Bibliothèque SQLite JDBC (incluse dans `lib/`)

### Compilation
```bash
javac -cp "lib/*" -d . *.java controller/*.java model/*.java view/*.java
```

### Exécution
```bash
java -cp ".;lib/*" Main
```

## Utilisateur par défaut
- **Email** : admin@example.com
- **Mot de passe** : admin
- **Rôle** : responsable

## Structure du projet
```
├── Main.java                 # Point d'entrée
├── controller/
│   └── MainController.java   # Logique principale
├── model/
│   ├── DatabaseManager.java  # Gestion BD
│   ├── User.java            # Modèle Utilisateur
│   ├── Room.java            # Modèle Salle
│   └── Reservation.java     # Modèle Réservation
├── view/                     # Interfaces Swing
│   ├── LoginView.java
│   ├── MenuResponsableView.java
│   ├── MenuEtudiantView.java
│   ├── UserManagementView.java
│   ├── RoomManagementView.java
│   ├── ReservationRequestView.java
│   ├── ReservationStatusView.java
│   └── RequestProcessingView.java
└── lib/                      # Bibliothèque SQLite
    └── sqlite-jdbc-3.51.2.0.jar
```

## Équipe
- [Vos noms ici]

## Remarques
- La base de données `reservation.db` est créée automatiquement
- Les sessions sont sauvegardées dans `session.txt`