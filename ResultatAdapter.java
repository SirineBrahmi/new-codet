package com.example.pfe;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ResultatAdapter extends RecyclerView.Adapter<ResultatAdapter.ResultatViewHolder> {
    private List<Resultat> resultats;

    public ResultatAdapter(List<Resultat> resultats) {
        this.resultats = resultats;
    }

    @NonNull
    @Override
    public ResultatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_resultat, parent, false);
        return new ResultatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ResultatViewHolder holder, int position) {
        Resultat resultat = resultats.get(position);
        holder.nom.setText(resultat.getNom());
        holder.prenom.setText(resultat.getPrenom());
        holder.email.setText(resultat.getEmail());
        holder.noteQuiz.setText(String.valueOf(resultat.getNoteQuiz()));
        holder.noteExamen.setText(String.valueOf(resultat.getNoteExamen()));

        // Vous pouvez ajouter d'autres champs ici si nécessaire
    }

    @Override
    public int getItemCount() {
        return resultats.size();
    }

    public void updateData(List<Resultat> newResultats) {
        resultats.clear();
        resultats.addAll(newResultats);
        notifyDataSetChanged();
    }

    public static class ResultatViewHolder extends RecyclerView.ViewHolder {
        TextView nom, prenom, email, noteQuiz, noteExamen;

        public ResultatViewHolder(@NonNull View itemView) {
            super(itemView);
            nom = itemView.findViewById(R.id.nomEtudiant);
            prenom = itemView.findViewById(R.id.prenomEtudiant);
            email = itemView.findViewById(R.id.emailEtudiant);
            noteQuiz = itemView.findViewById(R.id.noteQuiz);
            noteExamen = itemView.findViewById(R.id.noteExamen);
        }
    }
}