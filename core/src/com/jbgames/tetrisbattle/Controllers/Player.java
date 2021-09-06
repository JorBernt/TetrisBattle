package com.jbgames.tetrisbattle.Controllers;

import com.jbgames.tetrisbattle.Entities.Block;
import com.jbgames.tetrisbattle.Entities.BlockTypes;
import com.jbgames.tetrisbattle.Entities.PowerUp;
import com.jbgames.tetrisbattle.GameWorld.GameWorld;
import com.jbgames.tetrisbattle.Tools.Point;

import java.util.*;


public class Player {

    private final int id;
    private final GameWorld world;
    private final BlockTypes[][] playerGrid;
    private Block activeBlock;
    private final Point[][] gridPos;

    private Deque<Block> blockQueue;
    private Queue<BlockTypes> blockHistory;
    private Block holdBlock, ghostBlock;
    private int score;
    private final Random random;

    private final float FALLDOWN_TIMER = 1f;
    private float fallDownSpeedUp;
    private float gameTimeCounter;
    private float fallDownTimer = FALLDOWN_TIMER;
    private final float FALLDOWN_MAX_SPEED_TIMER = 0.2f;

    private float moveStepTimer = 0;

    private final float POPUP_TIMER = 2f;
    private float popUpTimer = POPUP_TIMER;
    private boolean showPopUp;
    private float popUpAnimation = 0f;

    private float itemDuration;
    private float maxItemDuration;
    private boolean itemActive;
    private PowerUp.Item item;

    private boolean itemAffectGameScreen;

    private final PlayerController playerController;

    private PowerUp.Item powerUp;
    private String popUp;

    public Player(int id, GameWorld world) {
        this.id = id;
        this.playerGrid = new BlockTypes[21][10];
        this.gridPos = new Point[21][10];
        this.world = world;
        random = new Random();
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

        blockHistory = new LinkedList<>();
        blockHistory.add(BlockTypes.SQUIGGLY);
        blockHistory.add(BlockTypes.SQUIGGLY);
        blockHistory.add(BlockTypes.REVERSE_SQUIGGLY);
        blockHistory.add(BlockTypes.REVERSE_SQUIGGLY);

        activeBlock = new Block(world.getNewBlock(), this, gridPos[0][4]);
        ghostBlock = new Block(activeBlock.getType(), this);
        for (int i = 0; i < 4; i++) {
            Block block = new Block(world.getNewBlock(blockHistory), this, new Point(30, 400));
            blockQueue.add(block);
            blockHistory.poll();
            blockHistory.add(block.getType());
        }
        score = 0;
        gameTimeCounter = 0;
        fallDownSpeedUp = 0.1f;
        holdBlock = new Block(BlockTypes.NONE, this);
        powerUp = PowerUp.Item.NONE;
        popUp = "";
        showPopUp = false;
        itemActive = false;
        itemAffectGameScreen = false;
    }

    public void update(float delta) {
        updateGhost();
        fallDown(delta);
        gameTimeCounter += delta;
        if (gameTimeCounter > 20) {
            fallDownSpeedUp += 0.1f;
            fallDownSpeedUp = Math.min(0.8f, fallDownSpeedUp);
            gameTimeCounter = 0f;
            if (showPopUp) popUp += " Speed up!";
            else popUp = "Speed up!";
            showPopUp = true;
            popUpTimer = POPUP_TIMER;
        }

        if (showPopUp) {
            if (popUpTimer <= 0) {
                popUpTimer = POPUP_TIMER;
                showPopUp = false;
                popUpAnimation = 0f;
            }
            popUpAnimation += 20 * delta;
            popUpTimer -= delta;
        }

        if (itemActive) {
            if (itemDuration <= 0) {
                itemActive = false;
                item = PowerUp.Item.NONE;
                itemAffectGameScreen = false;
            }
            itemDuration -= delta;
        }

        //Controls
        if (playerController.isDownPressed()) {
            move(BlockTypes.Direction.DOWN, delta);
        } else if (playerController.isLeftPressed()) {
            move(BlockTypes.Direction.LEFT, delta);
        } else if (playerController.isRightPressed()) {
            move(BlockTypes.Direction.RIGHT, delta);
        } else moveStepTimer = 0f;

        checkForFullLines(true);
    }

