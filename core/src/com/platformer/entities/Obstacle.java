package com.platformer.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.platformer.Game;

public class Obstacle extends B2DSprite {
    public Obstacle(Body body) {
        super(body);
        Texture texture = Game.resources.getTexture("penguin");
        TextureRegion[] walkRegion = TextureRegion.split(texture, 32, 32)[1];
//        idle = TextureRegion.split(texture, 32,32)[0];
        setAnimation(walkRegion, 1/10f);
    }
}
