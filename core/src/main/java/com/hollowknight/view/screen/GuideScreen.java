package com.hollowknight.view.screen;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.hollowknight.HollowKnightGame;


public class GuideScreen extends MenuScreen {

    public GuideScreen(HollowKnightGame game) {
        super(game, true);
        buildUi();
    }

    @Override
    protected void buildUi() {
        Table root = new Table();
        root.setFillParent(true);
        stage.addActor(root);

        Label title = new Label("GUIDE", skin(), "title");
        title.setFontScale(1.4f);
        root.add(title).padBottom(24).row();

        Table content = new Table();
        content.top().left();

        section(content, "Controls");
        line(content, "Move", "Left / Right Arrows");
        line(content, "Jump / Double Jump", "Z");
        line(content, "Dash", "C");
        line(content, "Attack (Nail)", "X");
        line(content, "Pogo", "Down + X (in air)");
        line(content, "Focus / Heal", "Hold A");
        line(content, "Cast Spell", "S");
        line(content, "Pause", "Esc");
        line(content, "Inventory", "I");

        section(content, "Abilities");
        line(content, "Masks (HP)", "Your health. Lose one per hit; die when empty.");
        line(content, "Soul", "Fills as you hit enemies with the nail.");
        line(content, "Focus", "Spend Soul to heal a Mask (hold still).");
        line(content, "Vengeful Spirit", "Spend Soul to fire a damaging projectile.");
        line(content, "Howling Wraiths", "Spend Soul for an upward burst of damage.");
        line(content, "Pogo", "Hold Down + Attack in the air to bounce off enemies/spikes.");

        section(content, "Charms & Checkpoints");
        line(content, "Charms", "Equip up to 3 (by notch cost) in the Inventory (I) for passive buffs.");
        line(content, "Checkpoints", "Touching one sets your respawn point when you hit a hazard.");
        line(content, "Death", "Losing all Masks sends you back to the room's start and resets it.");

        section(content, "Cheat Codes (hold Left Ctrl)");
        line(content, "Ctrl + B", "Teleport to boss arena");
        line(content, "Ctrl + N", "Noclip (fly through walls)");
        line(content, "Ctrl + H", "Emergency heal");
        line(content, "Ctrl + R", "Refill Soul");
        line(content, "Ctrl + G", "God mode (no damage)");
        line(content, "Ctrl + K", "Kill all enemies on screen");

        ScrollPane scroll = new ScrollPane(content, skin());
        scroll.setFadeScrollBars(false);
        root.add(scroll).width(720).height(440).padBottom(20).row();

        TextButton back = new TextButton("Back", skin());
        back.addListener(new ClickListener() {
            @Override public void clicked(InputEvent e, float x, float y) {
                game.setScreen(new MainMenuScreen(game));
            }
        });
        root.add(back).width(200).height(44).row();
    }

    private void section(Table t, String heading) {
        Label l = new Label(heading, skin(), "title");
        l.setFontScale(1.1f);
        t.add(l).left().padTop(18).padBottom(6).colspan(2).row();
    }

    private void line(Table t, String key, String desc) {
        t.add(new Label(key, skin())).left().padRight(24).padBottom(4).top();
        Label d = new Label(desc, skin(), "dim");
        d.setWrap(true);
        t.add(d).left().width(440).padBottom(4).row();
    }
}