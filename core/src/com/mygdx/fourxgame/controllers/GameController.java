package com.mygdx.fourxgame.controllers;

public class GameController {

    private GameSession gameSession;
    private boolean inMainMenu;
    private boolean inGame;

    public GameController() {
        inMainMenu = true;
        inGame = false;
        init();
    }

    public void init() {

    }

    public void startNewGame(int numberOfPlayers){
        initializeGameSession(numberOfPlayers);
    }

    public void update(float deltaTime) {
        if(inGame && !inMainMenu){
            gameSession.update(deltaTime);
        }
    }

    private void initializeGameSession(int numberOfPlayers) {
        gameSession = new GameSession(numberOfPlayers);
        inMainMenu = false;
        inGame = true;
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
}

