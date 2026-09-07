package com.hollowknight.controller;

import com.badlogic.gdx.math.Rectangle;
import com.hollowknight.model.GameModel;
import com.hollowknight.model.entity.enemy.EnemyModel;
import com.hollowknight.model.entity.enemy.flying.MosquitoModel;
import com.hollowknight.model.entity.enemy.flying.MossflyModel;
import com.hollowknight.model.entity.enemy.ground.CrystalGuardianModel;
import com.hollowknight.model.entity.enemy.ground.HuskHornheadModel;
import com.hollowknight.model.entity.player.PlayerModel;
import com.hollowknight.model.world.Room;
import com.hollowknight.util.Constants;


public class EnemyController {

    private final GameModel model;
    private final PlayerController playerController;

    public EnemyController(GameModel model, PlayerController playerController) {
        this.model = model;
        this.playerController = playerController;
    }

    public void update(float delta) {
        Room room = model.world.getCurrentRoom();
        PlayerModel player = model.player;

        for (EnemyModel e : room.enemies) {
            if (!e.isAlive()) {

                boolean wasFlying = e instanceof MosquitoModel || e instanceof MossflyModel;
                if (wasFlying) {
                    float v = e.getVelocityY() + Constants.GRAVITY * delta;
                    e.setVelocityY(Math.max(v, Constants.TERMINAL_VELOCITY));
                    e.setVelocityX(0); 
                    CollisionUtil.resolveEnemyCollisions(e, room, delta);
                }
                continue;
            }

            if (e.isKnockedBack()) {
                e.setKnockbackTimer(e.getKnockbackTimer() - delta);

                if (e.getKnockbackTimer() <= 0) {
                    resetToNormalState(e);
                }
            } else if (e instanceof HuskHornheadModel) {
                updateHuskHornhead((HuskHornheadModel) e, player, room, delta);
            } else if (e instanceof CrystalGuardianModel) {
                updateCrystalGuardian((CrystalGuardianModel) e, player, room, delta);
            } else if (e instanceof MosquitoModel) {
                updateMosquito((MosquitoModel) e, player, room, delta);
            } else if (e instanceof MossflyModel) {
                updateMossfly((MossflyModel) e, player, room, delta);
            } else {
                updateGenericGroundWalker(e, room, delta);
            }

            applyGravityAndCollide(e, room, delta);
            checkPlayerCollision(e, player);
        }
    }

    private void resetToNormalState(EnemyModel e) {
        if (e instanceof HuskHornheadModel) {
            HuskHornheadModel h = (HuskHornheadModel) e;
            h.setHHState(HuskHornheadModel.HHState.PATROL);
            h.setStateTimer(0);
        } else if (e instanceof MosquitoModel) {
            MosquitoModel m = (MosquitoModel) e;
            m.setMState(MosquitoModel.MState.PATROL);
            m.setStateTimer(0);
        } else if (e instanceof CrystalGuardianModel) {
            CrystalGuardianModel c = (CrystalGuardianModel) e;
            c.setCGState(CrystalGuardianModel.CGState.IDLE_WATCH);
            c.setStateTimer(0);
        } else if (e instanceof MossflyModel) {
            MossflyModel f = (MossflyModel) e;
            f.setMossflyState(MossflyModel.MossflyState.FLYING);
            f.setStateTimer(0);
        }
    }

    private void updateGenericGroundWalker(EnemyModel e, Room room, float delta) {
        boolean dir = e.isPatrolRight();
        e.setVelocityX(dir ? e.getSpeed() : -e.getSpeed());
        e.setFacingRight(dir);

        boolean hitWall = CollisionUtil.willHitWall(e, room, dir);
        boolean atCliff = CollisionUtil.isAtCliffEdge(e, room, dir);
        boolean hazardAhead = CollisionUtil.willHitHazard(e, room, dir); 
        if (hitWall || atCliff || hazardAhead) {
            e.setPatrolRight(!dir);
        }
    }

