package com.hollowknight.view.anim;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.hollowknight.model.entity.boss.FalseKnightModel.BossState;
import com.hollowknight.model.entity.enemy.EnemyModel.EnemyType;
import com.hollowknight.util.SpriteSheetLoader;

import java.util.EnumMap;
import java.util.Map;


public class EnemyAnimations {


    public static class Entry {
        public final Animation<TextureRegion> anim;
        public final boolean loop;
        public Entry(Animation<TextureRegion> anim, boolean loop) { this.anim = anim; this.loop = loop; }
    }

    private final Map<EnemyType, Entry> walkAnims    = new EnumMap<>(EnemyType.class);
    private final Map<EnemyType, Entry> specialAnims = new EnumMap<>(EnemyType.class);
    private final Map<EnemyType, Entry> deathAnims   = new EnumMap<>(EnemyType.class);
    private final Map<BossState, Entry> bossAnims    = new EnumMap<>(BossState.class);
    private final Map<BossState, Entry> bossDeathAnims = new EnumMap<>(BossState.class);

    public EnemyAnimations(AssetManager manager) {
        buildCrawlid(manager);
        buildMosquito(manager);
        buildHusk(manager);
        buildCrystalGuardian(manager);
        buildMosscreep(manager);
        buildMossfly(manager);
        buildFalseKnight(manager);
    }

    private Texture tex(AssetManager manager, String path) {
        Texture t = manager.get(path, Texture.class);
        t.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
        return t;
    }

    private Entry entry(Texture sheet, int frameW, int frameH, float frameDuration, boolean loop) {
        TextureRegion[] frames = SpriteSheetLoader.sliceAuto(sheet, frameW, frameH);
        Animation<TextureRegion> anim = new Animation<>(frameDuration, frames);
        anim.setPlayMode(loop ? Animation.PlayMode.LOOP : Animation.PlayMode.NORMAL);
        return new Entry(anim, loop);
    }


    private Entry entryRange(Texture sheet, int frameW, int frameH, int from, int to, float frameDuration, boolean loop) {
        TextureRegion[] all = SpriteSheetLoader.sliceAuto(sheet, frameW, frameH);
        TextureRegion[] sub = new TextureRegion[to - from];
        System.arraycopy(all, from, sub, 0, to - from);
        Animation<TextureRegion> anim = new Animation<>(frameDuration, sub);
        anim.setPlayMode(loop ? Animation.PlayMode.LOOP : Animation.PlayMode.NORMAL);
        return new Entry(anim, loop);
    }


    private void buildCrawlid(AssetManager m) {
        Texture walk  = tex(m, "sprites/enemies/crawlid/walk.png");
        Texture death = tex(m, "sprites/enemies/crawlid/death.png");
        walkAnims.put(EnemyType.CRAWLID,  entry(walk, 301, 149, 0.1f, true));
        deathAnims.put(EnemyType.CRAWLID, entry(death, 202, 177, 0.12f, false));
    }


    private void buildMosquito(AssetManager m) {
        Texture idle   = tex(m, "sprites/enemies/mosquito/idle.png");
        Texture attack = tex(m, "sprites/enemies/mosquito/attack.png");
        Texture death  = tex(m, "sprites/enemies/mosquito/death.png");
        walkAnims.put(EnemyType.MOSQUITO,    entry(idle, 220, 155, 0.08f, true));
        specialAnims.put(EnemyType.MOSQUITO, entry(attack, 220, 155, 0.05f, false));
        deathAnims.put(EnemyType.MOSQUITO,   entry(death, 220, 155, 0.12f, false));
    }


    private void buildHusk(AssetManager m) {
        Texture idle   = tex(m, "sprites/enemies/husk/idle.png");
        Texture walk   = tex(m, "sprites/enemies/husk/walk.png");
        Texture attack = tex(m, "sprites/enemies/husk/attack.png");
        Texture death  = tex(m, "sprites/enemies/husk/death.png");
        walkAnims.put(EnemyType.HUSK_HORNHEAD,    entry(walk, 239, 219, 0.1f, true));
        specialAnims.put(EnemyType.HUSK_HORNHEAD, entry(attack, 239, 219, 0.045f, true)); // charge
        deathAnims.put(EnemyType.HUSK_HORNHEAD,   entry(death, 239, 219, 0.1f, false));
        huskIdle = entry(idle, 239, 219, 0.15f, true);
    }
    private Entry huskIdle;


    private void buildCrystalGuardian(AssetManager m) {
        Texture idle  = tex(m, "sprites/enemies/crystal_guardian/idle.png");
        Texture run   = tex(m, "sprites/enemies/crystal_guardian/run.png");
        Texture shoot = tex(m, "sprites/enemies/crystal_guardian/shoot.png");
        Texture death = tex(m, "sprites/enemies/crystal_guardian/death.png");
        walkAnims.put(EnemyType.CRYSTAL_GUARDIAN,    entry(idle, 285, 189, 0.15f, true));
        specialAnims.put(EnemyType.CRYSTAL_GUARDIAN, entry(shoot, 285, 189, 0.085f, true));
        deathAnims.put(EnemyType.CRYSTAL_GUARDIAN,   entry(death, 285, 189, 0.12f, false));
        crystalGuardianEnraged = entry(run, 285, 189, 0.05f, true);
        Texture beam = tex(m, "sprites/enemies/crystal_guardian/laser_beam.png");
        crystalGuardianLaserBeam = entry(beam, 195, 117, 0.05f, true);
    }
    private Entry crystalGuardianEnraged;
    private Entry crystalGuardianLaserBeam;


