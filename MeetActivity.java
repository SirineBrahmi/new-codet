/*package com.example.pfe;

import android.os.Bundle;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.text.Editable;
import android.text.TextWatcher;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import org.jitsi.meet.sdk.JitsiMeetActivity;
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions;

import java.net.MalformedURLException;
import java.net.URL;

public class MeetActivity extends AppCompatActivity {

    private EditText secretCode;
    private Button joinButton;
    private String classroomId;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meet);

        secretCode = findViewById(R.id.editRoomName);
        joinButton = findViewById(R.id.buttonJoin);

        classroomId = getIntent().getStringExtra("classroomId");

        if (classroomId == null || classroomId.isEmpty()) {
            Toast.makeText(this, "Identifiant de classe manquant", Toast.LENGTH_SHORT).show();
            return;
        }

        databaseReference = FirebaseDatabase.getInstance().getReference("classrooms");

        // Validation en temps réel
        secretCode.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().matches("^[a-zA-Z]{3}-[a-zA-Z]{3}-\\d{3}$")) {
                    secretCode.setError("Format : abc-xyz-123");
                } else {
                    secretCode.setError(null);
                }
            }
            @Override public void afterTextChanged(Editable s) {}
        });

        joinButton.setOnClickListener(view -> {
            // Masquer le clavier
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(secretCode.getWindowToken(), 0);
            }

            String roomPassword = secretCode.getText().toString().trim();

            if (!roomPassword.matches("^[a-zA-Z]{3}-[a-zA-Z]{3}-\\d{3}$")) {
                secretCode.setError("Format invalide : abc-xyz-123");
                return;
            }

            joinButton.setEnabled(false);
            joinButton.setText("Connexion...");

            databaseReference.child(classroomId).child("motDePasse").get().addOnCompleteListener(task -> {
                joinButton.setEnabled(true);
                joinButton.setText("Join Meeting");

                if (task.isSuccessful()) {
                    String storedPassword = task.getResult().getValue(String.class);
                    if (storedPassword != null && storedPassword.equals(roomPassword)) {
                        try {
                            URL serverURL = new URL("https://meet.jit.si");

                            JitsiMeetConferenceOptions options = new JitsiMeetConferenceOptions.Builder()
                                    .setServerURL(serverURL)
                                    .setRoom(classroomId)
                                    .setWelcomePageEnabled(false)
                                    .build();

                            JitsiMeetActivity.launch(MeetActivity.this, options);
                        } catch (MalformedURLException e) {
                            Toast.makeText(MeetActivity.this, "URL Jitsi invalide", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    } else {
                        secretCode.setError("Mot de passe incorrect");
                    }
                } else {
                    Toast.makeText(MeetActivity.this, "Erreur Firebase : " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}
*/