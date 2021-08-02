package com.jbgames.tetrisbattle.Entities;

import com.badlogic.gdx.graphics.Color;

public enum BlockTypes {

    L(Color.RED),
    T(Color.BLUE),
    I(Color.LIME),
    REVERSE_L(Color.PINK),
    SQUIGGLY(Color.PURPLE),
    REVERSE_SQUIGGLY(Color.YELLOW),
    SQUARE(Color.GREEN),
    NONE(Color.GRAY),
    PLACED_BLOCK(Color.WHITE);


    public enum Direction {
        LEFT,RIGHT,UP,DOWN
    }

    private final Color color;

    BlockTypes(Color color) {
        this.color = color;
    }


    public Color getColor() {
        return color;
    }

    public int[][] getPosition(BlockTypes type, Direction direction) {
        int[][] pos = new int[4][4];
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


            default: pos = new int[4][4];
        }
        return pos;
    }
}
