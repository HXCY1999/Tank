package com.tankwar;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

public class Tank {

    private int x;

    private int y;

    private Direction direction;

    private boolean isEnemy;

    public Tank(int x, int y, Direction direction, boolean isEnemy) {
        this.x = x;
        this.y = y;
        this.direction = direction;
        this.isEnemy = isEnemy;
    }

    void move(){
        switch (direction){
            case UP:
                y-=5;
                break;
            case DOWN:
                y+=5;
                break;
            case LEFT:
                x-=5;
                break;
            case RIGHT:
                x+=5;
                break;
            case UPLEFT:
                y-=5;
                x-=5;
                break;
            case UPRIGHT:
                y-=5;
                x+=5;
                break;
            case DOWNLEFT:
                y+=5;
                x-=5;
                break;
            case DOWNRIGHT:
                y+=5;
                x+=5;
                break;
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
        String prefix = isEnemy ? "e" : "";
        switch (direction){
            case UP: return new ImageIcon("assets/images/"+prefix+"tankU.gif").getImage();
            case DOWN: return new ImageIcon("assets/images/"+prefix+"tankD.gif").getImage();
            case LEFT: return new ImageIcon("assets/images/"+prefix+"tankL.gif").getImage();
            case RIGHT: return new ImageIcon("assets/images/"+prefix+"tankR.gif").getImage();
            case UPLEFT: return new ImageIcon("assets/images/"+prefix+"tankLU.gif").getImage();
            case UPRIGHT: return new ImageIcon("assets/images/"+prefix+"tankRU.gif").getImage();
            case DOWNLEFT: return new ImageIcon("assets/images/"+prefix+"tankLD.gif").getImage();
            case DOWNRIGHT: return new ImageIcon("assets/images/"+prefix+"tankRD.gif").getImage();
        }
        return null;
    }
    void draw(Graphics g){
        this.determineDirection();
        g.drawImage(this.getImage(), this.getX(), this.getY(), null);
    }
    private boolean up = false, down = false, left = false, right = false;
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()){
            case  KeyEvent.VK_UP: up = true; break;
            case  KeyEvent.VK_DOWN: down = true; break;
            case  KeyEvent.VK_LEFT: left = true; break;
            case  KeyEvent.VK_RIGHT: right = true; break;
        }
        this.move();
    }
    private void determineDirection(){
        if(up && left && !down && !right) this.direction = Direction.UPLEFT;
        if(up && right && !left && !down) this.direction = Direction.UPRIGHT;
        if(down && left && !up && !right) this.direction = Direction.DOWNLEFT;
        if(down && right && !up && !left) this.direction = Direction.DOWNRIGHT;
        if(up && !left && !down && !right) this.direction = Direction.UP;
        if(!up && left && !down && !right) this.direction = Direction.LEFT;
        if(!up && !left && down && !right) this.direction = Direction.DOWN;
        if(!up && !left && !down && right) this.direction = Direction.RIGHT;
    }
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()){
            case  KeyEvent.VK_UP: up = false; break;
            case  KeyEvent.VK_DOWN: down = false; break;
            case  KeyEvent.VK_LEFT: left = false; break;
            case  KeyEvent.VK_RIGHT: right = false; break;
        }
        this.move();

    }
}
