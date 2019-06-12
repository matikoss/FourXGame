package com.mygdx.fourxgame.mainclasses;

import com.mygdx.fourxgame.maptiles.*;

import java.util.ArrayList;
import java.util.Random;
import java.lang.Math;

public class WorldMap {
    private ArrayList<MapTile> mapOfWorld;
    private int numberOfPlayers;
    private ArrayList<Player> players;

    private ArrayList<Player> playersWaitingToBeAdded;

    public WorldMap(int numberOfPlayers, ArrayList<Player> players, int gameMode) {
        this.numberOfPlayers = numberOfPlayers;

        mapOfWorld = new ArrayList<>();
        this.players = players;
        playersWaitingToBeAdded = new ArrayList<>();
        if (gameMode == 1) {
            startNewGame();
        }
    }

    private void startNewGame() {
        generateMap(numberOfPlayers);
    }

    private void loadGame() {

    }

    public void update(float deltaTime) {

    }

    private void generateMap(int numberOfPlayers) {
        generatePlayers(numberOfPlayers);
        fillMap();

    }

    private void fillMap() {
        ArrayList<MapTile> tilesWithTowns = new ArrayList<>();
        for (MapTile mapTile : mapOfWorld) {
            if (mapTile.getClass().getSimpleName().equals("TownTile")) {
                tilesWithTowns.add(mapTile);
            }
        }
        int maxX, maxY, minX, minY;
        maxX = tilesWithTowns.get(0).x;
        maxY = tilesWithTowns.get(0).y;
        minX = tilesWithTowns.get(0).x;
        minY = tilesWithTowns.get(0).y;

        for (MapTile mapTile : tilesWithTowns) {
            if (mapTile.x > maxX) {
                maxX = mapTile.x;
            }
            if (mapTile.y > maxY) {
                maxY = mapTile.y;
            }
            if (mapTile.x < minX) {
                minX = mapTile.x;
            }
            if (mapTile.y < minY) {
                minY = mapTile.y;
            }
        }
        boolean isEqual;
        for (int y = minY - 15; y <= maxY + 15; y += 5) {
            for (int x = minX - 15; x <= maxX + 15; x += 5) {
                isEqual = false;
                for (MapTile mapTile : tilesWithTowns) {
                    if (x == mapTile.x && y == mapTile.y) {
                        isEqual = true;
                        break;
                    }
                }
                if (!isEqual) {
                    generateStandardChunk("none", x, y);
                }
            }
        }
    }

    private void generatePlayers(int numberOfPlayers) {
        ArrayList<MapTile> checkedTiles = new ArrayList<>();
        MapTile tmpTile;

        Random random = new Random();
        int high = 25;
        int low = -25;
        int playerX = random.nextInt(high + 1 - low) + low;
        int playerY = random.nextInt(high + 1 - low) + low;
        playerX = 0;
        playerY = 0;
        int tryCounter = 0;
        int lastTryIndex = 1;
        int last = checkedTiles.size() - lastTryIndex;
        System.out.println(playerX + " " + playerY);
        generatePlayerChunk(players.get(0).playerName, playerX, playerY, players.get(0));
        int oldPlayerX = playerX;
        int oldPlayerY = playerY;
        boolean changeOldPlayerXY = false;
        boolean fromStart = false;

        for (int i = 0; i < numberOfPlayers - 1; i++) {
            boolean increaseX = random.nextBoolean();
            boolean increaseY = random.nextBoolean();

            if (changeOldPlayerXY) {
                oldPlayerX = playerX;
                oldPlayerY = playerY;
            }

            do {
                boolean changeX = random.nextBoolean();
                if (changeX && increaseX) {
                    //playerX++;
                    playerX += 5;
                } else if (changeX) {
                    //playerX--;
                    playerX -= 5;
                } else if (increaseY) {
                    //playerY++;
                    playerY += 5;
                } else {
                    //playerY--;
                    playerY -= 5;
                }
            } while (calculateDistance(oldPlayerX, oldPlayerY, playerX, playerY) < 15);
            System.out.println(calculateDistance(oldPlayerX, oldPlayerY, playerX, playerY));

            boolean duplicate = false;
            for (MapTile mapTile : mapOfWorld) {
                if ((mapTile.getClass().getSimpleName().equals("TownTile") && mapTile.x == playerX && mapTile.y == playerY) || (mapTile.getClass().getSimpleName().equals("TownTile") && calculateDistance(mapTile.x, mapTile.y, playerX, playerY) < 15)) {
                    playerX = oldPlayerX;
                    playerY = oldPlayerY;
                    duplicate = true;
                    i--;
                    tryCounter++;
                    break;
                }
            }
            if (tryCounter >= 50) {
                if (last >= 0) {
                    checkedTiles.remove(last);
                }
                changeOldPlayerXY = false;
                last = checkedTiles.size() - lastTryIndex;
                //int last = mapOfWorld.size() - lastTryIndex;
                if (last < 0) {
                    lastTryIndex = 1;
                }
                if (!fromStart) {
                    oldPlayerX = checkedTiles.get(last).x;
                    oldPlayerY = checkedTiles.get(last).y;
                    tryCounter = 0;
                    fromStart = true;
                } else {
                    oldPlayerX = checkedTiles.get(lastTryIndex).x;
                    oldPlayerY = checkedTiles.get(lastTryIndex).y;
                    tryCounter = 0;
                    fromStart = false;
                    lastTryIndex++;
                }
            }
            if (!duplicate) {
                System.out.println(playerX + " " + playerY);
                tmpTile = new TownTile(playerX, playerY, players.get(i + 1).playerName, GameplayConstants.timeToLoseTown);
                checkedTiles.add(tmpTile);
                generatePlayerChunk(players.get(i + 1).playerName, playerX, playerY, players.get(i + 1));
                changeOldPlayerXY = random.nextBoolean();
                tryCounter = 0;
                lastTryIndex = 1;
            }
        }
    }

