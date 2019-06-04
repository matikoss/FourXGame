package com.mygdx.fourxgame.controllers;

import com.badlogic.gdx.utils.viewport.Viewport;

import java.io.File;

public class GameController {

    private GameSession gameSession;
    private boolean inMainMenu;
    private boolean inGame;
    public boolean backToMainMenu = false;

    public GameController() {
        inMainMenu = true;
        inGame = false;
        init();
    }

    public void init() {

    }

    public void startNewGame(int numberOfPlayers) {
        initializeGameSession(numberOfPlayers, 1, null);
    }

    public void loadGame(int numberOfPlayers, String savePath) {
        initializeGameSession(numberOfPlayers, 2, savePath);
    }

    public void update(float deltaTime) {
        if (inGame && !inMainMenu) {
            gameSession.update(deltaTime);
        }
        if (gameSession != null && !gameSession.inGame && !inMainMenu) {
            inMainMenu = true;
            inGame = false;
            backToMainMenu = true;
        }
    }

    private void initializeGameSession(int numberOfPlayers, int startMode, String savePath) {
        gameSession = new GameSession(numberOfPlayers, startMode, savePath);
        inMainMenu = false;
        inGame = true;
    }

    public boolean checkIfSaveFilesExists(String directoryPath) {
        if (!checkIfFileExists(directoryPath + "armyTilesSave.json")) {
            return false;
        }
        if (!checkIfFileExists(directoryPath + "emptyTilesSave.json")) {
            return false;
        }
        if (!checkIfFileExists(directoryPath + "gameState.json")) {
            return false;
        }
        if (!checkIfFileExists(directoryPath + "goldTilesSave.json")) {
            return false;
        }
        if (!checkIfFileExists(directoryPath + "ironTilesSave.json")) {
            return false;
        }
        if (!checkIfFileExists(directoryPath + "woodTilesSave.json")) {
            return false;
        }
        if (!checkIfFileExists(directoryPath + "playersSave.json")) {
            return false;
        }
        if (!checkIfFileExists(directoryPath + "townTilesSave.json")) {
            return false;
        }
        return true;

    }

    public boolean checkIfFileExists(String filePath) {
        File f = new File(filePath);
        if (f.exists() && !f.isDirectory()) {
            return true;
        }
        return false;
    }

    public boolean isInMainMenu() {
        return inMainMenu;
    }

    public boolean isInGame() {
        return inGame;
    }

    public GameSession getGameSession() {
        return gameSession;
    }

    private boolean checkIfGameSessionInGame() {
        return gameSession.inGame;
    }

}

