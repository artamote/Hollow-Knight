package com.hollowknight.view.screen;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.hollowknight.HollowKnightGame;

public class VictoryScreen extends MenuScreen {

    private final int deaths;
    private final int kills;
    private final float timeElapsed;

    public VictoryScreen(HollowKnightGame game, int deaths, int kills, float timeElapsed) {
        super(game, true);
        this.deaths = deaths;
        this.kills = kills;
        this.timeElapsed = timeElapsed;
        buildUi();
    }

    @Override
    protected void buildUi() {
        Table root = new Table();
        root.setFillParent(true);
        stage.addActor(root);

        Label title = new Label("VICTORY", skin(), "title");
        title.setFontScale(1.8f);
        root.add(title).colspan(2).padBottom(14).row();

        Label sub = new Label("The False Knight has been defeated", skin(), "dim");
        root.add(sub).colspan(2).padBottom(40).row();

        int minutes = (int) (timeElapsed / 60f);
        int seconds = (int) (timeElapsed % 60f);
        String timeStr = String.format("%d:%02d", minutes, seconds);

        statLine(root, "Deaths", String.valueOf(deaths));
        statLine(root, "Enemies Killed", String.valueOf(kills));
        statLine(root, "Time", timeStr);

        TextButton newGame = new TextButton("New Game", skin());
        newGame.addListener(new ClickListener() {
            @Override public void clicked(InputEvent e, float x, float y) {
                game.setScreen(new StartGameScreen(game));
            }
        });
        root.add(newGame).colspan(2).width(260).height(48).padTop(30).padBottom(14).row();

        TextButton menu = new TextButton("Main Menu", skin());
        menu.addListener(new ClickListener() {
            @Override public void clicked(InputEvent e, float x, float y) {
                game.setScreen(new MainMenuScreen(game));
            }
        });
        root.add(menu).colspan(2).width(260).height(48).row();
        game.assetLoader.musicManager.playTrack("music/champion.mp3");
    }

    private void statLine(Table t, String label, String value) {
        t.add(new Label(label + ":", skin(), "dim")).right().padRight(16).padBottom(10);
        t.add(new Label(value, skin())).left().padBottom(10).row();
    }
}