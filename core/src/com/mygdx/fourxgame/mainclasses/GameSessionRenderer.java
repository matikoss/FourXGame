package com.mygdx.fourxgame.mainclasses;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.fourxgame.controllers.GameSession;
import com.mygdx.fourxgame.maptiles.MapTile;

public class GameSessionRenderer {
    private GameSession gameSession;
    private SpriteBatch batch;
    private Sprite sprite;


    public GameSessionRenderer(GameSession gameSession, SpriteBatch batch) {

        this.gameSession = gameSession;
        this.batch = batch;
    }
    private void init(){

    }

    public void render() {
        batch.begin();
        for (MapTile tile : gameSession.getMap()) {
            sprite = new Sprite(tile.getTexture());
            sprite.setSize(1, 1);
            sprite.setPosition(tile.x, tile.y);
            sprite.draw(batch);
        }
        if (gameSession.isTileSelected()) {
            Texture texture = new Texture(Gdx.files.internal("selectionTexture.png"));
            sprite = new Sprite(texture);
            sprite.setSize(1, 1);
            sprite.setPosition(gameSession.getSelectedTile().x, gameSession.getSelectedTile().y);
            sprite.draw(batch);
        }
        renderGrid(batch);
        batch.end();
    }
    public void renderGrid(SpriteBatch batch){
        Texture texture = new Texture(Gdx.files.internal("selectionTexture.png"));
        for(int i=-100; i<=100; i++){
            for (int j=-100; j<=100; j++){
                sprite = new Sprite(texture);
                sprite.setSize(1, 1);
                sprite.setPosition(i, j);
                sprite.draw(batch);
            }
        }
    }

}
