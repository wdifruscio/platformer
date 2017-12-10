package com.platformer.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.platformer.Game;
import com.platformer.handlers.Box2DConstants;
import com.platformer.handlers.ContactListener;
import com.platformer.handlers.GameInput;
import com.platformer.handlers.GameStateManager;

import static com.platformer.handlers.Box2DConstants.PPM;


//inherits batch, game, camera and hud from game state;

public class Play extends GameState {

    private BitmapFont font = new BitmapFont();
    private World world;
    private Box2DDebugRenderer box2dDebug;
    private OrthographicCamera box2dCam;
    private Body playerBody;
    private ContactListener cl;

    public Play(GameStateManager gsm) {
        super(gsm);
        world=new World(new Vector2(0, -9.81f), true);
        cl = new ContactListener();
        world.setContactListener(cl);
        box2dDebug = new Box2DDebugRenderer();

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(5 / PPM, 5/ PPM);

        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(160 / PPM,120/ PPM);
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        playerBody = world.createBody(bodyDef);

        //create player
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.filter.categoryBits = Box2DConstants.PLAYER_BIT;
        fixtureDef.filter.maskBits = Box2DConstants.GROUND_BIT;
        fixtureDef.shape = shape;
        playerBody.createFixture(fixtureDef).setUserData("Player");
        //create ground
        bodyDef.position.set(25/ PPM,25 / PPM);
        bodyDef.type = BodyDef.BodyType.StaticBody;
        Body body = world.createBody(bodyDef);
        shape.setAsBox(400 / PPM,5 / PPM);
        fixtureDef.shape = shape;
        fixtureDef.restitution = 0.3f;
        fixtureDef.filter.categoryBits = Box2DConstants.GROUND_BIT;
        fixtureDef.filter.maskBits = Box2DConstants.PLAYER_BIT;
        body.createFixture(fixtureDef).setUserData("Ground");

        //ground sensor
        shape.setAsBox(2/PPM, 2/PPM, new Vector2(0, -5/PPM), 0);
        fixtureDef.isSensor = true;
        fixtureDef.filter.categoryBits = Box2DConstants.PLAYER_BIT;
        fixtureDef.filter.maskBits = Box2DConstants.GROUND_BIT;
        fixtureDef.shape = shape;
        playerBody.createFixture(fixtureDef).setUserData("feet");


        box2dCam = new OrthographicCamera();
        box2dCam.setToOrtho(false, Game.WIDTH / PPM, Game.HEIGHT / PPM);



    }

    @Override
    public void handleInput() {
        if(GameInput.isPressed(GameInput.JUMP)) {
            if(cl.canPlayerJump()) {
                playerBody.applyForceToCenter(0,200,true);
            }
        }
        if(GameInput.isPressed(GameInput.RIGHT) || GameInput.isDown(GameInput.RIGHT)) {
            playerBody.setLinearVelocity(1,  playerBody.getLinearVelocity().y);
        }

        if(GameInput.isPressed(GameInput.LEFT) || GameInput.isDown(GameInput.LEFT)) {
            playerBody.setLinearVelocity(-1,playerBody.getLinearVelocity().y);

        }

    }

    @Override
    public void update(float dt) {
        handleInput();
        world.step(dt, 6, 2);
    }

    @Override
    public void render() {

        Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT);

        box2dDebug.render(world, box2dCam.combined);
    }

    @Override
    public void dispose() {

    }


}
