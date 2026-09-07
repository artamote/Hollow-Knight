package com.hollowknight.model.entity.enemy.ground;

import com.hollowknight.model.entity.enemy.EnemyModel;

public class CrystalGuardianModel extends EnemyModel {

    public enum CGState { IDLE_WATCH, FIRING_LASER, ENRAGED, COOLDOWN }

    private CGState cgState      = CGState.IDLE_WATCH;
    private float   enrageTimer  = 0f;
    private boolean laserFired   = false;

    private static final float ENRAGE_DURATION   = 3.0f;
    private static final float COOLDOWN_DURATION = 2.0f;
    private static final float ENRAGE_SPEED      = 400f;
    private static final float LASER_WINDUP_DURATION = 0.6f;
    private static final float LASER_TOTAL_DURATION  = 1.1f;

    public CrystalGuardianModel(float x, float y) {
        super(x, y, 40f, 44f, 80, 0f, EnemyType.CRYSTAL_GUARDIAN);
        this.detectionRange = 600f;
    }

    public CGState getCGState()            { return cgState; }
    public void    setCGState(CGState s)   { cgState = s; }
    public float   getEnrageTimer()        { return enrageTimer; }
    public void    setEnrageTimer(float t) { enrageTimer = t; }
    public boolean isLaserFired()          { return laserFired; }
    public void    setLaserFired(boolean v){ laserFired = v; }
    public float   getEnrageDuration()     { return ENRAGE_DURATION; }
    public float   getCooldownDuration()   { return COOLDOWN_DURATION; }
    public float   getEnrageSpeed()        { return ENRAGE_SPEED; }
    public float   getLaserWindupDuration() { return LASER_WINDUP_DURATION; }
    public float   getLaserTotalDuration()  { return LASER_TOTAL_DURATION; }
}