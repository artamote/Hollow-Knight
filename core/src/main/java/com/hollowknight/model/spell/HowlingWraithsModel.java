package com.hollowknight.model.spell;

import com.hollowknight.util.Constants;


public class HowlingWraithsModel {
    private final float x, y;
    private float timer;
    private int ticksDone = 0;
    private boolean alive = true;
    private final int damagePerTick;
    private static final float TICK_INTERVAL = Constants.HOWLING_WRAITHS_DURATION / 3f;
    private float tickTimer = 0f;

    public HowlingWraithsModel(float x, float y) {
        this.x = x; this.y = y;
        this.timer = Constants.HOWLING_WRAITHS_DURATION;
        this.damagePerTick = Constants.HOWLING_WRAITHS_DAMAGE;
    }

    public boolean update(float delta) {
        timer -= delta;
        tickTimer -= delta;
        if (timer <= 0) { alive = false; return false; }
        if (tickTimer <= 0 && ticksDone < 3) {
            tickTimer = TICK_INTERVAL;
            ticksDone++;
            return true;
        }
        return false;
    }

    public float getX() { return x; }
    public float getY() { return y; }
    public boolean isAlive() { return alive; }
    public int getDamagePerTick() { return damagePerTick; }

    public float getElapsed() { return Constants.HOWLING_WRAITHS_DURATION - timer; }
}