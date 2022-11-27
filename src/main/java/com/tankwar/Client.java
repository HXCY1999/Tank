package com.tankwar;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static com.sun.javafx.application.PlatformImpl.startup;

public class Client extends JComponent {

    private static final Client INSTANCE = new Client();

    public static Client getInstance(){
        return INSTANCE;
    }

    private Tank playTank;

    public Tank getPlayTank() {
        return playTank;
    }

    private  ArrayList<Tank> enemyTank;

    private final ArrayList<Wall> walls;

    private List<Missile> missiles;

    private List<Explosion> explosions;

    public ArrayList<Wall> getWalls() {
        return walls;
    }

    public ArrayList<Tank> getEnemyTank() {
        return enemyTank;
    }

    public List<Missile> getMissiles() {
        return missiles;
    }

    void addExplosion(Explosion explosion){
        explosions.add(explosion);
    }

    synchronized void removeMissile(Missile missile){
        missiles.remove(missile);
    }

    synchronized void removeEnemyTank(Tank enemyTank) {
        this.enemyTank.remove(enemyTank);
    }

    synchronized void add(Missile missile){
        missiles.add(missile);
    }

    public Client() {
        this.playTank = new Tank(400,100,Direction.DOWN,false);
        this.walls = new ArrayList<>();
        // the missile object run in two thread:main and paintComponent thread
        // so use this list to keep its thread safety
        //because the missile is modifying in main thread and be written in another thread
        this.missiles = new CopyOnWriteArrayList<>();
        this.explosions = new ArrayList<>();
        walls.add(new Wall(200,140,true,15));
        walls.add(new Wall(200,540,true,15));
        walls.add(new Wall(100,80,false,15));
        walls.add(new Wall(700,80,false,15));
        //initiate the enemy tanks
        initiateEnemyTank();
        //set the window dimension
        this.setPreferredSize(new Dimension(800,600));
    }

    private void initiateEnemyTank() {
        this.enemyTank = new ArrayList<>(12);
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 4; j++) {
                this.enemyTank.add(new Tank
                        (200 + j * 80, 400 + 40 * i, Direction.UP, true));
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        //draw background
        g.setColor(Color.BLACK);
        g.fillRect(0,0,800,600);

        if(!playTank.isLive()){
            g.setColor(Color.RED );
            g.setFont(new Font(null,Font.BOLD,100));
            g.drawString("GAME OVER",100,200);
            g.setFont(new Font(null,Font.BOLD,60));
            g.drawString("PRESS F2 TO RESTART",60,360);
        }else {
            //draw player tank
            if (playTank.isLive()) playTank.draw(g);
            //draw enemy tanks
            if (enemyTank.isEmpty()) initiateEnemyTank(); // if all tanks die, re-initiate them
            enemyTank.removeIf(tank -> !tank.isLive());//when tank die remove from list
            for (Tank tank : enemyTank) {
                tank.draw(g);
            }
            //draw all the walls
            for (Wall wall : walls) {
                wall.draw(g);
            }

//        System.out.println(Thread.currentThread().getName());
            //draw every missile
            missiles.removeIf(missile -> !missile.isLive());//missile die remove from list
            for (Missile missile : missiles) {
                missile.draw(g);
            }
            //draw explosion
            explosions.removeIf(explosion -> !explosion.isLive());
            for (Explosion explosion : explosions) {
                explosion.draw(g);
            }
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
        while (true) {
//            System.out.println(Thread.currentThread().getName());

            client.repaint();
            if (client.playTank.isLive()) {
                for (Tank enemyTank : client.enemyTank) {
                    enemyTank.addRandomlyMove();
                }
                Thread.sleep(50);
            }
        }
    }

    public void restart() {
        if (!playTank.isLive() ) {
            playTank = new Tank(400,100,Direction.DOWN,false);
        }
        this.initiateEnemyTank();
    }
}
