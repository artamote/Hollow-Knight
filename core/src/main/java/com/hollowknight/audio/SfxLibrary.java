package com.hollowknight.audio;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;

import java.util.Random;


public class SfxLibrary {

    private final AssetManager manager;
    private final java.util.Map<String, Sound> sounds = new java.util.HashMap<>();
    private final Random rng = new Random();


    public static final String[] ALL_KEYS = {
            "false_knight_attack_new_01", "false_knight_attack_new_02", "false_knight_attack_new_03",
            "false_knight_attack_new_04", "false_knight_attack_new_05",
            "zote_01", "zote_02", "zote_03", "zote_04", "zote_05",
            "zote_balloon_idle_02", "zote_balloon_idle_03",
            "zote_balloon_pre_burst_01", "zote_balloon_pre_burst_02", "zote_balloon_pre_burst_03",
            "zote_battle_attack_loop", "zote_battle_death", "zote_battle_defeat_end",
            "zote_battle_defeated_loop", "zote_complain_combined",
            "boss_defeat", "boss_explode", "boss_final_hit",
            "breakable_wall_death", "breakable_wall_hit_1", "breakable_wall_hit_2",
            "false_knight_ceiling_break", "false_knight_damage_armour", "false_knight_damage_armour_final",
            "false_knight_head_damage_2", "false_knight_jump", "false_knight_land",
            "false_knight_land_1st_time", "false_knight_roll", "false_knight_strike_ground", "false_knight_swing",
            "fly_flying",
            "focus_health_charging", "focus_health_heal", "focus_ready",
            "heart_piece_idle_loop", "heartbeat_b_01", "heartbeat_b_02", "heartbeat_b_03",
            "hero_damage", "hero_damage_less_harsh", "hero_damage_less_harsh_2",
            "hero_dash", "hero_death_extra_details", "hero_death_v2", "hero_double_damage",
            "hero_nail_art_charge_complete", "hero_nail_art_charge_initiate", "hero_nail_art_charge_loop",
            "hero_nail_art_cyclone_slash_long", "hero_nail_art_great_slash",
            "hero_run_footsteps_stone", "hero_scream_spell",
            "soul_pickup_1", "soul_pickup_2", "soul_pickup_3", "soul_pickup_4",
            "soul_pickup_5", "soul_pickup_6", "soul_pickup_7",
            "sword_1", "sword_2", "sword_3", "sword_4", "sword_5", "sword_hit_reject",
    };


    private static final String[][] KEY_TO_FILE = {
            {"false_knight_attack_new_01", "False_Knight_Attack_New_01.wav"},
            {"false_knight_attack_new_02", "False_Knight_Attack_New_02.wav"},
            {"false_knight_attack_new_03", "False_Knight_Attack_New_03.wav"},
            {"false_knight_attack_new_04", "False_Knight_Attack_New_04.wav"},
            {"false_knight_attack_new_05", "False_Knight_Attack_New_05.wav"},
            {"zote_01", "Zote_01.wav"}, {"zote_02", "Zote_02.wav"}, {"zote_03", "Zote_03.wav"},
            {"zote_04", "Zote_04.wav"}, {"zote_05", "Zote_05.wav"},
            {"zote_balloon_idle_02", "Zote_Balloon_idle_02.wav"},
            {"zote_balloon_idle_03", "Zote_Balloon_idle_03.wav"},
            {"zote_balloon_pre_burst_01", "Zote_Balloon_pre_burst_01.wav"},
            {"zote_balloon_pre_burst_02", "Zote_Balloon_pre_burst_02.wav"},
            {"zote_balloon_pre_burst_03", "Zote_Balloon_pre_burst_03.wav"},
            {"zote_battle_attack_loop", "Zote_battle_attack_loop.wav"},
            {"zote_battle_death", "Zote_battle_death.wav"},
            {"zote_battle_defeat_end", "Zote_battle_defeat_end.wav"},
            {"zote_battle_defeated_loop", "Zote_battle_defeated_loop.wav"},
            {"zote_complain_combined", "Zote_complain_combined.wav"},
            {"boss_defeat", "boss_defeat.wav"},
            {"heartbeat_b_01", "heartbeat_B_01.wav"},
            {"heartbeat_b_02", "heartbeat_B_02.wav"},
            {"heartbeat_b_03", "heartbeat_B_03.wav"},
    };

    public SfxLibrary(AssetManager manager) {
        this.manager = manager;
        for (String key : ALL_KEYS) {
            sounds.put(key, manager.get(path(key), Sound.class));
        }
    }

    private static String path(String key) {
        for (String[] entry : KEY_TO_FILE) {
            if (entry[0].equals(key)) return "sfx/" + entry[1];
        }
        return "sfx/" + key + ".wav";
    }


    public static String[] allPaths() {
        String[] paths = new String[ALL_KEYS.length];
        for (int i = 0; i < ALL_KEYS.length; i++) paths[i] = path(ALL_KEYS[i]);
        return paths;
    }

    public Sound get(String key) {
        Sound s = sounds.get(key);
        if (s == null) throw new IllegalArgumentException("No SFX registered for key: " + key);
        return s;
    }

    public Sound pickRandom(String prefix, int from, int to) {
        int n = from + rng.nextInt(to - from + 1);
        return get(prefix + n);
    }
}