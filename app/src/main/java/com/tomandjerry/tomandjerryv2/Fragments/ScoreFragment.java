package com.tomandjerry.tomandjerryv2.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tomandjerry.tomandjerryv2.Adapters.ScoreAdapter;
import com.tomandjerry.tomandjerryv2.Interfaces.ScoreClickCallBack;
import com.tomandjerry.tomandjerryv2.Utilities.MyScore;
import com.tomandjerry.tomandjerryv2.Utilities.MySharedPreferences;
import com.tomandjerry.tomandjerryv2.R;

import java.util.ArrayList;

public class ScoreFragment extends Fragment {

    private RecyclerView main_LST_scores;
    private ScoreAdapter scoreAdapter;
    private ArrayList<MyScore> scores;
    private ScoreClickCallBack scoreClickCallBack;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recycle_view, container, false);

        // Initialize views
        main_LST_scores = view.findViewById(R.id.recycle_view);

        // Initialize SharedPreferences and retrieve scores
        MySharedPreferences mySharedPreferences = MySharedPreferences.getInstance();
        scores = mySharedPreferences.readScores();
        // Set up RecyclerView and Adapter
        scoreAdapter = new ScoreAdapter(scores, myScore -> {
            if (scoreClickCallBack != null) {
                scoreClickCallBack.onScoreClick(myScore);
            }
        });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        main_LST_scores.setLayoutManager(linearLayoutManager);
        main_LST_scores.setAdapter(scoreAdapter);
        return view;
    }

    public void setScoreClickCallBack(ScoreClickCallBack scoreClickCallBack) {
        this.scoreClickCallBack = scoreClickCallBack;
    }

    public ArrayList<MyScore> getScores() {
        return scores;
    }

    public void setScores(ArrayList<MyScore> scores) {
        this.scores = scores;
    }
}
