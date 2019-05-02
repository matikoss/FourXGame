package com.mygdx.fourxgame.controllers;

public class GameController {
    public CameraController cameraController;
    private GameSession gameSession;

    public GameController() {
        init();
    }

    public void init() {
        cameraController = new CameraController();
        gameSession = new GameSession();
    }

    public void update(float deltaTime) {
        cameraController.update(deltaTime);
        gameSession.update(deltaTime);
    }

    public GameSession getGameSession() {
        return gameSession;
    }
}

