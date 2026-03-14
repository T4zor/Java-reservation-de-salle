package model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Reservation {

    // ── Statuts possibles ─────────────────────────────────
    public static final String STATUT_ATTENTE  = "en attente";
    public static final String STATUT_ACCEPTEE = "acceptee";
    public static final String STATUT_REFUSEE  = "refusee";

    private static final DateTimeFormatter FMT =
        DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    // ── Attributs — correspondent exactement à la table BD ──
    private int           id;
    private int           idUser;
    private int           idSalle;
    private LocalDateTime dateDebut;
    private LocalDateTime dateFin;
    private String        statut;

    // ── Champs joints pour l'affichage (pas en BD) ────────
    private String nomUser;
    private String nomSalle;

    // ── Constructeurs ─────────────────────────────────────
    public Reservation() {
        this.statut = STATUT_ATTENTE; // statut par défaut
    }

    public Reservation(int idUser, int idSalle,
                       LocalDateTime dateDebut, LocalDateTime dateFin) {
        this();
        this.idUser    = idUser;
        this.idSalle   = idSalle;
        this.dateDebut = dateDebut;
        this.dateFin   = dateFin;
    }

    // ── Getters & Setters ─────────────────────────────────
    public int getId()        { return id; }
    public void setId(int id) { this.id = id; }

    public int getIdUser()          { return idUser; }
    public void setIdUser(int id)   { this.idUser = id; }

    public int getIdSalle()           { return idSalle; }
    public void setIdSalle(int id)    { this.idSalle = id; }

    public LocalDateTime getDateDebut()               { return dateDebut; }
    public void setDateDebut(LocalDateTime dateDebut) { this.dateDebut = dateDebut; }

    public LocalDateTime getDateFin()             { return dateFin; }
    public void setDateFin(LocalDateTime dateFin) { this.dateFin = dateFin; }

    public String getStatut()             { return statut; }
    public void setStatut(String statut)  { this.statut = statut; }

    public String getNomUser()              { return nomUser; }
    public void setNomUser(String nomUser)  { this.nomUser = nomUser; }

    public String getNomSalle()               { return nomSalle; }
    public void setNomSalle(String nomSalle)  { this.nomSalle = nomSalle; }

    // ── Méthodes utilitaires ──────────────────────────────
    public String getDateDebutFormatee() {
        return dateDebut != null ? dateDebut.format(FMT) : "";
    }

    public String getDateFinFormatee() {
        return dateFin != null ? dateFin.format(FMT) : "";
    }

    public boolean isEnAttente() {
        return STATUT_ATTENTE.equalsIgnoreCase(statut);
    }

    public boolean isAcceptee() {
        return STATUT_ACCEPTEE.equalsIgnoreCase(statut);
    }

    public boolean isRefusee() {
        return STATUT_REFUSEE.equalsIgnoreCase(statut);
    }

    @Override
    public String toString() {
        return "Réservation #" + id
            + " | " + nomSalle
            + " | " + getDateDebutFormatee()
            + " → " + getDateFinFormatee()
            + " | " + statut;
    }
}