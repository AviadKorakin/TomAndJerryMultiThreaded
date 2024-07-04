package com.tomandjerry.tomandjerryv2.Interfaces;

import com.tomandjerry.tomandjerryv2.Enums.HitStatus;

public interface GameCallBack {
    void onHit(HitStatus status);

    void onGameUpdated(int[][] gameMatrix, int score, int lives, int meters);

    void onGameEnd(int score,int meters);

    void onGameStart(int loc, int mainCharacterRow, int mainCharacterImage);

    void onCharacterMove(int pervLoc, int loc, int mainCharacterRow, int mainCharacterImage);
}

