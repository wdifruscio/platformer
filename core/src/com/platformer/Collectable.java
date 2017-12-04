package com.platformer;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;

import java.awt.*;

public class Collectable {
    public static final int WIDTH = 16;
    public static final int HEIGHT = 16;
    private final Rectangle collision;
    private final Texture texture;
    private final float x;
    private final float y;
    public Collectable(Texture texture, int x, int y) {
        this.texture = texture;
        this.x = x;
        this.y = y;
        this.collision = new Rectangle(x,y, WIDTH,HEIGHT);
    }

    public void draw(Batch batch) {
        batch.draw(texture, x, y);
    }
}