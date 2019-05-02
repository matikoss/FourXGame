package com.mygdx.fourxgame.controllers;

public class GameController {
    private GameSession gameSession;

    public GameController() {
        init();
    }

    public void init() {
        gameSession = new GameSession();
    }

    public void update(float deltaTime) {
        gameSession.update(deltaTime);
    }

    public GameSession getGameSession() {
        return gameSession;
    }
}

