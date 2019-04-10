package com.mygdx.fourxgame;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class GameRenderer {
    private OrthographicCamera camera;
    private SpriteBatch batch;
    private GameController gameController;

    public GameRenderer(GameController gameController) {
        this.gameController = gameController;
        init();
    }

    private void init() {
        batch = new SpriteBatch();
        camera.position.set(0, 0, 0);
        camera.update();
    }

    public void render() {
        renderTestObjects();
    }

    public void resize(int width, int height) {
        camera.update();
    }

    public void dispose(){
        batch.dispose();
    }

    private void renderTestObjects() {
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        for(Sprite sprite : gameController.testSprites) {
            sprite.draw(batch);
        }
        batch.end();
    }

}
