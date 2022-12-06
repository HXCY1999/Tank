package com.tankwar;

import org.junit.jupiter.api.Test;

import java.io.IOException;

public class ClientTest {

    @Test
    void save() throws IOException {
        Client.getInstance().save();
    }


}
