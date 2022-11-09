package com.tankwar;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

public class Client extends JComponent {
    private Tank playTank;

    private ArrayList<Tank> enemyTank;

    public Client() {
        this.playTank = new Tank(400,100,Direction.DOWN,false);
        this.enemyTank = new ArrayList<>(12);
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
        playTank.draw(g);
        for (int i = 0; i < enemyTank.size(); i++){
            enemyTank.get(i).draw(g);
        }
    }

    public static void main(String[] args) throws InterruptedException {

        JFrame frame = new JFrame();
        frame.setTitle("The most boring Tank Game!");
        //add icon to frame window
        frame.setIconImage(new ImageIcon("assets/images/icon.png").getImage());
        //add window to frame
        Client client = new Client();
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
