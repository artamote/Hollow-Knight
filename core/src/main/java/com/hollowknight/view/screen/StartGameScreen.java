package com.hollowknight.view.screen;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.hollowknight.HollowKnightGame;
import com.hollowknight.model.save.SaveData;
import com.hollowknight.util.Constants;


public class StartGameScreen extends MenuScreen {

    private Label statusLabel;

    public StartGameScreen(HollowKnightGame game) {
        super(game, true);
        buildUi();
    }

    @Override
    protected void buildUi() {
        Table root = new Table();
        root.setFillParent(true);
        stage.addActor(root);

        Label title = new Label("START GAME", skin(), "title");
        title.setFontScale(1.4f);
        root.add(title).colspan(3).padBottom(30).row();


        TextButton newGameBtn = new TextButton("New Game", skin());
        newGameBtn.addListener(new ClickListener() {
            @Override public void clicked(InputEvent e, float x, float y) {
                startNewGameInFirstEmptySlot();
            }
        });
        root.add(newGameBtn).colspan(3).width(260).height(52).padBottom(8).row();

        statusLabel = new Label("", skin(), "dim");
        root.add(statusLabel).colspan(3).padBottom(20).row();

        for (int i = 0; i < Constants.SAVE_SLOTS; i++) {
            addSlotRow(root, i);
        }

        TextButton back = new TextButton("Back", skin());
        back.addListener(new ClickListener() {
            @Override public void clicked(InputEvent e, float x, float y) {
                game.setScreen(new MainMenuScreen(game));
            }
        });
        root.add(back).colspan(3).width(200).height(44).padTop(20).row();
    }

    private void startNewGameInFirstEmptySlot() {
        for (int slot = 0; slot < Constants.SAVE_SLOTS; slot++) {
            if (!game.saveManager.slotExists(slot)) {
                GameScreen gs = new GameScreen(game);
                gs.setSaveSlot(slot);
                game.setScreen(gs);
                return;
            }
        }
        statusLabel.setText("All 4 slots are full -- delete one below first.");
    }

    private void addSlotRow(Table root, final int slot) {
        SaveData data = game.saveManager.load(slot);
        boolean exists = data != null;

        String label = exists
                ? "Slot " + (slot + 1) + "  —  " + summarize(data)
                : "Slot " + (slot + 1) + "  —  New Game";

        TextButton slotBtn = new TextButton(label, skin());
        slotBtn.getLabel().setWrap(true);

        if (exists) {
            slotBtn.addListener(new ClickListener() {
                @Override public void clicked(InputEvent e, float x, float y) {
                    SaveData fresh = game.saveManager.load(slot);
                    game.setScreen(new GameScreen(game, fresh));
                }
            });
        } else {
            slotBtn.addListener(new ClickListener() {
                @Override public void clicked(InputEvent e, float x, float y) {
                    GameScreen gs = new GameScreen(game);
                    gs.setSaveSlot(slot);
                    game.setScreen(gs);
                }
            });
        }
        root.add(slotBtn).width(440).height(64).padBottom(14);


        if (exists) {
            TextButton del = new TextButton("Delete", skin());
            del.addListener(new ClickListener() {
                @Override public void clicked(InputEvent e, float x, float y) {
                    game.saveManager.deleteSlot(slot);
                    game.setScreen(new StartGameScreen(game));
                }
            });
            root.add(del).width(120).height(64).padBottom(14).padLeft(12);
        } else {
            root.add().width(120);
        }
        root.row();
    }

    private String summarize(SaveData d) {
        int mins = (int) (d.timeElapsed / 60);
        String room = d.currentRoom == null ? "?" : d.currentRoom.replace('_', ' ');
        return "HP " + d.hp + "/" + d.maxHp
                + "   " + d.kills + " kills"
                + "   " + mins + "m"
                + (d.bossDefeated ? "   [Boss Down]" : "")
                + "\n" + room;
    }
}