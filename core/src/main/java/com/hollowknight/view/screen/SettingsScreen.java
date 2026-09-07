package com.hollowknight.view.screen;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.hollowknight.HollowKnightGame;
import com.hollowknight.util.GameSettings;


public class SettingsScreen extends MenuScreen {


    public interface BackFactory {
        Screen create(HollowKnightGame game);
    }

    private final BackFactory backFactory;

    public SettingsScreen(HollowKnightGame game, BackFactory backFactory) {
        super(game, true);
        this.backFactory = backFactory;
        buildUi();
    }

    @Override
    protected void buildUi() {
        final GameSettings s = game.settings;

        Table root = new Table();
        root.setFillParent(true);
        stage.addActor(root);

        Label title = new Label("SETTINGS", skin(), "title");
        title.setFontScale(1.4f);
        root.add(title).colspan(2).padBottom(40).row();

        
        root.add(new Label("Music Volume", skin())).left().padRight(20);
        final Slider musicSlider = new Slider(0f, 1f, 0.05f, false, skin());
        musicSlider.setValue(s.getRawMusicVolume());
        musicSlider.addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent e, com.badlogic.gdx.scenes.scene2d.Actor a) {
                s.setMusicVolume(musicSlider.getValue());
            }
        });
        root.add(musicSlider).width(300).padBottom(16).row();

        
        root.add(new Label("Mute Music", skin())).left().padRight(20);
        final SelectBox<String> muteMusic = new SelectBox<>(skin());
        muteMusic.setItems("Off", "On");
        muteMusic.setSelected(s.isMusicMuted() ? "On" : "Off");
        muteMusic.addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent e, com.badlogic.gdx.scenes.scene2d.Actor a) {
                s.setMusicMuted("On".equals(muteMusic.getSelected()));
            }
        });
        root.add(muteMusic).width(120).left().padBottom(16).row();

        
        root.add(new Label("SFX Volume", skin())).left().padRight(20);
        final Slider sfxSlider = new Slider(0f, 1f, 0.05f, false, skin());
        sfxSlider.setValue(s.getRawSfxVolume());
        sfxSlider.addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent e, com.badlogic.gdx.scenes.scene2d.Actor a) {
                s.setSfxVolume(sfxSlider.getValue());
            }
        });
        root.add(sfxSlider).width(300).padBottom(16).row();

        
        root.add(new Label("Mute SFX", skin())).left().padRight(20);
        final SelectBox<String> muteSfx = new SelectBox<>(skin());
        muteSfx.setItems("Off", "On");
        muteSfx.setSelected(s.isSfxMuted() ? "On" : "Off");
        muteSfx.addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent e, com.badlogic.gdx.scenes.scene2d.Actor a) {
                s.setSfxMuted("On".equals(muteSfx.getSelected()));
            }
        });
        root.add(muteSfx).width(120).left().padBottom(16).row();

        
        root.add(new Label("Brightness", skin())).left().padRight(20);
        final Slider brightnessSlider = new Slider(0.5f, 1.5f, 0.05f, false, skin());
        brightnessSlider.setValue(s.getBrightness());
        brightnessSlider.addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent e, com.badlogic.gdx.scenes.scene2d.Actor a) {
                s.setBrightness(brightnessSlider.getValue());
            }
        });
        root.add(brightnessSlider).width(300).padBottom(30).row();

        
        TextButton reset = new TextButton("Reset to Defaults", skin());
        reset.addListener(new ClickListener() {
            @Override public void clicked(InputEvent e, float x, float y) {
                s.resetToDefaults();
                game.setScreen(new SettingsScreen(game, backFactory));
            }
        });
        root.add(reset).colspan(2).width(280).height(44).padBottom(12).row();

        TextButton back = new TextButton("Back", skin());
        back.addListener(new ClickListener() {
            @Override public void clicked(InputEvent e, float x, float y) {
                game.setScreen(backFactory.create(game));
            }
        });
        root.add(back).colspan(2).width(200).height(44).row();
    }
}