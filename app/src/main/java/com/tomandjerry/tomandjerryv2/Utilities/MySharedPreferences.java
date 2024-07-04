package com.tomandjerry.tomandjerryv2.Utilities;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;


import java.util.ArrayList;

public class MySharedPreferences {

    private static MySharedPreferences mySharedPreferences;
    private final SharedPreferences prefs;

    private MySharedPreferences(Context context) {
        prefs = context.getSharedPreferences("MyPreference", MODE_PRIVATE);
    }

    public static void init(Context context) {
        if (mySharedPreferences == null) {
            mySharedPreferences = new MySharedPreferences(context);
        }
    }

    public static MySharedPreferences getInstance() {
        return mySharedPreferences;
    }

    public void saveString(String key, String value) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public String readString(String key, String def) {
        return prefs.getString(key, def);
    }


    public ArrayList<MyScore> readScores() {
        String json = readString("MyScores", null);
        if (json == null)
            return null;
        String[] res = json.split(",");
        ArrayList<MyScore> scores = new ArrayList<>();
        for (String rv : res) {
            scores.add(new MyScore(rv));
        }
        return scores;
    }

    public void saveScores(ArrayList<MyScore> Scores) {
        StringBuilder sb = new StringBuilder();
        for (int x = 0; x < Scores.size() - 1; x++) {
            sb.append(Scores.get(x).toString());
            sb.append(",");
        }
        sb.append(Scores.get(Scores.size() - 1).toString());
        saveString("MyScores", sb.toString());
    }


}