package me.netkas.lifelesslife;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class Game
{
    private final ScheduledExecutorService scheduler;
    private boolean running;
    private static final Logger logger = Logger.getLogger(Game.class.getName());

    /**
     * Constructs a new Game instance.
     * This initializes the scheduler with a single-thread pool and sets the game state to not running.
     */
    public Game()
    {
        this.scheduler = Executors.newScheduledThreadPool(1);
        this.running = false;
    }

    /**
     * Starts the game loop if it is not already running.
     * The game loop runs at a fixed rate of one second.
     * Invokes the update method at every tick of the scheduler.
     */
    public void start()
    {
        if (this.running)
        {
            return;
        }

        this.running = true;
        this.scheduler.scheduleAtFixedRate(this::update, 0, 1, TimeUnit.SECONDS);
    }

    /**
     * Stops the game process by shutting down the scheduler and setting the running state to false.
     * If the scheduler does not terminate within 60 seconds, it will attempt an immediate shutdown.
     * This method handles interruptions and ensures the scheduler is properly terminated.
     *
     * @throws IllegalStateException if the scheduler fails to terminate after an immediate shutdown.
     */
    public void stop()
    {
        if (!this.running)
        {
            return;
        }

        this.running = false;
        this.scheduler.shutdown();

        try
        {
            if(!scheduler.awaitTermination(60, TimeUnit.SECONDS))
            {
                scheduler.shutdownNow();

                if (!scheduler.awaitTermination(60, TimeUnit.SECONDS)) 
                {
                    throw new IllegalStateException("ScheduledExecutorService did not terminate after shutdownNow.");
                }
            }
        }
        catch (InterruptedException e)
        {
            scheduler.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Returns the current state of the game, indicating whether the game loop is running.
     *
     * @return true if the game is currently running, false otherwise.
     */
    public boolean isRunning()
    {
        return running;
    }

    private void update()
    {
        logger.info("Game update");
    }
}
