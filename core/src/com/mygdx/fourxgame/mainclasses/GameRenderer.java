package com.mygdx.fourxgame.mainclasses;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.fourxgame.controllers.GameController;

public class GameRenderer {
    private OrthographicCamera camera;
    private SpriteBatch batch;
    private GameController gameController;
    private WorldMapRenderer worldMapRenderer;


    public GameRenderer(GameController gameController) {
        this.gameController = gameController;
        init();
    }

    private void init() {
        batch = new SpriteBatch();
        worldMapRenderer = new WorldMapRenderer(gameController.getWorldMap(), batch);
        camera = new OrthographicCamera(Constants.VIEWPORT_WIDTH, Constants.VIEWPORT_HEIGHT);
        camera.position.set(0, 0, 0);
        camera.update();
        gameController.getWorldMap().setCamera(camera);

    }

    public void render() {
        //gameController.cameraController.test(camera);
        //renderTestObjects();
        worldMapRenderer.render();
    }

    public void resize(int width, int height) {
        camera.viewportWidth = (Constants.VIEWPORT_HEIGHT / height) * width;
        camera.update();
    }

    public void dispose() {
        batch.dispose();
    }
    public void update() {
        gameController.cameraController.applyTo(camera);
        batch.setProjectionMatrix(camera.combined);
    }

    /*private void renderTestObjects() {
        batch.begin();
        for (Sprite sprite : gameController.testSprites) {
            sprite.draw(batch);
        }
        batch.end();
    }*/
}
