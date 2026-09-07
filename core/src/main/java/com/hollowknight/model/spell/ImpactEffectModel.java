package com.hollowknight.model.spell;


public class ImpactEffectModel {
    private static final float LIFETIME = 0.45f;

    private final float x, y;
    private float elapsed = 0f;
    private boolean alive = true;

    public ImpactEffectModel(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public void update(float delta) {
        elapsed += delta;
        if (elapsed >= LIFETIME) alive = false;
    }

    public float getX() { return x; }
    public float getY() { return y; }
    public float getElapsed() { return elapsed; }
    public boolean isAlive() { return alive; }
    public void kill() { alive = false; }
}