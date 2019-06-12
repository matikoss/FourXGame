package com.mygdx.fourxgame.controllers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.fourxgame.mainclasses.GameSessionHud;
import com.mygdx.fourxgame.mainclasses.GameplayConstants;
import com.mygdx.fourxgame.mainclasses.Player;
import com.mygdx.fourxgame.mainclasses.WorldMap;
import com.mygdx.fourxgame.maptiles.*;


import java.util.ArrayList;
import java.util.Objects;
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
    public int turnNumber;

    private Player playerWhoseTurnIs;

    private int indexOfPlayerWhoseTurnIs;

    private boolean isTileToBuySelected;
    private MapTile selectedTileToBuy;

    public SaveManager saveManager;

    public boolean showBorder;

    public boolean inGame;


    public GameSession(int numberOfPlayers, int startMode, String savePath) {
        cameraController = new CameraController();
        inGame = true;
        if (startMode == 1) {
            startNewGame(numberOfPlayers);
        } else {
            loadGame(savePath);
        }

        selectedTile = null;
        isTileSelected = false;
        selectedTileToBuy = null;
        isTileToBuySelected = false;
        showBorder = false;
        moveCameraToCurrentPlayerFirstTown(playerWhoseTurnIs);
    }

    private void startNewGame(int numberOfPlayers) {
        this.numberOfPlayers = numberOfPlayers;
        players = new ArrayList<>();
        createPlayers();
        worldMap = new WorldMap(numberOfPlayers, players, 1);
        saveManager = new SaveManager(this);
        turnNumber = 1;
    }

    private void loadGame(String savePath) {
        saveManager = new SaveManager(this);
        players = saveManager.loadPlayers(savePath);
        this.numberOfPlayers = players.size();
        worldMap = new WorldMap(numberOfPlayers, players, 2);
        worldMap.addManyToMap(saveManager.loadTownTiles(savePath));
        worldMap.addManyToMap(saveManager.loadArmyTiles(savePath));
        worldMap.addManyToMap(saveManager.loadEmptyTiles(savePath));
        worldMap.addManyToMap(saveManager.loadWoodTiles(savePath));
        worldMap.addManyToMap(saveManager.loadIronTiles(savePath));
        worldMap.addManyToMap(saveManager.loadGoldTiles(savePath));
        indexOfPlayerWhoseTurnIs = saveManager.loadGameState(savePath);
        turnNumber = saveManager.loadTurnNumber(savePath);
        playerWhoseTurnIs = players.get(indexOfPlayerWhoseTurnIs);
        gameSetupAfterLoad();
        textureSetupAfterLoad();
    }

    private void gameSetupAfterLoad() {
        for (Player player : players) {
            ArrayList<MapTile> tmpTilesOwned = new ArrayList<>();
            ArrayList<Army> tmpArmyOwned = new ArrayList<>();
            for (MapTile mapTile : worldMap.getMapOfWorld()) {
                if (mapTile.getOwner().equals(player.getPlayerName()) && mapTile.getClass().getSimpleName().equals("Army")) {
                    tmpArmyOwned.add((Army) mapTile);
                } else if (mapTile.getOwner().equals(player.getPlayerName())) {
                    tmpTilesOwned.add(mapTile);
                }
            }
            player.setTilesOwned(tmpTilesOwned);
            player.setArmyOwned(tmpArmyOwned);
            tilesNearTownsSetup();
        }
    }

    private void textureSetupAfterLoad() {
        for (MapTile mapTile : worldMap.getMapOfWorld()) {
            if (mapTile.getClass().getSimpleName().equals("Army")) {
                mapTile.setSpriteImageDir("armyTexture.png");
                mapTile.setTexture(new Texture(Gdx.files.internal("armyTexture.png")));
            } else if (mapTile.getClass().getSimpleName().equals("EmptyTile")) {
                mapTile.setSpriteImageDir("emptyTileTexture.png");
                mapTile.setTexture(new Texture(Gdx.files.internal("emptyTileTexture.png")));
            } else if (mapTile.getClass().getSimpleName().equals("GoldTile")) {
                mapTile.setSpriteImageDir("goldTexture.png");
                mapTile.setTexture(new Texture(Gdx.files.internal("goldTexture.png")));
            } else if (mapTile.getClass().getSimpleName().equals("IronTile")) {
                mapTile.setSpriteImageDir("ironTexture.png");
                mapTile.setTexture(new Texture(Gdx.files.internal("ironTexture.png")));
            } else if (mapTile.getClass().getSimpleName().equals("TownTile")) {
                mapTile.setSpriteImageDir("townTexture.png");
                mapTile.setTexture(new Texture(Gdx.files.internal("townTexture.png")));
            } else if (mapTile.getClass().getSimpleName().equals("WoodTile")) {
                mapTile.setSpriteImageDir("forestTexture.png");
                mapTile.setTexture(new Texture(Gdx.files.internal("forestTexture.png")));
            }
        }
    }

    private void tilesNearTownsSetup() {
        boolean noTilesAdded;
        int maxX, maxY;
        ArrayList<MapTile> tmpArrayList;
        for (MapTile townTile : worldMap.getMapOfWorld()) {
            maxX = 0;
            maxY = 0;
            tmpArrayList = new ArrayList<>();
            if (townTile.getClass().getSimpleName().equals("TownTile")) {
                tmpArrayList.add(townTile);
                do {
                    maxX++;
                    maxY++;
                    noTilesAdded = true;
                    for (int i = townTile.y - maxY; i <= townTile.y + maxY; i++) {
                        for (MapTile tmpTile : worldMap.getMapOfWorld()) {
                            if (tmpTile.x == (townTile.x - maxX) && tmpTile.y == i && tmpTile.getOwner().equals(townTile.getOwner())) {
                                tmpArrayList.add(tmpTile);
                                noTilesAdded = false;
                            } else if (tmpTile.x == (townTile.x + maxX) && tmpTile.y == i && tmpTile.getOwner().equals(townTile.getOwner())) {
                                tmpArrayList.add(tmpTile);
                                noTilesAdded = false;
                            }
                        }
                    }
                    for (int j = townTile.x - maxX + 1; j < townTile.x + maxX; j++) {
                        for (MapTile tmpTile : worldMap.getMapOfWorld()) {
                            if (tmpTile.x == j && tmpTile.y == (townTile.y - maxY) && tmpTile.getOwner().equals(townTile.getOwner())) {
                                tmpArrayList.add(tmpTile);
                                noTilesAdded = false;
                            } else if (tmpTile.x == j && tmpTile.y == (townTile.y + maxY) && tmpTile.getOwner().equals(townTile.getOwner())) {
                                tmpArrayList.add(tmpTile);
                                noTilesAdded = false;
                            }
                        }
                    }
                } while (!noTilesAdded);
                ((TownTile) townTile).setTilesNearTown(tmpArrayList);
            }
        }
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
        turnNumber++;
        moveCameraToCurrentPlayerFirstTown(playerWhoseTurnIs);

    }

    private void newTurnUpdate() {
        for (MapTile townTile : worldMap.getMapOfWorld()) {
            if (townTile.getClass().getSimpleName().equals("TownTile")) {
                ((TownTile) townTile).checkIfArmyCapturingTown(worldMap.getMapOfWorld());
                if (((TownTile) townTile).getTimeToLoseTown() <= 0) {
                    townCaptured((TownTile) townTile);
                }
            }
        }
        checkPlayersAndRemoveIfNecessary();
        for (Player player : players) {
            player.newTurnUpdate();
        }
        updatePlayersWaitingToBeAdded();
    }

    private void townCaptured(TownTile townTile) {
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                for (MapTile tmpTile : worldMap.getMapOfWorld()) {
                    if (tmpTile.getClass().getSimpleName().equals("Army") && (townTile.x + j == tmpTile.x) && (townTile.y + i == tmpTile.y)) {
                        String playerWhoCaptured = tmpTile.getOwner();
                        transferTownAfterCapture(townTile.getOwner(), playerWhoCaptured, townTile);
                    }
                }
            }
        }
    }

    private void transferTownAfterCapture(String oldOwner, String newOwner, TownTile townCaptured) {
        Player oldOwnerPlayer = null, newOwnerPlayer = null;
        for (Player tmpPlayer : players) {
            if (tmpPlayer.getPlayerName().equals(oldOwner)) {
                oldOwnerPlayer = tmpPlayer;
            }
            if (tmpPlayer.getPlayerName().equals(newOwner)) {
                newOwnerPlayer = tmpPlayer;
            }
        }

        for (int i = -2; i <= 2; i++) {
            for (int j = -2; j <= 2; j++) {
                for (MapTile tmpTile : townCaptured.getTilesNearTown()) {
                    if (tmpTile.x == townCaptured.x + j && tmpTile.y == townCaptured.y + i) {
                        assert newOwnerPlayer != null;
                        newOwnerPlayer.addTileToPlayer(tmpTile);
                        tmpTile.setOwner(newOwner);
                        assert oldOwnerPlayer != null;
                        oldOwnerPlayer.getTilesOwned().remove(tmpTile);
                    }
                }
            }
        }

        for (MapTile tmpTile : townCaptured.getTilesNearTown()) {
            if (!tmpTile.getOwner().equals(newOwner)) {
                tmpTile.setOwner("none");
                townCaptured.getTilesNearTown().remove(tmpTile);
            }
        }
        townCaptured.setTimeToLoseTown(GameplayConstants.timeToLoseTown);

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

    private void checkPlayersAndRemoveIfNecessary() {
        ArrayList<Player> playersToRemove = new ArrayList<>();
        for (Player checkedPlayer : players) {
            if (checkIfRemovePlayerFromGame(checkedPlayer)) {
                playersToRemove.add(checkedPlayer);
            }
        }
        players.removeAll(playersToRemove);
    }

    private boolean checkIfRemovePlayerFromGame(Player playerToCheck) {
        if (playerToCheck.getTilesOwned().isEmpty() && playerToCheck.getArmyOwned().isEmpty()) {
            return true;
        }
        return false;
    }

    private void selectTile() {
        hud.isResourceTileMenu = false;
        int x = Gdx.input.getX();
        int y = Gdx.input.getY();
        if ((y >= (Gdx.graphics.getHeight() - 20 * (Gdx.graphics.getHeight() / 720))) || (y <= (40 * (Gdx.graphics.getHeight() / 720))) || ((x >= (Gdx.graphics.getWidth() - 330 * Gdx.graphics.getWidth() / 1280)) && (y >= (Gdx.graphics.getHeight() - 220 * Gdx.graphics.getHeight() / 720)))) {
            return;
        }

        Vector3 tileCoordinates = translateCoordinates(x, y);


        x = (int) tileCoordinates.x;
        y = (int) tileCoordinates.y;


        if (tileCoordinates.x < 0) {
            x--;
        }
        if (tileCoordinates.y < 0) {
            y--;
        }

        for (MapTile tile : worldMap.getMapOfWorld()) {
            if (tile.x == x && tile.y == y) {
                selectedTile = tile;
                isTileSelected = true;
                selectedTileIndex = worldMap.getMapOfWorld().indexOf(tile);
                System.out.println(selectedTile.getOwner());
                if (selectedTile.getClass().getSimpleName().equals("Army")) {
                    return;
                }
            }
        }
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
    }

    private void moveArmy() {

        if (!playerWhoseTurnIs.getPlayerName().equals(selectedTile.getOwner())) {
            return;
        }

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

        if (checkMerge(newX, newY) != null) {
            mergeArmy((Army) selectedTile, Objects.requireNonNull(checkMerge(newX, newY)));
            return;
        }

        TownTile townWithWhichMightMerge = checkMergeWithTown(newX, newY);
        if (townWithWhichMightMerge != null) {
            mergeArmyWithTown((Army) selectedTile, townWithWhichMightMerge);
            selectedTile = null;
            isTileSelected = false;
            return;
        }

        if (worldMap.calculateDistance(selectedTile.x, selectedTile.y, newX, newY) == 1 && checkIfFight(newX, newY) != null && ((Army) selectedTile).canAttack) {
            Army armyToFightWith = checkIfFight(newX, newY);
            fightAtField((Army) selectedTile, armyToFightWith);
            return;
        }

        System.out.println(worldMap.getMapOfWorld().get(selectedTileIndex).x);
        ((Army) worldMap.getMapOfWorld().get(selectedTileIndex)).move(newX, newY, worldMap.calculateDistance(selectedTile.x, selectedTile.y, newX, newY));
        System.out.println(worldMap.getMapOfWorld().get(selectedTileIndex).x);
        checkAndExpandMapIfNecessary((Army) selectedTile, 10);
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
                mergeArmyAtTownEntrance(armyAtEntrance, armyLeaving);
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

    private void updatePlayersWaitingToBeAdded() {
        worldMap.addWaitingPlayers(turnNumber);
    }

    public void addNewPlayer() {
        String playerName = "Player" + (players.size() + worldMap.getPlayersWaitingToBeAdded().size());
        worldMap.addNewPlayerToWaitingList(playerName);
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

    private void mergeArmyAtTownEntrance(Army armyAtEntrance, Army armyLeaving) {
        armyAtEntrance.addArmy(armyLeaving.getArchersAmount(), armyLeaving.getFootmansAmount(), armyLeaving.getCavalryAmount());
    }

    private Army checkMerge(int newX, int newY) {
        for (MapTile mapTile : worldMap.getMapOfWorld()) {
            if (mapTile.getClass().getSimpleName().equals("Army") && mapTile.x == newX && mapTile.y == newY && mapTile.getOwner().equals(playerWhoseTurnIs.getPlayerName())) {
                return (Army) mapTile;
            }
        }
        return null;
    }

    private TownTile checkMergeWithTown(int newX, int newY) {
        for (MapTile mapTile : worldMap.getMapOfWorld()) {
            if (mapTile.getClass().getSimpleName().equals("TownTile") && mapTile.x == newX && mapTile.y == newY && mapTile.getOwner().equals(playerWhoseTurnIs.getPlayerName())) {
                return (TownTile) mapTile;
            }
        }
        return null;
    }

    private boolean mergeArmyWithTown(Army armyToMerge, TownTile townWithWhichMerge) {
        townWithWhichMerge.addArmy(armyToMerge.getArchersAmount(), armyToMerge.getFootmansAmount(), armyToMerge.getCavalryAmount());
        playerWhoseTurnIs.getArmyOwned().remove(armyToMerge);
        worldMap.getMapOfWorld().remove(armyToMerge);
        return true;
    }

    private void mergeArmy(Army firstArmy, Army secondArmy) {
        firstArmy.addArmy(secondArmy.getArchersAmount(), secondArmy.getFootmansAmount(), secondArmy.getCavalryAmount());
        playerWhoseTurnIs.getArmyOwned().remove(secondArmy);
        worldMap.getMapOfWorld().remove(secondArmy);
    }

    private Army checkIfFight(int newX, int newY) {
        for (MapTile mapTile : worldMap.getMapOfWorld()) {
            if (mapTile.getClass().getSimpleName().equals("Army") && mapTile.x == newX && mapTile.y == newY && !mapTile.getOwner().equals(playerWhoseTurnIs.getPlayerName())) {
                return (Army) mapTile;
            }
        }
        return null;
    }

    public ArrayList<MapTile> getArmiesThatCanBeAttacked() {
        if (isTileSelected && selectedTile.getClass().getSimpleName().equals("Army") && selectedTile.getOwner().equals(playerWhoseTurnIs.getPlayerName())) {
            ArrayList<MapTile> tilesThatCanBeAttacked = new ArrayList<>();
            for (MapTile tmpTile : worldMap.getMapOfWorld()) {
                for (int i = -1; i <= 1; i++) {
                    for (int j = -1; j <= 1; j++) {
                        if (tmpTile.x == selectedTile.x + j && tmpTile.y == selectedTile.y + i && !tmpTile.getOwner().equals(playerWhoseTurnIs.getPlayerName()) &&
                                (tmpTile.getClass().getSimpleName().equals("Army") || tmpTile.getClass().getSimpleName().equals("TownTile"))) {
                            tilesThatCanBeAttacked.add(tmpTile);
                        }
                    }
                }
            }
            return tilesThatCanBeAttacked;
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
        armyAttacking.canAttack = false;
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
                for (MapTile tmpMapTile : worldMap.getMapOfWorld()) {
                    if (tmpMapTile.x == x && tmpMapTile.y == y && !tmpMapTile.getClass().getSimpleName().equals("Army")) {
                        selectedTileToBuy = tmpMapTile;
                        isTileToBuySelected = true;
                        hud.selectedToBuyTileCost.setText("Field cost: " + calculateTileCost(selectedTileToBuy, 10) + "G");
                        hud.isBuyTileMenu = false;
                    }
                }
            }
        }
    }

    public void buyTile() {
        if (playerWhoseTurnIs.getAmountOfGold() >= calculateTileCost(selectedTileToBuy, 10) && selectedTileToBuy.getOwner().equals("none")) {
            playerWhoseTurnIs.setAmountOfGold(playerWhoseTurnIs.getAmountOfGold() - calculateTileCost(selectedTileToBuy, 10));
            playerWhoseTurnIs.addTileToPlayer(selectedTileToBuy);
            ((TownTile) selectedTile).getTilesNearTown().add(selectedTileToBuy);
            selectedTileToBuy.setOwner(playerWhoseTurnIs.getPlayerName());
            selectedTileToBuy = null;
            isTileToBuySelected = false;
        }
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

    public void buildResourceTile() {
        if (((ResourcesTile) selectedTile).isBuilt()) {
            return;
        }
        boolean canBuild = false;
        for (MapTile mapTile : playerWhoseTurnIs.getTilesOwned()) {
            if (mapTile.x == selectedTile.x && mapTile.y == selectedTile.y && !mapTile.getClass().getSimpleName().equals("Army")) {
                canBuild = true;
            }
        }
        if (canBuild) {
            if (playerWhoseTurnIs.payForResourceTile()) {
                ((ResourcesTile) selectedTile).build();
            }
        }
    }

    public void splitArmy(Army armyToSplit, int archersAmount, int footmansAmount, int cavalryAmount){
        if(armyToSplit.getArchersAmount()> archersAmount && armyToSplit.getFootmansAmount()>footmansAmount && armyToSplit.getCavalryAmount()>cavalryAmount){
            Army armyCreated = new Army(armyToSplit.x, armyToSplit.y-1, armyToSplit.getOwner(), archersAmount, footmansAmount, cavalryAmount);
            armyToSplit.removeUnitsFromArmy(archersAmount, footmansAmount, cavalryAmount);
            worldMap.getMapOfWorld().add(armyCreated);
            playerWhoseTurnIs.getArmyOwned().add(armyCreated);
        }
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
                cameraController.setPosition(mapTile.x, mapTile.y);
            }
        }
    }

    private void selectionReset() {
        selectedTile = null;
        isTileSelected = false;
        selectedTileIndex = 0;
        selectedTileToBuy = null;
        isTileToBuySelected = false;
        hud.selectionHudReset();
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
            selectionReset();
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
        if (button == Input.Buttons.LEFT && hud.isBuyTileMode) {
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

    public void setTileToBuySelected(boolean tileToBuySelected) {
        isTileToBuySelected = tileToBuySelected;
    }

    public void setSelectedTileToBuy(MapTile selectedTileToBuy) {
        this.selectedTileToBuy = selectedTileToBuy;
    }

    public int getIndexOfPlayerWhoseTurnIs() {
        return indexOfPlayerWhoseTurnIs;
    }


}
