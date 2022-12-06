package com.tankwar;

import com.alibaba.fastjson.JSON;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class Client extends JComponent {

    private static final Client INSTANCE = new Client();

    public static Client getInstance(){
        return INSTANCE;
    }

    private AidKit aidKit;

    private Tank playTank;

    public Tank getPlayTank() {
        return playTank;
    }

    private final AtomicInteger enemyKilled = new AtomicInteger(0);

    private CopyOnWriteArrayList<Tank> enemyTank;

    private final ArrayList<Wall> walls;

    private final List<Missile> missiles;

    private final List<Explosion> explosions;

    public ArrayList<Wall> getWalls() {
        return walls;
    }

    public CopyOnWriteArrayList<Tank> getEnemyTank() {
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

    public AidKit getAidKit() {
        return aidKit;
    }

    public Client() {
        this.playTank = new Tank(400,100,Direction.DOWN,false);
        this.walls = new ArrayList<>();
        this.aidKit = new AidKit(400,250,true);
        // the missile object run in two thread:main and paintComponent thread
        // so use this list to keep its thread safety
        //because the missile is modifying in main thread and be written in another thread
        this.missiles = new CopyOnWriteArrayList<>();
        this.explosions = new ArrayList<>();
        walls.add(new Wall(280,140,true,12));
        walls.add(new Wall(280,540,true,12));
        walls.add(new Wall(100,160,false,12));
        walls.add(new Wall(700,160,false,12));
        //initiate the enemy tanks
        initiateEnemyTank();
        //set the window dimension
        this.setPreferredSize(new Dimension(800,600));
    }

    private void initiateEnemyTank() {
        this.enemyTank = new CopyOnWriteArrayList<Tank>();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 4; j++) {
                this.enemyTank.add(new Tank
                        (200 + j * 80, 400 + 40 * i, Direction.UP, true));
            }
        }
    }

    private static final Random  RANDOM = new Random();
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
            g.setColor(Color.white);
            g.setFont(new Font(null,Font.BOLD,16));
            g.drawString("Missiles: " + missiles.size(),10,50);
            g.drawString("Explosions: " + explosions.size(),10,70);
            g.drawString("Player Tank HP: " + playTank.getHP(),10,90);
            g.drawString("Enemy left: " + enemyTank.size(),10,110);
            g.drawString("Enemy killed " + enemyKilled.get(),10,130);
            g.drawImage(Tools.getImage("tree.png"),720,10,null);
            g.drawImage(Tools.getImage("tree.png"),10,520,null);

            //if tank is dying we need a aid kit.
            //randomly appear can manually control Probability
            if(playTank.isDying() && RANDOM.nextInt(4) < 3){
                aidKit.setLive(true);
            }

            //draw aid kit
            if(aidKit.isLive() ) aidKit.draw(g);
            //draw player tank
            if (playTank.isLive()) playTank.draw(g);
            //draw enemy tanks
            if (enemyTank.isEmpty()) initiateEnemyTank(); // if all tanks die, re-initiate them

            int count = enemyTank.size();
            enemyTank.removeIf(tank -> !tank.isLive());//when tank die remove from list
            enemyKilled.addAndGet(count - enemyTank.size());

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

        //
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                try {
                    client.save();
                } catch (IOException ioException) {
                    JOptionPane.showMessageDialog(null,
                            "Fail to save current game","Oops! Error Occurred!",JOptionPane.ERROR_MESSAGE);
                }
                System.exit(0);
            }
        });


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
            try {
                client.repaint();
                if (client.playTank.isLive()) {
                    for (Tank enemyTank : client.enemyTank) {
                        enemyTank.addRandomlyMove();
                    }
                    Thread.sleep(50);
                }
            }catch (Exception e){
                e.printStackTrace();
            }

        }
    }

     void save() throws IOException {
        Save save = new Save(playTank.isLive(), playTank.getPosition(),
                enemyTank.stream().filter(e -> !e.isLive())
                        .map(Tank::getPosition).collect(Collectors.toList()));

        try(PrintWriter out =
                    new PrintWriter(new BufferedWriter(new FileWriter("game.sav")))){
            out.println(JSON.toJSONString(save,true));
        }
    }

    public void restart() {
        if (!playTank.isLive() ) {
            playTank = new Tank(400,100,Direction.DOWN,false);
        }
        this.initiateEnemyTank();
    }
}
