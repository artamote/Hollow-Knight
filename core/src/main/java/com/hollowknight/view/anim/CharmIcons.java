package com.hollowknight.view.anim;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.hollowknight.model.charm.Charm;

import java.util.EnumMap;
import java.util.Map;

public class CharmIcons {

    private final Map<Charm, TextureRegion> icons = new EnumMap<>(Charm.class);

    private static final String[][] PATHS = {
            {"SOUL_CATCHER",        "sprites/charms/soul_catcher.png"},
            {"DASHMASTER",          "sprites/charms/dashmaster.png"},
            {"UNBREAKABLE_STRENGTH","sprites/charms/unbreakable_strength.png"},
            {"QUICK_SLASH",         "sprites/charms/quick_slash.png"},
            {"QUICK_FOCUS",         "sprites/charms/quick_focus.png"},
            {"HEAVY_BLOW",          "sprites/charms/heavy_blow.png"},
            {"SHARP_SHADOW",        "sprites/charms/sharp_shadow.png"},
    };

    public CharmIcons(AssetManager manager) {
        for (String[] entry : PATHS) {
            Charm charm = Charm.valueOf(entry[0]);
            Texture tex = manager.get(entry[1], Texture.class);
            tex.setFilter(TextureFilter.Linear, TextureFilter.Linear);
            icons.put(charm, new TextureRegion(tex));
        }
    }


    public TextureRegion get(Charm charm) {
        return icons.get(charm);
    }

    public boolean has(Charm charm) {
        return icons.containsKey(charm);
    }

    public static String[] allPaths() {
        String[] paths = new String[PATHS.length];
        for (int i = 0; i < PATHS.length; i++) paths[i] = PATHS[i][1];
        return paths;
    }
}