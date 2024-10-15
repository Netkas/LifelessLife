package me.netkas.lifelesslife.objects.point_region;

import me.netkas.lifelesslife.enums.Orientation;
import me.netkas.lifelesslife.records.Point;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.*;

public class PointRegionTest {
    @Test
    public void testGetOrientationVertical() {
        List<Point> points = Arrays.asList(new Point(1, 1), new Point(1, 2), new Point(1, 3));
        PointRegion pointRegion = new PointRegion(points);
        Assertions.assertEquals(Orientation.VERTICAL, pointRegion.getOrientation());
    }

    @Test
    public void testGetOrientationHorizontal() {
        List<Point> points = Arrays.asList(new Point(1, 1), new Point(2, 1), new Point(3, 1));
        PointRegion pointRegion = new PointRegion(points);
        Assertions.assertEquals(Orientation.HORIZONTAL, pointRegion.getOrientation());
    }

    @Test
    public void testGetOrientationNotALine() {
        List<Point> points = Arrays.asList(new Point(1, 1), new Point(1, 2), new Point(2, 1), new Point(2, 2));
        PointRegion pointRegion = new PointRegion(points);
        Assertions.assertThrows(IllegalStateException.class, pointRegion::getOrientation);
    }

    @Test
    public void testContainsPointInRegion() {
        List<Point> points = Arrays.asList(new Point(1, 1), new Point(1, 2), new Point(1, 3));
        PointRegion pointRegion = new PointRegion(points);
        Assertions.assertTrue(pointRegion.contains(new Point(1, 2)));
    }

    @Test
    public void testDoesNotContainPointNotInRegion() {
        List<Point> points = Arrays.asList(new Point(1, 1), new Point(1, 2), new Point(1, 3));
        PointRegion pointRegion = new PointRegion(points);
        Assertions.assertFalse(pointRegion.contains(new Point(2, 2)));
    }

    @Test
    public void testLargeDataInput() {
        List<Point> points = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            points.add(new Point(i, 0));
        }
        PointRegion pointRegion = new PointRegion(points);
        Assertions.assertTrue(pointRegion.contains(new Point(500, 0)));
        Assertions.assertFalse(pointRegion.contains(new Point(1000, 0)));
    }

    @Test
    public void testNonAdjacentPoints() {
        List<Point> points = Arrays.asList(new Point(1, 1), new Point(1, 3));
        Assertions.assertThrows(IllegalArgumentException.class, () -> new PointRegion(points));
    }

    @Test
    public void testDuplicatePoints() {
        List<Point> points = Arrays.asList(new Point(1, 1), new Point(1, 1), new Point(1, 2));
        Assertions.assertThrows(IllegalArgumentException.class, () -> new PointRegion(points));
    }

    @Test
    public void testRandomPointSelection() {
        List<Point> points = Arrays.asList(new Point(1, 1), new Point(1, 2), new Point(1, 3));
        PointRegion pointRegion = new PointRegion(points);
        Random random = new Random();
        Point randomPoint = pointRegion.getRandom(random);
        Assertions.assertTrue(pointRegion.contains(randomPoint));
    }

    @Test
    public void testPointOrdering() {
        List<Point> points = Arrays.asList(new Point(1, 2), new Point(1, 1), new Point(1, 3));
        PointRegion pointRegion = new PointRegion(points);
        List<Point> orderedPoints = pointRegion.getPointsOrdered();
        Assertions.assertEquals(Arrays.asList(new Point(1, 1), new Point(1, 2), new Point(1, 3)), orderedPoints);
    }

    @Test
    public void testEmptyPointListThrowsException() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> new PointRegion(Collections.emptyList()));
    }
}
