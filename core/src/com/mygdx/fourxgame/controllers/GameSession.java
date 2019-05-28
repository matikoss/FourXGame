package com.mygdx.fourxgame.controllers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.fourxgame.mainclasses.GameSessionHud;
import com.mygdx.fourxgame.mainclasses.GameplayConstants;
import com.mygdx.fourxgame.mainclasses.Player;
import com.mygdx.fourxgame.mainclasses.WorldMap;
import com.mygdx.fourxgame.maptiles.*;

import org.neo4j.driver.v1.*;

import java.util.ArrayList;
import java.util.Random;

public class GameSession implements InputProcessor {

    public CameraController cameraController;
    private MapTile selectedTile;
    private boolean isTileSelected;
    private int selectedTileIndex;
    private OrthographicCamera camera;
    private WorldMap worldMap;
    private ArrayList<Player> players;
    private int numberOfPlayers;
    private GameSessionHud hud;

    private Player playerWhoseTurnIs;
    private int indexOfPlayerWhoseTurnIs;

    private boolean isTileToBuySelected;
    private MapTile selectedTileToBuy;

    public boolean showBorder;


    public GameSession(int numberOfPlayers) {
        cameraController = new CameraController();
        this.numberOfPlayers = numberOfPlayers;
        players = new ArrayList<>();
        createPlayers();
        worldMap = new WorldMap(numberOfPlayers, players);
        selectedTile = null;
        isTileSelected = false;
        selectedTileToBuy = null;
        isTileToBuySelected = false;
        worldMap.getMapOfWorld().add(new Army(0, 0, "OWNER", 100, 115, 50));
        showBorder = false;
        //loadMapFromDatabase();
        //mapOfWorld.add(new Army(2, 2, "Mati", 0, 0, 0));
        //Gdx.input.setInputProcessor(this);
        moveCameraToCurrentPlayerFirstTown(playerWhoseTurnIs);
    }

    public void update(float deltaTime) {
        cameraController.update(deltaTime);
        worldMap.update(deltaTime);
    }

    public void endTurn() {
        indexOfPlayerWhoseTurnIs++;
        System.out.println(indexOfPlayerWhoseTurnIs);
        System.out.println(players.size());
        if (indexOfPlayerWhoseTurnIs < players.size()) {
            nextPlayerTurn();
        } else {
            nextGameTurn();
        }
    }

    private void nextPlayerTurn() {
        playerWhoseTurnIs = players.get(indexOfPlayerWhoseTurnIs);
        moveCameraToCurrentPlayerFirstTown(playerWhoseTurnIs);
    }

    private void nextGameTurn() {
        indexOfPlayerWhoseTurnIs = 0;
        playerWhoseTurnIs = players.get(indexOfPlayerWhoseTurnIs);
        newTurnUpdate();
        moveCameraToCurrentPlayerFirstTown(playerWhoseTurnIs);

    }

    private void newTurnUpdate() {
        for (Player player : players) {
            player.newTurnUpdate();
        }
    }

    public void recruit(int amountOfArchers, int amountOfFootmans, int amountOfCavalry, TownTile townWhereRecruit) {
        players.get(indexOfPlayerWhoseTurnIs).recruit(townWhereRecruit, amountOfArchers, amountOfFootmans, amountOfCavalry);
    }

    private void createPlayers() {
        for (int i = 0; i < numberOfPlayers; i++) {
            players.add(new Player("Player" + i, 100, 100, 100, 50));
        }
        playerWhoseTurnIs = players.get(0);
        indexOfPlayerWhoseTurnIs = 0;
    }

    private void checkTileSelection() {
        if (Gdx.input.justTouched() && !isTileSelected) {
            selectTile();
        }
    }

