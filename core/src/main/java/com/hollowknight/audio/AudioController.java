package com.hollowknight.audio;

import com.badlogic.gdx.audio.Sound;
import com.hollowknight.observer.EventBus;
import com.hollowknight.observer.GameEvent;
import com.hollowknight.util.GameSettings;

public class AudioController {

    private final SfxLibrary sfx;
    private final GameSettings settings;

    private long focusChargeLoopId = -1;

    public AudioController(SfxLibrary sfx, GameSettings settings) {
        this.sfx = sfx;
        this.settings = settings;

        EventBus.subscribe(GameEvent.NAIL_SLASH, (e, data) ->
                play(sfx.pickRandom("sword_", 1, 5)));

        EventBus.subscribe(GameEvent.PLAYER_DAMAGED, (e, data) ->
                play(sfx.get("hero_damage")));

        EventBus.subscribe(GameEvent.PLAYER_DIED, (e, data) ->
                play(sfx.get("hero_death_v2")));

        EventBus.subscribe(GameEvent.PLAYER_SOUL_GAINED, (e, data) ->
                play(sfx.pickRandom("soul_pickup_", 1, 7)));

        EventBus.subscribe(GameEvent.PLAYER_DASHED, (e, data) ->
                play(sfx.get("hero_dash")));

        EventBus.subscribe(GameEvent.PLAYER_FOCUSED, (e, data) -> {
            stopFocusLoop();
            focusChargeLoopId = loop(sfx.get("focus_health_charging"));
        });

        EventBus.subscribe(GameEvent.PLAYER_FOCUS_STOPPED, (e, data) -> stopFocusLoop());

        EventBus.subscribe(GameEvent.PLAYER_HEALED, (e, data) ->
                play(sfx.get("focus_health_heal")));

        EventBus.subscribe(GameEvent.SPELL_CAST, (e, data) -> {
            if ("HOWLING_WRAITHS".equals(data)) {
                play(sfx.get("hero_scream_spell"));
            } else if ("VENGEFUL_SPIRIT".equals(data)) {
                play(sfx.get("hero_nail_art_charge_complete"));
            }
        });

        EventBus.subscribe(GameEvent.BOSS_ENTERED, (e, data) ->
                play(sfx.get("false_knight_ceiling_break")));

        EventBus.subscribe(GameEvent.BOSS_DAMAGED, (e, data) ->
                play(sfx.get("false_knight_damage_armour")));

        EventBus.subscribe(GameEvent.BOSS_STUNNED, (e, data) ->
                play(sfx.get("false_knight_damage_armour_final")));

        EventBus.subscribe(GameEvent.BOSS_PHASE2, (e, data) ->
                play(sfx.get("false_knight_strike_ground")));

        EventBus.subscribe(GameEvent.BOSS_KILLED, (e, data) ->
                play(sfx.get("boss_defeat")));
    }

    private void play(Sound sound) {
        sound.play(settings.getSfxVolume());
    }

    private long loop(Sound sound) {
        return sound.loop(settings.getSfxVolume());
    }

    private void stopFocusLoop() {
        if (focusChargeLoopId >= 0) {
            sfx.get("focus_health_charging").stop(focusChargeLoopId);
            focusChargeLoopId = -1;
        }
    }
}