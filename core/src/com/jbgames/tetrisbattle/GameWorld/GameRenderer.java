package com.jbgames.tetrisbattle.GameWorld;

import com.jbgames.tetrisbattle.Tools.Point;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
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
            case GAMEOVER:
                renderGameOver();
                break;
        }
    }

    private void renderReady() {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(55 / 255.0f, 80 / 255.0f, 100 / 255.0f, 1);
        shapeRenderer.rect(0, 0, TetrisBattle.V_WIDTH, TetrisBattle.V_HEIGHT);
        shapeRenderer.end();
        batch.begin();
        hud.draw("Press any key to start!", Hud.TextType.CENTER_SCREEN, -500, 0, batch, Hud.TextSize.BIG);
        batch.end();
    }

    private void renderGameOver() {
        renderRunning(GameWorld.GameState.GAMEOVER);

        batch.begin();
        hud.draw("Game Over!", Hud.TextType.CENTER_SCREEN, -500, 0, batch, Hud.TextSize.BIG);
        String winner = player1.getScore() > player2.getScore() ? "Player 1" : "Player 2";
        hud.draw(winner + " won with a score of " + Math.max(player1.getScore(), player2.getScore()), Hud.TextType.CENTER_SCREEN, -500, 100, batch, Hud.TextSize.BIG);
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

        if(world.getCurrentState() == GameWorld.GameState.RUNNING) {
            shapeRenderer.end();
            Gdx.gl.glEnable(GL20.GL_BLEND);
            Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            renderGhost(player1);
            renderGhost(player2);
        }

        renderBlock(player1);
        renderBlock(player2);

        shapeRenderer.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);

        batch.begin();

        hud.draw(Integer.toString(player1.getScore()), Hud.TextType.PLAYER1_SCORE, batch, Hud.TextSize.BIG);
        hud.draw(Integer.toString(player2.getScore()), Hud.TextType.PLAYER2_SCORE, batch, Hud.TextSize.BIG);
        hud.draw(player1.getPowerUp().getName(), Hud.TextType.PLAYER1_POWERUP, batch, Hud.TextSize.SMALL);
        hud.draw(player2.getPowerUp().getName(), Hud.TextType.PLAYER2_POWERUP, batch, Hud.TextSize.SMALL);

        if(player1.showPopUp())
        hud.draw(player1.getPopUp(), Hud.TextType.PLAYER1_POWERUP_POPUP, 0, player1.getPopUpAnimation(), batch, Hud.TextSize.MEDIUM);

        if(player2.showPopUp())
        hud.draw(player2.getPopUp(), Hud.TextType.PLAYER2_POWERUP_POPUP, 0, player2.getPopUpAnimation(), batch, Hud.TextSize.MEDIUM);

        if (state == GameWorld.GameState.COUNTDOWN) {
            hud.draw(world.getCountDown(), Hud.TextType.CENTER_SCREEN, batch, Hud.TextSize.BIG);
        }
        batch.end();
    }


    private void renderHoldBlock(Player player) {
        Point area = PlayerSettings.getSettings(player.getId()).getHoldArea();
        Block holdBlock = player.getHoldBlock();
        for (Point block : holdBlock.getBlocks()) {
            Color color = holdBlock.getColor();
            int offset = 0;
            if (holdBlock.getType() != BlockTypes.I && holdBlock.getType() != BlockTypes.SQUARE) {
                offset = GameWorld.BLOCK_SIZE / 2;
            }
            blockRenderer(area.x + block.x + offset, area.y + block.y, color, 1f);
        }
    }

    private void renderBlockQueue(Player player) {
        Point area = PlayerSettings.getSettings(player.getId()).getNextBlockArea();
        Queue<Block> blockQueue = player.getBlockQueue();
        int offset = 0;
        for (Block block : blockQueue) {
            for (Point blockPoint : block.getBlocks()) {
                Color color = block.getColor();
                blockRenderer(blockPoint.x + area.x, blockPoint.y + area.y - offset, color, 1f);
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

        //Power up area
        Point powerUpArea = PlayerSettings.getSettings(player.getId()).getPowerUpArea();
        shapeRenderer.setColor(150 / 255.0f, 150 / 255.0f, 150 / 255.0f, 1);
        shapeRenderer.rect(powerUpArea.x, powerUpArea.y, GameWorld.BLOCK_SIZE * 4, GameWorld.BLOCK_SIZE * 4);

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
            blockRenderer(block.x, block.y, color, 1f);
        }
    }

    private void renderGhost(Player player) {
        Block playerBlock = player.getGhostBlock();
        Color color = playerBlock.getColor();

        for (Point block : playerBlock.getBlocks()) {
            blockRenderer(block.x, block.y, color, 0.4f);
        }
    }

    private void blockRenderer(int x, int y, Color color, float alpha) {
        shapeRenderer.setColor(0, 0, 0, alpha);
        shapeRenderer.rect(x, y, GameWorld.BLOCK_SIZE, GameWorld.BLOCK_SIZE);
        shapeRenderer.setColor(color.r, color.g, color.b, alpha);
        shapeRenderer.rect(x + 2, y + 2, GameWorld.BLOCK_SIZE - 4, GameWorld.BLOCK_SIZE - 4);
    }

    private void renderPlacedBlocks(int i, int j, Player player) {
        BlockTypes[][] playerGrid = player.getPlayerGrid();
        if (playerGrid[i][j] != BlockTypes.NONE) {
            Color color = playerGrid[i][j].getColor();
            Point pos = new Point(j, i, player.getId(), Point.Convert.GRID_TO_COORD);
            blockRenderer(pos.x, pos.y, color, 1f);
        }
    }
}
