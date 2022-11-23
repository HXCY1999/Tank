package com.tankwar;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class Tools {

     static Image getImage(String imageName){
        return new ImageIcon("assets/images/"+imageName).getImage();
    }

    static void playAudio(String fineName) {
        Media sound = new Media(new File("assets/audios/"+fineName).toURI().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(sound);
        mediaPlayer.play();
    }
}
