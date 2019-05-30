package com.mygdx.fourxgame.mainclasses;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.fourxgame.controllers.GameSession;
import com.mygdx.fourxgame.maptiles.Army;
import com.mygdx.fourxgame.maptiles.EmptyTile;
import com.mygdx.fourxgame.maptiles.MapTile;

import java.util.ArrayList;

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
    private Texture tileToBuyTexture;
    private Texture armyRangeTexture;


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
        tileToBuyTexture = new Texture(Gdx.files.internal("buyTexture.png"));
        armyRangeTexture = new Texture(Gdx.files.internal("armyRangeMarker.png"));
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

        renderMapTiles(batch);
        renderArmies(batch);
        if(gameSession.showBorder){
            renderBorders(batch);
        }
        //renderGrid(batch);
        if (gameSession.isTileSelected()) {
            renderSelectionMarker(batch);
        }
        if(hud.isBuyTileMode){
            showTilesToBuy(gameSession.getTilesToBuy(), batch);
        }
        if(gameSession.getSelectedTile()!=null){
            renderArmyRange(batch);
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

    private void renderMapTiles(SpriteBatch batch){
        for (MapTile tile : gameSession.getMap()) {
            if(!tile.getClass().getSimpleName().equals("Army")){
                sprite = new Sprite(tile.getTexture());
                sprite.setSize(1, 1);
                sprite.setPosition(tile.x, tile.y);
                sprite.draw(batch);
            }
        }
    }

    private void renderArmies(SpriteBatch batch){
        for(Player player : gameSession.getPlayers()){
            for(Army army : player.getArmyOwned()){
                sprite = new Sprite(army.getTexture());
                sprite.setSize(1,1);
                sprite.setPosition(army.x, army.y);
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

    private void showTilesToBuy(ArrayList<EmptyTile> tilesAvailable, SpriteBatch batch) {
        for (EmptyTile tile : tilesAvailable) {
            sprite = new Sprite(tileToBuyTexture);
            sprite.setSize(1, 1);
            sprite.setPosition(tile.x, tile.y);
            sprite.draw(batch);
        }
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

    private void renderArmyRange(SpriteBatch batch){
        if(gameSession.getSelectedTile().getClass().getSimpleName().equals("Army")){
            int range = ((Army)gameSession.getSelectedTile()).getMoveDistanceLeft();
            for(int i = -range; i<=range; i++){
                for(int j=-range; j<=range; j++){
                    sprite = new Sprite(armyRangeTexture);
                    sprite.setSize(1,1);
                    sprite.setPosition(gameSession.getSelectedTile().x+j, gameSession.getSelectedTile().y+i);
                    sprite.draw(batch);
                }
            }
        }
    }

    public GameSessionHud getHud() {
        return hud;
    }
}
