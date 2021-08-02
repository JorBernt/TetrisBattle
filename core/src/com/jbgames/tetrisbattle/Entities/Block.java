package com.jbgames.tetrisbattle.Entities;

import Tools.Point;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.jbgames.tetrisbattle.Controllers.Player;
import com.jbgames.tetrisbattle.GameWorld.GameWorld;

import java.util.ArrayList;
import java.util.List;

public class Block {

    private BlockTypes type;
    private final Point position;
    private final int startPosY;
    private final int startPosX;
    private int[][] blockLayout;
    private final Player player;
    private boolean toBePlaced;
    private BlockTypes.Direction curDirection;

    public Block(BlockTypes type, Player player, Point startPosition) {
        position = startPosition;
        curDirection = BlockTypes.Direction.UP;
        this.type = type;
        this.player = player;
        startPosY = startPosition.y;
        startPosX = startPosition.x;
        toBePlaced = false;
        initBlocks();
    }

    public Block(BlockTypes type, Player player) {this(type, player, new Point(0,0));}

    private void initBlocks() {
        blockLayout = type.getPosition(type, curDirection);
    }

    public void rotate(BlockTypes.Direction direction) {
        int[][] temp = new int[blockLayout.length][blockLayout.length];
        temp = copyArray(blockLayout);

        switch (direction) {
            case LEFT: {
                blockLayout = rotateCounterClockWise(blockLayout);
                switch (curDirection) {
                    case LEFT:
                        curDirection = BlockTypes.Direction.DOWN;
                        break;
                    case RIGHT:
                        curDirection = BlockTypes.Direction.UP;
                        break;
                    case UP:
                        curDirection = BlockTypes.Direction.LEFT;
                        break;
                    case DOWN:
                        curDirection = BlockTypes.Direction.RIGHT;
                        break;
                }
                break;

            }
            case RIGHT: {
                blockLayout = rotateClockWise(blockLayout);
                switch (curDirection) {
                    case LEFT:
                        curDirection = BlockTypes.Direction.UP;
                        break;
                    case RIGHT:
                        curDirection = BlockTypes.Direction.DOWN;
                        break;
                    case UP:
                        curDirection = BlockTypes.Direction.RIGHT;
                        break;
                    case DOWN:
                        curDirection = BlockTypes.Direction.LEFT;
                        break;
                }
                break;
            }
        }

        for(Point pos : getBlocks()) {
            Point gridPos = pos.coordToGridConvert();
            if(gridPos.x < 0) {
                position.x += GameWorld.BLOCK_SIZE;
            }
            if(gridPos.x > 9) {
                position.x -= GameWorld.BLOCK_SIZE;
            }
            if(gridPos.y < 0) {
                blockLayout = temp;
                break;
            }
        }
        if(player.collision(this)) {
            blockLayout = temp;
        }
    }

    private int[][] copyArray(int[][] array) {
        int n = array.length;
        int[][] copy = new int[n][n];
        for(int i = 0; i < n; i++) {
            for(int j = 0; j < n; j++) {
                copy[i][j] = array[i][j];
            }
        }
        return copy;
    }

    private int[][] rotateClockWise(int[][] matrix) {
        int size = matrix.length;
        int[][] ret = new int[size][size];

        for (int i = 0; i < size; ++i)
            for (int j = 0; j < size; ++j)
                ret[i][j] = matrix[size - j - 1][i];

        return ret;
    }

    private int[][] rotateCounterClockWise(int[][] matrix) {
        int size = matrix.length;
        int[][] ret = new int[size][size];

        for (int i = 0; i < size; ++i)
            for (int j = 0; j < size; ++j)
                ret[i][j] = matrix[j][size - i - 1];

        return ret;
    }

    public void move(BlockTypes.Direction direction) {
        switch (direction) {
            case LEFT: position.x -= GameWorld.BLOCK_SIZE;break;
            case RIGHT: position.x += GameWorld.BLOCK_SIZE;break;
            case DOWN: position.y -= GameWorld.BLOCK_SIZE;break;
        }
    }

    public Color getColor() {
        return toBePlaced ? BlockTypes.PLACED_BLOCK.getColor() : type.getColor();
    }

    public List<Point> getBlocks() {
        List<Point> blocks = new ArrayList<>();
        for (int i = blockLayout.length - 1; i >= 0; i--) {
            for (int j = blockLayout.length - 1; j >= 0; j--) {
                if (blockLayout[i][j] == 1)
                    blocks.add(new Point(position.x + j * GameWorld.BLOCK_SIZE, position.y + i * GameWorld.BLOCK_SIZE, player.getId()));
            }
        }
        return blocks;
    }

    public void setNewBlock(BlockTypes type) {
        this.type = type;
        curDirection = BlockTypes.Direction.UP;
        blockLayout = type.getPosition(type, curDirection);
        toBePlaced = false;
    }

    public void nextBlock(BlockTypes type) {
        this.type = type;
        curDirection = BlockTypes.Direction.UP;
        blockLayout = type.getPosition(type, curDirection);
        position.y = startPosY;
        position.x = startPosX;
        toBePlaced = false;
    }



    public Player getPlayer() {
        return player;
    }

    public BlockTypes getType() {
        return type;
    }

    public void setToBePlaced(boolean val) {
        toBePlaced = val;
    }

    public boolean isToBePlaced() {
        return toBePlaced;
    }

    public Point getPosition() {
        return position;
    }

    public Block copy() {
        return new Block(type, player, new Point(0,0));
    }
}
