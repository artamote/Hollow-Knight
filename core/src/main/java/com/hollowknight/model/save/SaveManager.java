package com.hollowknight.model.save;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.hollowknight.util.Constants;

public class SaveManager {

    private static final String SAVE_DIR = "saves/";
    private final Json json = new Json();

    public void save(SaveData data, int slot) {
        FileHandle file = Gdx.files.local(SAVE_DIR + "slot" + slot + ".json");
        file.writeString(json.toJson(data), false);
    }

    public SaveData load(int slot) {
        FileHandle file = Gdx.files.local(SAVE_DIR + "slot" + slot + ".json");
        if (!file.exists()) return null;
        return json.fromJson(SaveData.class, file.readString());
    }

    public boolean slotExists(int slot) {
        return Gdx.files.local(SAVE_DIR + "slot" + slot + ".json").exists();
    }

    public void deleteSlot(int slot) {
        FileHandle file = Gdx.files.local(SAVE_DIR + "slot" + slot + ".json");
        if (file.exists()) file.delete();
    }

    public SaveData[] loadAll() {
        SaveData[] slots = new SaveData[Constants.SAVE_SLOTS];
        for (int i = 0; i < Constants.SAVE_SLOTS; i++) {
            slots[i] = load(i);
        }
        return slots;
    }
}