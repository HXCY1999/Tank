package com.tankwar;

import javax.swing.*;
import javax.xml.bind.annotation.XmlList;
import java.awt.*;
import java.awt.event.KeyEvent;

public class Tank {

    private int x;

    private int y;

    private Direction direction;

    private final boolean isEnemy;

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
            case UP: return Tools.getImage("tankU.gif");
            case DOWN: return Tools.getImage("tankD.gif");
            case LEFT: return Tools.getImage("tankL.gif");
            case RIGHT: return Tools.getImage("tankR.gif");
            case UPLEFT: return Tools.getImage("tankLU.gif");
            case UPRIGHT: return Tools.getImage("tankRU.gif");
            case DOWNLEFT: return Tools.getImage("tankLD.gif");
            case DOWNRIGHT: return Tools.getImage("tankRD.gif");
        }
        return null;
    }

    void draw(Graphics g){
        int oldX = x;
        int oldY = y;

        this.determineDirection();

        //boundary
        if(x < 0) x = 0;
        if(x > 800 - getImage().getWidth(null))
            x = 800- getImage().getWidth(null);
        if(y < 0) y = 0;
        if(y > 600 - getImage().getHeight(null))
            y =600- getImage().getHeight(null);

        //avoid wall and tank overlap
        Rectangle rec = this.getRectangle();
        for(Wall wall : Client.getInstance().getWalls()){
            if(rec.intersects(wall.getRectangle())){
                //The wall collided with the tank
                x = oldX;
                y = oldY;
                break;
            }
        }

        //avoid tank and enemy tank overlap
        for(Tank enemy : Client.getInstance().getEnemyTank()){
            if(rec.intersects(enemy.getRectangle())){
                x = oldX;
                y = oldY;
                break;
            }
        }

        g.drawImage(this.getImage(), this.getX(), this.getY(), null);
    }

    public Rectangle getRectangle(){
        return new Rectangle(x,y,getImage().getWidth(null),getImage().getHeight(null));
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
