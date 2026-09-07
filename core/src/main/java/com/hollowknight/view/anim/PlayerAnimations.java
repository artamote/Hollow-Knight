package com.hollowknight.view.anim;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.hollowknight.model.entity.player.PlayerState;
import com.hollowknight.util.SpriteSheetLoader;

import java.util.EnumMap;
import java.util.Map;

    
public class PlayerAnimations {

    private static final int FRAME_W = 349;
    private static final int FRAME_H = 186;

    private static final int SLASH_ALT_FRAME_W = 349;
    private static final int SLASH_ALT_FRAME_H = 186;
    private static final int DOWN_FX_FRAME_W = 273;
    private static final int DOWN_FX_FRAME_H = 209;
    private static final int DASH_FX_FRAME_W = 401;
    private static final int DASH_FX_FRAME_H = 217;

    
    public static class Entry {
        public final Animation<TextureRegion> anim;
        public final boolean loop;
        public Entry(Animation<TextureRegion> anim, boolean loop) { this.anim = anim; this.loop = loop; }
    }

    
    public static class EffectEntry {
        public final Animation<TextureRegion> anim;
        public final boolean loop;
        public final float aspect;
        public EffectEntry(Animation<TextureRegion> anim, boolean loop, float aspect) {
            this.anim = anim; this.loop = loop; this.aspect = aspect;
        }
    }

    private final Map<PlayerState, Entry> entries = new EnumMap<>(PlayerState.class);
    private final Entry upSlashEffectEntry;
    private Entry downSlashEntry; 
    private Entry vengefulSpiritCastEntry; 
    private Entry howlingWraithsCastEntry; 

    private EffectEntry slashEffectAlt;
    private EffectEntry downSlashEffect;
    private EffectEntry dashEffect;

