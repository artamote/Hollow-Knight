package com.hollowknight.controller;

import com.hollowknight.model.GameModel;
import com.hollowknight.model.charm.Charm;
import com.hollowknight.model.entity.enemy.EnemyModel;
import com.hollowknight.model.entity.player.PlayerModel;
import com.hollowknight.model.entity.player.PlayerState;
import com.hollowknight.model.spell.HowlingWraithsModel;
import com.hollowknight.model.spell.VengefulSpiritModel;
import com.hollowknight.model.world.Room;
import com.hollowknight.observer.EventBus;
import com.hollowknight.observer.GameEvent;
import com.hollowknight.util.Constants;
import com.badlogic.gdx.math.Rectangle;

import java.util.Iterator;


public class SpellController {

    private final GameModel model;
    private final InputController input;

    public SpellController(GameModel model, InputController input) {
        this.model = model;
        this.input = input;
    }

    public void update(float delta) {
        PlayerModel p = model.player;



        if (input.isJustPressed(InputController.Action.CAST) && p.getSpellLockTimer() <= 0) {
            if (input.isDown(InputController.Action.UP)) {
                castHowlingWraiths();
            } else if (p.spendSoul(Constants.VENGEFUL_SPIRIT_COST)) {
                model.activeSpirits.add(new VengefulSpiritModel(
                        p.getCenterX(), p.getCenterY(), p.isFacingRight()));
                p.setSpellLockTimer(0.3f);
                p.setState(PlayerState.CASTING_SPELL);
                p.setLastCastSpell("VENGEFUL_SPIRIT");
                EventBus.publish(GameEvent.SPELL_CAST, "VENGEFUL_SPIRIT");
            }
        }

        Room room = model.world.getCurrentRoom();

        Iterator<VengefulSpiritModel> sit = model.activeSpirits.iterator();
        while (sit.hasNext()) {
            VengefulSpiritModel s = sit.next();
            s.update(delta);
            Rectangle bounds = new Rectangle(s.getX() - 8, s.getY() - 8, 16, 16);
            for (EnemyModel e : room.enemies) {
                if (e.isAlive() && bounds.overlaps(e.getBounds())) {
                    e.takeDamage(s.getDamage());
                    if (!e.isAlive()) EventBus.publish(GameEvent.ENEMY_KILLED, e.getType().name());
                    if (!s.hasImpacted()) {
                        model.activeImpacts.add(new com.hollowknight.model.spell.ImpactEffectModel(s.getX(), s.getY()));
                        s.setHasImpacted(true);
                    }
                }
            }

            if (model.boss != null && model.boss.isAlive() && bounds.overlaps(model.boss.getBounds())) {
                model.boss.takeDamage(s.getDamage());
                EventBus.publish(GameEvent.BOSS_DAMAGED, s.getDamage());
                if (!model.boss.isAlive()) EventBus.publish(GameEvent.BOSS_KILLED, null);
                if (!s.hasImpacted()) {
                    model.activeImpacts.add(new com.hollowknight.model.spell.ImpactEffectModel(s.getX(), s.getY()));
                    s.setHasImpacted(true);
                }
            }
            boolean offscreen = s.getX() < -100 || s.getX() > 3000;
            if (!s.isAlive() || offscreen) sit.remove();
        }

        Iterator<com.hollowknight.model.spell.ImpactEffectModel> iit = model.activeImpacts.iterator();
        while (iit.hasNext()) {
            com.hollowknight.model.spell.ImpactEffectModel impact = iit.next();
            impact.update(delta);
            if (!impact.isAlive()) iit.remove();
        }

        Iterator<HowlingWraithsModel> wit = model.activeWraiths.iterator();
        while (wit.hasNext()) {
            HowlingWraithsModel w = wit.next();
            boolean tick = w.update(delta);
            if (tick) {

                Rectangle bounds = new Rectangle(w.getX() - 50, w.getY() - 60, 100, 140);
                for (EnemyModel e : room.enemies) {
                    if (e.isAlive() && bounds.overlaps(e.getBounds())) {
                        e.takeDamage(w.getDamagePerTick());
                        if (!e.isAlive()) EventBus.publish(GameEvent.ENEMY_KILLED, e.getType().name());
                    }
                }
                if (model.boss != null && model.boss.isAlive() && bounds.overlaps(model.boss.getBounds())) {
                    model.boss.takeDamage(w.getDamagePerTick());
                    EventBus.publish(GameEvent.BOSS_DAMAGED, w.getDamagePerTick());
                    if (!model.boss.isAlive()) EventBus.publish(GameEvent.BOSS_KILLED, null);
                }
            }
            if (!w.isAlive()) wit.remove();
        }
    }

    public void castHowlingWraiths() {
        PlayerModel p = model.player;

        if (p.getSpellLockTimer() <= 0 && p.spendSoul(Constants.HOWLING_WRAITHS_COST)) {
            model.activeWraiths.add(new HowlingWraithsModel(p.getCenterX(), p.getCenterY() + 40));
            p.setSpellLockTimer(0.4f);
            p.setState(PlayerState.CASTING_SPELL);
            p.setLastCastSpell("HOWLING_WRAITHS");
            EventBus.publish(GameEvent.SPELL_CAST, "HOWLING_WRAITHS");
        }
    }
}