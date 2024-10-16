package me.netkas.lifelesslife;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {

    @Test
    public void testGameLoop()
    {
        int loops = 0;
        Game game = new Game();
        game.start();

        do
        {
            assertTrue(game.isRunning());
            loops += 1;

            try
            {
                Thread.sleep(500);
            }
            catch (InterruptedException e)
            {
                fail("Threading sleep failed", e);
            }

        } while (loops < 10);

        game.stop();
    }
}