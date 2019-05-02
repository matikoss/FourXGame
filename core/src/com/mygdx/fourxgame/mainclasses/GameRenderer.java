package com.mygdx.fourxgame.mainclasses;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.fourxgame.controllers.GameController;

public class GameRenderer {

    private GameController gameController;
    private GameSessionRenderer gameSessionRenderer;


    public GameRenderer(GameController gameController) {
        this.gameController = gameController;
        init();
    }

    private void init() {
        gameSessionRenderer = new GameSessionRenderer(gameController.getGameSession());
    }

    public void render() {
        //gameController.cameraController.test(camera);
        //renderTestObjects();
        gameSessionRenderer.render();
    }

    public void resize(int width, int height) {
        gameSessionRenderer.resize(width,height);
    }

    public void dispose() {
        gameSessionRenderer.dispose();
    }
    public void update() {
        gameSessionRenderer.update();
    }

    /*private void renderTestObjects() {
        batch.begin();
        for (Sprite sprite : gameController.testSprites) {
            sprite.draw(batch);
        }
        batch.end();
    }*/
}
