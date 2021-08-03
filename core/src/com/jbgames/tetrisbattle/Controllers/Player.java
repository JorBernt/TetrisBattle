package com.jbgames.tetrisbattle.Controllers;

import com.jbgames.tetrisbattle.Entities.PowerUp;
import com.jbgames.tetrisbattle.Tools.Point;
import com.jbgames.tetrisbattle.Entities.Block;
import com.jbgames.tetrisbattle.Entities.BlockTypes;
import com.jbgames.tetrisbattle.GameWorld.GameWorld;

import java.util.*;


public class Player {

    private final int id;
    private final GameWorld world;
    private final BlockTypes[][] playerGrid;
    private Block activeBlock;
    private final Point[][] gridPos;

    private Queue<Block> blockQueue;
    private Block holdBlock, ghostBlock;
    private int score;

    private final float FALLDOWN_TIMER = 1f;
    private float fallDownSpeedUp;
    private float gameTimeCounter;
    private float fallDownTimer = FALLDOWN_TIMER;

    private final float MOVE_STEP_TIMER = 0.1f;
    private float moveStepTimer = 0;

    private final float POPUP_TIMER = 2f;
    private float popUpTimer = POPUP_TIMER;
    private boolean showPopUp;
    private float popUpAnimation = 0f;

    private final PlayerController playerController;

    private PowerUp.Item powerUp;
    private String popUp;

    public Player(int id, GameWorld world) {
        this.id = id;
        this.playerGrid = new BlockTypes[21][10];
        this.gridPos = new Point[21][10];
        this.world = world;
        init();
        playerController = new PlayerController(PlayerSettings.getSettings(id), this);
    }

    private void init() {
        blockQueue = new LinkedList<>();
        for (int i = 0; i < 21; i++) {
            for (int j = 0; j < 10; j++) {
                playerGrid[i][j] = BlockTypes.NONE;
                gridPos[i][j] = new Point(PlayerSettings.getSettings(id).getGridPosOffset().x - j * GameWorld.BLOCK_SIZE,
                        i * GameWorld.BLOCK_SIZE + PlayerSettings.getSettings(id).getGridPosOffset().y);
            }
        }
        for (int i = 0; i < 4; i++) {
            blockQueue.add(new Block(world.getNewBlock(), this, new Point(30, 400)));
        }
        activeBlock = new Block(world.getNewBlock(), this, gridPos[0][5]);
        ghostBlock = new Block(activeBlock.getType(), this, true);
        score = 0;
        gameTimeCounter = 0;
        fallDownSpeedUp = 0.1f;
        holdBlock = new Block(BlockTypes.NONE, this);
        powerUp = PowerUp.Item.INSTANT_FALL;
        popUp = "";
        showPopUp = false;
    }

    public void update(float delta) {
        updateGhost();
        fallDown(delta);
        gameTimeCounter += delta;
        if(gameTimeCounter > 20) {
            fallDownSpeedUp += 0.1f;
            fallDownSpeedUp = Math.min(0.8f, fallDownSpeedUp);
            gameTimeCounter = 0f;
            popUp = "Speed up!";
            showPopUp = true;
        }

        if(showPopUp) {
            if(popUpTimer <= 0) {
                popUpTimer = POPUP_TIMER;
                showPopUp = false;
                popUpAnimation = 0f;
            }
            popUpAnimation += 20*delta;
            popUpTimer -= delta;
        }

        //Controls
        if(playerController.isDownPressed()) {
            move(BlockTypes.Direction.DOWN, delta);
        }
        else if(playerController.isLeftPressed()) {
            move(BlockTypes.Direction.LEFT, delta);
        }
        else if(playerController.isRightPressed()) {
            move(BlockTypes.Direction.RIGHT, delta);
        }
        else moveStepTimer = 0f;

        checkForFullLines(true);
    }

