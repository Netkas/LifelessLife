package me.netkas.lifelesslife.objects;

import me.netkas.lifelesslife.enums.CardinalDirection;
import me.netkas.lifelesslife.objects.point_region.LineRegion;
import me.netkas.lifelesslife.objects.point_region.PointRegion;
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
public class AreaChunkNeighborTest {
    private static final String DIR_NAME = "rendering_tests";

    /**
     * This method tests the getEdge method with CardinalDirection.NORTH argument
     */
    @Test
    void getNorthNeighbors() {
        AreaChunk chunk = new AreaChunk(150, 150);
        Point startingPoint = chunk.getCenter();
        Point northPoint = startingPoint.toDirection(CardinalDirection.NORTH);
        PointRegion neighbors = chunk.getNeighbors(startingPoint, CardinalDirection.NORTH, 5);

        // Prepare the image
        int tileSize = 4;
        int imageHeight = chunk.getHeight() * tileSize;
        int imageWidth = chunk.getWidth() * tileSize;
        BufferedImage image = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = image.createGraphics();

        // Draw the direction
        graphics.setColor(Color.BLUE);
        graphics.fillRect(northPoint.x() * tileSize, northPoint.y() * tileSize, tileSize, tileSize);

        // Draw the neighbors
        graphics.setColor(Color.GREEN);
        for(Point point : neighbors.getPoints())
        {
            graphics.fillRect(point.x() * tileSize, point.y() * tileSize, tileSize, tileSize);
        }

        // Draw the initial point
        graphics.setColor(Color.RED);
        graphics.fillRect(startingPoint.x() * tileSize, startingPoint.y() * tileSize, tileSize, tileSize);

        // Save the image
        try
        {
            ImageIO.write(image, "PNG", new File(DIR_NAME + "/north_neighbors.png"));
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * This method tests the getEdge method with CardinalDirection.EAST argument
     */
    @Test
    void getEastNeighbors() {
        AreaChunk chunk = new AreaChunk(150, 150);
        Point startingPoint = chunk.getCenter();
        Point eastPoint = startingPoint.toDirection(CardinalDirection.EAST);
        PointRegion neighbors = chunk.getNeighbors(startingPoint, CardinalDirection.EAST, 5);

        // Prepare the image
        int tileSize = 4;
        int imageHeight = chunk.getHeight() * tileSize;
        int imageWidth = chunk.getWidth() * tileSize;
        BufferedImage image = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = image.createGraphics();

        // Draw the direction
        graphics.setColor(Color.BLUE);
        graphics.fillRect(eastPoint.x() * tileSize, eastPoint.y() * tileSize, tileSize, tileSize);

        // Draw the neighbors
        graphics.setColor(Color.GREEN);
        for(Point point : neighbors.getPoints())
        {
            graphics.fillRect(point.x() * tileSize, point.y() * tileSize, tileSize, tileSize);
        }

        // Draw the initial point
        graphics.setColor(Color.RED);
        graphics.fillRect(startingPoint.x() * tileSize, startingPoint.y() * tileSize, tileSize, tileSize);

        // Save the image
        try
        {
            ImageIO.write(image, "PNG", new File(DIR_NAME + "/east_neighbors.png"));
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * This method tests the getEdge method with CardinalDirection.SOUTH argument
     */
    @Test
    void getSouthNeighbors() {
        AreaChunk chunk = new AreaChunk(150, 150);
        Point startingPoint = chunk.getCenter();
        Point southPoint = startingPoint.toDirection(CardinalDirection.SOUTH);
        PointRegion neighbors = chunk.getNeighbors(startingPoint, CardinalDirection.SOUTH, 5);

        // Prepare the image
        int tileSize = 4;
        int imageHeight = chunk.getHeight() * tileSize;
        int imageWidth = chunk.getWidth() * tileSize;
        BufferedImage image = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = image.createGraphics();

        // Draw the direction
        graphics.setColor(Color.BLUE);
        graphics.fillRect(southPoint.x() * tileSize, southPoint.y() * tileSize, tileSize, tileSize);

        // Draw the neighbors
        graphics.setColor(Color.GREEN);
        for(Point point : neighbors.getPoints())
        {
            graphics.fillRect(point.x() * tileSize, point.y() * tileSize, tileSize, tileSize);
        }

        // Draw the initial point
        graphics.setColor(Color.RED);
        graphics.fillRect(startingPoint.x() * tileSize, startingPoint.y() * tileSize, tileSize, tileSize);

        // Save the image
        try
        {
            ImageIO.write(image, "PNG", new File(DIR_NAME + "/south_neighbors.png"));
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * This method tests the getEdge method with CardinalDirection.WEST argument
     */
    @Test
    void getWestNeighbors() {
        AreaChunk chunk = new AreaChunk(150, 150);
        Point startingPoint = chunk.getCenter();
        Point westPoint = startingPoint.toDirection(CardinalDirection.WEST);
        PointRegion neighbors = chunk.getNeighbors(startingPoint, CardinalDirection.WEST, 5);

        // Prepare the image
        int tileSize = 4;
        int imageHeight = chunk.getHeight() * tileSize;
        int imageWidth = chunk.getWidth() * tileSize;
        BufferedImage image = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = image.createGraphics();

        // Draw the direction
        graphics.setColor(Color.BLUE);
        graphics.fillRect(westPoint.x() * tileSize, westPoint.y() * tileSize, tileSize, tileSize);

        // Draw the neighbors
        graphics.setColor(Color.GREEN);
        for(Point point : neighbors.getPoints())
        {
            graphics.fillRect(point.x() * tileSize, point.y() * tileSize, tileSize, tileSize);
        }

        // Draw the initial point
        graphics.setColor(Color.RED);
        graphics.fillRect(startingPoint.x() * tileSize, startingPoint.y() * tileSize, tileSize, tileSize);

        // Save the image
        try
        {
            ImageIO.write(image, "PNG", new File(DIR_NAME + "/west_neighbors.png"));
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

}