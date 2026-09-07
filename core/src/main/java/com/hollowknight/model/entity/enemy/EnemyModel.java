package com.hollowknight.model.entity.enemy;

import com.hollowknight.model.entity.Entity;

public abstract class EnemyModel extends Entity {

    public enum EnemyType { CRAWLID, MOSSCREEP, TIKTIK, CRYSTAL_CRAWLER,
        MOSQUITO, MOSSFLY, WINGED_SENTRY, CRYSTAL_HUNTER,
        HUSK_HORNHEAD, CRYSTAL_GUARDIAN }

    protected EnemyType type;
    protected float speed;
    protected boolean aggro        = false;
    protected boolean patrolRight  = true;
    protected float stateTimer     = 0f;
    protected float detectionRange = 300f;
    protected float knockbackTimer = 0f;
    protected float spawnX, spawnY;

    public EnemyModel(float x, float y, float w, float h, int hp, float speed, EnemyType type) {
        super(x, y, w, h, hp);
        this.speed  = speed;
        this.type   = type;
        this.spawnX = x;
        this.spawnY = y;
    }

    public EnemyType getType()             { return type; }
    public float getSpeed()                { return speed; }
    public boolean isAggro()               { return aggro; }
    public void setAggro(boolean v)        { aggro = v; }
    public boolean isPatrolRight()         { return patrolRight; }
    public void setPatrolRight(boolean v)  { patrolRight = v; }
    public float getStateTimer()           { return stateTimer; }
    public void setStateTimer(float t)     { stateTimer = t; }
    public float getDetectionRange()       { return detectionRange; }
    public float getSpawnX()               { return spawnX; }
    public float getSpawnY()               { return spawnY; }
    public float getKnockbackTimer()       { return knockbackTimer; }
    public void setKnockbackTimer(float t) { knockbackTimer = t; }
    public boolean isKnockedBack()         { return knockbackTimer > 0; }

    public void respawn() {
        x = spawnX; y = spawnY;
        hp = maxHp; alive = true;
        velocityX = 0; velocityY = 0;
        aggro = false; stateTimer = 0f;
    }
}