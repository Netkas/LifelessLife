package me.netkas.lifelesslife.objects;

import me.netkas.lifelesslife.enums.CardinalDirection;
import me.netkas.lifelesslife.objects.point_region.LineRegion;
import me.netkas.lifelesslife.records.Point;
import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * A test class for AreaChunk.
 * This class tests the getEdge method of the AreaChunk class.
 */
public class AreaChunkTest {

    private static final String DIR_NAME = "rendering_tests";

    /**
     * This method tests the getEdge method with CardinalDirection.NORTH argument
     */
    @Test
    void getEdgeNorth() {
        AreaChunk areaChunk = new AreaChunk(10, 20);
        LineRegion actual = areaChunk.getEdge(CardinalDirection.NORTH);
        LineRegion expected = new LineRegion(new Point(0, 0), new Point(20, 0));
        assertEquals(expected, actual);
    }

    /**
     * This method tests the getEdge method with CardinalDirection.EAST argument
     */
    @Test
    void getEdgeEast() {
        AreaChunk areaChunk = new AreaChunk(10, 20);
        LineRegion actual = areaChunk.getEdge(CardinalDirection.EAST);
        LineRegion expected = new LineRegion(new Point(20, 0), new Point(20, 10));
        assertEquals(expected, actual);
    }

    /**
     * This method tests the getEdge method with CardinalDirection.SOUTH argument
     */
    @Test
    void getEdgeSouth() {
        AreaChunk areaChunk = new AreaChunk(10, 20);
        LineRegion actual = areaChunk.getEdge(CardinalDirection.SOUTH);
        LineRegion expected = new LineRegion(new Point(0, 10), new Point(20, 10));
        assertEquals(expected, actual);
    }

    /**
     * This method tests the getEdge method with CardinalDirection.WEST argument
     */
    @Test
    void getEdgeWest() {
        AreaChunk areaChunk = new AreaChunk(10, 20);
        LineRegion actual = areaChunk.getEdge(CardinalDirection.WEST);
        LineRegion expected = new LineRegion(new Point(0, 0), new Point(0, 10));
        assertEquals(expected, actual);
    }

    @Test
    void renderAreaChunk() {
        AreaChunk areaChunk = new AreaChunk(64, 64);
        int tileSize = 4;
        int imageHeight = areaChunk.getHeight() * tileSize;
        int imageWidth = areaChunk.getWidth() * tileSize;

        // Create directory if it doesn't exist
        File dir = new File(DIR_NAME);
        if (!dir.exists()) {
            if (dir.mkdir()) {
                System.out.println("Directory created: " + DIR_NAME);
            } else {
                System.err.println("Failed to create directory: " + DIR_NAME);
                return;
            }
        }

        renderEdgeAndSaveImage(areaChunk, CardinalDirection.NORTH, DIR_NAME + "/north_edge.png", Color.BLACK, tileSize, imageWidth, imageHeight);
        renderEdgeAndSaveImage(areaChunk, CardinalDirection.EAST, DIR_NAME + "/east_edge.png", Color.RED, tileSize, imageWidth, imageHeight);
        renderEdgeAndSaveImage(areaChunk, CardinalDirection.SOUTH, DIR_NAME + "/south_edge.png", Color.GREEN, tileSize, imageWidth, imageHeight);
        renderEdgeAndSaveImage(areaChunk, CardinalDirection.WEST, DIR_NAME + "/west_edge.png", Color.BLUE, tileSize, imageWidth, imageHeight);
        renderAllEdgesAndSaveImage(areaChunk, DIR_NAME + "/all_edges.png", tileSize, imageWidth, imageHeight);
    }

    private void renderEdgeAndSaveImage(AreaChunk areaChunk, CardinalDirection direction, String fileName, Color color, int tileSize, int imageWidth, int imageHeight) {
        BufferedImage image = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = image.createGraphics();

        // Fill the entire image with white
        graphics.setColor(Color.WHITE);
        graphics.fillRect(0, 0, imageWidth, imageHeight);
        graphics.setStroke(new BasicStroke(2));

        // Render the specific edge
        renderEdge(graphics, areaChunk.getEdge(direction), color, tileSize);

        // Dispose the graphics object
        graphics.dispose();

        // Save the image as PNG
        try {
            ImageIO.write(image, "PNG", new File(fileName));
            System.out.println("Image created: " + fileName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void renderAllEdgesAndSaveImage(AreaChunk areaChunk, String fileName, int tileSize, int imageWidth, int imageHeight) {
        BufferedImage image = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = image.createGraphics();

        // Fill the entire image with white
        graphics.setColor(Color.WHITE);
        graphics.fillRect(0, 0, imageWidth, imageHeight);
        graphics.setStroke(new BasicStroke(2));

        // Render all edges
        renderEdge(graphics, areaChunk.getEdge(CardinalDirection.NORTH), Color.BLACK, tileSize);
        renderEdge(graphics, areaChunk.getEdge(CardinalDirection.EAST), Color.RED, tileSize);
        renderEdge(graphics, areaChunk.getEdge(CardinalDirection.SOUTH), Color.GREEN, tileSize);
        renderEdge(graphics, areaChunk.getEdge(CardinalDirection.WEST), Color.BLUE, tileSize);

        // Dispose the graphics object
        graphics.dispose();

        // Save the image as PNG
        try {
            ImageIO.write(image, "PNG", new File(fileName));
            System.out.println("Image created: " + fileName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void renderEdge(Graphics2D graphics, LineRegion edge, Color color, int tileSize) {
        graphics.setColor(color);
        Point start = edge.start();
        Point end = edge.end();
        graphics.drawLine(start.x() * tileSize, start.y() * tileSize, end.x() * tileSize, end.y() * tileSize);
    }
}