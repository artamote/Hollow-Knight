package com.hollowknight.model.entity;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;


public abstract class Entity {

    
    protected float x, y;
    protected float width, height;

    
    protected float velocityX, velocityY;
    protected boolean onGround;
    protected boolean facingRight = true;

    
    protected int maxHp;
    protected int hp;
    protected boolean alive = true;

    
    private final Rectangle bounds = new Rectangle();

    public Entity(float x, float y, float width, float height, int hp) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.maxHp = hp;
        this.hp = hp;
    }

    public Rectangle getBounds() {
        bounds.set(x, y, width, height);
        return bounds;
    }

    public void takeDamage(int amount) {
        if (!alive) return;
        hp = Math.max(0, hp - amount);
        if (hp == 0) alive = false;
    }

    public boolean isAlive() { return alive; }


    public float getX()        { return x; }
    public float getY()        { return y; }
    public float getWidth()    { return width; }
    public float getHeight()   { return height; }
    public float getVelocityX(){ return velocityX; }
    public float getVelocityY(){ return velocityY; }
    public boolean isOnGround(){ return onGround; }
    public boolean isFacingRight(){ return facingRight; }
    public int getHp()         { return hp; }
    public int getMaxHp()      { return maxHp; }

    public void setHp(int hp)  { this.hp = Math.max(0, Math.min(hp, maxHp)); this.alive = this.hp > 0; }
    public void setMaxHp(int m){ this.maxHp = Math.max(1, m); if (hp > maxHp) hp = maxHp; }
    public void incrementMaxHp(){ this.maxHp++; }
    public void setAlive(boolean a){ this.alive = a; }

    public void setX(float x)  { this.x = x; }
    public void setY(float y)  { this.y = y; }
    public void setVelocityX(float vx){ this.velocityX = vx; }
    public void setVelocityY(float vy){ this.velocityY = vy; }
    public void setOnGround(boolean g){ this.onGround = g; }
    public void setFacingRight(boolean r){ this.facingRight = r; }


    public float getCenterX() { return x + width / 2f; }

    public float getCenterY() { return y + height / 2f; }

    public Vector2 getPosition() { return new Vector2(x, y); }
}