    private void selectTile() {
        int x = Gdx.input.getX();
        int y = Gdx.input.getY();
        System.out.println(x + " " + y);
        if ((y >= (Gdx.graphics.getHeight() - 20 * (Gdx.graphics.getHeight() / 720))) || (y <= (40 * (Gdx.graphics.getHeight() / 720))) || ((x >= (Gdx.graphics.getWidth() - 330 * Gdx.graphics.getWidth() / 1280)) && (y >= (Gdx.graphics.getHeight() - 220 * Gdx.graphics.getHeight() / 720)))) {
            System.out.println(Gdx.graphics.getWidth() + " " + Gdx.graphics.getHeight());
            return;
        }

        Vector3 tileCoordinates = translateCoordinates(x, y);

        System.out.println(tileCoordinates.x + " " + tileCoordinates.y);

        x = (int) tileCoordinates.x;
        y = (int) tileCoordinates.y;


        if (tileCoordinates.x < 0) {
            x--;
        }
        if (tileCoordinates.y < 0) {
            y--;
        }
        System.out.println(x + " " + y);

        for (MapTile tile : worldMap.getMapOfWorld()) {
            if (tile.x == x && tile.y == y) {
                selectedTile = tile;
                isTileSelected = true;
                selectedTileIndex = worldMap.getMapOfWorld().indexOf(tile);
                if (selectedTile.getClass().getSimpleName().equals("Army")) {
                    return;
                }
            }
        }
        System.out.println(selectedTile.getClass().getSimpleName());

        //System.out.println(selectedTile.getClass().getSimpleName().equals("Army"));
        //System.out.println(selectedTileIndex);
        //System.out.println(selectedTile.x + " " + selectedTile.y);
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

        if (tileCoordinates.x < 0) {
            newX--;
        }
        if (tileCoordinates.y < 0) {
            newY--;
        }

        if (worldMap.calculateDistance(selectedTile.x, selectedTile.y, newX, newY) > ((Army) selectedTile).calculateRange()) {
            return;
        }

        if (worldMap.calculateDistance(selectedTile.x, selectedTile.y, newX, newY) == 1 && checkIfFight(newX, newY) != null) {
            Army armyToFightWith = checkIfFight(newX, newY);
            fightAtField((Army) selectedTile, armyToFightWith);
            return;
        }

        //((Army) selectedTile).move(newX, newY);
        System.out.println(worldMap.getMapOfWorld().get(selectedTileIndex).x);
        ((Army) worldMap.getMapOfWorld().get(selectedTileIndex)).move(newX, newY, worldMap.calculateDistance(selectedTile.x, selectedTile.y, newX, newY));
        System.out.println(worldMap.getMapOfWorld().get(selectedTileIndex).x);

        checkAndExpandMapIfNecessary((Army) selectedTile, 10);
        //checkExpand((Army) selectedTile);

        selectedTile = null;
        isTileSelected = false;
        selectedTileIndex = 0;

    }

    public void leaveTheTownWithArmy(int archersLeaving, int footmansLeaving, int cavalryLeaving) {
        if ((archersLeaving != 0 || footmansLeaving != 0 || cavalryLeaving != 0) && selectedTile.getClass().getSimpleName().equals("TownTile")) {
            Army armyAtEntrance = getArmyAtTownEntrance();
            Army armyLeaving = ((TownTile) selectedTile).leaveTheTownWithArmy(archersLeaving, footmansLeaving, cavalryLeaving);
            if (armyLeaving == null) {
                return;
            }
            if (armyAtEntrance == null) {
                playerWhoseTurnIs.addArmyToPlayer(armyLeaving);
                worldMap.addToMap(armyLeaving);
                return;
            } else if (armyAtEntrance.getOwner().equals(playerWhoseTurnIs.playerName)) {
                mergeArmy(armyAtEntrance, armyLeaving);
                return;
            }
        }
    }

    private Army getArmyAtTownEntrance() {
        for (MapTile armyTile : worldMap.getMapOfWorld()) {
            if (selectedTile.x == armyTile.x && selectedTile.y - 1 == armyTile.y && armyTile.getClass().getSimpleName().equals("Army")) {
                return (Army) armyTile;
            }
        }
        return null;
    }

