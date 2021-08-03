package com.jbgames.tetrisbattle.Scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.jbgames.tetrisbattle.Controllers.PlayerSettings;
import com.jbgames.tetrisbattle.TetrisBattle;

public class Hud {

    public enum TextType {
        CENTER_SCREEN (TetrisBattle.V_WIDTH/2, TetrisBattle.V_HEIGHT/2),
        PLAYER1_SCORE(100, 100),
        PLAYER2_SCORE(1100, 100),
        PLAYER1_POWERUP_POPUP(200,700),
        PLAYER2_POWERUP_POPUP(1200,700),
        PLAYER1_POWERUP(PlayerSettings.getSettings(1).getPowerUpArea().x+20, PlayerSettings.getSettings(1).getPowerUpArea().y+100),
        PLAYER2_POWERUP(PlayerSettings.getSettings(2).getPowerUpArea().x+20, PlayerSettings.getSettings(2).getPowerUpArea().y+100);

        final int x, y;
        TextType(int x, int y){
            this.x = x;
            this.y = y;
        }
    }

    public enum TextSize {
        SMALL,
        MEDIUM,
        BIG;
    }

    public static BitmapFont fontBig, shadowBig, fontSmall, shadowSmall, fontMedium, shadowMedium;

    public Hud() {
        fontBig = new BitmapFont(Gdx.files.internal("text.fnt"));
        fontBig.getData().setScale(1f, 1f);
        shadowBig = new BitmapFont(Gdx.files.internal("shadow.fnt"));
        shadowBig.getData().setScale(1f,1f);

        fontMedium = new BitmapFont(Gdx.files.internal("text.fnt"));
        fontMedium.getData().setScale(.6f, .6f);
        shadowMedium = new BitmapFont(Gdx.files.internal("shadow.fnt"));
        shadowMedium.getData().setScale(.6f,.6f);

        fontSmall = new BitmapFont(Gdx.files.internal("text.fnt"));
        fontSmall.getData().setScale(.4f, .4f);
        shadowSmall = new BitmapFont(Gdx.files.internal("shadow.fnt"));
        shadowSmall.getData().setScale(.4f,.4f);
    }

    public void draw(String text, TextType type, SpriteBatch batch, TextSize size) {
        switch (size) {
            case SMALL:
                shadowSmall.draw(batch, text, type.x, type.y);
                fontSmall.draw(batch, text, type.x, type.y);
                break;
            case MEDIUM:
                shadowMedium.draw(batch, text, type.x, type.y);
                fontMedium.draw(batch, text, type.x, type.y);
                break;
            case BIG:
                shadowBig.draw(batch, text, type.x, type.y);
                fontBig.draw(batch, text, type.x, type.y);
                break;
        }
    }

    public void draw(String text, TextType type, float x, float y, SpriteBatch batch, TextSize size) {
        switch (size) {
            case SMALL:
                shadowSmall.draw(batch, text, type.x + x, type.y + y);
                fontSmall.draw(batch, text, type.x + x, type.y + y);
                break;
            case MEDIUM:
                shadowMedium.draw(batch, text, type.x + x, type.y + y);
                fontMedium.draw(batch, text, type.x + x, type.y + y);
                break;
            case BIG:
                shadowBig.draw(batch, text, type.x + x, type.y + y);
                fontBig.draw(batch, text, type.x + x, type.y + y);
                break;
        }
    }

    public void draw(String text, int x, int y, SpriteBatch batch, TextSize size) {
        switch (size) {
            case SMALL:
                shadowSmall.draw(batch, text, x, y);
                fontSmall.draw(batch, text, x, y);
                break;
            case MEDIUM:
                shadowMedium.draw(batch, text, x, y);
                fontMedium.draw(batch, text, x, y);
                break;
            case BIG:
                shadowBig.draw(batch, text, x, y);
                fontBig.draw(batch, text, x, y);
                break;
        }
    }
}
