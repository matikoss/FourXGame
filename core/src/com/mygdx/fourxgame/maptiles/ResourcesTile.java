package com.mygdx.fourxgame.maptiles;

//Klasa abstrakcyjna reprezentująca wszystkie pola związane z surowcami na mapie
public abstract class ResourcesTile extends  MapTile {
    public ResourcesTile(int x, int y, String owner, String spriteImageDir) {
        super(x, y, owner, spriteImageDir);
    }

    @Override
    public abstract void newTurnUpdate();

    public abstract void build();

    public abstract boolean isBuilt();
}