    private void updateGhost() {
        ghostBlock.setNewBlock(activeBlock.getType());
        ghostBlock.setPosition(activeBlock.getPosition());
        ghostBlock.setBlockLayout(activeBlock.getBlockLayout());
        while (canMoveBlock(ghostBlock, BlockTypes.Direction.DOWN)) {
            ghostBlock.move(BlockTypes.Direction.DOWN);
        }
    }


    public void rotate(BlockTypes.Direction direction) {
        if (item != PowerUp.Item.NO_ROTATION && !itemActive) {
            activeBlock.rotate(direction);
        }
    }

    public void holdBlock() {
        if (activeBlock.isToBePlaced()) return;
        if (holdBlock.getType() == BlockTypes.NONE) {
            holdBlock = activeBlock.copy();
            activeBlock.nextBlock(nextBlock());
        } else {
            Block temp = holdBlock;
            holdBlock = activeBlock.copy();
            activeBlock.setNewBlock(temp.getType());
            for (Point pos : activeBlock.getBlocks()) {
                Point p = pos.coordToGridConvert();
                if (p.x > 9) activeBlock.move(BlockTypes.Direction.LEFT);
                if (p.x < 0) activeBlock.move(BlockTypes.Direction.RIGHT);
            }
            if (collision(activeBlock)) {
                temp = activeBlock.copy();
                activeBlock.setNewBlock(holdBlock.getType());
                holdBlock = temp;
            }
        }
    }

    public void move(BlockTypes.Direction direction, float delta) {
        if (item == PowerUp.Item.MIRROR && itemActive) {
            switch (direction) {
                case LEFT:
                    direction = BlockTypes.Direction.RIGHT;
                    break;
                case RIGHT:
                    direction = BlockTypes.Direction.LEFT;
                    break;
            }
        }
        if (moveStepTimer <= 0) {
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
            moveStepTimer = 0.1f;
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
            if (canMoveBlock(activeBlock, BlockTypes.Direction.DOWN)) {
                if (fallDownTimer <= 0) {
                    activeBlock.move(BlockTypes.Direction.DOWN);
                    resetFallDownTimer();
                }
                activeBlock.setToBePlaced(false);
            }
        } else {
            if (fallDownTimer <= 0) {
                placeBlock(activeBlock, playerGrid);
                activeBlock.setToBePlaced(true);
                activeBlock.nextBlock(nextBlock());
                resetFallDownTimer();
                if (collision(activeBlock)) {
                    world.setCurrentState(GameWorld.GameState.GAMEOVER);
                }
            }
        }
        if (collision(activeBlock)) {
            activeBlock.setToBePlaced(true);
        }
        fallDownTimer -= delta;
    }

    private void resetFallDownTimer() {
        if(itemActive && item == PowerUp.Item.MAX_SPEED)
            fallDownTimer = FALLDOWN_MAX_SPEED_TIMER;
        else
            fallDownTimer = FALLDOWN_TIMER - fallDownSpeedUp;
    }

    private BlockTypes nextBlock() {
        Block nextBlock = blockQueue.poll();
        blockQueue.add(new Block(world.getNewBlock(blockHistory), this, new Point(30, 400)));
        addBlockHistory();
        return nextBlock.getType();
    }

    private void addBlockHistory() {
        blockHistory.poll();
        blockHistory.add(activeBlock.getType());
    }

    public void placeBlock(Block block, BlockTypes[][] playerGrid) {
        for (Point pos : block.getBlocks()) {
            Point gridPos = pos.coordToGridConvert();
            playerGrid[gridPos.y][gridPos.x] = block.getType();
        }
    }

    public void useItem() {
        if (powerUp != PowerUp.Item.NONE) {
            if(PowerUp.useItem(this, world.getPlayer(id == 1 ? 2 : 1), powerUp)) {
                powerUp = PowerUp.Item.NONE;
            }

        }
    }

    public boolean collision(Block block) {
        for (Point pos : block.getBlocks()) {
            Point gridPos = pos.coordToGridConvert();
            if (gridPos.y - 1 < 0 || gridPos.y < 0 || gridPos.x < 0 || gridPos.x > 9) return true;
            if (playerGrid[gridPos.y - 1][gridPos.x] != BlockTypes.NONE) return true;
            if (playerGrid[gridPos.y][gridPos.x] != BlockTypes.NONE) return true;
        }
        return false;
    }

