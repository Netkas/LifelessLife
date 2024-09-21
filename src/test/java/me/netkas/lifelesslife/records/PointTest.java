package me.netkas.lifelesslife.records;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PointTest {

    @Test
    public void testDistanceToSamePoint() {
        Point point = new Point(2, 2);
        int result = point.distanceTo(point);
        assertEquals(0, result, "Distance to the same point should be zero");
    }

    @Test
    public void testDistanceToDifferentPoint() {
        Point point = new Point(3, 4);
        Point other = new Point(6, 8);
        int result = point.distanceTo(other);
        assertEquals(5, result, "Distance to other point not calculated correctly");
    }

    @Test
    public void testDistanceToItselfPlusMovedPoint() {
        Point point = new Point(3, 3);
        Point other = point.move(4, 0);
        int result = point.distanceTo(other);
        assertEquals(4, result, "Distance to moved point not calculated correctly");
    }
}