package com.jbgames.tetrisbattle.Tools;

import com.jbgames.tetrisbattle.Controllers.PlayerSettings;
import com.jbgames.tetrisbattle.GameWorld.GameWorld;

public class Point {
    public int x, y;
    private int playerID;
    private Point pointOffset;
    private PlayerSettings settings;

    public Convert convertType;

    public enum Convert {
        COORD_TO_GRID,
        GRID_TO_COORD;
    }

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
        playerID = -1;
    }
    public Point(int x, int y, int playerID) {
        this.y = y;
        this.x = x;
        this.playerID = playerID;
        settings = PlayerSettings.getSettings(playerID);
        pointOffset = settings.getOffset();
    }


    public Point (int x, int y, int playerID,  Convert convert) {
        Point p = new Point(x,y, playerID);
        switch (convert) {
            case COORD_TO_GRID: p = p.coordToGridConvert();break;
            case GRID_TO_COORD: p = p.gridToCoordConvert();break;
        }
        this.x = p.x;
        this.y = p.y;
    }

    public Point coordToGridConvert() {
        Point newPos = new Point((x-pointOffset.x)/ GameWorld.BLOCK_SIZE,(y-pointOffset.y)/GameWorld.BLOCK_SIZE );
        return newPos;
    }

    public Point gridToCoordConvert() {
        Point newPos = new Point(pointOffset.x+(x)*GameWorld.BLOCK_SIZE,pointOffset.y+(y)*GameWorld.BLOCK_SIZE );
        return newPos;
    }
}
