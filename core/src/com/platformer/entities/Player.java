package com.platformer.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.platformer.Game;

public class Player extends B2DSprite {
    TextureRegion[] walkRegion;
    TextureRegion[] dodge;
    private boolean isDodging;

    public Player(Body body) {
        super(body);
        Texture texture = Game.resources.getTexture("penguin");
        walkRegion = TextureRegion.split(texture, 32, 32)[1];
        dodge = TextureRegion.split(texture, 32,32)[2];
        setAnimation(walkRegion, 1/10f);
    }

    public void updateAnimation() {
        if(!isDodging) {
            if(getAnimation().getTimesPlayed() > 0) {
                setAnimation(walkRegion, 1/10f);
            }
        } else {
            setAnimation(dodge, 1/1000f);
        }
    }

    public void setIsDodging(boolean isDodging) {
        this.isDodging = isDodging;
    }
    public boolean getIsDodging() {
        return this.isDodging;
    }
}
