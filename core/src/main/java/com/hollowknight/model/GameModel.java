package com.hollowknight.model;

import com.hollowknight.model.charm.CharmManager;
import com.hollowknight.model.entity.boss.FalseKnightModel;
import com.hollowknight.model.entity.player.PlayerModel;
import com.hollowknight.model.spell.HowlingWraithsModel;
import com.hollowknight.model.spell.VengefulSpiritModel;
import com.hollowknight.model.world.Room;
import com.hollowknight.model.world.WorldModel;
import java.util.ArrayList;
import java.util.List;


public class GameModel {

    public final PlayerModel    player;
    public final WorldModel     world;
    public final CharmManager   charms;
    public final AchievementSystem achievements;

    public FalseKnightModel boss; // null until boss room entered
    public boolean paused = false;
    public boolean inInventory = false;
    public boolean victory = false;
    public boolean bossDeathPending = false;
    public float victoryDelayTimer = 2.5f;

    public final List<VengefulSpiritModel> activeSpirits = new ArrayList<>();
    public final List<HowlingWraithsModel> activeWraiths = new ArrayList<>();
    public final List<com.hollowknight.model.spell.ImpactEffectModel> activeImpacts = new ArrayList<>();


    public boolean godMode = false;
    public boolean noclip  = false;


    public int saveSlot = -1;

    public GameModel() {
        world        = new WorldModel();
        Room startRoom = world.getCurrentRoom();
        player       = new PlayerModel(startRoom.spawnX, startRoom.spawnY);
        charms       = new CharmManager();
        achievements = new AchievementSystem();
    }

    public void applySaveData(com.hollowknight.model.save.SaveData data) {
        if (data == null) return;
        saveSlot = data.slot;
        world.setCurrentRoom(data.currentRoom);
        int max = Math.max(1, data.maxHp);
        while (player.getMaxHp() < max) player.incrementMaxHp();
        player.setHp(data.hp);
        player.setSoul(data.soul);
        player.setX(data.posX);
        player.setY(data.posY);
        player.setCheckpoint(data.checkpointX, data.checkpointY);
        player.setTimeElapsed(data.timeElapsed);
        player.setDeathCount(data.deaths);
        player.setKillCount(data.kills);
if (data.equippedCharms != null && !data.equippedCharms.isEmpty()) {
            for (String name : data.equippedCharms.split(",")) {
                try {
                    charms.equip(com.hollowknight.model.charm.Charm.valueOf(name.trim()));
                } catch (IllegalArgumentException ignored) { /* unknown charm name, skip */ }
            }
        }
    }
public com.hollowknight.model.save.SaveData toSaveData(int slot) {
        com.hollowknight.model.save.SaveData d = new com.hollowknight.model.save.SaveData();
        d.slot = slot;
        d.hp = player.getHp();
        d.maxHp = player.getMaxHp();
        d.soul = player.getSoul();
        d.posX = player.getX();
        d.posY = player.getY();
        d.checkpointX = player.getCheckpointX();
        d.checkpointY = player.getCheckpointY();
        d.deaths = player.getDeathCount();
        d.kills = player.getKillCount();
        d.timeElapsed = player.getTimeElapsed();
        d.bossDefeated = boss != null && !boss.isAlive();
        d.currentRoom = world.getCurrentRoomId();
        StringBuilder sb = new StringBuilder();
        for (com.hollowknight.model.charm.Charm c : charms.getEquipped()) {
            if (sb.length() > 0) sb.append(",");
            sb.append(c.name());
        }
        d.equippedCharms = sb.toString();
        return d;
    }

    public void update(float delta) {
        if (paused) return;
        player.addTime(delta);

        if (bossDeathPending) {
            victoryDelayTimer -= delta;
            if (victoryDelayTimer <= 0) {
                bossDeathPending = false;
                victory = true;
            }
        }
    }
}