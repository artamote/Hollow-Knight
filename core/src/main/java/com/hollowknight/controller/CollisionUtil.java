package com.hollowknight.controller;

import com.badlogic.gdx.math.Rectangle;
import com.hollowknight.model.entity.Entity;
import com.hollowknight.model.world.Platform;
import com.hollowknight.model.world.Room;


public final class CollisionUtil {
    private CollisionUtil() {}


    public static void resolveCollisions(Entity e, Room room, float delta) {
        float newX = e.getX() + e.getVelocityX() * delta;
        float newY = e.getY() + e.getVelocityY() * delta;

        Rectangle horiz = new Rectangle(newX, e.getY(), e.getWidth(), e.getHeight());
        for (Platform p : room.platforms) {
            if (horiz.overlaps(p.bounds)) {
                if (e.getVelocityX() > 0) newX = p.bounds.x - e.getWidth();
                else if (e.getVelocityX() < 0) newX = p.bounds.x + p.bounds.width;
                e.setVelocityX(0);
            }
        }
        e.setX(newX);

        boolean grounded = false;
        Rectangle vert = new Rectangle(e.getX(), newY, e.getWidth(), e.getHeight());
        for (Platform p : room.platforms) {
            if (vert.overlaps(p.bounds)) {
                if (e.getVelocityY() < 0) {
                    newY = p.bounds.y + p.bounds.height;
                    grounded = true;
                } else if (e.getVelocityY() > 0) {
                    newY = p.bounds.y - e.getHeight();
                }
                e.setVelocityY(0);
            }
        }
        e.setY(newY);
        e.setOnGround(grounded);
    }


    public static boolean isAtCliffEdge(Entity e, Room room, boolean facingRight) {
        float checkX = facingRight ? e.getX() + e.getWidth() + 4 : e.getX() - 4;
        Rectangle probe = new Rectangle(checkX, e.getY() - 8, 2, 8);
        for (Platform p : room.platforms) {
            if (probe.overlaps(p.bounds)) return false;
        }
        return true;
    }


    public static boolean willHitWall(Entity e, Room room, boolean facingRight) {
        float checkX = facingRight ? e.getX() + e.getWidth() + 2 : e.getX() - 2;
        Rectangle probe = new Rectangle(checkX, e.getY() + 4, 2, e.getHeight() - 8);
        for (Platform p : room.platforms) {
            if (probe.overlaps(p.bounds)) return true;
        }
        return false;
    }


    public static boolean willHitHazard(Entity e, Room room, boolean facingRight) {
        float checkX = facingRight ? e.getX() + e.getWidth() + 4 : e.getX() - 4;
        Rectangle probe = new Rectangle(checkX, e.getY(), 4, e.getHeight());
        for (com.hollowknight.model.world.Hazard h : room.hazards) {
            if (probe.overlaps(h.bounds)) return true;
        }
        return false;
    }


    public static void resolveEnemyCollisions(Entity e, Room room, float delta) {
        float newX = e.getX() + e.getVelocityX() * delta;
        float newY = e.getY() + e.getVelocityY() * delta;

        Rectangle horiz = new Rectangle(newX, e.getY(), e.getWidth(), e.getHeight());
        for (Platform p : room.platforms) {
            if (horiz.overlaps(p.bounds)) {
                if (e.getVelocityX() > 0) newX = p.bounds.x - e.getWidth();
                else if (e.getVelocityX() < 0) newX = p.bounds.x + p.bounds.width;
                e.setVelocityX(0);
                horiz.x = newX;
            }
        }
        for (com.hollowknight.model.world.Hazard h : room.hazards) {
            if (horiz.overlaps(h.bounds)) {
                if (e.getVelocityX() > 0) newX = h.bounds.x - e.getWidth();
                else if (e.getVelocityX() < 0) newX = h.bounds.x + h.bounds.width;
                e.setVelocityX(0);
            }
        }
        e.setX(newX);

        boolean grounded = false;
        Rectangle vert = new Rectangle(e.getX(), newY, e.getWidth(), e.getHeight());
        for (Platform p : room.platforms) {
            if (vert.overlaps(p.bounds)) {
                if (e.getVelocityY() < 0) { newY = p.bounds.y + p.bounds.height; grounded = true; }
                else if (e.getVelocityY() > 0) newY = p.bounds.y - e.getHeight();
                e.setVelocityY(0);
                vert.y = newY;
            }
        }
        for (com.hollowknight.model.world.Hazard h : room.hazards) {
            if (vert.overlaps(h.bounds)) {
                if (e.getVelocityY() < 0) { newY = h.bounds.y + h.bounds.height; grounded = true; }
                else if (e.getVelocityY() > 0) newY = h.bounds.y - e.getHeight();
                e.setVelocityY(0);
            }
        }
        e.setY(newY);
        e.setOnGround(grounded);
    }


    public static boolean hasLineOfSight(float x1, float y1, float x2, float y2, Room room) {
        int steps = 20;
        for (int i = 0; i <= steps; i++) {
            float t = i / (float) steps;
            float x = x1 + (x2 - x1) * t;
            float y = y1 + (y2 - y1) * t;
            for (Platform p : room.platforms) {
                if (p.bounds.contains(x, y)) return false;
            }
        }
        return true;
    }
}