package com.mygdx.fourxgame.mainclasses;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.fourxgame.controllers.GameSession;
import com.mygdx.fourxgame.maptiles.MapTile;

public class GameSessionRenderer {
    private OrthographicCamera camera;
    private GameSession gameSession;
    private SpriteBatch batch;
    private Sprite sprite;
    private GameSessionHud hud;
    private Viewport viewport;

    private InputProcessor gameSessionInputProcessor;
    private InputProcessor hudInputProcessor;
    private InputMultiplexer inputMultiplexer;

    private Texture enemyMarkerTexture;
    private Texture myMarkerTexture;


    public GameSessionRenderer(GameSession gameSession, SpriteBatch batch) {
        this.batch = batch;
        this.gameSession = gameSession;
        init();
        setupInput();
    }

    private void init() {
        camera = new OrthographicCamera(Constants.VIEWPORT_WIDTH, Constants.VIEWPORT_HEIGHT);
        hud = new GameSessionHud(batch, gameSession);
        camera.position.set(0, 0, 0);
        //camera.viewportWidth = (Constants.VIEWPORT_HEIGHT / Gdx.graphics.getHeight()) * Gdx.graphics.getWidth();
        resize(Gdx.graphics.getBackBufferWidth(), Gdx.graphics.getBackBufferHeight());
        camera.update();
//        viewport = new FitViewport(Constants.VIEWPORT_WIDTH, Constants.VIEWPORT_HEIGHT, camera);
//        viewport.apply();
        gameSession.setCamera(camera);

        enemyMarkerTexture = new Texture(Gdx.files.internal("enemyMarker.png"));
        myMarkerTexture = new Texture(Gdx.files.internal("myMarker.png"));
    }

    private void setupInput() {
        inputMultiplexer = new InputMultiplexer();
        gameSessionInputProcessor = gameSession;
        hudInputProcessor = hud.stage;

        inputMultiplexer.addProcessor(gameSessionInputProcessor);
        inputMultiplexer.addProcessor(hudInputProcessor);
        Gdx.input.setInputProcessor(inputMultiplexer);

    }

    public void render() {
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        for (MapTile tile : gameSession.getMap()) {
            sprite = new Sprite(tile.getTexture());
            sprite.setSize(1, 1);
            sprite.setPosition(tile.x, tile.y);
            sprite.draw(batch);
        }
        if(gameSession.showBorder){
            renderBorders(batch);
        }
        //renderGrid(batch);
        if (gameSession.isTileSelected()) {
            renderSelectionMarker(batch);
        }
        batch.end();
        batch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.render();
        hud.stage.draw();
    }


    public void resize(int width, int height) {
        hud.resize(width, height);
        camera.viewportWidth = (Constants.VIEWPORT_HEIGHT / height) * width;
        camera.update();

    }

    public void update() {
        hud.setPlayerWhoseTurnIs(gameSession.getPlayerWhoseTurnIs());
        hud.update();
        gameSession.cameraController.applyTo(camera);
        batch.setProjectionMatrix(camera.combined);
    }

    public void renderGrid(SpriteBatch batch) {
        Texture texture = new Texture(Gdx.files.internal("selectionTexture.png"));
        for (int i = -100; i <= 100; i++) {
            for (int j = -100; j <= 100; j++) {
                sprite = new Sprite(texture);
                sprite.setSize(1, 1);
                sprite.setPosition(i, j);
                sprite.draw(batch);
            }
        }
    }

    private void renderSelectionMarker(SpriteBatch batch){
        Texture texture = new Texture(Gdx.files.internal("selectionTexture.png"));
        sprite = new Sprite(texture);
        sprite.setSize(1, 1);
        sprite.setPosition(gameSession.getSelectedTile().x, gameSession.getSelectedTile().y);
        sprite.draw(batch);
    }

    private void renderBorders(SpriteBatch batch) {
        if (gameSession.getPlayerWhoseTurnIs() != null) {
            for (MapTile playerTile : gameSession.getPlayerWhoseTurnIs().getArmyOwned()) {
                sprite = new Sprite(myMarkerTexture);
                sprite.setSize(1, 1);
                sprite.setPosition(playerTile.x, playerTile.y);
                sprite.draw(batch);
            }

            for (MapTile playerTile : gameSession.getPlayerWhoseTurnIs().getTilesOwned()) {
                sprite = new Sprite(myMarkerTexture);
                sprite.setSize(1, 1);
                sprite.setPosition(playerTile.x, playerTile.y);
                sprite.draw(batch);
            }

            for (Player player : gameSession.getPlayers()) {
                if (!player.getPlayerName().equals(gameSession.getPlayerWhoseTurnIs().getPlayerName())) {
                    for (MapTile enemyTile : player.getTilesOwned()) {
                        sprite = new Sprite(enemyMarkerTexture);
                        sprite.setSize(1, 1);
                        sprite.setPosition(enemyTile.x, enemyTile.y);
                        sprite.draw(batch);
                    }

                    for (MapTile enemyTile : player.getArmyOwned()) {
                        sprite = new Sprite(enemyMarkerTexture);
                        sprite.setSize(1, 1);
                        sprite.setPosition(enemyTile.x, enemyTile.y);
                        sprite.draw(batch);
                    }
                }
            }
        }

    }

}
