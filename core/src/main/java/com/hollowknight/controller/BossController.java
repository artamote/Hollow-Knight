package com.hollowknight.controller;

import com.hollowknight.model.GameModel;
import com.hollowknight.model.entity.boss.FalseKnightModel;
import com.hollowknight.model.entity.boss.FalseKnightModel.BossState;
import com.hollowknight.model.entity.player.PlayerModel;
import com.hollowknight.model.world.Room;
import com.hollowknight.observer.EventBus;
import com.hollowknight.observer.GameEvent;
import com.hollowknight.util.Constants;

import java.util.Random;


public class BossController {

    private final GameModel model;
    private final PlayerController playerController;
    private final Random rng = new Random();

    private static final float STUN_DURATION = 4.0f;
    private float stunTimer = 0f;
    private float damageInWindow = 0;
    private float windowTimer = 0f;

    public BossController(GameModel model, PlayerController playerController) {
        this.model = model;
        this.playerController = playerController;

        EventBus.subscribe(GameEvent.BOSS_DAMAGED, (event, data) -> {
            if (data instanceof Integer) registerDamageTaken((Integer) data);
        });
    }

    public void update(float delta) {
        FalseKnightModel boss = model.boss;
        if (boss == null || !boss.isAlive()) return;

        PlayerModel player = model.player;
        Room room = model.world.getCurrentRoom();

        windowTimer -= delta;
        if (windowTimer <= 0) { damageInWindow = 0; windowTimer = 1.0f; }

        if (boss.getHitFlashTimer() > 0) boss.setHitFlashTimer(boss.getHitFlashTimer() - delta);

        if (boss.shouldStun() && boss.getBossState() != BossState.STUNNED) {
            enterStun(boss);
        }

        if (boss.getKnockbackTimer() > 0) {
            boss.setKnockbackTimer(boss.getKnockbackTimer() - delta);
            if (boss.getKnockbackTimer() <= 0 && boss.getBossState() != BossState.STUNNED) {
                boss.setBossState(BossState.IDLE);
                boss.setActionTimer(0);
            }
        } else {
            switch (boss.getBossState()) {
                case STUNNED:
                    stunTimer -= delta;
                    boss.setVelocityX(0);
                    boss.setStunTimeRemaining(stunTimer);
                    if (stunTimer <= 0) {
                        exitStun(boss);
                    }
                    break;
                case IDLE:
                    decideNextMove(boss, player);
                    break;
                default:
                    executeMove(boss, player, room, delta);
                    break;
            }
        }

        applyGravityAndCollide(boss, room, delta);
        checkWaveHit(boss, player);
        checkPlayerCollision(boss, player);
    }

    private void decideNextMove(FalseKnightModel boss, PlayerModel player) {
        float dist = Math.abs(player.getCenterX() - boss.getCenterX());
        boss.setFacingRight(player.getCenterX() > boss.getCenterX());


        float wSlam = 0.40f, wCharge = 0.30f, wLeapOff = 0.15f, wLeapDef = 0.15f;

        if (dist < 120) { wSlam += 0.3f; wCharge -= 0.15f; }
        else if (dist > 400) { wCharge += 0.3f; wSlam -= 0.15f; }
        else { wLeapOff += 0.15f; }

        if (damageInWindow >= 3) { wLeapDef += 0.35f; } // was +0.5f

        BossState choice = weightedPick(boss, wSlam, wCharge, wLeapOff, wLeapDef);

        if (choice == boss.getLastMove()) {
            boss.setSameMoveCnt(boss.getSameMoveCnt() + 1);
            if (boss.getSameMoveCnt() >= 2) {
                choice = pickDifferent(choice);
                boss.setSameMoveCnt(0);
            }
        } else {
            boss.setSameMoveCnt(0);
        }
        boss.setLastMove(choice);
        boss.setBossState(choice);
        boss.setActionTimer(0);
    }

    private BossState weightedPick(FalseKnightModel boss, float wSlam, float wCharge, float wLeapOff, float wLeapDef) {
        boolean usePowerSlam = boss.getPhase() == 2 && rng.nextFloat() < 0.25f;
        if (usePowerSlam) return BossState.POWER_SLAM;

        float total = wSlam + wCharge + wLeapOff + wLeapDef;
        float r = rng.nextFloat() * total;
        if (r < wSlam) return BossState.MACE_SLAM;
        r -= wSlam;
        if (r < wCharge) return BossState.CHARGE_RUN;
        r -= wCharge;
        if (r < wLeapOff - 500) return BossState.OFFENSIVE_LEAP;
        return BossState.DEFENSIVE_LEAP;
    }

    private BossState pickDifferent(BossState avoid) {
        BossState[] options = {BossState.MACE_SLAM, BossState.CHARGE_RUN, BossState.OFFENSIVE_LEAP};
        BossState pick;
        do { pick = options[rng.nextInt(options.length)]; } while (pick == avoid);
        return pick;
    }

