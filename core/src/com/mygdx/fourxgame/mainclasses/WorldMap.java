package com.mygdx.fourxgame.mainclasses;

import com.mygdx.fourxgame.maptiles.*;

import java.util.ArrayList;
import java.util.Random;
import java.lang.Math;

public class WorldMap {
    private ArrayList<MapTile> mapOfWorld;
    private int numberOfPlayers;
    private ArrayList<Player> players;

    public WorldMap(int numberOfPlayers, ArrayList<Player> players) {
        this.numberOfPlayers = numberOfPlayers;

        mapOfWorld = new ArrayList<>();
        this.players = players;
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
        //generatePlayers(3);
        generateMap(numberOfPlayers);
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

//        int radius = 3;
//        ArrayList<MapTile> tilesWithTowns = new ArrayList<>();
//        ArrayList<MapTile> generatorSeeds = new ArrayList<>();
//        for (MapTile mapTile : mapOfWorld) {
//            if (mapTile.getClass().getSimpleName().equals("TownTile")) {
//                tilesWithTowns.add(mapTile);
//            }
//        }
//        for (MapTile mapTile : tilesWithTowns) {
//            for (int i = radius * (-5); i <= radius * 5; i += 5) {
//                for (int j = radius * (-5); j <= radius * 5; j += 5) {
//                    if (i != 0 || j != 0) {
//                        generatorSeeds.add(new EmptyTile(j + mapTile.x, i + mapTile.y, "none"));
//                    }
//                }
//            }
//        }
//        LinkedHashSet<MapTile> noDuplicates = new LinkedHashSet<>();
//        noDuplicates.addAll(generatorSeeds);
//        generatorSeeds.clear();
//        generatorSeeds.addAll(noDuplicates);
//        for (int i = 0; i < generatorSeeds.size(); i++) {
//            for (int j = i + 1; j < generatorSeeds.size(); j++) {
//                if (generatorSeeds.get(i).x == generatorSeeds.get(j).x && generatorSeeds.get(i).y == generatorSeeds.get(j).y) {
//                    generatorSeeds.remove(j);
//                }
//            }
//        }
//
//        for (MapTile mapTile : generatorSeeds) {
//            for (MapTile mapTileTowns : tilesWithTowns) {
//                if ((mapTile.x != mapTileTowns.x) || (mapTile.y != mapTileTowns.y)) {
//                    generateStandardChunk("none", mapTile.x, mapTile.y);
//                }
//            }
//        }
    }

    private void generatePlayers(int numberOfPlayers) {
        ArrayList<MapTile> checkedTiles = new ArrayList<>();
        MapTile tmpTile;

        Random random = new Random();
        int high = 25;
        int low = -25;
        int playerX = random.nextInt(high + 1 - low) + low;
        int playerY = random.nextInt(high + 1 - low) + low;
        playerX=0;
        playerY=0;
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
                tmpTile = new TownTile(playerX, playerY, players.get(i + 1).playerName);
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
        ((TownTile)tmpChunk.get(0)).getTilesNearTown().addAll(tmpChunk);
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
                    tmpMapTile = new GoldTile(tmpX, tmpY, playerName);
                } else if (type == 85 || type == 1) {
                    tmpMapTile = new IronTile(tmpX, tmpY, playerName);
                } else if (type >= 11 && type <= 30) {
                    tmpMapTile = new WoodTile(tmpX, tmpY, playerName);
                } else {
                    tmpMapTile = new EmptyTile(tmpX, tmpY, playerName);
                }
                tmpChunk.add(tmpMapTile);
            }
        }
        mapOfWorld.addAll(tmpChunk);
    }

    public void expandMapHorizontally(String playerName, int startingX, int endingX, int yLevel){
        for(int i=startingX; i<=endingX; i+=5){
            generateStandardChunk(playerName, i, yLevel);
        }
    }

    public void expandMapSimpleVertically(String playerName, int xLevel, int startingY, int endingY){
        for(int i=startingY; i<=endingY; i+=5){
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
                if (checkExistanceOfChunk(emptyX + j*5, emptyY + i*5)) {
                    continue;
                }
                generateStandardChunk(playerName, emptyX + j*5, emptyY + i*5);
            }
        }
    }

    private void generationToExpandWithException(String playerName, int emptyX, int emptyY, int exX, int exY) {
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (checkExistanceOfChunk(emptyX + j*5, emptyY + i*5)) {
                    continue;
                }

                if (i == exY && j == exX) {
                    continue;
                } else {
                    generateStandardChunk(playerName, emptyX + j*5, emptyY + i*5);
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

    public int calculateDistance(int firstTileX, int firstTileY, int secondTileX, int secondTileY) {
        return Math.max(Math.abs(secondTileX - firstTileX), Math.abs(secondTileY - firstTileY));
    }

    public void addToMap(MapTile mapTile) {
        mapOfWorld.add(mapTile);
    }

    public ArrayList<MapTile> getMapOfWorld() {
        return mapOfWorld;
    }
}
