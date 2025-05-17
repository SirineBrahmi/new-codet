package com.example.pfe;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class QuizAdapter extends RecyclerView.Adapter<QuizAdapter.QuizViewHolder> {

    private Context context;
    private List<Quiz> quizList;
    private OnQuizClickListener listener;

    public interface OnQuizClickListener {
        void onQuizClick(Quiz quiz);
    }

    public QuizAdapter(Context context, List<Quiz> quizList, OnQuizClickListener listener) {
        this.context = context;
        this.quizList = quizList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public QuizViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_quiz, parent, false);
        return new QuizViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QuizViewHolder holder, int position) {
        Quiz quiz = quizList.get(position);

        holder.quizTitle.setText(quiz.getTitre());
        holder.quizDescription.setText(quiz.getDescription());
        holder.formationName.setText(quiz.getNomFormation());

        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
            SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            Date date = inputFormat.parse(quiz.getDateLimite());
            holder.quizDeadline.setText("Date limite: " + outputFormat.format(date));
        } catch (ParseException e) {
            holder.quizDeadline.setText("Date limite: Non spécifiée");
        }

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onQuizClick(quiz);
            }
        });
    }

    @Override
    public int getItemCount() {
        return quizList.size();
    }

    public static class QuizViewHolder extends RecyclerView.ViewHolder {
        TextView quizTitle;
        TextView quizDescription;
        TextView formationName;
        TextView quizDeadline;

        public QuizViewHolder(@NonNull View itemView) {
            super(itemView);
            quizTitle = itemView.findViewById(R.id.textViewQuizTitle);
            quizDescription = itemView.findViewById(R.id.textViewQuizDescription);
            formationName = itemView.findViewById(R.id.textViewFormation);
            quizDeadline = itemView.findViewById(R.id.textViewQuizDeadline);
        }
    }

    public void updateQuizList(List<Quiz> newQuizList) {
        quizList.clear();
        quizList.addAll(newQuizList);
        notifyDataSetChanged();
    }
}