package com.jbgames.tetrisbattle.Controllers;

import Tools.Point;
import com.jbgames.tetrisbattle.Entities.Block;
import com.jbgames.tetrisbattle.Entities.BlockTypes;
import com.jbgames.tetrisbattle.GameWorld.GameWorld;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;


public class Player {

    private final int id;
    private final GameWorld world;
    private final BlockTypes[][] playerGrid;
    private final Block activeBlock;
    private final Point[][] gridPos;
    private final int TIMER = 40;
    private int timer = TIMER;
    private final Queue<Block> blockQueue;
    private boolean downPressed;
    private int downTimer;
    private final int DOWNTIMER = 5;
    private Block holdBlock;
    private int score;

    public Player(int id, GameWorld world) {
        this.id = id;
        this.playerGrid = new BlockTypes[21][10];
        this.gridPos = new Point[21][10];
        this.world = world;
        score = 0;
        downPressed = false;
        downTimer = DOWNTIMER;
        blockQueue = new LinkedList<>();
        holdBlock = new Block(BlockTypes.NONE, this);

        for (int i = 0; i < 4; i++) {
            blockQueue.add(new Block(world.getNewBlock(), this, new Point(30, 400)));
        }

        for (int i = 0; i < 21; i++) {
            for (int j = 0; j < 10; j++) {
                playerGrid[i][j] = BlockTypes.NONE;
                gridPos[i][j] = new Point(PlayerSettings.getSettings(id).getGridPosOffset().x - j * GameWorld.BLOCK_SIZE,
                        i * GameWorld.BLOCK_SIZE + PlayerSettings.getSettings(id).getGridPosOffset().y);
            }
        }
        activeBlock = new Block(world.getNewBlock(), this, gridPos[0][5]);
    }

    public void update(float delta) {
        fallDown(delta);
        if(downPressed) {
            move(BlockTypes.Direction.DOWN);
            downTimer -= 1*delta;
        }
        checkForFullLines(true);
    }


    public void moveDown() {
        downPressed = !downPressed;
        if(!downPressed) {
            downTimer = DOWNTIMER;
        }
    }

    public void rotate(BlockTypes.Direction direction) {
        activeBlock.rotate(direction);
    }

    public void holdBlock() {
        if(activeBlock.isToBePlaced()) return;
        if(holdBlock.getType() == BlockTypes.NONE) {
            holdBlock = activeBlock.copy();
            activeBlock.nextBlock(nextBlock());
        }
        else {
            Block temp = holdBlock;
            holdBlock = activeBlock.copy();
            activeBlock.setNewBlock(temp.getType());
            for(Point pos : activeBlock.getBlocks()) {
                Point p = pos.coordToGridConvert();
                if(p.x > 9) activeBlock.move(BlockTypes.Direction.LEFT);
                if(p.x < 0) activeBlock.move(BlockTypes.Direction.RIGHT);
            }
            if(collision(activeBlock)) {
                temp = activeBlock.copy();
                activeBlock.setNewBlock(holdBlock.getType());
                holdBlock = temp;
            }
        }
    }

    public void move(BlockTypes.Direction direction) {
        switch (direction) {
            case LEFT:
                if (canMoveBlock(activeBlock, -1))
                    activeBlock.move(BlockTypes.Direction.LEFT);
                break;
            case RIGHT:
                if (canMoveBlock(activeBlock, 1))
                    activeBlock.move(BlockTypes.Direction.RIGHT);
                break;
            case DOWN:
                if (canMoveBlock(activeBlock,0)) {
                    if(downTimer <= 0) {
                        activeBlock.move(BlockTypes.Direction.DOWN);
                        downTimer = DOWNTIMER;
                    }
                }
                else activeBlock.setToBePlaced(true);
                break;
        }
    }

