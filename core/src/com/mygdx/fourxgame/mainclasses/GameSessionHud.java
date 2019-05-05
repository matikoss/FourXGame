package com.mygdx.fourxgame.mainclasses;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.fourxgame.controllers.GameSession;


public class GameSessionHud implements Disposable {

    private GameSession gameSession;

    public Stage stage;
    private Viewport viewport;
    private SpriteBatch batch;
    private Texture hudSkinTexture;
    private Player playerWhoseTurnIs;
    private Sprite hudSkinSprite;
    private Sprite woodIconSprite;
    private Sprite ironIconSprite;
    private Sprite goldIconSprite;
    private Sprite populationIconSprite;

    private Label woodAmountLabel;
    private Label ironAmountLabel;
    private Label goldAmountLabel;
    private Label populationLabel;

    private Button endTurnBtn;
    private Button menuBtn;
    private Button backBtnTownMenu;

    private Button buildingMenuBtn;
    private Button recruitmentMenuBtn;
    private Button buyTilesBtn;

    private boolean isTownMenu = false;


    public GameSessionHud(SpriteBatch batch, GameSession gameSession) {
        this.batch = batch;
        this.gameSession = gameSession;
        viewport = new StretchViewport(1280f, 720f, new OrthographicCamera());
        stage = new Stage(viewport, batch);
        hudSkinTexture = new Texture(Gdx.files.internal("hudskin.png"));
        hudSkinSprite = new Sprite(hudSkinTexture);
        hudSkinSprite.setPosition(0, 0);
        initResourcesBar();
        initButtons();
        setButtonsListenersHud();
        showMainButtons();


    }

    public void render() {
        drawBasicHUD();
    }

    private void drawBasicHUD() {
        batch.begin();
        hudSkinSprite.draw(batch);
        woodIconSprite.draw(batch);
        ironIconSprite.draw(batch);
        goldIconSprite.draw(batch);
        populationIconSprite.draw(batch);


        batch.end();
    }

    public void update() {
        updateResources();
        showTownButtons();
    }

    @Override
    public void dispose() {

    }

    private void showMainButtons() {
        stage.clear();

        addResourcesInfoToStage();

        Table buttonTable = new Table();
        buttonTable.setPosition(360, -250);
        buttonTable.setFillParent(true);

        buttonTable.add(menuBtn).pad(5);
        buttonTable.row();
        buttonTable.add(endTurnBtn).pad(5);

        stage.addActor(buttonTable);

    }

    private void showTownButtons() {
        if (gameSession.isTileSelected() && gameSession.getSelectedTile().getClass().getSimpleName().equals("TownTile") && !isTownMenu) {
            stage.clear();

            addResourcesInfoToStage();

            Table buttonTable = new Table();
            buttonTable.setPosition(480, -250);
            buttonTable.setFillParent(true);

            buttonTable.add(buildingMenuBtn).pad(5);
            buttonTable.add(recruitmentMenuBtn).pad(5);
            buttonTable.add(buyTilesBtn).pad(5);
            buttonTable.row();
            buttonTable.add(backBtnTownMenu);

            stage.addActor(buttonTable);
            isTownMenu = true;
        }
    }

    private void initResourcesBar() {
        Texture texture = new Texture(Gdx.files.internal("woodIcon.png"));
        woodIconSprite = new Sprite(texture);
        texture = new Texture(Gdx.files.internal("goldIcon.png"));
        goldIconSprite = new Sprite(texture);
        texture = new Texture(Gdx.files.internal("ironIcon.png"));
        ironIconSprite = new Sprite(texture);
        texture = new Texture(Gdx.files.internal("populationIcon.png"));
        populationIconSprite = new Sprite(texture);

        woodIconSprite.setPosition(10, Gdx.graphics.getHeight() - 38);
        ironIconSprite.setPosition(160, Gdx.graphics.getHeight() - 38);
        goldIconSprite.setPosition(310, Gdx.graphics.getHeight() - 38);
        populationIconSprite.setPosition(460, Gdx.graphics.getHeight() - 38);

        woodAmountLabel = new Label(Integer.toString(0), new Label.LabelStyle(new BitmapFont(), Color.BLACK));
        ironAmountLabel = new Label(Integer.toString(0), new Label.LabelStyle(new BitmapFont(), Color.BLACK));
        goldAmountLabel = new Label(Integer.toString(0), new Label.LabelStyle(new BitmapFont(), Color.BLACK));
        populationLabel = new Label(Integer.toString(0), new Label.LabelStyle(new BitmapFont(), Color.BLACK));

        woodAmountLabel.setX(60);
        woodAmountLabel.setY(Gdx.graphics.getHeight() - 30);
        woodAmountLabel.setFontScale(1f);

        ironAmountLabel.setX(210);
        ironAmountLabel.setY(Gdx.graphics.getHeight() - 30);
        ironAmountLabel.setFontScale(1f);

        goldAmountLabel.setX(360);
        goldAmountLabel.setY(Gdx.graphics.getHeight() - 30);
        goldAmountLabel.setFontScale(1f);

        populationLabel.setX(510);
        populationLabel.setY(Gdx.graphics.getHeight() - 30);
        populationLabel.setFontScale(1f);

        stage.addActor(woodAmountLabel);
        stage.addActor(ironAmountLabel);
        stage.addActor(goldAmountLabel);
        stage.addActor(populationLabel);

    }

