package com.hollowknight.model.save;


public class SaveData {
    public int     slot         = 0;
    public int     hp           = 5;
    public int     maxHp        = 5;
    public int     soul         = 0;
    public float   posX         = 200f;
    public float   posY         = 200f;
    public float   checkpointX  = 200f;
    public float   checkpointY  = 200f;
    public int     deaths       = 0;
    public int     kills        = 0;
    public float   timeElapsed  = 0f;
    public boolean bossDefeated = false;
    public int     bossHp       = 200;
    public String  currentRoom  = "forgotten_crossroads";


    public boolean achCompletion    = false;
    public boolean achSpeedrun      = false;
    public boolean achTrueHunter    = false;
    public boolean achFalseKnight   = false;


    public String equippedCharms = "";
}
