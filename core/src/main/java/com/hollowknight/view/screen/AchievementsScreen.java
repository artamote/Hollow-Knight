package com.hollowknight.view.screen;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.hollowknight.HollowKnightGame;
import com.hollowknight.model.AchievementStore;
import com.hollowknight.model.AchievementSystem.Achievement;

import java.util.Set;


public class AchievementsScreen extends MenuScreen {

    public AchievementsScreen(HollowKnightGame game) {
        super(game, true);
        buildUi();
    }

    @Override
    protected void buildUi() {
        Table root = new Table();
        root.setFillParent(true);
        stage.addActor(root);

        Label title = new Label("ACHIEVEMENTS", skin(), "title");
        title.setFontScale(1.4f);
        root.add(title).padBottom(30).row();

        Set<Achievement> unlocked = AchievementStore.loadUnlocked();

        Table list = new Table();
        for (Achievement a : Achievement.values()) {
            boolean got = unlocked.contains(a);

            Label name = new Label(a.title, skin(), got ? "title" : "dim");
            name.setFontScale(1.1f);
            list.add(name).left().padRight(30).padBottom(6).top();

            Label desc = new Label(got ? a.description : "???", skin(), "dim");
            desc.setWrap(true);
            list.add(desc).left().width(400).padBottom(6);

            Label status = new Label(got ? "UNLOCKED" : "LOCKED", skin(), got ? "default" : "dim");
            list.add(status).right().padLeft(20).padBottom(6).row();
        }
        root.add(list).width(760).padBottom(30).row();

        TextButton back = new TextButton("Back", skin());
        back.addListener(new ClickListener() {
            @Override public void clicked(InputEvent e, float x, float y) {
                game.setScreen(new MainMenuScreen(game));
            }
        });
        root.add(back).width(200).height(44).row();
    }
}