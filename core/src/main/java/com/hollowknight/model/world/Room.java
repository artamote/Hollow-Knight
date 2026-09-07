package com.hollowknight.model.world;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.hollowknight.model.entity.enemy.EnemyModel;
import java.util.ArrayList;
import java.util.List;


public class Room {
    public final String id;
    public final String displayName;
    public final String musicTrack;
    public final List<Platform> platforms = new ArrayList<>();
    public final List<Hazard>   hazards   = new ArrayList<>();
    public final List<EnemyModel> enemies = new ArrayList<>();
    public float spawnX, spawnY;


    public float width, height;


    public boolean hasBossSpawn = false;
    public float bossSpawnX, bossSpawnY;

    public TiledMap tiledMap;

    public final List<RoomTransition> transitions = new ArrayList<>();

    public static class RoomTransition {
        public final com.badlogic.gdx.math.Rectangle bounds;
        public final String targetRoomId;
        public RoomTransition(float x, float y, float w, float h, String targetRoomId) {
            this.bounds = new com.badlogic.gdx.math.Rectangle(x, y, w, h);
            this.targetRoomId = targetRoomId;
        }
    }

    public void addTransition(float x, float y, float w, float h, String targetRoomId) {
        transitions.add(new RoomTransition(x, y, w, h, targetRoomId));
    }


    public final List<com.badlogic.gdx.math.Rectangle> checkpoints = new ArrayList<>();

    private static final float CHECKPOINT_TRIGGER_SIZE = 60f; // square trigger zone centered on each defined point

    public void addCheckpoint(float x, float y) {
        float half = CHECKPOINT_TRIGGER_SIZE / 2f;
        checkpoints.add(new com.badlogic.gdx.math.Rectangle(x - half, y - half, CHECKPOINT_TRIGGER_SIZE, CHECKPOINT_TRIGGER_SIZE));
    }

    public Room(String id, String displayName, String musicTrack) {
        this.id = id;
        this.displayName = displayName;
        this.musicTrack = musicTrack;
    }

    public void addPlatform(float x, float y, float w, float h) {
        platforms.add(new Platform(x, y, w, h));
    }

    public void addHazard(float x, float y, float w, float h, int damage) {
        hazards.add(new Hazard(x, y, w, h, damage));
    }

    public void addEnemy(EnemyModel e) {
        enemies.add(e);
    }
}