package com.mygdx.fourxgame.maptiles;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;


public abstract class MapTile {
    public int x;
    public int y;
    private String spriteImageDir;
    private Texture texture;
    private String owner;

    public MapTile(int x, int y, String owner, String spriteImageDir) {
        this.spriteImageDir = spriteImageDir;
        this.x = x;
        this.y = y;
        this.owner = owner;
        texture = new Texture(Gdx.files.internal(spriteImageDir));
    }
    public abstract void newTurnUpdate();

    public Texture getTexture() {
        return texture;
    }

    public void dispose() {
        texture.dispose();
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

}
