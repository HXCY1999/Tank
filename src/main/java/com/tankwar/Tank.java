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

    private boolean live = true;

    private int HP = 100;

    public void setHP(int HP) {
        this.HP = HP;
    }

    public int getHP() {
        return HP;
    }

    private Direction direction;

     boolean isLive() {
        return live;
    }

     void setLive(boolean live) {
        this.live = live;
    }

    private final boolean isEnemy;

    private final int speed = 10;

    public boolean isEnemy() {
        return isEnemy;
    }

     Tank(int x, int y, Direction direction, boolean isEnemy) {
        this.x = x;
        this.y = y;
        this.direction = direction;
        this.isEnemy = isEnemy;
    }

    void move(){
        x = x + direction.xFactor * speed;
        y = y + direction.yFactor * speed;
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

        if(!this.isLive()) return;

        int oldX = x;
        int oldY = y;

        //only determine direction for player tank
        if(!this.isEnemy) this.determineDirection();

        //the stop is false initially
        //after determinate direction player thank is stopped
        //after press key stop variable can be false so player tank can move
        //no determinate direction of enemy tank so enemy tank can move
        if(!stopped) this.move();

        //boundary
        if(x < 0) x = 0;
        if(x > 800 - getImage().getWidth(null))
            x = 800- getImage().getWidth(null);
        if(y < 0) y = 0;
        if(y > 600 - getImage().getHeight(null))
            y =600- getImage().getHeight(null);

        //avoid wall and tank overlap
        Rectangle rectangle = this.getRectangle();
        for(Wall wall : Client.getInstance().getWalls()){
            if(rectangle.intersects(wall.getRectangle())){
                //The wall collided with the tank
                x = oldX;
                y = oldY;
                break;
            }
        }

        //avoid enemy tank and enemy tank overlap
        for(Tank enemy : Client.getInstance().getEnemyTank()){
            if(enemy != this && rectangle.intersects(enemy.getRectangle())){
                x = oldX;
                y = oldY;
                break;
            }
        }
        // enemy tank hit player tank
        if(this.isEnemy && rectangle.intersects(Client.getInstance()
                .getPlayTank().getRectangle())){
            x = oldX;
            y = oldY;
        }

        //add HP bar for player tank
        if(!isEnemy){
            g.setColor(Color.white);
            g.drawRect(x,y - 10,this.getImage().getWidth(null),10);

            g.setColor(Color.red);
            int width = HP * this.getImage().getWidth(null) / 100;
            g.fillRect(x,y - 10,width,10);

            Image petImage = Tools.getImage("pet-camel.gif");
            g.drawImage(petImage,
                    this.x - petImage.getWidth(null) - DISTANCE_TO_PET,
                    this.y,null);

        }
        g.drawImage(this.getImage(), this.getX(), this.getY(), null);
    }

    private static final int DISTANCE_TO_PET = 4;

    public Rectangle getRectangle(){
        if(isEnemy)
        return new Rectangle(x,y,getImage().getWidth(null),getImage().getHeight(null));
        else{
            Image petImage = Tools.getImage("pet-camel.gif");
            final int delta = petImage.getWidth(null
            ) + DISTANCE_TO_PET;
            return new Rectangle(x,y,
                    getImage().getWidth(null) + delta,
                    getImage().getHeight(null));

        }
    }

    private boolean up = false, down = false, left = false, right = false;
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()){
            case  KeyEvent.VK_UP: up = true;
                break;
            case  KeyEvent.VK_DOWN: down = true;
                break;
            case  KeyEvent.VK_LEFT: left = true;
                break;
            case  KeyEvent.VK_RIGHT: right = true;
                break;
            case KeyEvent.VK_CONTROL:fire(); break;
            case KeyEvent.VK_A:superFire(); break;
            case KeyEvent.VK_F2:Client.getInstance().restart();break;
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
        Tools.playAudio(audioFile);
    }


    private void fire() {
        Missile missile = new Missile(x+getImage().getWidth(null)/2 - 6,
                y+getImage().getHeight(null)/2 - 6,isEnemy,this.direction);
        Client.getInstance().getMissiles().add(missile);

        Tools.playAudio("shoot.wav");
    }

    private boolean stopped = false;

    private void determineDirection(){

        //initially set the tank still not move automatically
        if(!up && !right && !down && !left) this.stopped = true;
        else {
            if (up && left && !down && !right) this.direction = Direction.LEFT_UP;
            if (up && right && !left && !down) this.direction = Direction.RIGHT_UP;
            if (down && left && !up && !right) this.direction = Direction.LEFT_DOWN;
            if (down && right && !up && !left) this.direction = Direction.RIGHT_DOWN;
            if (up && !left && !down && !right) this.direction = Direction.UP;
            if (!up && left && !down && !right) this.direction = Direction.LEFT;
            if (!up && !left && down && !right) this.direction = Direction.DOWN;
            if (!up && !left && !down && right) this.direction = Direction.RIGHT;
            this.stopped = false;
        }
    }
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()){
            case  KeyEvent.VK_UP: up = false; break;
            case  KeyEvent.VK_DOWN: down = false; break;
            case  KeyEvent.VK_LEFT: left = false; break;
            case  KeyEvent.VK_RIGHT: right = false; break;
        }
    }

    private final Random random = new Random();

    private int step = random.nextInt(12) + 3 ;//control the frequency of shooting
    void addRandomlyMove() {
        Direction[] dirs = Direction.values();
        if(step == 0){//if step is 0, shoot!
            step = random.nextInt(12) + 3;//set frequency again
            this.direction = dirs[random.nextInt(dirs.length)]; //randomly direction
            if(random.nextBoolean()){//fire randomly,used to decrease fire frequency
                this.fire();
            }
        }
        step--;

    }
}
