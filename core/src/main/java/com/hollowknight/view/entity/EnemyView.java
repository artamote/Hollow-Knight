package com.hollowknight.view.entity;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.hollowknight.model.entity.boss.FalseKnightModel;
import com.hollowknight.model.entity.enemy.EnemyModel;
import com.hollowknight.model.entity.enemy.flying.MosquitoModel;
import com.hollowknight.model.entity.enemy.flying.MossflyModel;
import com.hollowknight.model.entity.enemy.ground.CrystalGuardianModel;
import com.hollowknight.model.entity.enemy.ground.HuskHornheadModel;
import com.hollowknight.model.world.Room;
import com.hollowknight.view.anim.EnemyAnimations;
import com.hollowknight.view.anim.EnemyAnimations.Entry;

import java.util.IdentityHashMap;
import java.util.Map;


public class EnemyView {

    private final EnemyAnimations animations;
    private final Map<EnemyModel, Float> stateTimes  = new IdentityHashMap<>();
    private final Map<EnemyModel, Entry> lastEntry    = new IdentityHashMap<>();
    private final Map<EnemyModel, Float> deathElapsed = new IdentityHashMap<>();

    private float bossStateTime = 0f;
    private FalseKnightModel.BossState lastBossState = null;

    public EnemyView(EnemyAnimations animations) {
        this.animations = animations;
    }

    public void update(float delta, Room room) {
        for (EnemyModel e : room.enemies) {
            if (!e.isAlive()) {
                deathElapsed.merge(e, delta, Float::sum);
                deathElapsed.putIfAbsent(e, 0f);
                continue;
            }
            Entry entry = pickAnim(e);
            Entry prev = lastEntry.get(e);
            float t = stateTimes.getOrDefault(e, 0f);
            if (prev != entry) {
                t = 0f;
            } else {
                t += delta;
            }
            stateTimes.put(e, t);
            lastEntry.put(e, entry);
        }
    }

    public void updateBoss(float delta, FalseKnightModel boss) {
        if (boss == null) return;
        if (boss.getBossState() != lastBossState) {
            bossStateTime = 0f;
            lastBossState = boss.getBossState();
        } else {
            bossStateTime += delta;
        }
    }

    
    private Entry pickAnim(EnemyModel e) {
        if (e instanceof HuskHornheadModel) {
            HuskHornheadModel h = (HuskHornheadModel) e;
            switch (h.getHHState()) {
                case REST:   return animations.getHuskIdle();
                case CHARGE: return animations.getSpecial(EnemyModel.EnemyType.HUSK_HORNHEAD);
                default:     return animations.getWalk(EnemyModel.EnemyType.HUSK_HORNHEAD);
            }
        }
        if (e instanceof CrystalGuardianModel) {
            CrystalGuardianModel c = (CrystalGuardianModel) e;
            switch (c.getCGState()) {
                case FIRING_LASER: return animations.getSpecial(EnemyModel.EnemyType.CRYSTAL_GUARDIAN);
                case ENRAGED:      return animations.getCrystalGuardianEnraged();
                default:           return animations.getWalk(EnemyModel.EnemyType.CRYSTAL_GUARDIAN);
            }
        }
        if (e instanceof MosquitoModel) {
            MosquitoModel mq = (MosquitoModel) e;
            return mq.getMState() == MosquitoModel.MState.DASHING
                    ? animations.getSpecial(EnemyModel.EnemyType.MOSQUITO)
                    : animations.getWalk(EnemyModel.EnemyType.MOSQUITO);
        }
        if (e instanceof MossflyModel) {
            MossflyModel mf = (MossflyModel) e;
            switch (mf.getMossflyState()) {
                case HIDDEN:    return animations.getMossflyHidden();
                case REVEALING: return animations.getSpecial(EnemyModel.EnemyType.MOSSFLY);
                default:        return animations.getWalk(EnemyModel.EnemyType.MOSSFLY);
            }
        }
        return animations.getWalk(e.getType());
    }

