package com.example.pfe;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;

import java.util.ArrayList;
import java.util.List;

public class QuizListActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private QuizAdapter quizAdapter;
    private List<Quiz> quizList = new ArrayList<>();
    private List<String> formationsIds = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_list);

        recyclerView = findViewById(R.id.recyclerViewQuiz);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        quizAdapter = new QuizAdapter(this, quizList, quiz -> {
            Intent intent = new Intent(QuizListActivity.this, QuizActivity.class);
            intent.putExtra("QUIZ", quiz);
            startActivity(intent);
        });

        recyclerView.setAdapter(quizAdapter);
        chargerFormationsInscrites();
    }

    private void chargerFormationsInscrites() {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("inscriptions");
        ref.orderByChild("idEtudiant").equalTo(userId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        formationsIds.clear();
                        for (DataSnapshot ds : snapshot.getChildren()) {
                            String idForm = ds.child("idFormation").getValue(String.class);
                            if (idForm != null) formationsIds.add(idForm);
                        }
                        if (formationsIds.isEmpty()) {
                            Toast.makeText(QuizListActivity.this,
                                    "Vous n'êtes inscrit à aucune formation.",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            chargerQuizs();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        Log.e("QuizList", "Erreur chargement formations: " + error.getMessage());
                    }
                });
    }

    private void chargerQuizs() {
        DatabaseReference quizRef = FirebaseDatabase.getInstance().getReference("quizs");
        quizRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                quizList.clear();
                for (DataSnapshot formationSnapshot : dataSnapshot.getChildren()) {
                    for (DataSnapshot quizSnapshot : formationSnapshot.getChildren()) {
                        Quiz quiz = quizSnapshot.getValue(Quiz.class);
                        if (quiz != null && formationsIds.contains(quiz.getIdFormation())) {
                            quiz.setQuizId(quizSnapshot.getKey());
                            quizList.add(quiz);
                        }
                    }
                }
                quizAdapter.notifyDataSetChanged();

                if (quizList.isEmpty()) {
                    Toast.makeText(QuizListActivity.this,
                            "Aucun quiz disponible pour vos formations.",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.e("QuizList", "Erreur chargement quiz: " + error.getMessage());
            }
        });
    }
}