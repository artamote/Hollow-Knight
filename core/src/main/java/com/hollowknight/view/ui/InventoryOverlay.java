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
import com.hollowknight.model.charm.Charm;
import com.hollowknight.model.charm.CharmManager;
import com.hollowknight.view.screen.GameScreen;


public class InventoryOverlay {

    private final HollowKnightGame game;
    private final GameScreen gameScreen;
    private final Stage stage;
    private final Texture scrimTex;

    private Label notchLabel;
    private Label descName;
    private Label descText;
    private Label hint;

    public InventoryOverlay(HollowKnightGame game, GameScreen gameScreen) {
        this.game = game;
        this.gameScreen = gameScreen;
        this.stage = new Stage(new ScreenViewport(), game.batch);
        this.scrimTex = solidTexture(new Color(0f, 0f, 0f, 0.7f));
    }

    public Stage getStage() { return stage; }

    public void rebuild() {
        stage.clear();

        Image scrim = new Image(scrimTex);
        scrim.setFillParent(true);
        stage.addActor(scrim);

        Table root = new Table();
        root.setFillParent(true);
        root.pad(30);
        stage.addActor(root);

        Label title = new Label("INVENTORY", game.assetLoader.menuSkin, "title");
        title.setFontScale(1.4f);
        root.add(title).colspan(2).padBottom(16).row();

        final CharmManager charms = gameScreen.getController().model.charms;
        notchLabel = new Label("", game.assetLoader.menuSkin, "dim");
        updateNotchLabel(charms);
        root.add(notchLabel).colspan(2).padBottom(30).row();


        Table grid = new Table();
        int col = 0;
        for (final Charm c : Charm.values()) {
            final TextButton btn = new TextButton(c.name, game.assetLoader.menuSkin);
            btn.setChecked(charms.has(c));

            btn.getLabel().setWrap(true);
            btn.getLabel().setAlignment(com.badlogic.gdx.utils.Align.center);
            btn.addListener(new ClickListener() {
                @Override public void clicked(InputEvent e, float x, float y) {
                    boolean wasEquipped = charms.has(c);
                    boolean changed = charms.toggle(c);
                    if (!wasEquipped && !changed) {

                        hint.setText("Not enough notches for " + c.name + ".");
                    } else {
                        hint.setText("");
                    }
                    btn.setChecked(charms.has(c));
                    updateNotchLabel(charms);
                    showDescription(c);
                }
            });

            btn.addListener(new ClickListener() {
                @Override public void enter(InputEvent e, float x, float y, int pointer, com.badlogic.gdx.scenes.scene2d.Actor from) {
                    showDescription(c);
                }
            });

            Table cell = new Table();
            if (game.assetLoader.charmIcons.has(c)) {
                Image icon = new Image(game.assetLoader.charmIcons.get(c));
                cell.add(icon).size(56, 56).padBottom(8).row();
            }
            cell.add(btn).width(230).height(64);

            grid.add(cell).pad(16);
            if (++col % 2 == 0) grid.row();
        }
        root.add(grid).padRight(70).top();


        Table desc = new Table();
        desc.top().left();
        descName = new Label("", game.assetLoader.menuSkin, "title");
        descName.setFontScale(1.1f);
        descText = new Label("Select a charm.", game.assetLoader.menuSkin, "dim");
        descText.setWrap(true);
        hint = new Label("", game.assetLoader.menuSkin, "dim");
        hint.setWrap(true);
        desc.add(descName).left().width(300).padBottom(16).row();
        desc.add(descText).left().width(300).padBottom(30).row();
        desc.add(hint).left().width(300).row();
        root.add(desc).top();
        root.row();

        TextButton close = new TextButton("Close (I)", game.assetLoader.menuSkin);
        close.addListener(new ClickListener() {
            @Override public void clicked(InputEvent e, float x, float y) {
                gameScreen.setInventoryOpen(false);
            }
        });
        root.add(close).colspan(2).width(200).height(44).padTop(30).row();
    }

    private void showDescription(Charm c) {
        descName.setText(c.name);
        descText.setText(c.description + "  (" + c.notchCost + " notch)");
    }

    private void updateNotchLabel(CharmManager charms) {
        notchLabel.setText("Notches:  " + charms.getUsedNotches() + " / " + charms.getMaxNotches());
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