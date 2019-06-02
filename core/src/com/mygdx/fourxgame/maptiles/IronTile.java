package com.mygdx.fourxgame.maptiles;

public class IronTile extends ResourcesTile {
    private boolean isIronMineBuilt;

    public IronTile(int x, int y, String owner, boolean isIronMineBuilt) {
        super(x, y, owner, "ironTexture.png");
        this.isIronMineBuilt = isIronMineBuilt;
    }
    public void newTurnUpdate(){
        
    }

    @Override
    public void build() {
        isIronMineBuilt = true;
    }

    @Override
    public boolean isBuilt() {
        return isIronMineBuilt;
    }

    public boolean isIronMineBuilt() {
        return isIronMineBuilt;
    }

    public void setIronMineBuilt(boolean ironMineBuilt) {
        isIronMineBuilt = ironMineBuilt;
    }
}
