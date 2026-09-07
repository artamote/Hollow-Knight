package com.hollowknight.view.hud;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.hollowknight.model.AchievementSystem.Achievement;
import com.hollowknight.observer.EventBus;
import com.hollowknight.observer.GameEvent;
import com.hollowknight.util.Constants;

import java.util.ArrayDeque;
import java.util.Deque;


public class AchievementPopupView implements EventBus.Listener {

    private static final float WIDTH = 360f;
    private static final float HEIGHT = 74f;
    private static final float MARGIN_TOP = 20f;
    private static final float SLIDE_IN_TIME = 0.35f;
    private static final float HOLD_TIME = 2.6f;
    private static final float SLIDE_OUT_TIME = 0.35f;
    private static final float TOTAL_TIME = SLIDE_IN_TIME + HOLD_TIME + SLIDE_OUT_TIME;

    private static final Color BG_COLOR = new Color(0.05f, 0.05f, 0.08f, 0.92f);
    private static final Color BORDER_COLOR = new Color(0.75f, 0.68f, 0.4f, 1f);
    private static final Color HEADER_COLOR = new Color(0.9f, 0.85f, 0.55f, 1f);

    private final Deque<Achievement> queue = new ArrayDeque<>();
    private Achievement current;
    private float timer;

    private final Texture pixel;
    private final BitmapFont font;
    private final GlyphLayout layout = new GlyphLayout();

    public AchievementPopupView(BitmapFont font) {
        this.font = font;
        Pixmap pm = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pm.setColor(1f, 1f, 1f, 1f);
        pm.fill();
        pixel = new Texture(pm);
        pm.dispose();

        EventBus.subscribe(GameEvent.ACHIEVEMENT_UNLOCKED, this);
    }

    @Override
    public void onEvent(GameEvent event, Object data) {
        if (event == GameEvent.ACHIEVEMENT_UNLOCKED && data instanceof Achievement) {
            queue.addLast((Achievement) data);
        }
    }

    public void update(float delta) {
        if (current == null) {
            if (queue.isEmpty()) return;
            current = queue.pollFirst();
            timer = 0f;
        }

        timer += delta;
        if (timer >= TOTAL_TIME) {
            current = null;
        }
    }

    public void render(SpriteBatch batch) {
        if (current == null) return;

        float x = (Constants.SCREEN_WIDTH - WIDTH) / 2f;
        float restY = Constants.SCREEN_HEIGHT - MARGIN_TOP - HEIGHT;

        float y;
        float alpha;
        if (timer < SLIDE_IN_TIME) {
            float t = timer / SLIDE_IN_TIME;
            float eased = 1f - (1f - t) * (1f - t);
            y = restY + (1f - eased) * (HEIGHT + MARGIN_TOP);
            alpha = eased;
        } else if (timer < SLIDE_IN_TIME + HOLD_TIME) {
            y = restY;
            alpha = 1f;
        } else {
            float t = (timer - SLIDE_IN_TIME - HOLD_TIME) / SLIDE_OUT_TIME;
            float eased = t * t;
            y = restY + eased * (HEIGHT + MARGIN_TOP);
            alpha = 1f - eased;
        }

        Color prevBatchColor = batch.getColor();
        float pr = prevBatchColor.r, pg = prevBatchColor.g, pb = prevBatchColor.b, pa = prevBatchColor.a;

        batch.setColor(BORDER_COLOR.r, BORDER_COLOR.g, BORDER_COLOR.b, alpha);
        batch.draw(pixel, x - 2, y - 2, WIDTH + 4, HEIGHT + 4);
        batch.setColor(BG_COLOR.r, BG_COLOR.g, BG_COLOR.b, alpha * BG_COLOR.a);
        batch.draw(pixel, x, y, WIDTH, HEIGHT);

        font.setColor(HEADER_COLOR.r, HEADER_COLOR.g, HEADER_COLOR.b, alpha);
        layout.setText(font, "Achievement Unlocked");
        font.draw(batch, layout, x + (WIDTH - layout.width) / 2f, y + HEIGHT - 14f);

        font.setColor(1f, 1f, 1f, alpha);
        layout.setText(font, current.title);
        font.draw(batch, layout, x + (WIDTH - layout.width) / 2f, y + HEIGHT - 38f);

        font.setColor(0.8f, 0.8f, 0.8f, alpha);
        layout.setText(font, current.description);
        font.draw(batch, layout, x + (WIDTH - layout.width) / 2f, y + HEIGHT - 58f);

        batch.setColor(pr, pg, pb, pa);
        font.setColor(Color.WHITE);
    }

    public void dispose() {
        pixel.dispose();
        EventBus.unsubscribe(GameEvent.ACHIEVEMENT_UNLOCKED, this);
    }
}