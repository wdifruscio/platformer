package com.platformer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;

import javax.xml.soap.Text;

public class Player {
    private static final float MAX_X_SPEED = 2;
    private static final float MAX_Y_SPEED = 1;
    private static final float GRAVITY = 0.1F;
    public static final int WIDTH = 16;
    public static final int HEIGHT = 15;
    private static final float MAX_JUMP_DISTANCE = 5 * HEIGHT;
    private boolean blockJump = false;
    private float jumpYDistance = 0;
    private final Rectangle collisionRectangle = new Rectangle(0,0,WIDTH,HEIGHT);
    private float x = 20;
    private float y = 20;
    private float xSpeed = 0;
    private float ySpeed = 0;

    private float jumpTimer = 0;

    private boolean canJump = true;
    private boolean isJumping = false;
    private int lastDirectionPressed;

    private float animationTimer = 0;
    private final Animation<TextureRegion> walking;
    private final Animation<TextureRegion> standing;
    private final Animation<TextureRegion> jumpUp;
    private final Animation<TextureRegion> jumpDown;

    public Player(Texture texture, TextureAtlas textureAtlas) {
        TextureRegion[] regions = TextureRegion.split(texture, WIDTH, HEIGHT)[0];


        walking = new Animation<TextureRegion>(0.1f, textureAtlas.findRegions("run"));
        standing = new Animation<TextureRegion>(0.1F, textureAtlas.findRegions("idle"));
        jumpUp = new Animation<TextureRegion>(0.1F, textureAtlas.findRegion("jump", 0));
        jumpDown = new Animation<TextureRegion>(0.1F, textureAtlas.findRegion("jump", 3));
        walking.setPlayMode(Animation.PlayMode.LOOP);
        standing.setPlayMode(Animation.PlayMode.LOOP);
        jumpUp.setPlayMode(Animation.PlayMode.NORMAL);
        jumpDown.setPlayMode(Animation.PlayMode.NORMAL);

        animationTimer = 0;
    }

    public void update(float delta) {
        Input input = Gdx.input;
        if (input.isKeyPressed(Input.Keys.RIGHT)) {
            lastDirectionPressed = Input.Keys.RIGHT;
            xSpeed = MAX_X_SPEED;
        }
        else if (input.isKeyPressed(Input.Keys.LEFT)) {
            lastDirectionPressed = Input.Keys.LEFT;
            xSpeed = -MAX_X_SPEED;
        }
        else xSpeed = 0;

        if (input.isKeyPressed(Input.Keys.UP) && !blockJump) {
            ySpeed = MAX_Y_SPEED * 5;
            blockJump = true;
        }

        if(jumpTimer >= 2f && blockJump) {
            landed();
        }

        ySpeed -= 0.2;

        x += xSpeed;
        y += ySpeed;
        animationTimer += delta;
        jumpTimer += delta;
        updateCollisionRectangle();
    }

    //rudimentary jump -> check if input is pressed, flip boolean and add speed to y,
    // apply gravity while y is large, when its not flip back to true
    public void landed() {
        jumpTimer=0;
        blockJump = false;
        ySpeed = 0;
    }



    public void drawDebug(ShapeRenderer shapeRenderer) {
        shapeRenderer.rect(collisionRectangle.x, collisionRectangle.y, collisionRectangle.width, collisionRectangle.height);
    }

    public void draw(SpriteBatch batch) {
        TextureRegion toDraw = standing.getKeyFrame(animationTimer);
        if(xSpeed > 0 && !isJumping) {
            toDraw = walking.getKeyFrame(animationTimer,true);
        }
        else if(xSpeed < 0 && !isJumping) {
            toDraw = walking.getKeyFrame(animationTimer, true);
        }
        else if(ySpeed > 0) {
            toDraw = jumpUp.getKeyFrame(animationTimer);
        }
        else if(ySpeed < -1) {
            toDraw = jumpDown.getKeyFrame(animationTimer);
        }
        else {
            toDraw = standing.getKeyFrame(animationTimer);
        }

        if(lastDirectionPressed == Input.Keys.RIGHT) {
            if (toDraw.isFlipX()) toDraw.flip(true,false);
        }
        if(lastDirectionPressed == Input.Keys.LEFT) {
            if (!toDraw.isFlipX()) toDraw.flip(true,false);
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

    public Rectangle getCollisionRectangle() {
        return collisionRectangle;
    }
}
