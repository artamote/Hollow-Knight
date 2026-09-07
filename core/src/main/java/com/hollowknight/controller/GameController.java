package com.hollowknight.controller;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.hollowknight.model.GameModel;


public class GameController {

    public final GameModel model;
    public final InputController input;
    public final PlayerController playerController;
    public final EnemyController enemyController;
    public final BossController bossController;
    public final SpellController spellController;
    public final CameraController cameraController;
    public final CheatController cheatController;

    public GameController(OrthographicCamera camera) {
        this(camera, null);
    }

    public GameController(OrthographicCamera camera, com.hollowknight.model.save.SaveData saveData) {
        model = new GameModel();
        if (saveData != null) model.applySaveData(saveData);
        input = new InputController();
        playerController = new PlayerController(model, input);
        enemyController = new EnemyController(model, playerController);
        bossController = new BossController(model, playerController);
        spellController = new SpellController(model, input);
        cameraController = new CameraController(camera, model);
        cheatController = new CheatController(model, input, bossController);


        com.hollowknight.observer.EventBus.subscribe(
                com.hollowknight.observer.GameEvent.ACHIEVEMENT_UNLOCKED,
                (event, data) -> {
                    if (data instanceof com.hollowknight.model.AchievementSystem.Achievement) {
                        com.hollowknight.model.AchievementStore.markUnlocked(
                                (com.hollowknight.model.AchievementSystem.Achievement) data);
                    }
                });
    }

    public void update(float delta) {
        if (model.paused || model.inInventory) return;

        cheatController.update(delta);
        model.update(delta);
        playerController.update(delta);
        checkRoomTransitions();
        enemyController.update(delta);
        bossController.update(delta);
        spellController.update(delta);
        cameraController.update(delta);
    }


    private void checkRoomTransitions() {
        com.hollowknight.model.world.Room room = model.world.getCurrentRoom();
        if (room == null) return;

        for (com.hollowknight.model.world.Room.RoomTransition t : room.transitions) {
            if (!model.player.getBounds().overlaps(t.bounds)) continue;

            com.hollowknight.model.world.Room target = model.world.getRoom(t.targetRoomId);
            if (target == null) continue;

            model.world.setCurrentRoom(t.targetRoomId);
            model.player.setX(target.spawnX);
            model.player.setY(target.spawnY);
            model.player.setVelocityX(0);
            model.player.setVelocityY(0);
            model.player.setCheckpoint(target.spawnX, target.spawnY);
            cameraController.snapTo(model.player.getCenterX(), model.player.getCenterY());
            com.hollowknight.observer.EventBus.publish(
                    com.hollowknight.observer.GameEvent.ROOM_CHANGED, t.targetRoomId);
            break;
        }
    }
}