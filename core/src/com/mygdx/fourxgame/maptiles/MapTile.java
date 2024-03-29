package com.mygdx.fourxgame.maptiles;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

//Klasa abstrakcyjna stanowiąca ogólny obraz pola mapy
public abstract class MapTile {
    private String classType;
    public int x;
    public int y;
    private transient String  spriteImageDir;
    private transient Texture texture;
    private String owner;

    public MapTile(int x, int y, String owner, String spriteImageDir) {
        this.classType = this.getClass().getSimpleName();
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

    public String getClassType() {
        return classType;
    }

    public void setSpriteImageDir(String spriteImageDir) {
        this.spriteImageDir = spriteImageDir;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if (!(obj instanceof MapTile)) {
            return false;
        }

        MapTile mapTile = (MapTile) obj;

        if (x == mapTile.x && y == mapTile.y && getOwner().equals(mapTile.getOwner()) && classType.equals(mapTile.classType)) {
            return true;
        } else {
            return false;
        }
    }
}
