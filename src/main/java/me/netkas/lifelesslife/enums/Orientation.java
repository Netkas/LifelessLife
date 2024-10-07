package me.netkas.lifelesslife.enums;

import java.util.Random;

public enum Orientation
{
    HORIZONTAL("horizontal"),
    VERTICAL("vertical");

    private final String name;

    /**
     * Represents the orientation of an object.
     * An orientation can be either HORIZONTAL or VERTICAL.
     */
    Orientation(String name)
    {
        this.name = name;
    }

    /**
     * Returns the name associated with the Orientation.
     *
     * @return the name associated with the Orientation
     */
    public String getName()
    {
        return name;
    }

    /**
     * Returns the opposite orientation of the current Orientation.
     *
     * @return the opposite orientation
     */
    public Orientation getOpposite()
    {
        return this == HORIZONTAL ? VERTICAL : HORIZONTAL;
    }

    /**
     * Returns the CardinalDirection to the left of the current Orientation.
     *
     * @return the CardinalDirection to the left
     */
    public CardinalDirection getLeft()
    {
        return this == HORIZONTAL ? CardinalDirection.NORTH : CardinalDirection.WEST;
    }

    /**
     * Returns the cardinal direction to the right of the current orientation.
     *
     * @return the cardinal direction to the right
     */
    public CardinalDirection getRight()
    {
        return this == HORIZONTAL ? CardinalDirection.SOUTH : CardinalDirection.EAST;
    }

    /**
     * Generates a random CardinalDirection based on the given Random object.
     *
     * @param random the Random object used to generate the random direction
     * @return a random CardinalDirection
     * @throws IllegalStateException if the generated value is unexpected
     */
    public CardinalDirection random(Random random)
    {
        return switch(random.nextInt(2))
        {
            case 0 -> this == HORIZONTAL ? CardinalDirection.WEST : CardinalDirection.NORTH;
            case 1 -> this == HORIZONTAL ? CardinalDirection.EAST : CardinalDirection.SOUTH;
            default -> throw new IllegalStateException("Unexpected value: " + random.nextInt(2));
        };
    }

    /**
     * Generates a random CardinalDirection based on the given Random object.
     *
     * @return a random CardinalDirection
     * @throws IllegalStateException if the generated value is unexpected
     */
    public CardinalDirection random()
    {
        return random(new Random());
    }
}
