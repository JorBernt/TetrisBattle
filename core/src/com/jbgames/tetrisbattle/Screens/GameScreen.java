package com.jbgames.tetrisbattle.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.jbgames.tetrisbattle.Controllers.InputController;
import com.jbgames.tetrisbattle.GameWorld.GameRenderer;
import com.jbgames.tetrisbattle.GameWorld.GameWorld;
import com.jbgames.tetrisbattle.TetrisBattle;

public class GameScreen implements Screen {

    private final TetrisBattle game;
    private final GameRenderer renderer;
    private final GameWorld world;

    public GameScreen(TetrisBattle game) {
        this.game = game;
        world = new GameWorld();
        renderer = new GameRenderer(world);
        Gdx.input.setInputProcessor(new InputController(world));
    }

    @Override
    public void show() {

    }


    @Override
    public void render(float delta) {
        world.update(delta);
        renderer.render();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
