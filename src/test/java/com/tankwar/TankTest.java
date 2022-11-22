package com.tankwar;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TankTest {

    @Test
    void getImage() { // test if can get the image
        for (Direction direction : Direction.values()){
            Tank tank = new Tank(0,0,direction,false);
            assertTrue(tank.getImage().getWidth(null) > 0,
                    direction + " cant get valid image!");

            Tank enemyTank = new Tank(0,0,direction,true);
            assertTrue(tank.getImage().getWidth(null) > 0,
                    direction + "get get valid image!");

        }

    }
}