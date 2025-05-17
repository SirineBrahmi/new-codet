package com.example.pfe;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;
import java.util.List;

public class QuizActivity extends AppCompatActivity {
    private TextView questionText, timerText, questionCounterText;
    private RadioGroup optionsGroup;
    private Button nextButton;
    private Quiz quiz;
    private List<Question> questions;
    private int currentQuestionIndex = 0;
    private int score = 0;
    private CountDownTimer questionTimer;
    private long timeLeftInMillis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        quiz = (Quiz) getIntent().getSerializableExtra("QUIZ");
        if (quiz == null || quiz.getQuestions() == null) {
            finishWithError("Erreur: Quiz invalide");
            return;
        }

        questions = quiz.getQuestions();
        if (questions.isEmpty()) {
            finishWithError("Ce quiz ne contient aucune question");
            return;
        }

        checkIfQuizAlreadyPassed();
    }

    private void checkIfQuizAlreadyPassed() {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("quiz_results");
        ref.orderByChild("userId_quizId").equalTo(userId + "_" + quiz.getQuizId())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            finishWithError("Vous avez déjà passé ce quiz");
                        } else {
                            initializeQuiz();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        initializeQuiz();
                    }
                });
    }

    private void initializeQuiz() {
        setContentView(R.layout.activity_quiz);

        questionText = findViewById(R.id.questionText);
        timerText = findViewById(R.id.timerText);
        questionCounterText = findViewById(R.id.questionCounterText);
        optionsGroup = findViewById(R.id.optionsGroup);
        nextButton = findViewById(R.id.nextButton);

        showQuestion();

        nextButton.setOnClickListener(v -> {
            if (optionsGroup.getCheckedRadioButtonId() == -1) {
                Toast.makeText(this, "Veuillez sélectionner une réponse", Toast.LENGTH_SHORT).show();
                return;
            }
            checkAnswer();
            moveToNextQuestion();
        });
    }

    private void showQuestion() {
        Question currentQuestion = questions.get(currentQuestionIndex);

        // Reset timer for new question
        if (questionTimer != null) {
            questionTimer.cancel();
        }

        // Get time limit for this question
        timeLeftInMillis = getQuestionTimeLimit(currentQuestion);
        startQuestionTimer();

        // Display question
        questionText.setText(currentQuestion.getQuestion());
        questionCounterText.setText((currentQuestionIndex + 1) + "/" + questions.size());

        // Display options
        optionsGroup.removeAllViews();
        for (String option : currentQuestion.getOptions()) {
            RadioButton rb = new RadioButton(this);
            rb.setText(option);
            optionsGroup.addView(rb);
        }
    }

    private long getQuestionTimeLimit(Question question) {
        try {
            // Convert seconds to milliseconds
            return Long.parseLong(question.getTimeLimit()) * 1000;
        } catch (Exception e) {
            return 30000; // 30 secondes par défaut si erreur
        }
    }

    private void startQuestionTimer() {
        questionTimer = new CountDownTimer(timeLeftInMillis, 1000) {
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                updateTimer();
            }

            public void onFinish() {
                timeUp();
            }
        }.start();
    }

    private void updateTimer() {
        int minutes = (int) (timeLeftInMillis / 1000) / 60;
        int seconds = (int) (timeLeftInMillis / 1000) % 60;
        timerText.setText(String.format("%02d:%02d", minutes, seconds));
    }

    private void timeUp() {
        Toast.makeText(this, "Temps écoulé pour cette question!", Toast.LENGTH_SHORT).show();
        moveToNextQuestion();
    }

    private void checkAnswer() {
        int selectedId = optionsGroup.getCheckedRadioButtonId();
        if (selectedId != -1) {
            RadioButton selected = findViewById(selectedId);
            if (selected.getText().toString().equals(
                    questions.get(currentQuestionIndex).getCorrectAnswer())) {
                score += getQuestionPoints(currentQuestionIndex);
            }
        }
    }

    private int getQuestionPoints(int index) {
        try {
            return Integer.parseInt(questions.get(index).getPoints());
        } catch (Exception e) {
            return 1; // 1 point par défaut si erreur
        }
    }

    private void moveToNextQuestion() {
        currentQuestionIndex++;
        if (currentQuestionIndex < questions.size()) {
            showQuestion();
        } else {
            finishQuiz();
        }
    }

    private void finishQuiz() {
        if (questionTimer != null) {
            questionTimer.cancel();
        }

        saveQuizResult();

        Intent intent = new Intent(this, QuizResultActivity.class);
        intent.putExtra("SCORE", score);
        intent.putExtra("TOTAL", calculateTotalPoints());
        startActivity(intent);
        finish();
    }

    private int calculateTotalPoints() {
        int total = 0;
        for (Question q : questions) {
            total += getQuestionPoints(questions.indexOf(q));
        }
        return total;
    }

    private void saveQuizResult() {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("quiz_results");
        String resultId = ref.push().getKey();

        QuizResult result = new QuizResult(userId, quiz.getQuizId(), score);
        ref.child(resultId).setValue(result)
                .addOnSuccessListener(aVoid -> Log.d("Quiz", "Résultat sauvegardé"))
                .addOnFailureListener(e -> Log.e("Quiz", "Erreur sauvegarde", e));
    }

    private void finishWithError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (questionTimer != null) {
            questionTimer.cancel();
        }
    }
}