package com.platformer.handlers;


import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;

public class InputProcessor extends InputAdapter {
    public boolean keyDown (int keycode) {
        if(keycode == Input.Keys.UP)
            GameInput.setKey(GameInput.JUMP, true);

        if(keycode == Input.Keys.LEFT)
            GameInput.setKey(GameInput.LEFT, true);

        if(keycode == Input.Keys.RIGHT)
            GameInput.setKey(GameInput.RIGHT, true);

        if(keycode == Input.Keys.DOWN)
            GameInput.setKey(GameInput.DOWN, true);
        return true;

    }

    public boolean keyUp (int keycode) {
        if(keycode == Input.Keys.UP)
            GameInput.setKey(GameInput.JUMP, false);

        if(keycode == Input.Keys.LEFT)
            GameInput.setKey(GameInput.LEFT, false);

        if(keycode == Input.Keys.RIGHT)
            GameInput.setKey(GameInput.RIGHT, false);

        if(keycode == Input.Keys.DOWN)
            GameInput.setKey(GameInput.DOWN, false);

        return true;
    }


}
