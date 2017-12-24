package com.platformer.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.platformer.Game;


public class Score {
    BitmapFont font;
    private int score;
    private float counter = 0;
    private static float RESET = 0.05f;

    public Score() {
        font  = new BitmapFont();
        font.setColor(Color.RED);
    }

    public void update(float dt) {
        counter+=dt;
        if(counter > RESET) {
            score++;
            counter = 0;
        }
    }

    public void draw(SpriteBatch sb) {
        sb.begin();
        font.draw(sb, String.valueOf(score), Game.WIDTH - 25, Game.HEIGHT -5);
        sb.end();
    }
}
