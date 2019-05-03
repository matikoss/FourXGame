package com.mygdx.fourxgame.mainclasses;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;


public class GameSessionHud {
    public Stage stage;
    private Viewport viewport;
    private TextButton button;
    private TextButton.TextButtonStyle textButtonStyle;

    public GameSessionHud(SpriteBatch batch){
        viewport = new FitViewport(1280f, 720f, new OrthographicCamera());
        stage = new Stage(viewport, batch);

        Table table  = new Table();
        table.top();
        table.setFillParent(true);

        textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = new BitmapFont();
        button = new TextButton("Button1", textButtonStyle );

        button.setTransform(true);
        button.setScale(1f);
        button.setPosition(10,10);

        table.add(button);



        stage.addActor(table);
    }

}
