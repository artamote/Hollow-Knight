package com.hollowknight.view.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.hollowknight.model.entity.player.PlayerModel;
import com.hollowknight.model.entity.player.PlayerState;
import com.hollowknight.view.anim.PlayerAnimations;
import com.hollowknight.view.anim.PlayerAnimations.EffectEntry;
import com.hollowknight.view.anim.PlayerAnimations.Entry;


public class PlayerView {

    private static final float VISUAL_HEIGHT = 96f; 

    private final PlayerAnimations animations;
    private float stateTime = 0f;
    private Object lastAnimKey = null;

    private float arcTime = -1f;    
    private float slashFxTime = -1f; 
    private float downFxTime = -1f;  
    private float dashFxTime = -1f;  

    public PlayerView(PlayerAnimations animations) {
        this.animations = animations;
    }

    public void update(float delta, PlayerModel p) {
        Object key = animKey(p);
        if (!key.equals(lastAnimKey)) {
            stateTime = 0f;
            lastAnimKey = key;
        } else {
            stateTime += delta;
        }

        boolean upAttack = p.getState() == PlayerState.ATTACKING
                && p.getAttackDirection() == PlayerModel.AttackDir.UP;
        arcTime = tick(arcTime, upAttack, delta);

        boolean horizontalAttack = p.getState() == PlayerState.ATTACKING
                && (p.getAttackDirection() == PlayerModel.AttackDir.LEFT
                || p.getAttackDirection() == PlayerModel.AttackDir.RIGHT);
        slashFxTime = tick(slashFxTime, horizontalAttack, delta);

        boolean downAttack = p.getState() == PlayerState.ATTACKING
                && p.getAttackDirection() == PlayerModel.AttackDir.DOWN;
        downFxTime = tick(downFxTime, downAttack, delta);

        boolean dashing = p.getState() == PlayerState.DASHING;
        dashFxTime = tick(dashFxTime, dashing, delta);
    }

    private float tick(float current, boolean active, float delta) {
        if (!active) return -1f;
        return current < 0 ? 0f : current + delta;
    }

    private Object animKey(PlayerModel p) {
        if (p.getState() == PlayerState.ATTACKING) {
            switch (p.getAttackDirection()) {
                case LEFT:
                case RIGHT: return PlayerState.ATTACKING;
                case DOWN:  return "ATTACK_DOWN";
                default:    return "ATTACK_UP_" + p.isOnGround();
            }
        }
        if (p.getState() == PlayerState.CASTING_SPELL) {
            return "CAST_" + p.getLastCastSpell();
        }
        return p.getState();
    }

    private Entry bodyAnimFor(PlayerModel p) {
        if (p.getState() == PlayerState.ATTACKING) {
            switch (p.getAttackDirection()) {
                case LEFT:
                case RIGHT:
                    return animations.get(PlayerState.ATTACKING); 
                case DOWN:
                    return animations.getDownSlash(); 
                default:
                    
                    return animations.get(p.getVelocityY() > 0 ? PlayerState.JUMPING : PlayerState.FALLING);
            }
        }
        if (p.getState() == PlayerState.CASTING_SPELL) {
            return "HOWLING_WRAITHS".equals(p.getLastCastSpell())
                    ? animations.getHowlingWraithsCast()
                    : animations.getVengefulSpiritCast();
        }
        return animations.get(p.getState());
    }

    public void render(SpriteBatch batch, PlayerModel p) {
        Entry entry = bodyAnimFor(p);
        TextureRegion frame = entry.anim.getKeyFrame(stateTime, entry.loop);

        boolean flip = p.isFacingRight();
        if (frame.isFlipX() != flip) frame.flip(true, false);

        float aspect = PlayerAnimations.frameAspect();
        float drawH = VISUAL_HEIGHT;
        float drawW = drawH * aspect;
        float drawX = p.getCenterX() - drawW / 2f;
        float drawY = p.getY(); 

        boolean flashSkip = p.isInvincible() && !p.isDying() && ((int) (p.getInvincibleTimer() * 10) % 2 == 0);
        if (!flashSkip) {
            batch.draw(frame, drawX, drawY, drawW, drawH);
        }

        
        if (dashFxTime >= 0) {
            drawEffect(batch, animations.getDashEffect(), dashFxTime, flip,
                    p.getCenterX(), p.getY() + p.getHeight() / 2f, VISUAL_HEIGHT * 1.1f);
        }

        if (arcTime >= 0) {
            Entry upSlash = animations.getUpSlashEffect();
            TextureRegion arcFrame = upSlash.anim.getKeyFrame(arcTime, upSlash.loop);
            float sh = 70f;
            float arcAspect = (float) arcFrame.getRegionWidth() / arcFrame.getRegionHeight();
            float sw = sh * arcAspect;
            float sx = p.getCenterX() - sw / 2f;
            float sy = p.getY() + p.getHeight();
            batch.draw(arcFrame, sx, sy, sw, sh);
        }

        
        if (slashFxTime >= 0) {
            EffectEntry fx = animations.getSlashEffectAlt();
            TextureRegion fxFrame = fx.anim.getKeyFrame(slashFxTime, fx.loop);
            if (fxFrame.isFlipX() != flip) fxFrame.flip(true, false);
            batch.draw(fxFrame, drawX, drawY, drawW, drawH);
        }

        
        if (downFxTime >= 0) {
            drawEffect(batch, animations.getDownSlashEffect(), downFxTime, flip,
                    p.getCenterX(), p.getY() + p.getHeight() * 0.2f, VISUAL_HEIGHT * 0.8f);
        }
    }

    private void drawEffect(SpriteBatch batch, EffectEntry fx, float time, boolean flip,
                            float centerX, float centerY, float drawHeight) {
        TextureRegion frame = fx.anim.getKeyFrame(time, fx.loop);
        if (frame.isFlipX() != flip) frame.flip(true, false);
        float w = drawHeight * fx.aspect;
        float h = drawHeight;
        batch.draw(frame, centerX - w / 2f, centerY - h / 2f, w, h);
    }
}