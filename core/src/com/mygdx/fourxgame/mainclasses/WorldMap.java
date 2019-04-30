package com.mygdx.fourxgame.mainclasses;

import com.mygdx.fourxgame.maptiles.*;

import java.util.ArrayList;
import java.util.Random;
import java.lang.Math;

public class WorldMap {
    private ArrayList<MapTile> mapOfWorld;

    public WorldMap() {
        mapOfWorld = new ArrayList<>();
//        generatePlayerChunk("Mati", 0, 0);
//        generatePlayerChunk("Mati", 5, 0);
//        generatePlayerChunk("Mati", 10, 0);
//        generatePlayerChunk("Mati", 0, 5);
//        generatePlayerChunk("Mati", 0, 10);
//        generatePlayerChunk("Mati", 0, 15);
//        generatePlayerChunk("Mati", -5, 0);
//        generatePlayerChunk("Mati", -10, 5);
//        generatePlayerChunk("Mati", -15, 10);
//        generatePlayerChunk("Mati", 5, 5);
//        generatePlayerChunk("Mati", 10, 10);
//        generateStandardChunk("Mati", 15, 15);
        generatePlayers(10);
    }

    public void update(float deltaTime) {

    }

    private void generatePlayers(int numberOfPlayers) {

        Random random = new Random();
        int high = 25;
        int low = -25;
        int playerX = random.nextInt(high + 1 - low) + low;
        int playerY = random.nextInt(high + 1 - low) + low;
        int tryCounter = 0;
        int lastTryIndex = 1;
        System.out.println(playerX + " " + playerY);
        generatePlayerChunk("Player", playerX, playerY);
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
                    playerX++;
                } else if (changeX) {
                    playerX--;
                } else if (increaseY) {
                    playerY++;
                } else {
                    playerY--;
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
                changeOldPlayerXY = false;
                int last = mapOfWorld.size() - lastTryIndex;
                if (last < 0) {
                    lastTryIndex = 1;
                }
                if (!fromStart) {
                    oldPlayerX = mapOfWorld.get(last).x;
                    oldPlayerY = mapOfWorld.get(last).y;
                    tryCounter = 0;
                    fromStart = true;
                } else {
                    oldPlayerX = mapOfWorld.get(lastTryIndex).x;
                    oldPlayerY = mapOfWorld.get(lastTryIndex).y;
                    tryCounter = 0;
                    fromStart = false;
                    lastTryIndex++;
                }
            }
            if (!duplicate) {
                System.out.println(playerX + " " + playerY);
                generatePlayerChunk("Player", playerX, playerY);
                changeOldPlayerXY = random.nextBoolean();
                tryCounter = 0;
                lastTryIndex = 1;
            }
        }
    }

    private void generatePlayerChunk(String playerName, int startingX, int startingY) {
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
                tmpMapTile = new TownTile(startingX, startingY, playerName);
            } else if (tmpChunk.size() == 1) {
                tmpMapTile = new IronTile(tmpX, tmpY, playerName);
            } else if (tmpChunk.size() == 2) {
                tmpMapTile = new GoldTile(tmpX, tmpY, playerName);
            } else if (tmpChunk.size() == 3) {
                tmpMapTile = new WoodTile(tmpX, tmpY, playerName);
            } else if (tmpChunk.size() == 4) {
                tmpMapTile = new WoodTile(tmpX, tmpY, playerName);
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
        mapOfWorld.addAll(tmpChunk);
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
                int type = random.nextInt(10) + 1;
                System.out.println(type);
                if (type == 1) {
                    tmpMapTile = new GoldTile(tmpX, tmpY, playerName);
                } else if (type == 2) {
                    tmpMapTile = new IronTile(tmpX, tmpY, playerName);
                } else if (type >= 3 && type <= 5) {
                    tmpMapTile = new WoodTile(tmpX, tmpY, playerName);
                } else {
                    tmpMapTile = new EmptyTile(tmpX, tmpY, playerName);
                }
                tmpChunk.add(tmpMapTile);
            }
        }
        mapOfWorld.addAll(tmpChunk);
    }

    private int calculateDistance(int firstTileX, int firstTileY, int secondTileX, int secondTileY) {
        return Math.max(Math.abs(secondTileX - firstTileX), Math.abs(secondTileY - firstTileY));
    }

    public void addToMap(MapTile mapTile) {
        mapOfWorld.add(mapTile);
    }

    public ArrayList<MapTile> getMapOfWorld() {
        return mapOfWorld;
    }
}
