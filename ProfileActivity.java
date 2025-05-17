package com.example.pfe;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ProfileActivity extends AppCompatActivity {

    private TextView textViewNom, textViewPrenom, textViewAge, textViewEmail, textViewNiveau, textViewRole;
    private EditText editTextNumTel, editTextAdresse, editTextAncienMotDePasse, editTextNouveauMotDePasse;
    private Button buttonUpdate;

    private FirebaseAuth mAuth;
    private DatabaseReference usersRef;
    private String userId; // ID de l'étudiant connecté

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Initialisation Firebase
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            userId = currentUser.getUid();
            usersRef = FirebaseDatabase.getInstance().getReference("utilisateurs").child("etudiants");
        }

        // Lier les composants
        textViewNom = findViewById(R.id.textViewNom);
        textViewPrenom = findViewById(R.id.textViewPrenom);
        textViewAge = findViewById(R.id.textViewAge);
        textViewEmail = findViewById(R.id.textViewEmail);
        textViewNiveau = findViewById(R.id.textViewNiveau);
        textViewRole = findViewById(R.id.textViewRole);

        editTextNumTel = findViewById(R.id.editTextNumTel);
        editTextAdresse = findViewById(R.id.editTextAdresse);
        editTextAncienMotDePasse = findViewById(R.id.editTextAncienMotDePasse);
        editTextNouveauMotDePasse = findViewById(R.id.editTextNouveauMotDePasse);

        buttonUpdate = findViewById(R.id.buttonUpdate);

        loadUserProfile();

        buttonUpdate.setOnClickListener(view -> updateProfile());
    }

    private void loadUserProfile() {
        if (userId == null) {
            Toast.makeText(this, "Utilisateur non connecté!", Toast.LENGTH_SHORT).show();
            return;
        }

        usersRef.child(userId).get().addOnSuccessListener(dataSnapshot -> {
            if (dataSnapshot.exists()) {
                String nom = dataSnapshot.child("nom").getValue(String.class);
                String prenom = dataSnapshot.child("prenom").getValue(String.class);
                Integer ageInt = dataSnapshot.child("age").getValue(Integer.class);
                String email = dataSnapshot.child("email").getValue(String.class);
                String niveau = dataSnapshot.child("niveau").getValue(String.class);
                String numTel = dataSnapshot.child("numTel").getValue(String.class);
                String adresse = dataSnapshot.child("adresse").getValue(String.class);

                textViewNom.setText("Nom : " + (nom != null ? nom : ""));
                textViewPrenom.setText("Prénom : " + (prenom != null ? prenom : ""));
                textViewAge.setText("Âge : " + (ageInt != null ? String.valueOf(ageInt) : ""));
                textViewEmail.setText("Email : " + (email != null ? email : ""));
                textViewNiveau.setText("Niveau : " + (niveau != null ? niveau : ""));
                textViewRole.setText("Role : Etudiant");

                editTextNumTel.setText(numTel != null ? numTel : "");
                editTextAdresse.setText(adresse != null ? adresse : "");
            }
        }).addOnFailureListener(e -> {
            Toast.makeText(ProfileActivity.this, "Erreur de chargement des données!", Toast.LENGTH_SHORT).show();
        });
    }

    private void updateProfile() {
        if (userId == null) {
            Toast.makeText(this, "Utilisateur non connecté!", Toast.LENGTH_SHORT).show();
            return;
        }

        String numTel = editTextNumTel.getText().toString().trim();
        String adresse = editTextAdresse.getText().toString().trim();
        String ancienMotDePasse = editTextAncienMotDePasse.getText().toString().trim();
        String nouveauMotDePasse = editTextNouveauMotDePasse.getText().toString().trim();

        if (numTel.isEmpty() || adresse.isEmpty()) {
            Toast.makeText(this, "Numéro et adresse sont obligatoires!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Mise à jour téléphone et adresse
        usersRef.child(userId).child("numTel").setValue(numTel);
        usersRef.child(userId).child("adresse").setValue(adresse);

        // Mise à jour mot de passe si demandé
        if (!ancienMotDePasse.isEmpty() && !nouveauMotDePasse.isEmpty()) {
            FirebaseUser user = mAuth.getCurrentUser();
            if (user != null && user.getEmail() != null) {
                user.reauthenticate(EmailAuthProvider.getCredential(user.getEmail(), ancienMotDePasse))
                        .addOnSuccessListener(authResult -> {
                            user.updatePassword(nouveauMotDePasse)
                                    .addOnSuccessListener(unused -> {
                                        Toast.makeText(ProfileActivity.this, "Profil et mot de passe mis à jour!", Toast.LENGTH_SHORT).show();
                                    })
                                    .addOnFailureListener(e -> {
                                        Toast.makeText(ProfileActivity.this, "Erreur mise à jour mot de passe: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    });
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(ProfileActivity.this, "Ancien mot de passe incorrect!", Toast.LENGTH_SHORT).show();
                        });
            }
        } else {
            Toast.makeText(this, "Profil mis à jour!", Toast.LENGTH_SHORT).show();
        }
    }
}
