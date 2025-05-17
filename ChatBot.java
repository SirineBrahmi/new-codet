package com.example.pfe;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class ChatBot extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Lien de ton chatbot Flask
        String url = "http://192.168.33.210:5000";

        // Créer un intent pour ouvrir le navigateur
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        startActivity(intent);

        // Tu peux supprimer setContentView si tu n'as pas besoin d'une interface
        finish(); // Fermer l'activité si tu veux que ça sorte direct
    }
}
