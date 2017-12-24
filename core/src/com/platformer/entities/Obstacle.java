package com.platformer.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.platformer.Game;

public class Obstacle extends B2DSprite {
    TextureRegion[] walkRegion;
    public Obstacle(Body body, String tex) {
        super(body);
        Texture texture = Game.resources.getTexture(tex);
        walkRegion = TextureRegion.split(texture, 32, 32)[0];
        setAnimation(walkRegion, 1/15f);
    }
}
