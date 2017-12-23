package com.platformer.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.platformer.Game;
import com.platformer.entities.Background;
import com.platformer.entities.Obstacle;
import com.platformer.entities.Player;
import com.platformer.handlers.*;
import com.platformer.handlers.ContactListener;

import static com.platformer.handlers.Box2DConstants.GROUND_BIT;
import static com.platformer.handlers.Box2DConstants.PLAYER_BIT;
import static com.platformer.handlers.Box2DConstants.PPM;


//inherits batch, game, camera and hud from game state;

public class Play extends GameState {

    private BitmapFont font = new BitmapFont();
    private World world;
    private Box2DDebugRenderer box2dDebug;
    private OrthographicCamera box2dCam;
    private ContactListener cl;
    private float tileSize;

    private TiledMap tiledMap;
    private OrthogonalTiledMapRenderer tilemapRenderer;
    private Background[] bgs = new Background[3];

    private Player player;

    private Array<Obstacle> obstacles = new Array<Obstacle>();
    float obstacleTime = 0;

    public Play(GameStateManager gsm) {
        super(gsm);
        //setup box2d

        world = new World(new Vector2(0, -9.81f), true);
        cl = new ContactListener();
        world.setContactListener(cl);
        box2dDebug = new Box2DDebugRenderer();
        box2dCam = new OrthographicCamera();
        box2dCam.setToOrtho(false, Game.WIDTH / PPM, Game.HEIGHT / PPM);
        //create player and tiles
        Texture bg = Game.resources.getTexture("bg");
        TextureRegion sky = new TextureRegion(bg, 0, 0, 320, 240);
        TextureRegion clouds = new TextureRegion(bg, 0, 240, 320, 240);
        TextureRegion mountains = new TextureRegion(bg, 0, 480, 320, 240);

        bgs[0] = new Background(sky, camera, 5f);
        bgs[1] = new Background(clouds, camera, 4f);
        bgs[2] = new Background(mountains, camera, 3f);

        createTiles();
        createPlayer();
//        player.getBody().setLinearVelocity(1, 0);

    }

    @Override
    public void handleInput() {

        if (GameInput.isPressed(GameInput.JUMP)) {
            if (cl.canPlayerJump()) {
                player.getBody().applyForceToCenter(0, 200, true);
            }
        }
    }

    @Override
    public void update(float dt) {
        handleInput();
        world.step(dt, 6, 2);
        player.update(dt);
        for (int i = 0; i < bgs.length; i++) {
            bgs[i].update(dt);
        }
        createObstacle(dt);
        removeObstacles();
    }

    @Override
    public void render() {
        Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT);
        //draw bgs
        for (int i = 0; i < bgs.length; i++) {
            bgs[i].render(batch);
        }

        //draw map
        tilemapRenderer.setView(camera);
        tilemapRenderer.render();
        batch.setProjectionMatrix(camera.combined);

        player.render(batch);
        if(obstacles.size >= 0 ) {
            for(Obstacle o : obstacles) {
                o.render(batch);
            }
        }

        box2dDebug.render(world, box2dCam.combined);
    }

    @Override
    public void dispose() {

    }

    public void createPlayer() {
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(10 / PPM, 13 / PPM);
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(50 / PPM, 120 / PPM);
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        Body playerBody = world.createBody(bodyDef);

        //create player
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.filter.categoryBits = Box2DConstants.PLAYER_BIT;
        fixtureDef.filter.maskBits = Box2DConstants.GROUND_BIT;
        fixtureDef.shape = shape;
        fixtureDef.friction = 0;
        playerBody.createFixture(fixtureDef).setUserData("player");

        shape.setAsBox(2 / PPM, 2 / PPM, new Vector2(0, -13 / PPM), 0);
        fixtureDef.isSensor = true;
        fixtureDef.filter.categoryBits = Box2DConstants.PLAYER_BIT;
        fixtureDef.filter.maskBits = Box2DConstants.GROUND_BIT;
        fixtureDef.shape = shape;
        playerBody.createFixture(fixtureDef).setUserData("feet");


        shape.setAsBox(2 / PPM, 2 / PPM, new Vector2(10 / PPM, 2 / PPM), 0);
        fixtureDef.isSensor = true;
        fixtureDef.shape = shape;
        playerBody.createFixture(fixtureDef).setUserData("player_front");

        player = new Player(playerBody);

    }

    public void createTiles() {
        //load map
        tiledMap = new TmxMapLoader().load("tmx/map.tmx");
        tilemapRenderer = new OrthogonalTiledMapRenderer(tiledMap);
        TiledMapTileLayer terrainLayer = (TiledMapTileLayer) tiledMap.getLayers().get("terrain");
        createLayers(terrainLayer);

    }

    public void createLayers(TiledMapTileLayer layer) {
        tileSize = layer.getTileWidth();
        BodyDef bodyDef = new BodyDef();
        FixtureDef fixtureDef = new FixtureDef();
        //create b2d bodies for each tile.
        for (int row = 0; row < layer.getHeight(); row++) {
            for (int col = 0; col < layer.getWidth(); col++) {
                TiledMapTileLayer.Cell cell = layer.getCell(col, row);
                if (cell == null || cell.getTile() == null) continue;
                bodyDef.type = BodyDef.BodyType.StaticBody;
                bodyDef.position.set((col + 0.5f) * tileSize / PPM, (row + 0.5f) * tileSize / PPM);
                ChainShape cshape = new ChainShape();
                Vector2 verticies[] = {
                        new Vector2(-tileSize / 2 / PPM, -tileSize / 2 / PPM),
                        new Vector2(-tileSize / 2 / PPM, tileSize / 2 / PPM),
                        new Vector2(tileSize / 2 / PPM, tileSize / 2 / PPM),
                };
                cshape.createChain(verticies);
                fixtureDef.friction = 0;
                fixtureDef.shape = cshape;
                fixtureDef.filter.categoryBits = GROUND_BIT;
                fixtureDef.filter.maskBits = PLAYER_BIT;
                fixtureDef.isSensor = false;
                world.createBody(bodyDef).createFixture(fixtureDef).setUserData("ground");
            }
        }
    }

    public void createObstacle(float dt) {
        obstacleTime+=dt;
        if(obstacleTime > 1 && obstacles.size < 1) {
            PolygonShape shape = new PolygonShape();
            shape.setAsBox(10 / PPM, 13 / PPM);
            BodyDef bodyDef = new BodyDef();
            bodyDef.position.set(320 / PPM, 64 / PPM);
            bodyDef.type = BodyDef.BodyType.DynamicBody;
            Body body = world.createBody(bodyDef);
            body.setLinearVelocity(-1, 0);
            FixtureDef fixtureDef = new FixtureDef();
            fixtureDef.filter.categoryBits = -1;
            fixtureDef.filter.maskBits = -1;
            fixtureDef.shape = shape;
            fixtureDef.friction = 0;
            body.createFixture(fixtureDef).setUserData("obs");

            fixtureDef.isSensor = true;
            shape.setAsBox(11 / PPM, 13 / PPM, new Vector2(-5 / PPM, 5 / PPM), 0);
            fixtureDef.shape = shape;
            body.createFixture(fixtureDef).setUserData("obs_front");

            Obstacle obs = new Obstacle(body);
            obstacles.add(obs);
            obstacleTime=0;
        }
    }

    public void removeObstacles() {
        for(int i = 0; i < obstacles.size; i++) {
            if(obstacles.get(i).getPosition().x <= 0) {
                world.destroyBody(obstacles.get(i).getBody());
                obstacles.removeIndex(i);
                System.out.printf("yo");
            }
        }
    }
}
