package com.tankwar;

import javax.swing.*;
import java.awt.*;

public class Tank {

    private int x;

    private int y;

    private Direction direction;

    public Tank(int x, int y, Direction direction) {
        this.x = x;
        this.y = y;
        this.direction = direction;
    }
    void move(){
        switch (direction){
            case UP:   y-=5;
            case DOWN:  y+=5;
            case LEFT:  x-=5;
            case RIGHT: x+=5;
        }
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Direction getDirection() {
        return direction;
    }

    Image getImage(){
        switch (direction){
            case UP: return new ImageIcon("assets/images/tankU.gif").getImage();
            case DOWN: return new ImageIcon("assets/images/tankD.gif").getImage();
            case LEFT: return new ImageIcon("assets/images/tankL.gif").getImage();
            case RIGHT: return new ImageIcon("assets/images/tankR.gif").getImage();
        }
        return null;
    }



}
