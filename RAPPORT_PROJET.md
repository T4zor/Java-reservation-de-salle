# Rapport de Projet - Système de Réservation de Salles



**Projet ICT-205** - Développement d'une application Java de gestion des réservations de salles



## Équipe de Développement



| Matricule | Nom Complet | Contribution (%) |
|-----------|-------------|------------------|
| 23U2485 | Kamdem Woyim Franck Romain | 100% |
| 24H2023 | Nnanga Clarisse Glorieuse | 80% |
| 24G2004 | Djikap Moyo Ange Prisca | 80% |
| 23U2968 | NJIEMOUN MFONDOUM YASSINE EL-BECHIR | 80% |
| 22W2432 | ESSOMBA ESSOMBA MARCEL | 90% |
| 23U2224 | Ayissi Nomo Raymond Claude | 80% |
| 23U2400 | NZAHA NEGHAM NAOMI SOLANGE | 90% |
| 25I2258 | TEMGOUA SOUNGMO KERRY CARREL | 90% |
| 24H2083 | Ngobo Abega Philibert | 80% |



<div style="page-break-after: always;"></div>

## Présentation du Projet

### Contexte
Ce projet académique consiste en le développement d'un système complet de gestion des réservations de salles pour établissements éducatifs. L'application permet aux étudiants de réserver des salles d'étude et aux responsables de gérer les demandes et les ressources.

### Objectifs Réalisés
-  Développement d'une interface graphique moderne avec Java Swing
-  Implémentation d'un système d'authentification avec rôles utilisateurs
-  Gestion complète des utilisateurs, salles et réservations (CRUD)
-  Validation automatique des conflits de réservation
-  Workflow d'approbation des demandes
-  Persistance des données avec SQLite

---

## Architecture Technique

### Technologies Utilisées
- **Langage** : Java 8+
- **Interface** : Java Swing avec composants stylisés
- **Base de données** : SQLite avec driver JDBC
- **Architecture** : Pattern MVC (Modèle-Vue-Contrôleur)

### Structure du Projet
```
├── controller/MainController.java    # Logique métier principale
├── model/                          # Classes métier (User, Room, Reservation)
├── view/                           # Interfaces utilisateur Swing
├── lib/sqlite-jdbc-3.51.2.0.jar    # Driver base de données
└── Main.java                       # Point d'entrée de l'application
```

---

## Fonctionnalités Principales

### 1. Gestion des Utilisateurs
- Authentification sécurisée (email/mot de passe)
- Deux rôles : Étudiant et Responsable
- Gestion CRUD des comptes utilisateur

### 2. Gestion des Salles
- Ajout/modification des salles avec capacités et équipements
- Visualisation des disponibilités
- Gestion des ressources matérielles

### 3. Système de Réservation
- **Deux types de réservation** :
  - Réservation horaire (date + heures début/fin)
  - Privatisation complète (période sur plusieurs jours)
- **Validation temps réel** des dates et conflits
- **Workflow d'approbation** par les responsables

### 4. Interface Utilisateur
- Design moderne et intuitif
- Calendrier intégré pour visualisation
- Tables triables et recherche
- Feedback visuel pour les erreurs

---

## Contraintes et Règles Métier

### Règles de Réservation
-  **Horaires autorisés** : 7h30 - 21h00
-  **Pas de réservation dans le passé**
-  **Détection automatique des conflits**
-  **Limites selon le rôle** : durée max 24h pour étudiants

### Sécurité
-  Mots de passe hashés (SHA-256)
-  Gestion des rôles et permissions
-  Logs d'audit des actions
-  Sessions utilisateur persistantes

---

## Déploiement et Utilisation

### Installation
```bash
# Prérequis : Java 8+ installé

# Compilation
javac -cp "lib/*" -d . *.java controller/*.java model/*.java view/*.java

# Exécution
java -cp ".;lib/*" Main
```

### Compte Administrateur par Défaut
- **Email** : admin@example.com
- **Mot de passe** : admin
- **Rôle** : Responsable (accès complet)

### Base de Données
- Créée automatiquement (`reservation.db`)
- Sessions sauvegardées (`session.txt`)
- Structure relationnelle avec tables users, rooms, reservations, logs

---

## Tests et Validation

### Scénarios Validés
-  Authentification et gestion des rôles
-  CRUD complet utilisateurs et salles
-  Réservations avec validation des conflits
-  Workflow d'approbation/rejet
-  Interface responsive et ergonomique

### Métriques de Qualité
- **Fonctionnalités** : 100% implémentées
- **Stabilité** : Application robuste sans crash
- **UX** : Interface intuitive et moderne
- **Performance** : Temps de réponse acceptable

---

## Répartition des Tâches

### Contributions par Domaine

| Domaine | Membres Principaux | Description |
|---------|-------------------|-------------|
| **Architecture & MVC** | NJIEMOUN MFONDOUM, Kamdem Woyim | Conception du pattern MVC et structure globale |
| **Base de Données** | ESSOMBA ESSOMBA, Ayissi Nomo | Schéma SQLite et requêtes JDBC |
| **Authentification** | Nnanga Clarisse, Djikap Moyo | Système login et gestion des rôles |
| **Interface Utilisateur** | NZAHA NEGHAM, TEMGOUA SOUNGMO | Design Swing et composants visuels |
| **Logique Métier** | Ngobo Abega, Kamdem Woyim | Validation et workflow des réservations |
| **Tests & Intégration** | Équipe complète | Validation fonctionnelle et débogage |

### Évaluation des Contributions
Les pourcentages reflètent l'implication effective de chaque membre mesurée par :
- Participation aux réunions de conception
- Quantité et qualité du code produit
- Implication dans les tests et débogage
- Contribution à la documentation

---

## Conclusion

Ce projet démontre la réussite collective de l'équipe dans la réalisation d'une application Java complète respectant les exigences fonctionnelles et techniques. L'architecture MVC a été correctement implémentée, les fonctionnalités demandées sont opérationnelles, et l'interface utilisateur offre une expérience satisfaisante.

### Points Forts
-  Architecture logicielle solide
-  Interface moderne et fonctionnelle
-  Validation métier complète
-  Persistance des données fiable
-  Travail d'équipe efficace

### Évolutions Possibles
- Migration vers JavaFX pour une interface plus moderne
- Ajout d'une API REST pour intégrations externes
- Implémentation de notifications par email
- Système de réservations récurrentes

**Date de remise :** Mars 2026
**État du projet :** Terminé et fonctionnel