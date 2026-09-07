package com.hollowknight.model.entity.player;

import com.hollowknight.model.entity.Entity;
import com.hollowknight.util.Constants;


public class PlayerModel extends Entity {


    private PlayerState state = PlayerState.IDLE;

    private String lastCastSpell = "VENGEFUL_SPIRIT";


    private boolean canDoubleJump    = true;
    private boolean canDash          = true;
    private boolean isDashGravityOff = false;


    private float dashTimer       = 0f;
    private float dashCooldown    = 0f;
    private float invincibleTimer = 0f;
    private float focusTimer      = 0f;
    private float attackTimer     = 0f;
    private float hurtTimer       = 0f;
    private float spellLockTimer  = 0f;
    private float doubleJumpTimer = 0f;
    private float deathTimer      = 0f;
    private float attackHitWindowTimer = 0f;
    private final java.util.Set<com.hollowknight.model.entity.Entity> hitThisSwing = new java.util.HashSet<>();
    private final java.util.Set<com.hollowknight.model.entity.Entity> hitThisDash = new java.util.HashSet<>();
    private boolean pogoedThisSwing = false;


    private boolean onWall     = false;
    private boolean wallOnLeft = false;


    private int soul = 0;


    private int deathCount    = 0;
    private int killCount     = 0;
    private float timeElapsed = 0f;

    private float checkpointX;
    private float checkpointY;

    public enum AttackDir { LEFT, RIGHT, UP, DOWN }
    private AttackDir attackDirection = AttackDir.RIGHT;

    public PlayerModel(float x, float y) {
        super(x, y, 32f, 48f, Constants.PLAYER_MAX_MASKS);
        this.checkpointX = x;
        this.checkpointY = y;
    }


    public void gainSoul(int amount) {
        soul = Math.min(Constants.SOUL_MAX, soul + amount);
    }

    public boolean spendSoul(int amount) {
        if (soul < amount) return false;
        soul -= amount;
        return true;
    }

    public int getSoul()  { return soul; }
    public void setSoul(int s) { this.soul = s; }


    public PlayerState getState()              { return state; }
    public void setState(PlayerState state)    { this.state = state; }
    public String getLastCastSpell()           { return lastCastSpell; }
    public void setLastCastSpell(String s)     { lastCastSpell = s; }


    public boolean canDoubleJump()             { return canDoubleJump; }
    public void setCanDoubleJump(boolean v)    { canDoubleJump = v; }
    public boolean canDash()                   { return canDash; }
    public void setCanDash(boolean v)          { canDash = v; }
    public boolean isDashGravityOff()          { return isDashGravityOff; }
    public void setDashGravityOff(boolean v)   { isDashGravityOff = v; }


    public boolean isOnWall()                  { return onWall; }
    public void setOnWall(boolean v)           { onWall = v; }
    public boolean isWallOnLeft()              { return wallOnLeft; }
    public void setWallOnLeft(boolean v)       { wallOnLeft = v; }


    public float getDashTimer()                { return dashTimer; }
    public void setDashTimer(float t)          { dashTimer = t; }
    public float getDashCooldown()             { return dashCooldown; }
    public void setDashCooldown(float t)       { dashCooldown = t; }
    public float getInvincibleTimer()          { return invincibleTimer; }
    public void setInvincibleTimer(float t)    { invincibleTimer = t; }
    public float getFocusTimer()               { return focusTimer; }
    public void setFocusTimer(float t)         { focusTimer = t; }
    public float getAttackTimer()              { return attackTimer; }
    public void setAttackTimer(float t)        { attackTimer = t; }
    public float getHurtTimer()                { return hurtTimer; }
    public void setHurtTimer(float t)          { hurtTimer = t; }
    public float getSpellLockTimer()           { return spellLockTimer; }
    public void setSpellLockTimer(float t)     { spellLockTimer = t; }
    public float getDoubleJumpTimer()          { return doubleJumpTimer; }
    public void setDoubleJumpTimer(float t)    { doubleJumpTimer = t; }
    public float getDeathTimer()               { return deathTimer; }
    public void setDeathTimer(float t)         { deathTimer = t; }
    public boolean isDying()                   { return deathTimer > 0; }
    public float getAttackHitWindowTimer()             { return attackHitWindowTimer; }
    public void setAttackHitWindowTimer(float t)       { attackHitWindowTimer = t; }
    public java.util.Set<com.hollowknight.model.entity.Entity> getHitThisSwing() { return hitThisSwing; }
    public java.util.Set<com.hollowknight.model.entity.Entity> getHitThisDash() { return hitThisDash; }
    public boolean isPogoedThisSwing()                 { return pogoedThisSwing; }
    public void setPogoedThisSwing(boolean v)          { pogoedThisSwing = v; }

    public boolean isInvincible()              { return invincibleTimer > 0; }
    public boolean isAttacking()               { return attackTimer > 0; }
    public boolean isFocusing()                { return state == PlayerState.FOCUSING; }


    public AttackDir getAttackDirection()         { return attackDirection; }
    public void setAttackDirection(AttackDir d)   { attackDirection = d; }


    public float getCheckpointX()              { return checkpointX; }
    public float getCheckpointY()              { return checkpointY; }
    public void setCheckpoint(float cx, float cy) {
        this.checkpointX = cx;
        this.checkpointY = cy;
    }


    public int getDeathCount()                 { return deathCount; }
    public void incrementDeaths()              { deathCount++; }
    public void setDeathCount(int d)           { deathCount = d; }
    public int getKillCount()                  { return killCount; }
    public void incrementKills()               { killCount++; }
    public void setKillCount(int k)            { killCount = k; }
    public float getTimeElapsed()              { return timeElapsed; }
    public void addTime(float delta)           { timeElapsed += delta; }
    public void setTimeElapsed(float t)        { timeElapsed = t; }
}