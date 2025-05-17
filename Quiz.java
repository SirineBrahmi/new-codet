package com.example.pfe;

import java.util.List;
import java.io.Serializable;

public class Quiz implements Serializable {
    private String quizId;
    private String titre;
    private String description;
    private String formateurId;
    private String idFormation;
    private String nomFormation;
    private String dateDepot;
    private String dateLimite;
    private List<Question> questions;

    // Constructeur vide requis pour Firebase
    public Quiz() {
    }

    // Getters et Setters
    public String getQuizId() {
        return quizId;
    }

    public void setQuizId(String quizId) {
        this.quizId = quizId;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFormateurId() {
        return formateurId;
    }

    public void setFormateurId(String formateurId) {
        this.formateurId = formateurId;
    }

    public String getIdFormation() {
        return idFormation;
    }

    public void setIdFormation(String idFormation) {
        this.idFormation = idFormation;
    }

    public String getNomFormation() {
        return nomFormation;
    }

    public void setNomFormation(String nomFormation) {
        this.nomFormation = nomFormation;
    }

    public String getDateDepot() {
        return dateDepot;
    }

    public void setDateDepot(String dateDepot) {
        this.dateDepot = dateDepot;
    }

    public String getDateLimite() {
        return dateLimite;
    }

    public void setDateLimite(String dateLimite) {
        this.dateLimite = dateLimite;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }
}