package com.jbgames.tetrisbattle.Scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.jbgames.tetrisbattle.TetrisBattle;

public class Hud {

    public enum TextType {
        CENTER_SCREEN (TetrisBattle.V_WIDTH/2, TetrisBattle.V_HEIGHT/2),
        PLAYER1(100, 100),
        PLAYER2(1100, 100);
        final int x, y;
        TextType(int x, int y){
            this.x = x;
            this.y = y;
        }
    }

    public static BitmapFont font, shadow;

    public Hud() {
        font = new BitmapFont(Gdx.files.internal("text.fnt"));
        font.getData().setScale(1f, 1f);
        shadow = new BitmapFont(Gdx.files.internal("shadow.fnt"));
        shadow.getData().setScale(1f,1f);
    }

    public void draw(String text, TextType type, SpriteBatch batch) {
        font.draw(batch, text, type.x, type.y);
    }

    public void draw(String text, TextType type, int x, int y, SpriteBatch batch) {
        font.draw(batch, text, type.x + x, type.y + y);
    }

    public void draw(String text, int x, int y, SpriteBatch batch) {
        font.draw(batch, text, x, y);
    }


}
