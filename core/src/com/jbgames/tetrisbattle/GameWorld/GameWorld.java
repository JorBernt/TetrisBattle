package com.jbgames.tetrisbattle.GameWorld;


import com.jbgames.tetrisbattle.Controllers.Player;
import com.jbgames.tetrisbattle.Controllers.PlayerSettings;
import com.jbgames.tetrisbattle.Entities.BlockTypes;

import java.util.*;

public class GameWorld {

    public static final int BLOCK_SIZE = 40;
    private Player player1, player2;
    private final Random random;
    private final List<BlockTypes> types;
    private float countDown = 3f;


    private GameState currentState;
    public enum GameState {
        READY, COUNTDOWN, RUNNING, GAMEOVER, HIGHSCORE;
    }

    public GameWorld () {
        currentState = GameState.READY;
        random = new Random();
        types = new ArrayList<>();
        for(BlockTypes type : BlockTypes.values()) {
            if(type != BlockTypes.NONE && type != BlockTypes.PLACED_BLOCK)
            types.add(type);
        }
        player1 = new Player(1, this);
        player2 = new Player(2, this);
    }

    public void update(float delta) {
        switch (currentState) {
            case READY:
                updateReady(delta);
                break;
            case COUNTDOWN:
                updateCountDown(delta);
                break;
            case RUNNING:
                updateRunning(delta);
                break;
            case GAMEOVER:
                updateGameOver(delta);
                break;
        }
    }

    private void updateRunning(float delta) {
        player1.update(delta);
        player2.update(delta);
    }

    private void updateCountDown(float delta) {
        countDown -= delta;
        if(countDown <= 0) {
            countDown = 3f;
            currentState = GameState.RUNNING;
        }
    }

    private void updateReady(float delta) {

    }

    private void updateGameOver(float delta) {

    }

    public void reset() {
        player1.reset();
        player2.reset();
    }



    public BlockTypes getNewBlock() {
        return types.get(random.nextInt(types.size()));
    }



    public Player getPlayer(int id) {
        switch (id) {
            case 1 : return player1;
            case 2 : return player2;
            default: return null;
        }
    }


    public GameState getCurrentState() {
        return currentState;
    }

    public void setCurrentState(GameState state) {
        currentState = state;
    }

    public String getCountDown() {
        if(countDown >= 2) return "READY!";
        if(countDown >= 1) return "SET!";
        else return "GO!";
    }
}
