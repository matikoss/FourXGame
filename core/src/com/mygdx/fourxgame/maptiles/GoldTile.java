package com.mygdx.fourxgame.maptiles;

public class GoldTile extends ResourcesTile {
    private boolean isGoldMineBuilt;
    public GoldTile(int x, int y, String owner) {
        super(x, y, owner, "goldTexture.png");
        isGoldMineBuilt = false;
    }

    @Override
    public void newTurnUpdate() {

    }

    public boolean isGoldMineBuilt() {
        return isGoldMineBuilt;
    }

    public void setGoldMineBuilt(boolean goldMineBuilt) {
        isGoldMineBuilt = goldMineBuilt;
    }
}
