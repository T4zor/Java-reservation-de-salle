package model;

public class User {

    private int id;
    private String nom;
    private String prenom;
    private String email;
    private String telephone;
    private String role; // "ADMIN" ou "USER"

    // ── Constructeurs ─────────────────────────────────────
    public User() {}

    public User(String nom, String prenom, String email,
                String telephone, String role) {
        this.nom       = nom;
        this.prenom    = prenom;
        this.email     = email;
        this.telephone = telephone;
        this.role      = role;
    }

    // ── Getters & Setters ─────────────────────────────────
    public int getId()           { return id; }
    public void setId(int id)    { this.id = id; }

    public String getNom()             { return nom; }
    public void setNom(String nom)     { this.nom = nom; }

    public String getPrenom()                { return prenom; }
    public void setPrenom(String prenom)     { this.prenom = prenom; }

    public String getEmail()               { return email; }
    public void setEmail(String email)     { this.email = email; }

    public String getTelephone()                 { return telephone; }
    public void setTelephone(String telephone)   { this.telephone = telephone; }

    public String getRole()            { return role; }
    public void setRole(String role)   { this.role = role; }

    // ── Méthodes utilitaires ──────────────────────────────
    public String getNomComplet() {
        return prenom + " " + nom;
    }

    @Override
    public String toString() {
        return getNomComplet() + " (" + role + ")";
    }
}