package me.netkas.lifelesslife.enums;

import javax.smartcardio.Card;
import java.util.Random;

public enum CardinalDirection
{
    /**
     * Represents the cardinal direction "north".
     */
    NORTH("north"),

    /**
     * Represents the cardinal direction "east".
     */
    EAST("east"),

    /**
     * Represents the cardinal direction "south".
     */
    SOUTH("south"),

    /**
     * Represents the cardinal direction "west".
     */
    WEST("west");

    private final String name;

    /**
     * Represents a cardinal direction (north, east, south, west).
     */
    CardinalDirection(String name)
    {
        this.name = name;
    }

    /**
     * Returns the name associated with the CardinalDirection.
     *
     * @return the name associated with the CardinalDirection
     */
    public String getName()
    {
        return name;
    }

    /**
     * Generates a random CardinalDirection enum value.
     *
     * @return a random CardinalDirection enum value
     */
    public static CardinalDirection random()
    {
        return values()[(int) (Math.random() * values().length)];
    }

    /**
     * Generates a random CardinalDirection enum value.
     *
     * @param random the RandomGenerator object used to generate the random value
     * @return a random CardinalDirection enum value
     */
    public static CardinalDirection random(Random random)
    {
        if (random == null)
        {
            return random();
        }

        return values()[random.nextInt(values().length)];
    }

    /**
     * Returns the cardinal direction that is to the left of the current cardinal direction.
     *
     * @return the cardinal direction to the left
     */
    public CardinalDirection getLeft()
    {
        return switch (this)
        {
            case NORTH -> WEST;
            case EAST -> NORTH;
            case SOUTH -> EAST;
            case WEST -> SOUTH;
        };
    }

    /**
     * Returns the cardinal direction that is to the right of the current cardinal direction.
     * The right direction is determined based on the current cardinal direction as follows:
     * - If the current cardinal direction is NORTH, the right direction is EAST.
     * - If the current cardinal direction is EAST, the right direction is SOUTH.
     * - If the current cardinal direction is SOUTH, the right direction is WEST.
     * - If the current cardinal direction is WEST, the right direction is NORTH.
     *
     * @return the cardinal direction that is to the right of the current cardinal direction
     */
    public CardinalDirection getRight()
    {
        return switch (this)
        {
            case NORTH -> EAST;
            case EAST -> SOUTH;
            case SOUTH -> WEST;
            case WEST -> NORTH;
        };
    }

    /**
     * Returns a random cardinal direction that is either to the left or right of the current cardinal direction.
     *
     * @param random the Random object used to generate the random value
     * @return a random cardinal direction that is either to the left or right of the current cardinal direction
     */
    public CardinalDirection getLeftOrRight(Random random)
    {
        return random.nextBoolean() ? getLeft() : getRight();
    }

    /**
     * Returns the opposite cardinal direction of the current cardinal direction.
     *
     * @return the opposite cardinal direction
     */
    public CardinalDirection opposite()
    {
        return switch (this)
        {
            case NORTH -> SOUTH;
            case EAST -> WEST;
            case SOUTH -> NORTH;
            case WEST -> EAST;
        };
    }
}
