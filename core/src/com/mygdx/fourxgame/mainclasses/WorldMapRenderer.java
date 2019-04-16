package com.mygdx.fourxgame.mainclasses;


import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.fourxgame.controllers.WorldMap;
import com.mygdx.fourxgame.maptiles.MapTile;

public class WorldMapRenderer {
    private WorldMap worldMap;
    private SpriteBatch batch;
    private Sprite sprite;



    public WorldMapRenderer(WorldMap worldMap, SpriteBatch batch){

        this.worldMap = worldMap;
        this.batch = batch;
    }

    public void render(){
        batch.begin();
        for(MapTile tile : worldMap.getMapOfWorld()){
            sprite = new Sprite(tile.getTexture());
            sprite.setSize(1,1);
            sprite.setPosition(tile.x, tile.y);
            sprite.draw(batch);
        }
        batch.end();
    }
}
