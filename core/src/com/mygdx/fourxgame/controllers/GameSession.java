package com.mygdx.fourxgame.controllers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.fourxgame.mainclasses.WorldMap;
import com.mygdx.fourxgame.maptiles.*;

import org.neo4j.driver.v1.*;

import java.util.ArrayList;
import java.util.Random;

public class GameSession implements InputProcessor {

    private MapTile selectedTile;
    private boolean isTileSelected;
    private int selectedTileIndex;
    private OrthographicCamera camera;
    private WorldMap worldMap;

    public GameSession() {
        worldMap = new WorldMap();
        selectedTile = null;
        isTileSelected = false;
        //loadMapFromDatabase();
        //mapOfWorld.add(new Army(2, 2, "Mati", 0, 0, 0));
        Gdx.input.setInputProcessor(this);
    }

    public void update(float deltaTime) {
        worldMap.update(deltaTime);
    }

    private void checkTileSelection() {
        if (Gdx.input.justTouched() && !isTileSelected) {
            selectTile();
        }
    }

    private void selectTile() {
        int x = Gdx.input.getX();
        int y = Gdx.input.getY();
        Vector3 tileCoordinates = translateCoordinates(x, y);

        x = (int) tileCoordinates.x;
        y = (int) tileCoordinates.y;

        for (MapTile tile : worldMap.getMapOfWorld()) {
            if (tile.x == x && tile.y == y) {
                selectedTile = tile;
                isTileSelected = true;
                selectedTileIndex = worldMap.getMapOfWorld().indexOf(tile);
            }
            if (tile.getClass().getSimpleName().equals("Army")) {
                return;
            }
        }
        System.out.println(selectedTile.getClass().getSimpleName());
        System.out.println(selectedTile.getClass().getSimpleName().equals("Army"));
        System.out.println(selectedTileIndex);
    }

    private void switchSelection() {
        for (MapTile tile : worldMap.getMapOfWorld()) {
            if (tile.x == selectedTile.x && tile.y == selectedTile.y && !tile.getClass().getSimpleName().equals(selectedTile.getClass().getSimpleName())) {
                System.out.println(tile.getClass().getSimpleName() + "<-------" + selectedTile.getClass().getSimpleName());
                selectedTile = tile;
                isTileSelected = true;
                selectedTileIndex = worldMap.getMapOfWorld().indexOf(tile);
                return;
            }
        }
        System.out.println("Nazwa znalezionej klasy" + selectedTile.getClass().getSimpleName());
    }

    private void moveArmy() {

        int newX = Gdx.input.getX();
        int newY = Gdx.input.getY();
        Vector3 tileCoordinates = translateCoordinates(newX, newY);

        newX = (int) tileCoordinates.x;
        newY = (int) tileCoordinates.y;

        //((Army) selectedTile).move(newX, newY);
        System.out.println(worldMap.getMapOfWorld().get(selectedTileIndex).x);
        ((Army) worldMap.getMapOfWorld().get(selectedTileIndex)).move(newX, newY);
        System.out.println(worldMap.getMapOfWorld().get(selectedTileIndex).x);

        selectedTile = null;
        isTileSelected = false;
        selectedTileIndex = 0;

    }

    private void loadMapFromDatabase() {
        Driver driver = null;
        try {
            driver = GraphDatabase.driver("bolt://localhost:7687", AuthTokens.basic("neo4j", "0000"));
            Session session = driver.session();
            StatementResult result = session.run("MATCH(e) WHERE e:EmptyField OR e:City OR e:Forest " +
                    " RETURN e.class, e.x, e.y, e.owner");
            while (result.hasNext()) {
                Record record = result.next();
                String tileType = record.get("e.class").asString();
                int x = record.get("e.x").asInt();
                int y = record.get("e.y").asInt();
                String owner = record.get("e.owner").asString();
                System.out.println(tileType);
                if (tileType.equals("EmptyTile")) {
                    worldMap.addToMap(new EmptyTile(x, y, owner));
                } else if (tileType.equals("TownTile")) {
                    worldMap.addToMap(new TownTile(x, y, owner));
                } else if (tileType.equals("WoodTile")) {
                    worldMap.addToMap(new WoodTile(x, y, owner));
                }
            }
//            result = session.run("MATCH(t:City) RETURN t.x, t.y, t.owner");
//            while(result.hasNext()){
//                Record record = result.next();
//                int x = record.get("t.x").asInt();
//                int y = record.get("t.y").asInt();
//                String owner = record.get("t.owner").asString();
//                addToMap(new TownTile(x,y,owner));
//            }
//
//            result = session.run("MATCH(w:Forest) RETURN w.x, w.y, w.owner");
//            while(result.hasNext()){
//                Record record = result.next();
//                int x = record.get("w.x").asInt();
//                int y = record.get("w.y").asInt();
//                String owner = record.get("w.owner").asString();
//                addToMap(new WoodTile(x,y,owner));
//            }
            session.close();

        } catch (Exception e) {

        } finally {
            if (driver != null) {
                driver.close();
            }
        }

        System.out.println(worldMap.getMapOfWorld());
    }

    public void setCamera(OrthographicCamera camera) {
        this.camera = camera;
    }

    private Vector3 translateCoordinates(int x, int y) {
        Vector3 worldCoordinates = new Vector3(x, y, 0);
        camera.unproject(worldCoordinates);
        return worldCoordinates;
    }

    public boolean isTileSelected() {
        return isTileSelected;
    }

    public MapTile getSelectedTile() {
        return selectedTile;
    }

    @Override
    public boolean keyDown(int keycode) {
        System.out.println("KeyDown");
        if (keycode == Input.Keys.ESCAPE && isTileSelected) {
            selectedTile = null;
            isTileSelected = false;
            selectedTileIndex = 0;
            return true;
        }
        if (keycode == Input.Keys.TAB && isTileSelected) {
            switchSelection();
            return true;
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (button == Input.Buttons.LEFT) {
            selectTile();
            return true;
        }
        if (button == Input.Buttons.RIGHT && isTileSelected && selectedTile.getClass().getSimpleName().equals("Army")) {
            moveArmy();
            return true;
        }
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }

    public ArrayList<MapTile> getMap(){
        return worldMap.getMapOfWorld();

    }

}
