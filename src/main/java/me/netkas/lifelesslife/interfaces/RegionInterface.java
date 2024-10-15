package me.netkas.lifelesslife.interfaces;


import me.netkas.lifelesslife.records.Point;

import java.util.List;
import java.util.Random;

public interface RegionInterface
{
    /**
     * Determines if the region defined by a list of connected points is a line.
     *
     * @return true if the region is a line, false otherwise
     */
    boolean isLine();

    /**
     * Retrieves the orientation of the region.
     *
     * @return the orientation of the region
     * @throws IllegalStateException if the region is not a line
     */
    me.netkas.lifelesslife.enums.Orientation getOrientation();

    /**
     * Returns the height of the region defined by a list of connected points.
     *
     * @return the height of the region
     */
    int getHeight();

    /**
     * Returns the width of the region defined by a list of connected points.
     *
     * @return the width of the region
     */
    int getWidth();

    /**
     * Returns a list of connected points that define a region.
     *
     * @return a list of connected points that define a region
     */
    List<Point> getPoints();

    /**
     * Returns a list of points ordered by their y-coordinate and x-coordinate in case of tie.
     *
     * @return a list of points ordered by their y-coordinate and x-coordinate
     */
    List<Point> getPointsOrdered();

    /**
     * Determines if the region contains the specified point.
     *
     * @param point the Point object to check
     * @return true if the region contains the point, false otherwise
     */
    boolean contains(Point point);

    /**
     * Determines if the region contains the specified region.
     *
     * @param region the RegionInterface object to check
     * @return true if the region contains the specified region, false otherwise
     */
    boolean contains(RegionInterface region);

    /**
     * Returns a random point from the list of points.
     *
     * @param random the RandomGenerator object used to generate the random index
     * @return a random Point object from the list
     */
    Point getRandom(Random random);

    /**
     * Returns a random Point object from the RegionInterface.
     *
     * @return a random Point object
     */
    Point getRandom();

    /**
     * Returns the size of the region defined by a list of connected points.
     *
     * @return the size of the region
     */
    int size();
}
