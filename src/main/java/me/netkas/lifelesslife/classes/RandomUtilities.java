package me.netkas.lifelesslife.classes;

import java.util.Random;

public final class RandomUtilities
{
    private static final int PRECISION = 10000;

    /**
     * Determines if an event occurs based on a percentage chance.
     * @param chance The percentage chance (0-100)
     * @param random The Random instance to use
     * @return true if the event occurs
     * @throws IllegalArgumentException if chance is not between 0 and 100
     */
    public static boolean randomChance(int chance, Random random)
    {
        if (chance < 0 || chance > 100)
        {
            throw new IllegalArgumentException("Chance must be between 0 and 100, got: " + chance);
        }

        // Handle edge cases explicitly
        if (chance == 0) return false;
        if (chance == 100) return true;

        // Convert percentage to our precision scale
        int scaledChance = chance * (PRECISION / 100);
        return random.nextInt(PRECISION) < scaledChance;
    }

    /**
     * More precise version that accepts floating-point chances.
     * @param chance The percentage chance (0.0-100.0)
     * @param random The Random instance to use
     * @return true if the event occurs
     * @throws IllegalArgumentException if chance is not between 0 and 100
     */
    public static boolean randomChance(double chance, Random random)
    {
        if (chance < 0.0 || chance > 100.0)
        {
            throw new IllegalArgumentException("Chance must be between 0.0 and 100.0, got: " + chance);
        }

        // Handle edge cases explicitly
        if (chance == 0.0) return false;
        if (chance == 100.0) return true;

        // Convert percentage to our precision scale
        int scaledChance = (int)(chance * (PRECISION / 100.0));
        return random.nextInt(PRECISION) < scaledChance;
    }

}
