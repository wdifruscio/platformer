package com.platformer.entities;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.platformer.Game;


public class Background {

    private TextureRegion image;
    private OrthographicCamera gameCam;
    private float scale;

    private float x;
    private float y;
    private int numDrawX;
    private int numDrawY;

    private float dx;
    private float dy;

    public Background(TextureRegion image, OrthographicCamera gameCam, float scale) {
        this.image = image;
        this.gameCam = gameCam;
        this.scale = scale;
        numDrawX = Game.WIDTH / image.getRegionWidth() + 2;
        numDrawY = Game.HEIGHT / image.getRegionHeight() + 2;
    }

    public void setVector(float dx, float dy) {
        this.dx = dx;
        this.dy = dy;
    }

    public void update(float dt) {
        x -= scale * dt;
//        y += (dy * scale) * dt;
    }

    public void render(SpriteBatch sb) {

        float x = ((this.x + gameCam.viewportWidth / 2 - gameCam.position.x) * scale) % image.getRegionWidth();
        float y = ((this.y + gameCam.viewportHeight / 2 - gameCam.position.y) * scale) % image.getRegionHeight();

        sb.begin();

        int colOffset = x > 0 ? -1 : 0;
        int rowOffset = y > 0 ? -1 : 0;
        for(int row = 0; row < numDrawY; row++) {
            for(int col = 0; col < numDrawX; col++) {
                sb.draw(image, x + (col + colOffset) * image.getRegionWidth(), y + (rowOffset + row) * image.getRegionHeight());
            }
        }

        sb.end();

    }

}
