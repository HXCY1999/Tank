package com.tankwar;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.swing.text.Position;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Save {

    private boolean GameContinued;

    private Position position;

    private List<Position> enemyPosition;


    //lombok plugin
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Position{
        private int x, y;
        private Direction direction;

    }

}
