package com.example.pfe;

public class QuizResult {
    private String userId;
    private String quizId;
    private String userId_quizId; // Champ composite pour faciliter les requêtes
    private int score;
    private long timestamp;

    public QuizResult() {}

    public QuizResult(String userId, String quizId, int score) {
        this.userId = userId;
        this.quizId = quizId;
        this.userId_quizId = userId + "_" + quizId;
        this.score = score;
        this.timestamp = System.currentTimeMillis();
    }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public String getQuizId() { return quizId; }
    public void setQuizId(String quizId) { this.quizId = quizId; }
    public String getUserId_quizId() { return userId_quizId; }
    public void setUserId_quizId(String userId_quizId) { this.userId_quizId = userId_quizId; }
    public int getScore() { return score; }
    public void setScore(int score) { this.score = score; }
    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
}