package com.platformer.handlers;

import com.platformer.Game;
import com.platformer.states.GameState;
import com.platformer.states.Play;

import java.util.Stack;

public class GameStateManager {
    public Game game() {
        return game;
    }

    private Game game;
    private Stack<GameState> gameStates;

    public static final int PLAY = 100;

    public GameStateManager(Game game) {
        this.game = game;
        gameStates = new Stack<GameState>();
        pushState(PLAY);
    }

    public void update(float dt) {
        gameStates.peek().update(dt);
    }

    public void render() {
        gameStates.peek().render();
    }

    private GameState getState(int state) {
        if(state == PLAY) {
            return new Play(this);
        }
        return null;
    }

    public void setState(int state) {
        popState();
        pushState(state);
    }

    public void pushState(int state) {
        gameStates.push(getState(state));
    }

    public void popState() {
        GameState g = gameStates.pop();
        g.dispose();
    }


}
