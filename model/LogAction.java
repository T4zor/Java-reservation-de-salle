package model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LogAction {

    private int id;
    private int idUser;
    private String action;
    private String details;
    private LocalDateTime dateAction;

    // ── Champ joint pour l'affichage ────────
    private String nomUser;

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public LogAction() {}

    public LogAction(int idUser, String action, String details) {
        this.idUser = idUser;
        this.action = action;
        this.details = details;
        this.dateAction = LocalDateTime.now();
    }

    // Getters & Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getIdUser() { return idUser; }
    public void setIdUser(int idUser) { this.idUser = idUser; }

    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }

    public String getDetails() { return details; }
    public void setDetails(String details) { this.details = details; }

    public LocalDateTime getDateAction() { return dateAction; }
    public void setDateAction(LocalDateTime dateAction) { this.dateAction = dateAction; }

    public String getNomUser() { return nomUser; }
    public void setNomUser(String nomUser) { this.nomUser = nomUser; }

    public String getDateActionFormatee() {
        return dateAction != null ? dateAction.format(FMT) : "";
    }

    // CRUD Methods
    public boolean save() {
        String sql = "INSERT INTO LogAction (id_user, action, details, date_action) VALUES (?, ?, ?, ?)";
        try {
            var ps = DatabaseManager.getConnection().prepareStatement(sql);
            ps.setInt(1, idUser);
            ps.setString(2, action);
            ps.setString(3, details);
            ps.setString(4, dateAction.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            System.err.println("Erreur save LogAction : " + e.getMessage());
            return false;
        }
    }

    public static java.util.List<LogAction> findAll() {
        java.util.List<LogAction> logs = new java.util.ArrayList<>();
        String sql = "SELECT l.*, u.nom as nom_user, u.prenom as prenom_user FROM LogAction l JOIN User u ON l.id_user = u.id ORDER BY l.date_action DESC";
        try {
            var st = DatabaseManager.getConnection().createStatement();
            var rs = st.executeQuery(sql);
            while (rs.next()) {
                LogAction log = new LogAction();
                log.setId(rs.getInt("id"));
                log.setIdUser(rs.getInt("id_user"));
                log.setAction(rs.getString("action"));
                log.setDetails(rs.getString("details"));
                log.setDateAction(LocalDateTime.parse(rs.getString("date_action"), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                log.setNomUser(rs.getString("nom_user") + " " + rs.getString("prenom_user"));
                logs.add(log);
            }
        } catch (Exception e) {
            System.err.println("Erreur findAll LogAction : " + e.getMessage());
        }
        return logs;
    }

    @Override
    public String toString() {
        return "[" + getDateActionFormatee() + "] " + action + " - " + details;
    }
}