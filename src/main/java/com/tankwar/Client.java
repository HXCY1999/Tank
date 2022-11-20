package com.tankwar;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.sun.javafx.application.PlatformImpl.startup;

public class Client extends JComponent {

    private static final Client INSTANCE = new Client();

    public static Client getInstance(){
        return INSTANCE;
    }

    private final Tank playTank;

    private final ArrayList<Tank> enemyTank;

    private final ArrayList<Wall> walls;

    private List<Missile> missiles;

    public ArrayList<Wall> getWalls() {
        return walls;
    }

    public ArrayList<Tank> getEnemyTank() {
        return enemyTank;
    }

    public List<Missile> getMissiles() {
        return missiles;
    }

    public Client() {
        this.playTank = new Tank(400,100,Direction.DOWN,false);
        this.enemyTank = new ArrayList<>(12);
        this.walls = new ArrayList<>();
        this.missiles = new ArrayList<>();
        walls.add(new Wall(200,140,true,15));
        walls.add(new Wall(200,540,true,15));
        walls.add(new Wall(100,80,false,15));
        walls.add(new Wall(700,80,false,15));
        //initiate the enemy tanks
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 4; j++) {
                this.enemyTank.add(new Tank
                        (200 + j* 80, 400 + 40 * i,Direction.UP,true));
            }
        }
        //set the window dimension
        this.setPreferredSize(new Dimension(800,600));
    }

    @Override
    protected void paintComponent(Graphics g) {
        //draw background
        g.setColor(Color.cyan);
        g.fillRect(0,0,800,600);
        //draw player tank
        playTank.draw(g);
        //draw enemy tank
        for (Tank tank : enemyTank) {
            tank.draw(g);
        }
        //draw wall
        for (Wall wall: walls){
            wall.draw(g);
        }
        for (Missile missile : missiles){
            missile.draw(g);
        }
    }

    public static void main(String[] args) throws InterruptedException {
        com.sun.javafx.application.PlatformImpl.startup(()->{});
        JFrame frame = new JFrame();
        frame.setTitle("The most boring Tank Game!");
        //add icon to frame window
        frame.setIconImage(new ImageIcon("assets/images/icon.png").getImage());
        //add window to frame
        Client client =Client.getInstance();
        frame.add(client);
        //let the frame adapt to the window dimension
        frame.pack();
        //set frame visible
        frame.setVisible(true);
        //set frame to the center of window
        frame.setLocationRelativeTo(null);
        //add keyboard listener
        frame.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                client.playTank.keyPressed(e);
            }
            @Override
            public void keyReleased(KeyEvent e) {
                client.playTank.keyReleased(e);
            }
        });
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);//end the program as the window closes
        while (true){
            client.repaint();
            Thread.sleep(50);
        }
    }
}
