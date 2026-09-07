package com.hollowknight.util;

public class GameSettings {
    private float musicVolume = 0.7f;
    private float sfxVolume   = 0.8f;
    private boolean musicMuted = false;
    private boolean sfxMuted   = false;
    private float brightness = 1.0f;
    public float getMusicVolume()          { return musicMuted ? 0f : musicVolume; }
    public float getRawMusicVolume()       { return musicVolume; }
    public void setMusicVolume(float v)    { musicVolume = clamp(v); }
    public boolean isMusicMuted()          { return musicMuted; }
    public void setMusicMuted(boolean m)   { musicMuted = m; }
    public float getSfxVolume()            { return sfxMuted ? 0f : sfxVolume; }
    public float getRawSfxVolume()         { return sfxVolume; }
    public void setSfxVolume(float v)      { sfxVolume = clamp(v); }
    public boolean isSfxMuted()            { return sfxMuted; }
    public void setSfxMuted(boolean m)     { sfxMuted = m; }
    public float getBrightness()           { return brightness; }
    public void setBrightness(float b)     { brightness = Math.max(0.5f, Math.min(1.5f, b)); }
    public void resetToDefaults() {
        musicVolume = 0.7f;
        sfxVolume   = 0.8f;
        musicMuted  = false;
        sfxMuted    = false;
        brightness  = 1.0f;
    }

    private float clamp(float v) {
        return Math.max(0f, Math.min(1f, v));
    }
}