    private void generatePlayerChunk(String playerName, int startingX, int startingY, Player ownerOfStartingChunk) {
        ArrayList<MapTile> tmpChunk = new ArrayList<>();
        MapTile tmpMapTile;
        Random random = new Random();
        int low = -2;
        int high = 2;
        int tmpX, tmpY;
        int counter;

        while (tmpChunk.size() < 25) {
            tmpX = random.nextInt(high + 1 - low) + low + startingX;
            tmpY = random.nextInt(high + 1 - low) + low + startingY;


            if (tmpChunk.isEmpty()) {
                //tmpMapTile = new TownTile(tmpX, tmpY, playerName);
                tmpMapTile = new TownTile(startingX, startingY, playerName, GameplayConstants.timeToLoseTown);
            } else if (tmpChunk.size() == 1) {
                tmpMapTile = new IronTile(tmpX, tmpY, playerName, false);
            } else if (tmpChunk.size() == 2) {
                tmpMapTile = new GoldTile(tmpX, tmpY, playerName, false);
            } else if (tmpChunk.size() == 3) {
                tmpMapTile = new WoodTile(tmpX, tmpY, playerName, false);
            } else if (tmpChunk.size() == 4) {
                tmpMapTile = new WoodTile(tmpX, tmpY, playerName, false);
            } else {
                tmpMapTile = new EmptyTile(tmpX, tmpY, playerName);
            }
            counter = 0;
            for (MapTile tile : tmpChunk) {
                if (tmpX != tile.x || tmpY != tile.y) {
                    counter++;
                }
            }
            if (counter == tmpChunk.size()) {
                tmpChunk.add(tmpMapTile);
            }
        }
        ((TownTile) tmpChunk.get(0)).getTilesNearTown().addAll(tmpChunk);
        mapOfWorld.addAll(tmpChunk);
        ownerOfStartingChunk.addMultipleTilesToPlayer(tmpChunk);
    }

    private void generateStandardChunk(String playerName, int xPosition, int yPosition) {
        int rows = 5;
        int columns = 5;
        ArrayList<MapTile> tmpChunk = new ArrayList<>();
        MapTile tmpMapTile;
        int tmpX, tmpY;
        Random random = new Random();
        for (int i = 0; i < rows; i++) {
            tmpY = yPosition - 2 + i;
            for (int j = 0; j < columns; j++) {
                tmpX = xPosition - 2 + j;
                int type = random.nextInt(100) + 1;
                System.out.println(type);
                if (type == 5 || type == 56) {
                    tmpMapTile = new GoldTile(tmpX, tmpY, playerName, false);
                } else if (type == 85 || type == 1) {
                    tmpMapTile = new IronTile(tmpX, tmpY, playerName, false);
                } else if (type >= 11 && type <= 30) {
                    tmpMapTile = new WoodTile(tmpX, tmpY, playerName, false);
                } else {
                    tmpMapTile = new EmptyTile(tmpX, tmpY, playerName);
                }
                tmpChunk.add(tmpMapTile);
            }
        }
        mapOfWorld.addAll(tmpChunk);
    }

