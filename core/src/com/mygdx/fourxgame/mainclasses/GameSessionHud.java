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
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.fourxgame.controllers.GameSession;
import com.mygdx.fourxgame.maptiles.MapTile;
import com.mygdx.fourxgame.maptiles.TownTile;
import javafx.scene.control.Tab;


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

    private Button buildCastleBtn;
    private Button buildBarracksBtn;
    private Button buildTownHallBtn;
    private Button buildStablesButton;
    private Button buildWallBtn;
    private Button buildBankBtn;
    private Button buildHousesBtn;
    private Button buildMenuBackBtn;

    private Button recruitBtn;
    private Button backRecruitmentMenuBtn;
    private Label recruitMenuArchersLabel;
    private Label recruitMenuFootmanLabel;
    private Label recruitMenuCavalryLabel;
    private TextField amountOfArchersToRecruit;
    private TextField amountOfFootmansToRecruit;
    private TextField amountOfCavalryToRecruit;

    private Image townInfoBackground;
    private Label townInfoTitle;

    private Label townInfoCastle;
    private Label townInfoTownhall;
    private Label townInfoWall;
    private Label townInfoBarracks;
    private Label townInfoStables;
    private Label townInfoHouses;
    private Label townInfoBank;

    private Label armySectionInfo;
    private Label inTownArchersAmount;
    private Label inTownFootmansAmount;
    private Label inTownCavalryAmount;


    private boolean isTownMenu = false;
    private boolean isBuildingMenu = false;
    private boolean isRecruitmentMenu = false;


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
        buttonTable.setPosition(380, -250);
        buttonTable.setFillParent(true);

        buttonTable.add(menuBtn).pad(5);
        buttonTable.row();
        buttonTable.add(endTurnBtn).pad(5);

        stage.addActor(buttonTable);

    }

    private void showTownButtons() {
        if (gameSession.isTileSelected() && gameSession.getSelectedTile().getClass().getSimpleName().equals("TownTile") && !isTownMenu && !isBuildingMenu && !isRecruitmentMenu) {
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
            showTownInfo();
            isTownMenu = true;
        }
    }

    private void showBuildingMenu() {
        stage.clear();

        isBuildingMenu = true;
        isTownMenu = false;

        addResourcesInfoToStage();

        Table buildingButtonsTable = new Table();
        buildingButtonsTable.setPosition(480, -250);
        buildingButtonsTable.setFillParent(true);

        buildingButtonsTable.add(buildCastleBtn).pad(3);
        buildingButtonsTable.add(buildTownHallBtn).pad(3);
        buildingButtonsTable.add(buildHousesBtn).pad(3);
        buildingButtonsTable.row();
        buildingButtonsTable.add(buildBarracksBtn).pad(3);
        buildingButtonsTable.add(buildStablesButton).pad(3);
        buildingButtonsTable.add(buildWallBtn).pad(3);
        buildingButtonsTable.row();
        buildingButtonsTable.add(buildMenuBackBtn).pad(3);
        buildingButtonsTable.add(buildBankBtn).pad(3);
        showTownInfo();

        stage.addActor(buildingButtonsTable);


    }

    private void showRecruitmentMenu() {
        stage.clear();

        isRecruitmentMenu = true;
        isTownMenu = false;

        addResourcesInfoToStage();

        Table recruitmentTable = new Table();
        recruitmentTable.setPosition(480, -250);
        recruitmentTable.setFillParent(true);

        recruitmentTable.add(recruitMenuArchersLabel).padBottom(3);
        recruitmentTable.add(amountOfArchersToRecruit).padBottom(3);
        recruitmentTable.row();
        recruitmentTable.add(recruitMenuFootmanLabel).padBottom(3);
        recruitmentTable.add(amountOfFootmansToRecruit).padBottom(3);
        recruitmentTable.row();
        recruitmentTable.add(recruitMenuCavalryLabel).padBottom(3);
        recruitmentTable.add(amountOfCavalryToRecruit).padBottom(3);
        recruitmentTable.row();
        recruitmentTable.add(backRecruitmentMenuBtn).pad(3);
        recruitmentTable.add(recruitBtn);
        showTownInfo();

        stage.addActor(recruitmentTable);
    }

    private void updateTownInfo() {
        TownTile tmpSelectedTown = (TownTile) gameSession.getSelectedTile();
        townInfoCastle.setText("Castle level: " + tmpSelectedTown.getCastle());
        townInfoTownhall.setText("Town Hall level: " + tmpSelectedTown.getTownHall());
        townInfoHouses.setText("Houses level: " + tmpSelectedTown.getHouses());
        townInfoBank.setText("Bank level: " + tmpSelectedTown.getBank());
        townInfoWall.setText("Wall level: " + tmpSelectedTown.getWall());
        townInfoBarracks.setText("Barracks level: " + tmpSelectedTown.getBarrack());
        townInfoStables.setText("Stable level: " + tmpSelectedTown.getStable());
    }

    private void showTownInfo() {
        townInfoBackground.setPosition(viewport.getWorldWidth() - 330, 250);

        Table townInfoTable = new Table();
        townInfoTable.setPosition(viewport.getWorldWidth() - 165, 450);
        townInfoTitle.setFontScale(1.5f);
        armySectionInfo.setFontScale(1.2f);
        updateTownInfo();


        townInfoTable.add(townInfoTitle).pad(2);
        townInfoTable.row();
        townInfoTable.add(townInfoCastle).pad(2);
        townInfoTable.row();
        townInfoTable.add(townInfoTownhall).pad(2);
        townInfoTable.row();
        townInfoTable.add(townInfoHouses).pad(2);
        townInfoTable.row();
        townInfoTable.add(townInfoBank).pad(2);
        townInfoTable.row();
        townInfoTable.add(townInfoBarracks).pad(2);
        townInfoTable.row();
        townInfoTable.add(townInfoStables).pad(2);
        townInfoTable.row();
        townInfoTable.add(townInfoWall).pad(2);
        townInfoTable.row();
        townInfoTable.row();
        townInfoTable.add(armySectionInfo).pad(2);
        townInfoTable.row();
        townInfoTable.add(inTownArchersAmount).pad(2);
        townInfoTable.row();
        townInfoTable.add(inTownFootmansAmount).pad(2);
        townInfoTable.row();
        townInfoTable.add(inTownCavalryAmount).pad(2);
        townInfoTable.row();

        stage.addActor(townInfoBackground);
        stage.addActor(townInfoTable);
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
        Texture townInfoBackgroundTexture = new Texture(Gdx.files.internal("townInfoBackgroundTexture.png"));


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

        buildCastleBtn = new TextButton("Build Castle", emptyTextButtonStyle);
        buildBarracksBtn = new TextButton("Build Barracks", emptyTextButtonStyle);
        buildTownHallBtn = new TextButton("Build Town Hall", emptyTextButtonStyle);
        buildStablesButton = new TextButton("Build Stables", emptyTextButtonStyle);
        buildBankBtn = new TextButton("Build Bank", emptyTextButtonStyle);
        buildHousesBtn = new TextButton("Build Houses", emptyTextButtonStyle);
        buildWallBtn = new TextButton("Build Wall", emptyTextButtonStyle);
        buildMenuBackBtn = new TextButton("Back", emptyTextButtonStyle);

        backRecruitmentMenuBtn = new TextButton("Back", emptyTextButtonStyle);
        recruitBtn = new TextButton("Recruit!", emptyTextButtonStyle);
        recruitMenuArchersLabel = new Label("Amount of archers:", new Label.LabelStyle(new BitmapFont(), Color.BLACK));
        recruitMenuFootmanLabel = new Label("Amount of footman:", new Label.LabelStyle(new BitmapFont(), Color.BLACK));
        recruitMenuCavalryLabel = new Label("Amount of cavalry:", new Label.LabelStyle(new BitmapFont(), Color.BLACK));
        amountOfArchersToRecruit = new TextField("0", new Skin(Gdx.files.internal("defaultAssets/uiskin.json")));
        amountOfFootmansToRecruit = new TextField("0", new Skin(Gdx.files.internal("defaultAssets/uiskin.json")));
        amountOfCavalryToRecruit = new TextField("0", new Skin(Gdx.files.internal("defaultAssets/uiskin.json")));

        townInfoBackground = new Image(townInfoBackgroundTexture);

        townInfoTitle = new Label("Town Information", new Label.LabelStyle(new BitmapFont(), Color.BLACK));
        townInfoCastle = new Label("Castle: ", new Label.LabelStyle(new BitmapFont(), Color.BLACK));
        townInfoTownhall = new Label("Townhall: ", new Label.LabelStyle(new BitmapFont(), Color.BLACK));
        townInfoHouses = new Label("Houses: ", new Label.LabelStyle(new BitmapFont(), Color.BLACK));
        townInfoBank = new Label("Bank: ", new Label.LabelStyle(new BitmapFont(), Color.BLACK));
        townInfoBarracks = new Label("Barracks: ", new Label.LabelStyle(new BitmapFont(), Color.BLACK));
        townInfoStables = new Label("Stables: ", new Label.LabelStyle(new BitmapFont(), Color.BLACK));
        townInfoWall = new Label("Wall: ", new Label.LabelStyle(new BitmapFont(), Color.BLACK));

        armySectionInfo = new Label("Army in town: ", new Label.LabelStyle(new BitmapFont(), Color.BLACK));
        inTownArchersAmount = new Label("Archers: ", new Label.LabelStyle(new BitmapFont(), Color.BLACK));
        inTownFootmansAmount = new Label("Footmans: ", new Label.LabelStyle(new BitmapFont(), Color.BLACK));
        inTownCavalryAmount = new Label("Cavalry: ", new Label.LabelStyle(new BitmapFont(), Color.BLACK));
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
        buildingMenuBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                showBuildingMenu();
            }
        });

        recruitmentMenuBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {

                showRecruitmentMenu();
            }
        });

        buildMenuBackBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                isBuildingMenu = false;
                showTownButtons();
            }
        });

        backRecruitmentMenuBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                isRecruitmentMenu = false;
                showTownButtons();
            }
        });


    }
}
