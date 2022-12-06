package com.tankwar;

import java.awt.*;

public class AidKit {

    private  int x ,y;

    private final Image image = Tools.getImage("blood.png");;

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


    void  draw(Graphics g){
        g.drawImage(image, x,y,null);

    }


    public Rectangle getRectangle() {
        return new Rectangle(x,y,image.getWidth(null),image.getHeight(null));
    }
}
