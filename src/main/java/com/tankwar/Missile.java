package com.tankwar;

import sun.security.util.Length;

import java.awt.*;

public class Missile {

    private int x;

    public boolean isLive() {
        return live;
    }

    public void setLive(boolean live) {
        this.live = live;
    }

    private int y;

    private boolean live = true;

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

        if(x < 0 || x > Client.WIDTH || y < 0 || y > Client.HEIGHT) {// boundary of frame
            setLive(false);
            return;//stop drawing
        }

        //when missile shot on the wall
        Rectangle rectangle = this.getRectangle();
        for (Wall wall : Client.getInstance().getWalls()){
            if(rectangle.intersects(wall.getRectangle())){
                setLive(false);
                return;
            }
        }

        //when the missile shot on the tank
        if(enemy){//enemy's missile hit on player's tank
            Tank playTank = Client.getInstance().getPlayTank();
            if(rectangle.intersects(playTank.getRectangle())){
                addExplosion();//add explosion gif
                playTank.setHP(playTank.getHP() - 20);
                if(playTank.getHP() <=0 ) playTank.setLive(false);//play tank die
                setLive(false);
                return;//stop drawing the missile
            }
        }else {//player's tank's missile hit on enemy tanks
            //traversal every tank see if missile hit on the tank
            for (Tank enemyTank : Client.getInstance().getEnemyTank()){
                if(rectangle.intersects(enemyTank.getRectangle())){
                    addExplosion();
                    enemyTank.setLive(false);//tank is hit and die
                    Client.getInstance().removeEnemyTank(enemyTank);//remove the tank
                    setLive(false);
                    break;//if hit on the tank stop traversal
                }
            }
        }

        g.drawImage(getImage(),x,y,null);

    }

    private void addExplosion(){
        Client.getInstance().addExplosion(new Explosion(x,y));//add explosion object

        Tools.playAudio("explode.wav");
    }

    Rectangle getRectangle(){
        return new Rectangle(x,y,getImage().getWidth(null),getImage().getHeight(null));
    }

    void move(){
        x += direction.xFactor * SPEED;
        y += direction.yFactor * SPEED;
    }
}
