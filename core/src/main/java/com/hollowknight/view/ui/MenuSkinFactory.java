package com.hollowknight.view.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import java.util.ArrayList;



public final class MenuSkinFactory {

    private MenuSkinFactory() {}

    public static final Color BG_DARK      = new Color(0.04f, 0.04f, 0.07f, 0.92f);
    public static final Color PANEL_FILL   = new Color(0.07f, 0.08f, 0.12f, 0.95f);
    public static final Color BORDER_SOFT  = new Color(0.45f, 0.52f, 0.65f, 1f);
    public static final Color BORDER_HOVER = new Color(0.62f, 0.78f, 0.95f, 1f);
    public static final Color TEXT_PALE    = new Color(0.90f, 0.92f, 0.97f, 1f);
    public static final Color TEXT_DIM     = new Color(0.50f, 0.53f, 0.60f, 1f);
    public static final Color ACCENT       = new Color(0.62f, 0.78f, 0.95f, 1f);

    private static final ArrayList<Texture> generated = new ArrayList<>();

    
    public static void disposeAll() {
        for (Texture t : generated) t.dispose();
        generated.clear();
    }

    public static Skin build() {
        Skin skin = new Skin();

        BitmapFont font = new BitmapFont();
        font.getData().setScale(1.3f);
        skin.add("default-font", font, BitmapFont.class);

        
        NinePatchDrawable panelBg = squarePatch(32, PANEL_FILL, BORDER_SOFT, 2, 7);
        skin.add("panel", panelBg);

        
        NinePatchDrawable btnUp       = squarePatch(28, new Color(0.10f, 0.11f, 0.16f, 0.95f), BORDER_SOFT, 2, 6);
        NinePatchDrawable btnOver     = squarePatch(28, new Color(0.13f, 0.15f, 0.21f, 0.95f), BORDER_HOVER, 2, 6);
        NinePatchDrawable btnDown     = squarePatch(28, new Color(0.18f, 0.22f, 0.30f, 0.95f), BORDER_HOVER, 3, 6);
        NinePatchDrawable btnChecked  = squarePatch(28, new Color(0.15f, 0.19f, 0.27f, 0.95f), ACCENT, 3, 6);
        NinePatchDrawable btnDisabled = squarePatch(28, new Color(0.06f, 0.06f, 0.08f, 0.7f), TEXT_DIM, 1, 6);

        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.up = btnUp;
        buttonStyle.over = btnOver;
        buttonStyle.down = btnDown;
        buttonStyle.checked = btnChecked;
        buttonStyle.disabled = btnDisabled;
        buttonStyle.font = font;
        buttonStyle.fontColor = TEXT_PALE;
        buttonStyle.overFontColor = BORDER_HOVER;
        buttonStyle.downFontColor = ACCENT;
        buttonStyle.checkedFontColor = ACCENT;
        buttonStyle.disabledFontColor = TEXT_DIM;
        skin.add("default", buttonStyle);


        Label.LabelStyle labelStyle = new Label.LabelStyle(font, TEXT_PALE);
        skin.add("default", labelStyle);
        Label.LabelStyle titleStyle = new Label.LabelStyle(font, ACCENT);
        skin.add("title", titleStyle);
        Label.LabelStyle dimStyle = new Label.LabelStyle(font, TEXT_DIM);
        skin.add("dim", dimStyle);


        Window.WindowStyle windowStyle = new Window.WindowStyle(font, TEXT_PALE, panelBg);
        skin.add("default", windowStyle);


        NinePatchDrawable cbOff = squarePatch(20, new Color(0.08f, 0.08f, 0.11f, 0.9f), BORDER_SOFT, 2, 4);
        NinePatchDrawable cbOn  = squarePatch(20, ACCENT, ACCENT, 2, 4);
        CheckBox.CheckBoxStyle checkBoxStyle = new CheckBox.CheckBoxStyle();
        checkBoxStyle.checkboxOff = cbOff;
        checkBoxStyle.checkboxOn = cbOn;
        checkBoxStyle.font = font;
        checkBoxStyle.fontColor = TEXT_PALE;
        skin.add("default", checkBoxStyle);


        NinePatchDrawable sliderBg = barPatch(32, 8, new Color(0.10f, 0.10f, 0.14f, 0.9f), BORDER_SOFT, 1, 3);
        TextureRegionDrawable knob = new TextureRegionDrawable(new TextureRegion(circleTexture(20, ACCENT, BORDER_HOVER)));
        Slider.SliderStyle sliderStyle = new Slider.SliderStyle();
        sliderStyle.background = sliderBg;
        sliderStyle.knob = knob;
        skin.add("default-horizontal", sliderStyle);


        NinePatchDrawable progressFill = barPatch(32, 8, ACCENT, ACCENT, 0, 3);
        ProgressBar.ProgressBarStyle progressStyle = new ProgressBar.ProgressBarStyle();
        progressStyle.background = sliderBg;
        progressStyle.knob = knob;
        progressStyle.knobBefore = progressFill;
        skin.add("default-horizontal", progressStyle);


        NinePatchDrawable listBg        = squarePatch(28, PANEL_FILL, BORDER_SOFT, 2, 6);
        NinePatchDrawable listSelection = squarePatch(28, new Color(0.18f, 0.22f, 0.30f, 0.95f), ACCENT, 2, 6);

        List.ListStyle listStyle = new List.ListStyle();
        listStyle.font = font;
        listStyle.fontColorSelected = ACCENT;
        listStyle.fontColorUnselected = TEXT_PALE;
        listStyle.selection = listSelection;
        listStyle.background = listBg;
        skin.add("default", listStyle);

        ScrollPane.ScrollPaneStyle scrollStyle = new ScrollPane.ScrollPaneStyle();
        scrollStyle.background = listBg;
        skin.add("default", scrollStyle);

        NinePatchDrawable selectBg   = squarePatch(28, new Color(0.10f, 0.11f, 0.16f, 0.95f), BORDER_SOFT, 2, 6);
        NinePatchDrawable selectOpen = squarePatch(28, new Color(0.13f, 0.15f, 0.21f, 0.95f), BORDER_HOVER, 2, 6);
        SelectBox.SelectBoxStyle selectBoxStyle = new SelectBox.SelectBoxStyle();
        selectBoxStyle.font = font;
        selectBoxStyle.fontColor = TEXT_PALE;
        selectBoxStyle.background = selectBg;
        selectBoxStyle.backgroundOver = selectOpen;
        selectBoxStyle.backgroundOpen = selectOpen;
        selectBoxStyle.scrollStyle = scrollStyle;
        selectBoxStyle.listStyle = listStyle;
        skin.add("default", selectBoxStyle);

        return skin;
    }


