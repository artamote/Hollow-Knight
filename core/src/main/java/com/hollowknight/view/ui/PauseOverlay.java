package com.hollowknight.view.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.hollowknight.HollowKnightGame;
import com.hollowknight.view.screen.GameScreen;
import com.hollowknight.view.screen.MainMenuScreen;
import com.hollowknight.view.screen.SettingsScreen;


public class PauseOverlay {

    private final HollowKnightGame game;
    private final GameScreen gameScreen;
    private final Stage stage;
    private final Texture scrimTex;
    private boolean showingCheats = false;

    public PauseOverlay(HollowKnightGame game, GameScreen gameScreen) {
        this.game = game;
        this.gameScreen = gameScreen;
        this.stage = new Stage(new ScreenViewport(), game.batch);
        this.scrimTex = solidTexture(new Color(0f, 0f, 0f, 0.6f));
    }

    public Stage getStage() { return stage; }

    public void rebuild() {
        showingCheats = false;
        rebuildInternal();
    }

    private void rebuildInternal() {
        stage.clear();

        Image scrim = new Image(scrimTex);
        scrim.setFillParent(true);
        stage.addActor(scrim);

        Table root = new Table();
        root.setFillParent(true);
        stage.addActor(root);

        if (showingCheats) {
            buildCheatsView(root);
            return;
        }

        Label title = new Label("PAUSED", game.assetLoader.menuSkin, "title");
        title.setFontScale(1.5f);
        root.add(title).padBottom(40).row();

        TextButton resume = new TextButton("Resume", game.assetLoader.menuSkin);
        resume.addListener(new ClickListener() {
            @Override public void clicked(InputEvent e, float x, float y) {
                gameScreen.setPaused(false);
            }
        });
        root.add(resume).width(260).height(48).padBottom(14).row();

        TextButton cheats = new TextButton("Cheat Codes", game.assetLoader.menuSkin);
        cheats.addListener(new ClickListener() {
            @Override public void clicked(InputEvent e, float x, float y) {
                showingCheats = true;
                rebuildInternal();
            }
        });
        root.add(cheats).width(260).height(48).padBottom(14).row();

        TextButton settings = new TextButton("Settings", game.assetLoader.menuSkin);
        settings.addListener(new ClickListener() {
            @Override public void clicked(InputEvent e, float x, float y) {
                
                game.setScreen(new SettingsScreen(game, g -> {
                    gameScreen.setPaused(true); 
                    return gameScreen;
                }));
            }
        });
        root.add(settings).width(260).height(48).padBottom(14).row();

        TextButton saveQuit = new TextButton("Save & Quit to Menu", game.assetLoader.menuSkin);
        saveQuit.addListener(new ClickListener() {
            @Override public void clicked(InputEvent e, float x, float y) {
                gameScreen.saveGame();
                game.setScreen(new MainMenuScreen(game));
                gameScreen.dispose();
            }
        });
        root.add(saveQuit).width(260).height(48).row();
    }

    private void buildCheatsView(Table root) {
        Label title = new Label("CHEAT CODES", game.assetLoader.menuSkin, "title");
        title.setFontScale(1.3f);
        root.add(title).colspan(2).padBottom(10).row();

        Label hint = new Label("(hold Left Ctrl)", game.assetLoader.menuSkin, "dim");
        root.add(hint).colspan(2).padBottom(24).row();

        cheatLine(root, "Ctrl + B", "Teleport to boss arena");
        cheatLine(root, "Ctrl + N", "Noclip (fly through walls)");
        cheatLine(root, "Ctrl + H", "Emergency heal");
        cheatLine(root, "Ctrl + R", "Refill Soul");
        cheatLine(root, "Ctrl + G", "God mode (no damage)");
        cheatLine(root, "Ctrl + K", "Kill all enemies on screen");

        TextButton back = new TextButton("Back", game.assetLoader.menuSkin);
        back.addListener(new ClickListener() {
            @Override public void clicked(InputEvent e, float x, float y) {
                showingCheats = false;
                rebuildInternal();
            }
        });
        root.add(back).colspan(2).width(200).height(44).padTop(30).row();
    }

    private void cheatLine(Table t, String key, String desc) {
        t.add(new Label(key, game.assetLoader.menuSkin)).left().padRight(20).padBottom(8);
        t.add(new Label(desc, game.assetLoader.menuSkin, "dim")).left().padBottom(8).row();
    }

    public void render(float delta) {
        stage.act(delta);
        stage.draw();
    }

    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    public void dispose() {
        stage.dispose();
        scrimTex.dispose();
    }

    private Texture solidTexture(Color c) {
        Pixmap pm = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pm.setColor(c);
        pm.fill();
        Texture t = new Texture(pm);
        pm.dispose();
        return t;
    }
}