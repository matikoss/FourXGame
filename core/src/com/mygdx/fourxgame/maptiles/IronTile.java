package com.mygdx.fourxgame.maptiles;

public class IronTile extends ResourcesTile {
    private boolean isIronMineBuilt;

    public IronTile(int x, int y, String owner) {
        super(x, y, owner, "ironTexture.png");
        isIronMineBuilt = false;
    }
    public void newTurnUpdate(){
        
    }

    public boolean isIronMineBuilt() {
        return isIronMineBuilt;
    }

    public void setIronMineBuilt(boolean ironMineBuilt) {
        isIronMineBuilt = ironMineBuilt;
    }
}
