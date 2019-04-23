package com.mygdx.fourxgame.maptiles;

public class Army extends MapTile {
    private int archersAmount;
    private int footmansAmount;
    private int cavalryAmount;

    public Army(int x, int y, String owner, int archersAmount, int footmansAmount, int cavalryAmount) {
        super(x, y, owner, "armyTexture.png");
        this.archersAmount = archersAmount;
        this.footmansAmount = footmansAmount;
        this.cavalryAmount = cavalryAmount;
    }

    public void move(int newX, int newY) {
        this.x = newX;
        this.y = newY;
    }

    private void calculateRange() {

    }
}