    private void checkExpand(Army armyToCheck) {
        int minX = 0;
        int maxX = 0;
        int minY = 0;
        int maxY = 0;

        for (MapTile mapTile : worldMap.getMapOfWorld()) {
            if (mapTile.x < minX) {
                minX = mapTile.x;
            }
            if (mapTile.x > maxX) {
                maxX = mapTile.x;
            }
            if (mapTile.y < minY) {
                minY = mapTile.y;
            }
            if (mapTile.y > maxY) {
                maxY = mapTile.y;
            }
        }

        if (worldMap.calculateDistance(armyToCheck.x, armyToCheck.y, minX, armyToCheck.y) <= 10) {
            worldMap.expandMapSimpleVertically("none", minX - 3, minY, maxY);
        }
        if (worldMap.calculateDistance(armyToCheck.x, armyToCheck.y, maxX, armyToCheck.y) <= 10) {
            worldMap.expandMapSimpleVertically("none", maxX + 3, minY, maxY);
        }
        if (worldMap.calculateDistance(armyToCheck.x, armyToCheck.y, armyToCheck.x, minY) <= 10) {
            worldMap.expandMapHorizontally("none", minX, maxX, minY - 3);
        }
        if (worldMap.calculateDistance(armyToCheck.x, armyToCheck.y, armyToCheck.x, maxY) <= 10) {
            worldMap.expandMapHorizontally("none", minX, maxX, maxY + 3);
        }

    }

    private void checkAndExpandMapIfNecessary(Army armyToCheck, int range) {
        int chunkX, chunkY;


        /*if ((armyToCheck.x + moduloForX) % 5 == 0) {
            chunkX = armyToCheck.x + moduloForX;
        } else {
            chunkX = armyToCheck.x - moduloForX;
        }

        if ((armyToCheck.y + moduloForY) % 5 == 0) {
            chunkY = armyToCheck.y + moduloForY;
        } else {
            chunkY = armyToCheck.y - moduloForY;
        }*/

        chunkX = findMiddleOfChunkCoord(armyToCheck.x);
        chunkY = findMiddleOfChunkCoord(armyToCheck.y);


        boolean[] necessity = new boolean[8];
        for (int checkOnThisRange = range - 2; checkOnThisRange <= range; checkOnThisRange++) {
            for (MapTile mapTile : worldMap.getMapOfWorld()) {
                if (chunkX + range == mapTile.x && chunkY + range == mapTile.y) {
                    necessity[GameplayConstants.north_east - 1] = true;
                }
                if (chunkX + range == mapTile.x && chunkY - range == mapTile.y) {
                    necessity[GameplayConstants.south_east - 1] = true;
                }
                if (chunkX - range == mapTile.x && chunkY - range == mapTile.y) {
                    necessity[GameplayConstants.south_west - 1] = true;
                }
                if (chunkX - range == mapTile.x && chunkY + range == mapTile.y) {
                    necessity[GameplayConstants.north_west - 1] = true;
                }
                if (chunkX + range == mapTile.x && chunkY == mapTile.y) {
                    necessity[GameplayConstants.east - 1] = true;
                }
                if (chunkX - range == mapTile.x && chunkY == mapTile.y) {
                    necessity[GameplayConstants.west - 1] = true;
                }
                if (chunkX == mapTile.x && chunkY + range == mapTile.y) {
                    necessity[GameplayConstants.north - 1] = true;
                }
                if (chunkX == mapTile.x && chunkY - range == mapTile.y) {
                    necessity[GameplayConstants.south - 1] = true;
                }
            }
            boolean wasExpanded = false;
            for (int i = 0; i < necessity.length; i++) {
                if (!necessity[i]) {
                    wasExpanded = true;
                    if (i == 0) {
                        worldMap.expandMap("none", chunkX, chunkY + range, i + 1);
                    } else if (i == 1) {
                        worldMap.expandMap("none", chunkX + range, chunkY + range, i + 1);
                    } else if (i == 2) {
                        worldMap.expandMap("none", chunkX + range, chunkY, i + 1);
                    } else if (i == 3) {
                        worldMap.expandMap("none", chunkX + range, chunkY - range, i + 1);
                    } else if (i == 4) {
                        worldMap.expandMap("none", chunkX, chunkY - range, i + 1);
                    } else if (i == 5) {
                        worldMap.expandMap("none", chunkX - range, chunkY - range, i + 1);
                    } else if (i == 6) {
                        worldMap.expandMap("none", chunkX - range, chunkY, i + 1);
                    } else {
                        worldMap.expandMap("none", chunkX - range, chunkY + range, i + 1);
                    }
                }
            }
            if (wasExpanded) {
                break;
            }

        }

    }

