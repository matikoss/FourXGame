package com.mygdx.fourxgame.maptiles;

public class TownTile extends MapTile {

    private int castle;
    private int townHall;
    private int wall;
    private int barrack;
    private int stable;
    private int bank;
    private int houses;


    public TownTile(int x, int y, String owner) {
        super(x, y, owner, "townTexture.png");
    }
}
