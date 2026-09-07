package com.hollowknight.model.entity.boss;

import com.hollowknight.model.entity.Entity;


public class FalseKnightModel extends Entity {

    public enum BossState {
        IDLE, MACE_SLAM, CHARGE_RUN, OFFENSIVE_LEAP,
        DEFENSIVE_LEAP, POWER_SLAM, STUNNED, DEAD
    }

    private BossState bossState   = BossState.IDLE;
    private int       phase       = 1;
    private boolean   arenaLocked = false;


    private float decisionTimer   = 0f;
    private float actionTimer     = 0f;


    private BossState lastMove    = null;
    private int       sameMoveCnt = 0;


    private float speedMultiplier = 1.0f;


    private float stunTimeRemaining = 0f;


    private float waveX;
    private boolean waveActive     = false;
    private boolean waveFacingRight = true;
    private boolean waveHitPlayer  = false;
    public static final float WAVE_ACCEL = 250f;


    private float hitFlashTimer = 0f;
    public float getHitFlashTimer()        { return hitFlashTimer; }
    public void  setHitFlashTimer(float t) { hitFlashTimer = t; }


    private float knockbackTimer = 0f;
    public float getKnockbackTimer()        { return knockbackTimer; }
    public void  setKnockbackTimer(float t) { knockbackTimer = t; }

    private static final int   BOSS_HP         = 200;
    private static final float BASE_SPEED      = 220f;
    private static final float CHARGE_SPEED    = 400f; // was 580f -- slower rush
    private static final float DECISION_DELAY  = 1.2f;

    public FalseKnightModel(float x, float y) {
        super(x, y, 112f, 126f, BOSS_HP);
    }


    public boolean shouldStun() {
        return phase == 1 && hp <= maxHp / 2;
    }

    public BossState getBossState()              { return bossState; }
    public void      setBossState(BossState s)   { bossState = s; }
    public int       getPhase()                  { return phase; }
    public void      setPhase(int p)             { phase = p; }
    public boolean   isArenaLocked()             { return arenaLocked; }
    public void      setArenaLocked(boolean v)   { arenaLocked = v; }
    public float     getDecisionTimer()          { return decisionTimer; }
    public void      setDecisionTimer(float t)   { decisionTimer = t; }
    public float     getActionTimer()            { return actionTimer; }
    public void      setActionTimer(float t)     { actionTimer = t; }
    public BossState getLastMove()               { return lastMove; }
    public void      setLastMove(BossState m)    { lastMove = m; }
    public int       getSameMoveCnt()            { return sameMoveCnt; }
    public void      setSameMoveCnt(int c)       { sameMoveCnt = c; }
    public float     getSpeedMultiplier()        { return speedMultiplier; }
    public void      setSpeedMultiplier(float m) { speedMultiplier = m; }
    public float     getStunTimeRemaining()          { return stunTimeRemaining; }
    public void      setStunTimeRemaining(float t)   { stunTimeRemaining = t; }
    public float     getBaseSpeed()              { return BASE_SPEED; }
    public float     getChargeSpeed()            { return CHARGE_SPEED; }
    public float     getDecisionDelay()          { return DECISION_DELAY; }
    public float     getWaveX()                  { return waveX; }
    public void      setWaveX(float x)           { waveX = x; }
    public boolean   isWaveActive()              { return waveActive; }
    public void      setWaveActive(boolean v)    { waveActive = v; }
    public boolean   isWaveFacingRight()         { return waveFacingRight; }
    public void      setWaveFacingRight(boolean v){ waveFacingRight = v; }
    public boolean   hasWaveHitPlayer()          { return waveHitPlayer; }
    public void      setWaveHitPlayer(boolean v) { waveHitPlayer = v; }
}