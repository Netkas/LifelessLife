package me.netkas.lifelesslife.objects.point_region;

import me.netkas.lifelesslife.abstracts.AreaRegion;
import me.netkas.lifelesslife.classes.RandomGenerator;
import me.netkas.lifelesslife.enums.Orientation;
import me.netkas.lifelesslife.interfaces.RegionInterface;
import me.netkas.lifelesslife.records.Point;

import java.util.*;

public final class PointRegion implements RegionInterface
{
    private final List<Integer> xCoords;
    private final List<Integer> yCoords;

    /**
     * Represents a region defined by a list of connected points in a 2D coordinate system.
     */
    public PointRegion(List<Point> points)
    {
        if (points.isEmpty())
        {
            throw new IllegalArgumentException("The list of points must not be empty.");
        }

        xCoords = new ArrayList<>(points.size());
        yCoords = new ArrayList<>(points.size());

        for (Point point : points)
        {
            xCoords.add(point.x());
            yCoords.add(point.y());
        }

        validatePoints(points);
    }

    /**
     * Represents a region defined by a list of connected points.
     */
    public PointRegion(List<Integer> xCoords, List<Integer> yCoords)
    {
        if (xCoords.isEmpty() || yCoords.isEmpty())
        {
            throw new IllegalArgumentException("The lists of coordinates must not be empty.");
        }

        if (xCoords.size() != yCoords.size())
        {
            throw new IllegalArgumentException("The lists of x and y coordinates must have the same size.");
        }

        this.xCoords = new ArrayList<>(xCoords);
        this.yCoords = new ArrayList<>(yCoords);

        List<Point> points = new ArrayList<>(xCoords.size());
        for (int i = 0; i < xCoords.size(); i++)
        {
            points.add(new Point(xCoords.get(i), yCoords.get(i)));
        }

        validatePoints(points);
    }

    /**
     * Validates a list of points that define a region.
     *
     * @param points the list of points to validate
     * @throws IllegalArgumentException if the points are not valid
     */
    private void validatePoints(List<Point> points)
    {
        if (points.size() > 1)
        {
            // Use a set to store visited points for uniqueness check
            Set<Point> uniquePoints = new HashSet<>(points.size());

            // Normalize the points relative to the first point
            Point firstPoint = points.getFirst();
            List<Point> normalizedPoints = points.stream()
                    .map(p -> new Point(p.x() - firstPoint.x(), p.y() - firstPoint.y()))
                    .toList();

            // Check for duplicates
            for (Point p : normalizedPoints)
            {
                if (!uniquePoints.add(p))
                {
                    throw new IllegalArgumentException("All points must be unique.");
                }
            }

            // Use a boolean array for visited check
            boolean[] visited = new boolean[normalizedPoints.size()];
            Deque<Integer> stack = new ArrayDeque<>();
            stack.push(0);

            while (!stack.isEmpty())
            {
                int current = stack.pop();
                if (!visited[current])
                {
                    visited[current] = true;
                    Point currentPoint = normalizedPoints.get(current);

                    // Check all other points for adjacency
                    for (int i = 0; i < normalizedPoints.size(); i++)
                    {
                        if (!visited[i] && isAdjacent(currentPoint, normalizedPoints.get(i)))
                        {
                            stack.push(i);
                        }
                    }
                }
            }

            for (boolean isVisited : visited)
            {
                if (!isVisited)
                {
                    throw new IllegalArgumentException("All points must be connected to each other.");
                }
            }
        }
    }

    /**
     * Checks whether two points are adjacent in a 2D coordinate system.
     *
     * @param p1 the first Point object
     * @param p2 the second Point object
     * @return {@code true} if the points are adjacent, {@code false} otherwise
     */
    private boolean isAdjacent(Point p1, Point p2)
    {
        return Math.abs(p1.x() - p2.x()) + Math.abs(p1.y() - p2.y()) == 1;
    }

    /**
     * Determines if the region defined by a list of connected points is a line.
     *
     * @return true if the region is a line, false otherwise
     */
    @Override
    public boolean isLine()
    {
        return xCoords.stream().distinct().count() == 1 || yCoords.stream().distinct().count() == 1;
    }

    /**
     * Returns the orientation of the region.
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

        return xCoords.get(0).equals(xCoords.get(1)) ? Orientation.VERTICAL : Orientation.HORIZONTAL;
    }

    /**
     * Returns the height of the region defined by a list of connected points.
     *
     * @return the height of the region
     */
    @Override
    public int getHeight()
    {
        return Collections.max(yCoords) - Collections.min(yCoords);
    }

    /**
     * Returns the width of the region defined by a list of connected points.
     *
     * @return the width of the region
     */
    @Override
    public int getWidth()
    {
        return Collections.max(xCoords) - Collections.min(xCoords);
    }

    /**
     * Returns a list of connected points that define a region.
     *
     * @return a list of connected points that define a region
     */
    @Override
    public List<Point> getPoints()
    {
        List<Point> points = new ArrayList<>(xCoords.size());
        for (int i = 0; i < xCoords.size(); i++)
        {
            points.add(new Point(xCoords.get(i), yCoords.get(i)));
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
        for (int i = 0; i < xCoords.size(); i++)
        {
            if (xCoords.get(i).equals(point.x()) && yCoords.get(i).equals(point.y()))
            {
                return true;
            }
        }

        return false;
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
        Set<Point> pointSet = new HashSet<>(this.getPoints());

        if (region instanceof BoundingBoxRegion other)
        {
            return pointSet.contains(other.topLeft()) || pointSet.contains(other.bottomRight());
        }
        else if (region instanceof LineRegion other)
        {
            return pointSet.contains(other.start()) || pointSet.contains(other.end());
        }
        else if (region instanceof PointRegion)
        {
            for (Point point : region.getPoints())
            {
                if (pointSet.contains(point))
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
        final int index = random.nextInt(xCoords.size());
        return new Point(xCoords.get(index), yCoords.get(index));
    }

    /**
     * Returns a random Point object from the list of points.
     *
     * @return a random Point object from the list of points
     */
    @Override
    public Point getRandom()
    {
        return getRandom(RandomGenerator.getInstance());
    }

    /**
     * Returns the size of the point list.
     *
     * @return the size of the point list
     */
    @Override
    public int size()
    {
        return xCoords.size();
    }
}
