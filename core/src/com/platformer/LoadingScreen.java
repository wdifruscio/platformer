package com.platformer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.sun.xml.internal.bind.v2.runtime.reflect.opt.Const;

public class LoadingScreen extends ScreenAdapter {
    private static final float PROGRESS_BAR_WIDTH = 100;
    private static final float PROGRESS_BAR_HEIGHT = 25;
    private ShapeRenderer shapeRenderer;
    private Viewport viewport;
    private OrthographicCamera camera;
    private float progress = 0;

    private Platformer platformer;

    public LoadingScreen(Platformer platformer){
        this.platformer = platformer;
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    public void show() {
        camera = new OrthographicCamera();
        camera.position.set(Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT, 0);
        camera.update();
        viewport = new FitViewport(Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT, camera);
        shapeRenderer = new ShapeRenderer();
        platformer.getAssetManager().load("platformer.tmx", TiledMap.class);
    }

    @Override
    public void render(float delta) {
        update();
        clearScreen();
        draw();
    }

    @Override
    public void dispose() {
        shapeRenderer.dispose();
    }

    private void update() {
        if(platformer.getAssetManager().update()) {
            platformer.setScreen(new GameScreen(platformer));
        } else {
            progress = platformer.getAssetManager().getProgress();
        }
    }

    private void clearScreen() {
        Gdx.gl.glClearColor(Color.BLACK.r, Color.BLACK.g, Color.BLACK.b, Color.BLACK.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }
    private void draw() {
        shapeRenderer.setProjectionMatrix(camera.projection);
        shapeRenderer.setTransformMatrix(camera.view);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.WHITE);
        shapeRenderer.rect(
                (Constants.WORLD_WIDTH  - PROGRESS_BAR_WIDTH) / 2, Constants.WORLD_HEIGHT / 2 - PROGRESS_BAR_HEIGHT / 2,
                progress * PROGRESS_BAR_WIDTH, PROGRESS_BAR_HEIGHT);
        shapeRenderer.end();
    }

}
