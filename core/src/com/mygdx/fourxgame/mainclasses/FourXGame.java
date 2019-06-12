package com.mygdx.fourxgame.mainclasses;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.mygdx.fourxgame.controllers.GameController;

//Główna klasa w module core
//Odpowiada za rozpoczęcie gry i koordynowaniem wszystkiego co się dzieje w trakcie
public class FourXGame implements ApplicationListener {
	private GameController gameController;
	private GameRenderer gameRenderer;
	
	@Override
	public void create () {
		Gdx.app.setLogLevel(Application.LOG_DEBUG);
		gameController = new GameController();
		gameRenderer = new GameRenderer(gameController);
	}

	@Override
	public void resize(int width, int height) {
		gameRenderer.resize(width, height);
	}

	@Override
	public void render () {
		gameController.update(Gdx.graphics.getDeltaTime());
		gameRenderer.update();

		Gdx.gl.glClearColor(0, 112/255.0f, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		gameRenderer.render();

	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void dispose () {
		gameRenderer.dispose();
	}
}
