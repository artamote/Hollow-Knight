package com.hollowknight.model.world.tiled;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Rectangle;
import com.hollowknight.model.entity.enemy.EnemyModel;
import com.hollowknight.model.entity.enemy.flying.MosquitoModel;
import com.hollowknight.model.entity.enemy.flying.MossflyModel;
import com.hollowknight.model.entity.enemy.ground.CrawlidModel;
import com.hollowknight.model.entity.enemy.ground.CrystalGuardianModel;
import com.hollowknight.model.entity.enemy.ground.HuskHornheadModel;
import com.hollowknight.model.entity.enemy.ground.MosscreepModel;
import com.hollowknight.model.world.Room;


public class TiledRoomLoader {

    private final TmxMapLoader loader = new TmxMapLoader();

    public Room load(String tmxPath, String id, String name, String music) {
        TiledMap map = loader.load(tmxPath);
        Room room = new Room(id, name, music);
        room.tiledMap = map;

        MapProperties mapProps = map.getProperties();
        int mapWidthTiles  = mapProps.get("width", Integer.class);
        int mapHeightTiles = mapProps.get("height", Integer.class);
        int tileWidth  = mapProps.get("tilewidth", Integer.class);
        int tileHeight = mapProps.get("tileheight", Integer.class);
        room.width  = mapWidthTiles * tileWidth;
        room.height = mapHeightTiles * tileHeight;

        MapLayer collisions = map.getLayers().get("Collisions");
        if (collisions != null) {
            for (MapObject obj : collisions.getObjects()) {
                Rectangle r = rectOf(obj);
                if (r != null) room.addPlatform(r.x, r.y, r.width, r.height);
            }
        }

        MapLayer hazards = map.getLayers().get("Hazards");
        if (hazards != null) {
            for (MapObject obj : hazards.getObjects()) {
                Rectangle r = rectOf(obj);
                if (r == null) continue;
                int damage = obj.getProperties().get("damage", 1, Integer.class);
                room.addHazard(r.x, r.y, r.width, r.height, damage);
            }
        }


        MapLayer transitionsLayer = map.getLayers().get("Transitions");
        if (transitionsLayer != null) {
            for (MapObject obj : transitionsLayer.getObjects()) {
                Rectangle r = rectOf(obj);
                if (r == null) continue;
                String targetRoom = obj.getProperties().get("targetRoom", "", String.class);
                if (!targetRoom.isEmpty()) {
                    room.addTransition(r.x, r.y, r.width, r.height, targetRoom);
                }
            }
        }

        MapLayer enemiesLayer = map.getLayers().get("Enemies");
        if (enemiesLayer != null) {
            for (MapObject obj : enemiesLayer.getObjects()) {
                MapProperties p = obj.getProperties();
                float x = p.get("x", 0f, Float.class);
                float y = p.get("y", 0f, Float.class);
                String type = p.get("type", "", String.class);
                EnemyModel enemy = createEnemy(type, x, y);
                if (enemy != null) room.addEnemy(enemy);
            }
        }

        MapLayer spawnLayer = map.getLayers().get("Spawn");
        if (spawnLayer != null) {
            for (MapObject obj : spawnLayer.getObjects()) {
                if ("PlayerSpawn".equals(obj.getName())) {
                    MapProperties p = obj.getProperties();
                    room.spawnX = p.get("x", 0f, Float.class);
                    room.spawnY = p.get("y", 0f, Float.class);
                }
            }
        }


        MapLayer bossSpawnLayer = map.getLayers().get("BossSpawn");
        if (bossSpawnLayer != null) {
            for (MapObject obj : bossSpawnLayer.getObjects()) {
                MapProperties p = obj.getProperties();
                room.bossSpawnX = p.get("x", 0f, Float.class);
                room.bossSpawnY = p.get("y", 0f, Float.class);
                room.hasBossSpawn = true;
                break; // only one boss spawn point expected
            }
        }


        MapLayer checkpointsLayer = map.getLayers().get("Checkpoints");
        if (checkpointsLayer != null) {
            for (MapObject obj : checkpointsLayer.getObjects()) {
                MapProperties p = obj.getProperties();
                float cx = p.get("x", 0f, Float.class);
                float cy = p.get("y", 0f, Float.class);
                room.addCheckpoint(cx, cy);
            }
        }

        return room;
    }


    private Rectangle rectOf(MapObject obj) {
        if (obj instanceof RectangleMapObject) {
            return ((RectangleMapObject) obj).getRectangle();
        }
        com.badlogic.gdx.Gdx.app.error("TiledRoomLoader",
                "Object '" + obj.getName() + "' in a Collisions/Hazards layer isn't a plain rectangle — skipped. " +
                        "Draw it as a rectangle in Tiled (not point/ellipse/polygon).");
        return null;
    }


    private EnemyModel createEnemy(String type, float x, float y) {
        switch (type) {
            case "CRAWLID":          return new CrawlidModel(x, y);
            case "MOSQUITO":         return new MosquitoModel(x, y);
            case "HUSK_HORNHEAD":    return new HuskHornheadModel(x, y);
            case "CRYSTAL_GUARDIAN": return new CrystalGuardianModel(x, y);
            case "MOSSCREEP":        return new MosscreepModel(x, y);
            case "MOSSFLY":          return new MossflyModel(x, y);
            default:
                com.badlogic.gdx.Gdx.app.error("TiledRoomLoader",
                        "Unknown enemy type '" + type + "' in map — skipped. " +
                                "Add a case in TiledRoomLoader.createEnemy() once its model class exists.");
                return null;
        }
    }
}