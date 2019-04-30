package com.mygdx.fourxgame.controllers;

import com.badlogic.gdx.graphics.g2d.Sprite;


public class GameController {
    public Sprite[] testSprites;
    public int selectedSprite;
    public CameraController cameraController;
    private GameSession gameSession;

    public GameController() {
        init();
    }

    public void init() {
        cameraController = new CameraController();
        gameSession = new GameSession();
        //initTestObjects();
    }

    public void update(float deltaTime) {
        cameraController.update(deltaTime);
        gameSession.update(deltaTime);
        //updateTestObjects(deltaTime);

    }

    public GameSession getGameSession() {
        return gameSession;
    }

    /*private void initTestObjects() {
        testSprites = new Sprite[6];

        int width = 32;
        int height = 32;

        Pixmap pixmap = createProceduralPixmap(width, height);

        Texture texture = new Texture(pixmap);

        for (int i = 0; i < testSprites.length-1; i++) {
            Sprite spr = new Sprite(texture);
            spr.setSize(1, 1);
            spr.setOrigin(spr.getWidth() / 2.0f, spr.getHeight() / 2.0f);
            float randomX = MathUtils.random(-2.0f, 2.0f);
            float randomY = MathUtils.random(-2.0f, 2.0f);
            spr.setPosition(randomX, randomY);

            testSprites[i] = spr;
        }
        Sprite testspr = new Sprite(texture);
        testspr.setSize(1,1);
        testspr.setPosition(10.0f, 10.0f);
        testSprites[5] = testspr;


        selectedSprite = 0;
    }*/

    /*private Pixmap createProceduralPixmap(int width, int height) {
        Pixmap pixmap = new Pixmap(width, height, Pixmap.Format.RGBA8888);
        pixmap.setColor(1, 0, 0, 0.5f);
        pixmap.fill();
        pixmap.setColor(1, 1, 0, 1);
        pixmap.drawLine(0, 0, width, height);
        pixmap.drawLine(width, 0, 0, height);
        pixmap.setColor(0, 1, 1, 1);
        pixmap.drawRectangle(0, 0, width, height);
        return pixmap;
    }*/

    /*private void updateTestObjects(float deltaTime) {
        float rotation = testSprites[selectedSprite].getRotation();
        rotation += 90 * deltaTime;
        rotation %= 360;
        testSprites[selectedSprite].setRotation(rotation);
    }*/


}

