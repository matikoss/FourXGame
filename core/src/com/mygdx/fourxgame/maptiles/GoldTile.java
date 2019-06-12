package com.mygdx.fourxgame.maptiles;

//Klasa reprezentująca złoże złota na mapie
public class GoldTile extends ResourcesTile {
    private boolean isGoldMineBuilt;

    public GoldTile(int x, int y, String owner, boolean isGoldMineBuilt) {
        super(x, y, owner, "goldTexture.png");
        this.isGoldMineBuilt = isGoldMineBuilt;
    }

    @Override
    public void newTurnUpdate() {

    }

    @Override
    public void build() {
        isGoldMineBuilt = true;
    }

    @Override
    public boolean isBuilt() {
        return isGoldMineBuilt;
    }

    public boolean isGoldMineBuilt() {
        return isGoldMineBuilt;
    }

    public void setGoldMineBuilt(boolean goldMineBuilt) {
        isGoldMineBuilt = goldMineBuilt;
    }
}
