package com.mygdx.fourxgame.maptiles;

public class EmptyTile extends MapTile {
    public EmptyTile(int x, int y, String owner) {
        super(x, y, owner, "emptyTileTexture.png");
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MapTile mapTile = (MapTile) o;
        return (x == mapTile.x && y == mapTile.y);
    }

    @Override
    public void newTurnUpdate() {

    }
}
