package com.hollowknight.audio;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.hollowknight.util.GameSettings;

import java.util.HashMap;
import java.util.Map;


public class MusicManager {

    public static final String MENU_THEME = "music/main_theme.mp3";

    public static final String[] ALL_PATHS = {
            MENU_THEME,
            "music/crossroads.mp3",
            "music/greenpath.mp3",
            "music/boss.mp3",
            "music/champion.mp3",
    };

    private static final float FADE_DURATION = 1.2f;

    private enum FadeState { NONE, FADING_OUT, FADING_IN }

    private final AssetManager manager;
    private final GameSettings settings;
    private final Map<String, Music> tracks = new HashMap<>();

    private Music current;
    private String currentPath;
    private Music incoming;
    private String incomingPath;
    private FadeState fadeState = FadeState.NONE;
    private float fadeTimer = 0f;

    public MusicManager(AssetManager manager, GameSettings settings) {
        this.manager = manager;
        this.settings = settings;
    }

    private Music get(String path) {
        Music m = tracks.get(path);
        if (m == null) {
            m = manager.get(path, Music.class);
            m.setLooping(true);
            tracks.put(path, m);
        }
        return m;
    }

    public void playTrack(String path) {
        if (path == null || path.isEmpty()) return;
        if (path.equals(currentPath) && fadeState != FadeState.FADING_OUT) return;
        if (path.equals(incomingPath) && fadeState == FadeState.FADING_OUT) return;

        incomingPath = path;
        incoming = get(path);

        if (current == null) {
            current = incoming;
            currentPath = path;
            incoming = null;
            incomingPath = null;
            current.setVolume(0f);
            current.play();
            fadeState = FadeState.FADING_IN;
            fadeTimer = 0f;
        } else {
            fadeState = FadeState.FADING_OUT;
            fadeTimer = 0f;
        }
    }

    public void update(float delta) {
        float target = settings.getMusicVolume();

        switch (fadeState) {
            case FADING_OUT:
                fadeTimer += delta;
                float outT = Math.min(1f, fadeTimer / FADE_DURATION);
                if (current != null) current.setVolume(target * (1f - outT));
                if (outT >= 1f) {
                    if (current != null) current.stop();
                    current = incoming;
                    currentPath = incomingPath;
                    incoming = null;
                    incomingPath = null;
                    if (current != null) {
                        current.setVolume(0f);
                        current.play();
                    }
                    fadeState = FadeState.FADING_IN;
                    fadeTimer = 0f;
                }
                break;
            case FADING_IN:
                fadeTimer += delta;
                float inT = Math.min(1f, fadeTimer / FADE_DURATION);
                if (current != null) current.setVolume(target * inT);
                if (inT >= 1f) fadeState = FadeState.NONE;
                break;
            case NONE:
                if (current != null) current.setVolume(target);
                break;
        }
    }

    public void stopAll() {
        if (current != null) current.stop();
        if (incoming != null) incoming.stop();
        current = null;
        incoming = null;
        currentPath = null;
        incomingPath = null;
        fadeState = FadeState.NONE;
    }
}