package com.example.pfe;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class EtudiantActivity extends AppCompatActivity {

    CardView cardCours, cardDevoir, cardQuiz, cardResultat, cardChat, cardChatBot, cardprofile;
    private DatabaseReference databaseRef;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_etudiant);

        // Initialisation Firebase
        databaseRef = FirebaseDatabase.getInstance().getReference();

        cardCours = findViewById(R.id.cardCours);
        cardDevoir = findViewById(R.id.card_devoir);
        cardQuiz = findViewById(R.id.card_quiz);
        cardResultat = findViewById(R.id.card_resultat);
        cardChat = findViewById(R.id.card_chat);
        cardChatBot = findViewById(R.id.cardChatBot);
        cardprofile = findViewById(R.id.card_profil);

        cardCours.setOnClickListener(v ->
                startActivity(new Intent(this, CoursListActivity.class)));
        cardDevoir.setOnClickListener(v ->
                startActivity(new Intent(this, DevoirListActivity.class)));
        cardQuiz.setOnClickListener(v ->
                startActivity(new Intent(this, QuizListActivity.class)));
        cardResultat.setOnClickListener(v -> {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user != null) {
                startActivity(new Intent(EtudiantActivity.this, ResultatsActivity.class));
            } else {
                Toast.makeText(EtudiantActivity.this,
                        "Vous devez être connecté", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(EtudiantActivity.this, LoginActivity.class));
            }
        });
        cardChat.setOnClickListener(v -> {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user != null) {
                getFormationsForStudent(user.getUid(), formationIds -> {
                    if (formationIds != null && !formationIds.isEmpty()) {
                        // On prend la première formation de la liste
                        String formationId = formationIds.get(0);
                        // Charger le nom de la formation
                        databaseRef.child("formations").child(formationId).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                String formationName = snapshot.child("nom").getValue(String.class);
                                Intent intent = new Intent(EtudiantActivity.this, ChatActivity.class);
                                intent.putExtra("formationId", formationId);
                                intent.putExtra("formationName", formationName != null ? formationName : "Formation");
                                startActivity(intent);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Toast.makeText(EtudiantActivity.this, "Erreur de chargement", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        Toast.makeText(EtudiantActivity.this,
                                "Vous n'êtes inscrit à aucune formation",
                                Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Toast.makeText(this,
                        "Vous devez être connecté pour accéder au Chat.",
                        Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, LoginActivity.class));
                finish();
            }
        });
        cardChatBot.setOnClickListener(v ->
                startActivity(new Intent(this, ChatBot.class)));
        cardprofile.setOnClickListener(v -> startActivity(new Intent(this, ProfileActivity.class)));
    }

    private interface FormationsCallback {
        void onCallback(List<String> formationIds);
    }

    private void getFormationsForStudent(String studentId, FormationsCallback callback) {
        databaseRef.child("inscriptions")
                .orderByChild("idEtudiant")
                .equalTo(studentId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        List<String> formationIds = new ArrayList<>();
                        for (DataSnapshot ds : snapshot.getChildren()) {
                            String formationId = ds.child("idFormation").getValue(String.class);
                            if (formationId != null && !formationId.isEmpty()) {
                                formationIds.add(formationId);
                            }
                        }
                        callback.onCallback(formationIds);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        callback.onCallback(new ArrayList<>());
                    }
                });
    }
}