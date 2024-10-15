package me.netkas.lifelesslife.objects.point_region;

import me.netkas.lifelesslife.enums.Orientation;
import me.netkas.lifelesslife.records.Point;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class BoundingBoxRegionTest {

    private BoundingBoxRegion region;
    private Point topLeft;
    private Point bottomRight;

    @BeforeEach
    void setUp() {
        topLeft = new Point(0, 0);
        bottomRight = new Point(5, 5);
        region = new BoundingBoxRegion(topLeft, bottomRight);
    }

    @Test
    void testConstructor() {
        Assertions.assertEquals(topLeft, region.topLeft());
        Assertions.assertEquals(bottomRight, region.bottomRight());

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new BoundingBoxRegion(new Point(5, 5), new Point(0, 0));
        });
    }

    @Test
    void testIsLine() {
        Assertions.assertFalse(region.isLine());

        BoundingBoxRegion horizontalLine = new BoundingBoxRegion(new Point(0, 0), new Point(5, 0));
        Assertions.assertTrue(horizontalLine.isLine());

        BoundingBoxRegion verticalLine = new BoundingBoxRegion(new Point(0, 0), new Point(0, 5));
        Assertions.assertTrue(verticalLine.isLine());
    }

    @Test
    void testGetOrientation() {
        BoundingBoxRegion horizontalLine = new BoundingBoxRegion(new Point(0, 0), new Point(5, 0));
        Assertions.assertEquals(Orientation.HORIZONTAL, horizontalLine.getOrientation());

        BoundingBoxRegion verticalLine = new BoundingBoxRegion(new Point(0, 0), new Point(0, 5));
        Assertions.assertEquals(Orientation.VERTICAL, verticalLine.getOrientation());

        Assertions.assertThrows(IllegalStateException.class, () -> {
            region.getOrientation();
        });
    }

    @Test
    void testGetHeight() {
        Assertions.assertEquals(5, region.getHeight());
    }

    @Test
    void testGetWidth() {
        Assertions.assertEquals(5, region.getWidth());
    }

    @Test
    void testGetPoints() {
        List<Point> points = region.getPoints();
        Assertions.assertEquals(36, points.size());
        Assertions.assertTrue(points.contains(new Point(0, 0)));
        Assertions.assertTrue(points.contains(new Point(5, 5)));
        Assertions.assertTrue(points.contains(new Point(2, 3)));
    }

    @Test
    void testGetPointsOrdered() {
        List<Point> orderedPoints = region.getPointsOrdered();
        Assertions.assertEquals(36, orderedPoints.size());
        Assertions.assertEquals(new Point(0, 0), orderedPoints.getFirst());
        Assertions.assertEquals(new Point(5, 5), orderedPoints.getLast());

        for (int i = 1; i < orderedPoints.size(); i++) {
            Point prev = orderedPoints.get(i - 1);
            Point current = orderedPoints.get(i);
            Assertions.assertTrue(prev.y() < current.y() || (prev.y() == current.y() && prev.x() <= current.x()));
        }
    }

    @Test
    void testContainsPoint() {
        Assertions.assertTrue(region.contains(new Point(0, 0)));
        Assertions.assertTrue(region.contains(new Point(5, 5)));
        Assertions.assertTrue(region.contains(new Point(2, 3)));
        Assertions.assertFalse(region.contains(new Point(-1, 0)));
        Assertions.assertFalse(region.contains(new Point(6, 6)));
    }

    @Test
    void testGetRandom() {
        Random mockRandom = new Random(42); // Use a fixed seed for reproducibility
        Point randomPoint = region.getRandom(mockRandom);
        Assertions.assertTrue(region.contains(randomPoint));

        // Test the parameterless getRandom() method
        Point anotherRandomPoint = region.getRandom();
        Assertions.assertTrue(region.contains(anotherRandomPoint));
    }

    @Test
    void testSize() {
        Assertions.assertEquals(25, region.size());

        BoundingBoxRegion singlePoint = new BoundingBoxRegion(new Point(0, 0), new Point(0, 0));
        Assertions.assertEquals(0, singlePoint.size());
    }
}