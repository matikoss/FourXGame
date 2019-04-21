package com.mygdx.fourxgame.maptiles;

public class Army extends MapTile {
    private int archersAmount;
    private int footmansAmount;
    private int cavalryAmount;
    public Army(int x, int y, String owner) {
        super(x, y, owner, "armyTexture.jpg");
    }
}
