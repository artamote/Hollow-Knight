package com.hollowknight.model.spell;

import com.hollowknight.util.Constants;


public class VengefulSpiritModel {
    private float x, y;
    private float velocityX;
    private boolean alive = true;
    private final boolean facingRight;
    private final int damage;
    private float stateTime = 0f;
    private boolean hasImpacted = false;

    public VengefulSpiritModel(float x, float y, boolean facingRight) {
        this.x = x; this.y = y;
        this.facingRight = facingRight;
        this.velocityX = facingRight ? Constants.VENGEFUL_SPIRIT_SPEED : -Constants.VENGEFUL_SPIRIT_SPEED;
        this.damage = Constants.VENGEFUL_SPIRIT_DAMAGE;
    }

    public void update(float delta) {
        x += velocityX * delta;
        stateTime += delta;
    }

    public float getX() { return x; }
    public float getY() { return y; }
    public boolean isAlive() { return alive; }
    public void kill() { alive = false; }
    public int getDamage() { return damage; }
    public boolean isFacingRight() { return facingRight; }
    public float getStateTime() { return stateTime; }
    public boolean hasImpacted() { return hasImpacted; }
    public void setHasImpacted(boolean b) { hasImpacted = b; }
}