    private static NinePatchDrawable squarePatch(int size, Color fill, Color border, int borderPx, int margin) {
        Pixmap pm = new Pixmap(size, size, Pixmap.Format.RGBA8888);
        pm.setColor(fill);
        pm.fill();
        pm.setColor(border);
        for (int i = 0; i < borderPx; i++) {
            pm.drawRectangle(i, i, size - i * 2, size - i * 2);
        }
        Texture tex = new Texture(pm);
        pm.dispose();
        generated.add(tex);
        NinePatch patch = new NinePatch(tex, margin, margin, margin, margin);
        return new NinePatchDrawable(patch);
    }


    private static NinePatchDrawable barPatch(int width, int height, Color fill, Color border, int borderPx, int marginX) {
        Pixmap pm = new Pixmap(width, height, Pixmap.Format.RGBA8888);
        pm.setColor(fill);
        pm.fill();
        pm.setColor(border);
        for (int i = 0; i < borderPx; i++) {
            pm.drawRectangle(i, i, width - i * 2, height - i * 2);
        }
        Texture tex = new Texture(pm);
        pm.dispose();
        generated.add(tex);
        NinePatch patch = new NinePatch(tex, marginX, marginX, 1, 1);
        return new NinePatchDrawable(patch);
    }

    private static Texture circleTexture(int diameter, Color fill, Color border) {
        Pixmap pm = new Pixmap(diameter, diameter, Pixmap.Format.RGBA8888);
        pm.setColor(0, 0, 0, 0);
        pm.fill();
        pm.setColor(fill);
        pm.fillCircle(diameter / 2, diameter / 2, diameter / 2 - 1);
        pm.setColor(border);
        pm.drawCircle(diameter / 2, diameter / 2, diameter / 2 - 1);
        Texture tex = new Texture(pm);
        pm.dispose();
        generated.add(tex);
        return tex;
    }
}