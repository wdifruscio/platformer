package com.platformer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;

import javax.xml.soap.Text;

public class Player {
    private static final float MAX_X_SPEED = 2;
    private static final float MAX_Y_SPEED = 2;
    private static final float GRAVITY = 0.1F;
    public static final int WIDTH = 16;
    public static final int HEIGHT = 15;
    private final Rectangle collisionRectangle = new Rectangle(0,0,WIDTH,HEIGHT);
    private float x = 0;
    private float y = 0;
    private float xSpeed = 0;
    private float ySpeed = 0;

    private boolean canJump = true;
    private boolean isJumping = false;
    private float jumpYDistance = 0;
    private static final float MAX_JUMP_DISTANCE = 3 * HEIGHT;

    private float animationTimer = 0;
    private final Animation<TextureRegion> walking;
    private final Animation<TextureRegion> standing;
    private final Animation<TextureRegion> jumpUp;
    private final Animation<TextureRegion> jumpDown;

    public Player(Texture texture) {
        TextureRegion[] tmp = TextureRegion.split(texture, WIDTH, HEIGHT)[0];
        TextureRegion[] regions = new TextureRegion[tmp.length];

        for(int i = 0; i < tmp.length; i++) {
            regions[i] = tmp[i];
        }


        walking = new Animation<TextureRegion>(0.25F, regions);
        standing = new Animation<TextureRegion>(0.25F, regions[0], regions[0]);
        jumpUp = new Animation<TextureRegion>(0.25F, regions[2], regions[2]);
        jumpDown = new Animation<TextureRegion>(0.25F, regions[3], regions[3]);
        walking.setPlayMode(Animation.PlayMode.LOOP);
        standing.setPlayMode(Animation.PlayMode.LOOP);
        jumpUp.setPlayMode(Animation.PlayMode.LOOP);
        jumpDown.setPlayMode(Animation.PlayMode.LOOP);
    }

    public void update(float delta) {
        Input input = Gdx.input;
        if (input.isKeyPressed(Input.Keys.RIGHT)) xSpeed = MAX_X_SPEED;
        else if (input.isKeyPressed(Input.Keys.LEFT)) xSpeed = -MAX_X_SPEED;
        else xSpeed = 0;
        jump(input, delta);

        x += xSpeed;
        y += ySpeed;
        animationTimer += delta;
        updateCollisionRectangle();
    }

    //rudimentary jump -> check if input is pressed, flip boolean and add speed to y,
    // apply gravity while y is large, when its not flip back to true
    private void jump(Input input, float delta) {
        if (input.isKeyPressed(Input.Keys.UP) && canJump) {
            ySpeed = 5;
            canJump = false;
        }
        if (ySpeed >= -5){
            ySpeed -= GRAVITY;
        } else {
            canJump = true;
        }

    }

    public void drawDebug(ShapeRenderer shapeRenderer) {
        shapeRenderer.rect(collisionRectangle.x, collisionRectangle.y, collisionRectangle.width, collisionRectangle.height);
    }

    public void draw(SpriteBatch batch) {
        TextureRegion toDraw = standing.getKeyFrame(animationTimer,true);
        if (xSpeed != 0) {
            toDraw = walking.getKeyFrame(animationTimer, true);
        }
        if (ySpeed > 0) {
            toDraw = jumpUp.getKeyFrame(animationTimer, true);
        } else if (ySpeed < 0) {
            toDraw = jumpDown.getKeyFrame(animationTimer, true);
        }

        if (xSpeed < 0) {
            if (!toDraw.isFlipX()) toDraw.flip(true,false);
        } else if (xSpeed > 0) {
            if (toDraw.isFlipX()) toDraw.flip(true,false);
        }

        batch.draw(toDraw, x, y);
    }

    private void updateCollisionRectangle() {
        collisionRectangle.setPosition(x,y);
    }

    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
        updateCollisionRectangle();
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }
}
