package com.jbgames.tetrisbattle.Controllers;

import com.badlogic.gdx.InputProcessor;
import com.jbgames.tetrisbattle.Entities.BlockTypes;
import com.jbgames.tetrisbattle.GameWorld.GameWorld;

public class InputController implements InputProcessor {

    private final GameWorld world;
    private final Player player1, player2;

    public InputController(GameWorld world) {
        this.world = world;
        player1 = world.getPlayer(1);
        player2 = world.getPlayer(2);
    }

    @Override
    public boolean keyDown(int keycode) {
        if(world.getCurrentState() == GameWorld.GameState.READY) {
            world.setCurrentState(GameWorld.GameState.COUNTDOWN);
        }
        System.out.println(keycode);
        if(world.getCurrentState() == GameWorld.GameState.RUNNING) {
            switch (keycode) {
                case 51: //Left-Player rotate left
                    player1.rotate(BlockTypes.Direction.LEFT);
                    break;
                case 47:
                    player1.rotate(BlockTypes.Direction.RIGHT);
                    //leftPlayer.move(BlockTypes.Direction.DOWN);
                    break;
                case 29:
                    player1.move(BlockTypes.Direction.LEFT);
                    break;
                case 32:
                    player1.move(BlockTypes.Direction.RIGHT);
                    break;
                case 62:
                    player1.moveDown();
                    break;
                case 129:
                    player1.holdBlock();
                    break;

                case 19: //Left-Player rotate left
                    player2.rotate(BlockTypes.Direction.LEFT);
                    break;
                case 20:
                    player2.rotate(BlockTypes.Direction.RIGHT);
                    //leftPlayer.move(BlockTypes.Direction.DOWN);
                    break;
                case 21:
                    player2.move(BlockTypes.Direction.LEFT);
                    break;
                case 22:
                    player2.move(BlockTypes.Direction.RIGHT);
                    break;
                case 144:
                    player2.moveDown();
                    break;
                case 130:
                    player2.holdBlock();
                    break;
                case 59:
                    player1.placeBlockInstant();
            }
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        if(world.getCurrentState() == GameWorld.GameState.RUNNING) {
            if(keycode == 62) {
                player1.moveDown();
            }
            if(keycode == 144) {
                player2.moveDown();
            }
        }
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }
}
