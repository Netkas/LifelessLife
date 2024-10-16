package me.netkas.lifelesslife.classes;

import java.util.Random;

public final class RandomGenerator
{
    private final static Random instance = new Random();

    /**
     * Returns the instance of the Random class.
     *
     * @return The instance of RandomGenerator.
     */
    public static Random getInstance()
    {
        return instance;
    }
}
