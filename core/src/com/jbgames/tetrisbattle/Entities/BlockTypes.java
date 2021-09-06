package com.jbgames.tetrisbattle.Entities;

import com.badlogic.gdx.graphics.Color;

public enum BlockTypes {

    L(Color.RED, true),
    T(Color.BLUE, true),
    I(Color.LIME, true),
    REVERSE_L(Color.PINK, true),
    SQUIGGLY(Color.PURPLE, true),
    REVERSE_SQUIGGLY(Color.YELLOW, true),
    SQUARE(Color.GREEN, true),
    NONE(Color.GRAY, false),
    PLACED_BLOCK(Color.WHITE, false),
    SOLID_BLOCK(Color.DARK_GRAY, false),
    MONSTER(Color.GOLD, false);


    public enum Direction {
        LEFT,RIGHT,UP,DOWN
    }

    private final Color color;
    private final boolean standardBlock;

    BlockTypes(Color color, boolean standardBlock) {
        this.color = color;
        this.standardBlock = standardBlock;
    }


    public Color getColor() {
        return color;
    }

    public boolean isStandardBlock() { return standardBlock; }

    public int[][] getPosition(BlockTypes type, Direction direction) {
        int[][] pos;
        switch (type) {
            case I:
                pos = new int[][]
                                {{0,0,0,0},
                                {0,0,0,0},
                                {1,1,1,1},
                                {0,0,0,0},};
                break;

            case T:
                pos = new int[][]
                                {{0,0,0},
                                {1,1,1},
                                {0,1,0},};
                break;

            case L:
                pos = new int[][]
                                {{0,0,0},
                                {1,1,1},
                                {1,0,0},};
                break;
            case REVERSE_L:
                pos = new int[][]
                                {{0,0,0},
                                {1,1,1},
                                {0,0,1},};
                break;

            case SQUIGGLY:
                pos = new int[][]
                                {{0,0,0},
                                {0,1,1},
                                {1,1,0},};
                break;
            case REVERSE_SQUIGGLY:
                pos = new int[][]
                                {{0,0,0},
                                {1,1,0},
                                {0,1,1},};
                break;

            case SQUARE:
                pos = new int[][]
                                {{0,0,0,0},
                                {0,1,1,0},
                                {0,1,1,0},
                                {0,0,0,0}};
                break;
            case MONSTER:
                pos = new int[][]
                                {{1,1,1,1},
                                {0,1,1,0},
                                {1,1,1,1},
                                {1,0,0,1}};
                break;


            default: pos = new int[4][4];
        }
        return pos;
    }
}
