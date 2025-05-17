package com.example.pfe;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class CoursDetailActivity extends AppCompatActivity {
    private TextView titreTextView, descriptionTextView;
    private Button ouvrirFichierButton;
    private String fichierUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cours_detail);

        titreTextView = findViewById(R.id.titreDetail);
        descriptionTextView = findViewById(R.id.descriptionDetail);
        ouvrirFichierButton = findViewById(R.id.ouvrirFichierButton);

        Intent intent = getIntent();
        String titre = intent.getStringExtra("TITRE");
        String description = intent.getStringExtra("DESCRIPTION");
        fichierUrl = intent.getStringExtra("FICHIER_URL");

        titreTextView.setText(titre);
        descriptionTextView.setText(description);

        ouvrirFichierButton.setOnClickListener(v -> {
            if (fichierUrl != null && !fichierUrl.isEmpty()) {
                if (fichierUrl.endsWith(".pdf")) {
                    Intent pdfIntent = new Intent(CoursDetailActivity.this, PdfViewerActivity.class);
                    pdfIntent.putExtra("PDF_URL", fichierUrl);
                    startActivity(pdfIntent);
                } else {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(fichierUrl));
                    startActivity(browserIntent);
                }
            } else {
                Toast.makeText(CoursDetailActivity.this, "URL du fichier invalide", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
