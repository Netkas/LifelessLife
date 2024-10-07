package me.netkas.lifelesslife.records;

import me.netkas.lifelesslife.enums.CardinalDirection;

/**
 * Represents a point in a 2D coordinate system.
 */
public record Point(int x, int y)
{
    /**
     * Returns a new Point object representing the direction based on the given CardinalDirection.
     *
     * @param direction the CardinalDirection representing the desired direction
     * @return a new Point object representing the direction
     */
    public Point toDirection(me.netkas.lifelesslife.enums.CardinalDirection direction)
    {
        return switch (direction)
        {
            case NORTH -> new Point(x, y - 1);
            case EAST -> new Point(x + 1, y);
            case SOUTH -> new Point(x, y + 1);
            case WEST -> new Point(x - 1, y);
        };
    }

    /**
     * Returns a new Point object representing the direction based on the given CardinalDirection and distance.
     *
     * @param direction the CardinalDirection representing the desired direction
     * @param distance the distance to move in the specified direction
     * @return a new Point object representing the direction and distance
     */
    public Point toDirection(CardinalDirection direction, int distance)
    {
        return switch (direction)
        {
            case NORTH -> new Point(x, y - distance);
            case EAST -> new Point(x + distance, y);
            case SOUTH -> new Point(x, y + distance);
            case WEST -> new Point(x - distance, y);
        };
    }

    /**
     * Compares this Point object with the specified object for equality.
     *
     * @param obj the object to compare
     * @return {@code true} if the specified object is a Point and has the same x and y values as this Point, {@code false} otherwise.
     */
    @Override
    public boolean equals(Object obj)
    {
        if (obj instanceof Point pt)
        {
            return (x == pt.x) && (y == pt.y);
        }

        return false;
    }

    /**
     * Returns a `String` representation of the `Point` object.
     *
     * @return a `String` representation of the `Point` object
     */
    @Override
    public String toString()
    {
        return "[x=" + x + ",y=" + y + "]";
    }
}
