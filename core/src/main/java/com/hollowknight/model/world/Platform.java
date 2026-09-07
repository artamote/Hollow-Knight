package com.hollowknight.model.world;

import com.badlogic.gdx.math.Rectangle;
import java.util.ArrayList;
import java.util.List;


public class Platform {
    public final Rectangle bounds;
    public Platform(float x, float y, float w, float h) {
        bounds = new Rectangle(x, y, w, h);
    }
}