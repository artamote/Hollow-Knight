package com.hollowknight.view.anim;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.hollowknight.util.Constants;
import com.hollowknight.util.SpriteSheetLoader;


public class SpellAnimations {

    private static final int BALL_FRAME_W = 317;
    private static final int BALL_FRAME_H = 143;
    private static final int SCREAM_FRAME_W = 332;
    private static final int SCREAM_FRAME_H = 306;
    private static final int BLAST_FRAME_W = 272;
    private static final int BLAST_FRAME_H = 289;

    public final Animation<TextureRegion> vengefulSpirit;
    public final Animation<TextureRegion> howlingWraiths;
    public final Animation<TextureRegion> blastSoul;

    public SpellAnimations(AssetManager manager) {
        Texture ballTex = tex(manager, "sprites/spells/soul_ball.png");
        Texture screamTex = tex(manager, "sprites/spells/soul_scream.png");
        Texture blastTex = tex(manager, "sprites/spells/blast_soul.png");

        TextureRegion[] ballFrames = SpriteSheetLoader.sliceAuto(ballTex, BALL_FRAME_W, BALL_FRAME_H);
        vengefulSpirit = new Animation<>(0.04f, ballFrames);
        vengefulSpirit.setPlayMode(Animation.PlayMode.NORMAL);

        TextureRegion[] screamFrames = SpriteSheetLoader.sliceAuto(screamTex, SCREAM_FRAME_W, SCREAM_FRAME_H);
        float screamFrameDuration = Constants.HOWLING_WRAITHS_DURATION / screamFrames.length;
        howlingWraiths = new Animation<>(screamFrameDuration, screamFrames);
        howlingWraiths.setPlayMode(Animation.PlayMode.NORMAL);

        TextureRegion[] blastFrames = SpriteSheetLoader.sliceAuto(blastTex, BLAST_FRAME_W, BLAST_FRAME_H);
        blastSoul = new Animation<>(0.05f, blastFrames); 
        blastSoul.setPlayMode(Animation.PlayMode.NORMAL);
    }

    public static float ballAspect()   { return (float) BALL_FRAME_W / BALL_FRAME_H; }
    public static float screamAspect() { return (float) SCREAM_FRAME_W / SCREAM_FRAME_H; }
    public static float blastAspect()  { return (float) BLAST_FRAME_W / BLAST_FRAME_H; }

    private Texture tex(AssetManager manager, String path) {
        Texture t = manager.get(path, Texture.class);
        t.setFilter(TextureFilter.Linear, TextureFilter.Linear);
        return t;
    }
}