package com.tankwar;

import java.awt.*;

public class Missile {

    private int x;

    private int y;

    private final boolean enemy;

    private final Direction direction;

    public static final int SPEED = 10;

    public Missile(int x, int y, boolean enemy, Direction direction) {
        this.x = x;
        this.y = y;
        this.enemy = enemy;
        this.direction = direction;
    }

    Image getImage(){
        return direction.getImage("missile");
//        switch (direction){
//            case UP: return Tools.getImage("missileU.gif");
//            case DOWN: return Tools.getImage("missileD.gif");
//            case LEFT: return Tools.getImage("missileL.gif");
//            case RIGHT: return Tools.getImage("missileR.gif");
//            case LEFT_UP: return Tools.getImage("missileLU.gif");
//            case RIGHT_UP: return Tools.getImage("missileRU.gif");
//            case LEFT_DOWN: return Tools.getImage("missileLD.gif");
//            case RIGHT_DOWN: return Tools.getImage("missileRD.gif");
//        }
//        return null;
    }

    public void draw(Graphics g) {
        move();
        if(x < 0 || x > 800 || y < 0 || y > 600) return; // boundary

        g.drawImage(getImage(),x,y,null);

    }

    Rectangle getRectangle(){
        return new Rectangle(x,y,getImage().getWidth(null),getImage().getHeight(null));
    }

    void move(){
        x += direction.xFactor * SPEED;
        y += direction.yFactor * SPEED;
    }
}
