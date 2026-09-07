package com.hollowknight.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.assets.loaders.TextureLoader.TextureParameter;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.hollowknight.audio.MusicManager;
import com.hollowknight.audio.SfxLibrary;
import com.hollowknight.view.anim.CharmIcons;
import com.hollowknight.view.anim.EnemyAnimations;
import com.hollowknight.view.anim.HudAnimations;
import com.hollowknight.view.anim.PlayerAnimations;
import com.hollowknight.view.anim.SpellAnimations;
import com.hollowknight.view.ui.MenuSkinFactory;


public class AssetLoader {

    public final AssetManager manager = new AssetManager();
    public BitmapFont defaultFont;
    public PlayerAnimations playerAnimations;
    public EnemyAnimations enemyAnimations;
    public HudAnimations hudAnimations;
    public CharmIcons charmIcons;
    public SpellAnimations spellAnimations;
    public SfxLibrary sfxLibrary;
    public MusicManager musicManager;
    public Skin menuSkin;
    private static final String MENU_SKIN_JSON = "ui/hollow_knight_skin.json";
    public Texture menuBackground;
    private static final String MENU_BACKGROUND = "ui/menu_background.png";

    private static final String[] KNIGHT_SHEETS = {
            "sprites/knight/idle.png",
            "sprites/knight/run.png",
            "sprites/knight/airborne.png",
            "sprites/knight/dash.png",
            "sprites/knight/double_jump.png",
            "sprites/knight/slash.png",
            "sprites/knight/down_slash.png",
            "sprites/knight/focus.png",
            "sprites/knight/vengeful_spirit_cast.png",
            "sprites/knight/howling_wraiths_cast.png",
            "sprites/knight/effects/slash_effect.png",
            "sprites/knight/effects/down_slash_effect.png",
            "sprites/knight/effects/dash_effect.png",
            "sprites/knight/effects/up_slash_effect.png",
            "sprites/knight/idle_hurt.png",
            "sprites/knight/death.png",
    };

    private static final String[] ENEMY_SHEETS = {
            "sprites/enemies/crawlid/walk.png",
            "sprites/enemies/crawlid/death.png",
            "sprites/enemies/mosquito/idle.png",
            "sprites/enemies/mosquito/attack.png",
            "sprites/enemies/mosquito/death.png",
            "sprites/enemies/husk/idle.png",
            "sprites/enemies/husk/walk.png",
            "sprites/enemies/husk/attack.png",
            "sprites/enemies/husk/death.png",
            "sprites/enemies/crystal_guardian/idle.png",
            "sprites/enemies/crystal_guardian/run.png",
            "sprites/enemies/crystal_guardian/shoot.png",
            "sprites/enemies/crystal_guardian/death.png",
            "sprites/enemies/crystal_guardian/laser_beam.png",
            "sprites/enemies/mosscreep/walk.png",
            "sprites/enemies/mosscreep/death.png",
            "sprites/enemies/mossfly/appear.png",
            "sprites/enemies/mossfly/fly.png",
            "sprites/enemies/mossfly/death.png",
            "sprites/enemies/false_knight/idle.png",
            "sprites/enemies/false_knight/run.png",
            "sprites/enemies/false_knight/jump_antic.png",
            "sprites/enemies/false_knight/jump.png",
            "sprites/enemies/false_knight/jump_attack.png",
            "sprites/enemies/false_knight/land.png",
            "sprites/enemies/false_knight/attack_antic.png",
            "sprites/enemies/false_knight/attack.png",
            "sprites/enemies/false_knight/attack_recover.png",
            "sprites/enemies/false_knight/stun_recover.png",
            "sprites/enemies/false_knight/stun_idle.png",
            "sprites/enemies/false_knight/death_fall.png",
            "sprites/enemies/false_knight/death_land.png",
            "sprites/enemies/false_knight/shockwave_000.png",
            "sprites/enemies/false_knight/shockwave_001.png",
            "sprites/enemies/false_knight/shockwave_002.png",
            "sprites/enemies/false_knight/shockwave_003.png",
            "sprites/enemies/false_knight/shockwave_004.png",
    };

