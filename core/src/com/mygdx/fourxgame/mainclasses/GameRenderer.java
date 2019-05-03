package com.mygdx.fourxgame.mainclasses;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.fourxgame.controllers.GameController;

public class GameRenderer {

    private GameController gameController;
    private GameSessionRenderer gameSessionRenderer;
    private MainMenu mainMenu;
    private SpriteBatch batch;


    public GameRenderer(GameController gameController) {
        this.gameController = gameController;
        init();
    }

    private void init() {
        batch = new SpriteBatch();
        mainMenu = new MainMenu(batch, this, gameController);
    }

    public void startNewGame() {
        gameSessionRenderer = new GameSessionRenderer(gameController.getGameSession(), batch);
    }

    public void render() {
        if (gameController.isInMainMenu() && !gameController.isInGame()) {
            batch.setProjectionMatrix(mainMenu.stage.getCamera().combined);
            mainMenu.stage.draw();
        } else if (gameController.isInGame() && !gameController.isInMainMenu()) {
            gameSessionRenderer.render();
        }

    }

    public void resize(int width, int height) {
        if (gameController.isInGame() && !gameController.isInMainMenu()) {
            gameSessionRenderer.resize(width, height);
        }
    }

    public void dispose() {
        batch.dispose();
    }

    public void update() {
        if (gameController.isInGame() && !gameController.isInMainMenu()) {
            gameSessionRenderer.update();
        }
    }
}
