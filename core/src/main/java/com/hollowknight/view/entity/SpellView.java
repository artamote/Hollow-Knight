package com.hollowknight.view.entity;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.hollowknight.model.GameModel;
import com.hollowknight.model.spell.HowlingWraithsModel;
import com.hollowknight.model.spell.ImpactEffectModel;
import com.hollowknight.model.spell.VengefulSpiritModel;
import com.hollowknight.view.anim.SpellAnimations;


public class SpellView {

    private static final float BALL_HEIGHT = 40f;
    private static final float SCREAM_HEIGHT = 110f;
    private static final float BLAST_HEIGHT = 70f;

    private final SpellAnimations animations;

    public SpellView(SpellAnimations animations) {
        this.animations = animations;
    }

    public void render(SpriteBatch batch, GameModel model) {
        for (VengefulSpiritModel spirit : model.activeSpirits) {
            if (!spirit.isAlive()) continue;
            TextureRegion frame = animations.vengefulSpirit.getKeyFrame(spirit.getStateTime(), false);
            boolean flip = !spirit.isFacingRight();
            if (frame.isFlipX() != flip) frame.flip(true, false);
            float aspect = SpellAnimations.ballAspect();
            float h = BALL_HEIGHT;
            float w = h * aspect;
            batch.draw(frame, spirit.getX() - w / 2f, spirit.getY() - h / 2f, w, h);
        }

        for (HowlingWraithsModel wraiths : model.activeWraiths) {
            if (!wraiths.isAlive()) continue;
            TextureRegion frame = animations.howlingWraiths.getKeyFrame(wraiths.getElapsed(), false);

            float aspect = SpellAnimations.screamAspect();
            float h = SCREAM_HEIGHT;
            float w = h * aspect;
            batch.draw(frame, wraiths.getX() - w / 2f, wraiths.getY() - h / 2f, w, h);
        }

        for (ImpactEffectModel impact : model.activeImpacts) {
            if (!impact.isAlive()) continue;
            TextureRegion frame = animations.blastSoul.getKeyFrame(impact.getElapsed(), false);

            float aspect = SpellAnimations.blastAspect();
            float h = BLAST_HEIGHT;
            float w = h * aspect;
            batch.draw(frame, impact.getX() - w / 2f, impact.getY() - h / 2f, w, h);
        }
    }
}