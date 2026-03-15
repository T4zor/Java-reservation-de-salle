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
    private String        motif;

    // ── Champs joints pour l'affichage (pas en BD) ────────
    private String nomUser;
    private String nomSalle;

    // ── Constructeurs ─────────────────────────────────────
    public Reservation() {
        this.statut = STATUT_ATTENTE; // statut par défaut
    }

    public Reservation(int idUser, int idSalle,
                       LocalDateTime dateDebut, LocalDateTime dateFin, String motif) {
        this();
        this.idUser    = idUser;
        this.idSalle   = idSalle;
        this.dateDebut = dateDebut;
        this.dateFin   = dateFin;
        this.motif     = motif;
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

    public String getMotif()             { return motif; }
    public void setMotif(String motif)   { this.motif = motif; }

    public String getNomUser()              { return nomUser; }
    public void setNomUser(String nomUser)  { this.nomUser = nomUser; }

    public String getNomSalle()               { return nomSalle; }
    public void setNomSalle(String nomSalle)  { this.nomSalle = nomSalle; }

    // ── Méthodes CRUD ─────────────────────────────────────
    public boolean save() {
        String sql = "INSERT INTO Reservation (id_user, id_salle, date_debut, date_fin, statut, motif) VALUES (?, ?, ?, ?, ?, ?)";
        try {
            var ps = DatabaseManager.getConnection().prepareStatement(sql);
            ps.setInt(1, idUser);
            ps.setInt(2, idSalle);
            ps.setString(3, dateDebut.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            ps.setString(4, dateFin.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            ps.setString(5, statut);
            ps.setString(6, motif);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            System.err.println("Erreur save Reservation : " + e.getMessage());
            return false;
        }
    }

    public static java.util.List<Reservation> findAll() {
        java.util.List<Reservation> reservations = new java.util.ArrayList<>();
        String sql = "SELECT r.*, u.nom as nom_user, s.nom as nom_salle FROM Reservation r JOIN User u ON r.id_user = u.id JOIN Salle s ON r.id_salle = s.id ORDER BY r.date_debut";
        try {
            var ps = DatabaseManager.getConnection().prepareStatement(sql);
            var rs = ps.executeQuery();
            while (rs.next()) {
                Reservation r = new Reservation();
                r.setId(rs.getInt("id"));
                r.setIdUser(rs.getInt("id_user"));
                r.setIdSalle(rs.getInt("id_salle"));
                r.setDateDebut(LocalDateTime.parse(rs.getString("date_debut"), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                r.setDateFin(LocalDateTime.parse(rs.getString("date_fin"), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                r.setStatut(rs.getString("statut"));
                r.setMotif(rs.getString("motif"));
                r.setNomUser(rs.getString("nom_user"));
                r.setNomSalle(rs.getString("nom_salle"));
                reservations.add(r);
            }
        } catch (Exception e) {
            System.err.println("Erreur findAll Reservation : " + e.getMessage());
        }
        return reservations;
    }
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

    // ── Méthode statique pour vérifier les conflits ──────
    public static boolean verifierConflit(int salleId, LocalDateTime dateDebut, LocalDateTime dateFin) {
        String sql = "SELECT COUNT(*) FROM reservation WHERE id_salle = ? AND statut != ? " +
                     "AND ((date_debut < ? AND date_fin > ?) OR (date_debut < ? AND date_fin > ?) OR (date_debut >= ? AND date_fin <= ?))";

        try (var conn = DatabaseManager.getConnection();
             var stmt = conn.prepareStatement(sql)) {

            String dateDebutStr = dateDebut.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            String dateFinStr = dateFin.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

            stmt.setInt(1, salleId);
            stmt.setString(2, STATUT_REFUSEE);
            stmt.setString(3, dateFinStr);  // date_debut < dateFin (nouvelle)
            stmt.setString(4, dateDebutStr); // date_fin > dateDebut (nouvelle)
            stmt.setString(5, dateDebutStr); // date_debut < dateDebut (nouvelle)
            stmt.setString(6, dateFinStr);   // date_fin > dateFin (nouvelle)
            stmt.setString(7, dateDebutStr); // date_debut >= dateDebut (nouvelle)
            stmt.setString(8, dateFinStr);   // date_fin <= dateFin (nouvelle)

            try (var rs = stmt.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        } catch (Exception e) {
            System.err.println("Erreur vérification conflit : " + e.getMessage());
            return false; // En cas d'erreur, on permet la réservation pour éviter de bloquer
        }
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