    private static final String[] HUD_SHEETS = {
            "sprites/hud/HealthBar.png",
            "sprites/hud/BreakHealth.png",
            "sprites/hud/FilledHealthShine.png",
            "sprites/hud/HealthRefill.png",
            "sprites/hud/soul_fill_00.png", "sprites/hud/soul_fill_01.png",
            "sprites/hud/soul_fill_02.png", "sprites/hud/soul_fill_03.png",
            "sprites/hud/soul_fill_04.png", "sprites/hud/soul_fill_05.png",
            "sprites/hud/soul_fill_06.png", "sprites/hud/soul_fill_07.png",
            "sprites/hud/soul_fill_08.png", "sprites/hud/soul_fill_09.png",
            "sprites/hud/soul_fill_10.png", "sprites/hud/soul_fill_11.png",
            "sprites/hud/soul_fill_12.png", "sprites/hud/soul_fill_13.png",
            "sprites/hud/soul_fill_14.png", "sprites/hud/soul_fill_15.png",
            "sprites/hud/soul_fill_16.png", "sprites/hud/soul_fill_17.png",
    };

    private static final String[] SPELL_SHEETS = {
            "sprites/spells/soul_ball.png",
            "sprites/spells/soul_scream.png",
            "sprites/spells/blast_soul.png",
    };

    private static final String[] CHARM_ICONS = CharmIcons.allPaths();
    private static final String[] SFX = SfxLibrary.allPaths();
    private static final String[] MUSIC = MusicManager.ALL_PATHS;

    public void loadAll(com.hollowknight.util.GameSettings settings) {
        TextureParameter param = new TextureParameter();
        param.minFilter = TextureFilter.Nearest;
        param.magFilter = TextureFilter.Nearest;

        for (String path : KNIGHT_SHEETS) manager.load(path, Texture.class, param);
        for (String path : ENEMY_SHEETS)  manager.load(path, Texture.class, param);
        for (String path : HUD_SHEETS)    manager.load(path, Texture.class, param);
        for (String path : CHARM_ICONS)   manager.load(path, Texture.class, param);
        for (String path : SPELL_SHEETS)  manager.load(path, Texture.class, param);
        for (String path : SFX)           manager.load(path, com.badlogic.gdx.audio.Sound.class);
        for (String path : MUSIC)         manager.load(path, com.badlogic.gdx.audio.Music.class);

        manager.finishLoading();

        defaultFont = new BitmapFont();
        playerAnimations = new PlayerAnimations(manager);
        enemyAnimations = new EnemyAnimations(manager);
        hudAnimations = new HudAnimations(manager);
        charmIcons = new CharmIcons(manager);
        spellAnimations = new SpellAnimations(manager);
        sfxLibrary = new SfxLibrary(manager);
        musicManager = new MusicManager(manager, settings);

        try {
            menuSkin = new Skin(Gdx.files.internal(MENU_SKIN_JSON));
        } catch (Exception e) {
            Gdx.app.error("AssetLoader",
                    "Could not load " + MENU_SKIN_JSON + " (or its atlas/fonts) -- " +
                            "falling back to the procedural menu skin.", e);
            menuSkin = MenuSkinFactory.build();
        }
        FileHandle bgFile = Gdx.files.internal(MENU_BACKGROUND);
        if (bgFile.exists()) {
            menuBackground = new Texture(bgFile);
            menuBackground.setFilter(TextureFilter.Linear, TextureFilter.Linear);
        } else {
            menuBackground = null;
        }
    }

    public Texture texture(String path) {
        if (!manager.isLoaded(path)) return null;
        return manager.get(path, Texture.class);
    }

    public void dispose() {
        manager.dispose();
        if (defaultFont != null) defaultFont.dispose();
        if (menuSkin != null) menuSkin.dispose();
        if (menuBackground != null) menuBackground.dispose();
        MenuSkinFactory.disposeAll();
    }
}