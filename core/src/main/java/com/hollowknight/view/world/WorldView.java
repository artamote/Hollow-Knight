package com.hollowknight.view.world;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapLayer;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.hollowknight.model.world.Hazard;
import com.hollowknight.model.world.Platform;
import com.hollowknight.model.world.Room;

import java.util.IdentityHashMap;
import java.util.Map;


public class WorldView {

    private static final String FOREGROUND_LAYER_NAME = "foreground";
    private static final float OUTLINE_THICKNESS = 3f;

    
    private final Map<TiledMap, OrthogonalTiledMapRenderer> renderers = new IdentityHashMap<>();

    
    public void renderTilesBehindEntities(OrthographicCamera camera, Room room) {
        renderTiles(camera, room, false);
    }

    
    public void renderTilesInFrontOfEntities(OrthographicCamera camera, Room room) {
        renderTiles(camera, room, true);
    }

    private void renderTiles(OrthographicCamera camera, Room room, boolean foregroundPass) {
        if (room.tiledMap == null) return;
        OrthogonalTiledMapRenderer renderer = renderers.computeIfAbsent(room.tiledMap,
                OrthogonalTiledMapRenderer::new);
        renderer.setView(camera);

        int fgIndex = findLayerIndex(room.tiledMap, FOREGROUND_LAYER_NAME);
        if (fgIndex < 0) {
            
            
            if (!foregroundPass) renderer.render();
            return;
        }

        int total = room.tiledMap.getLayers().getCount();
        if (foregroundPass) {
            renderer.render(new int[]{fgIndex});
        } else {
            int[] indices = new int[total - 1];
            int idx = 0;
            for (int i = 0; i < total; i++) {
                if (i != fgIndex) indices[idx++] = i;
            }
            renderer.render(indices);
        }
    }

    private int findLayerIndex(TiledMap map, String nameIgnoreCase) {
        java.util.Iterator<MapLayer> it = map.getLayers().iterator();
        int i = 0;
        while (it.hasNext()) {
            MapLayer layer = it.next();
            if (layer.getName() != null && layer.getName().equalsIgnoreCase(nameIgnoreCase)) return i;
            i++;
        }
        return -1;
    }

    

    public void render(ShapeRenderer shapes, Room room) {

    }

    


    public void dispose() {
        for (OrthogonalTiledMapRenderer r : renderers.values()) r.dispose();
        renderers.clear();
    }
}