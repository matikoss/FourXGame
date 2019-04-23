package com.mygdx.fourxgame.mainclasses;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.fourxgame.controllers.WorldMap;
import com.mygdx.fourxgame.maptiles.MapTile;

public class WorldMapRenderer {
    private WorldMap worldMap;
    private SpriteBatch batch;
    private Sprite sprite;


    public WorldMapRenderer(WorldMap worldMap, SpriteBatch batch) {

        this.worldMap = worldMap;
        this.batch = batch;
    }

    public void render() {
        batch.begin();
        for (MapTile tile : worldMap.getMapOfWorld()) {
            sprite = new Sprite(tile.getTexture());
            sprite.setSize(1, 1);
            sprite.setPosition(tile.x, tile.y);
            sprite.draw(batch);
        }
        if (worldMap.isTileSelected()) {
            Texture texture = new Texture(Gdx.files.internal("selectionTexture.png"));
            sprite = new Sprite(texture);
            sprite.setSize(1, 1);
            sprite.setPosition(worldMap.getSelectedTile().x, worldMap.getSelectedTile().y);
            sprite.draw(batch);
        }
        batch.end();
    }
}
