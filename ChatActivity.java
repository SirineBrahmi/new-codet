package com.example.pfe;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class ChatActivity extends AppCompatActivity {

    private EditText messageEditText;
    private ImageButton sendButton;
    private RecyclerView messagesRecyclerView;

    private FirebaseAuth mAuth;
    private DatabaseReference databaseRef;
    private String currentUserId;
    private String currentUserName = "Utilisateur";
    private String formationId;
    private String formationName;
    private boolean isUserNameLoaded = false;

    private MessageAdapter adapter;
    private DatabaseReference messagesRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        // Initialisation Firebase
        mAuth = FirebaseAuth.getInstance();
        databaseRef = FirebaseDatabase.getInstance().getReference();
        currentUserId = mAuth.getCurrentUser().getUid();

        // Récupération des données de l'intent
        formationId = getIntent().getStringExtra("formationId");
        formationName = getIntent().getStringExtra("formationName");

        if (formationId == null || formationId.isEmpty()) {
            Toast.makeText(this, "ID de formation manquant", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Initialisation des vues
        initViews();

        // Chargement des données utilisateur et configuration du chat
        loadUserData();
        setupChat();
    }

    private void initViews() {
        messageEditText = findViewById(R.id.message_edit_text);
        sendButton = findViewById(R.id.send_button);
        messagesRecyclerView = findViewById(R.id.messages_recycler_view);

        messagesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        messagesRecyclerView.setHasFixedSize(true);

        sendButton.setOnClickListener(v -> sendMessage());
    }

    private void loadUserData() {
        databaseRef.child("utilisateurs").child("etudiants").child(currentUserId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            String prenom = snapshot.child("prenom").getValue(String.class);
                            String nom = snapshot.child("nom").getValue(String.class);

                            StringBuilder nameBuilder = new StringBuilder();
                            if (prenom != null && !prenom.isEmpty()) {
                                nameBuilder.append(prenom.trim());
                            }
                            if (nom != null && !nom.isEmpty()) {
                                if (nameBuilder.length() > 0) nameBuilder.append(" ");
                                nameBuilder.append(nom.trim());
                            }

                            currentUserName = nameBuilder.length() > 0 ? nameBuilder.toString() : "Utilisateur";
                        }
                        isUserNameLoaded = true;
                        Log.d("ChatActivity", "Nom utilisateur chargé: " + currentUserName);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        currentUserName = "Utilisateur";
                        isUserNameLoaded = true;
                        Log.e("ChatActivity", "Erreur chargement utilisateur", error.toException());
                    }
                });
    }

    private void setupChat() {
        messagesRef = databaseRef.child("chats").child(formationId).child("messages");

        Query query = messagesRef.orderByChild("timestamp");

        FirebaseRecyclerOptions<Message> options = new FirebaseRecyclerOptions.Builder<Message>()
                .setQuery(query, Message.class)
                .build();

        adapter = new MessageAdapter(options, currentUserId);
        messagesRecyclerView.setAdapter(adapter);

        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                messagesRecyclerView.smoothScrollToPosition(adapter.getItemCount());
            }
        });
    }

    private void sendMessage() {
        if (!isUserNameLoaded) {
            Toast.makeText(this, "Chargement des informations en cours...", Toast.LENGTH_SHORT).show();
            return;
        }

        String messageText = messageEditText.getText().toString().trim();
        if (TextUtils.isEmpty(messageText)) {
            return;
        }

        String messageId = messagesRef.push().getKey();
        long timestamp = System.currentTimeMillis();

        Map<String, Object> message = new HashMap<>();
        message.put("senderId", currentUserId);
        message.put("senderName", currentUserName);
        message.put("text", messageText);
        message.put("timestamp", timestamp);
        message.put("type", "text");
        message.put("formationId", formationId);
        message.put("formationName", formationName);

        Log.d("ChatActivity", "Envoi message - Nom: " + currentUserName);

        if (messageId != null) {
            messagesRef.child(messageId).setValue(message)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            messageEditText.setText("");
                        } else {
                            Toast.makeText(this, "Échec de l'envoi", Toast.LENGTH_SHORT).show();
                            Log.e("ChatActivity", "Erreur envoi", task.getException());
                        }
                    });
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (adapter != null) adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (adapter != null) adapter.stopListening();
    }
}