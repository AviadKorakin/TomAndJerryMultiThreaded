package com.tomandjerry.tomandjerryv2.GameLogic;

public class MainCharacter {
    private int image;
    private int location;
    private final int length;

    public MainCharacter(int image, int location, int length) {
        this.image = image;
        this.location = location;
        this.length = length;
    }


    public int moveLeft() {
        location = (location == 0) ? length - 1 : --location;
        return location;
    }

    public int moveRight() {
        location = (location == length - 1) ? 0 : ++location;
        return location;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public int getLocation() {
        return location;
    }
}
