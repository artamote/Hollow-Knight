package com.hollowknight.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.hollowknight.model.AchievementSystem.Achievement;

import java.util.EnumSet;
import java.util.Set;



public final class AchievementStore {

    private static final String FILE = "saves/achievements.txt";

    private AchievementStore() {}

    public static Set<Achievement> loadUnlocked() {
        Set<Achievement> set = EnumSet.noneOf(Achievement.class);
        FileHandle f = Gdx.files.local(FILE);
        if (!f.exists()) return set;
        for (String line : f.readString().split("\n")) {
            String name = line.trim();
            if (name.isEmpty()) continue;
            try {
                set.add(Achievement.valueOf(name));
            } catch (IllegalArgumentException ignored) {  }
        }
        return set;
    }

    public static void markUnlocked(Achievement a) {
        Set<Achievement> set = loadUnlocked();
        if (set.add(a)) {
            StringBuilder sb = new StringBuilder();
            for (Achievement x : set) sb.append(x.name()).append("\n");
            Gdx.files.local(FILE).writeString(sb.toString(), false);
        }
    }

    public static boolean isUnlocked(Achievement a) {
        return loadUnlocked().contains(a);
    }
}