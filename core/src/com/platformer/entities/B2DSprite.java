package com.platformer.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.platformer.handlers.Animation;

import static com.platformer.handlers.Box2DConstants.PPM;

public class B2DSprite {
    protected Body body;
    protected Animation animation;
    protected float width;
    protected float height;
    protected boolean isFlipped = false;

    public B2DSprite(Body body) {
        this.body = body;
        animation = new Animation();
    }

    public void setAnimation(TextureRegion[] reg, float delay) {
        animation.setFrames(reg, delay);
        width = reg[0].getRegionWidth();
        height = reg[0].getRegionHeight();
    }

    public void update(float dt) {
        animation.update(dt);
    }

    public void render(SpriteBatch sb) {
        sb.begin();
        sb.draw(animation.getFrame(), body.getPosition().x * PPM - width / 2, body.getPosition().y * PPM - height / 2);
        sb.end();
    }

    public void setIsFlipped(boolean flip) { this.isFlipped = flip; }
    public Body getBody() {return this.body;}
    public Vector2 getPosition() {return body.getPosition();}
    public float getWidth() { return width; }
    public float getHeight() { return height; }

    public Animation getAnimation() {
        return animation;
    }
}
