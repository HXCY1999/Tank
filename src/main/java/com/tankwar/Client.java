package com.tankwar;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Client extends JComponent {
    private Tank playTank;

    public Client() {
        this.playTank = new Tank(400,100,Direction.DOWN);

        //set the window dimension
        this.setPreferredSize(new Dimension(800,600));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(this.playTank.getImage(),
                this.playTank.getX(), this.playTank.getY(), null);
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
                switch (e.getKeyCode()){
                    case KeyEvent.VK_UP:
                        client.playTank.setY(client.playTank.getY()-5);
                        break;
                    case KeyEvent.VK_DOWN:
                        client.playTank.setY(client.playTank.getY()+5);
                        break;
                    case KeyEvent.VK_LEFT:
                        client.playTank.setX(client.playTank.getX()-5);
                        break;
                    case KeyEvent.VK_RIGHT:
                        client.playTank.setX(client.playTank.getX()+5);
                        break;
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);//end the program as the window closes
        while (true){
            client.repaint();
        }
    }
}
