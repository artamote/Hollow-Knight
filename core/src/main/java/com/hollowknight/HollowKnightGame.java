package com.hollowknight;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.hollowknight.model.save.SaveManager;
import com.hollowknight.util.AssetLoader;
import com.hollowknight.util.GameSettings;
import com.hollowknight.view.screen.MainMenuScreen;


public class HollowKnightGame extends Game {

    
    public SpriteBatch batch;
    public AssetLoader assetLoader;
    public GameSettings settings;   
    public SaveManager saveManager; 

    @Override
    public void create() {
        batch = new SpriteBatch();
        settings = new GameSettings();
        assetLoader = new AssetLoader();
        assetLoader.loadAll(settings);
        saveManager = new SaveManager();


        setScreen(new MainMenuScreen(this));
    }

    @Override
    public void dispose() {
        batch.dispose();
        assetLoader.dispose();
    }
}