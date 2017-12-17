package com.platformer.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.platformer.Game;

public class Player extends B2DSprite {
    TextureRegion[] walkRegion;
    TextureRegion[] idle;

    public Player(Body body) {
        super(body);
        Texture texture = Game.resources.getTexture("penguin");
        walkRegion = TextureRegion.split(texture, 32, 32)[1];
//        idle = TextureRegion.split(texture, 32,32)[0];
        setAnimation(walkRegion, 1/12f);

    }

    public void setTextureRegion(String key) {
        if(key == "walking") {
            setAnimation(walkRegion, 1/12f);
            setAnimation(walkRegion, 1/12f);
        }
    }


}
