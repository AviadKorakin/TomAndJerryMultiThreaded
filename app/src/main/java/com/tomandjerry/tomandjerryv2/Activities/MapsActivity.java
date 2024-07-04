package com.tomandjerry.tomandjerryv2.Activities;

import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.tomandjerry.tomandjerryv2.Interfaces.ScoreClickCallBack;
import com.tomandjerry.tomandjerryv2.R;
import com.tomandjerry.tomandjerryv2.Utilities.MyBackgroundMusic;
import com.tomandjerry.tomandjerryv2.Utilities.MyScore;
import com.tomandjerry.tomandjerryv2.databinding.MapsActivityBinding;

import android.content.Intent;


import androidx.appcompat.widget.AppCompatImageButton;
import androidx.fragment.app.FragmentManager;

import com.tomandjerry.tomandjerryv2.Fragments.ScoreFragment;

import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private MapsActivityBinding binding;
    private AppCompatImageButton TomAndJerry_ReturnButton;
    private ScoreFragment scoreFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = MapsActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        FragmentManager fragmentManager = getSupportFragmentManager();


        // Add ScoreFragment dynamically
        scoreFragment = new ScoreFragment();
        fragmentManager.beginTransaction().replace(R.id.scorelist, scoreFragment).commit();
        scoreFragment.setScoreClickCallBack(myScore -> {
            LatLng location = new LatLng(myScore.getLat(), myScore.getLng());
            mMap.clear();
            mMap.addMarker(new MarkerOptions().position(location).title("You played here!"));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 8f));
        });


        SupportMapFragment mapFragment = new SupportMapFragment();
        fragmentManager.beginTransaction().replace(R.id.map, mapFragment).commit();

        TomAndJerry_ReturnButton = findViewById(R.id.returnButton);
        TomAndJerry_ReturnButton.setOnClickListener(v -> returnToMenu());
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        ArrayList<MyScore> scores = scoreFragment.getScores();
        double lat = 0;
        double lng = 0;
        for (MyScore score : scores) {
            lat += score.getLat();
            lng += score.getLng();
            LatLng location = new LatLng(score.getLat(), score.getLng());
            mMap.addMarker(new MarkerOptions().position(location).title("You played here!"));
        }
        LatLng avgLocation = new LatLng(lat / scores.size(), lng / scores.size());
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(avgLocation, 6f));
    }

    /* @Override
     public void onScoreSelected(double lat, double lng) {
         if (mMap != null) {
             LatLng location = new LatLng(lat, lng);
             mMap.clear();
             mMap.addMarker(new MarkerOptions().position(location).title("You played here!"));
             mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15f));

         }
     }*/
    @Override
    protected void onResume() {
        super.onResume();
        start();

    }

    @Override
    protected void onPause() {
        super.onPause();
        stop();

    }

    private void stop() {
        MyBackgroundMusic.getInstance().pauseMusic();
    }

    private void start() {
        MyBackgroundMusic.getInstance().playMusic();
    }

    private void returnToMenu() {
        Intent i = new Intent(getApplicationContext(), LobbyActivity.class);
        startActivity(i);
        finish();
    }
}