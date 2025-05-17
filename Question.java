package com.example.pfe;

import java.io.Serializable;
import java.util.List;

public class Question implements Serializable {
    private String question;
    private List<String> options;
    private String correctAnswer;
    private String timeLimit;
    private String points;

    // Constructeur vide requis pour Firebase
    public Question() {
    }

    // Getters et Setters
    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public List<String> getOptions() {
        return options;
    }

    public void setOptions(List<String> options) {
        this.options = options;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public String getTimeLimit() {
        return timeLimit;
    }

    public void setTimeLimit(String timeLimit) {
        this.timeLimit = timeLimit;
    }

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }
}