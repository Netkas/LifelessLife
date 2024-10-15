package me.netkas.lifelesslife.objects.point_region;


import me.netkas.lifelesslife.enums.Orientation;
import me.netkas.lifelesslife.interfaces.RegionInterface;
import me.netkas.lifelesslife.records.Point;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public record BoundingBoxRegion(Point topLeft, Point bottomRight) implements RegionInterface
{
    /**
     * Represents a region defined by a bounding box.
     * The region is defined by a top left point and a bottom right point.
     */
    public BoundingBoxRegion(Point topLeft, Point bottomRight)
    {
        this.topLeft = topLeft;
        this.bottomRight = bottomRight;

        if (topLeft.x() > bottomRight.x() || topLeft.y() > bottomRight.y())
        {
            throw new IllegalArgumentException("Top left point must be above and to the left of bottom right point.");
        }
    }

    /**
     * Determines if the region defined by a list of connected points is a line.
     *
     * @return true if the region is a line, false otherwise
     */
    @Override
    public boolean isLine()
    {
        return this.topLeft.x() == this.bottomRight.x() || this.topLeft.y() == this.bottomRight.y();
    }

    /**
     * Retrieves the orientation of the region.
     *
     * @return the orientation of the region
     * @throws IllegalStateException if the region is not a line
     */
    @Override
    public Orientation getOrientation()
    {
        if (!isLine())
        {
            throw new IllegalStateException("The region must be a line to determine its orientation.");
        }

        return this.topLeft.x() == this.bottomRight.x() ? Orientation.VERTICAL : Orientation.HORIZONTAL;
    }

    /**
     * Returns the height of the region defined by the top left and bottom right points.
     *
     * @return the height of the region
     */
    @Override
    public int getHeight()
    {
        return bottomRight.y() - topLeft.y();
    }

    /**
     * Returns the width of the region defined by a list of connected points.
     *
     * @return the width of the region
     */
    @Override
    public int getWidth()
    {
        return this.bottomRight.x() - this.topLeft.x();
    }

    /**
     * Retrieves a list of points that form a rectangular region defined by the top-left and bottom-right points.
     *
     * @return a list of points within the region
     */
    @Override
    public List<Point> getPoints()
    {
        List<Point> points = new ArrayList<>();

        for (int x = this.topLeft.x(); x <= this.bottomRight.x(); x++)
        {
            for (int y = this.topLeft.y(); y <= this.bottomRight.y(); y++)
            {
                points.add(new Point(x, y));
            }
        }

        return points;
    }

    /**
     * Returns a list of points ordered by their y-coordinate and x-coordinate in case of tie.
     *
     * @return a list of points ordered by their y-coordinate and x-coordinate
     */
    @Override
    public List<Point> getPointsOrdered()
    {
        List<Point> points = getPoints();
        points.sort((p1, p2) -> p1.y() == p2.y() ? Integer.compare(p1.x(), p2.x()) : Integer.compare(p1.y(), p2.y()));
        return points;
    }

    /**
     * Determines if the region contains the specified point.
     *
     * @param point the Point object to check
     * @return true if the region contains the point, false otherwise
     */
    @Override
    public boolean contains(Point point)
    {
        return point.x() >= this.topLeft.x() && point.x() <= this.bottomRight.x() &&
                point.y() >= this.topLeft.y() && point.y() <= this.bottomRight.y();
    }

    /**
     * Determines if the region contains the specified region.
     *
     * @param region the RegionInterface object to check
     * @return true if the region contains the specified region, false otherwise
     */
    @Override
    public boolean contains(RegionInterface region)
    {
        if (region instanceof BoundingBoxRegion other)
        {
            return !(other.topLeft.x() > this.bottomRight.x() || other.bottomRight.x() < this.topLeft.x() ||
                    other.topLeft.y() > this.bottomRight.y() || other.bottomRight.y() < this.topLeft.y());
        }
        else if (region instanceof LineRegion other)
        {
            return this.contains(other.start()) || this.contains(other.end());
        }
        else if (region instanceof PointRegion)
        {
            for (Point point : region.getPoints())
            {
                if (this.contains(point))
                {
                    return true;
                }
            }
        }

        return false;
    }


    /**
     * Returns a random point within the bounding box region.
     *
     * @param random the Random object used to generate the random coordinates
     * @return a Point object representing a random point within the bounding box region
     */
    @Override
    public Point getRandom(Random random)
    {
        return new Point(
                this.topLeft.x() + random.nextInt(this.getWidth() + 1),
                this.topLeft.y() + random.nextInt(this.getHeight() + 1)
        );
    }

    /**
     * Returns a randomly generated Point object within the bounding box defined by the top-left and bottom-right points.
     *
     * @return a randomly generated Point object
     */
    @Override
    public Point getRandom()
    {
        return getRandom(new Random());
    }

    /**
     * Returns the size of the region defined by the width and height.
     *
     * @return the size of the region
     */
    @Override
    public int size()
    {
        return this.getWidth() * this.getHeight();
    }

    /**
     * Returns the top left point of the bounding box region.
     *
     * @return the top left point of the bounding box region
     */
    @Override
    public Point topLeft()
    {
        return this.topLeft;
    }

    /**
     * Retrieves the bottom right point of the bounding box region.
     *
     * @return the bottom right point of the bounding box region
     */
    @Override
    public Point bottomRight()
    {
        return this.bottomRight;
    }
}