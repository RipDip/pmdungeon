package de.pmdungeon.graphic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import de.pmdungeon.interfaces.IHUDElement;
import de.pmdungeon.tools.Constants;

import java.util.ArrayList;
import java.util.List;

/** Holds the HUD. */
public class HUD extends Stage {
    private final SpriteBatch hudBatch;
    private final OrthographicCamera hudCamera;
    private final List<IHUDElement> hudElements;
    private boolean usePixelSystem = false;

    public HUD(SpriteBatch batch) {
        super(new ScreenViewport(), batch);
        hudBatch = batch;
        hudCamera = new OrthographicCamera();
        hudCamera.position.set(0, 0, 0);
        hudCamera.update();
        hudElements = new ArrayList<>();
    }

    /**
     * Toogle between grid and pixel based system
     *
     * @param b activate or deactivate pixel system
     */
    public void usePixelSystem(boolean b) {
        this.usePixelSystem = b;
    }

    /**
     * Add an element to draw on the hud
     *
     * @param element element to add
     */
    public void addHudElement(IHUDElement element) {
        this.hudElements.add(element);
    }

    /**
     * Remove element
     *
     * @param element element to remove
     */
    public void removeHudElement(IHUDElement element) {
        if (hudElements.contains(element))
            this.hudElements.remove(element);
    }

    /** Main loop of the hud. */
    public void draw() {
        if (!usePixelSystem) {
            hudCamera.update();
            hudBatch.setProjectionMatrix(hudCamera.combined);
        }
        drawElements();
        resize();
        super.act();
        super.draw();
    }

    /** Draws all the elements */
    private void drawElements() {
        for (IHUDElement element : hudElements) {
            Texture texture = element.getTexture();
            Sprite sprite = new Sprite(texture);
            // scaling
            sprite.setSize(element.getWidth(), element.getHeight() / texture.getWidth());
            sprite.setPosition(element.getPosition().x, element.getPosition().y);
            hudBatch.begin();
            sprite.draw(hudBatch);
            hudBatch.end();
        }
    }

    /** Resizing the camera according to the size of the window. */
    public void resize() {
        if (usePixelSystem)
            return;
        hudCamera.setToOrtho(false, Constants.VIRTUALHEIGHT * Constants.WIDTH / (float) Constants.HEIGHT,
                Constants.VIRTUALHEIGHT);
        hudBatch.setProjectionMatrix(hudCamera.combined);
    }

    public SpriteBatch getHudBatch() {
        return this.hudBatch;
    }

    /**
     * Draws a given text on the screen.
     *
     * @param text        text to draw
     * @param fontPath    font to use
     * @param color       color to use
     * @param size        font size to use
     * @param width       width of the text box
     * @param height      height of the text box
     * @param x           x-position in pixel
     * @param y           y-position in pixel
     * @param borderWidth border width for the text
     * @return Label (use this to alter text or remove the text later)
     */
    public Label drawText(String text, String fontPath, Color color, int size, int width, int height, int x, int y,
            int borderWidth) {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal(fontPath));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = size;
        parameter.borderWidth = borderWidth;
        parameter.color = color;
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = generator.generateFont(parameter);
        generator.dispose();
        Label label = new Label(text, labelStyle);
        label.setSize(width, height);
        label.setPosition(x, y);

        this.addActor(label);
        return label;
    }

    /**
     * Draws a given text on the screen.
     *
     * @param text        text to draw
     * @param fontPath    font to use
     * @param color       color to use
     * @param size        font size to use
     * @param width       width of the text box
     * @param height      height of the text box
     * @param x           x-position in pixel
     * @param y           y-position in pixel
     * @return Label (use this to alter text or remove the text later)
     */
    public Label drawText(String text, String fontPath, Color color, int size, int width, int height, int x, int y) {
        return this.drawText(text, fontPath, color, size, width, height, x, y, 1);
    }
}
