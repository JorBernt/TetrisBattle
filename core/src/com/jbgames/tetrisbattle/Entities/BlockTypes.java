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
                switch (direction) {
                    case UP :
                    case DOWN :
                        pos = new int[][]
                                        {{0,0,0,0},
                                        {0,0,0,0},
                                        {1,1,1,1},
                                        {0,0,0,0},};
                        break;
                    case LEFT :
                        pos = new int[][]
                                        {{0,1,0,0},
                                        {0,1,0,0},
                                        {0,1,0,0},
                                        {0,1,0,0},};
                        break;
                    case RIGHT:
                        pos = new int[][]
                                        {{0,0,1,0},
                                        {0,0,1,0},
                                        {0,0,1,0},
                                        {0,0,1,0},};
                        break;
                }
                break;

            case T:
                switch (direction) {
                    case UP :
                        pos = new int[][]
                                        {{1,1,1},
                                        {0,1,0},
                                        {0,0,0},};
                        break;
                    case DOWN :
                        pos = new int[][]
                                        {{0,0,0},
                                        {0,1,0},
                                        {1,1,1},};
                        break;
                    case LEFT :
                        pos = new int[][]
                                        {{0,1,0},
                                        {0,1,1},
                                        {0,1,0},};
                        break;
                    case RIGHT:
                        pos = new int[][]
                                        {{0,1,0},
                                        {1,1,0},
                                        {0,1,0},};
                        break;
                }
                break;

            case L:
                switch (direction) {
                    case UP :
                        pos = new int[][]
                                        {{1,1,1},
                                        {1,0,0},
                                        {0,0,0},};
                        break;
                    case DOWN :
                        pos = new int[][]
                                        {{0,0,0},
                                        {0,0,1},
                                        {1,1,1},};
                        break;
                    case LEFT :
                        pos = new int[][]
                                        {{0,1,0},
                                        {0,1,0},
                                        {0,1,1},};
                        break;
                    case RIGHT:
                        pos = new int[][]
                                        {{1,1,0},
                                        {0,1,0},
                                        {0,1,0},};
                        break;
                }
                break;
            case REVERSE_L:
                switch (direction) {
                    case UP :
                        pos = new int[][]
                                        {{1,1,1},
                                        {0,0,1},
                                        {0,0,0},};
                        break;
                    case DOWN :
                        pos = new int[][]
                                        {{0,0,0},
                                        {1,0,0},
                                        {1,1,1},};
                        break;
                    case LEFT :
                        pos = new int[][]
                                        {{0,1,1},
                                        {0,1,0},
                                        {0,1,0},};
                        break;
                    case RIGHT:
                        pos = new int[][]
                                        {{0,1,0},
                                        {0,1,0},
                                        {1,1,0},};
                        break;
                }
                break;

            case SQUIGGLY:
                switch (direction) {
                    case UP :
                        pos = new int[][]
                                        {{0,0,0},
                                        {0,1,1},
                                        {1,1,0},};
                        break;
                    case DOWN :
                        pos = new int[][]
                                        {{0,0,0},
                                        {0,1,1},
                                        {1,1,0},};
                        break;
                    case LEFT :
                        pos = new int[][]
                                        {{1,0,0},
                                        {1,1,0},
                                        {0,1,0},};
                        break;
                    case RIGHT:
                        pos = new int[][]
                                        {{0,1,0},
                                        {0,1,1},
                                        {0,0,1},};
                        break;
                }
                break;
            case REVERSE_SQUIGGLY:
                switch (direction) {
                    case UP :
                        pos = new int[][]
                                        {{0,0,0},
                                        {1,1,0},
                                        {0,1,1},};
                        break;
                    case DOWN :
                        pos = new int[][]
                                        {{0,0,0},
                                        {1,1,0},
                                        {0,1,1},};
                        break;
                    case LEFT :
                        pos = new int[][]
                                        {{0,1,0},
                                        {1,1,0},
                                        {1,0,0},};
                        break;
                    case RIGHT:
                        pos = new int[][]
                                        {{0,0,1},
                                        {0,1,1},
                                        {0,1,0},};
                        break;
                }
                break;

            case SQUARE:
                pos = new int[][]
                                {{0,0,0,0},
                                {0,1,1},
                                {0,1,1},};
                break;


            default: pos = new int[4][4];
        }
        return pos;
    }
}
