package com.jbgames.tetrisbattle.GameWorld;


import Tools.Point;
import com.badlogic.gdx.math.Vector2;
import com.jbgames.tetrisbattle.Controllers.Player;
import com.jbgames.tetrisbattle.Controllers.PlayerSettings;
import com.jbgames.tetrisbattle.Entities.Block;
import com.jbgames.tetrisbattle.Entities.BlockTypes;
import com.jbgames.tetrisbattle.Scenes.Hud;

import java.util.*;

public class GameWorld {

    public static final int BLOCK_SIZE = 40;

    private Player player1, player2;

    private final Random random;

    private final List<BlockTypes> types;

    private float countDown = 3f;


    private GameState currentState;
    public enum GameState {
        READY, COUNTDOWN, RUNNING, GAMEOVER, HIGHSCORE
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
        }

    }

    public void updateRunning(float delta) {
        player1.update(delta);
        player2.update(delta);
    }

    public void updateCountDown(float delta) {
        countDown -= delta;
        System.out.println(countDown);
        if(countDown <= 0) {
            countDown = 3f;
            currentState = GameState.RUNNING;
        }
    }

    public void updateReady(float delta) {

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
        return String.format("%.1f", countDown);
    }
}
