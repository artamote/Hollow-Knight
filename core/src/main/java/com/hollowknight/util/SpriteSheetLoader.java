package com.hollowknight.util;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Slices a single-row horizontal sprite sheet (all frames same width/height,
 * laid out left-to-right) into an array of TextureRegion frames.
 *
 * Used for the real Knight sprite sheets (idle.png, run.png, etc — each is
 * 349x186 per frame, N frames wide, 1 row tall).
 */
public final class SpriteSheetLoader {
    private SpriteSheetLoader() {}

    public static TextureRegion[] slice(Texture sheet, int frameWidth, int frameHeight, int frameCount) {
        TextureRegion[] frames = new TextureRegion[frameCount];
        for (int i = 0; i < frameCount; i++) {
            frames[i] = new TextureRegion(sheet, i * frameWidth, 0, frameWidth, frameHeight);
        }
        return frames;
    }

    /** Auto-detects frame count from sheet width / frameWidth. */
    public static TextureRegion[] sliceAuto(Texture sheet, int frameWidth, int frameHeight) {
        int count = sheet.getWidth() / frameWidth;
        return slice(sheet, frameWidth, frameHeight, count);
    }
}
