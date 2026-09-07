package com.hollowknight.view.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.hollowknight.HollowKnightGame;
import com.hollowknight.controller.GameController;
import com.hollowknight.model.world.Room;
import com.hollowknight.util.Constants;
import com.hollowknight.view.entity.EnemyView;
import com.hollowknight.view.entity.PlayerView;
import com.hollowknight.view.entity.SpellView;
import com.hollowknight.view.hud.HUDView;
import com.hollowknight.view.hud.AchievementPopupView;
import com.hollowknight.view.ui.InventoryOverlay;
import com.hollowknight.view.ui.PauseOverlay;
import com.hollowknight.view.world.WorldView;


public class GameScreen implements Screen {

    private final HollowKnightGame game;
    private final OrthographicCamera worldCamera;
    private final OrthographicCamera hudCamera;
    private final ShapeRenderer shapes;
    private final GameController controller;

    private final WorldView worldView;
    private final PlayerView playerView;
    private final EnemyView enemyView;
    private final SpellView spellView;
    private final com.hollowknight.audio.AudioController audioController;
    private final HUDView hudView = new HUDView();
    private final AchievementPopupView achievementPopupView;

    private PauseOverlay pauseOverlay;
    private InventoryOverlay inventoryOverlay;

    private final Texture overlayPixel;

    public GameScreen(HollowKnightGame game) {
        this(game, null);
    }

    public GameScreen(HollowKnightGame game, com.hollowknight.model.save.SaveData saveData) {
        this.game = game;
        worldCamera = new OrthographicCamera(Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT);
        worldCamera.position.set(Constants.SCREEN_WIDTH / 2f, Constants.SCREEN_HEIGHT / 2f, 0);
        hudCamera = new OrthographicCamera(Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT);
        hudCamera.position.set(Constants.SCREEN_WIDTH / 2f, Constants.SCREEN_HEIGHT / 2f, 0);
        shapes = new ShapeRenderer();
        controller = new GameController(worldCamera, saveData);
        audioController = new com.hollowknight.audio.AudioController(game.assetLoader.sfxLibrary, game.settings);

        worldView = new WorldView();
        playerView = new PlayerView(game.assetLoader.playerAnimations);
        enemyView = new EnemyView(game.assetLoader.enemyAnimations);
        spellView = new SpellView(game.assetLoader.spellAnimations);

        pauseOverlay = new PauseOverlay(game, this);
        inventoryOverlay = new InventoryOverlay(game, this);
        achievementPopupView = new AchievementPopupView(game.assetLoader.defaultFont);

        Pixmap pm = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pm.setColor(1f, 1f, 1f, 1f);
        pm.fill();
        overlayPixel = new Texture(pm);
        pm.dispose();


        lastRoomId = controller.model.world.getCurrentRoomId();
        game.assetLoader.musicManager.playTrack(controller.model.world.getCurrentRoom().musicTrack);
    }

    private String lastRoomId;

