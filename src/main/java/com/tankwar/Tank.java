package com.tankwar;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import javax.swing.*;
import javax.xml.bind.annotation.XmlList;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.Random;

public class Tank {

    private int x;

    private int y;

    private Direction direction;

    private final boolean isEnemy;

    private final int speed = 10;

    public Tank(int x, int y, Direction direction, boolean isEnemy) {
        this.x = x;
        this.y = y;
        this.direction = direction;
        this.isEnemy = isEnemy;
    }

    void move(){
        x = x + direction.xFactor * speed;
        y = y + direction.yFactor * speed;
//        switch (direction){
//            case UP:
//                y-=speed;
//                break;
//            case DOWN:
//                y+=speed;
//                break;
//            case LEFT:
//                x-=speed;
//                break;
//            case RIGHT:
//                x+=speed;
//                break;
//            case LEFT_UP:
//                y-=speed;
//                x-=speed;
//                break;
//            case RIGHT_UP:
//                y-=speed;
//                x+=speed;
//                break;
//            case LEFT_DOWN:
//                y+=speed;
//                x-=speed;
//                break;
//            case RIGHT_DOWN:
//                y+=speed;
//                x+=speed;
//                break;
//        }
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
        return direction.getImage(prefix + "tank"); //use enum to simplify code
//        switch (direction){
//            case UP: return Tools.getImage("tankU.gif");
//            case DOWN: return Tools.getImage("tankD.gif");
//            case LEFT: return Tools.getImage("tankL.gif");
//            case RIGHT: return Tools.getImage("tankR.gif");
//            case LEFT_UP: return Tools.getImage("tankLU.gif");
//            case RIGHT_UP: return Tools.getImage("tankRU.gif");
//            case LEFT_DOWN: return Tools.getImage("tankLD.gif");
//            case RIGHT_DOWN: return Tools.getImage("tankRD.gif");
//        }
//        return null;
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
            case  KeyEvent.VK_UP: up = true;
            this.move();
                break;
            case  KeyEvent.VK_DOWN: down = true;
                this.move();
                break;
            case  KeyEvent.VK_LEFT: left = true;
                this.move();
                break;
            case  KeyEvent.VK_RIGHT: right = true;
                this.move();
                break;
            case KeyEvent.VK_CONTROL:fire(); break;
            case KeyEvent.VK_A:superFire(); break;
        }

    }
    private void superFire() {
        for (Direction direction : Direction.values()) {
            Missile missile = new Missile(x + getImage().getWidth(null) / 2 - 6,
                    y + getImage().getHeight(null) / 2 - 6, isEnemy, direction);
            Client.getInstance().getMissiles().add(missile);
        }
        //set two kinds of audio randomly generate
        String audioFile = new Random().nextBoolean() ? "supershoot.aiff" : "supershoot.wav";
        playAudio(audioFile);
    }

    private void playAudio(String fineName) {
        Media sound = new Media(new File("assets/audios/"+fineName).toURI().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(sound);
        mediaPlayer.play();
    }

    private void fire() {
        Missile missile = new Missile(x+getImage().getWidth(null)/2 - 6,
                y+getImage().getHeight(null)/2 - 6,isEnemy,this.direction);
        Client.getInstance().getMissiles().add(missile);

        playAudio("shoot.wav");
    }

    private void determineDirection(){
        if(up && left && !down && !right) this.direction = Direction.LEFT_UP;
        if(up && right && !left && !down) this.direction = Direction.RIGHT_UP;
        if(down && left && !up && !right) this.direction = Direction.LEFT_DOWN;
        if(down && right && !up && !left) this.direction = Direction.RIGHT_DOWN;
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
    }
}
