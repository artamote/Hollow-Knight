package com.hollowknight.desktop;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.hollowknight.HollowKnightGame;
import com.hollowknight.util.Constants;

public class DesktopLauncher {
    public static void main(String[] args) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setTitle(Constants.TITLE);
        config.setWindowedMode(Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT);
        config.useVsync(true);
        config.setForegroundFPS(60);
        new Lwjgl3Application(new HollowKnightGame(), config);
    }
}
