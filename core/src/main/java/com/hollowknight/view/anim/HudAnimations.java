package com.hollowknight.view.anim;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.hollowknight.util.SpriteSheetLoader;


public class HudAnimations {

    public final Animation<TextureRegion> breakHealth;
    public final Animation<TextureRegion> filledHealthShine;
    public final Animation<TextureRegion> healthRefill;

    public final TextureRegion[] vesselGrowFrames;
    public final TextureRegion vesselFull;

    private final TextureRegion[] soulFillFrames = new TextureRegion[18];

    private static final int MASK_FRAME_W = 126;
    private static final int MASK_FRAME_H = 167;
    private static final int VESSEL_FRAME_W = 257;
    private static final int VESSEL_FRAME_H = 164;

    public HudAnimations(AssetManager manager) {
        Texture breakTex  = tex(manager, "sprites/hud/BreakHealth.png");
        Texture shineTex  = tex(manager, "sprites/hud/FilledHealthShine.png");
        Texture refillTex = tex(manager, "sprites/hud/HealthRefill.png");
        Texture vesselTex = tex(manager, "sprites/hud/HealthBar.png");

        breakHealth = new Animation<>(0.05f, SpriteSheetLoader.sliceAuto(breakTex, MASK_FRAME_W, MASK_FRAME_H));
        breakHealth.setPlayMode(Animation.PlayMode.NORMAL);

        filledHealthShine = new Animation<>(0.15f, SpriteSheetLoader.sliceAuto(shineTex, MASK_FRAME_W, MASK_FRAME_H));
        filledHealthShine.setPlayMode(Animation.PlayMode.LOOP);

        healthRefill = new Animation<>(0.06f, SpriteSheetLoader.sliceAuto(refillTex, MASK_FRAME_W, MASK_FRAME_H));
        healthRefill.setPlayMode(Animation.PlayMode.NORMAL);

        vesselGrowFrames = SpriteSheetLoader.sliceAuto(vesselTex, VESSEL_FRAME_W, VESSEL_FRAME_H);
        vesselFull = vesselGrowFrames[vesselGrowFrames.length - 1];

        for (int i = 0; i < 18; i++) {
            Texture t = tex(manager, String.format("sprites/hud/soul_fill_%02d.png", i));
            soulFillFrames[i] = new TextureRegion(t);
        }
    }


    public TextureRegion getSoulFillFrame(float fraction) {
        int index = Math.round(fraction * (soulFillFrames.length - 1));
        index = Math.max(0, Math.min(soulFillFrames.length - 1, index));
        return soulFillFrames[index];
    }

    private Texture tex(AssetManager manager, String path) {
        Texture t = manager.get(path, Texture.class);
        t.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
        return t;
    }
}