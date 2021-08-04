package com.jbgames.tetrisbattle.Controllers;

import com.jbgames.tetrisbattle.Tools.Point;
import com.badlogic.gdx.Input;

public enum PlayerSettings {
    PLAYER_1(new Point(200, 100),
            new Point(480, 780),
            new Point(650, 300),
            new Point(20,700),
            new Point(20,400)),
    PLAYER_2(new Point(1200, 100),
            new Point(1520, 780),
            new Point(1650, 300),
            new Point(1020, 700),
            new Point(1020, 400));

    private final Point playAreaOffset, gridPosOffset, nextBlockArea, holdArea, powerUpArea;

    PlayerSettings(Point playAreaOffset, Point gridPosOffset, Point nextBlockArea, Point holdArea, Point powerUpArea) {
        this.playAreaOffset = playAreaOffset;
        this.gridPosOffset = gridPosOffset;
        this.nextBlockArea = nextBlockArea;
        this.holdArea = holdArea;
        this.powerUpArea = powerUpArea;
    }

    public enum KeyBindings {
        LEFT(Input.Keys.J, Input.Keys.NUMPAD_4),
        DOWN(Input.Keys.K, Input.Keys.NUMPAD_5),
        RIGHT(Input.Keys.L, Input.Keys.NUMPAD_6),
        INSTANT_DROP(Input.Keys.SPACE, Input.Keys.DOWN),
        HOLD_BLOCK(Input.Keys.I, Input.Keys.NUMPAD_8),
        ROTATE_LEFT(Input.Keys.F, Input.Keys.LEFT),
        ROTATE_RIGHT(Input.Keys.G, Input.Keys.RIGHT),
        USE_ITEM(Input.Keys.H, Input.Keys.CONTROL_RIGHT),
        ANY_KEY(0,0);

        private final int player1, player2;
        KeyBindings(int player1, int player2){
            this.player1 = player1;
            this.player2 = player2;
        }

    }

    public Point getPlayAreaOffset() {
        return playAreaOffset;
    }

    public Point getGridPosOffset() {
        return gridPosOffset;
    }

    public Point getNextBlockArea() {
        return nextBlockArea;
    }

    public Point getHoldArea() {
        return holdArea;
    }

    public Point getPowerUpArea() {
        return powerUpArea;
    }

    public KeyBindings getKey(int keyCode, int playerId) {
        for (KeyBindings key : KeyBindings.values()) {
            if (playerId == 1) {
                if (key.player1 == keyCode) return key;
            } else if (playerId == 2) {
                if (key.player2 == keyCode) return key;
            }
        }
        return KeyBindings.ANY_KEY;
    }


    public static PlayerSettings getSettings(int id) {
        switch (id) {
            case 1:
                return PlayerSettings.PLAYER_1;
            case 2:
                return PlayerSettings.PLAYER_2;
            default:
                return null;
        }
    }

}

