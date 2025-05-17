package com.example.pfe;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AfficherCoursActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private CoursAdapter coursAdapter; // Adapter pour 'Cour'
    private List<Cour> courList; // Liste de type 'Cour'

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_afficher_cours);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        courList = new ArrayList<>(); // Liste pour stocker les objets 'Cour'
        coursAdapter = new CoursAdapter(this, courList);
        recyclerView.setAdapter(coursAdapter);

        // Référence Firebase
        DatabaseReference coursRef = FirebaseDatabase.getInstance().getReference("cours");

        // Écouteur pour récupérer les cours en temps réel
        coursRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                courList.clear(); // Vider la liste avant de la remplir

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Cour cour = snapshot.getValue(Cour.class); // Utilisation de la classe 'Cour'
                    if (cour != null) {
                        courList.add(cour); // Ajouter les éléments 'Cour' à la liste
                    }
                }

                coursAdapter.notifyDataSetChanged(); // Notifier l'adaptateur que les données ont changé
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(AfficherCoursActivity.this, "Erreur de récupération des données", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
