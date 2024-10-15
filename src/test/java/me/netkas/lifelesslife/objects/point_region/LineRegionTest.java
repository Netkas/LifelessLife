package me.netkas.lifelesslife.objects.point_region;

import me.netkas.lifelesslife.records.Point;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class LineRegionTest {

    @Test
    void containsPointWithinVerticalLineRegion() {
        Point start = new Point(2, 0);
        Point end = new Point(2, 100);  // Larger region
        LineRegion lineRegion = new LineRegion(start, end);

        Point testPoint = new Point(2, 50);  // Middle point
        assertTrue(lineRegion.contains(testPoint));
    }

    @Test
    void doesNotContainPointOutsideVerticalLineRegion() {
        Point start = new Point(2, 0);
        Point end = new Point(2, 100);  // Larger region
        LineRegion lineRegion = new LineRegion(start, end);

        Point testPoint = new Point(3, 50);  // Outside the line
        assertFalse(lineRegion.contains(testPoint));
    }

    @Test
    void containsPointWithinHorizontalLineRegion() {
        Point start = new Point(0, 2);
        Point end = new Point(100, 2);  // Larger region
        LineRegion lineRegion = new LineRegion(start, end);

        Point testPoint = new Point(50, 2);  // Middle point
        assertTrue(lineRegion.contains(testPoint));
    }

    @Test
    void doesNotContainPointOutsideHorizontalLineRegion() {
        Point start = new Point(0, 2);
        Point end = new Point(100, 2);  // Larger region
        LineRegion lineRegion = new LineRegion(start, end);

        Point testPoint = new Point(50, 3);  // Outside the line
        assertFalse(lineRegion.contains(testPoint));
    }

    @Test
    void throwsExceptionForDiagonalLineRegion() {
        Point start = new Point(0, 0);
        Point end = new Point(3, 3);  // Diagonal points

        assertThrows(IllegalArgumentException.class, () -> new LineRegion(start, end));
    }

    @Test
    void containsPointExactlyOnBoundaryOfVerticalLineRegion() {
        Point start = new Point(2, 0);
        Point end = new Point(2, 100);
        LineRegion lineRegion = new LineRegion(start, end);

        Point boundaryPoint = new Point(2, 100);  // Exact boundary point
        assertTrue(lineRegion.contains(boundaryPoint));
    }

    @Test
    void containsPointExactlyOnBoundaryOfHorizontalLineRegion() {
        Point start = new Point(0, 2);
        Point end = new Point(100, 2);
        LineRegion lineRegion = new LineRegion(start, end);

        Point boundaryPoint = new Point(100, 2);  // Exact boundary point
        assertTrue(lineRegion.contains(boundaryPoint));
    }

    @Test
    void containsMultiplePointsWithinLineRegion() {
        Point start = new Point(0, 2);
        Point end = new Point(100, 2);
        LineRegion lineRegion = new LineRegion(start, end);

        List<Point> points = lineRegion.getPoints();
        assertEquals(101, points.size());  // Includes both endpoints
    }

    @Test
    void randomPointWithinLargeLineRegion() {
        Point start = new Point(0, 2);
        Point end = new Point(1000, 2);  // Very large region
        LineRegion lineRegion = new LineRegion(start, end);

        Point randomPoint = lineRegion.getRandom(new Random());
        assertTrue(lineRegion.contains(randomPoint));
    }

    @Test
    void lineRegionContainsAnotherLineRegion() {
        Point start = new Point(0, 2);
        Point end = new Point(10, 2);
        LineRegion lineRegion = new LineRegion(start, end);

        Point containedStart = new Point(2, 2);
        Point containedEnd = new Point(8, 2);
        LineRegion containedLineRegion = new LineRegion(containedStart, containedEnd);

        assertTrue(lineRegion.contains(containedLineRegion));
    }

    @Test
    void lineRegionDoesNotContainDisjointLineRegion() {
        Point start = new Point(0, 2);
        Point end = new Point(10, 2);
        LineRegion lineRegion = new LineRegion(start, end);

        Point disjointStart = new Point(12, 2);
        Point disjointEnd = new Point(14, 2);
        LineRegion disjointLineRegion = new LineRegion(disjointStart, disjointEnd);

        assertFalse(lineRegion.contains(disjointLineRegion));
    }

    @Test
    void largeHorizontalRegionSize() {
        Point start = new Point(0, 2);
        Point end = new Point(1000, 2);
        LineRegion lineRegion = new LineRegion(start, end);

        assertEquals(1001, lineRegion.size());
    }

    @Test
    void largeVerticalRegionSize() {
        Point start = new Point(2, 0);
        Point end = new Point(2, 1000);
        LineRegion lineRegion = new LineRegion(start, end);

        assertEquals(1001, lineRegion.size());
    }
}
