package com.mygdx.fourxgame.maptiles;

public class TownTile extends MapTile {


    private String townName;
    private int castle;
    private int townHall;
    private int wall;
    private int barrack;
    private int stable;
    private int bank;
    private int houses;


    public TownTile(int x, int y, String owner) {
        super(x, y, owner, "townTexture.png");
        townName = "Town Name";
        castle=0;
        townHall=0;
        wall=0;
        barrack=0;
        stable=0;
        bank=0;
        houses=0;
    }

    public int getCastle() {
        return castle;
    }

    public int getTownHall() {
        return townHall;
    }

    public int getWall() {
        return wall;
    }

    public int getBarrack() {
        return barrack;
    }

    public int getStable() {
        return stable;
    }

    public int getBank() {
        return bank;
    }

    public int getHouses() {
        return houses;
    }

}
