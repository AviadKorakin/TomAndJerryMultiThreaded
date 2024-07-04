package com.tomandjerry.tomandjerryv2.GameLogic;

public class Bonus {
    private final int image;
    private int scoreValue;//score -1 for extra life.

    // Constructor
    public Bonus(int image, int scoreValue) {
        this.image = image;
        this.scoreValue = scoreValue;
    }

    // Getter for image
    public int getImage() {
        return image;
    }


    // Getter for scoreValue
    public int getScoreValue() {
        return scoreValue;
    }

    // Setter for scoreValue
    public void setScoreValue(int scoreValue) {
        this.scoreValue = scoreValue;
    }

    // Override equals method
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Bonus bonus = (Bonus) o;

        return image == bonus.image;
    }

    // Override hashCode method
    @Override
    public int hashCode() {
        int result = image;
        result = 31 * result + scoreValue;
        return result;
    }
}