    private void updateHuskHornhead(HuskHornheadModel e, PlayerModel player, Room room, float delta) {
        e.setStateTimer(e.getStateTimer() + delta);

        float dx = player.getCenterX() - e.getCenterX();
        boolean playerInFront = (e.isFacingRight() && dx > 0 && dx < e.getDetectionRange())
                || (!e.isFacingRight() && dx < 0 && -dx < e.getDetectionRange());
        boolean sameLevel = Math.abs(player.getCenterY() - e.getCenterY()) < 60f;
        boolean hasSight = CollisionUtil.hasLineOfSight(
                e.getCenterX(), e.getCenterY(), player.getCenterX(), player.getCenterY(), room);

        switch (e.getHHState()) {
            case PATROL:
                updateGenericGroundWalker(e, room, delta);
                boolean canCharge = e.getStateTimer() > 0.4f;
                if (canCharge && playerInFront && sameLevel && hasSight) {
                    e.setHHState(HuskHornheadModel.HHState.CHARGE);
                    e.setStateTimer(0);
                } else if (e.getStateTimer() > e.getPatrolDuration()) {
                    e.setHHState(HuskHornheadModel.HHState.REST);
                    e.setStateTimer(0);
                    e.setVelocityX(0);
                }
                break;
            case REST:
                e.setVelocityX(0);
                if (e.getStateTimer() > e.getRestDuration()) {
                    e.setHHState(HuskHornheadModel.HHState.PATROL);
                    e.setStateTimer(0);
                }
                break;
            case CHARGE:
                boolean dir = e.isFacingRight();
                e.setVelocityX(dir ? e.getChargeSpeed() : -e.getChargeSpeed());
                if (CollisionUtil.willHitWall(e, room, dir) || CollisionUtil.isAtCliffEdge(e, room, dir)
                        || CollisionUtil.willHitHazard(e, room, dir)) {
                    e.setHHState(HuskHornheadModel.HHState.REST);
                    e.setStateTimer(0);
                    e.setVelocityX(0);
                }
                break;
        }
    }

    private void updateCrystalGuardian(CrystalGuardianModel e, PlayerModel player, Room room, float delta) {
        e.setVelocityX(0);
        float dx = player.getCenterX() - e.getCenterX();

        boolean inRange = Math.abs(dx) < e.getDetectionRange()
                && Math.abs(player.getCenterY() - e.getCenterY()) < 40f
                && CollisionUtil.hasLineOfSight(e.getCenterX(), e.getCenterY(), player.getCenterX(), player.getCenterY(), room);

        switch (e.getCGState()) {
            case IDLE_WATCH:
                if (inRange) {
                    e.setFacingRight(dx > 0);
                    e.setCGState(CrystalGuardianModel.CGState.FIRING_LASER);
                    e.setLaserFired(false);
                    e.setStateTimer(0);
                }
                break;
            case FIRING_LASER:
                e.setStateTimer(e.getStateTimer() + delta);
                if (!e.isLaserFired() && e.getStateTimer() > e.getLaserWindupDuration()) {
                    e.setLaserFired(true);

                    float beamHeight = 80f;
                    float beamX = e.isFacingRight() ? e.getCenterX() : e.getCenterX() - e.getDetectionRange();
                    float beamY = e.getCenterY() - beamHeight / 2f;
                    Rectangle beam = new Rectangle(beamX, beamY, e.getDetectionRange(), beamHeight);
                    if (beam.overlaps(player.getBounds())) {
                        playerController.damagePlayer(player, 2);
                    }
                }
                if (e.getStateTimer() > e.getLaserTotalDuration()) {
                    e.setCGState(CrystalGuardianModel.CGState.ENRAGED);
                    e.setEnrageTimer(0);
                }
                break;
            case ENRAGED:
                e.setEnrageTimer(e.getEnrageTimer() + delta);
                boolean chargeDir = e.isFacingRight();

                if (CollisionUtil.willHitWall(e, room, chargeDir) || CollisionUtil.isAtCliffEdge(e, room, chargeDir)
                        || CollisionUtil.willHitHazard(e, room, chargeDir)) {
                    e.setVelocityX(0);
                    e.setCGState(CrystalGuardianModel.CGState.COOLDOWN);
                    e.setStateTimer(0);
                } else {
                    e.setVelocityX(chargeDir ? e.getEnrageSpeed() : -e.getEnrageSpeed());
                }
                if (e.getEnrageTimer() > e.getEnrageDuration()) {
                    e.setCGState(CrystalGuardianModel.CGState.COOLDOWN);
                    e.setStateTimer(0);
                }
                break;
            case COOLDOWN:
                e.setStateTimer(e.getStateTimer() + delta);
                if (e.getStateTimer() > e.getCooldownDuration()) {
                    e.setCGState(CrystalGuardianModel.CGState.IDLE_WATCH);
                }
                break;
        }
    }

