package com.mygdx.fourxgame.controllers;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mygdx.fourxgame.mainclasses.Player;
import com.mygdx.fourxgame.maptiles.*;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;

//Klasa odpowiadająca za zapisywanie i wczytywanie stanu gry
public class SaveManager {

    private GameSession gameSession;

    public SaveManager(GameSession gameSession) {
        this.gameSession = gameSession;
    }

    public void saveGame(String savePath) {
        saveGameState(savePath);
        saveTurnNumber(savePath);
        savePlayers(savePath);
        saveArmyTiles(savePath);
        saveTownTiles(savePath);
        saveEmptyTiles(savePath);
        saveGoldTiles(savePath);
        saveIronTiles(savePath);
        saveWoodTiles(savePath);
    }

    public ArrayList<Army> loadArmyTiles(String savePath) {
        Reader reader = null;
        Gson gson = new Gson();

        try {
            reader = new BufferedReader(new FileReader(savePath + "armyTilesSave.json"));
            Type armyTilesArrayListType = new TypeToken<ArrayList<Army>>() {
            }.getType();
            return gson.fromJson(reader, armyTilesArrayListType);

        } catch (IOException e) {

        }
        return null;
    }

    public ArrayList<Player> loadPlayers(String savePath) {
        Reader reader = null;
        Gson gson = new Gson();

        try {
            reader = new BufferedReader(new FileReader(savePath + "playersSave.json"));
            Type playersArrayListType = new TypeToken<ArrayList<Player>>() {
            }.getType();
            return gson.fromJson(reader, playersArrayListType);

        } catch (IOException e) {

        }
        return null;
    }

    public ArrayList<TownTile> loadTownTiles(String savePath) {
        Reader reader = null;
        Gson gson = new Gson();

        try {
            reader = new BufferedReader(new FileReader(savePath + "townTilesSave.json"));
            Type townsArrayListType = new TypeToken<ArrayList<TownTile>>() {
            }.getType();
            return gson.fromJson(reader, townsArrayListType);

        } catch (IOException e) {

        }
        return null;
    }

    public ArrayList<EmptyTile> loadEmptyTiles(String savePath) {
        Reader reader = null;
        Gson gson = new Gson();

        try {
            reader = new BufferedReader(new FileReader(savePath + "emptyTilesSave.json"));
            Type emptyTilesArrayListType = new TypeToken<ArrayList<EmptyTile>>() {
            }.getType();
            return gson.fromJson(reader, emptyTilesArrayListType);

        } catch (IOException e) {

        }
        return null;
    }

    public ArrayList<WoodTile> loadWoodTiles(String savePath) {
        Reader reader = null;
        Gson gson = new Gson();

        try {
            reader = new BufferedReader(new FileReader(savePath + "woodTilesSave.json"));
            Type woodTilesArrayListType = new TypeToken<ArrayList<WoodTile>>() {
            }.getType();
            return gson.fromJson(reader, woodTilesArrayListType);

        } catch (IOException e) {

        }
        return null;
    }

    public ArrayList<IronTile> loadIronTiles(String savePath) {
        Reader reader = null;
        Gson gson = new Gson();

        try {
            reader = new BufferedReader(new FileReader(savePath + "ironTilesSave.json"));
            Type ironTilesArrayListType = new TypeToken<ArrayList<IronTile>>() {
            }.getType();
            return gson.fromJson(reader, ironTilesArrayListType);

        } catch (IOException e) {

        }
        return null;
    }

    public ArrayList<GoldTile> loadGoldTiles(String savePath) {
        Reader reader = null;
        Gson gson = new Gson();

        try {
            reader = new BufferedReader(new FileReader(savePath + "goldTilesSave.json"));
            Type goldTilesArrayListType = new TypeToken<ArrayList<GoldTile>>() {
            }.getType();
            return gson.fromJson(reader, goldTilesArrayListType);

        } catch (IOException e) {

        }
        return null;
    }

    public int loadGameState(String savePath) {
        Reader reader = null;
        Gson gson = new Gson();

        try {
            reader = new BufferedReader(new FileReader(savePath + "gameState.json"));
            return gson.fromJson(reader, int.class);

        } catch (IOException e) {

        }
        return -1;
    }

    public int loadTurnNumber(String savePath){
        Reader reader = null;
        Gson gson = new Gson();

        try {
            reader = new BufferedReader(new FileReader(savePath + "turnNumber.json"));
            return gson.fromJson(reader, int.class);

        } catch (IOException e) {

        }
        return -1;
    }

    private boolean saveGameState(String savePath) {
        Writer writer = null;
        Gson gson = new Gson();
        try {
            writer = new FileWriter(savePath + "gameState.json");
            gson.toJson(gameSession.getIndexOfPlayerWhoseTurnIs(), writer);
            writer.flush();
            writer.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean saveTurnNumber(String savePath) {
        Writer writer = null;
        Gson gson = new Gson();
        try{
            writer = new FileWriter(savePath+"turnNumber.json");
            gson.toJson(gameSession.turnNumber, writer);
            writer.flush();
            writer.close();
        }catch (IOException e){
            e.printStackTrace();
        }
        return  false;
    }

    private boolean savePlayers(String savePath) {
        Writer writer = null;
        Gson gson = new Gson();
        try {
            writer = new FileWriter(savePath + "playersSave.json");
            gson.toJson(gameSession.getPlayers(), writer);
            writer.flush();
            writer.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean saveArmyTiles(String savePath) {
        Writer writer = null;
        Gson gson = new Gson();
        try {
            writer = new FileWriter(savePath + "armyTilesSave.json");
            gson.toJson(getArrayOfType("Army"), writer);
            writer.flush();
            writer.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean saveTownTiles(String savePath) {
        Writer writer = null;
        Gson gson = new Gson();
        try {
            writer = new FileWriter(savePath + "townTilesSave.json");
            gson.toJson(getArrayOfType("TownTile"), writer);
            writer.flush();
            writer.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean saveEmptyTiles(String savePath) {
        Writer writer = null;
        Gson gson = new Gson();
        try {
            writer = new FileWriter(savePath + "emptyTilesSave.json");
            gson.toJson(getArrayOfType("EmptyTile"), writer);
            writer.flush();
            writer.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean saveWoodTiles(String savePath) {
        Writer writer = null;
        Gson gson = new Gson();
        try {
            writer = new FileWriter(savePath + "woodTilesSave.json");
            gson.toJson(getArrayOfType("WoodTile"), writer);
            writer.flush();
            writer.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean saveIronTiles(String savePath) {
        Writer writer = null;
        Gson gson = new Gson();
        try {
            writer = new FileWriter(savePath + "ironTilesSave.json");
            gson.toJson(getArrayOfType("IronTile"), writer);
            writer.flush();
            writer.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean saveGoldTiles(String savePath) {
        Writer writer = null;
        Gson gson = new Gson();
        try {
            writer = new FileWriter(savePath + "goldTilesSave.json");
            gson.toJson(getArrayOfType("GoldTile"), writer);
            writer.flush();
            writer.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private ArrayList<MapTile> getArrayOfType(String classType) {
        ArrayList<MapTile> arrayListOfType = new ArrayList<>();
        for (MapTile tmpTile : gameSession.getMap()) {
            if (tmpTile.getClass().getSimpleName().equals(classType)) {
                arrayListOfType.add(tmpTile);
            }
        }
        return arrayListOfType;
    }
}
