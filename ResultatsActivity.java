package com.example.pfe;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ResultatsActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ResultatAdapter adapter;
    private List<Resultat> resultatsList;
    private ProgressBar progressBar;
    private DatabaseReference databaseRef;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultats);

        // Initialisation
        recyclerView = findViewById(R.id.recyclerViewResultats);
        progressBar = findViewById(R.id.progressBar);
        resultatsList = new ArrayList<>();

        // Configuration du RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ResultatAdapter(resultatsList);
        recyclerView.setAdapter(adapter);

        // Initialisation Firebase
        databaseRef = FirebaseDatabase.getInstance().getReference("resultats");
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser == null) {
            Toast.makeText(this, "Utilisateur non connecté", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Charger les résultats
        loadStudentResults();
    }

    private void loadStudentResults() {
        progressBar.setVisibility(View.VISIBLE);

        databaseRef.orderByChild("etudiantId")
                .equalTo(currentUser.getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        resultatsList.clear();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Resultat resultat = snapshot.getValue(Resultat.class);
                            if (resultat != null) {
                                resultat.setResultatId(snapshot.getKey());
                                resultatsList.add(resultat);
                            }
                        }

                        if (resultatsList.isEmpty()) {
                            Toast.makeText(ResultatsActivity.this,
                                    "Aucun résultat trouvé", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(ResultatsActivity.this,
                                    resultatsList.size() + " résultats trouvés", Toast.LENGTH_SHORT).show();
                        }

                        adapter.notifyDataSetChanged();
                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(ResultatsActivity.this,
                                "Erreur de chargement: " + databaseError.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }
}