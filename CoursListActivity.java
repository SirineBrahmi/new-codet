package com.example.pfe;

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

public class CoursListActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private CoursAdapter adapter;
    private List<Cour> coursList = new ArrayList<>();
    private List<String> formationsIds = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cours_list);

        recyclerView = findViewById(R.id.recyclerViewCours);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CoursAdapter(this, coursList);
        recyclerView.setAdapter(adapter);

        chargerFormationsInscrites();
    }

    private void chargerFormationsInscrites() {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("inscriptions");
        ref.orderByChild("idEtudiant").equalTo(userId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override public void onDataChange(DataSnapshot snapshot) {
                        formationsIds.clear();
                        for (DataSnapshot ds : snapshot.getChildren()) {
                            String idForm = ds.child("idFormation").getValue(String.class);
                            if (idForm != null) formationsIds.add(idForm);
                        }
                        if (formationsIds.isEmpty()) {
                            Toast.makeText(CoursListActivity.this,
                                    "Vous n'êtes inscrit à aucune formation.",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            chargerCours();
                        }
                    }

                    @Override public void onCancelled(DatabaseError error) {
                        Log.e("MesCours", error.getMessage());
                    }
                });
    }

    private void chargerCours() {
        DatabaseReference coursRef = FirebaseDatabase.getInstance().getReference("cours");
        coursRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override public void onDataChange(DataSnapshot snap) {
                coursList.clear();
                for (DataSnapshot formationSnap : snap.getChildren()) {
                    // formationSnap.getKey() -> "Espagnol", "Francais", etc.
                    for (DataSnapshot semestreSnap : formationSnap.getChildren()) {
                        for (DataSnapshot coursSnap : semestreSnap.getChildren()) {
                            Cour c = coursSnap.getValue(Cour.class);
                            if (c != null && formationsIds.contains(c.getIdFormation())) {
                                c.setId(coursSnap.getKey());
                                c.setNomFormation(formationSnap.getKey());
                                c.setSemestre(semestreSnap.getKey());
                                coursList.add(c);
                            }
                        }
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override public void onCancelled(DatabaseError error) {
                Log.e("MesCours", error.getMessage());
            }
        });
    }
}