    public boolean canMoveBlock(Block block, BlockTypes.Direction direction) {
        boolean canMove = true;
        List<Point> blocks = block.getBlocks();
        for (Point pos : blocks) {
            Point gridPos = pos.coordToGridConvert();
            if (direction == BlockTypes.Direction.LEFT && (gridPos.x == 0 || playerGrid[gridPos.y][gridPos.x - 1] != BlockTypes.NONE))
                canMove = false;
            if (direction == BlockTypes.Direction.RIGHT && (gridPos.x == 9 || playerGrid[gridPos.y][gridPos.x + 1] != BlockTypes.NONE))
                canMove = false;
            if (direction == BlockTypes.Direction.DOWN && (gridPos.y == 0 || playerGrid[gridPos.y - 1][gridPos.x] != BlockTypes.NONE))
                canMove = false;
        }
        return canMove;
    }

    private void checkForFullLines(boolean wipe) {
        List<Integer> fullLines = new ArrayList<>();
        int numberOfClearedLines = 0;
        for (int i = 0; i < 21; i++) {
            boolean full = true;
            for (int j = 0; j < 10; j++) {
                if (playerGrid[i][j] == BlockTypes.NONE || playerGrid[i][j] == BlockTypes.SOLID_BLOCK) {
                    full = false;
                    break;
                }
            }
            if (full) {
                fullLines.add(i);
                numberOfClearedLines++;
            }
        }

        if (numberOfClearedLines == 0) return;

        for (int n : fullLines) {
            for (int i = 0; i < 10; i++) {
                playerGrid[n][i] = BlockTypes.PLACED_BLOCK;
            }
            if (wipe) {
                for (int i = n; i < 20; i++) {
                    System.arraycopy(playerGrid[i + 1], 0, playerGrid[i], 0, 10);
                }
            }
        }
        updateScore(numberOfClearedLines, true);
        addPowerUp(numberOfClearedLines);
    }

    public void clearAllLines() {
        int score = 0;
        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 10; j++) {
                if (playerGrid[i][j] != BlockTypes.NONE) score += 10;
                playerGrid[i][j] = BlockTypes.NONE;
            }
        }
        updateScore(score, false);
    }

    public void addSolidLine() {
        for(int i = 19; i > 0; i--) {
            for(int j = 0; j < 10; j++) {
                playerGrid[i+1][j] = playerGrid[i][j];
            }
        }
        for(int i = 0; i < 10; i++) {
            playerGrid[0][i] = BlockTypes.SOLID_BLOCK;
        }
    }

    public void setNextBlock(BlockTypes type) {
        blockQueue.poll();
        blockQueue.addFirst(new Block(type, this, new Point(30, 400)));
    }


    public void activateItem(PowerUp.Item item, float durability) {
        this.item = item;
        itemDuration = durability;
        maxItemDuration = itemDuration;
        itemActive = true;
        if(item == PowerUp.Item.MAX_SPEED) {
            resetFallDownTimer();
        }
    }

    private void addPowerUp(int numberOfClearedLines) {
        if (powerUp != PowerUp.Item.NONE) return;
        PowerUp.Item[] powerUps = PowerUp.Item.values();
        powerUp = powerUps[random.nextInt(powerUps.length - 1) + 1];
        popUp = "You got " + powerUp.getName() + "!";
        showPopUp = true;
    }

    private void updateScore(int value, boolean line) {
        if (line)
            score += 100 * value * value;
        else
            score += value;
    }

    public void setNewBlockQueue(BlockTypes type) {
        blockQueue.clear();
        for(int i = 0; i < 4; i++) {
            blockQueue.add(new Block(BlockTypes.I, this, new Point(30, 400)));
        }
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

    public Block getGhostBlock() {
        return ghostBlock;
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

    public PlayerController getPlayerController() {
        return playerController;
    }

    public PowerUp.Item getPowerUp() {
        return powerUp;
    }

    public String getPopUp() {
        return popUp;
    }

    public boolean getShowPopUp() {
        return showPopUp;
    }

    public void setShowPopUp(boolean value) {
        showPopUp = value;
    }

    public float getPopUpAnimation() {
        return popUpAnimation;
    }

    public boolean itemAffectGameScreen() {
        return itemAffectGameScreen;
    }

    public void setItemAffectGameScreen(boolean value) {
        itemAffectGameScreen = value;
    }

    public float getRemainingItemDuration() {
        return (itemDuration / maxItemDuration);
    }

    public void setPopUp(String text, boolean show) {
        popUp = text;
        showPopUp = show;
    }

    public boolean isAttacked() {
        return itemActive;
    }
}