    private void updateMosquito(MosquitoModel e, PlayerModel player, Room room, float delta) {
        float dx = player.getCenterX() - e.getCenterX();
        float dy = player.getCenterY() - e.getCenterY();
        float dist = (float) Math.sqrt(dx * dx + dy * dy);

        switch (e.getMState()) {
            case PATROL:
                e.setVelocityY((float) Math.sin(e.getStateTimer() * 2f) * 30f);
                e.setStateTimer(e.getStateTimer() + delta);
                if (dist < e.getDetectionRange()
                        && CollisionUtil.hasLineOfSight(e.getCenterX(), e.getCenterY(), player.getCenterX(), player.getCenterY(), room)) {
                    e.setMState(MosquitoModel.MState.PREPARE_DASH);
                    e.setTarget(player.getCenterX(), player.getCenterY());
                    e.setStateTimer(0);
                }
                break;
            case PREPARE_DASH:
                e.setVelocityX(0); e.setVelocityY(0);
                e.setFacingRight(dx >= 0);
                e.setStateTimer(e.getStateTimer() + delta);
                if (e.getStateTimer() > e.getPrepareDuration()) {
                    e.setMState(MosquitoModel.MState.DASHING);
                    e.setStateTimer(0);
                    float tdx = e.getTargetX() - e.getCenterX();
                    float tdy = e.getTargetY() - e.getCenterY();
                    float tdist = Math.max(1f, (float) Math.sqrt(tdx * tdx + tdy * tdy));
                    e.setVelocityX(tdx / tdist * 500f);
                    e.setVelocityY(tdy / tdist * 500f);
                    e.setFacingRight(tdx >= 0);
                }
                break;
            case DASHING:
                e.setStateTimer(e.getStateTimer() + delta);
                if (e.getStateTimer() > 1.0f) {
                    e.setMState(MosquitoModel.MState.PATROL);
                    e.setVelocityX(0); e.setVelocityY(0);
                    e.setStateTimer(0);
                }
                break;
        }
    }

    private void updateMossfly(MossflyModel e, PlayerModel player, Room room, float delta) {
        float dx = player.getCenterX() - e.getCenterX();
        float dy = player.getCenterY() - e.getCenterY();
        float dist = (float) Math.sqrt(dx * dx + dy * dy);

        switch (e.getMossflyState()) {
            case HIDDEN:
                e.setVelocityX(0);
                e.setVelocityY(0);
                if (dist < e.getDetectionRange()
                        && CollisionUtil.hasLineOfSight(e.getCenterX(), e.getCenterY(), player.getCenterX(), player.getCenterY(), room)) {
                    e.setMossflyState(MossflyModel.MossflyState.REVEALING);
                    e.setFacingRight(dx >= 0);
                    e.setStateTimer(0);
                }
                break;
            case REVEALING:
                e.setVelocityX(0);
                e.setVelocityY(0);
                e.setStateTimer(e.getStateTimer() + delta);
                if (e.getStateTimer() >= e.getRevealDuration()) {
                    e.setMossflyState(MossflyModel.MossflyState.FLYING);
                    e.setStateTimer(0);
                }
                break;
            case FLYING:
                float dirLen = Math.max(1f, dist);
                e.setVelocityX(dx / dirLen * e.getSpeed());
                e.setVelocityY(dy / dirLen * e.getSpeed());
                e.setFacingRight(dx >= 0);
                break;
        }
    }

    private void applyGravityAndCollide(EnemyModel e, Room room, float delta) {
        boolean flying = e instanceof MosquitoModel || e instanceof MossflyModel;
        if (!flying) {
            float v = e.getVelocityY() + Constants.GRAVITY * delta;
            e.setVelocityY(Math.max(v, Constants.TERMINAL_VELOCITY));
        }

        CollisionUtil.resolveEnemyCollisions(e, room, delta);
    }

    private void checkPlayerCollision(EnemyModel e, PlayerModel player) {
        if (!player.getBounds().overlaps(e.getBounds())) return;
        playerController.damagePlayer(player, 1);

        if (e instanceof HuskHornheadModel) {
            HuskHornheadModel husk = (HuskHornheadModel) e;
            if (husk.getHHState() == HuskHornheadModel.HHState.CHARGE) {
                float recoilDir = husk.isFacingRight() ? -1f : 1f;
                husk.setVelocityX(recoilDir * Constants.HUSK_CHARGE_STOP_RECOIL);
                husk.setKnockbackTimer(Constants.HUSK_CHARGE_STOP_DURATION);
                husk.setHHState(HuskHornheadModel.HHState.REST);
                husk.setStateTimer(0f);
            }
        }
    }
}