    private int findMiddleOfChunkCoord(int coordinate) {
        int chunkMiddleCoord;

        int moduloOfCoordinate = coordinate % 5;

        if (moduloOfCoordinate == 0) {
            chunkMiddleCoord = coordinate;
        } else if (moduloOfCoordinate == 1) {
            chunkMiddleCoord = coordinate - 1;
        } else if (moduloOfCoordinate == 2) {
            chunkMiddleCoord = coordinate - 2;
        } else if (moduloOfCoordinate == 3) {
            chunkMiddleCoord = coordinate + 2;
        } else if (moduloOfCoordinate == 4) {
            chunkMiddleCoord = coordinate + 1;
        } else if (moduloOfCoordinate == -1) {
            chunkMiddleCoord = coordinate + 1;
        } else if (moduloOfCoordinate == -2) {
            chunkMiddleCoord = coordinate + 2;
        } else if (moduloOfCoordinate == -3) {
            chunkMiddleCoord = coordinate - 2;
        } else {
            chunkMiddleCoord = coordinate - 1;
        }

        return chunkMiddleCoord;
    }

    private void mergeArmy(Army armyAtEntrance, Army armyLeaving) {
        armyAtEntrance.addArmy(armyLeaving.getArchersAmount(), armyLeaving.getFootmansAmount(), armyLeaving.getCavalryAmount());
    }

    private Army checkIfFight(int newX, int newY) {
        for (MapTile mapTile : worldMap.getMapOfWorld()) {
            if (mapTile.getClass().getSimpleName().equals("Army") && mapTile.x == newX && mapTile.y == newY) {
                return (Army) mapTile;
            }
        }
        return null;
    }

    private void fightAtField(Army armyAttacking, Army armyAttacked) {

        if (armyAttacking.getArchersAmount() > 0) {
            archersFightingSequence(armyAttacking, armyAttacked);
        }
        if (armyAttacking.getCavalryAmount() > 0) {
            cavalryFightingSequence(armyAttacking, armyAttacked);
        }
        if (armyAttacking.getFootmansAmount() > 0) {
            infantryFightingSequence(armyAttacking, armyAttacked);
        }

        if (armyAttacking.isArmyEmpty()) {
            removeArmy(armyAttacking);
        }

        if (armyAttacked.isArmyEmpty()) {
            removeArmy(armyAttacked);
        }
    }

    private void archersFightingSequence(Army armyAttacking, Army armyAttacked) {
        Random k6dice = new Random();
        int k6value;
        boolean status;
        for (int attackingArchersAmount = armyAttacking.getArchersAmount(); attackingArchersAmount > 0; attackingArchersAmount--) {
            k6value = k6dice.nextInt(6) + 1;
            if (k6value <= 2) {
                if (armyAttacked.getCavalryAmount() > 0) {
                    status = armyAttacking.archersAttacksCavalry(k6dice, 0);
                    if (status) {
                        armyAttacked.cavalryEliminated();
                    }
                } else if (armyAttacked.getArchersAmount() > 0) {
                    status = armyAttacking.archersAttacksArchers(k6dice, 0);
                    if (status) {
                        armyAttacked.archersEliminated();
                    }
                } else if (armyAttacked.getFootmansAmount() > 0) {
                    status = armyAttacking.archersAttacksInfantry(k6dice, 0);
                    if (status) {
                        armyAttacked.infantryEliminated();
                    }
                } else {
                    break;
                }
            } else if (k6value <= 4) {
                if (armyAttacked.getArchersAmount() > 0) {
                    status = armyAttacking.archersAttacksArchers(k6dice, 0);
                    if (status) {
                        armyAttacked.archersEliminated();
                    }
                } else if (armyAttacked.getCavalryAmount() > 0) {
                    status = armyAttacking.archersAttacksCavalry(k6dice, 0);
                    if (status) {
                        armyAttacked.cavalryEliminated();
                    }
                } else if (armyAttacked.getFootmansAmount() > 0) {
                    status = armyAttacking.archersAttacksInfantry(k6dice, 0);
                    if (status) {
                        armyAttacked.infantryEliminated();
                    }
                }
            } else if (k6value <= 6) {
                if (armyAttacked.getFootmansAmount() > 0) {
                    status = armyAttacking.archersAttacksInfantry(k6dice, 0);
                    if (status) {
                        armyAttacked.infantryEliminated();
                    }
                } else if (armyAttacked.getArchersAmount() > 0) {
                    status = armyAttacking.archersAttacksArchers(k6dice, 0);
                    if (status) {
                        armyAttacked.archersEliminated();
                    }
                } else if (armyAttacked.getCavalryAmount() > 0) {
                    status = armyAttacking.archersAttacksCavalry(k6dice, 0);
                    if (status) {
                        armyAttacked.cavalryEliminated();
                    }
                } else {
                    break;
                }
            }
        }
    }

