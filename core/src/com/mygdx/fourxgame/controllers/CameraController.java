package com.mygdx.fourxgame.controllers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class CameraController {
    private final float MAX_ZOOM_IN = 0.25f;
    private final float MAX_ZOOM_OUT = 10.0f;

    private Vector2 position;
    private float zoom;

    public CameraController() {
        position = new Vector2();
        zoom = 1.0f;
    }

    public void update(float deltaTime) {
        moveCamera(deltaTime);
        zoomCamera(deltaTime);
        moveCameraWithMouse(deltaTime);
    }

    public void moveCamera(float deltaTime) {
        float cameraMoveSpeed = 5 * deltaTime;
        float camMoveSpeedAcceleration = 5;

        if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) {
            cameraMoveSpeed *= camMoveSpeedAcceleration;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            changePositionOfCamera(-cameraMoveSpeed, 0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            changePositionOfCamera(cameraMoveSpeed, 0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            changePositionOfCamera(0, cameraMoveSpeed);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            changePositionOfCamera(0, -cameraMoveSpeed);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.BACKSPACE)) {
            this.setPosition(0, 0);
        }
    }

    public void moveCameraWithMouse(float deltaTime) {
        float cameraMoveSpeed = 6 * deltaTime;
        int mouseX = Gdx.input.getX();
        int mouseY = Gdx.input.getY();
        int screenWidth = Gdx.app.getGraphics().getWidth();
        int screenHeight = Gdx.app.getGraphics().getHeight();

        if (mouseX >= screenWidth - 10) {
            changePositionOfCamera(cameraMoveSpeed, 0);
        }
        if (mouseX <= 10) {
            changePositionOfCamera(-cameraMoveSpeed, 0);
        }
        if (mouseY >= screenHeight - 10) {
            changePositionOfCamera(0, -cameraMoveSpeed);
        }
        if (mouseY <= 10) {
            changePositionOfCamera(0, cameraMoveSpeed);
        }

        //System.out.println(mouseX);
        //System.out.println(mouseY);
        //System.out.println(Gdx.app.getGraphics().getWidth());
        //System.out.println(Gdx.app.getGraphics().getHeight());

    }

    public void zoomCamera(float deltaTime) {
        float cameraZoomSpeed = 1 * deltaTime;
        float cameraZoomSpeedAcceleration = 5;

        if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) {
            cameraZoomSpeed *= cameraZoomSpeedAcceleration;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.COMMA)) {
            addZoom(cameraZoomSpeed);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.PERIOD)) {
            addZoom(-cameraZoomSpeed);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.SLASH)) {
            setZoom(1);
        }
    }

    public void setPosition(float x, float y) {
        this.position.set(x, y);
    }

    public Vector2 getPosition() {
        return position;
    }

    public void addZoom(float amount) {
        setZoom(zoom + amount);
    }

    public void setZoom(float zoom) {
        this.zoom = MathUtils.clamp(zoom, MAX_ZOOM_IN, MAX_ZOOM_OUT);
    }

    public float getZoom() {
        return zoom;
    }

    public void applyTo(OrthographicCamera camera) {
        camera.position.x = position.x;
        camera.position.y = position.y;
        camera.zoom = zoom;
        camera.update();
    }

    public void changePositionOfCamera(float x, float y) {
        x += getPosition().x;
        y += getPosition().y;
        setPosition(x, y);
    }

    public void test(OrthographicCamera camera){
        int x = Gdx.input.getX();
        int y = Gdx.input.getY();
        Vector3 worldCoordinates = new Vector3(x, y, 0);
        camera.unproject(worldCoordinates);
        System.out.println(worldCoordinates.x);
        System.out.println(worldCoordinates.y);
    }
}
