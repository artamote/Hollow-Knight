package com.hollowknight.model.world;

import com.badlogic.gdx.math.Rectangle;


public class Hazard {
    public final Rectangle bounds;
    public final int damage;

    public Hazard(float x, float y, float w, float h, int damage) {
        bounds = new Rectangle(x, y, w, h);
        this.damage = damage;
    }
}