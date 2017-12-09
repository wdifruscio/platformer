package com.platformer.states;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.platformer.handlers.GameStateManager;



//inherits batch, game, camera and hud from game state;

public class Play extends GameState {

    private BitmapFont font = new BitmapFont();

    public Play(GameStateManager gsm) {
        super(gsm);
    }

    @Override
    public void handleInput() {

    }

    @Override
    public void update(float dt) {

    }

    @Override
    public void render() {
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        font.draw(batch, "Hello", 100, 100);
        batch.end();

    }

    @Override
    public void dispose() {

    }


}
