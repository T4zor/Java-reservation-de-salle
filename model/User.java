package model;

public class User {

    // ── Attributs — correspondent exactement à la table BD ──
    private int    id;
    private String nom;
    private String prenom;
    private String email;
    private String role;       // "enseignant", "etudiant", "responsable"
    private String motDePasse;

    // ── Constructeurs ─────────────────────────────────────
    public User() {}

    public User(String nom, String prenom, String email,
                String role, String motDePasse) {
        this.nom        = nom;
        this.prenom     = prenom;
        this.email      = email;
        this.role       = role;
        this.motDePasse = motDePasse;
    }

    // ── Getters & Setters ─────────────────────────────────
    public int getId()          { return id; }
    public void setId(int id)   { this.id = id; }

    public String getNom()            { return nom; }
    public void setNom(String nom)    { this.nom = nom; }

    public String getPrenom()               { return prenom; }
    public void setPrenom(String prenom)    { this.prenom = prenom; }

    public String getEmail()              { return email; }
    public void setEmail(String email)    { this.email = email; }

    public String getRole()             { return role; }
    public void setRole(String role)    { this.role = role; }

    public String getMotDePasse()                   { return motDePasse; }
    public void setMotDePasse(String motDePasse)    { this.motDePasse = motDePasse; }

    // ── Méthodes utilitaires ──────────────────────────────
    public String getNomComplet() {
        return prenom + " " + nom;
    }

    public boolean isResponsable() {
        return "responsable".equalsIgnoreCase(role);
    }

    public boolean isEnseignant() {
        return "enseignant".equalsIgnoreCase(role);
    }

    public boolean isEtudiant() {
        return "etudiant".equalsIgnoreCase(role);
    }

    @Override
    public String toString() {
        return getNomComplet() + " (" + role + ")";
    }
}