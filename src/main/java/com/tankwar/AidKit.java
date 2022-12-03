package com.tankwar;

import java.awt.*;

public class AidKit {

    private  int x ,y;


    private boolean live = true;


    public AidKit(int x, int y, boolean live) {
        this.x = x;
        this.y = y;
        this.live = live;
    }

    public boolean isLive() {
        return live;
    }

    public void setLive(boolean live) {
        this.live = live;
    }

    public AidKit(int x, int y) {
        this.x = x;
        this.y = y;
    }

    void  draw(Graphics g){
        g.drawImage(Tools.getImage("blood.png"), x,y,null);

    }



}