    private void executeMove(FalseKnightModel boss, PlayerModel player, Room room, float delta) {
        boss.setActionTimer(boss.getActionTimer() + delta);
        float speedMult = boss.getSpeedMultiplier();

        switch (boss.getBossState()) {
            case MACE_SLAM:
                boss.setVelocityX(0);
                if (boss.getActionTimer() > 0.8f) finishMove(boss);
                break;

            case CHARGE_RUN:
                float dir = boss.isFacingRight() ? 1 : -1;
                boss.setVelocityX(dir * boss.getChargeSpeed() * speedMult);
                if (boss.getActionTimer() > 1.2f
                        || CollisionUtil.willHitWall(boss, room, boss.isFacingRight())) {
                    finishMove(boss);
                }
                break;

            case OFFENSIVE_LEAP:
                if (boss.getActionTimer() < 0.05f) {
                    boss.setVelocityY(550f);
                    boss.setVelocityX((boss.isFacingRight() ? 1 : -1) * 300f * speedMult);
                }
                if (boss.isOnGround() && boss.getActionTimer() > 0.2f) finishMove(boss);
                break;

            case DEFENSIVE_LEAP:
                if (boss.getActionTimer() < 0.05f) {
                    boss.setVelocityY(450f);
                    boss.setVelocityX((boss.isFacingRight() ? -1 : 1) * 350f * speedMult);
                }
                if (boss.isOnGround() && boss.getActionTimer() > 0.2f) finishMove(boss);
                break;

            case POWER_SLAM:
                if (boss.getActionTimer() < 0.05f) {
                    boss.setVelocityY(600f);
                }
                if (boss.isOnGround() && boss.getActionTimer() > 0.15f) {
                    if (!boss.isWaveActive()) {
                        boss.setWaveActive(true);
                        boss.setWaveX(boss.getCenterX());
                        boss.setWaveHitPlayer(false);
                    }
                    if (boss.getActionTimer() > 2.0f) {
                        boss.setWaveActive(false);
                        finishMove(boss);
                    }
                }
                break;
            default:
                finishMove(boss);
        }
    }

    private void finishMove(FalseKnightModel boss) {
        boss.setVelocityX(0);
        boss.setBossState(BossState.IDLE);
        boss.setDecisionTimer(0);
    }

    private void enterStun(FalseKnightModel boss) {
        boss.setBossState(BossState.STUNNED);
        stunTimer = STUN_DURATION;
        boss.setStunTimeRemaining(STUN_DURATION);
        boss.setVelocityX(0);
        EventBus.publish(GameEvent.BOSS_STUNNED, null);
    }

    private void exitStun(FalseKnightModel boss) {
        boss.setPhase(2);
        boss.setSpeedMultiplier(1.6f);
        boss.setBossState(BossState.IDLE);
        EventBus.publish(GameEvent.BOSS_PHASE2, null);
    }

    public void registerDamageTaken(int amount) {
        damageInWindow += amount;
    }

    private void applyGravityAndCollide(FalseKnightModel boss, Room room, float delta) {
        float v = boss.getVelocityY() + Constants.GRAVITY * delta;
        boss.setVelocityY(Math.max(v, Constants.TERMINAL_VELOCITY));
        CollisionUtil.resolveCollisions(boss, room, delta);
    }


    private static final float WAVE_HIT_TOLERANCE = 40f;

    private void checkWaveHit(FalseKnightModel boss, PlayerModel player) {
        if (!boss.isWaveActive() || boss.hasWaveHitPlayer()) return;
        if (Math.abs(player.getY() - boss.getY()) >= 30) return;

        float elapsed = Math.max(0f, boss.getActionTimer() - 0.15f); // time since the wave started traveling
        float distance = FalseKnightModel.WAVE_ACCEL * elapsed * elapsed;
        float leftFrontX = boss.getWaveX() - distance;
        float rightFrontX = boss.getWaveX() + distance;

        boolean hitLeft = Math.abs(player.getCenterX() - leftFrontX) < WAVE_HIT_TOLERANCE;
        boolean hitRight = Math.abs(player.getCenterX() - rightFrontX) < WAVE_HIT_TOLERANCE;
        if (hitLeft || hitRight) {
            playerController.damagePlayer(player, 2);
            boss.setWaveHitPlayer(true);
        }
    }

    private void checkPlayerCollision(FalseKnightModel boss, PlayerModel player) {
        if (boss.getBossState() == BossState.STUNNED) return;
        if (player.getBounds().overlaps(boss.getBounds())) {
            playerController.damagePlayer(player, 1);
        }
    }

    public void spawnBoss(float x, float y) {
        model.boss = new FalseKnightModel(x, y);
        model.boss.setArenaLocked(true);
        EventBus.publish(GameEvent.BOSS_ENTERED, null);
    }
}