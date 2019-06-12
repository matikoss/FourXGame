package com.mygdx.fourxgame.maptiles;

//Klasa reprezentująca na mapie las
public class WoodTile extends ResourcesTile {
    private boolean isLumbermillBuilt;

    public WoodTile(int x, int y, String owner, boolean isLumbermillBuilt) {
        super(x, y, owner, "forestTexture.png");
        this.isLumbermillBuilt = isLumbermillBuilt;
    }

    public void newTurnUpdate(){

    }

    @Override
    public void build() {
        isLumbermillBuilt = true;
    }

    @Override
    public boolean isBuilt() {
        return  isLumbermillBuilt;
    }

    public boolean isLumbermillBuilt() {
        return isLumbermillBuilt;
    }

    public void setLumbermillBuilt(boolean lumbermillBuilt) {
        isLumbermillBuilt = lumbermillBuilt;
    }
}
