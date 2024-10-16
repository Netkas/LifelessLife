package me.netkas.lifelesslife.classes.layer_generators;

import me.netkas.lifelesslife.abstracts.AreaRegion;
import me.netkas.lifelesslife.enums.AreaRegionType;
import me.netkas.lifelesslife.enums.DensityLevel;
import me.netkas.lifelesslife.objects.AreaChunk;
import me.netkas.lifelesslife.records.Point;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

class MainStreetGeneratorTest {
    private static final String DIR_NAME = "rendering_tests";

    @Test
    void generateLowDensity() {
        AreaChunk chunk = new AreaChunk(512,  512);
        MainStreetGenerator generator = new MainStreetGenerator();
        generator.generateLayer(chunk, DensityLevel.LOW, new Random());

        try
        {
            renderChunk(chunk, "low_density_main_streets.png");
        }
        catch(IOException e)
        {
            Assertions.fail(e);
        }
    }

    @Test
    void generateMediumDensity() {
        AreaChunk chunk = new AreaChunk(512,  512);
        MainStreetGenerator generator = new MainStreetGenerator();
        generator.generateLayer(chunk, DensityLevel.MEDIUM, new Random());

        try
        {
            renderChunk(chunk, "medium_density_main_streets.png");
        }
        catch(IOException e)
        {
            Assertions.fail(e);
        }
    }

    @Test
    void generateHighDensity() {
        AreaChunk chunk = new AreaChunk(512,  512);
        MainStreetGenerator generator = new MainStreetGenerator();
        generator.generateLayer(chunk, DensityLevel.HIGH, new Random());

        try
        {
            renderChunk(chunk, "high_density_main_streets.png");
        }
        catch(IOException e)
        {
            Assertions.fail(e);
        }
    }

    private void renderChunk(AreaChunk chunk, String name) throws IOException {
        // Prepare the image
        int tileSize = 4;
        int imageHeight = chunk.getHeight() * tileSize;
        int imageWidth = chunk.getWidth() * tileSize;
        BufferedImage image = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = image.createGraphics();

        // Fill the entire image with white
        graphics.setColor(Color.BLACK);
        graphics.fillRect(0, 0, imageWidth, imageHeight);
        graphics.setStroke(new BasicStroke(2));

        // Render the chunk
        graphics.setColor(Color.WHITE);
        for(AreaRegion region : chunk.getRegions(AreaRegionType.MAIN_STREET))
        {
            for(Point point : region.getRegion().getPoints())
            {
                graphics.fillRect(point.x() * tileSize, point.y() * tileSize, tileSize, tileSize);
            }
        }

        // Save the image
        ImageIO.write(image, "PNG", new File(DIR_NAME + "/" + name));
        System.out.println("Image created: " + DIR_NAME + "/" + name);
    }
}