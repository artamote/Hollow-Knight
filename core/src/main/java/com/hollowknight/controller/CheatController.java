package com.hollowknight.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.hollowknight.model.GameModel;
import com.hollowknight.model.entity.player.PlayerModel;


public class CheatController {

    private final GameModel model;
    private final InputController input;
    private final BossController bossController;

    public CheatController(GameModel model, InputController input, BossController bossController) {
        this.model = model;
        this.input = input;
        this.bossController = bossController;
    }

    public void update(float delta) {
        boolean ctrl = Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT);
        if (!ctrl) return;

        PlayerModel p = model.player;

        if (Gdx.input.isKeyJustPressed(Input.Keys.B)) {
            model.world.setCurrentRoom("BOSS");
            com.hollowknight.model.world.Room arena = model.world.getCurrentRoom();
            p.setX(arena.spawnX); p.setY(arena.spawnY);
            p.setCheckpoint(arena.spawnX, arena.spawnY);
            if (arena.hasBossSpawn) {
                bossController.spawnBoss(arena.bossSpawnX, arena.bossSpawnY);
            } else {
                bossController.spawnBoss(arena.spawnX + 600, arena.spawnY);
            }
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.N)) {
            model.noclip = !model.noclip;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.H)) {
            if (p.getHp() <= 0) p.setHp(100);
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.R)) {
            p.setSoul(99);
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.G)) {
            model.godMode = !model.godMode;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.K)) {
            model.world.getCurrentRoom().enemies.forEach(e -> e.takeDamage(9999));
        }
    }
}