    @Override
    public void render(float delta) {
        handleToggles();

        if (controller.model.victory) {
            com.hollowknight.model.entity.player.PlayerModel p = controller.model.player;
            game.setScreen(new VictoryScreen(game, p.getDeathCount(), p.getKillCount(), p.getTimeElapsed()));
            dispose();
            return;
        }

        game.assetLoader.musicManager.update(delta);
        String currentRoomId = controller.model.world.getCurrentRoomId();
        if (!currentRoomId.equals(lastRoomId)) {
            lastRoomId = currentRoomId;
            game.assetLoader.musicManager.playTrack(controller.model.world.getCurrentRoom().musicTrack);
        }

        boolean overlayOpen = controller.model.paused || controller.model.inInventory;

        if (!overlayOpen) {
            controller.update(delta);
            playerView.update(delta, controller.model.player);
            enemyView.update(delta, controller.model.world.getCurrentRoom());
            enemyView.updateBoss(delta, controller.model.boss);
        }
        hudView.update(delta, controller.model);
        achievementPopupView.update(delta);

        Gdx.gl.glClearColor(0.06f, 0.06f, 0.1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        worldCamera.update();
        Room currentRoom = controller.model.world.getCurrentRoom();

        worldView.renderTilesBehindEntities(worldCamera, currentRoom);

        shapes.setProjectionMatrix(worldCamera.combined);
        shapes.begin(ShapeRenderer.ShapeType.Filled);
        worldView.render(shapes, currentRoom);
        shapes.end();

        game.batch.setProjectionMatrix(worldCamera.combined);
        game.batch.begin();
        enemyView.render(game.batch, currentRoom);
        enemyView.renderBoss(game.batch, controller.model.boss);
        playerView.render(game.batch, controller.model.player);
        spellView.render(game.batch, controller.model);
        game.batch.end();

        worldView.renderTilesInFrontOfEntities(worldCamera, currentRoom);

        hudCamera.update();
        game.batch.setProjectionMatrix(hudCamera.combined);
        game.batch.begin();
        hudView.renderVesselAndMasks(game.batch, controller.model, game.assetLoader.hudAnimations);
        achievementPopupView.render(game.batch);
        game.batch.end();

        renderBrightnessOverlay();


        if (controller.model.paused) {
            pauseOverlay.render(delta);
        } else if (controller.model.inInventory) {
            inventoryOverlay.render(delta);
        }
    }

    private void renderBrightnessOverlay() {
        float b = game.settings.getBrightness();
        if (Math.abs(b - 1f) < 0.001f) return;

        game.batch.setProjectionMatrix(hudCamera.combined);
        game.batch.begin();
        if (b < 1f) {
            game.batch.setColor(0f, 0f, 0f, 1f - b);
        } else {
            game.batch.setColor(1f, 1f, 1f, b - 1f);
        }
        game.batch.draw(overlayPixel, 0, 0, Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT);
        game.batch.setColor(1f, 1f, 1f, 1f);
        game.batch.end();
    }

    private void handleToggles() {

        if (Gdx.input.isKeyJustPressed(controller.input.getKey(
                com.hollowknight.controller.InputController.Action.PAUSE))) {
            if (controller.model.inInventory) {
                setInventoryOpen(false);
            } else {
                setPaused(!controller.model.paused);
            }
        }

        if (Gdx.input.isKeyJustPressed(controller.input.getKey(
                com.hollowknight.controller.InputController.Action.INVENTORY))) {
            if (!controller.model.paused) {
                setInventoryOpen(!controller.model.inInventory);
            }
        }
    }



    public void setPaused(boolean paused) {
        controller.model.paused = paused;
        if (paused) {
            pauseOverlay.rebuild();
            Gdx.input.setInputProcessor(pauseOverlay.getStage());
        } else {
            Gdx.input.setInputProcessor(null);
        }
    }

    public void setInventoryOpen(boolean open) {
        controller.model.inInventory = open;
        if (open) {
            inventoryOverlay.rebuild();
            Gdx.input.setInputProcessor(inventoryOverlay.getStage());
        } else {
            Gdx.input.setInputProcessor(null);
        }
    }

    @Override
    public void resize(int width, int height) {
        pauseOverlay.resize(width, height);
        inventoryOverlay.resize(width, height);
    }

    @Override
    public void show() {
        if (controller.model.paused) {
            Gdx.input.setInputProcessor(pauseOverlay.getStage());
        } else if (controller.model.inInventory) {
            Gdx.input.setInputProcessor(inventoryOverlay.getStage());
        } else {
            Gdx.input.setInputProcessor(null);
        }
    }
    @Override public void hide() {}
    @Override public void pause() {}
    @Override public void resume() {}


    public void setSaveSlot(int slot) {
        controller.model.saveSlot = slot;
    }


    public void saveGame() {
        int slot = controller.model.saveSlot;
        if (slot < 0) return;
        game.saveManager.save(controller.model.toSaveData(slot), slot);
    }

    public GameController getController() { return controller; }

    @Override
    public void dispose() {
        shapes.dispose();
        worldView.dispose();
        overlayPixel.dispose();
        achievementPopupView.dispose();
        if (pauseOverlay != null) pauseOverlay.dispose();
        if (inventoryOverlay != null) inventoryOverlay.dispose();
        for (Room r : controller.model.world.getAllRooms().values()) {
            if (r.tiledMap != null) r.tiledMap.dispose();
        }
    }
}