    private void buildMosscreep(AssetManager m) {
        Texture walk  = tex(m, "sprites/enemies/mosscreep/walk.png");
        Texture death = tex(m, "sprites/enemies/mosscreep/death.png");
        walkAnims.put(EnemyType.MOSSCREEP,  entry(walk, 157, 123, 0.15f, true));
        deathAnims.put(EnemyType.MOSSCREEP, entry(death, 157, 123, 0.15f, false));
    }

    private void buildMossfly(AssetManager m) {
        Texture appear = tex(m, "sprites/enemies/mossfly/appear.png");
        Texture fly    = tex(m, "sprites/enemies/mossfly/fly.png");
        Texture death  = tex(m, "sprites/enemies/mossfly/death.png");


        walkAnims.put(EnemyType.MOSSFLY,    entry(fly, 181, 141, 0.06f, true));
        specialAnims.put(EnemyType.MOSSFLY, entryRange(appear, 181, 141, 0, 6, 0.1f, false));
        deathAnims.put(EnemyType.MOSSFLY,   entry(death, 157, 123, 0.12f, false));

        mossflyHidden = entryRange(appear, 181, 141, 0, 3, 0.2f, true);
    }
    private Entry mossflyHidden;
    private Entry bossStunIdle;


    private void buildFalseKnight(AssetManager m) {
        Texture idle          = tex(m, "sprites/enemies/false_knight/idle.png");
        Texture run           = tex(m, "sprites/enemies/false_knight/run.png");
        Texture jumpAntic     = tex(m, "sprites/enemies/false_knight/jump_antic.png");
        Texture jump          = tex(m, "sprites/enemies/false_knight/jump.png");
        Texture jumpAttack    = tex(m, "sprites/enemies/false_knight/jump_attack.png");
        Texture land          = tex(m, "sprites/enemies/false_knight/land.png");
        Texture attackAntic   = tex(m, "sprites/enemies/false_knight/attack_antic.png");
        Texture attack        = tex(m, "sprites/enemies/false_knight/attack.png");
        Texture attackRecover = tex(m, "sprites/enemies/false_knight/attack_recover.png");
        Texture stunRecover   = tex(m, "sprites/enemies/false_knight/stun_recover.png");
        Texture stunIdle      = tex(m, "sprites/enemies/false_knight/stun_idle.png");
        Texture deathFall     = tex(m, "sprites/enemies/false_knight/death_fall.png");
        Texture deathLand     = tex(m, "sprites/enemies/false_knight/death_land.png");

        int fw = 1095, fh = 636;
        bossAnims.put(BossState.IDLE,           entry(idle, fw, fh, 0.15f, true));
        bossAnims.put(BossState.CHARGE_RUN,     entry(run, fw, fh, 0.05f, true));
        bossAnims.put(BossState.MACE_SLAM,      entry(attackAntic, fw, fh, 0.08f, true));
        bossAnims.put(BossState.POWER_SLAM,     entry(attack, fw, fh, 0.06f, true));
        bossAnims.put(BossState.OFFENSIVE_LEAP, entry(jump, fw, fh, 0.08f, true));
        bossAnims.put(BossState.DEFENSIVE_LEAP, entry(jumpAntic, fw, fh, 0.08f, true));
        bossAnims.put(BossState.STUNNED,        entry(stunRecover, fw, fh, 0.15f, false));
        bossStunIdle = entry(stunIdle, fw, fh, 0.12f, true);
        bossAnims.put(BossState.DEAD,           entry(deathLand, fw, fh, 0.1f, false));
        bossJumpAttack   = entry(jumpAttack, fw, fh, 0.06f, true);
        bossLand         = entry(land, fw, fh, 0.08f, false);
        bossAttackRecover= entry(attackRecover, fw, fh, 0.08f, false);
        bossDeathFall    = entry(deathFall, fw, fh, 0.1f, false);
        TextureRegion[] shockwaveFrames = new TextureRegion[5];
        for (int i = 0; i < 5; i++) {
            Texture t = tex(m, String.format("sprites/enemies/false_knight/shockwave_%03d.png", i));
            shockwaveFrames[i] = new TextureRegion(t);
        }
        bossShockwave = new Animation<>(0.05f, shockwaveFrames);
        bossShockwave.setPlayMode(Animation.PlayMode.LOOP);
    }
    private Entry bossJumpAttack, bossLand, bossAttackRecover, bossDeathFall;
    private Animation<TextureRegion> bossShockwave;

    public Entry getWalk(EnemyType type) { return walkAnims.get(type); }
    public Entry getSpecial(EnemyType type) {
        Entry e = specialAnims.get(type);
        return e != null ? e : walkAnims.get(type);
    }
    public Entry getDeath(EnemyType type) { return deathAnims.get(type); }

    public Entry getHuskIdle() { return huskIdle; }
    public Entry getCrystalGuardianEnraged() { return crystalGuardianEnraged; }
    public Entry getCrystalGuardianLaserBeam() { return crystalGuardianLaserBeam; }
    public Entry getMossflyHidden() { return mossflyHidden; }
    public Entry getBossStunIdle() { return bossStunIdle; }

    public Entry getBoss(BossState state) {
        Entry e = bossAnims.get(state);
        return e != null ? e : bossAnims.get(BossState.IDLE);
    }
    public Entry getBossJumpAttack()    { return bossJumpAttack; }
    public Entry getBossLand()          { return bossLand; }
    public Entry getBossAttackRecover() { return bossAttackRecover; }
    public Entry getBossDeathFall()     { return bossDeathFall; }
    public Animation<TextureRegion> getBossShockwave() { return bossShockwave; }
}