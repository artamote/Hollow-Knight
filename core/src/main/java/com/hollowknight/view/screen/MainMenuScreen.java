package com.hollowknight.view.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.hollowknight.HollowKnightGame;


public class MainMenuScreen implements Screen {

    private final HollowKnightGame game;
    private final Stage stage;


    private static final float BACKGROUND_DIM = 1f;

    public MainMenuScreen(HollowKnightGame game) {
        this.game = game;
        stage = new Stage(new ScreenViewport(), game.batch);

        if (game.assetLoader.menuBackground != null) {
            Image background = new Image(game.assetLoader.menuBackground);
            background.setScaling(Scaling.fill);
            background.setFillParent(true);
            background.setColor(new Color(BACKGROUND_DIM, BACKGROUND_DIM, BACKGROUND_DIM, 1f));
            stage.addActor(background); 
        }

        Table root = new Table();
        root.setFillParent(true);
        stage.addActor(root);

        Label title = new Label("HOLLOW KNIGHT", game.assetLoader.menuSkin, "title");
        title.setFontScale(1.6f);
        root.add(title).padBottom(50).row();

        TextButton startBtn        = new TextButton("Game Start", game.assetLoader.menuSkin);
        TextButton settingsBtn     = new TextButton("Settings", game.assetLoader.menuSkin);
        TextButton guideBtn        = new TextButton("Guide", game.assetLoader.menuSkin);
        TextButton achievementsBtn = new TextButton("Achievements", game.assetLoader.menuSkin);
        TextButton quitBtn         = new TextButton("Quit Game", game.assetLoader.menuSkin);

        startBtn.addListener(new ClickListener() {
            @Override public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new StartGameScreen(game));
            }
        });
        settingsBtn.addListener(new ClickListener() {
            @Override public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new SettingsScreen(game, MainMenuScreen::new));
            }
        });
        guideBtn.addListener(new ClickListener() {
            @Override public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new GuideScreen(game));
            }
        });
        achievementsBtn.addListener(new ClickListener() {
            @Override public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new AchievementsScreen(game));
            }
        });
        quitBtn.addListener(new ClickListener() {
            @Override public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });

        float btnWidth = 260f, btnHeight = 48f, pad = 12f;
        root.add(startBtn).width(btnWidth).height(btnHeight).padBottom(pad).row();
        root.add(settingsBtn).width(btnWidth).height(btnHeight).padBottom(pad).row();
        root.add(guideBtn).width(btnWidth).height(btnHeight).padBottom(pad).row();
        root.add(achievementsBtn).width(btnWidth).height(btnHeight).padBottom(pad).row();
        root.add(quitBtn).width(btnWidth).height(btnHeight).padBottom(pad).row();

        game.assetLoader.musicManager.playTrack(com.hollowknight.audio.MusicManager.MENU_THEME);
    }

    @Override
    public void render(float delta) {
        game.assetLoader.musicManager.update(delta);
        Gdx.gl.glClearColor(0.03f, 0.03f, 0.05f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage); 
    }

    @Override public void hide() {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void dispose() { stage.dispose(); }
}