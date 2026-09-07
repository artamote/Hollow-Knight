package com.hollowknight.util;


public final class Constants {

    private Constants() {}

    
    public static final int SCREEN_WIDTH  = 1280;
    public static final int SCREEN_HEIGHT = 720;
    public static final String TITLE = "Hollow Knight - AP Project";

    
    public static final float GRAVITY           = -1800f;  
    public static final float TERMINAL_VELOCITY = -1200f;

    
    public static final float PLAYER_SPEED          = 280f;
    public static final float PLAYER_JUMP_VELOCITY  = 700f;
    public static final float PLAYER_JUMP_CUTOFF    = 200f;  
    public static final float PLAYER_DASH_SPEED     = 900f;
    public static final float PLAYER_DASH_DURATION  = 0.18f; 
    public static final float PLAYER_DASH_COOLDOWN  = 0.6f;
    public static final float PLAYER_WALL_SLIDE_SPEED = -80f; 
    public static final float PLAYER_INVINCIBLE_TIME  = 1.0f;
    public static final float PLAYER_DEATH_ANIM_TIME  = 1.44f; 
    public static final float PLAYER_FOCUS_TIME       = 1.5f;
    public static final int   PLAYER_MAX_MASKS        = 5;
    public static final int   SOUL_GAIN_PER_HIT       = 11;
    public static final int   SOUL_MAX                = 99;
    public static final int   SOUL_FOCUS_COST         = 33;  
    public static final float KNOCKBACK_FORCE         = 350f;
    public static final float ENEMY_KNOCKBACK_DURATION = 0.25f; 
    public static final float ENEMY_KNOCKBACK_POP_Y    = 180f;  
    public static final float HUSK_CHARGE_STOP_RECOIL   = 200f; 
    public static final float HUSK_CHARGE_STOP_DURATION = 0.35f; 

    
    public static final int   VENGEFUL_SPIRIT_COST    = 33;
    public static final float VENGEFUL_SPIRIT_SPEED   = 700f;
    public static final int   VENGEFUL_SPIRIT_DAMAGE  = 15;
    public static final int   HOWLING_WRAITHS_COST    = 33;
    public static final int   HOWLING_WRAITHS_DAMAGE  = 8; 
    public static final float HOWLING_WRAITHS_DURATION = 0.4f;

    
    public static final int   NAIL_DAMAGE      = 10;
    public static final float NAIL_COOLDOWN    = 0.25f;
    public static final float NAIL_RANGE_H     = 60f;  
    public static final float NAIL_RANGE_V     = 70f;  
    public static final float NAIL_HIT_WINDOW  = 0.12f; 
    public static final float NAIL_HIT_PADDING = 10f;   

    
    public static final float CAMERA_LERP_SPEED = 5f;

    
    public static final float POGO_BOUNCE_VELOCITY = 600f;
    public static final float POGO_INVINCIBLE_TIME = 0.3f; 

    
    public static final int SAVE_SLOTS = 4;

    
    public static final int MAX_NOTCHES = 3;

    
    public static final int TILE_SIZE = 32;
}