    private void cavalryFightingSequence(Army armyAttacking, Army armyAttacked) {
        Random k6dice = new Random();
        int k6value;
        boolean status;
        for (int attackingCavalryAmount = armyAttacking.getCavalryAmount(); attackingCavalryAmount > 0; attackingCavalryAmount--) {
            k6value = k6dice.nextInt(6) + 1;
            if (k6value <= 2) {
                if (armyAttacked.getCavalryAmount() > 0) {
                    status = armyAttacking.cavalryAttacksCavalry(k6dice, 0);
                    if (status) {
                        armyAttacked.cavalryEliminated();
                    }
                } else if (armyAttacked.getFootmansAmount() > 0) {
                    status = armyAttacking.cavalryAttacksInfantry(k6dice, 0);
                    if (status) {
                        armyAttacked.infantryEliminated();
                    }
                } else if (armyAttacked.getArchersAmount() > 0) {
                    status = armyAttacking.cavalryAttacksArchers(k6dice, 0);
                    if (status) {
                        armyAttacked.archersEliminated();
                    }
                } else {
                    break;
                }
            } else if (k6value <= 4) {
                if (armyAttacked.getFootmansAmount() > 0) {
                    status = armyAttacking.cavalryAttacksInfantry(k6dice, 0);
                    if (status) {
                        armyAttacked.infantryEliminated();
                    }
                } else if (armyAttacked.getCavalryAmount() > 0) {
                    status = armyAttacking.cavalryAttacksCavalry(k6dice, 0);
                    if (status) {
                        armyAttacked.cavalryEliminated();
                    }
                } else if (armyAttacked.getArchersAmount() > 0) {
                    status = armyAttacking.cavalryAttacksArchers(k6dice, 0);
                    if (status) {
                        armyAttacked.archersEliminated();
                    }
                }
            } else if (k6value <= 6) {
                if (armyAttacked.getArchersAmount() > 0) {
                    status = armyAttacking.cavalryAttacksArchers(k6dice, 0);
                    if (status) {
                        armyAttacked.archersEliminated();
                    }
                } else if (armyAttacked.getFootmansAmount() > 0) {
                    status = armyAttacking.cavalryAttacksInfantry(k6dice, 0);
                    if (status) {
                        armyAttacked.infantryEliminated();
                    }
                } else if (armyAttacked.getCavalryAmount() > 0) {
                    status = armyAttacking.cavalryAttacksCavalry(k6dice, 0);
                    if (status) {
                        armyAttacked.cavalryEliminated();
                    }
                } else {
                    break;
                }
            }
        }
    }

