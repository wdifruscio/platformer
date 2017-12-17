package com.platformer;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.platformer.handlers.GameInput;
import com.platformer.handlers.GameStateManager;
import com.platformer.handlers.InputProcessor;
import com.platformer.handlers.ResourceManager;


public class Game implements ApplicationListener {

    public static final String TITLE = "Platformer";
    public static final int WIDTH = 320;
    public static final int HEIGHT = 240;
    public static final int SCALE = 2;

    public static final float STEP = 1/60f;
    private float accumulatedTime = 0;


    private SpriteBatch batch;
    private OrthographicCamera camera;
    private OrthographicCamera hud;
    private GameStateManager gsm;

    public SpriteBatch getBatch() {
        return batch;
    }
    public OrthographicCamera getCamera() {
        return camera;
    }
    public OrthographicCamera getHud() {
        return hud;
    }

    public static ResourceManager resources;

    @Override
    public void create() {
        Gdx.input.setInputProcessor(new InputProcessor());
        batch = new SpriteBatch();
        camera = new OrthographicCamera();
        hud = new OrthographicCamera();

        camera.setToOrtho(false,WIDTH, HEIGHT);
        hud.setToOrtho(false, WIDTH, HEIGHT);

        resources = new ResourceManager();
        resources.loadTexture("penguin.png", "penguin");
        gsm = new GameStateManager(this);
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void render() {
        accumulatedTime += Gdx.graphics.getDeltaTime();
        while(accumulatedTime >= STEP) {
            accumulatedTime -= STEP;
            gsm.update(STEP);
            gsm.render();
            GameInput.update();
        }
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {

    }
}
