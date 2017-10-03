package com.platformer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;

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

    public void update(float delta) {
        Input input = Gdx.input;
        if (input.isKeyPressed(Input.Keys.RIGHT)) xSpeed = MAX_X_SPEED;
        else if (input.isKeyPressed(Input.Keys.LEFT)) xSpeed = -MAX_X_SPEED;
        else xSpeed = 0;

        jump(input, delta);


        x += xSpeed;
        y += ySpeed;
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
