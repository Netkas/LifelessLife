package me.netkas.lifelesslife.objects.point_region;

import me.netkas.lifelesslife.abstracts.AreaRegion;
import me.netkas.lifelesslife.enums.Orientation;
import me.netkas.lifelesslife.interfaces.RegionInterface;
import me.netkas.lifelesslife.records.Point;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public record LineRegion(Point start, Point end) implements RegionInterface
{
    /**
     * Represents a region defined by a line between two points.
     */
    public LineRegion
    {
        if (start.x() != end.x() && start.y() != end.y())
        {
            throw new IllegalArgumentException("The points must be aligned horizontally or vertically.");
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
        return true;
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
        return this.start.x() == this.end.x() ? Orientation.VERTICAL : Orientation.HORIZONTAL;
    }

    /**
     * Returns the height of the region defined by a list of connected points.
     *
     * @return the height of the region
     */
    @Override
    public int getHeight()
    {
        return this.getOrientation() == Orientation.VERTICAL ? Math.abs(this.start.y() - this.end.y()) : 0;
    }

    /**
     * Returns the width of the region defined by a list of connected points.
     *
     * @return the width of the region
     */
    @Override
    public int getWidth()
    {
        return this.getOrientation() == Orientation.HORIZONTAL ? Math.abs(this.start.x() - this.end.x()) : 0;
    }

    /**
     * Returns a list of connected points that define a region.
     *
     * @return a list of connected points that define a region
     */
    @Override
    public List<Point> getPoints() {
        List<Point> points = new ArrayList<>();

        if (this.getOrientation() == Orientation.VERTICAL)
        {
            for (int y = Math.min(this.start.y(), this.end.y()); y <= Math.max(this.start.y(), this.end.y()); y++)
            {
                points.add(new Point(this.start.x(), y));
            }
        }
        else
        {
            for (int x = Math.min(this.start.x(), this.end.x()); x <= Math.max(this.start.x(), this.end.x()); x++)
            {
                points.add(new Point(x, this.start.y()));
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
        List<Point> points = new ArrayList<>();

        if (this.getOrientation() == Orientation.VERTICAL)
        {
            for (int y = Math.min(this.start.y(), this.end.y()); y <= Math.max(this.start.y(), this.end.y()); y++)
            {
                points.add(new Point(this.start.x(), y));
            }
        }
        else
        {
            for (int x = Math.min(this.start.x(), this.end.x()); x <= Math.max(this.start.x(), this.end.x()); x++)
            {
                points.add(new Point(x, this.start.y()));
            }
        }

        return points;
    }

    public List<Point> getPointsOrdered(Point startFrom)
    {
        if (startFrom == null) {
            return this.getPointsOrdered();
        }

        List<Point> points = new ArrayList<>();
        boolean found = false;

        if (this.getOrientation() == Orientation.VERTICAL)
        {
            int yStart = this.start.y();
            int yEnd = this.end.y();

            // Check the direction of traversal (bottom-to-top or top-to-bottom)
            if (yStart > yEnd) {
                for (int y = yStart; y >= yEnd; y--) {
                    Point point = new Point(this.start.x(), y);
                    if (found || point.equals(startFrom)) {
                        points.add(point);
                        found = true;
                    }
                }
            } else {
                for (int y = yStart; y <= yEnd; y++) {
                    Point point = new Point(this.start.x(), y);
                    if (found || point.equals(startFrom)) {
                        points.add(point);
                        found = true;
                    }
                }
            }
        }
        else
        {
            int xStart = this.start.x();
            int xEnd = this.end.x();

            // Check the direction of traversal (right-to-left or left-to-right)
            if (xStart > xEnd) {
                for (int x = xStart; x >= xEnd; x--) {
                    Point point = new Point(x, this.start.y());
                    if (found || point.equals(startFrom)) {
                        points.add(point);
                        found = true;
                    }
                }
            } else {
                for (int x = xStart; x <= xEnd; x++) {
                    Point point = new Point(x, this.start.y());
                    if (found || point.equals(startFrom)) {
                        points.add(point);
                        found = true;
                    }
                }
            }
        }

        return points;
    }
    
    /**
     * Checks if the given point is contained within the region.
     *
     * @param point the point to check
     * @return true if the point is contained within the region, false otherwise
     */
    @Override
    public boolean contains(Point point)
    {
        if (getOrientation() == Orientation.VERTICAL)
        {
            return point.x() == start.x() && point.y() >= Math.min(start.y(), end.y()) && point.y() <= Math.max(start.y(), end.y());
        }
        else // HORIZONTAL
        {
            return point.y() == start.y() && point.x() >= Math.min(start.x(), end.x()) && point.x() <= Math.max(start.x(), end.x());
        }
    }

    /**
     * Determines if the LineRegion contains the specified region.
     *
     * @param region the RegionInterface object to check
     * @return true if the LineRegion contains the specified region, false otherwise
     */
    @Override
    public boolean contains(RegionInterface region)
    {
        if (region instanceof BoundingBoxRegion other)
        {
            return this.contains(other.topLeft()) || this.contains(other.bottomRight());
        }
        else if (region instanceof LineRegion other)
        {
            // Check if the lines are aligned and overlap
            if (this.getOrientation() == other.getOrientation())
            {
                if (this.getOrientation() == Orientation.VERTICAL && this.start.x() == other.start.x())
                {
                    return !(other.start.y() > this.end.y() || other.end.y() < this.start.y());
                }
                else if (this.getOrientation() == Orientation.HORIZONTAL && this.start.y() == other.start.y())
                {
                    return !(other.start.x() > this.end.x() || other.end.x() < this.start.x());
                }
            }
            else
            {
                return this.contains(other.start()) || this.contains(other.end());
            }
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
     * Determines if the region contains the specified area region.
     *
     * @param areaRegion the AreaRegion object to check
     * @return true if the region contains the specified area region, false otherwise
     */
    @Override
    public boolean contains(AreaRegion areaRegion)
    {
        return this.contains(areaRegion.getRegion());
    }

    /**
     * Returns a random point from the list of points.
     *
     * @param random the RandomGenerator object used to generate the random index
     * @return a random Point object from the list
     */
    @Override
    public Point getRandom(Random random)
    {
        int minY = Math.min(this.start.y(), this.end.y());
        int maxY = Math.max(this.start.y(), this.end.y());
        int minX = Math.min(this.start.x(), this.end.x());
        int maxX = Math.max(this.start.x(), this.end.x());

        if (this.getOrientation() == Orientation.VERTICAL)
        {
            return new Point(this.start.x(), random.nextInt((maxY - minY + 1)) + minY);
        }
        else
        {
            return new Point(random.nextInt((maxX - minX + 1)) + minX, this.start.y());
        }
    }

    /**
     * Returns a random Point object from the RegionInterface.
     *
     * @return a random Point object
     */
    @Override
    public Point getRandom()
    {
        return this.getRandom(new Random());
    }

    /**
     * Returns the size of the region defined by a list of connected points.
     *
     * @return the size of the region
     */
    @Override
    public int size()
    {
        // If the line is vertical, the size is the difference in y-coordinates plus one
        if (getOrientation() == Orientation.VERTICAL)
        {
            return Math.abs(end.y() - start.y()) + 1;
        }

        // If the line is horizontal, the size is the difference in x-coordinates plus one
        return Math.abs(end.x() - start.x()) + 1;
    }

    /**
     * Retrieves the starting point of the LineRegion.
     *
     * @return the starting point of the LineRegion
     */
    @Override
    public Point start()
    {
        return this.start;
    }

    /**
     * Retrieves the end point of a line region.
     *
     * @return the end point of the line region
     */
    @Override
    public Point end()
    {
        return this.end;
    }
}
