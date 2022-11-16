package com.tankwar;

import javax.swing.*;
import java.awt.*;

public class Wall {
    private final int x;

    private final int y;

    private final boolean horizontal;

    private final int bricks;

    private final Image brickImage;

    public Wall(int x, int y, boolean horizontal, int bricks) {
        this.brickImage = Tools.getImage("brick.png");
        this.x = x;
        this.y = y;
        this.horizontal = horizontal;
        this.bricks = bricks;
    }

    public Rectangle getRectangle(){
        return horizontal ? new Rectangle(x,y,bricks * brickImage.getWidth(null),
                brickImage.getHeight(null)) : new Rectangle(x,y,brickImage.getWidth(null),
                brickImage.getHeight(null) *  bricks);
    }

    public void draw(Graphics g){
        if(horizontal){
            for (int i = 0; i < bricks; i++) {
                g.drawImage(brickImage,x + i * brickImage.getWidth(null),
                        y,null);
            }
        }else {
            for (int i = 0; i < bricks; i++) {
                g.drawImage(brickImage,x,
                        y+ i * brickImage.getWidth(null),null);
            }
        }
    }

}