    @SuppressWarnings("unchecked")
    public PlayerAnimations(AssetManager manager) {
        Texture idle       = tex(manager, "sprites/knight/idle.png");
        Texture run        = tex(manager, "sprites/knight/run.png");
        Texture airborne   = tex(manager, "sprites/knight/airborne.png");
        Texture dash       = tex(manager, "sprites/knight/dash.png");
        Texture doubleJump = tex(manager, "sprites/knight/double_jump.png");
        Texture slash      = tex(manager, "sprites/knight/slash.png");
        Texture downSlash  = tex(manager, "sprites/knight/down_slash.png");
        Texture focus      = tex(manager, "sprites/knight/focus.png");
        Texture vengefulCast = tex(manager, "sprites/knight/vengeful_spirit_cast.png");
        Texture howlingCast  = tex(manager, "sprites/knight/howling_wraiths_cast.png");
        Texture upSlash      = tex(manager, "sprites/knight/effects/up_slash_effect.png");
        Texture idleHurt   = tex(manager, "sprites/knight/idle_hurt.png");
        Texture death      = tex(manager, "sprites/knight/death.png");
        Texture slashAltTex  = tex(manager, "sprites/knight/effects/slash_effect.png");
        Texture downFxTex    = tex(manager, "sprites/knight/effects/down_slash_effect.png");
        Texture dashFxTex    = tex(manager, "sprites/knight/effects/dash_effect.png");
        TextureRegion[] idleFrames       = SpriteSheetLoader.sliceAuto(idle, FRAME_W, FRAME_H);
        TextureRegion[] runFrames        = SpriteSheetLoader.sliceAuto(run, FRAME_W, FRAME_H);
        TextureRegion[] airborneFrames   = SpriteSheetLoader.sliceAuto(airborne, FRAME_W, FRAME_H);
        TextureRegion[] dashFrames       = SpriteSheetLoader.sliceAuto(dash, FRAME_W, FRAME_H);
        TextureRegion[] doubleJumpFrames = SpriteSheetLoader.sliceAuto(doubleJump, FRAME_W, FRAME_H);
        TextureRegion[] slashFrames      = SpriteSheetLoader.sliceAuto(slash, FRAME_W, FRAME_H);
        TextureRegion[] downSlashFrames  = SpriteSheetLoader.sliceAuto(downSlash, FRAME_W, FRAME_H);
        TextureRegion[] focusFrames      = SpriteSheetLoader.sliceAuto(focus, FRAME_W, FRAME_H);
        TextureRegion[] vengefulCastFrames = SpriteSheetLoader.sliceAuto(vengefulCast, FRAME_W, FRAME_H);
        TextureRegion[] howlingCastFrames  = SpriteSheetLoader.sliceAuto(howlingCast, FRAME_W, FRAME_H);
        TextureRegion[] idleHurtFrames   = SpriteSheetLoader.sliceAuto(idleHurt, FRAME_W, FRAME_H);
        TextureRegion[] deathFrames      = SpriteSheetLoader.sliceAuto(death, FRAME_W, FRAME_H);
        int mid = airborneFrames.length / 2;
        TextureRegion[] risingFrames  = slice(airborneFrames, 0, mid);
        TextureRegion[] fallingFrames = slice(airborneFrames, mid, airborneFrames.length);
        put(PlayerState.IDLE,           idleFrames,       0.1f,  true);
        put(PlayerState.RUNNING,        runFrames,        0.045f, true);
        put(PlayerState.JUMPING,        risingFrames,     0.05f, false);
        put(PlayerState.FALLING,        fallingFrames,    0.06f, false);
        put(PlayerState.DOUBLE_JUMPING, doubleJumpFrames, 0.05f, false);
        put(PlayerState.DASHING,        dashFrames,       0.015f, true);
        put(PlayerState.ATTACKING,      slashFrames,      0.045f, false); 
        put(PlayerState.HURT,           idleHurtFrames,   0.06f, true);
        put(PlayerState.DEAD,           deathFrames,      0.08f, false);

        downSlashEntry = new Entry(
                animOf(downSlashFrames, 0.045f, Animation.PlayMode.NORMAL), false);
        put(PlayerState.WALL_SLIDING,   slice(fallingFrames, 0, 1), 1f, true);
        put(PlayerState.FOCUSING,       focusFrames,      0.08f, true);
        vengefulSpiritCastEntry = new Entry(animOf(vengefulCastFrames, 0.045f, Animation.PlayMode.NORMAL), false);
        howlingWraithsCastEntry = new Entry(animOf(howlingCastFrames, 0.05f, Animation.PlayMode.NORMAL), false);
        TextureRegion[] upSlashFrames = SpriteSheetLoader.sliceAuto(upSlash, 169, 192);
        upSlashEffectEntry = new Entry(
                animOf(upSlashFrames, 0.045f, Animation.PlayMode.NORMAL), false);


        
        TextureRegion[] slashAltFrames = SpriteSheetLoader.sliceAuto(slashAltTex, SLASH_ALT_FRAME_W, SLASH_ALT_FRAME_H);
        slashEffectAlt = new EffectEntry(
                animOf(slashAltFrames, 0.04f, Animation.PlayMode.NORMAL), false,
                (float) SLASH_ALT_FRAME_W / SLASH_ALT_FRAME_H);

        TextureRegion[] downFxFrames = SpriteSheetLoader.sliceAuto(downFxTex, DOWN_FX_FRAME_W, DOWN_FX_FRAME_H);
        downSlashEffect = new EffectEntry(
                animOf(downFxFrames, 0.04f, Animation.PlayMode.NORMAL), false,
                (float) DOWN_FX_FRAME_W / DOWN_FX_FRAME_H);

        TextureRegion[] dashFxFrames = SpriteSheetLoader.sliceAuto(dashFxTex, DASH_FX_FRAME_W, DASH_FX_FRAME_H);
        dashEffect = new EffectEntry(
                animOf(dashFxFrames, 0.02f, Animation.PlayMode.NORMAL), false,
                (float) DASH_FX_FRAME_W / DASH_FX_FRAME_H);
    }

    private Texture tex(AssetManager manager, String path) {
        Texture t = manager.get(path, Texture.class);
        t.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
        return t;
    }

    private TextureRegion[] slice(TextureRegion[] src, int from, int to) {
        TextureRegion[] out = new TextureRegion[to - from];
        System.arraycopy(src, from, out, 0, to - from);
        return out;
    }

    private Animation<TextureRegion> animOf(TextureRegion[] frames, float frameDuration, Animation.PlayMode mode) {
        Animation<TextureRegion> anim = new Animation<>(frameDuration, frames);
        anim.setPlayMode(mode);
        return anim;
    }

    private void put(PlayerState state, TextureRegion[] frames, float frameDuration, boolean loop) {
        Animation<TextureRegion> anim = animOf(frames, frameDuration,
                loop ? Animation.PlayMode.LOOP : Animation.PlayMode.NORMAL);
        entries.put(state, new Entry(anim, loop));
    }

    public Entry get(PlayerState state) {
        Entry e = entries.get(state);
        return e != null ? e : entries.get(PlayerState.IDLE);
    }

    
    public Entry getDownSlash() {
        return downSlashEntry;
    }

    public Entry getVengefulSpiritCast() {
        return vengefulSpiritCastEntry;
    }

    public Entry getHowlingWraithsCast() {
        return howlingWraithsCastEntry;
    }
    public Entry getUpSlashEffect() {
        return upSlashEffectEntry;
    }


    public EffectEntry getSlashEffectAlt()  { return slashEffectAlt; }
    public EffectEntry getDownSlashEffect() { return downSlashEffect; }
    public EffectEntry getDashEffect()      { return dashEffect; }

    
    public static float frameAspect() { return (float) FRAME_W / FRAME_H; }
}