package com.hollowknight.controller;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.hollowknight.model.GameModel;
import com.hollowknight.model.world.Room;
import com.hollowknight.util.Constants;

import java.util.Random;


public class CameraController {

    private final OrthographicCamera camera;
    private final GameModel model;
    private final Random rng = new Random();

    private float shakeTimer = 0f;
    private float shakeMagnitude = 0f;

    public CameraController(OrthographicCamera camera, GameModel model) {
        this.camera = camera;
        this.model = model;
        com.hollowknight.observer.EventBus.subscribe(
                com.hollowknight.observer.GameEvent.BOSS_DAMAGED,
                (event, data) -> shake(0.18f, 12f));
        com.hollowknight.observer.EventBus.subscribe(
                com.hollowknight.observer.GameEvent.PLAYER_DAMAGED,
                (event, data) -> shake(0.15f, 10f));
    }

    public void update(float delta) {
        float targetX = model.player.getCenterX();
        float targetY = model.player.getCenterY();
        float lerp = Constants.CAMERA_LERP_SPEED * delta;
        camera.position.x += (targetX - camera.position.x) * Math.min(lerp, 1f);
        camera.position.y += (targetY - camera.position.y) * Math.min(lerp, 1f);

        if (model.boss != null && model.boss.isArenaLocked()) {
            Room room = model.world.getCurrentRoom();
            if (room != null && room.width > 0 && room.height > 0) {
                clamp(0, room.width, 0, room.height);
            } else {
                clamp(0, 1400, 0, 720); 
            }
        } else {
            
            Room room = model.world.getCurrentRoom();
            if (room != null && room.width > 0 && room.height > 0) {
                clamp(0, room.width, 0, room.height);
            }
        }

        if (shakeTimer > 0) {
            shakeTimer -= delta;
            camera.position.x += (rng.nextFloat() - 0.5f) * shakeMagnitude;
            camera.position.y += (rng.nextFloat() - 0.5f) * shakeMagnitude;
        }

        camera.update();
    }

    
    private void clamp(float minX, float maxX, float minY, float maxY) {
        float halfW = camera.viewportWidth / 2f;
        float halfH = camera.viewportHeight / 2f;

        if (maxX - minX <= camera.viewportWidth) {
            camera.position.x = (minX + maxX) / 2f;
        } else {
            camera.position.x = Math.max(minX + halfW, Math.min(maxX - halfW, camera.position.x));
        }

        if (maxY - minY <= camera.viewportHeight) {
            camera.position.y = (minY + maxY) / 2f;
        } else {
            camera.position.y = Math.max(minY + halfH, Math.min(maxY - halfH, camera.position.y));
        }
    }

    public void shake(float duration, float magnitude) {
        shakeTimer = Math.max(shakeTimer, duration);
        shakeMagnitude = Math.max(shakeMagnitude, magnitude);
    }

    
    public void snapTo(float x, float y) {
        camera.position.set(x, y, 0);
        camera.update();
    }
}