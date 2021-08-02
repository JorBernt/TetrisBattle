package com.jbgames.tetrisbattle;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.jbgames.tetrisbattle.Screens.GameScreen;

public class TetrisBattle extends Game {

	public static final int V_WIDTH = 1920;
	public static final int V_HEIGHT = 1080;

	private SpriteBatch batch;

	@Override
	public void create () {
		batch = new SpriteBatch();
		setScreen(new GameScreen(this));
	}

	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose () {
		super.dispose();
		batch.dispose();
	}
}
