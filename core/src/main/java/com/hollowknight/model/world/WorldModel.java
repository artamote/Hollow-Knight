package com.hollowknight.model.world;

import com.hollowknight.model.entity.enemy.ground.CrawlidModel;
import com.hollowknight.model.entity.enemy.ground.HuskHornheadModel;
import com.hollowknight.model.entity.enemy.flying.MosquitoModel;
import com.hollowknight.model.world.tiled.TiledRoomLoader;
import java.util.LinkedHashMap;
import java.util.Map;


public class WorldModel {

    private final Map<String, Room> rooms = new LinkedHashMap<>();
    private String currentRoomId;
    private final TiledRoomLoader tiledLoader = new TiledRoomLoader();

    public WorldModel() {
        buildForgottenCrossroads();
        buildGreenpath();
        buildFalseKnightArena();
        currentRoomId = "forgotten_crossroads";
    }


    private void buildFalseKnightArena() {
        Room r;
        try {
            r = tiledLoader.load("maps/false_knight.tmx",
                    "BOSS", "False Knight Arena", "music/boss.mp3");
        } catch (Exception e) {
            com.badlogic.gdx.Gdx.app.error("WorldModel",
                    "Could not load false_knight_arena.tmx, falling back to hardcoded room.", e);
            r = new Room("BOSS", "False Knight Arena", "music/boss.mp3");
            r.spawnX = 100; r.spawnY = 100;
            r.width = 1400; r.height = 720;
            r.hasBossSpawn = true; r.bossSpawnX = 700; r.bossSpawnY = 100;
            r.addPlatform(0, 0, 1400, 64);
            r.addPlatform(0, 64, 20, 656);
            r.addPlatform(1380, 64, 20, 656);
        }
        rooms.put(r.id, r);
    }

    private void buildForgottenCrossroads() {
        Room r;
        try {
            r = tiledLoader.load("maps/forgotten.tmx",
                    "forgotten_crossroads", "Forgotten Crossroads", "music/crossroads.mp3");
        } catch (Exception e) {

            com.badlogic.gdx.Gdx.app.error("WorldModel",
                    "Could not load forgotten_crossroads.tmx, falling back to hardcoded room. " +
                            "Check that core/assets is on the run configuration's classpath.", e);
            r = buildForgottenCrossroadsFallback();
        }
        rooms.put(r.id, r);
    }


    private Room buildForgottenCrossroadsFallback() {
        Room r = new Room("forgotten_crossroads", "Forgotten Crossroads", "music/crossroads.mp3");
        r.spawnX = 100; r.spawnY = 200;
        r.width = 2000; r.height = 720;
        r.addPlatform(0, 0, 2000, 64);
        r.addPlatform(300, 150, 120, 20);
        r.addPlatform(500, 250, 120, 20);
        r.addPlatform(750, 180, 120, 20);
        r.addHazard(900, 64, 100, 20, 1);
        r.addEnemy(new CrawlidModel(400, 80));
        r.addEnemy(new CrawlidModel(800, 80));
        r.addEnemy(new MosquitoModel(600, 300));
        r.addEnemy(new HuskHornheadModel(1200, 80));
        r.addTransition(1950, 0, 50, 720, "greenpath"); // walk off the right edge -> Greenpath
        return r;
    }

    private void buildGreenpath() {
        Room r;
        try {
            r = tiledLoader.load("maps/greenpath.tmx", "greenpath", "Greenpath", "music/greenpath.mp3");
        } catch (Exception e) {
            com.badlogic.gdx.Gdx.app.error("WorldModel",
                    "Could not load greenpath.tmx, falling back to hardcoded room.", e);
            r = buildGreenpathFallback();
        }
        rooms.put(r.id, r);
    }

    private Room buildGreenpathFallback() {
        Room r = new Room("greenpath", "Greenpath", "music/greenpath.mp3");
        r.spawnX = 100; r.spawnY = 200;
        r.width = 2000; r.height = 720;

        r.addPlatform(0, 0, 2000, 64);
        r.addPlatform(350, 180, 120, 20);
        r.addPlatform(600, 260, 120, 20);

        r.addHazard(1000, 64, 150, 20, 1);

        r.addEnemy(new com.hollowknight.model.entity.enemy.ground.MosscreepModel(450, 80));
        r.addEnemy(new com.hollowknight.model.entity.enemy.ground.MosscreepModel(900, 80));
        r.addEnemy(new com.hollowknight.model.entity.enemy.flying.MossflyModel(700, 220));
        r.addTransition(0, 0, 50, 720, "forgotten_crossroads"); // walk off the left edge -> back to Forgotten Crossroads

        return r;
    }

    public Room getCurrentRoom() { return rooms.get(currentRoomId); }
    public Room getRoom(String id) { return rooms.get(id); }
    public void setCurrentRoom(String id) {
        if (rooms.containsKey(id)) currentRoomId = id;
    }
    public String getCurrentRoomId() { return currentRoomId; }
    public Map<String, Room> getAllRooms() { return rooms; }
}