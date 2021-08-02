package com.jbgames.tetrisbattle.Controllers;

import Tools.Point;

public enum PlayerSettings {
    PLAYER_1(new Point(200, 100), new Point(480, 780), new Point(650, 300), new Point(20,700)),
    PLAYER_2(new Point(1200, 100), new Point(1520, 780), new Point(1650, 300), new Point(1020, 700));

    private final Point playAreaOffset, gridPosOffset, nextBlockArea, holdArea;

    PlayerSettings(Point playAreaOffset, Point gridPosOffset, Point nextBlockArea, Point holdArea) {
        this.playAreaOffset = playAreaOffset;
        this.gridPosOffset = gridPosOffset;
        this.nextBlockArea = nextBlockArea;
        this.holdArea = holdArea;
    }

    public Point getOffset() {
        return playAreaOffset;
    }

    public Point getGridPosOffset() {
        return gridPosOffset;
    }

    public Point getNextBlockArea() {
        return nextBlockArea;
    }

    public Point getHoldArea() {
        return holdArea;
    }


    public static PlayerSettings getSettings(int id) {
        switch (id) {
            case 1:
                return PlayerSettings.PLAYER_1;
            case 2:
                return PlayerSettings.PLAYER_2;
            default:
                return null;
        }
    }

}