    public void render(SpriteBatch batch, Room room) {
        for (EnemyModel e : room.enemies) {
            if (!e.isAlive()) {
                renderDeath(batch, e);
                continue;
            }
            Entry entry = lastEntry.get(e);
            if (entry == null) continue;
            float t = stateTimes.getOrDefault(e, 0f);
            TextureRegion frame = entry.anim.getKeyFrame(t, entry.loop);
            drawFrame(batch, frame, e);

            if (e instanceof CrystalGuardianModel) {
                renderLaserBeam(batch, (CrystalGuardianModel) e);
            }
        }
    }

    
    private void renderLaserBeam(SpriteBatch batch, CrystalGuardianModel guardian) {
        if (guardian.getCGState() != CrystalGuardianModel.CGState.FIRING_LASER) return;
        if (!guardian.isLaserFired()) return;
        com.hollowknight.view.anim.EnemyAnimations.Entry beamEntry = animations.getCrystalGuardianLaserBeam();
        float elapsedSinceFired = guardian.getStateTimer() - guardian.getLaserWindupDuration();
        TextureRegion beamTex = beamEntry.anim.getKeyFrame(Math.max(0f, elapsedSinceFired), beamEntry.loop);
        float beamHeight = 48f;
        float range = guardian.getDetectionRange();
        float beamX = guardian.isFacingRight() ? guardian.getCenterX() : guardian.getCenterX() - range;
        float beamY = guardian.getCenterY() - beamHeight / 2f;
        boolean flip = !guardian.isFacingRight();
        if (beamTex.isFlipX() != flip) beamTex.flip(true, false);
        batch.draw(beamTex, beamX, beamY, range, beamHeight);
    }

    private void renderDeath(SpriteBatch batch, EnemyModel e) {
        Entry deathEntry = animations.getDeath(e.getType());
        if (deathEntry == null) return;
        float elapsed = deathElapsed.getOrDefault(e, 0f);
        TextureRegion frame = deathEntry.anim.getKeyFrame(elapsed, false);
        drawFrame(batch, frame, e);
    }

    private void drawFrame(SpriteBatch batch, TextureRegion frame, EnemyModel e) {
        boolean flip = e.isFacingRight();
        if (frame.isFlipX() != flip) frame.flip(true, false);
        
        float drawH = e.getHeight() * 2.2f;
        float aspect = (float) frame.getRegionWidth() / frame.getRegionHeight();
        float drawW = drawH * aspect;
        float drawX = e.getCenterX() - drawW / 2f;
        float drawY = e.getY();
        batch.draw(frame, drawX, drawY, drawW, drawH);
    }

    public void renderBoss(SpriteBatch batch, FalseKnightModel boss) {
        if (boss == null) return;
        TextureRegion frame;
        if (boss.getBossState() == FalseKnightModel.BossState.STUNNED) {
            Entry recoverEntry = animations.getBoss(FalseKnightModel.BossState.STUNNED);
            float recoverDuration = recoverEntry.anim.getAnimationDuration();
            float remaining = boss.getStunTimeRemaining();
            if (remaining > recoverDuration) {
                Entry idleEntry = animations.getBossStunIdle();
                frame = idleEntry.anim.getKeyFrame(bossStateTime, true);
            } else {
                float intoRecovery = recoverDuration - Math.max(0f, remaining);
                frame = recoverEntry.anim.getKeyFrame(intoRecovery, false);
            }
        } else {
            Entry entry = animations.getBoss(boss.getBossState());
            frame = entry.anim.getKeyFrame(bossStateTime, entry.loop);
        }

        boolean flip = boss.isFacingRight();
        if (frame.isFlipX() != flip) frame.flip(true, false);

        float drawH = boss.getHeight() * 1.8f;
        float aspect = (float) frame.getRegionWidth() / frame.getRegionHeight();
        float drawW = drawH * aspect;
        float drawX = boss.getCenterX() - drawW / 2f;
        float drawY = boss.getY();
        if (boss.getHitFlashTimer() > 0) {
            batch.setColor(2f, 2f, 2f, 1f); 
        }
        batch.draw(frame, drawX, drawY, drawW, drawH);
        batch.setColor(1f, 1f, 1f, 1f);

        renderShockwave(batch, boss);
    }

    
    private void renderShockwave(SpriteBatch batch, FalseKnightModel boss) {
        if (!boss.isWaveActive()) return;
        float elapsed = Math.max(0f, boss.getActionTimer() - 0.15f);
        float distance = FalseKnightModel.WAVE_ACCEL * elapsed * elapsed;
        Animation<TextureRegion> wave = animations.getBossShockwave();
        TextureRegion frame = wave.getKeyFrame(elapsed, true);
        float waveH = 170f; 
        float aspect = (float) frame.getRegionWidth() / frame.getRegionHeight();
        float waveW = waveH * aspect;
        float waveY = boss.getY();
        batch.draw(frame, boss.getWaveX() + distance - waveW / 2f, waveY, waveW, waveH);
        batch.draw(frame, boss.getWaveX() - distance + waveW / 2f, waveY, -waveW, waveH);
    }
}