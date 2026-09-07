package com.hollowknight.view.hud;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.hollowknight.model.GameModel;
import com.hollowknight.model.entity.player.PlayerModel;
import com.hollowknight.util.Constants;
import com.hollowknight.view.anim.HudAnimations;

public class HUDView {

    private static final float HUD_X = 20f;
    private static final float HUD_Y = 640f;

    private static final float VESSEL_H = 110f;
    private static final float SOUL_CENTER_X_FRAC = 0.296f;
    private static final float SOUL_CENTER_Y_FRAC = 0.521f;
    private static final float SOUL_MAX_RADIUS_FRAC = 0.409f;
    private static final float MASK_H = 50f; 
    private static final float MASK_ROW_START_FRAC = 0.40f;
    private static final float MASK_ROW_END_FRAC = 1.05f;
    private static final float MASK_ROW_Y_FRAC = 0.60f;
    private static final Color EMPTY_MASK_TINT = new Color(0.28f, 0.28f, 0.34f, 0.85f);
    private int previousHp = -1;
    private float[] breakTimer;
    private float[] refillTimer;
    private float shineTime = 0f;

    public void update(float delta, GameModel model) {
        PlayerModel p = model.player;
        int maxHp = p.getMaxHp();
        if (breakTimer == null) {
            breakTimer = new float[maxHp];
            refillTimer = new float[maxHp];
            for (int i = 0; i < maxHp; i++) { breakTimer[i] = -1; refillTimer[i] = -1; }
        }

        int hp = p.getHp();
        if (previousHp == -1) previousHp = hp;
        if (hp < previousHp) {
            for (int i = hp; i < previousHp; i++) { breakTimer[i] = 0f; refillTimer[i] = -1; }
        } else if (hp > previousHp) {
            for (int i = previousHp; i < hp; i++) { refillTimer[i] = 0f; breakTimer[i] = -1; }
        }
        previousHp = hp;

        for (int i = 0; i < maxHp; i++) {
            if (breakTimer[i] >= 0) breakTimer[i] += delta;
            if (refillTimer[i] >= 0) refillTimer[i] += delta;
        }
        shineTime += delta;
    }

    public void renderVesselAndMasks(SpriteBatch batch, GameModel model, HudAnimations hud) {
        PlayerModel p = model.player;
        int maxHp = p.getMaxHp();
        int hp = p.getHp();

        TextureRegion vessel = hud.vesselFull;
        float vesselAspect = (float) vessel.getRegionWidth() / vessel.getRegionHeight();
        float vesselW = VESSEL_H * vesselAspect;
        float vesselX = HUD_X;
        float vesselY = HUD_Y - VESSEL_H;

        batch.draw(vessel, vesselX, vesselY, vesselW, VESSEL_H);
        float soulFraction = p.getSoul() / (float) Constants.SOUL_MAX;
        float diameter = VESSEL_H * SOUL_MAX_RADIUS_FRAC * 3f;
        float cx = vesselX + vesselW * SOUL_CENTER_X_FRAC - 2;
        float cy = vesselY + VESSEL_H * SOUL_CENTER_Y_FRAC - 15;
        TextureRegion soulFrame = hud.getSoulFillFrame(soulFraction);
        batch.draw(soulFrame, cx - diameter / 2f, cy - diameter / 2f, diameter, diameter);
        float maskAspect = 126f / 167f;
        float maskW = MASK_H * maskAspect;
        float rowStartX = vesselX + vesselW * MASK_ROW_START_FRAC;
        float rowEndX = vesselX + vesselW * MASK_ROW_END_FRAC;
        float rowY = vesselY + VESSEL_H * MASK_ROW_Y_FRAC;

        for (int i = 0; i < maxHp; i++) {
            float t = maxHp > 1 ? i / (float) (maxHp - 1) : 0f;
            float x = rowStartX + t * (rowEndX - rowStartX);
            float y = rowY;

            TextureRegion frame = null;
            boolean tintEmpty = false;

            if (breakTimer[i] >= 0) {
                if (breakTimer[i] <= hud.breakHealth.getAnimationDuration()) {
                    frame = hud.breakHealth.getKeyFrame(breakTimer[i], false);
                } else {
                    breakTimer[i] = -1; 
                }
            } else if (refillTimer[i] >= 0) {
                if (refillTimer[i] <= hud.healthRefill.getAnimationDuration()) {
                    frame = hud.healthRefill.getKeyFrame(refillTimer[i], false);
                } else {
                    refillTimer[i] = -1; 
                }
            }

            if (frame == null && i < hp) {
                frame = hud.filledHealthShine.getKeyFrame(shineTime + i * 0.1f, true);
            } else if (frame == null) {
                
                
                
                frame = hud.filledHealthShine.getKeyFrame(0f, false);
                tintEmpty = true;
            }

            if (frame != null) {
                if (tintEmpty) batch.setColor(EMPTY_MASK_TINT);
                batch.draw(frame, x - maskW / 2f, y - MASK_H / 2f, maskW, MASK_H);
                if (tintEmpty) batch.setColor(Color.WHITE);
            }
        }
    }


}