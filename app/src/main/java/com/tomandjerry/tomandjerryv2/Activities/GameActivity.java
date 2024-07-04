package com.tomandjerry.tomandjerryv2.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatImageView;

import com.google.android.material.textview.MaterialTextView;
import com.tomandjerry.tomandjerryv2.GameLogic.GameManager;
import com.tomandjerry.tomandjerryv2.GameLogic.MainCharacter;
import com.tomandjerry.tomandjerryv2.Interfaces.GameCallBack;
import com.tomandjerry.tomandjerryv2.Interfaces.MoveCallback;
import com.tomandjerry.tomandjerryv2.R;
import com.tomandjerry.tomandjerryv2.Utilities.MoveDetector;
import com.tomandjerry.tomandjerryv2.Utilities.MyBackgroundMusic;
import com.tomandjerry.tomandjerryv2.Utilities.MyLocationManager;
import com.tomandjerry.tomandjerryv2.Utilities.MyMediaPlayer;
import com.tomandjerry.tomandjerryv2.Enums.HitStatus;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GameActivity extends AppCompatActivity {

    private final static int DEFAULTDELAY = 1000;
    private AppCompatImageView[][] gridImages;
    private MaterialTextView scoreTextView;
    private MaterialTextView metersTextView;
    private AppCompatImageView[] lifeScale;
    private AppCompatImageButton buttonLeft, buttonRight;
    private AppCompatImageButton returnButton;
    private int stepCounter;
    private GameManager gameManager;
    private MyMediaPlayer myMediaPlayer;
    private MyLocationManager myLocationManager;
    private MoveDetector moveDetector;
    private Vibrator vib;
    private int rows;
    private int cols;
    private int delay;
    private boolean isSensor, isFast;
    private ExecutorService executorService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.game_activity);

        findViews();
        initViews();
        setListeners();
    }

    private void setListeners() {
        buttonLeft.setOnClickListener(v -> moveCharRight(false));
        buttonRight.setOnClickListener(v -> moveCharRight(true));
        returnButton.setOnClickListener(v -> returnToMenu());
    }


    private void moveCharRight(boolean direction) {
        executorService.execute(() -> {
            gameManager.mainCharacterMoveRight(direction);
            if (stepCounter > 2) stepCounter = 0;

            if (stepCounter == 0)
                myMediaPlayer.playSound(R.raw.sn_originalstepssound1, direction ? 0.5f : 0, direction ? 0 : 0.5f);
            else if (stepCounter == 1)
                myMediaPlayer.playSound(R.raw.sn_originalstepssound2, direction ? 0.5f : 0, direction ? 0 : 0.5f);
            else if (stepCounter == 2)
                myMediaPlayer.playSound(R.raw.sn_originalstepssound3, direction ? 0.5f : 0, direction ? 0 : 0.5f);

            stepCounter++;
        });

    }

    private void initViews() {
        stepCounter = 0;
        myMediaPlayer = new MyMediaPlayer(this);
        myLocationManager = new MyLocationManager(this);
        executorService = Executors.newSingleThreadExecutor();
        vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        initMoveDetector();
        Intent prev = getIntent();
        Bundle bundle = prev.getExtras();
        if (bundle != null && bundle.getInt("FAST") == 1) {
            delay = DEFAULTDELAY / 2;
            isFast = true;
        }
        if (bundle != null && bundle.getInt("SENSOR") == 1) {
            isSensor = true;
            buttonLeft.setEnabled(false);
            buttonRight.setEnabled(false);
            buttonLeft.setVisibility(View.INVISIBLE);
            buttonRight.setVisibility(View.INVISIBLE);


        } else {
            buttonLeft.setOnClickListener(v -> moveCharRight(false));
            buttonRight.setOnClickListener(v -> moveCharRight(true));
        }

        GameCallBack gameCallBack = new GameCallBack() {
            @Override

            public void onHit(HitStatus status) {
                runOnUiThread(() -> {

                    if (status == HitStatus.NO_HIT) {
                        myMediaPlayer.playSound(R.raw.sn_tick, 0.8f, 0.8f);
                        return;
                    } else myMediaPlayer.stopAllSounds();
                    if (status == HitStatus.OBSTACLE_HIT)
                        myMediaPlayer.playSound(R.raw.sn_hit, 1f, 1f);
                    else if (status == HitStatus.BONUS_LIFE)
                        myMediaPlayer.playSound(R.raw.sn_eat, 1f, 1f);
                    else if (status == HitStatus.BONUS_POINTS)
                        myMediaPlayer.playSound(R.raw.sn_kiss, 1f, 1f);
                });
            }

            @Override
            public void onGameUpdated(int[][] gameMatrix, int score, int lives, int meters) {

                runOnUiThread(() -> {
                    for (int row = 0; row < rows - 1; row++) {
                        for (int col = 0; col < cols; col++) {
                            gridImages[row][col].setImageResource(gameMatrix[row][col]);
                        }
                    }
                    // Update score, lives, and meters displays
                    scoreTextView.setText(String.valueOf(score));
                    metersTextView.setText(String.valueOf(meters));
                    setLifeUI(lives);

                });

            }

            @Override
            public void onGameEnd(int score,int meters) {
                runOnUiThread(() -> {
                    Toast.makeText(GameActivity.this, score + " points, lets try again", Toast.LENGTH_LONG).show();
                    vib.vibrate(500);
                });

                executorService.execute(() -> {
                    gameManager.updateScoreBoard(score,meters, myLocationManager.getLatitude(), myLocationManager.getLongitude());
                });
            }

            @Override
            public void onGameStart(int loc, int mainCharacterRow, int mainCharacterImage) {
                runOnUiThread(() -> {
                    for (int x = 0; x < cols; x++) {
                        gridImages[mainCharacterRow][x].setImageResource(0);
                    }
                    gridImages[mainCharacterRow][loc].setImageResource(mainCharacterImage);
                });
            }

            @Override
            public void onCharacterMove(int pervLoc, int loc, int mainCharacterRow, int mainCharacterImage) {
                runOnUiThread(() -> {
                    gridImages[mainCharacterRow][pervLoc].setImageResource(0);
                    gridImages[mainCharacterRow][loc].setImageResource(mainCharacterImage);

                });
            }


        };


        executorService.execute(() -> {
            gameManager = new GameManager(3, 3, gridImages.length, gridImages[0].length,
                    new MainCharacter(R.drawable.ic_jerry, gridImages[0].length / 2, gridImages[0].length),
                    delay, gameCallBack);
        });
    }

    private void initMoveDetector() {
        moveDetector = new MoveDetector(this,
                new MoveCallback() {

                    @Override
                    public void moveLeft() {
                        moveCharRight(false);
                    }

                    @Override
                    public void moveRight() {
                        moveCharRight(true);
                    }

                    @Override
                    public void moveUp() {
                        if (isFast)
                            delay = DEFAULTDELAY / 4;
                        else delay = DEFAULTDELAY / 2;
                        gameManager.setTickTime(delay);
                    }

                    @Override
                    public void moveDown() {
                        if (isFast)
                            delay = DEFAULTDELAY / 2;
                        else delay = DEFAULTDELAY;
                        gameManager.setTickTime(delay);
                    }
                });

    }


    private void setLifeUI(int lives) {
        for (int x = 0; x < lives; x++)
            lifeScale[x].setVisibility(View.VISIBLE);
        for (int y = lives; y < lifeScale.length; y++)
            lifeScale[y].setVisibility(View.INVISIBLE);
    }

    private void findViews() {

        // Initialize gridImages


        int[][] imageViewIds = {
                {R.id.imageView_col0_row0, R.id.imageView_col1_row0, R.id.imageView_col2_row0, R.id.imageView_col3_row0, R.id.imageView_col4_row0},
                {R.id.imageView_col0_row1, R.id.imageView_col1_row1, R.id.imageView_col2_row1, R.id.imageView_col3_row1, R.id.imageView_col4_row1},
                {R.id.imageView_col0_row2, R.id.imageView_col1_row2, R.id.imageView_col2_row2, R.id.imageView_col3_row2, R.id.imageView_col4_row2},
                {R.id.imageView_col0_row3, R.id.imageView_col1_row3, R.id.imageView_col2_row3, R.id.imageView_col3_row3, R.id.imageView_col4_row3},
                {R.id.imageView_col0_row4, R.id.imageView_col1_row4, R.id.imageView_col2_row4, R.id.imageView_col3_row4, R.id.imageView_col4_row4},
                {R.id.imageView_col0_row5, R.id.imageView_col1_row5, R.id.imageView_col2_row5, R.id.imageView_col3_row5, R.id.imageView_col4_row5}
        };
        rows = imageViewIds.length;
        cols = imageViewIds[0].length;
        gridImages = new AppCompatImageView[rows][cols];


        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                gridImages[row][col] = findViewById(imageViewIds[row][col]);
            }
        }
        lifeScale = new AppCompatImageView[]{findViewById(R.id.imageView_cheese3),
                findViewById(R.id.imageView_cheese2), findViewById(R.id.imageView_cheese1)};
        // Initialize control buttons
        buttonLeft = findViewById(R.id.button_left);
        buttonRight = findViewById(R.id.button_right);
        returnButton = findViewById(R.id.button_return);
        scoreTextView = findViewById(R.id.textView_scoreValue);
        metersTextView = findViewById(R.id.textView_odometerValue);

    }

    @Override
    protected void onPause() {
        gameManager.stopGame();
        super.onPause();
        stop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        start();
    }

    private void stop() {
        myMediaPlayer.stopAllSounds();
        if (isSensor)
            moveDetector.stop();
        myLocationManager.detachLocationListener();
        MyBackgroundMusic.getInstance().pauseMusic();
    }

    private void start() {
        if (isSensor)
            moveDetector.start();
        myLocationManager.attachLocationListener();
        gameManager.startGame();
        MyBackgroundMusic.getInstance().playMusic();

    }

    private void returnToMenu() {
        gameManager.updateScoreBoard(gameManager.getScore(),gameManager.getMeters(), myLocationManager.getLatitude(), myLocationManager.getLongitude());
        gameManager.stopGame();
        Intent i = new Intent(getApplicationContext(), LobbyActivity.class);
        startActivity(i);
        finish();
    }
}