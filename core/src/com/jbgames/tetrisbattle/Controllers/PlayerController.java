package com.jbgames.tetrisbattle.Controllers;

import com.jbgames.tetrisbattle.Entities.BlockTypes;

public class PlayerController {
    private boolean leftPressed, rightPressed, downPressed;
    private final PlayerSettings settings;
    private final Player player;

    public PlayerController(PlayerSettings settings, Player player) {
        leftPressed = false;
        rightPressed = false;
        downPressed = false;
        this.settings = settings;
        this.player = player;
    }

    public void keyPressed(int keycode) {
        switch (settings.getKey(keycode, player.getId())) {
            case LEFT:
                leftPressed = true;
                break;
            case RIGHT:
                rightPressed = true;
                break;
            case DOWN:
                downPressed = true;
                break;
            case HOLD_BLOCK:
                player.holdBlock();
                break;
            case ROTATE_LEFT:
                player.rotate(BlockTypes.Direction.LEFT);
                break;
            case ROTATE_RIGHT:
                player.rotate(BlockTypes.Direction.RIGHT);
                break;
            case INSTANT_DROP:
                player.placeBlockInstant();
                break;

        }
    }

    public void keyReleased(int keycode) {
        switch (settings.getKey(keycode, player.getId())) {
            case LEFT:
                leftPressed = false;
                break;
            case RIGHT:
                rightPressed = false;
                break;
            case DOWN:
                downPressed = false;
                break;
        }
    }

    public boolean isLeftPressed() {
        return leftPressed;
    }

    public boolean isRightPressed() {
        return rightPressed;
    }

    public boolean isDownPressed() {
        return downPressed;
    }
}
