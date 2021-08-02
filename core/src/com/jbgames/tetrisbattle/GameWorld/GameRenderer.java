package com.jbgames.tetrisbattle.GameWorld;

import Tools.Point;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.jbgames.tetrisbattle.Controllers.Player;
import com.jbgames.tetrisbattle.Controllers.PlayerSettings;
import com.jbgames.tetrisbattle.Entities.Block;
import com.jbgames.tetrisbattle.Entities.BlockTypes;
import com.jbgames.tetrisbattle.Scenes.Hud;
import com.jbgames.tetrisbattle.TetrisBattle;

import java.util.Queue;

public class GameRenderer {

    private final GameWorld world;
    private final OrthographicCamera cam;
    private final ShapeRenderer shapeRenderer;
    private final SpriteBatch batch;
    private Hud hud;
    private Player player1, player2;

    public GameRenderer(GameWorld world) {
        this.world = world;
        cam = new OrthographicCamera();
        cam.setToOrtho(false, TetrisBattle.V_WIDTH, TetrisBattle.V_HEIGHT);
        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setProjectionMatrix(cam.combined);
        batch = new SpriteBatch();
        batch.setProjectionMatrix(cam.combined);
        hud = new Hud();
        initGameObjects();

    }

    private void initGameObjects() {
        player1 = world.getPlayer(1);
        player2 = world.getPlayer(2);
    }

    public void render() {
        GameWorld.GameState state = world.getCurrentState();
        switch (state) {
            case READY:
                renderReady();
                break;
            case COUNTDOWN:
                renderRunning(state);
                break;
            case RUNNING:
                renderRunning(state);
                break;
        }
    }

    public void renderReady() {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(55 / 255.0f, 80 / 255.0f, 100 / 255.0f, 1);
        shapeRenderer.rect(0, 0, TetrisBattle.V_WIDTH, TetrisBattle.V_HEIGHT);
        shapeRenderer.end();

        batch.begin();
        hud.draw("Press any key to start!", Hud.TextType.CENTER_SCREEN, -500, 0, batch);
        batch.end();

    }

    public void renderRunning(GameWorld.GameState state) {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(55 / 255.0f, 80 / 255.0f, 100 / 255.0f, 1);
        shapeRenderer.rect(0, 0, TetrisBattle.V_WIDTH, TetrisBattle.V_HEIGHT);

        renderAreas(player1);
        renderAreas(player2);


        renderBlockQueue(player1);
        renderBlockQueue(player2);

        renderHoldBlock(player1);
        renderHoldBlock(player2);

        for (int i = 0; i < 21; i++) {
            for (int j = 0; j < 10; j++) {
                renderPlacedBlocks(i, j, player1);
                renderPlacedBlocks(i, j, player2);
            }
        }

        renderBlock(player1);
        renderBlock(player2);
        shapeRenderer.end();


        batch.begin();
        if (state == GameWorld.GameState.COUNTDOWN) {
            hud.draw(world.getCountDown(), Hud.TextType.CENTER_SCREEN, batch);
        }
        hud.draw(Integer.toString(player1.getScore()), Hud.TextType.PLAYER1, batch);
        hud.draw(Integer.toString(player2.getScore()), Hud.TextType.PLAYER2, batch);

        batch.end();




    }

    private void renderHoldBlock(Player player) {
        Point area = PlayerSettings.getSettings(player.getId()).getHoldArea();
        Block holdBlock = player.getHoldBlock();
        for (Point block : holdBlock.getBlocks()) {
            Color color = holdBlock.getColor();
            blockRenderer(area.x + block.x + (holdBlock.getType() != BlockTypes.I ? GameWorld.BLOCK_SIZE / 2 : 0), area.y + block.y, color);
        }

    }

    private void renderBlockQueue(Player player) {
        Point area = PlayerSettings.getSettings(player.getId()).getNextBlockArea();
        Queue<Block> blockQueue = player.getBlockQueue();
        int offset = 0;
        for (Block block : blockQueue) {
            for (Point blockPoint : block.getBlocks()) {
                Color color = block.getColor();
                blockRenderer(blockPoint.x + area.x, blockPoint.y + area.y - offset, color);
            }
            offset += GameWorld.BLOCK_SIZE * 3 + 10;
        }
    }

    private void renderAreas(Player player) {
        //Main area
        Point mainArea = PlayerSettings.getSettings(player.getId()).getOffset();
        shapeRenderer.setColor(150 / 255.0f, 150 / 255.0f, 150 / 255.0f, 1);
        shapeRenderer.rect(mainArea.x, mainArea.y, GameWorld.BLOCK_SIZE * 10, GameWorld.BLOCK_SIZE * 21);
        //Next block area
        Point nextBlockArea = PlayerSettings.getSettings(player.getId()).getNextBlockArea();
        shapeRenderer.setColor(150 / 255.0f, 150 / 255.0f, 150 / 255.0f, 1);
        shapeRenderer.rect(nextBlockArea.x, nextBlockArea.y, GameWorld.BLOCK_SIZE * 5, GameWorld.BLOCK_SIZE * 15);

        //Hold block area
        Point holdBlockArea = PlayerSettings.getSettings(player.getId()).getHoldArea();
        shapeRenderer.setColor(150 / 255.0f, 150 / 255.0f, 150 / 255.0f, 1);
        shapeRenderer.rect(holdBlockArea.x, holdBlockArea.y, GameWorld.BLOCK_SIZE * 4, GameWorld.BLOCK_SIZE * 4);


        //GridOverlay
        shapeRenderer.end();
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.BLACK);
        for (int i = 0; i < 21; i++) {
            Point pos = PlayerSettings.getSettings(player.getId()).getOffset();
            shapeRenderer.line(pos.x, pos.y + GameWorld.BLOCK_SIZE * i, pos.x + GameWorld.BLOCK_SIZE * 10, pos.y + GameWorld.BLOCK_SIZE * i);
        }
        for (int i = 0; i < 10; i++) {
            Point pos = PlayerSettings.getSettings(player.getId()).getOffset();
            shapeRenderer.line(pos.x + GameWorld.BLOCK_SIZE * i, pos.y, pos.x + GameWorld.BLOCK_SIZE * i, pos.y + GameWorld.BLOCK_SIZE * 21);
        }
        shapeRenderer.end();
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
    }

    private void renderBlock(Player player) {
        Block playerBlock = player.getActiveBlock();
        Color color = playerBlock.getColor();
        for (Point block : playerBlock.getBlocks()) {
            blockRenderer(block.x, block.y, color);
        }
    }

    private void blockRenderer(int x, int y, Color color) {
        shapeRenderer.setColor(Color.BLACK);
        shapeRenderer.rect(x, y, GameWorld.BLOCK_SIZE, GameWorld.BLOCK_SIZE);
        shapeRenderer.setColor(color);
        shapeRenderer.rect(x+2, y+2, GameWorld.BLOCK_SIZE-4, GameWorld.BLOCK_SIZE-4);

    }

    private void renderPlacedBlocks(int i, int j, Player player) {
        BlockTypes[][] playerGrid = player.getPlayerGrid();
        if (playerGrid[i][j] != BlockTypes.NONE) {
            Color color = playerGrid[i][j].getColor();
            Point pos = new Point(j, i, player.getId(), Point.Convert.GRID_TO_COORD);
            blockRenderer(pos.x, pos.y, color);
        }
    }
}
