package me.netkas.lifelesslife.classes.layer_generators;

import me.netkas.lifelesslife.abstracts.LayerGenerator;
import me.netkas.lifelesslife.classes.ResourceFinder;
import me.netkas.lifelesslife.enums.AreaRegionType;
import me.netkas.lifelesslife.enums.CardinalDirection;
import me.netkas.lifelesslife.enums.DensityLevel;
import me.netkas.lifelesslife.enums.resources.NameResources;
import me.netkas.lifelesslife.objects.AreaChunk;
import me.netkas.lifelesslife.objects.area_region.MainStreetRegion;
import me.netkas.lifelesslife.objects.point_region.LineRegion;
import me.netkas.lifelesslife.records.Point;

import java.util.Random;

public class MainStreetGenerator extends LayerGenerator
{
    private final static int MAX_FAILED_ATTEMPTS = 100;
    private final static int MIN_DISTANCE = 18;
    private final static int MAX_DISTANCE = 25;

    /**
     * Generates a layer of main streets within the given AreaChunk based on the
     * specified DensityLevel and Random instance.
     *
     * @param chunk The AreaChunk object representing the area where main streets
     *              will be generated.
     * @param level The DensityLevel object specifying the density and other
     *              parameters for the main street generation.
     * @param random The Random instance used for stochastic processes within the
     *               main street generation.
     */
    @Override
    public void generateLayer(AreaChunk chunk, DensityLevel level, Random random)
    {
        int failedAttempts = 0;
        while((chunk.getRegionUsage(AreaRegionType.MAIN_STREET) < level.getMainStreetDensity()) && (failedAttempts < MAX_FAILED_ATTEMPTS))
        {
            this.logger.info(String.format("Main Street Occupation: %s/%s", chunk.getRegionUsage(AreaRegionType.MAIN_STREET), level.getMainStreetDensity()));

            // Random starting edge
            CardinalDirection startingEdge = CardinalDirection.random(random);
            // The direction the road is going to be built
            CardinalDirection roadDirection = startingEdge.opposite();
            // Get a random starting point from the starting edge
            Point startingPoint = chunk.getEdge(startingEdge).getRandom(random);
            this.logger.finest("Generated starting point: " + startingPoint);

            // First check if the starting point is already occupied
            if(chunk.regionExists(startingPoint))
            {
                this.logger.finest("Starting point is already occupied.");
                continue;
            }

            if(checkConflict(chunk, startingPoint, roadDirection, random.nextInt(MIN_DISTANCE, MAX_DISTANCE)))
            {
                this.logger.finest(String.format("Point %s has a conflict.", startingPoint));
                failedAttempts++;
                continue;
            }

            // Traverse
            this.logger.finest("No conflicts found. Building road.");
            LineRegion road = chunk.traverseDirection(startingPoint, roadDirection, true);

            // Create the region
            this.logger.finest("Adding region to chunk.");
            chunk.addRegion(new MainStreetRegion(road.start(), road.end(), roadDirection, ResourceFinder.getRandomName(NameResources.MAIN_STREETS, random)));
            failedAttempts = 0;
        }

        if(failedAttempts >= MAX_FAILED_ATTEMPTS)
        {
            this.logger.warning(String.format("Failed to generate main streets due to too many conflicts. Main Street Occupation: %s/%s", chunk.getRegionUsage(AreaRegionType.MAIN_STREET), level.getMainStreetDensity()));
        }
    }

    private boolean checkConflict(AreaChunk chunk, Point startingPoint, CardinalDirection direction, int distance)
    {
        LineRegion leftTraversal = chunk.traverseDirection(startingPoint, direction.getLeft(), distance, false);

        if(leftTraversal.size() < MIN_DISTANCE)
        {
            return true;
        }

        for(Point point : leftTraversal.getPoints())
        {
            if(!chunk.inBounds(point) || chunk.regionTypeExists(point, AreaRegionType.MAIN_STREET))
            {
                return true;
            }
        }

        LineRegion rightTraversal = chunk.traverseDirection(startingPoint, direction.getRight(), distance, false);

        if(rightTraversal.size() < MIN_DISTANCE)
        {
            return true;
        }

        for(Point point : rightTraversal.getPoints())
        {
            if(!chunk.inBounds(point) || chunk.regionTypeExists(point, AreaRegionType.MAIN_STREET))
            {
                return true;
            }
        }

        return false;
    }
}
