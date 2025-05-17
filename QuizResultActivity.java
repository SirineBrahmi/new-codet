package com.example.pfe;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class QuizResultActivity extends AppCompatActivity {
    private TextView scoreTextView, totalTextView, resultTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_result);

        scoreTextView = findViewById(R.id.scoreTextView);
        totalTextView = findViewById(R.id.totalTextView);
        resultTextView = findViewById(R.id.resultTextView);

        Intent intent = getIntent();
        int score = intent.getIntExtra("SCORE", 0);
        int total = intent.getIntExtra("TOTAL", 0);

        scoreTextView.setText("Score: " + score);
        totalTextView.setText("Total: " + total);

        if (score == total) {
            resultTextView.setText("Félicitations! Vous avez tout juste!");
        } else if (score >= total / 2) {
            resultTextView.setText("Bien joué! Vous avez bien répondu à la moitié des questions.");
        } else {
            resultTextView.setText("Ce n'est pas grave! Vous pouvez améliorer votre score.");
        }
    }
}