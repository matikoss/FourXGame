package com.mygdx.fourxgame.mainclasses;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
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
import com.mygdx.fourxgame.maptiles.*;

//Klasa zarządzająca rysowaniem interfejsu użytkownika w czasie gry (HUDu)
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

    private BitmapFont bitmapFont;
    private Label.LabelStyle labelStyleBlack;
    private Label.LabelStyle labelStyleBrown;
    private Label.LabelStyle labelStyleWhite;
    private Label.LabelStyle labelStyleGold;

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
    private Button leaveArmyFromTownBtn;

    private Button buyTilesMenuBackBtn;

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
    private Label recruitmentTotalCostLabel;
    private TextField amountOfArchersToRecruitTF;
    private TextField amountOfFootmansToRecruitTF;
    private TextField amountOfCavalryToRecruitTF;

    private Image infoBackground;
    private Label townInfoTitle;
    private Label townOwner;
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

    private Label armyInfo;
    private Label armyArchersAmountInfo;
    private Label armyFootmansAmountInfo;
    private Label armyCavalryAmountInfo;
    private Button armyBackButton;
    private Button armySplitButtonMenu;
    private Button armySplitButton;
    private TextField amountOfArchersToSplitTF;
    private TextField amountOfFootmansToSplitTF;
    private TextField amountOfCavalryToSplitTF;

    private Label currentPlayer;

    private TextField amountOfArchersToLeaveTown;
    private TextField amountOfFootmansToLeaveTown;
    private TextField amountOfCavalryToLeaveTown;
    private Label amountOfArchersToLeaveTownLabel;
    private Label amountOfFootmansToLeaveTownLabel;
    private Label amountOfCavalryToLeaveTownLabel;
    private Button leaveTownBtn;
    private Button leaveTownBackBtn;

    private Label hoveringLabel;

    private Button buyTileYesButton;
    private Button buyTileNoButton;
    public Label selectedToBuyTileCost;
    private Label selectedToBuyTileQuestion;

    private Label resourceBuildingInfo;
    private Label resourceTileBuildingCostInfo;
    private Button buildResourceBuildingBtn;
    private Button buildResourceBuildingBackBtn;

    private Button inGameMenuBack;
    private Button menuExitToMainMenu;
    private Button closeGame;
    private Button addNewPlayerButton;
    private Button menuSaveGame;

    private Button saveToSlot1;
    private Button saveToSlot2;
    private Button saveToSlot3;
    private Button saveMenuBack;


    private boolean isTownMenu = false;
    private boolean isBuildingMenu = false;
    private boolean isRecruitmentMenu = false;
    private boolean isArmyLeaveMenu = false;
    private boolean isMouseOnButton = false;
    public boolean isBuyTileMenu = false;
    public boolean isBuyTileMode = false;
    public boolean isResourceTileMenu = false;
    private boolean isSplitArmyMenu = false;


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
        stage.act();

        drawBasicHUD();
        showTownButtons();
        showArmyInfo();
        emptyTileSelectedHud();
        showResourceTileMenu();
        showBuildingCostLabel();
        showBuyTileMenu();
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
        updateRecruitmentTotalCost();
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

    private void showMenuButtons() {
        showMenuOrSaveMenuPattern(menuSaveGame, menuExitToMainMenu, closeGame, inGameMenuBack, addNewPlayerButton);
    }

    private void showSaveMenu() {
        showMenuOrSaveMenuPattern(saveToSlot1, saveToSlot2, saveToSlot3, saveMenuBack, null);


    }

    private void showMenuOrSaveMenuPattern(Button button1, Button button2, Button button3, Button button4, Button button5) {
        stage.clear();

        addResourcesInfoToStage();

        Table saveTable = new Table();
        saveTable.setPosition(480, -250);
        saveTable.setFillParent(true);

        saveTable.add(button1).pad(2);
        saveTable.add(button2).pad(2);
        saveTable.add(button3).pad(2);
        saveTable.row();
        saveTable.add(button4);
        saveTable.add(button5).pad(2);

        stage.addActor(saveTable);
    }

    private void showTownButtons() {
        if (gameSession.isTileSelected() && gameSession.getSelectedTile().getClass().getSimpleName().equals("TownTile") && !isTownMenu && !isBuildingMenu && !isRecruitmentMenu && !isArmyLeaveMenu && !isBuyTileMode) {
            stage.clear();

            addResourcesInfoToStage();

            Table buttonTable = new Table();
            buttonTable.setPosition(480, -250);
            buttonTable.setFillParent(true);
            if (gameSession.getSelectedTile().getOwner().equals(playerWhoseTurnIs.playerName)) {
                buttonTable.add(buildingMenuBtn).pad(5);
                buttonTable.add(recruitmentMenuBtn).pad(5);
                buttonTable.add(buyTilesBtn).pad(5);
                buttonTable.row();
                buttonTable.add(backBtnTownMenu);
                buttonTable.add(leaveArmyFromTownBtn);
            } else {
                buttonTable.add(backBtnTownMenu);
            }
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

        recruitmentTable.add(recruitMenuArchersLabel).pad(3);
        recruitmentTable.add(amountOfArchersToRecruitTF).pad(3);
        recruitmentTable.row();
        recruitmentTable.add(recruitMenuFootmanLabel).pad(3);
        recruitmentTable.add(amountOfFootmansToRecruitTF).pad(3);
        recruitmentTable.row();
        recruitmentTable.add(recruitMenuCavalryLabel).pad(3);
        recruitmentTable.add(amountOfCavalryToRecruitTF).pad(3);
        recruitmentTable.row();
        recruitmentTable.add(recruitmentTotalCostLabel).padBottom(3).center();
        recruitmentTable.row();
        recruitmentTable.add(backRecruitmentMenuBtn).padBottom(3);
        recruitmentTable.add(recruitBtn);
        showTownInfo();

        stage.addActor(recruitmentTable);
    }

    private void updateRecruitmentTotalCost() {
        if (isRecruitmentMenu) {
            int woodTotalCost, ironTotalCost, goldTotalCost;
            int amountOfArchers, amountOfFootmans, amountOfCavalry;
            try {
                amountOfArchers = Integer.parseInt(amountOfArchersToRecruitTF.getText());
                amountOfFootmans = Integer.parseInt(amountOfFootmansToRecruitTF.getText());
                amountOfCavalry = Integer.parseInt(amountOfCavalryToRecruitTF.getText());
            } catch (NumberFormatException e) {
                woodTotalCost = 0;
                ironTotalCost = 0;
                goldTotalCost = 0;
                recruitmentTotalCostLabel.setText("Podano złe wartości");
                return;
            }
            woodTotalCost = amountOfArchers * GameplayConstants.archersWoodCost + amountOfFootmans * GameplayConstants.footmansWoodCost + amountOfCavalry * GameplayConstants.cavalryWoodCost;
            ironTotalCost = amountOfArchers * GameplayConstants.archersIronCost + amountOfFootmans * GameplayConstants.footmansIronCost + amountOfCavalry * GameplayConstants.cavalryIronCost;
            goldTotalCost = amountOfArchers * GameplayConstants.archersGoldCost + amountOfFootmans * GameplayConstants.footmansGoldCost + amountOfCavalry * GameplayConstants.cavalryGoldCost;

            recruitmentTotalCostLabel.setText("Recruitment cost: " + woodTotalCost + "W " + ironTotalCost + "I " + goldTotalCost + "G");
        }
    }

    private void updateTownInfo() {
        TownTile tmpSelectedTown = (TownTile) gameSession.getSelectedTile();
        townOwner.setText("Owner: " + tmpSelectedTown.getOwner());
        townInfoCastle.setText("Castle: " + tmpSelectedTown.getCastle() + "lvl");
        townInfoTownhall.setText("Town Hall: " + tmpSelectedTown.getTownHall() + "lvl");
        townInfoHouses.setText("Houses level: " + tmpSelectedTown.getHouses() + "lvl");
        townInfoBank.setText("Bank level: " + tmpSelectedTown.getBank() + "lvl");
        townInfoWall.setText("Wall level: " + tmpSelectedTown.getWall() + "lvl");
        townInfoBarracks.setText("Barracks level: " + tmpSelectedTown.getBarrack() + "lvl");
        townInfoStables.setText("Stable level: " + tmpSelectedTown.getStable() + "lvl");


        if (gameSession.getSelectedTile().getOwner().equals(playerWhoseTurnIs.playerName)) {
            inTownArchersAmount.setText("Archers: " + tmpSelectedTown.getArchersInTown());
            inTownFootmansAmount.setText("Footmans: " + tmpSelectedTown.getFootmansInTown());
            inTownCavalryAmount.setText("Cavalry: " + tmpSelectedTown.getCavalryInTown());
        } else {
            inTownArchersAmount.setText("Archers: " + calculateArmyInfo(tmpSelectedTown.getArchersInTown()));
            inTownFootmansAmount.setText("Footmans: " + calculateArmyInfo(tmpSelectedTown.getFootmansInTown()));
            inTownCavalryAmount.setText("Cavalry: " + calculateArmyInfo(tmpSelectedTown.getCavalryInTown()));

        }


    }

    private void showTownInfo() {
        infoBackground.setPosition(viewport.getWorldWidth() - 330, 250);

        Table townInfoTable = new Table();
        townInfoTable.setPosition(viewport.getWorldWidth() - 165, 450);
        townInfoTitle.setFontScale(1.5f);
        armySectionInfo.setFontScale(1.2f);
        updateTownInfo();
        townInfoTable.add(townInfoTitle).pad(2);
        townInfoTable.row();
        townInfoTable.add(townOwner).pad(2);
        townInfoTable.row();

        if (gameSession.getSelectedTile().getOwner().equals(playerWhoseTurnIs.playerName)) {
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
        }
        townInfoTable.add(armySectionInfo).pad(2);
        townInfoTable.row();
        townInfoTable.add(inTownArchersAmount).pad(2);
        townInfoTable.row();
        townInfoTable.add(inTownFootmansAmount).pad(2);
        townInfoTable.row();
        townInfoTable.add(inTownCavalryAmount).pad(2);
        townInfoTable.row();

        stage.addActor(infoBackground);
        stage.addActor(townInfoTable);
    }

    private void showArmyInfo() {
        if (gameSession.isTileSelected() && gameSession.getSelectedTile().getClass().getSimpleName().equals("Army") && gameSession.getPlayerWhoseTurnIs().getPlayerName().equals(gameSession.getSelectedTile().getOwner()) && !isSplitArmyMenu) {
            showFullArmyInfo();
            showArmyButtons();
        } else if (gameSession.isTileSelected() && gameSession.getSelectedTile().getClass().getSimpleName().equals("Army") && !gameSession.getPlayerWhoseTurnIs().getPlayerName().equals(gameSession.getSelectedTile().getOwner())) {
            showShortArmyInfo();
        }
    }

    private void showFullArmyInfo() {
        isTownMenu = false;
        stage.clear();
        //showMainButtons();
        addResourcesInfoToStage();

        infoBackground.setPosition(viewport.getWorldWidth() - 330, 250);


        Table armyInfoTable = new Table();
        armyInfoTable.setPosition(viewport.getWorldWidth() - 165, 450);
        armyArchersAmountInfo.setText("Archers: " + ((Army) gameSession.getSelectedTile()).getArchersAmount());
        armyFootmansAmountInfo.setText("Footmans: " + ((Army) gameSession.getSelectedTile()).getFootmansAmount());
        armyCavalryAmountInfo.setText("Cavalry:" + ((Army) gameSession.getSelectedTile()).getCavalryAmount());


        armyInfoTable.add(armyInfo).pad(3);
        armyInfoTable.row();
        armyInfoTable.add(armyArchersAmountInfo).pad(1);
        armyInfoTable.row();
        armyInfoTable.add(armyFootmansAmountInfo).pad(1);
        armyInfoTable.row();
        armyInfoTable.add(armyCavalryAmountInfo).pad(1);
        armyInfoTable.row();
        isSplitArmyMenu = true;

        stage.addActor(infoBackground);
        stage.addActor(armyInfoTable);
    }

    private void showShortArmyInfo() {
        stage.clear();
        showMainButtons();
        addResourcesInfoToStage();
        infoBackground.setPosition(viewport.getWorldWidth() - 330, 250);


        Table armyInfoTable = new Table();
        armyInfoTable.setPosition(viewport.getWorldWidth() - 165, 450);

        armyArchersAmountInfo.setText("Archers: " + calculateArmyInfo(((Army) gameSession.getSelectedTile()).getArchersAmount()));
        armyFootmansAmountInfo.setText("Footmans: " + calculateArmyInfo(((Army) gameSession.getSelectedTile()).getFootmansAmount()));
        armyCavalryAmountInfo.setText("Cavalry:" + calculateArmyInfo(((Army) gameSession.getSelectedTile()).getCavalryAmount()));

        armyInfoTable.add(armyInfo).pad(3);
        armyInfoTable.row();
        armyInfoTable.add(armyArchersAmountInfo).pad(1);
        armyInfoTable.row();
        armyInfoTable.add(armyFootmansAmountInfo).pad(1);
        armyInfoTable.row();
        armyInfoTable.add(armyCavalryAmountInfo).pad(1);
        armyInfoTable.row();

        stage.addActor(infoBackground);
        stage.addActor(armyInfoTable);
    }

    private void showArmyButtons() {
        Table armyButtonsTable = new Table();
        armyButtonsTable.setPosition(380, -250);
        armyButtonsTable.setFillParent(true);
        armyButtonsTable.add(armySplitButtonMenu).pad(2);
        armyButtonsTable.row();
        armyButtonsTable.add(armyBackButton);
        stage.addActor(armyButtonsTable);
    }

    private void showSplitArmyMenu() {
        stage.clear();
        showFullArmyInfo();
        Table splitMenuTable = new Table();
        splitMenuTable.setPosition(480, -250);
        splitMenuTable.setFillParent(true);
        splitMenuTable.add(recruitMenuArchersLabel).pad(2);
        splitMenuTable.add(amountOfArchersToSplitTF).pad(2);
        splitMenuTable.row();
        splitMenuTable.add(recruitMenuFootmanLabel).pad(2);
        splitMenuTable.add(amountOfFootmansToSplitTF).pad(2);
        splitMenuTable.row();
        splitMenuTable.add(recruitMenuCavalryLabel);
        splitMenuTable.add(amountOfCavalryToSplitTF).pad(2);
        splitMenuTable.row();
        splitMenuTable.add(armyBackButton).pad(2);
        splitMenuTable.add(armySplitButton).pad(2);
        stage.addActor(splitMenuTable);
    }

    private void showBuyTilesButtons() {
        stage.clear();
        isBuyTileMode = true;
        isTownMenu = false;
        Table table = new Table();
        table.setPosition(480, -250);
        table.setFillParent(true);
        table.add(buyTilesMenuBackBtn);
        addResourcesInfoToStage();
        showTownInfo();
        stage.addActor(table);
    }

    private void emptyTileSelectedHud() {
        if (gameSession.isTileSelected() && gameSession.getSelectedTile().getClass().getSimpleName().equals("EmptyTile")) {
            isTownMenu = false;
            stage.clear();
            showMainButtons();
            addResourcesInfoToStage();
        }
    }

    private void showBuildingCostLabel() {
        if (isMouseOnButton) {
            Vector3 vector3 = viewport.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
            hoveringLabel.setX(vector3.x - 200);
            hoveringLabel.setY(vector3.y + 10);
            stage.addActor(hoveringLabel);
        }
    }

    private void showArmyLeaveMenu() {
        stage.clear();

        isTownMenu = false;
        isArmyLeaveMenu = true;

        addResourcesInfoToStage();

        Table armyLeaveTable = new Table();
        armyLeaveTable.setPosition(480, -250);
        armyLeaveTable.setFillParent(true);

        armyLeaveTable.add(amountOfArchersToLeaveTownLabel).pad(2);
        armyLeaveTable.add(amountOfArchersToLeaveTown);
        armyLeaveTable.row();
        armyLeaveTable.add(amountOfFootmansToLeaveTownLabel).pad(2);
        armyLeaveTable.add(amountOfFootmansToLeaveTown);
        armyLeaveTable.row();
        armyLeaveTable.add(amountOfCavalryToLeaveTownLabel).pad(2);
        armyLeaveTable.add(amountOfCavalryToLeaveTown);
        armyLeaveTable.row();
        armyLeaveTable.add(leaveTownBackBtn).pad(2);
        armyLeaveTable.add(leaveTownBtn);


        stage.addActor(armyLeaveTable);

    }

    private void showBuyTileMenu() {
        if (gameSession.isTileToBuySelected() && !isBuyTileMenu) {
            stage.clear();
            addResourcesInfoToStage();
            showTownInfo();
            Table buyTileTable = new Table();
            buyTileTable.setPosition(480, -250);
            buyTileTable.setFillParent(true);
            buyTileTable.add(selectedToBuyTileCost).pad(2);
            buyTileTable.row();
            buyTileTable.add(selectedToBuyTileQuestion).center().pad(2);
            buyTileTable.row();
            buyTileTable.add(buyTileNoButton).pad(2);
            buyTileTable.add(buyTileYesButton).pad(2);

            stage.addActor(buyTileTable);
            isBuyTileMenu = true;
        }
    }

    private void showResourceTileMenu() {
        if (gameSession.isTileSelected() && !gameSession.getSelectedTile().getClass().getSimpleName().equals("TownTile") && !gameSession.getSelectedTile().getClass().getSimpleName().equals("Army")
                && !gameSession.getSelectedTile().getClass().getSimpleName().equals("EmptyTile") && !isResourceTileMenu) {
            isTownMenu = false;
            isResourceTileMenu = true;
            stage.clear();
            addResourcesInfoToStage();
            Table resourceTileTable = new Table();
            resourceTileTable.setPosition(480, -250);
            resourceTileTable.setFillParent(true);
            resourceTileBuildingCostInfo.setText("Building cost: " + GameplayConstants.woodCostResourceTile + "W " + GameplayConstants.ironCostResourceTile + "I " + GameplayConstants.goldCostResourceTile + "G");
            resourceTileTable.add(resourceBuildingInfo).pad(2);
            resourceTileTable.row();
            if (gameSession.getSelectedTile().getClass().getSimpleName().equals("WoodTile")) {
                resourceBuildingInfo.setText("Is lumber mill built: " + ((WoodTile) gameSession.getSelectedTile()).isLumbermillBuilt());

                if (!((WoodTile) gameSession.getSelectedTile()).isLumbermillBuilt()) {

                    resourceTileTable.add(resourceTileBuildingCostInfo).pad(2);
                    resourceTileTable.row();
                    resourceTileTable.add(buildResourceBuildingBtn).pad(2);
                }
            } else if (gameSession.getSelectedTile().getClass().getSimpleName().equals("IronTile")) {
                resourceBuildingInfo.setText("Is iron mine built: " + ((IronTile) gameSession.getSelectedTile()).isIronMineBuilt());
                if (!((IronTile) gameSession.getSelectedTile()).isIronMineBuilt()) {
                    resourceTileTable.add(resourceTileBuildingCostInfo).pad(2);
                    resourceTileTable.row();
                    resourceTileTable.add(buildResourceBuildingBtn).pad(2);
                }
            } else if (gameSession.getSelectedTile().getClass().getSimpleName().equals("GoldTile")) {
                resourceBuildingInfo.setText("Is gold mine built: " + ((GoldTile) gameSession.getSelectedTile()).isGoldMineBuilt());
                if (!((GoldTile) gameSession.getSelectedTile()).isGoldMineBuilt()) {
                    resourceTileTable.add(resourceTileBuildingCostInfo).pad(2);
                    resourceTileTable.row();
                    resourceTileTable.add(buildResourceBuildingBtn).pad(2);
                }

            }
            resourceTileTable.add(buildResourceBuildingBackBtn).pad(2);
            stage.addActor(resourceTileTable);
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

        woodIconSprite.setPosition(10, viewport.getWorldHeight() - 38);
        ironIconSprite.setPosition(160, viewport.getWorldHeight() - 38);
        goldIconSprite.setPosition(310, viewport.getWorldHeight() - 38);
        populationIconSprite.setPosition(460, viewport.getWorldHeight() - 38);

        woodAmountLabel = new Label(Integer.toString(0), new Label.LabelStyle(new BitmapFont(), Color.BLACK));
        ironAmountLabel = new Label(Integer.toString(0), new Label.LabelStyle(new BitmapFont(), Color.BLACK));
        goldAmountLabel = new Label(Integer.toString(0), new Label.LabelStyle(new BitmapFont(), Color.BLACK));
        populationLabel = new Label(Integer.toString(0), new Label.LabelStyle(new BitmapFont(), Color.BLACK));
        currentPlayer = new Label("Player", new Label.LabelStyle(new BitmapFont(), Color.BLACK));

        woodAmountLabel.setX(60);
        woodAmountLabel.setY(viewport.getWorldHeight() - 30);
        woodAmountLabel.setFontScale(1f);

        ironAmountLabel.setX(210);
        ironAmountLabel.setY(viewport.getWorldHeight() - 30);
        ironAmountLabel.setFontScale(1f);

        goldAmountLabel.setX(360);
        goldAmountLabel.setY(viewport.getWorldHeight() - 30);
        goldAmountLabel.setFontScale(1f);

        populationLabel.setX(510);
        populationLabel.setY(viewport.getWorldHeight() - 30);
        populationLabel.setFontScale(1f);

        currentPlayer.setX(viewport.getWorldWidth() - 100);
        currentPlayer.setY(viewport.getWorldHeight() - 30);
        currentPlayer.setFontScale(1f);


        stage.addActor(woodAmountLabel);
        stage.addActor(ironAmountLabel);
        stage.addActor(goldAmountLabel);
        stage.addActor(populationLabel);
        stage.addActor(currentPlayer);

    }


    private void addResourcesInfoToStage() {
        stage.addActor(woodAmountLabel);
        stage.addActor(ironAmountLabel);
        stage.addActor(goldAmountLabel);
        stage.addActor(populationLabel);
        stage.addActor(currentPlayer);
    }

    private void updateResources() {
        woodAmountLabel.setText(Integer.toString(playerWhoseTurnIs.getAmountOfWood()));
        ironAmountLabel.setText(Integer.toString(playerWhoseTurnIs.getAmountOfIron()));
        goldAmountLabel.setText(Integer.toString(playerWhoseTurnIs.getAmountOfGold()));
        populationLabel.setText(Integer.toString(playerWhoseTurnIs.getPopulation()));
        currentPlayer.setText(playerWhoseTurnIs.playerName);
    }

    private void initButtons() {
        bitmapFont = new BitmapFont();
        labelStyleBlack = new Label.LabelStyle(bitmapFont, Color.BLACK);
        labelStyleBrown = new Label.LabelStyle(bitmapFont, Color.BROWN);
        labelStyleWhite = new Label.LabelStyle(bitmapFont, Color.WHITE);
        labelStyleGold = new Label.LabelStyle(bitmapFont, Color.GOLD);

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
        leaveArmyFromTownBtn = new TextButton("Leave the army", emptyTextButtonStyle);

        buyTilesMenuBackBtn = new TextButton("Back", emptyTextButtonStyle);

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
        recruitMenuArchersLabel = new Label("Amount of archers:", labelStyleBlack);
        recruitMenuFootmanLabel = new Label("Amount of footman:", labelStyleBlack);
        recruitMenuCavalryLabel = new Label("Amount of cavalry:", labelStyleBlack);
        recruitmentTotalCostLabel = new Label("Total recruitment cost:", labelStyleGold);
        amountOfArchersToRecruitTF = new TextField("0", new Skin(Gdx.files.internal("defaultAssets/uiskin.json")));
        amountOfFootmansToRecruitTF = new TextField("0", new Skin(Gdx.files.internal("defaultAssets/uiskin.json")));
        amountOfCavalryToRecruitTF = new TextField("0", new Skin(Gdx.files.internal("defaultAssets/uiskin.json")));

        amountOfArchersToLeaveTown = new TextField("0", new Skin(Gdx.files.internal("defaultAssets/uiskin.json")));
        amountOfFootmansToLeaveTown = new TextField("0", new Skin(Gdx.files.internal("defaultAssets/uiskin.json")));
        amountOfCavalryToLeaveTown = new TextField("0", new Skin(Gdx.files.internal("defaultAssets/uiskin.json")));

        amountOfArchersToLeaveTownLabel = new Label("Archers leaving town: ", labelStyleBlack);
        amountOfFootmansToLeaveTownLabel = new Label("Footmans leaving town: ", labelStyleBlack);
        amountOfCavalryToLeaveTownLabel = new Label("Cavalry leaving town: ", labelStyleBlack);
        leaveTownBtn = new TextButton("Leave town!", emptyTextButtonStyle);
        leaveTownBackBtn = new TextButton("Back", emptyTextButtonStyle);

        infoBackground = new Image(townInfoBackgroundTexture);

        townInfoTitle = new Label("Town statistics", labelStyleBrown);
        townOwner = new Label("Owner: ", labelStyleWhite);
        townInfoCastle = new Label("Castle: ", labelStyleBlack);
        townInfoTownhall = new Label("Townhall: ", labelStyleBlack);
        townInfoHouses = new Label("Houses: ", labelStyleBlack);
        townInfoBank = new Label("Bank: ", labelStyleBlack);
        townInfoBarracks = new Label("Barracks: ", labelStyleBlack);
        townInfoStables = new Label("Stables: ", labelStyleBlack);
        townInfoWall = new Label("Wall: ", labelStyleBlack);

        hoveringLabel = new Label("", labelStyleGold);
        hoveringLabel.setFontScale(1f);

        buyTileYesButton = new TextButton("Yes", emptyTextButtonStyle);
        buyTileNoButton = new TextButton("No", emptyTextButtonStyle);
        selectedToBuyTileCost = new Label("", labelStyleGold);
        selectedToBuyTileQuestion = new Label("Buy field?", labelStyleBlack);


        armySectionInfo = new Label("Army in town: ", labelStyleBrown);
        inTownArchersAmount = new Label("Archers: ", labelStyleBlack);
        inTownFootmansAmount = new Label("Footmans: ", labelStyleBlack);
        inTownCavalryAmount = new Label("Cavalry: ", labelStyleBlack);

        armyInfo = new Label("Army statistics", labelStyleBlack);
        armyArchersAmountInfo = new Label("Archers amount: ", labelStyleBlack);
        armyFootmansAmountInfo = new Label("Footmans amount: ", labelStyleBlack);
        armyCavalryAmountInfo = new Label("Cavalry amount: ", labelStyleBlack);
        armyInfo.setFontScale(1.5f);
        armyBackButton = new TextButton("Back", emptyTextButtonStyle);
        armySplitButtonMenu = new TextButton("Split army", emptyTextButtonStyle);
        armySplitButton = new TextButton("Ok", emptyTextButtonStyle);
        amountOfArchersToSplitTF = new TextField("0", new Skin(Gdx.files.internal("defaultAssets/uiskin.json")));
        amountOfFootmansToSplitTF = new TextField("0", new Skin(Gdx.files.internal("defaultAssets/uiskin.json")));
        amountOfCavalryToSplitTF = new TextField("0", new Skin(Gdx.files.internal("defaultAssets/uiskin.json")));

        resourceTileBuildingCostInfo = new Label("", labelStyleGold);
        resourceBuildingInfo = new Label("", labelStyleBlack);
        buildResourceBuildingBtn = new TextButton("Build", emptyTextButtonStyle);
        buildResourceBuildingBackBtn = new TextButton("Back", emptyTextButtonStyle);


        inGameMenuBack = new TextButton("Back", emptyTextButtonStyle);
        menuExitToMainMenu = new TextButton("Exit to main menu", emptyTextButtonStyle);
        closeGame = new TextButton("Close game", emptyTextButtonStyle);
        menuSaveGame = new TextButton("Save", emptyTextButtonStyle);
        addNewPlayerButton = new TextButton("Add new player", emptyTextButtonStyle);

        saveToSlot1 = new TextButton("Save to slot 1", emptyTextButtonStyle);
        saveToSlot2 = new TextButton("Save to slot 2", emptyTextButtonStyle);
        saveToSlot3 = new TextButton("Save to slot 3", emptyTextButtonStyle);
        saveMenuBack = new TextButton("Back", emptyTextButtonStyle);


        //Gdx.input.setInputProcessor(stage);

    }

    private String calculateArmyInfo(int amountOfUnit) {
        if (amountOfUnit < 5) {
            return "Very small";
        } else if (amountOfUnit < 10) {
            return "Small";
        } else if (amountOfUnit < 20) {
            return "Medium";
        } else if (amountOfUnit < 30) {
            return "Large";
        } else if (amountOfUnit < 40) {
            return "Very large";
        } else {
            return "Huge";
        }

    }

    private void hoveringLabelSetupOnEnter(String infoOnHoveringLabel) {
        isMouseOnButton = true;
        hoveringLabel.setText(infoOnHoveringLabel);
    }

    private void hoveringLabelSetupOnExit() {
        isMouseOnButton = false;
        hoveringLabel.setText("");
    }

    public void selectionHudReset() {
        isTownMenu = false;
        isBuildingMenu = false;
        isArmyLeaveMenu = false;
        isRecruitmentMenu = false;
        isBuyTileMode = false;
        isBuyTileMenu = false;
        showMainButtons();
    }


    public void setPlayerWhoseTurnIs(Player playerWhoseTurnIs) {
        this.playerWhoseTurnIs = playerWhoseTurnIs;
    }

    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    private void setButtonsListenersHud() {
        endTurnBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                gameSession.endTurn();
            }
        });

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
        buyTilesBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                showBuyTilesButtons();
            }
        });

        buyTilesMenuBackBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                isBuyTileMode = false;
                showTownButtons();
            }
        });

        buildMenuBackBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                isBuildingMenu = false;
                showTownButtons();
            }
        });

        leaveArmyFromTownBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                showArmyLeaveMenu();
            }
        });

        backRecruitmentMenuBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                isRecruitmentMenu = false;
                showTownButtons();
            }
        });

        recruitBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                int amountOfArchers, amountOfFootmans, amountOfCavalry;
                try {
                    amountOfArchers = Integer.parseInt(amountOfArchersToRecruitTF.getText());
                    amountOfFootmans = Integer.parseInt(amountOfFootmansToRecruitTF.getText());
                    amountOfCavalry = Integer.parseInt(amountOfCavalryToRecruitTF.getText());
                    gameSession.recruit(amountOfArchers, amountOfFootmans, amountOfCavalry, (TownTile) gameSession.getSelectedTile());
                } catch (NumberFormatException e) {
                }
            }
        });

        leaveTownBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                try {
                    int archersLeaving = Integer.parseInt(amountOfArchersToLeaveTown.getText());
                    int footmansLeaving = Integer.parseInt(amountOfFootmansToLeaveTown.getText());
                    int cavalryLeaving = Integer.parseInt(amountOfCavalryToLeaveTown.getText());

                    gameSession.leaveTheTownWithArmy(archersLeaving, footmansLeaving, cavalryLeaving);
                } catch (NumberFormatException e) {

                }
            }
        });

        leaveTownBackBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                isArmyLeaveMenu = false;
                showTownButtons();
            }
        });

        buildCastleBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                gameSession.getPlayerWhoseTurnIs().build((TownTile) gameSession.getSelectedTile(), GameplayConstants.castleIndex);
            }

            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                TownTile tmpSelectedTown = (TownTile) gameSession.getSelectedTile();
                hoveringLabelSetupOnEnter(" Next level cost: " + tmpSelectedTown.buildingTotalCostToString(GameplayConstants.castleIndex));
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                hoveringLabelSetupOnExit();
            }
        });
        buildTownHallBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                gameSession.getPlayerWhoseTurnIs().build((TownTile) gameSession.getSelectedTile(), GameplayConstants.townhallIndex);
            }

            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                TownTile tmpSelectedTown = (TownTile) gameSession.getSelectedTile();
                hoveringLabelSetupOnEnter(" Next level cost: " + tmpSelectedTown.buildingTotalCostToString(GameplayConstants.townhallIndex));
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                hoveringLabelSetupOnExit();
            }
        });
        buildBarracksBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                gameSession.getPlayerWhoseTurnIs().build((TownTile) gameSession.getSelectedTile(), GameplayConstants.barracksIndex);
            }

            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                TownTile tmpSelectedTown = (TownTile) gameSession.getSelectedTile();
                hoveringLabelSetupOnEnter(" Next level cost: " + tmpSelectedTown.buildingTotalCostToString(GameplayConstants.barracksIndex));
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                hoveringLabelSetupOnExit();
            }
        });
        buildStablesButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                gameSession.getPlayerWhoseTurnIs().build((TownTile) gameSession.getSelectedTile(), GameplayConstants.stablesIndex);
            }

            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                TownTile tmpSelectedTown = (TownTile) gameSession.getSelectedTile();
                hoveringLabelSetupOnEnter(" Next level cost: " + tmpSelectedTown.buildingTotalCostToString(GameplayConstants.stablesIndex));
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                hoveringLabelSetupOnExit();
            }
        });
        buildHousesBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                gameSession.getPlayerWhoseTurnIs().build((TownTile) gameSession.getSelectedTile(), GameplayConstants.housesIndex);
            }

            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                TownTile tmpSelectedTown = (TownTile) gameSession.getSelectedTile();
                hoveringLabelSetupOnEnter(" Next level cost: " + tmpSelectedTown.buildingTotalCostToString(GameplayConstants.housesIndex));
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                hoveringLabelSetupOnExit();
            }
        });
        buildWallBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                gameSession.getPlayerWhoseTurnIs().build((TownTile) gameSession.getSelectedTile(), GameplayConstants.wallIndex);
            }

            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                TownTile tmpSelectedTown = (TownTile) gameSession.getSelectedTile();
                hoveringLabelSetupOnEnter(" Next level cost: " + tmpSelectedTown.buildingTotalCostToString(GameplayConstants.wallIndex));
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                hoveringLabelSetupOnExit();
            }
        });
        buildBankBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                gameSession.getPlayerWhoseTurnIs().build((TownTile) gameSession.getSelectedTile(), GameplayConstants.bankIndex);
            }

            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                TownTile tmpSelectedTown = (TownTile) gameSession.getSelectedTile();
                hoveringLabelSetupOnEnter(" Next level cost: " + tmpSelectedTown.buildingTotalCostToString(GameplayConstants.bankIndex));
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                hoveringLabelSetupOnExit();
            }
        });

        buyTileYesButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                gameSession.buyTile();
                isBuyTileMenu = false;
                isBuyTileMode = true;
                showBuyTilesButtons();
            }
        });

        buyTileNoButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                isBuyTileMenu = false;
                gameSession.setTileToBuySelected(false);
                gameSession.setSelectedTileToBuy(null);
                showBuyTilesButtons();
            }
        });

        buildResourceBuildingBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                gameSession.buildResourceTile();
            }
        });

        buildResourceBuildingBackBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                isResourceTileMenu = false;
                gameSession.setTileSelected(false);
                gameSession.setSelectedTile(null);
                showMainButtons();
            }
        });

        menuBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                gameSession.setSelectedTile(null);
                gameSession.setTileSelected(false);
                showMenuButtons();
            }
        });

        menuSaveGame.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                showSaveMenu();
            }
        });

        saveMenuBack.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                showMenuButtons();
            }
        });

        inGameMenuBack.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                showMainButtons();
            }
        });

        saveToSlot1.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                gameSession.saveManager.saveGame("../saves/savegame1/");
            }
        });

        saveToSlot2.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                gameSession.saveManager.saveGame("../saves/savegame2/");
            }
        });

        saveToSlot3.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                gameSession.saveManager.saveGame("../saves/savegame3/");
            }
        });

        closeGame.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });

        menuExitToMainMenu.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                gameSession.inGame = false;
            }
        });

        addNewPlayerButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                gameSession.addNewPlayer();
            }
        });

        armySplitButtonMenu.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                showSplitArmyMenu();
            }
        });

        armyBackButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                showMainButtons();
                isSplitArmyMenu = false;
                gameSession.setTileSelected(false);
                gameSession.setSelectedTile(null);
            }
        });

        armySplitButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                int amountOfArchers, amountOfFootmans, amountOfCavalry;
                try {
                    amountOfArchers = Integer.parseInt(amountOfArchersToSplitTF.getText());
                    amountOfFootmans = Integer.parseInt(amountOfFootmansToSplitTF.getText());
                    amountOfCavalry = Integer.parseInt(amountOfCavalryToSplitTF.getText());
                    gameSession.splitArmy((Army)gameSession.getSelectedTile(), amountOfArchers, amountOfFootmans, amountOfCavalry);
                } catch (NumberFormatException e) {
                }
            }
        });
    }


}
