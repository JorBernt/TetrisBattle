package com.jbgames.tetrisbattle;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.jbgames.tetrisbattle.Screens.GameScreen;

public class TetrisBattle extends Game {

	public static final int V_WIDTH = 1920;
	public static final int V_HEIGHT = 1080;


	@Override
	public void create () {
		setScreen(new GameScreen(this));
	}

	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose () {
		super.dispose();
	}
}