    private void infantryFightingSequence(Army armyAttacking, Army armyAttacked) {
        Random k6dice = new Random();
        int k6value;
        boolean status;
        for (int attackingInfantryAmount = armyAttacking.getFootmansAmount(); attackingInfantryAmount > 0; attackingInfantryAmount--) {
            k6value = k6dice.nextInt(6) + 1;
            if (k6value <= 2) {
                if (armyAttacked.getArchersAmount() > 0) {
                    status = armyAttacking.infantryAttacksArchers(k6dice, 0);
                    if (status) {
                        armyAttacked.archersEliminated();
                    }
                } else if (armyAttacked.getCavalryAmount() > 0) {
                    status = armyAttacking.infantryAttacksCavalry(k6dice, 0);
                    if (status) {
                        armyAttacked.cavalryEliminated();
                    }
                } else if (armyAttacked.getFootmansAmount() > 0) {
                    status = armyAttacking.infantryAttacksInfantry(k6dice, 0);
                    if (status) {
                        armyAttacked.infantryEliminated();
                    }
                } else {
                    break;
                }
            } else if (k6value <= 4) {
                if (armyAttacked.getFootmansAmount() > 0) {
                    status = armyAttacking.infantryAttacksInfantry(k6dice, 0);
                    if (status) {
                        armyAttacked.infantryEliminated();
                    }
                } else if (armyAttacked.getArchersAmount() > 0) {
                    status = armyAttacking.infantryAttacksArchers(k6dice, 0);
                    if (status) {
                        armyAttacked.archersEliminated();
                    }
                } else if (armyAttacked.getCavalryAmount() > 0) {
                    status = armyAttacking.infantryAttacksCavalry(k6dice, 0);
                    if (status) {
                        armyAttacked.cavalryEliminated();
                    }
                }
            } else if (k6value <= 6) {
                if (armyAttacked.getFootmansAmount() > 0) {
                    status = armyAttacking.infantryAttacksInfantry(k6dice, 0);
                    if (status) {
                        armyAttacked.infantryEliminated();
                    }
                } else if (armyAttacked.getCavalryAmount() > 0) {
                    status = armyAttacking.infantryAttacksCavalry(k6dice, 0);
                    if (status) {
                        armyAttacked.cavalryEliminated();
                    }
                } else if (armyAttacked.getArchersAmount() > 0) {
                    status = armyAttacking.infantryAttacksArchers(k6dice, 0);
                    if (status) {
                        armyAttacked.archersEliminated();
                    }
                } else {
                    break;
                }
            }
        }
    }

    private void selectTileToBuy() {
        ArrayList<EmptyTile> tmpList = getTilesToBuy();

        int x = Gdx.input.getX();
        int y = Gdx.input.getY();
        System.out.println(x + " " + y);
        if ((y >= (Gdx.graphics.getHeight() - 20 * (Gdx.graphics.getHeight() / 720))) || (y <= (40 * (Gdx.graphics.getHeight() / 720))) || ((x >= (Gdx.graphics.getWidth() - 330 * Gdx.graphics.getWidth() / 1280)) && (y >= (Gdx.graphics.getHeight() - 220 * Gdx.graphics.getHeight() / 720)))) {
            System.out.println(Gdx.graphics.getWidth() + " " + Gdx.graphics.getHeight());
            return;
        }

        Vector3 tileCoordinates = translateCoordinates(x, y);

        System.out.println(tileCoordinates.x + " " + tileCoordinates.y);

        x = (int) tileCoordinates.x;
        y = (int) tileCoordinates.y;


        if (tileCoordinates.x < 0) {
            x--;
        }
        if (tileCoordinates.y < 0) {
            y--;
        }

        for (EmptyTile tile : tmpList) {
            if (tile.x == x && tile.y == y) {
                for(MapTile tmpMapTile : worldMap.getMapOfWorld()){
                    if(tmpMapTile.x == x && tmpMapTile.y == y && !tmpMapTile.getClass().getSimpleName().equals("Army")){
                        selectedTileToBuy = tmpMapTile;
                        isTileToBuySelected = true;
                        hud.selectedToBuyTileCost.setText("Field cost: " + calculateTileCost(selectedTileToBuy, 10) + "G");
                        hud.isBuyTileMenu = false;
                    }
                }
            }
        }
    }

    public void buyTile(){
        playerWhoseTurnIs.setAmountOfGold(playerWhoseTurnIs.getAmountOfGold() - calculateTileCost(selectedTileToBuy, 10));
        playerWhoseTurnIs.addTileToPlayer(selectedTileToBuy);
        ((TownTile)selectedTile).getTilesNearTown().add(selectedTileToBuy);
        selectedTileToBuy = null;
        isTileToBuySelected = false;
    }


