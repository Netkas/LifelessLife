package me.netkas.lifelesslife.objects;

import me.netkas.lifelesslife.abstracts.AreaRegion;
import me.netkas.lifelesslife.enums.AreaRegionType;
import me.netkas.lifelesslife.enums.CardinalDirection;
import me.netkas.lifelesslife.objects.point_region.LineRegion;
import me.netkas.lifelesslife.objects.point_region.PointRegion;
import me.netkas.lifelesslife.records.Point;

import java.util.*;

public class AreaChunk
{
    private final int height;
    private final int width;
    private final Map<AreaRegionType, List<AreaRegion>> regions;

    public AreaChunk(int height, int width)
    {
        this.height = height;
        this.width = width;
        this.regions = new HashMap<>();
    }

    public int getHeight()
    {
        return height;
    }

    public int getWidth()
    {
        return width;
    }

    public List<AreaRegion> getRegions()
    {
        List<AreaRegion> allRegions = new ArrayList<>();
        for (List<AreaRegion> regionList : regions.values())
        {
            allRegions.addAll(regionList);
        }

        return allRegions;
    }

    public List<AreaRegion> getRegions(AreaRegionType type)
    {
        return regions.getOrDefault(type, new ArrayList<>());
    }

    public List<AreaRegion> getRegions(Point point)
    {
        List<AreaRegion> pointRegions = new ArrayList<>();
        for (AreaRegion region : getRegions())
        {
            if (region.getRegion().contains(point))
            {
                pointRegions.add(region);
            }
        }

        return pointRegions;
    }

    public void addRegion(AreaRegion region)
    {
        AreaRegionType type = region.getType();
        regions.putIfAbsent(type, new ArrayList<>());
        regions.get(type).add(region);
    }

    public void removeRegion(AreaRegion region)
    {
        AreaRegionType type = region.getType();
        regions.get(type).remove(region);
    }

    public boolean regionExists(AreaRegion region)
    {
        for (AreaRegion r : regions.getOrDefault(region.getType(), new ArrayList<>()))
        {
            if (r.getRegion().contains(region))
            {
                return true;
            }
        }

        return false;
    }

    public boolean regionExists(Point point)
    {
        for (AreaRegion region : getRegions())
        {
            if (region.getRegion().contains(point))
            {
                return true;
            }
        }

        return false;
    }

    public boolean regionTypeExists(AreaRegionType type)
    {
        return this.regions.containsKey(type);
    }

    public boolean regionTypeExists(Point point, AreaRegionType type)
    {
        for (AreaRegion region : getRegions(type))
        {
            if (region.getRegion().contains(point))
            {
                return true;
            }
        }

        return false;
    }

    public boolean regionTypeExists(Point point, List<AreaRegionType> types)
    {
        for (AreaRegionType type : types)
        {
            if (regionTypeExists(point, type))
            {
                return true;
            }
        }

        return false;
    }

    public LineRegion getEdge(CardinalDirection direction)
    {
        return switch (direction)
        {
            case NORTH -> new LineRegion(new Point(0, 0), new Point(width, 0));
            case EAST -> new LineRegion(new Point(width, 0), new Point(width, height));
            case SOUTH -> new LineRegion(new Point(0, height), new Point(width, height));
            case WEST -> new LineRegion(new Point(0, 0), new Point(0, height));
        };
    }

    public double getRegionUsage(AreaRegion region)
    {
        int totalArea = height * width;
        int usedArea = region.getRegion().size();

        return ((double) usedArea / totalArea) * 100;
    }

    public double getRegionUsage(AreaRegionType type)
    {
        int totalArea = height * width;
        int usedArea = 0;

        for (AreaRegion region : regions.getOrDefault(type, new ArrayList<>()))
        {
            usedArea += region.getRegion().size();
        }

        return ((double) usedArea / totalArea) * 100;
    }

    public AreaRegion getRandomRegion(Random random)
    {
        List<AreaRegion> allRegions = getRegions();
        return allRegions.get(random.nextInt(allRegions.size()));
    }

    public AreaRegion getRandomRegion(AreaRegionType type, Random random)
    {
        List<AreaRegion> typeRegions = getRegions(type);
        return typeRegions.get(random.nextInt(typeRegions.size()));
    }

    public boolean inBounds(Point point)
    {
        return point.x() >= 0 && point.x() < this.width && point.y() >= 0 && point.y() < this.height;
    }

    public boolean isCorner(Point point)
    {
        return (point.x() == 0 && point.y() == 0) || (point.x() == width - 1 && point.y() == 0) || (point.x() == 0 && point.y() == height - 1) || (point.x() == width - 1 && point.y() == height - 1);
    }

    public boolean isEdge(Point point)
    {
        return point.x() == 0 || point.y() == 0 || point.x() == width - 1 || point.y() == height - 1;
    }

    public LineRegion traverseDirection(Point point, CardinalDirection direction, int distance, boolean includeStart)
    {
        final int startX = point.x();
        final int startY = point.y();
        int endX = startX;
        int endY = startY;

        switch (direction)
        {
            case NORTH -> endY = Math.max(startY - distance, 0);
            case EAST -> endX = Math.min(startX + distance, width - 1);
            case SOUTH -> endY = Math.min(startY + distance, height - 1);
            case WEST -> endX = Math.max(startX - distance, 0);
        }

        Point start = includeStart ? point : switch (direction)
        {
            case NORTH -> new Point(startX, Math.max(startY - 1, 0));
            case EAST -> new Point(Math.min(startX + 1, width - 1), startY);
            case SOUTH -> new Point(startX, Math.min(startY + 1, height - 1));
            case WEST -> new Point(Math.max(startX - 1, 0), startY);
        };

        return new LineRegion(start, new Point(endX, endY));
    }

    public LineRegion traverseDirection(Point point, CardinalDirection direction, boolean includeStart)
    {
        return switch (direction)
        {
            case NORTH -> traverseDirection(point, CardinalDirection.NORTH, height, includeStart);
            case EAST -> traverseDirection(point, CardinalDirection.EAST, width, includeStart);
            case SOUTH -> traverseDirection(point, CardinalDirection.SOUTH, height, includeStart);
            case WEST -> traverseDirection(point, CardinalDirection.WEST, width, includeStart);
        };
    }

    public PointRegion getNeighbors(Point point, CardinalDirection direction, int distance)
    {
        List<Point> points = new ArrayList<>();
        for(Point traversedPoint : this.traverseDirection(point, direction.getLeft(), distance, false).getPoints())
        {
            if(!this.inBounds(traversedPoint))
            {
                break;
            }

            points.add(traversedPoint);
        }

        for(Point traversedPoint : this.traverseDirection(point, direction.getRight(), distance, false).getPoints())
        {
            if(!this.inBounds(traversedPoint))
            {
                break;
            }

            points.add(traversedPoint);
        }

        points.add(point);
        return new PointRegion(points);
    }

    public Point getCenter()
    {
        return new Point(width / 2, height / 2);
    }

}
