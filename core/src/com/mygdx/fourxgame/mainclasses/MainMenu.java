package com.mygdx.fourxgame.mainclasses;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.fourxgame.controllers.GameController;

public class MainMenu {

    private SpriteBatch batch;
    protected Stage stage;
    private Viewport viewport;
    private OrthographicCamera camera;
    private Button newGameBtn;
    private Button loadGameBtn;
    private Button exitGameBtn;
    private GameRenderer gameRenderer;
    private GameController gameController;
    private TextField numberOfPlayersTextField;
    private Button startGameBtn;
    private Button backBtn;
    private int numberOfPlayers;
    public boolean loading = false;


    public MainMenu(SpriteBatch batch, GameRenderer gameRenderer, GameController gameController) {
        this.batch = batch;
        this.gameRenderer = gameRenderer;
        this.gameController = gameController;
        init();
        initMenuElements();
        setButtonsListeners();
        showFirstMenu();
    }

    private void init() {
        camera = new OrthographicCamera();
        viewport = new FitViewport(Constants.MAINMENU_VIEWPORT_WIDTH, Constants.MAINMENU_VIEWPORT_HEIGHT, camera);
        viewport.apply();
        stage = new Stage(viewport, batch);
        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
        camera.update();
    }

    private void initMenuElements() {
        Texture textureBtnUp = new Texture(Gdx.files.internal("menubtnUp.png"));
        Texture textureBtnDown = new Texture(Gdx.files.internal("menubtnDown.png"));

        Drawable drawableTextureBtnUp = new TextureRegionDrawable(new TextureRegion(textureBtnUp));
        Drawable drawableTextureBtnDown = new TextureRegionDrawable(new TextureRegion(textureBtnDown));

        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.up = drawableTextureBtnUp;
        textButtonStyle.down = drawableTextureBtnDown;
        textButtonStyle.font = new BitmapFont();

        newGameBtn = new TextButton("New Game", textButtonStyle);
        loadGameBtn = new TextButton("Load Game", textButtonStyle);
        exitGameBtn = new TextButton("Exit", textButtonStyle);

        startGameBtn = new TextButton("Start Game", textButtonStyle);
        backBtn = new TextButton("Back", textButtonStyle);

        String text = "2";
        numberOfPlayers = Integer.parseInt(text);
        numberOfPlayersTextField = new TextField(text, new Skin(Gdx.files.internal("defaultAssets/uiskin.json")));

        Gdx.input.setInputProcessor(stage);
    }

    private void showFirstMenu() {
        stage.clear();
        Table buttonTable = new Table();
        buttonTable.center();
        buttonTable.setFillParent(true);


        buttonTable.add(newGameBtn).pad(5);
        buttonTable.row();

        buttonTable.add(loadGameBtn).pad(5);
        buttonTable.row();

        buttonTable.add(exitGameBtn).pad(5);
        buttonTable.row();


        stage.addActor(buttonTable);
    }

    private void showSecondMenu() {
        stage.clear();

        Label whatToDoInfo = new Label("Insert number of players: <2;50>", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        whatToDoInfo.setFontScale(2f);

        Table startGameTable = new Table();
        startGameTable.center();
        startGameTable.setFillParent(true);

        startGameTable.add(whatToDoInfo).pad(10);
        startGameTable.row();
        startGameTable.add(numberOfPlayersTextField).pad(5);
        startGameTable.add(startGameBtn).pad(5);
        startGameTable.row();

        backBtn.setPosition(20, 20);

        stage.addActor(backBtn);
        stage.addActor(startGameTable);
    }

    private void loadingScreen() {
        loading = true;
        stage.clear();

        Label loadingLabel = new Label("LOADING...", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        loadingLabel.setFontScale(2f);
        Table loadingTable = new Table();
        loadingTable.center();
        loadingTable.setFillParent(true);
        loadingTable.add(loadingLabel);

        stage.addActor(loadingTable);

    }

    private void initButtons() {

        stage.clear();
        Texture textureBtnUp = new Texture(Gdx.files.internal("menubtnUp.png"));
        Texture textureBtnDown = new Texture(Gdx.files.internal("menubtnDown.png"));

        Drawable drawableTextureBtnUp = new TextureRegionDrawable(new TextureRegion(textureBtnUp));
        Drawable drawableTextureBtnDown = new TextureRegionDrawable(new TextureRegion(textureBtnDown));

        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.up = drawableTextureBtnUp;
        textButtonStyle.down = drawableTextureBtnDown;
        textButtonStyle.font = new BitmapFont();

        newGameBtn = new TextButton("New Game", textButtonStyle);
        loadGameBtn = new TextButton("Load Game", textButtonStyle);
        exitGameBtn = new TextButton("Exit", textButtonStyle);

        Table buttonTable = new Table();
        buttonTable.center();
        buttonTable.setFillParent(true);


        buttonTable.add(newGameBtn).pad(5);
        buttonTable.row();

        buttonTable.add(loadGameBtn).pad(5);
        buttonTable.row();

        buttonTable.add(exitGameBtn).pad(5);
        buttonTable.row();


        stage.addActor(buttonTable);
        Gdx.input.setInputProcessor(stage);
    }

    private void newGameConfigMenu() {
        stage.clear();

        Texture textureBtnUp = new Texture(Gdx.files.internal("menubtnUp.png"));
        Texture textureBtnDown = new Texture(Gdx.files.internal("menubtnDown.png"));

        Drawable drawableTextureBtnUp = new TextureRegionDrawable(new TextureRegion(textureBtnUp));
        Drawable drawableTextureBtnDown = new TextureRegionDrawable(new TextureRegion(textureBtnDown));

        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.up = drawableTextureBtnUp;
        textButtonStyle.down = drawableTextureBtnDown;
        textButtonStyle.font = new BitmapFont();

        startGameBtn = new TextButton("Start Game", textButtonStyle);
        backBtn = new TextButton("Back", textButtonStyle);

        String text = "2";
        numberOfPlayers = Integer.parseInt(text);
        numberOfPlayersTextField = new TextField(text, new Skin(Gdx.files.internal("defaultAssets/uiskin.json")));

        Table startGameTable = new Table();
        startGameTable.center();
        startGameTable.setFillParent(true);

        startGameTable.add(numberOfPlayersTextField).pad(5);
        startGameTable.add(startGameBtn).pad(5);
        startGameTable.row();
        startGameTable.add(backBtn).pad(5);


        stage.addActor(startGameTable);

    }

    private void setButtonsListeners() {
        newGameBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                showSecondMenu();
            }
        });
        loadGameBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("LOADING!!!");
            }
        });
        exitGameBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });

        startGameBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {

                numberOfPlayers = Integer.parseInt(numberOfPlayersTextField.getText());
                if (numberOfPlayers >= 2 && numberOfPlayers <= 50) {
                    loadingScreen();
                    Gdx.app.postRunnable(() -> {
                        gameController.startNewGame(numberOfPlayers);
                        gameRenderer.startNewGame();
                    });

                }
            }
        });

        backBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                showFirstMenu();
            }
        });
    }

}
