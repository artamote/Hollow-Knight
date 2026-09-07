package com.hollowknight.view.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.hollowknight.HollowKnightGame;


public abstract class MenuScreen implements Screen {

    protected final HollowKnightGame game;
    protected final Stage stage;

    private static final float BACKGROUND_DIM = 0.55f;

    protected MenuScreen(HollowKnightGame game, boolean withBackground) {
        this.game = game;
        this.stage = new Stage(new ScreenViewport(), game.batch);

        if (withBackground && game.assetLoader.menuBackground != null) {
            Image background = new Image(game.assetLoader.menuBackground);
            background.setScaling(Scaling.fill);
            background.setFillParent(true);
            background.setColor(new Color(BACKGROUND_DIM, BACKGROUND_DIM, BACKGROUND_DIM, 1f));
            stage.addActor(background);
        }
    }

    protected Skin skin() {
        return game.assetLoader.menuSkin;
    }

    
    protected abstract void buildUi();

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

    @Override
    public void dispose() {
        stage.dispose();
    }
}