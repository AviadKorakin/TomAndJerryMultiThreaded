package com.tomandjerry.tomandjerryv2.Activities;
import androidx.annotation.NonNull;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.core.app.ActivityCompat;
import androidx.core.splashscreen.SplashScreen;

import com.tomandjerry.tomandjerryv2.R;
import com.tomandjerry.tomandjerryv2.Utilities.MyBackgroundMusic;


public class LobbyActivity extends AppCompatActivity {
    private static final int REQUEST_FINELOCATION_PERMISSION = 999;
    private static final int REQUEST_COARSELOCATION_PERMISSION = 998;
    private AppCompatImageButton TomAndJerry_LaunchGame;
    private AppCompatImageButton TomAndJerry_Buttons;
    private AppCompatImageButton TomAndJerry_Sensor;
    private AppCompatImageButton TomAndJerry_Fast;
    private AppCompatImageButton TomAndJerry_Slow;
    private AppCompatImageButton TomAndJerry_Scoreboard;
    private AppCompatImageButton TomAndJerry_Rest;
    private int fast=0;
    private int sensor=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SplashScreen.installSplashScreen(this);
        setContentView(R.layout.lobby_activity);
        findViews();
        setListeners();
        requestLocationPermission();

    }

    private void requestLocationPermission() {
        String[] permissions = new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION  };

        if (ActivityCompat.checkSelfPermission(this, permissions[0]) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, permissions, REQUEST_FINELOCATION_PERMISSION);
        }
        if (ActivityCompat.checkSelfPermission(this, permissions[1]) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, permissions, REQUEST_COARSELOCATION_PERMISSION);
        }
    }

    private void setListeners() {
        TomAndJerry_Fast.setOnClickListener(v -> checkBoxManipulationFast(1));
        TomAndJerry_Slow.setOnClickListener(v -> checkBoxManipulationFast(0));
        TomAndJerry_Buttons.setOnClickListener(v -> checkBoxManipulationButtons(1));
        TomAndJerry_Sensor.setOnClickListener(v -> checkBoxManipulationButtons(0));
        TomAndJerry_Rest.setOnClickListener(v -> reset());
        TomAndJerry_LaunchGame.setOnClickListener(v -> launchGame());
        TomAndJerry_Scoreboard.setOnClickListener(v -> scoreboard());
    }
    private void scoreboard() {
        Intent i = new Intent(getApplicationContext(), MapsActivity.class);
        // Intent i = new Intent(getApplicationContext(), ScoreBoard_Activity.class);
        startActivity(i);
        finish();
    }


    private void launchGame() {
        if(testPicks())
        {
            Intent i = new Intent(getApplicationContext(), GameActivity.class);
            Bundle bundle = new Bundle();
            bundle.putInt("FAST", fast);
            bundle.putInt("SENSOR", sensor);
            i.putExtras(bundle);
            startActivity(i);
            finish();
        }
    }

    private boolean testPicks() {
        int msg1 = 0;
        int msg2 = 0;
        if (TomAndJerry_Slow.getVisibility() == View.VISIBLE
                && TomAndJerry_Fast.getVisibility() == View.VISIBLE)
        {
            msg1++;
        }
        if (TomAndJerry_Buttons.getVisibility() == View.VISIBLE
                && TomAndJerry_Sensor.getVisibility() == View.VISIBLE)
        {
            msg2++;
        }

        if (msg1 == 0 && msg2 == 0) return true;
        else {
            if (msg1 == 1 && msg2 == 0) {
                Toast.makeText(this, "Please pick speed", Toast.LENGTH_SHORT).show();
            } else {
                if (msg1 == 0) {
                    Toast.makeText(this, "Please pick button mode", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Please pick speed and button mode", Toast.LENGTH_SHORT).show();
                }
            }
        }
        return false;
    }

    private void reset() {
        TomAndJerry_Slow.setVisibility(View.VISIBLE);
        TomAndJerry_Fast.setVisibility(View.VISIBLE);
        TomAndJerry_Sensor.setVisibility(View.VISIBLE);
        TomAndJerry_Buttons.setVisibility(View.VISIBLE);
    }

    private void checkBoxManipulationFast(int i) {
        if(i==1)
        {
            TomAndJerry_Slow.setVisibility(View.INVISIBLE);
            fast=1;
        }
        else {
            TomAndJerry_Fast.setVisibility(View.INVISIBLE);
            fast=0;
        }
    }
    private void checkBoxManipulationButtons(int i) {
        if(i==1)
        {
            TomAndJerry_Sensor.setVisibility(View.INVISIBLE);
            sensor=0;
        }
        else {
            TomAndJerry_Buttons.setVisibility(View.INVISIBLE);
            sensor=1;
        }
    }

    private void findViews() {
        TomAndJerry_LaunchGame = findViewById(R.id.TomAndJerry_LaunchGame);
        TomAndJerry_Buttons = findViewById(R.id.TomAndJerry_Buttons);
        TomAndJerry_Sensor = findViewById(R.id.TomAndJerry_Sensor);
        TomAndJerry_Fast = findViewById(R.id.TomAndJerry_Fast);
        TomAndJerry_Slow = findViewById(R.id.TomAndJerry_Slow);
        TomAndJerry_Scoreboard = findViewById(R.id.TomAndJerry_Scoreboard);
        TomAndJerry_Rest = findViewById(R.id.TomAndJerry_Reset);
    }

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

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_FINELOCATION_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show();
            }
        }
        if (requestCode == REQUEST_COARSELOCATION_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}