package com.example.pfe;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CoursAdapter extends RecyclerView.Adapter<CoursAdapter.CoursViewHolder> {
    private Context context;
    private List<Cour> coursList;

    public CoursAdapter(Context context, List<Cour> coursList) {
        this.context = context;
        this.coursList = coursList;
    }

    @NonNull
    @Override
    public CoursViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_cours, parent, false);
        return new CoursViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CoursViewHolder holder, int position) {
        Cour cour = coursList.get(position);
        holder.titreTextView.setText(cour.getTitre());
        holder.descriptionTextView.setText(cour.getDescription());

        holder.itemView.setOnClickListener(v -> {
            // Envoie les détails du cours à l'activité de détail
            Intent intent = new Intent(context, CoursDetailActivity.class);
            intent.putExtra("TITRE", cour.getTitre());
            intent.putExtra("DESCRIPTION", cour.getDescription());
            intent.putExtra("FICHIER_URL", cour.getFileURL()); // Ajouter si vous avez l'URL du fichier
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return coursList.size();
    }

    public static class CoursViewHolder extends RecyclerView.ViewHolder {
        TextView titreTextView, descriptionTextView;

        public CoursViewHolder(View itemView) {
            super(itemView);
            titreTextView = itemView.findViewById(R.id.titreCours);
            descriptionTextView = itemView.findViewById(R.id.descriptionCours);
        }
    }
}
