package com.mygdx.fourxgame.maptiles;

public class WoodTile extends ResourcesTile {
    private boolean isLumbermillBuilt;
    public WoodTile(int x, int y, String owner) {
        super(x, y, owner, "forestTexture.png");
        isLumbermillBuilt = false;
    }
}