    public void expandMapHorizontally(String playerName, int startingX, int endingX, int yLevel) {
        for (int i = startingX; i <= endingX; i += 5) {
            generateStandardChunk(playerName, i, yLevel);
        }
    }

    public void expandMapSimpleVertically(String playerName, int xLevel, int startingY, int endingY) {
        for (int i = startingY; i <= endingY; i += 5) {
            generateStandardChunk(playerName, xLevel, i);
        }
    }

    public void expandMap(String playerName, int startingX, int startingY, int emptyDirection) {
        if (emptyDirection == GameplayConstants.north) {
            generationToExpand(playerName, startingX, startingY, -1, 1, 0, 1);
        } else if (emptyDirection == GameplayConstants.north_east) {
            generationToExpandWithException(playerName, startingX, startingY, -1, -1);
        } else if (emptyDirection == GameplayConstants.east) {
            generationToExpand(playerName, startingX, startingY, 0, 1, -1, 1);
        } else if (emptyDirection == GameplayConstants.south_east) {
            generationToExpandWithException(playerName, startingX, startingY, -1, 1);
        } else if (emptyDirection == GameplayConstants.south) {
            generationToExpand(playerName, startingX, startingY, -1, 1, -1, 0);
        } else if (emptyDirection == GameplayConstants.south_west) {
            generationToExpandWithException(playerName, startingX, startingY, 1, 1);
        } else if (emptyDirection == GameplayConstants.west) {
            generationToExpand(playerName, startingX, startingY, -1, 0, -1, 1);
        } else if (emptyDirection == GameplayConstants.north_west) {
            generationToExpandWithException(playerName, startingX, startingY, 1, -1);
        }
    }

    private void generationToExpand(String playerName, int emptyX, int emptyY, int startX, int endX, int startY, int endY) {
        for (int i = startY; i <= endY; i++) {
            for (int j = startX; j <= endX; j++) {
                if (checkExistanceOfChunk(emptyX + j * 5, emptyY + i * 5)) {
                    continue;
                }
                generateStandardChunk(playerName, emptyX + j * 5, emptyY + i * 5);
            }
        }
    }

