package com.example.pfe;

public class Resultat {
    private String resultatId;
    private String etudiantId;
    private String nom;
    private String prenom;
    private String email;
    private float noteQuiz;
    private float noteExamen;
    private String dateAjout;
    private String formateurId;
    private String formationId;

    public Resultat() {
    }

    // Getters et Setters (inchangés)
    public String getResultatId() {
        return resultatId;
    }

    public void setResultatId(String resultatId) {
        this.resultatId = resultatId;
    }

    public String getEtudiantId() {
        return etudiantId;
    }

    public void setEtudiantId(String etudiantId) {
        this.etudiantId = etudiantId;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public float getNoteQuiz() {
        return noteQuiz;
    }

    public void setNoteQuiz(float noteQuiz) {
        this.noteQuiz = noteQuiz;
    }

    public float getNoteExamen() {
        return noteExamen;
    }

    public void setNoteExamen(float noteExamen) {
        this.noteExamen = noteExamen;
    }

    public String getDateAjout() {
        return dateAjout;
    }

    public void setDateAjout(String dateAjout) {
        this.dateAjout = dateAjout;
    }

    public String getFormateurId() {
        return formateurId;
    }

    public void setFormateurId(String formateurId) {
        this.formateurId = formateurId;
    }

    public String getFormationId() {
        return formationId;
    }

    public void setFormationId(String formationId) {
        this.formationId = formationId;
    }
}