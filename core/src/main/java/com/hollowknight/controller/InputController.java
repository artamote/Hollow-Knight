package com.hollowknight.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import java.util.EnumMap;
import java.util.Map;


public class InputController {

    public enum Action { LEFT, RIGHT, JUMP, DASH, ATTACK, FOCUS, DOWN, UP, PAUSE, INVENTORY, CAST }

    private final Map<Action, Integer> keymap = new EnumMap<>(Action.class);

    public InputController() {
        resetToDefault();
    }

    public void resetToDefault() {
        keymap.put(Action.LEFT,      Input.Keys.LEFT);
        keymap.put(Action.RIGHT,     Input.Keys.RIGHT);
        keymap.put(Action.JUMP,      Input.Keys.Z);
        keymap.put(Action.DASH,      Input.Keys.C);
        keymap.put(Action.ATTACK,    Input.Keys.X);
        keymap.put(Action.FOCUS,     Input.Keys.A);
        keymap.put(Action.DOWN,      Input.Keys.DOWN);
        keymap.put(Action.UP,        Input.Keys.UP);
        keymap.put(Action.PAUSE,     Input.Keys.ESCAPE);
        keymap.put(Action.INVENTORY, Input.Keys.I);
        keymap.put(Action.CAST,      Input.Keys.S);
    }

    public void rebind(Action action, int newKey) { keymap.put(action, newKey); }
    public int getKey(Action action) { return keymap.get(action); }

    public boolean isDown(Action a)      { return Gdx.input.isKeyPressed(keymap.get(a)); }
    public boolean isJustPressed(Action a){ return Gdx.input.isKeyJustPressed(keymap.get(a)); }
}