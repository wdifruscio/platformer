package com.platformer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import org.w3c.dom.css.Rect;

import java.util.Iterator;

public class GameScreen extends ScreenAdapter {
    private ShapeRenderer shapeRenderer;
    private Viewport viewport;
    private OrthographicCamera camera;
    private SpriteBatch batch;
    private final Platformer platfomer;
    private TiledMap tiledMap;
    private OrthogonalTiledMapRenderer orthogonalTiledMapRenderer;
    private Player player;
    private Texture playerTexture;
    private Array<Collectable> mushrooms = new Array<Collectable>();

    private final int CELL_SIZE = 16;

    public GameScreen(Platformer platfomer) {
        this.platfomer = platfomer;
    }
    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    @Override
    public void show() {
        camera = new OrthographicCamera();
        camera.update();
        viewport = new FitViewport(Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT, camera);

        viewport.apply(true);
        shapeRenderer = new ShapeRenderer();
        batch = new SpriteBatch();
        tiledMap = platfomer.getAssetManager().get("platformer.tmx");

        orthogonalTiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap, batch);
        orthogonalTiledMapRenderer.setView(camera);
        playerTexture = platfomer.getAssetManager().get("textureatlas.png");
        TextureAtlas textureAtlas = platfomer.getAssetManager().get("textureatlas.txt");
        player = new Player(playerTexture, textureAtlas);
//        populateCollectables();

    }

    @Override
    public void render(float delta) {
        update(delta);
        clearScreen();
        draw();
//        drawDebug();
    }

    private void update(float delta) {
        checkBoundaries();
        handleCollisions();
        player.update(delta);
        updateCameraX();
    }

    private void clearScreen() {
        Gdx.gl.glClearColor(Color.BLACK.r, Color.BLACK.g, Color.BLACK.b, Color.BLACK.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    private void draw() {
        batch.setProjectionMatrix(camera.projection);
        batch.setTransformMatrix(camera.view);
        orthogonalTiledMapRenderer.render();
        batch.begin();
        player.draw(batch);
        for (Collectable collectable : mushrooms) {
            collectable.draw(batch);
        }
        batch.end();
    }

    private void drawDebug() {
        shapeRenderer.setProjectionMatrix(camera.projection);
        shapeRenderer.setTransformMatrix(camera.view);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        player.drawDebug(shapeRenderer);
        shapeRenderer.end();
    }

    private void checkBoundaries() {
        if(player.getX() < 0) {
            player.setPosition(0, player.getY());
            player.landed();
        }
        TiledMapTileLayer tiledMapTileLayer = (TiledMapTileLayer) tiledMap.getLayers().get(0);
        float levelWidth =  tiledMapTileLayer.getWidth() * tiledMapTileLayer.getTileWidth();
        if (player.getX() + player.WIDTH > levelWidth) {
            player.setPosition(levelWidth - player.WIDTH, player.getY());
        }
    }

    private void handleCollisions() {
        Array<CollisionCell> playerCells = whichCellDoesPlayerCover();
        playerCells = filterOutNonTiledCells(playerCells);

        for(CollisionCell cell : playerCells) {
            float cellLevelX = cell.cellX * CELL_SIZE;
            float cellLevelY = cell.cellY * CELL_SIZE;
            Rectangle intersection = new Rectangle();
            Intersector.intersectRectangles(player.getCollisionRectangle(), new Rectangle(cellLevelX, cellLevelY, CELL_SIZE, CELL_SIZE), intersection);

            if (intersection.getHeight() < intersection.getWidth()) {
                player.setPosition(player.getX(), intersection.getY() + intersection.getHeight());
                player.landed();
            } else if (intersection.getWidth() < intersection.getHeight()) {
                if (intersection.getX() == player.getX()) {
                    player.setPosition(intersection.getX() + intersection.getWidth(), player.getY());
                }
                if (intersection.getX() > player.getX()) {
                    player.setPosition(intersection.getX() - player.WIDTH, player.getY());
                }
            }
        }
    }

    private Array<CollisionCell> whichCellDoesPlayerCover() {
        float x = player.getX();
        float y = player.getY();
        Array<CollisionCell> cellsCovered = new Array<CollisionCell>();
        float cellX = x / CELL_SIZE;
        float cellY = y / CELL_SIZE;

        int bottomLeftCellX = MathUtils.floor(cellX);
        int bottomLeftCellY = MathUtils.floor(cellY);

        TiledMapTileLayer tiledMapTileLayer = (TiledMapTileLayer) tiledMap.getLayers().get(1);

        cellsCovered.add(new CollisionCell(tiledMapTileLayer.getCell(bottomLeftCellX, bottomLeftCellY), bottomLeftCellX, bottomLeftCellY));

        if (cellX % 1 != 0 && cellY % 1 != 0) {
            int topRightCellX = bottomLeftCellX + 1;
            int topRightCellY = bottomLeftCellY + 1;
            cellsCovered.add(new CollisionCell(tiledMapTileLayer.getCell(topRightCellX, topRightCellY), topRightCellX, topRightCellY));
        }

        if (cellX % 1 != 0) {
            int bottomRightCellX = bottomLeftCellX + 1;
            int bottomRightCellY = bottomLeftCellY;
            cellsCovered.add(new CollisionCell(tiledMapTileLayer.getCell(bottomRightCellX, bottomRightCellY), bottomRightCellX, bottomRightCellY));
        }

        if (cellY % 1 != 0) {
            int topLeftCellX = bottomLeftCellX;
            int topLeftCellY = bottomLeftCellY + 1;
            cellsCovered.add(new CollisionCell(tiledMapTileLayer.getCell(topLeftCellX, topLeftCellY), topLeftCellX, topLeftCellY));
        }

        return cellsCovered;
    }

    private Array<CollisionCell> filterOutNonTiledCells(Array<CollisionCell> cells) {
        for (Iterator<CollisionCell> iter = cells.iterator(); iter.hasNext(); ) {
            CollisionCell collisionCell = iter.next();
            if (collisionCell.isEmpty()) {
                iter.remove();
            }
        }
        return cells;
    }

//    private void populateCollectables() {
//        MapLayer mapLayer = tiledMap.getLayers().get("Collectables");
//        for (MapObject mapObject : mapLayer.getObjects()) {
//            mushrooms.add(
//                    new Collectable(platfomer.getAssetManager().get("kenney_16x16.png", Texture.class),
//                            mapObject.getProperties().get("x", Integer.class),
//                            mapObject.getProperties().get("y", Integer.class)
//                    )
//            );
//        }
//    }


    private class CollisionCell {
        private final TiledMapTileLayer.Cell cell;
        private final int cellX;
        private final int cellY;

        public CollisionCell(TiledMapTileLayer.Cell cell, int cellX, int cellY) {
            this.cell = cell;
            this.cellX = cellX;
            this.cellY = cellY;
        }

        public boolean isEmpty() {
            return cell == null;
        }
    }
    private void updateCameraX() {
        TiledMapTileLayer tiledMapTileLayer = (TiledMapTileLayer) tiledMap.getLayers().get(0);
        float levelWidth = tiledMapTileLayer.getWidth() * tiledMapTileLayer.getTileWidth();
        if ( (player.getX() > Constants.WORLD_WIDTH / 2f) && (player.getX() < (levelWidth - Constants.WORLD_WIDTH / 2f)) ) {
            camera.position.set(player.getX(), camera.position.y, camera.position.z);
            camera.update();
            orthogonalTiledMapRenderer.setView(camera);
        }
    }
}