    public void placeBlockInstant() {
        while (canMoveBlock(activeBlock, 0)) {
            activeBlock.move(BlockTypes.Direction.DOWN);
        }
        placeBlock(activeBlock, playerGrid);
        activeBlock.nextBlock(nextBlock());
    }

    private void fallDown(float delta) {
            if (!activeBlock.isToBePlaced() || !collision(activeBlock)) {
                if(canMoveBlock(activeBlock, 0)) {
                    if(timer <= 0) {
                        activeBlock.move(BlockTypes.Direction.DOWN);
                        timer = TIMER;
                    }
                    activeBlock.setToBePlaced(false);
                }

            } else {
                if(timer <= 0) {
                    placeBlock(activeBlock, playerGrid);
                    activeBlock.setToBePlaced(true);
                    activeBlock.nextBlock(nextBlock());
                    timer = TIMER;
                }
            }
            if (collision(activeBlock)) {
                activeBlock.setToBePlaced(true);
            }
        timer -= 1 * delta;
    }

    private BlockTypes nextBlock() {
        Block nextBlock = blockQueue.poll();
        blockQueue.add(new Block(world.getNewBlock(), this, new Point(30, 400)));
        return nextBlock.getType();
    }

    public void placeBlock(Block block, BlockTypes[][] playerGrid) {
        for (Point pos : block.getBlocks()) {
            Point gridPos = pos.coordToGridConvert();
            playerGrid[gridPos.y][gridPos.x] = block.getType();
        }
    }



    public boolean collision(Block block) {
        for (Point pos : block.getBlocks()) {
            Point gridPos = pos.coordToGridConvert();
            if (gridPos.y - 1 < 0 || gridPos.y < 0 || gridPos.x < 0 ||gridPos.x > 9) return true;
            if (playerGrid[gridPos.y - 1][gridPos.x] != BlockTypes.NONE) return true;
        }
        return false;
    }

    public boolean canMoveBlock(Block block, int dir) {
        boolean canMove = true;
        for (Point pos : block.getBlocks()) {
            Point gridPos = pos.coordToGridConvert();
            if (dir == -1 && (gridPos.x == 0 || playerGrid[gridPos.y][gridPos.x - 1] != BlockTypes.NONE))
                canMove = false;
            if (dir == 1 && (gridPos.x == 9 || playerGrid[gridPos.y][gridPos.x + 1] != BlockTypes.NONE))
                canMove = false;
            if(dir == 0 && (gridPos.y == 0 || playerGrid[gridPos.y-1][gridPos.x] != BlockTypes.NONE))
                canMove = false;
        }
        return canMove;
    }

    private void checkForFullLines(boolean wipe) {
        List<Integer> fullLines = new ArrayList<>();
        int numberOfClearedLines = 0;
        for(int i = 0; i < 21; i++) {
            boolean full = true;
            for(int j = 0; j < 10; j++) {
                if(playerGrid[i][j] == BlockTypes.NONE) {
                    full = false;
                    break;
                }
            }
            if(full) {
                fullLines.add(i);
                numberOfClearedLines++;
            }
        }

        if(numberOfClearedLines == 0) return;

        for(int n : fullLines) {
            for(int i = 0; i < 10; i++) {
                playerGrid[n][i] = BlockTypes.PLACED_BLOCK;
            }
            if(wipe) {
                for (int i = n; i < 20; i++) {
                    for (int j = 0; j < 10; j++) {
                        playerGrid[i][j] = playerGrid[i + 1][j];
                    }
                }
            }
        }

        score += 100 * numberOfClearedLines * numberOfClearedLines;
    }


    public int getId() {
        return id;
    }

    public Block getActiveBlock() {
        return activeBlock;
    }

    public BlockTypes[][] getPlayerGrid() {
        return playerGrid;
    }

    public Queue<Block> getBlockQueue() {
        return blockQueue;
    }

    public Block getHoldBlock() {
        return holdBlock;
    }

    public int getScore() {
        return score;
    }
}
