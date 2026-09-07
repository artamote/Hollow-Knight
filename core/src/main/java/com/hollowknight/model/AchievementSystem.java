package com.hollowknight.model;

import com.hollowknight.observer.EventBus;
import com.hollowknight.observer.GameEvent;
import java.util.EnumMap;
import java.util.Map;

public class AchievementSystem implements EventBus.Listener {

    public enum Achievement {
        COMPLETION      ("Completion",       "Finish the game"),
        SPEEDRUN        ("Speedrun",         "Finish in under 15 minutes"),
        TRUE_HUNTER     ("True Hunter",      "Kill every enemy type"),
        DEFEAT_FALSE_KNIGHT("Defeat False Knight", "Beat the boss");

        public final String title, description;
        Achievement(String t, String d) { title = t; description = d; }
    }

    private final Map<Achievement, Boolean> unlocked = new EnumMap<>(Achievement.class);

    private final java.util.Set<String> killedTypes = new java.util.HashSet<>();

    public AchievementSystem() {
        for (Achievement a : Achievement.values()) unlocked.put(a, false);
        EventBus.subscribe(GameEvent.BOSS_KILLED,     this);
        EventBus.subscribe(GameEvent.ENEMY_KILLED,    this);
        EventBus.subscribe(GameEvent.GAME_COMPLETED,  this);
        EventBus.subscribe(GameEvent.SPEEDRUN_FINISHED, this);
    }

    @Override
    public void onEvent(GameEvent event, Object data) {
        switch (event) {
            case BOSS_KILLED:
                unlock(Achievement.DEFEAT_FALSE_KNIGHT); break;
            case GAME_COMPLETED:
                unlock(Achievement.COMPLETION); break;
            case SPEEDRUN_FINISHED:
                unlock(Achievement.SPEEDRUN); break;
            case ENEMY_KILLED:
                if (data instanceof String) {
                    killedTypes.add((String) data);

                    if (killedTypes.size() >= 6) unlock(Achievement.TRUE_HUNTER);
                }
                break;
            default: break;
        }
    }

    private void unlock(Achievement a) {
        if (!unlocked.get(a)) {
            unlocked.put(a, true);
            EventBus.publish(GameEvent.ACHIEVEMENT_UNLOCKED, a);
        }
    }

    public boolean isUnlocked(Achievement a) { return unlocked.getOrDefault(a, false); }
    public Map<Achievement, Boolean> getAll() { return unlocked; }
}