    public ArrayList<EmptyTile> getTilesToBuy() {
        ArrayList<EmptyTile> tmpList = new ArrayList<>();
        boolean toBuy;
        boolean duplicate;
        for (MapTile mapTileChecked : ((TownTile) getSelectedTile()).getTilesNearTown()) {
            for (int i = -1; i <= 1; i++) {
                for (int j = -1; j <= 1; j++) {
                    toBuy = true;
                    for (MapTile mapTileToCompare : ((TownTile) getSelectedTile()).getTilesNearTown()) {
                        if (mapTileChecked.x + j == mapTileToCompare.x && mapTileChecked.y + i == mapTileToCompare.y) {
                            toBuy = false;
                            break;
                        }
                    }
                    if (toBuy) {
                        duplicate = false;
                        for (EmptyTile tmpEmptyTile : tmpList) {
                            if (mapTileChecked.x + j == tmpEmptyTile.x && mapTileChecked.y + i == tmpEmptyTile.y) {
                                duplicate = true;
                            }
                        }
                        if (!duplicate) {
                            tmpList.add(new EmptyTile(mapTileChecked.x + j, mapTileChecked.y + i, ""));
                        }
                    }
                }
            }
        }
        return tmpList;
    }

    public int calculateTileCost(MapTile mapTileToCalculate, int baseCost) {
        return baseCost * (worldMap.calculateDistance(selectedTile.x, selectedTile.y, mapTileToCalculate.x, mapTileToCalculate.y) - 2);
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

    private void removeArmy(Army armyToRemove) {
        String ownerName = armyToRemove.getOwner();
        for (Player player : players) {
            if (player.playerName.equals(ownerName)) {
                player.removeArmy(armyToRemove);
            }
        }
        worldMap.getMapOfWorld().remove(armyToRemove);
    }

    public void setCamera(OrthographicCamera camera) {
        this.camera = camera;
    }

    private Vector3 translateCoordinates(int x, int y) {
        Vector3 worldCoordinates = new Vector3(x, y, 0);
        camera.unproject(worldCoordinates);
        return worldCoordinates;
    }

    private void moveCameraToCurrentPlayerFirstTown(Player playerWhoseTurnIs) {
        for (MapTile mapTile : playerWhoseTurnIs.getTilesOwned()) {
            if (mapTile.getClass().getSimpleName().equals("TownTile")) {
                //Vector3 vectorProjected = camera.project(new Vector3(mapTile.x, mapTile.y,0));
                //cameraController.setPosition(vectorProjected.x, vectorProjected.y);
                cameraController.setPosition(mapTile.x, mapTile.y);
            }
        }
    }

    public boolean isTileSelected() {
        return isTileSelected;
    }

    public MapTile getSelectedTile() {
        return selectedTile;
    }

    public void setSelectedTile(MapTile selectedTile) {
        this.selectedTile = selectedTile;
    }

    public void setTileSelected(boolean tileSelected) {
        isTileSelected = tileSelected;
    }

    public Player getPlayerWhoseTurnIs() {
        return playerWhoseTurnIs;
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
        if (keycode == Input.Keys.B) {
            if (!showBorder) {
                showBorder = true;
            } else {
                showBorder = false;
            }
        }
        return false;
    }

    @Override
    public boolean keyTyped(char character) {

        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        int x = Gdx.input.getX();
        int y = Gdx.input.getY();

        if ((y >= (Gdx.graphics.getHeight() - 20 * (Gdx.graphics.getHeight() / 720))) || (y <= (40 * (Gdx.graphics.getHeight() / 720)))
                || ((x >= (Gdx.graphics.getWidth() - 330 * Gdx.graphics.getWidth() / 1280)) && (y >= (Gdx.graphics.getHeight() - 220 * Gdx.graphics.getHeight() / 720)))) {
            return false;
        }

        if (button == Input.Buttons.LEFT && !hud.isBuyTileMode) {
            selectTile();
            return true;
        }
        if(button == Input.Buttons.LEFT && hud.isBuyTileMode){
            selectTileToBuy();
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

    public ArrayList<MapTile> getMap() {
        return worldMap.getMapOfWorld();

    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public void setHud(GameSessionHud hud) {
        this.hud = hud;
    }

    public boolean isTileToBuySelected() {
        return isTileToBuySelected;
    }

}
