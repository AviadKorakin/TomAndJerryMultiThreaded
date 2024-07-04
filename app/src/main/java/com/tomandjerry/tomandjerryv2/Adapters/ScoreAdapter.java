package com.tomandjerry.tomandjerryv2.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textview.MaterialTextView;
import com.tomandjerry.tomandjerryv2.Interfaces.ScoreClickCallBack;
import com.tomandjerry.tomandjerryv2.R;
import com.tomandjerry.tomandjerryv2.Utilities.MyScore;

import java.util.ArrayList;

public class ScoreAdapter extends RecyclerView.Adapter<ScoreAdapter.ScoreViewHolder> {

    private ArrayList<MyScore> scores;
    private final ScoreClickCallBack scoreClickCallBack;


    public ScoreAdapter(ArrayList<MyScore> scores, ScoreClickCallBack scoreClickCallBack) {
        this.scores = scores;
        this.scoreClickCallBack = scoreClickCallBack;
    }

    @NonNull
    @Override
    public ScoreViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.horizontal_score_item, parent, false);
        return new ScoreViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ScoreViewHolder holder, int position) {
        MyScore score = scores.get(position);
        holder.scoreText.setText(String.valueOf(score.getScore()));
        holder.metersText.setText(String.valueOf(score.getMeters()));
        holder.scoreImage.setOnClickListener(v -> {
            if (scoreClickCallBack != null) {
                scoreClickCallBack.onScoreClick(score);
            }
        });
    }

    @Override
    public int getItemCount() {
        return scores == null ? 0 : scores.size();
    }

    public static class ScoreViewHolder extends RecyclerView.ViewHolder {

        private final AppCompatImageButton scoreImage;
        private final MaterialTextView scoreText;
        private final MaterialTextView metersText;

        public ScoreViewHolder(@NonNull View itemView) {
            super(itemView);
            scoreImage = itemView.findViewById(R.id.score_img);
            scoreText = itemView.findViewById(R.id.score);
            metersText = itemView.findViewById(R.id.meter);
        }
    }
}