    private void generationToExpandWithException(String playerName, int emptyX, int emptyY, int exX, int exY) {
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (checkExistanceOfChunk(emptyX + j * 5, emptyY + i * 5)) {
                    continue;
                }

                if (i == exY && j == exX) {
                    continue;
                } else {
                    generateStandardChunk(playerName, emptyX + j * 5, emptyY + i * 5);
                }
            }
        }
    }

    private boolean checkExistanceOfChunk(int x, int y) {
        for (MapTile mapTile : mapOfWorld) {
            if (x == mapTile.x && y == mapTile.y) {
                return true;
            }
        }
        return false;
    }

    public void addWaitingPlayers(int turnNumber) {
        while (!playersWaitingToBeAdded.isEmpty()) {
            createNewPlayer(turnNumber);
        }
    }

    private void createNewPlayer(int turnNumber) {
        int distance = 15;
        if (turnNumber <= 15) {
            distance = (turnNumber / 5) * 5 + 15;
        } else {
            distance = 30;
        }
        boolean succeeded = false;
        for (MapTile mapTile : mapOfWorld) {
            if (mapTile.getClass().getSimpleName().equals("TownTile")) {
                succeeded = tryToPlaceNewPlayer((TownTile) mapTile, distance);
                if (succeeded) {
                    break;
                }
            }
        }
    }

    private boolean tryToPlaceNewPlayer(TownTile townTileChecked, int distance) {
        Random random = new Random();

        int direction = random.nextInt(4) + 1;

        int maxX = townTileChecked.x, minX = townTileChecked.x, maxY = townTileChecked.y, minY = townTileChecked.y;
        int westDistance = distance, eastDistance = distance, northDistance = distance, southDistance = distance;

        for (MapTile mapTile : townTileChecked.getTilesNearTown()) {
            if (maxX < mapTile.x) {
                maxX = mapTile.x;
            }
            if (minX > mapTile.x) {
                minX = mapTile.x;
            }
            if (maxY < mapTile.y) {
                maxY = mapTile.y;
            }
            if (minY > mapTile.y) {
                minY = mapTile.y;
            }
        }

        westDistance = -(distance + (calculateDistance(townTileChecked.x, townTileChecked.y, minX, townTileChecked.y) / 5) * 5);
        eastDistance = (distance + (calculateDistance(townTileChecked.x, townTileChecked.y, maxX, townTileChecked.y) / 5) * 5);
        northDistance = (distance + (calculateDistance(townTileChecked.x, townTileChecked.y, townTileChecked.x, maxY) / 5) * 5);
        southDistance = -(distance + (calculateDistance(townTileChecked.x, townTileChecked.y, townTileChecked.x, minY) / 5) * 5);

        int newTownX;
        int newTownY;
        boolean canBePlaced;

        for (int i = 0; i < 4; i++) {
            if (direction + i % 4 == 0) {
                newTownX = townTileChecked.x;
                newTownY = townTileChecked.y + northDistance;
            } else if (direction + i % 4 == 1) {
                newTownX = townTileChecked.x + eastDistance;
                newTownY = townTileChecked.y;
            } else if (direction + i % 4 == 2) {
                newTownX = townTileChecked.x;
                newTownY = townTileChecked.y + southDistance;
            } else {
                newTownX = townTileChecked.x + westDistance;
                newTownY = townTileChecked.y;
            }

            canBePlaced = true;

            for (Player playerWhoseTilesAreChecked : players) {
                for (MapTile tileChecked : playerWhoseTilesAreChecked.getTilesOwned()) {
                    if (calculateDistance(newTownX, newTownY, tileChecked.x, tileChecked.y) < distance - 5) {
                        canBePlaced = false;
                        break;
                    }
                }
                if (!canBePlaced) {
                    break;
                }
            }

            if (canBePlaced) {
                placeNewPlayer(newTownX, newTownY);
                return true;
            }
        }

        return false;
    }

    private boolean placeNewPlayer(int newTownX, int newTownY) {
        ArrayList<MapTile> tilesToRemove = new ArrayList<>();
        for (int i = -2; i <= 2; i++) {
            for (int j = -2; j <= 2; j++) {
                for (MapTile tileToCheck : mapOfWorld) {
                    if ((tileToCheck.x == newTownX + j) && (tileToCheck.y == newTownY + i) && !tileToCheck.getClass().getSimpleName().equals("Army")) {
                        tilesToRemove.add(tileToCheck);
                    }
                }
            }
        }
        mapOfWorld.removeAll(tilesToRemove);

        Player newPlayer = playersWaitingToBeAdded.get(0);
        players.add(newPlayer);
        generatePlayerChunk(newPlayer.getPlayerName(), newTownX, newTownY, newPlayer);
        generateMapAroundTheAddedPlayer(newTownX, newTownY);
        playersWaitingToBeAdded.remove(newPlayer);

        return true;
    }

    private boolean generateMapAroundTheAddedPlayer(int newTownX, int newTownY){
        boolean tileExists;
        for(int i=-15; i<=15; i+=5){
            for(int j=-15; j<=15; j+=5){
                tileExists = false;
                for(MapTile mapTileToCheck : mapOfWorld){
                    if((newTownX + j == mapTileToCheck.x) && (newTownY + i == mapTileToCheck.y)){
                        tileExists = true;
                        break;
                    }
                }
                if(!tileExists){
                    generateStandardChunk("none", newTownX + j, newTownY + i);
                }
            }
        }
        return true;
    }

    private Player generateNewPlayer(String playerName, int amountOfWood, int amountOfIron, int amountOfGold, int population) {
        return new Player(playerName, amountOfWood, amountOfIron, amountOfGold, population);
    }

    public void addNewPlayerToWaitingList(String newPlayerName) {
        playersWaitingToBeAdded.add(generateNewPlayer(newPlayerName, 100, 100, 100, 50));
    }

    public int calculateDistance(int firstTileX, int firstTileY, int secondTileX, int secondTileY) {
        return Math.max(Math.abs(secondTileX - firstTileX), Math.abs(secondTileY - firstTileY));
    }

    public void addToMap(MapTile mapTile) {
        mapOfWorld.add(mapTile);
    }

    public ArrayList<MapTile> getMapOfWorld() {
        return mapOfWorld;
    }

    public void addManyToMap(ArrayList<? extends MapTile> tmpMap) {
        mapOfWorld.addAll(tmpMap);
    }

    public ArrayList<Player> getPlayersWaitingToBeAdded() {
        return playersWaitingToBeAdded;
    }
}
