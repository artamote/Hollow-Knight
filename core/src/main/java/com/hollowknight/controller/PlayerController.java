package com.hollowknight.controller;
import com.hollowknight.model.GameModel;
import com.hollowknight.model.charm.Charm;
import com.hollowknight.model.entity.player.PlayerModel;
import com.hollowknight.model.entity.player.PlayerState;
import com.hollowknight.model.entity.enemy.EnemyModel;
import com.hollowknight.model.world.Hazard;
import com.hollowknight.model.world.Room;
import com.hollowknight.observer.EventBus;
import com.hollowknight.observer.GameEvent;
import com.hollowknight.util.Constants;
import com.badlogic.gdx.math.Rectangle;
/**
 * CONTROLLER: drives PlayerModel each frame based on InputController.
 * No rendering here — pure logic.
 */
public class PlayerController {
    private final GameModel model;
    private final InputController input;
    public PlayerController(GameModel model, InputController input) {
        this.model = model;
        this.input = input;
    }
    public void update(float delta) {
        PlayerModel p = model.player;
        if (!p.isAlive() && !p.isDying()) return;
        if (p.isDying()) {
            p.setDeathTimer(p.getDeathTimer() - delta);
            p.setVelocityX(0);
            if (p.getDeathTimer() <= 0) {
                p.setAlive(true);
                respawnAtRoomSpawnAndResetRoom(p);
                p.takeDamage(-Constants.PLAYER_MAX_MASKS); 
                p.setState(PlayerState.IDLE);
            }
            return;
        }
        Room room = model.world.getCurrentRoom();
        if (model.noclip) {
            float flySpeed = 400f;
            float vx = 0f, vy = 0f;
            if (input.isDown(InputController.Action.LEFT))  vx -= flySpeed;
            if (input.isDown(InputController.Action.RIGHT)) vx += flySpeed;
            if (input.isDown(InputController.Action.UP))    vy += flySpeed;
            if (input.isDown(InputController.Action.DOWN))  vy -= flySpeed;
            p.setVelocityX(vx);
            p.setVelocityY(vy);
            p.setX(p.getX() + vx * delta);
            p.setY(p.getY() + vy * delta);
            p.setState(vx != 0 || vy != 0 ? PlayerState.RUNNING : PlayerState.IDLE);
            if (vx != 0) p.setFacingRight(vx > 0);
            return;
        }
        tickTimers(p, delta);
        handleHorizontalMovement(p);
        handleJump(p);
        handleDash(p, room, delta);
        handleWallSlide(p, room);
        handlePogoAndAttack(p, room, delta);
        handleFocus(p, delta);
        applyGravity(p, delta);
        CollisionUtil.resolveCollisions(p, room, delta);
        if (p.isOnGround()) {
            p.setCanDoubleJump(true);
        }
        checkHazards(p, room);
        checkCheckpoint(p, room);
        updateState(p);
    }
    private void tickTimers(PlayerModel p, float delta) {
        if (p.getInvincibleTimer() > 0) p.setInvincibleTimer(p.getInvincibleTimer() - delta);
        if (p.getAttackTimer() > 0)     p.setAttackTimer(p.getAttackTimer() - delta);
        if (p.getDashCooldown() > 0)    p.setDashCooldown(p.getDashCooldown() - delta);
        if (p.getHurtTimer() > 0)       p.setHurtTimer(p.getHurtTimer() - delta);
        if (p.getSpellLockTimer() > 0)  p.setSpellLockTimer(p.getSpellLockTimer() - delta);
        if (p.getDoubleJumpTimer() > 0) p.setDoubleJumpTimer(p.getDoubleJumpTimer() - delta);
    }
    private boolean isLocked(PlayerModel p) {
        return p.getHurtTimer() > 0 || p.getSpellLockTimer() > 0 || p.getState() == PlayerState.FOCUSING;
    }
    private void handleHorizontalMovement(PlayerModel p) {
        if (isLocked(p) || p.getState() == PlayerState.DASHING) return;
        float speed = Constants.PLAYER_SPEED;
        if (input.isDown(InputController.Action.LEFT)) {
            p.setVelocityX(-speed);
            p.setFacingRight(false);
        } else if (input.isDown(InputController.Action.RIGHT)) {
            p.setVelocityX(speed);
            p.setFacingRight(true);
        } else {
            p.setVelocityX(0);
        }
    }
    private void handleJump(PlayerModel p) {
        if (isLocked(p)) return;
        if (input.isJustPressed(InputController.Action.JUMP)) {
            if (p.isOnGround()) {
                p.setVelocityY(Constants.PLAYER_JUMP_VELOCITY);
                p.setOnGround(false);
            } else if (p.isOnWall()) {
                p.setVelocityY(Constants.PLAYER_JUMP_VELOCITY);
                p.setVelocityX(p.isWallOnLeft() ? Constants.PLAYER_SPEED : -Constants.PLAYER_SPEED);
                p.setOnWall(false);
            } else if (p.canDoubleJump()) {
                p.setVelocityY(Constants.PLAYER_JUMP_VELOCITY);
                p.setCanDoubleJump(false);
                p.setDoubleJumpTimer(0.4f);
            }
        }
        if (!input.isDown(InputController.Action.JUMP) && p.getVelocityY() > Constants.PLAYER_JUMP_CUTOFF) {
            p.setVelocityY(Constants.PLAYER_JUMP_CUTOFF);
        }
    }
    private void handleDash(PlayerModel p, Room room, float delta) {
        boolean dashmaster = model.charms.has(Charm.DASHMASTER);
        float cooldownMax = dashmaster ? Constants.PLAYER_DASH_COOLDOWN * 0.5f : Constants.PLAYER_DASH_COOLDOWN;
        boolean sharpShadow = model.charms.has(Charm.SHARP_SHADOW);
        if (p.getDashTimer() > 0) {
            p.setDashTimer(p.getDashTimer() - delta);
            float dashSpeed = Constants.PLAYER_DASH_SPEED;
            if (sharpShadow) dashSpeed *= 1.2f;
            p.setVelocityX(p.isFacingRight() ? dashSpeed : -dashSpeed);
            p.setVelocityY(0);
            if (sharpShadow) {
                int dmg = Constants.NAIL_DAMAGE;
                if (model.charms.has(Charm.UNBREAKABLE_STRENGTH)) dmg = (int) (dmg * 1.4f);
                for (EnemyModel enemy : room.enemies) {
                    if (!enemy.isAlive() || p.getHitThisDash().contains(enemy)) continue;
                    if (p.getBounds().overlaps(enemy.getBounds())) {
                        applyNailDamage(p, enemy, dmg);
                        p.getHitThisDash().add(enemy);
                    }
                }
                if (model.boss != null && model.boss.isAlive() && !p.getHitThisDash().contains(model.boss)) {
                    if (p.getBounds().overlaps(model.boss.getBounds())) {
                        applyBossDamage(p, dmg);
                        p.getHitThisDash().add(model.boss);
                    }
                }
            }
            if (p.getDashTimer() <= 0) {
                p.setDashGravityOff(false);
                p.setDashCooldown(cooldownMax);
            }
            return;
        }
        if (isLocked(p)) return;
        if (input.isJustPressed(InputController.Action.DASH) && p.canDash() && p.getDashCooldown() <= 0) {
            p.setDashTimer(Constants.PLAYER_DASH_DURATION);
            p.setDashGravityOff(true);
            p.setState(PlayerState.DASHING);
            p.getHitThisDash().clear();
            EventBus.publish(GameEvent.PLAYER_DASHED, null);
        }
    }
    private void handleWallSlide(PlayerModel p, Room room) {
        boolean touchingWall = !p.isOnGround() &&
                CollisionUtil.willHitWall(p, room, p.isFacingRight());
        boolean pushingIntoWall =
                (input.isDown(InputController.Action.LEFT) && !p.isFacingRight()) ||
                        (input.isDown(InputController.Action.RIGHT) && p.isFacingRight());
        if (touchingWall && pushingIntoWall && p.getVelocityY() < 0) {
            p.setOnWall(true);
            p.setWallOnLeft(p.isFacingRight());
            p.setVelocityY(Constants.PLAYER_WALL_SLIDE_SPEED);
            p.setCanDoubleJump(true);
        } else {
            p.setOnWall(false);
        }
    }
    private void handlePogoAndAttack(PlayerModel p, Room room, float delta) {
        if (!isLocked(p) && input.isJustPressed(InputController.Action.ATTACK) && p.getAttackTimer() <= 0) {
            boolean quickSlash = model.charms.has(Charm.QUICK_SLASH);
            p.setAttackTimer(quickSlash ? Constants.NAIL_COOLDOWN * 0.5f : Constants.NAIL_COOLDOWN);
            p.setAttackHitWindowTimer(Constants.NAIL_HIT_WINDOW);
            p.getHitThisSwing().clear();
            p.setPogoedThisSwing(false);
            boolean pogo = input.isDown(InputController.Action.DOWN) && !p.isOnGround();
            p.setAttackDirection(pogo ? PlayerModel.AttackDir.DOWN
                    : p.isFacingRight() ? PlayerModel.AttackDir.RIGHT : PlayerModel.AttackDir.LEFT);
            EventBus.publish(GameEvent.NAIL_SLASH, p.getAttackDirection());
        }
        if (p.getAttackHitWindowTimer() > 0) {
            p.setAttackHitWindowTimer(p.getAttackHitWindowTimer() - delta);
            resolveAttackHits(p, room);
        }
    }
    private void resolveAttackHits(PlayerModel p, Room room) {
        int dmg = Constants.NAIL_DAMAGE;
        if (model.charms.has(Charm.UNBREAKABLE_STRENGTH)) dmg = (int)(dmg * 1.4f);
        boolean pogoAttack = p.getAttackDirection() == PlayerModel.AttackDir.DOWN;
        Rectangle hitbox = computeAttackHitbox(p);
        for (EnemyModel enemy : room.enemies) {
            if (!enemy.isAlive() || p.getHitThisSwing().contains(enemy)) continue;
            Rectangle forgiving = pad(enemy.getBounds(), Constants.NAIL_HIT_PADDING);
            if (hitbox.overlaps(forgiving)) {
                applyNailDamage(p, enemy, dmg);
                p.getHitThisSwing().add(enemy);
                if (pogoAttack && !p.isPogoedThisSwing()) {
                    triggerPogoBounce(p);
                }
            }
        }
        if (model.boss != null && model.boss.isAlive() && !p.getHitThisSwing().contains(model.boss)) {
            Rectangle forgivingBoss = pad(model.boss.getBounds(), Constants.NAIL_HIT_PADDING);
            if (hitbox.overlaps(forgivingBoss)) {
                applyBossDamage(p, dmg);
                p.getHitThisSwing().add(model.boss);
                if (pogoAttack && !p.isPogoedThisSwing()) {
                    triggerPogoBounce(p);
                }
            }
        }
        if (pogoAttack && !p.isPogoedThisSwing()) {
            for (Hazard h : room.hazards) {
                Rectangle forgivingHazard = pad(h.bounds, Constants.NAIL_HIT_PADDING);
                if (hitbox.overlaps(forgivingHazard)) {
                    triggerPogoBounce(p);
                    break;
                }
            }
        }
    }
    private void triggerPogoBounce(PlayerModel p) {
        p.setVelocityY(Constants.POGO_BOUNCE_VELOCITY);
        p.setCanDoubleJump(true);
        p.setDashCooldown(0);
        p.setPogoedThisSwing(true);
        p.setInvincibleTimer(Math.max(p.getInvincibleTimer(), Constants.POGO_INVINCIBLE_TIME));
    }
    private Rectangle pad(Rectangle r, float amount) {
        return new Rectangle(r.x - amount, r.y - amount, r.width + amount * 2f, r.height + amount * 2f);
    }
    private Rectangle computeAttackHitbox(PlayerModel p) {
        switch (p.getAttackDirection()) {
            case DOWN:
                return new Rectangle(p.getX(), p.getY() - Constants.NAIL_RANGE_V,
                        p.getWidth(), Constants.NAIL_RANGE_V);
            case RIGHT:
                return new Rectangle(p.getX() + p.getWidth(), p.getY(),
                        Constants.NAIL_RANGE_H, p.getHeight());
            default:
                return new Rectangle(p.getX() - Constants.NAIL_RANGE_H, p.getY(),
                        Constants.NAIL_RANGE_H, p.getHeight());
        }
    }
    /**
     * Boss damage, separate from applyNailDamage() since FalseKnightModel
     * isn't an EnemyModel (no setKnockbackTimer/generic AI-stagger stuff --
     * the boss's own move state machine drives its velocity directly, so
     * generic knockback would just get overwritten next frame anyway,
     * same class of bug fixed for Husk's knockback earlier). Still grants
     * soul like any other hit, and notifies BossController via the event
     * bus so its anti-spam "damage in window" tracking and the 50%-HP
     * stun trigger actually have real damage feeding them.
     */
    private void applyBossDamage(PlayerModel p, int dmg) {
        model.boss.takeDamage(dmg);
        model.boss.setHitFlashTimer(0.15f);
        float pushDir = model.boss.getCenterX() >= p.getCenterX() ? 1f : -1f;
        model.boss.setVelocityX(pushDir * 300f);
        model.boss.setKnockbackTimer(0.2f);
        int soulGain = Constants.SOUL_GAIN_PER_HIT;
        if (model.charms.has(Charm.SOUL_CATCHER)) soulGain = (int) (soulGain * 1.5f);
        p.gainSoul(soulGain);
        EventBus.publish(GameEvent.PLAYER_SOUL_GAINED, soulGain);
        EventBus.publish(GameEvent.BOSS_DAMAGED, dmg);
        if (!model.boss.isAlive()) {
            model.boss.setBossState(com.hollowknight.model.entity.boss.FalseKnightModel.BossState.DEAD);
            EventBus.publish(GameEvent.BOSS_KILLED, null);
            EventBus.publish(GameEvent.GAME_COMPLETED, null);
            if (p.getTimeElapsed() < 900f) { 
                EventBus.publish(GameEvent.SPEEDRUN_FINISHED, null);
            }
            model.bossDeathPending = true;
            model.victoryDelayTimer = 2.5f;
        }
    }
    private void applyNailDamage(PlayerModel p, EnemyModel enemy, int dmg) {
        boolean wasAlive = enemy.isAlive();
        enemy.takeDamage(dmg);
        boolean heavyBlow = model.charms.has(Charm.HEAVY_BLOW);
        float kb = heavyBlow ? Constants.KNOCKBACK_FORCE * 1.6f : Constants.KNOCKBACK_FORCE;
        enemy.setVelocityX(p.isFacingRight() ? kb : -kb);
        enemy.setVelocityY(Constants.ENEMY_KNOCKBACK_POP_Y);
        enemy.setKnockbackTimer(Constants.ENEMY_KNOCKBACK_DURATION);
        int soulGain = Constants.SOUL_GAIN_PER_HIT;
        if (model.charms.has(Charm.SOUL_CATCHER)) soulGain = (int)(soulGain * 1.5f);
        p.gainSoul(soulGain);
        EventBus.publish(GameEvent.PLAYER_SOUL_GAINED, soulGain);
        if (wasAlive && !enemy.isAlive()) {
            p.incrementKills();
            EventBus.publish(GameEvent.ENEMY_KILLED, enemy.getType().name());
        }
    }
    private void handleFocus(PlayerModel p, float delta) {
        boolean wantsFocus = input.isDown(InputController.Action.FOCUS);
        boolean canStart = p.isOnGround() && !isLocked(p) && p.getState() != PlayerState.DASHING
                && p.getSoul() >= Constants.SOUL_FOCUS_COST;
        if (wantsFocus && (p.isFocusing() || canStart)) {
            boolean justStarted = !p.isFocusing();
            if (justStarted) EventBus.publish(GameEvent.PLAYER_FOCUSED, null);
            p.setState(PlayerState.FOCUSING);
            p.setVelocityX(0);
            float maxTime = model.charms.has(Charm.QUICK_FOCUS)
                    ? Constants.PLAYER_FOCUS_TIME * 0.6f : Constants.PLAYER_FOCUS_TIME;
            p.setFocusTimer(p.getFocusTimer() + delta);
            if (p.getFocusTimer() >= maxTime) {
                if (p.spendSoul(Constants.SOUL_FOCUS_COST) && p.getHp() < p.getMaxHp()) {
                    p.takeDamage(-1);
                    EventBus.publish(GameEvent.PLAYER_HEALED, null);
                }
                p.setFocusTimer(0);
                p.setState(PlayerState.IDLE);
                EventBus.publish(GameEvent.PLAYER_FOCUS_STOPPED, null);
            }
        } else if (p.isFocusing()) {
            p.setFocusTimer(0);
            p.setState(PlayerState.IDLE);
            EventBus.publish(GameEvent.PLAYER_FOCUS_STOPPED, null);
        }
    }
    private void applyGravity(PlayerModel p, float delta) {
        if (p.isDashGravityOff()) return;
        if (p.isOnWall()) return;
        float v = p.getVelocityY() + Constants.GRAVITY * delta;
        p.setVelocityY(Math.max(v, Constants.TERMINAL_VELOCITY));
    }
    private void checkHazards(PlayerModel p, Room room) {
        if (model.godMode || p.isInvincible()) return;
        for (Hazard h : room.hazards) {
            if (p.getBounds().overlaps(h.bounds)) {
                boolean lethal = p.getHp() <= h.damage;
                damagePlayer(p, h.damage);
                if (!lethal) {
                    respawnAtCheckpoint(p); 
                }
                return;
            }
        }
    }
    public void damagePlayer(PlayerModel p, int amount) {
        if (model.godMode || p.isInvincible()) return;
        if (p.getState() == PlayerState.DASHING && model.charms.has(Charm.SHARP_SHADOW)) return;
        p.takeDamage(amount);
        p.setInvincibleTimer(Constants.PLAYER_INVINCIBLE_TIME);
        EventBus.publish(GameEvent.PLAYER_DAMAGED, amount);
        if (p.getHp() <= 0) {
            p.incrementDeaths();
            EventBus.publish(GameEvent.PLAYER_DIED, null);
            p.setState(PlayerState.DEAD);
            p.setDeathTimer(Constants.PLAYER_DEATH_ANIM_TIME);
            p.setHurtTimer(0);
            p.setVelocityX(0);
            p.setVelocityY(0);
        } else {
            p.setHurtTimer(0.2f);
            p.setVelocityX(p.isFacingRight() ? -250f : 250f);
            p.setVelocityY(200f);
            p.setState(PlayerState.HURT);
        }
    }
    private void respawnAtCheckpoint(PlayerModel p) {
        p.setX(p.getCheckpointX());
        p.setY(p.getCheckpointY());
        p.setVelocityX(0);
        p.setVelocityY(0);
    }
    private void respawnAtRoomSpawnAndResetRoom(PlayerModel p) {
        Room room = model.world.getCurrentRoom();
        p.setX(room.spawnX);
        p.setY(room.spawnY);
        p.setVelocityX(0);
        p.setVelocityY(0);
        p.setCheckpoint(room.spawnX, room.spawnY);
        for (EnemyModel enemy : room.enemies) {
            enemy.respawn();
        }
        if (model.boss != null && room.hasBossSpawn) {
            model.boss = new com.hollowknight.model.entity.boss.FalseKnightModel(room.bossSpawnX, room.bossSpawnY);
            model.boss.setArenaLocked(true);
        }
    }
    private void checkCheckpoint(PlayerModel p, Room room) {
        for (com.badlogic.gdx.math.Rectangle checkpoint : room.checkpoints) {
            if (p.getBounds().overlaps(checkpoint)) {
                p.setCheckpoint(p.getX(), p.getY());
                return;
            }
        }
    }
    private void updateState(PlayerModel p) {
        if (p.isDying()) { p.setState(PlayerState.DEAD); return; }
        if (p.getHurtTimer() > 0) { p.setState(PlayerState.HURT); return; }
        if (p.getDashTimer() > 0) { p.setState(PlayerState.DASHING); return; }
        if (p.getState() == PlayerState.FOCUSING) return; 
        if (p.getSpellLockTimer() > 0) { p.setState(PlayerState.CASTING_SPELL); return; }
        if (p.isAttacking()) { p.setState(PlayerState.ATTACKING); return; }
        if (p.getDoubleJumpTimer() > 0) { p.setState(PlayerState.DOUBLE_JUMPING); return; }
        if (p.isOnWall()) {
            p.setState(PlayerState.WALL_SLIDING);
        } else if (!p.isOnGround()) {
            p.setState(p.getVelocityY() > 0 ? PlayerState.JUMPING : PlayerState.FALLING);
        } else if (Math.abs(p.getVelocityX()) > 1f) {
            p.setState(PlayerState.RUNNING);
        } else {
            p.setState(PlayerState.IDLE);
        }
    }
}