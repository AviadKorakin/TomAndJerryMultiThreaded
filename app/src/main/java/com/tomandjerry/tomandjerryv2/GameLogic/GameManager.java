package com.tomandjerry.tomandjerryv2.GameLogic;


import com.tomandjerry.tomandjerryv2.Interfaces.GameCallBack;
import com.tomandjerry.tomandjerryv2.R;

import com.tomandjerry.tomandjerryv2.Utilities.MyScore;
import com.tomandjerry.tomandjerryv2.Utilities.MySharedPreferences;
import com.tomandjerry.tomandjerryv2.Enums.HitStatus;

import java.util.ArrayList;

import java.util.Collections;
import java.util.Random;


public class GameManager {
    private final ArrayList<Integer> obstacles = new ArrayList<>();
    private final ArrayList<Bonus> bonuses = new ArrayList<>();
    private int lives;
    private int maxLives;
    private int meters = 0;
    private int score = 0;
    private final int rows;
    private final int cols;
    private final int[][] gameMatrix;
    private final MainCharacter mainCharacter;
    private final GameCallBack gameCallback;
    private boolean isRunning;
    private Thread gameThread;
    private int TickTime;



    public GameManager(int initialLives, int maxLives, int rows,
                       int cols, MainCharacter mainCharacter, int tickTime,
                       GameCallBack gameCallback) {
        if (initialLives > 0 && initialLives <= 4) {
            lives = initialLives;
            this.maxLives = maxLives;
        }
        if (rows < 0 || cols < 0) {
            throw new RuntimeException("Invalid arguments");
        }
        isRunning = false;
        this.mainCharacter = mainCharacter;
        this.rows = rows;
        this.cols = cols;
        gameMatrix = new int[rows][cols];
        gameMatrix[rows - 1][mainCharacter.getLocation()] = mainCharacter.getImage();
        setObstacles();
        setBonuses();
        this.gameCallback = gameCallback;
        this.TickTime = tickTime;
        gameCallback.onGameStart(mainCharacter.getLocation(), rows - 1, mainCharacter.getImage());
    }

    public void setTickTime(int tickTime) {
        TickTime = tickTime;
    }


    private void setObstacles() {
        obstacles.add(R.drawable.ic_tom);
        obstacles.add(R.drawable.ic_tomevil);
        obstacles.add(R.drawable.ic_tombomb);
        obstacles.add(R.drawable.ic_tomplan);
    }

    private void setBonuses() {
        bonuses.add(new Bonus(R.drawable.ic_jerrybounslife, -1));
        bonuses.add(new Bonus(R.drawable.ic_jerryloverpoint10, 10));
        bonuses.add(new Bonus(R.drawable.ic_jerryloverpoint30, 30));
    }

    public int getLives() {
        return lives;
    }

    public int getScore() {
        return score;
    }


    public int getMeters() {
        return meters;
    }

    public void startGame() {

        gameThread = new Thread(() -> {
            while (isRunning) {
                HitStatus hitStatus = checkForHit();
                gameCallback.onHit(hitStatus);
                updateGame();
                gameCallback.onGameUpdated(gameMatrix, score, lives, meters);


                try {
                    Thread.sleep(TickTime);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt(); // Restore interrupted state
                    e.printStackTrace();
                }
            }
        });
        isRunning = true;
        gameThread.start();

    }

    public void stopGame() {
        isRunning = false;
    }

    private void updateGame() {
        for (int row = rows - 2; row >= 1; row--) {
            System.arraycopy(gameMatrix[row - 1], 0, gameMatrix[row], 0, cols);
        }
        for (int col = 0; col < cols; col++) {
            gameMatrix[0][col] = 0;
        }
        if (toGenerate(6) == 0) {
            gameMatrix[0][generateLocation()] = bonuses.get(generateABonus()).getImage();
        } else {
            gameMatrix[0][generateLocation()] = obstacles.get(generateAObstacle());
        }
        meters++;
    }

    private HitStatus checkForHit() {
        if (obstacles.contains(gameMatrix[rows - 2][mainCharacter.getLocation()])) {
            lives--;
            if (lives == 0) {
                resetGame(maxLives);
            }
            return HitStatus.OBSTACLE_HIT;
        } else {
            for (Bonus b : bonuses) {
                if (b.getImage() == gameMatrix[rows - 2][mainCharacter.getLocation()]) {
                    if (b.getScoreValue() == -1) {
                        if (lives < maxLives) {
                            lives++;
                        }
                        return HitStatus.BONUS_LIFE;
                    } else {
                        score += b.getScoreValue();
                        return HitStatus.BONUS_POINTS;
                    }
                }
            }
        }
        return HitStatus.NO_HIT;
    }

    public void resetGame(int newLives) {
        for (int row = 0; row < rows - 1; row++) {
            for (int col = 0; col < cols; col++) {
                gameMatrix[row][col] = 0;
            }
        }
        gameMatrix[rows - 1][mainCharacter.getLocation()] = mainCharacter.getImage();
        gameCallback.onGameEnd(score,meters);
        score = 0;
        meters=0;
        this.lives = newLives;
    }

    public void updateScoreBoard(int score,int meters,double lat,double lng) {
        if (score > 0) {
            MySharedPreferences mySharedPreferences = MySharedPreferences.getInstance();
            ArrayList<MyScore> scores = mySharedPreferences.readScores();
            if (scores == null) scores = new ArrayList<>();

            scores.add(new MyScore(score,meters, lat, lng));

            Collections.sort(scores);

            if (scores.size() > 10) {
                scores.remove(scores.size() - 1);
            }
            mySharedPreferences.saveScores(scores);
        }
    }

    public void mainCharacterMoveRight(boolean right) {

        int prev = mainCharacter.getLocation();
        int next = right ? mainCharacter.moveRight() : mainCharacter.moveLeft();
        gameMatrix[rows - 1][prev] = 0;
        gameMatrix[rows - 1][next] = mainCharacter.getImage();
        gameCallback.onCharacterMove(prev, next, rows - 1, mainCharacter.getImage());
    }

    private int toGenerate(int num) {
        return new Random().nextInt(num);
    }

    private int generateAObstacle() {
        return new Random().nextInt(obstacles.size());
    }

    private int generateABonus() {
        return new Random().nextInt(bonuses.size());
    }

    private int generateLocation() {
        return new Random().nextInt(cols);
    }
}