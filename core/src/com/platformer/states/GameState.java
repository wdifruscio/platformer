package com.platformer.states;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.platformer.Game;
import com.platformer.handlers.GameStateManager;

public abstract class GameState {

    protected GameStateManager gsm;
    protected Game game;
    protected SpriteBatch batch;
    protected OrthographicCamera camera;
    protected OrthographicCamera hud;

    protected GameState(GameStateManager gsm) {
        this.gsm = gsm;
        game = gsm.game();
        batch = game.getBatch();
        camera = game.getCamera();
        hud = game.getHud();
    }

    public abstract void handleInput();
    public abstract void update(float dt);
    public abstract void render();
    public abstract void dispose();
}
