package com.mygdx.fourxgame.mainclasses;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.fourxgame.controllers.GameSession;
import com.mygdx.fourxgame.maptiles.MapTile;

public class GameSessionRenderer {
    private OrthographicCamera camera;
    private GameSession gameSession;
    private SpriteBatch batch;
    private Sprite sprite;


    public GameSessionRenderer(GameSession gameSession) {
        this.gameSession = gameSession;
        init();
    }
    private void init(){
        batch = new SpriteBatch();
        camera = new OrthographicCamera(Constants.VIEWPORT_WIDTH, Constants.VIEWPORT_HEIGHT);
        camera.position.set(0, 0, 0);
        camera.update();
        gameSession.setCamera(camera);
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
    public void resize(int width, int height){
        camera.viewportWidth = (Constants.VIEWPORT_HEIGHT / height) * width;
        camera.update();
    }

    public void dispose(){
        batch.dispose();
    }

    public void update(){
        gameSession.cameraController.applyTo(camera);
        batch.setProjectionMatrix(camera.combined);
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
