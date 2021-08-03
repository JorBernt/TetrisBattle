package com.jbgames.tetrisbattle.Controllers;

import com.badlogic.gdx.InputProcessor;
import com.jbgames.tetrisbattle.Entities.BlockTypes;
import com.jbgames.tetrisbattle.GameWorld.GameWorld;

public class InputController implements InputProcessor {

    private final GameWorld world;
    private final PlayerController player1, player2;

    public InputController(GameWorld world) {
        this.world = world;
        player1 = world.getPlayer(1).getPlayerController();
        player2 = world.getPlayer(2).getPlayerController();
    }

    @Override
    public boolean keyDown(int keycode) {
        if(world.getCurrentState() == GameWorld.GameState.READY) {
            world.setCurrentState(GameWorld.GameState.COUNTDOWN);
        }
        if(world.getCurrentState() == GameWorld.GameState.RUNNING) {
            player1.keyPressed(keycode);
            player2.keyPressed(keycode);
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        if(world.getCurrentState() == GameWorld.GameState.RUNNING) {
            player1.keyReleased(keycode);
            player2.keyReleased(keycode);
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