    private void addResourcesInfoToStage() {
        stage.addActor(woodAmountLabel);
        stage.addActor(ironAmountLabel);
        stage.addActor(goldAmountLabel);
        stage.addActor(populationLabel);
    }

    private void updateResources() {
        woodAmountLabel.setText(Integer.toString(playerWhoseTurnIs.getAmountOfWood()));
        ironAmountLabel.setText(Integer.toString(playerWhoseTurnIs.getAmountOfIron()));
        goldAmountLabel.setText(Integer.toString(playerWhoseTurnIs.getAmountOfGold()));
        populationLabel.setText(Integer.toString(playerWhoseTurnIs.getPopulation()));
    }

    private void initButtons() {
        Texture emptyTextureBtnUp = new Texture(Gdx.files.internal("emptyButtonOff.png"));
        Texture emptyTextureBtnDown = new Texture(Gdx.files.internal("emptyButtonOn.png"));
        Texture buildingTextureBtnUp = new Texture(Gdx.files.internal("buildButtonOff.png"));
        Texture buildingTextureBtnDown = new Texture(Gdx.files.internal("buildButtonOn.png"));
        Texture recruitTextureBtnUp = new Texture(Gdx.files.internal("recruitButtonOff.png"));
        Texture recruitTextureBtnDown = new Texture(Gdx.files.internal("recruitButtonOn.png"));


        Drawable drawableEmptyTextureBtnUp = new TextureRegionDrawable(new TextureRegion(emptyTextureBtnUp));
        Drawable drawableEmptyTextureBtnDown = new TextureRegionDrawable(new TextureRegion(emptyTextureBtnDown));

        Drawable drawableBuildingTextureBtnUp = new TextureRegionDrawable(new TextureRegion(buildingTextureBtnUp));
        Drawable drawableBuildingTextureBtnDown = new TextureRegionDrawable(new TextureRegion(buildingTextureBtnDown));

        Drawable drawableRecruitTextureBtnUp = new TextureRegionDrawable(new TextureRegion(recruitTextureBtnUp));
        Drawable drawableRecruitTextureBtnDown = new TextureRegionDrawable(new TextureRegion(recruitTextureBtnDown));

        ImageButton.ImageButtonStyle buildingButtonStyle = new ImageButton.ImageButtonStyle();
        buildingButtonStyle.up = drawableBuildingTextureBtnUp;
        buildingButtonStyle.down = drawableBuildingTextureBtnDown;

        ImageButton.ImageButtonStyle recruitButtonStyle = new ImageButton.ImageButtonStyle();
        recruitButtonStyle.up = drawableRecruitTextureBtnUp;
        recruitButtonStyle.down = drawableRecruitTextureBtnDown;


        TextButton.TextButtonStyle emptyTextButtonStyle = new TextButton.TextButtonStyle();
        emptyTextButtonStyle.up = drawableEmptyTextureBtnUp;
        emptyTextButtonStyle.down = drawableEmptyTextureBtnDown;
        emptyTextButtonStyle.font = new BitmapFont();

        endTurnBtn = new TextButton("End Turn", emptyTextButtonStyle);
        menuBtn = new TextButton("Menu", emptyTextButtonStyle);
        backBtnTownMenu = new TextButton("Back", emptyTextButtonStyle);

        buildingMenuBtn = new ImageButton(buildingButtonStyle);
        recruitmentMenuBtn = new ImageButton(recruitButtonStyle);
        buyTilesBtn = new TextButton("Buy Tiles", emptyTextButtonStyle);


        //Gdx.input.setInputProcessor(stage);

    }


    public void setPlayerWhoseTurnIs(Player playerWhoseTurnIs) {
        this.playerWhoseTurnIs = playerWhoseTurnIs;
    }

    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    private void setButtonsListenersHud() {
        backBtnTownMenu.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                gameSession.setTileSelected(false);
                gameSession.setSelectedTile(null);
                isTownMenu = false;
                showMainButtons();
            }
        });

    }
}