    private void updateGhost() {
        ghostBlock.setNewBlock(activeBlock.getType());
        ghostBlock.setPosition(activeBlock.getPosition());
        ghostBlock.setBlockLayout(activeBlock.getBlockLayout());
        while(canMoveBlock(ghostBlock, BlockTypes.Direction.DOWN)) {
            ghostBlock.move(BlockTypes.Direction.DOWN);
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

    public void move(BlockTypes.Direction direction, float delta) {
        if(moveStepTimer <= 0) {
            switch (direction) {
                case LEFT:
                    if (canMoveBlock(activeBlock, BlockTypes.Direction.LEFT))
                        activeBlock.move(BlockTypes.Direction.LEFT);
                    break;
                case RIGHT:
                    if (canMoveBlock(activeBlock, BlockTypes.Direction.RIGHT))
                        activeBlock.move(BlockTypes.Direction.RIGHT);
                    break;
                case DOWN:
                    if (canMoveBlock(activeBlock, BlockTypes.Direction.DOWN)) {
                        activeBlock.move(BlockTypes.Direction.DOWN);
                    } else activeBlock.setToBePlaced(true);
                    break;
            }
            moveStepTimer = MOVE_STEP_TIMER;
        }
        moveStepTimer -= delta;
    }

    public void placeBlockInstant() {
        while (canMoveBlock(activeBlock, BlockTypes.Direction.DOWN)) {
            activeBlock.move(BlockTypes.Direction.DOWN);
        }
        placeBlock(activeBlock, playerGrid);
        activeBlock.nextBlock(nextBlock());

    }

    private void fallDown(float delta) {
            if (!activeBlock.isToBePlaced() || !collision(activeBlock)) {
                if(canMoveBlock(activeBlock, BlockTypes.Direction.DOWN)) {
                    if(fallDownTimer <= 0) {
                        activeBlock.move(BlockTypes.Direction.DOWN);
                        fallDownTimer = FALLDOWN_TIMER-fallDownSpeedUp;
                    }
                    activeBlock.setToBePlaced(false);
                }
            } else {
                if(fallDownTimer <= 0) {
                    placeBlock(activeBlock, playerGrid);
                    activeBlock.setToBePlaced(true);
                    activeBlock.nextBlock(nextBlock());
                    fallDownTimer = FALLDOWN_TIMER-fallDownSpeedUp;
                    if(collision(activeBlock)) {
                        world.setCurrentState(GameWorld.GameState.GAMEOVER);
                    }
                }
            }
            if (collision(activeBlock)) {
                activeBlock.setToBePlaced(true);
            }
        fallDownTimer -= delta;
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

    public void useItem() {
        if(powerUp != PowerUp.Item.NONE) {
            PowerUp.useItem(this, world.getPlayer(id==1?2:1), powerUp);
            powerUp = PowerUp.Item.NONE;
        }
    }

    public boolean collision(Block block) {
        for (Point pos : block.getBlocks()) {
            Point gridPos = pos.coordToGridConvert();
            if (gridPos.y - 1 < 0 || gridPos.y < 0 || gridPos.x < 0 ||gridPos.x > 9) return true;
            if (playerGrid[gridPos.y - 1][gridPos.x] != BlockTypes.NONE) return true;
            if (playerGrid[gridPos.y][gridPos.x] != BlockTypes.NONE) return true;
        }
        return false;
    }

    public boolean canMoveBlock(Block block, BlockTypes.Direction direction) {
        boolean canMove = true;
        for (Point pos : block.getBlocks()) {
            Point gridPos = pos.coordToGridConvert();
            if (direction == BlockTypes.Direction.LEFT && (gridPos.x == 0 || playerGrid[gridPos.y][gridPos.x - 1] != BlockTypes.NONE))
                canMove = false;
            if (direction == BlockTypes.Direction.RIGHT && (gridPos.x == 9 || playerGrid[gridPos.y][gridPos.x + 1] != BlockTypes.NONE))
                canMove = false;
            if(direction == BlockTypes.Direction.DOWN && (gridPos.y == 0 || playerGrid[gridPos.y-1][gridPos.x] != BlockTypes.NONE))
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
        updateScore(numberOfClearedLines);
        addPowerUp(numberOfClearedLines);
    }

    public void attacked(PowerUp.Item item) {
        popUp = "Opponent used " + item.getName() + "!";
        showPopUp = true;
        switch (item) {
            case INSTANT_FALL:
                placeBlockInstant();
                break;
        }
    }

    private void addPowerUp(int numberOfClearedLines) {
        powerUp = PowerUp.Item.INSTANT_FALL;
        popUp = "You got " + powerUp.getName() + "!";
        showPopUp = true;
    }

    private void updateScore(int numberOfClearedLines) {
        score += 100 * numberOfClearedLines * numberOfClearedLines;
    }

    public void reset() {
        score = 0;
        init();
    }

    public int getId() {
        return id;
    }

    public Block getActiveBlock() {
        return activeBlock;
    }

    public Block getGhostBlock() { return ghostBlock; }

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

    public PlayerController getPlayerController() {
        return playerController;
    }

    public PowerUp.Item getPowerUp() {
        return powerUp;
    }

    public String getPopUp() {
        return popUp;
    }

    public boolean showPopUp() {
        return showPopUp;
    }

    public float getPopUpAnimation() {
        return popUpAnimation;
    }
}
