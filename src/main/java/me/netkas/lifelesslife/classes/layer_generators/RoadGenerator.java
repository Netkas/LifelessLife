package me.netkas.lifelesslife.classes.layer_generators;

import me.netkas.lifelesslife.abstracts.AreaRegion;
import me.netkas.lifelesslife.abstracts.LayerGenerator;
import me.netkas.lifelesslife.classes.MathUtilities;
import me.netkas.lifelesslife.classes.RandomUtilities;
import me.netkas.lifelesslife.classes.ResourceFinder;
import me.netkas.lifelesslife.enums.AreaRegionType;
import me.netkas.lifelesslife.enums.CardinalDirection;
import me.netkas.lifelesslife.enums.DensityLevel;
import me.netkas.lifelesslife.enums.resources.NameResources;
import me.netkas.lifelesslife.objects.AreaChunk;
import me.netkas.lifelesslife.objects.area_region.MainStreetRegion;
import me.netkas.lifelesslife.objects.area_region.RoadRegion;
import me.netkas.lifelesslife.objects.point_region.LineRegion;
import me.netkas.lifelesslife.objects.point_region.PointRegion;
import me.netkas.lifelesslife.records.Point;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RoadGenerator extends LayerGenerator
{
    private final static int MAX_FAILED_ATTEMPTS = 5000;
    private final static int MIN_DISTANCE = 3;
    private final static int MAX_DISTANCE = 8;
    private final static int MIN_LENGTH = 7;
    private final static int MAX_LENGTH = 10;
    private final static int TURN_CHANCE = 50;
    private final static int MIN_TURN_LENGTH = 4; // Minimum length before attempting a turn
    private final static int POST_TURN_MIN_LENGTH = 4; // Minimum length after a turn

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
        while((chunk.getRegionUsage(AreaRegionType.ROAD) < level.getRoadDensity()) && (failedAttempts < MAX_FAILED_ATTEMPTS))
        {
            this.logger.info(String.format("Road Occupation: %s/%s", chunk.getRegionUsage(AreaRegionType.ROAD), level.getRoadDensity()));

            // Random starting main street
            AreaRegion randomStreet = this.getRandomRoad(chunk, random);
            // The direction the road is going to be built branching off the main street (left or right randomly)
            CardinalDirection roadDirection = this.determineDirection(randomStreet, random);
            // Get a random starting point from the main street (One step away from the main street for branching)
            Point startingPoint = randomStreet.getRegion().getRandom(random).toDirection(roadDirection);

            if(!this.buildRoad(chunk, startingPoint, level, roadDirection, random))
            {
                failedAttempts++;
                continue;
            }

            failedAttempts = 0;
        }

        if(failedAttempts >= MAX_FAILED_ATTEMPTS)
        {
            this.logger.warning(String.format("Failed to generate main streets due to too many conflicts. Main Street Occupation: %s/%s", chunk.getRegionUsage(AreaRegionType.MAIN_STREET), level.getMainStreetDensity()));
        }
    }

    private boolean buildRoad(AreaChunk chunk, Point startingPoint, DensityLevel level, CardinalDirection direction, Random random)
    {
        // Always consider turning roads with a higher base chance
        if(RandomUtilities.randomChance(50, random))
        {
            boolean turnResult = this.buildTurningRoad(chunk, startingPoint, direction, random);
            if(turnResult) {
                return true;
            }
            // If turning road fails, fallback to straight road
        }

        return this.buildStraightRoad(chunk, startingPoint, direction, random);
    }

    private boolean buildTurningRoad(AreaChunk chunk, Point startingPoint,
                                     CardinalDirection direction, Random random) {
        List<Point> roadPoints = new ArrayList<>();
        Point currentPoint = startingPoint;
        CardinalDirection currentDirection = direction;
        boolean hasTurned = false;

        // Initial validation
        if (checkConflict(chunk, startingPoint, direction, MIN_DISTANCE)) {
            return false;
        }

        while (roadPoints.size() < MAX_LENGTH) {
            // Add current point if valid
            if (!isValidPoint(chunk, currentPoint, currentDirection)) {
                break;
            }
            roadPoints.add(currentPoint);

            // Consider turning if we haven't turned yet and have minimum length
            if (!hasTurned && roadPoints.size() >= MIN_TURN_LENGTH) {
                CardinalDirection turnDirection = random.nextBoolean() ?
                        currentDirection.getLeft() : currentDirection.getRight();

                Point turnPoint = currentPoint.toDirection(turnDirection);

                if (isValidPoint(chunk, turnPoint, turnDirection) &&
                        !checkConflict(chunk, turnPoint, turnDirection, MIN_DISTANCE)) {
                    currentDirection = turnDirection;
                    currentPoint = turnPoint;
                    hasTurned = true;
                    continue;
                }
            }

            // Move to next point in current direction
            currentPoint = currentPoint.toDirection(currentDirection);
        }

        // Validate final road
        if (roadPoints.size() < MIN_LENGTH ||
                (hasTurned && roadPoints.size() < MIN_TURN_LENGTH + POST_TURN_MIN_LENGTH)) {
            return false;
        }

        // Ensure road connects to something
        if (!hasValidConnection(chunk, roadPoints.get(roadPoints.size() - 1))) {
            return false;
        }

        // Create road region
        try {
            chunk.addRegion(new RoadRegion(
                    new PointRegion(roadPoints),
                    direction,
                    ResourceFinder.getRandomName(NameResources.STREET_NAMES, random)
            ));
            return true;
        } catch (Exception e) {
            this.logger.warning("Failed to generate road: " + e.getMessage());
            return false;
        }
    }

    private boolean isValidPoint(AreaChunk chunk, Point point, CardinalDirection direction) {
        if (!chunk.inBounds(point)) {
            return false;
        }

        // Check if point is already occupied
        if (chunk.regionTypeExists(point, List.of(AreaRegionType.MAIN_STREET, AreaRegionType.ROAD))) {
            return false;
        }

        // Check minimum spacing requirement
        for (int dist = 1; dist <= MIN_DISTANCE; dist++) {
            for (CardinalDirection dir : CardinalDirection.values()) {
                Point checkPoint = point.toDirection(dir, dist);
                if (chunk.inBounds(checkPoint) &&
                        chunk.regionTypeExists(checkPoint, List.of(AreaRegionType.MAIN_STREET, AreaRegionType.ROAD))) {
                    return false;
                }
            }
        }

        return true;
    }

    private boolean hasValidConnection(AreaChunk chunk, Point point) {
        // Check if point connects to existing road/street or chunk edge
        if (!chunk.inBounds(point)) {
            return true; // Connecting to edge
        }

        for (CardinalDirection dir : CardinalDirection.values()) {
            Point adjacent = point.toDirection(dir);
            if (chunk.inBounds(adjacent) &&
                    chunk.regionTypeExists(adjacent, List.of(AreaRegionType.MAIN_STREET, AreaRegionType.ROAD))) {
                return true;
            }
        }

        return false;
    }

    private boolean isValidRoadPoint(AreaChunk chunk, Point point, CardinalDirection direction) {
        if(!chunk.inBounds(point)) return false;

        // Check immediate point
        if(chunk.regionTypeExists(point, List.of(AreaRegionType.MAIN_STREET, AreaRegionType.ROAD))) {
            return false;
        }

        // Check minimum spacing requirement
        for(CardinalDirection dir : CardinalDirection.values()) {
            for(int dist = 1; dist <= MIN_DISTANCE; dist++) {
                Point checkPoint = point.toDirection(dir, dist);
                if(chunk.inBounds(checkPoint) &&
                        chunk.regionTypeExists(checkPoint, List.of(AreaRegionType.MAIN_STREET, AreaRegionType.ROAD))) {
                    return false;
                }
            }
        }

        return true;
    }
    private boolean hasValidEndpoint(AreaChunk chunk, List<Point> points) {
        if(points.isEmpty()) return false;

        Point lastPoint = points.get(points.size() - 1);

        // Check if we're connected to a road/main street
        for(CardinalDirection dir : CardinalDirection.values()) {
            Point adjacent = lastPoint.toDirection(dir);
            if(chunk.inBounds(adjacent) &&
                    chunk.regionTypeExists(adjacent, List.of(AreaRegionType.MAIN_STREET, AreaRegionType.ROAD))) {
                return true;
            }
        }

        return false;
    }
    private Point findValidTurnPoint(AreaChunk chunk, Point currentPoint, CardinalDirection currentDirection, Random random) {
        // Try both left and right turns
        CardinalDirection[] turnDirections = {
                currentDirection.getLeft(),
                currentDirection.getRight()
        };

        for(CardinalDirection turnDir : turnDirections) {
            Point turnPoint = currentPoint.toDirection(turnDir);
            if(isValidRoadPoint(chunk, turnPoint, turnDir) &&
                    hasValidContinuation(chunk, turnPoint, turnDir)) {
                return turnPoint;
            }
        }

        return null;
    }


    private boolean hasValidContinuation(AreaChunk chunk, Point point, CardinalDirection direction) {
        Point checkPoint = point;
        int validPoints = 0;

        // Look ahead to ensure we have enough space to build
        for(int i = 0; i < MIN_LENGTH; i++) {
            checkPoint = checkPoint.toDirection(direction);
            if(!isValidRoadPoint(chunk, checkPoint, direction)) {
                break;
            }
            validPoints++;
        }

        return validPoints >= MIN_LENGTH/2; // Allow shorter segments after turns
    }

    private Point findNearestValidConnection(AreaChunk chunk, Point point, CardinalDirection direction) {
        // Check in the current direction first
        Point ahead = point.toDirection(direction);
        if(isValidConnectionPoint(chunk, ahead)) {
            return ahead;
        }

        // Check surrounding points within 2 blocks
        for(int distance = 1; distance <= 2; distance++) {
            for(CardinalDirection checkDir : CardinalDirection.values()) {
                Point checkPoint = point.toDirection(checkDir, distance);
                if(isValidConnectionPoint(chunk, checkPoint)) {
                    // Make sure the connection point maintains spacing
                    Point connectionPoint = checkPoint.toDirection(checkDir.opposite());
                    if(isValidRoadPoint(chunk, connectionPoint, direction)) {
                        return connectionPoint;
                    }
                }
            }
        }

        return null;
    }

    private boolean isValidConnectionPoint(AreaChunk chunk, Point point) {
        if(!chunk.inBounds(point)) {
            return false;
        }

        // Check if point is adjacent to existing road/street
        for(CardinalDirection dir : CardinalDirection.values()) {
            Point adjacent = point.toDirection(dir);
            if(chunk.inBounds(adjacent) &&
                    chunk.regionTypeExists(adjacent, List.of(AreaRegionType.MAIN_STREET, AreaRegionType.ROAD))) {
                return true;
            }
        }

        return false;
    }

    private boolean isValidRoad(AreaChunk chunk, List<Point> points, CardinalDirection finalDirection) {
        // Road must be long enough
        if(points.size() < MIN_LENGTH) {
            return false;
        }

        // Road must connect to something at the end
        Point lastPoint = points.get(points.size() - 1);
        boolean hasConnection = false;

        for(CardinalDirection dir : CardinalDirection.values()) {
            Point adjacent = lastPoint.toDirection(dir);
            if(chunk.inBounds(adjacent) &&
                    chunk.regionTypeExists(adjacent, List.of(AreaRegionType.MAIN_STREET, AreaRegionType.ROAD))) {
                hasConnection = true;
                break;
            }
        }

        return hasConnection;
    }

    private CardinalDirection getNewDirection(Point from, Point to) {
        for(CardinalDirection dir : CardinalDirection.values()) {
            if(from.toDirection(dir).equals(to)) {
                return dir;
            }
        }
        return null;
    }

    private boolean buildStraightRoad(AreaChunk chunk, Point startingPoint, CardinalDirection direction, Random random)
    {
        // Check if the road has a conflict
        if(this.checkConflict(chunk, startingPoint, direction, random.nextInt(MIN_DISTANCE, MAX_DISTANCE)))
        {
            return false;
        }

        LineRegion traversal = chunk.traverseDirection(startingPoint, direction, true);
        Point endPoint = null;

        for(Point point : traversal.getPointsOrdered(startingPoint))
        {
            if(this.checkConflict(chunk, point, direction, MIN_DISTANCE))
            {
                break;
            }

            endPoint = point;
        }

        if(endPoint == null)
        {
            return false;
        }

        LineRegion road = new LineRegion(startingPoint, endPoint);
        if(road.size() < MIN_LENGTH)
        {
            return false;
        }

        chunk.addRegion(new RoadRegion(road, direction, ResourceFinder.getRandomName(NameResources.STREET_NAMES, random)));
        return true;
    }

    private AreaRegion getRandomRoad(AreaChunk chunk, Random random)
    {
        if(chunk.regionTypeExists(AreaRegionType.ROAD))
        {
            if(RandomUtilities.randomChance(30, random))
            {
                return chunk.getRandomRegion(AreaRegionType.ROAD, random);
            }
        }

        return chunk.getRandomRegion(AreaRegionType.MAIN_STREET, random);
    }

    private CardinalDirection determineDirection(AreaRegion region, Random random)
    {
        if(region instanceof MainStreetRegion)
        {
            return ((MainStreetRegion)region).getDirection().getLeftOrRight(random);
        }

        if(region instanceof RoadRegion)
        {
            return ((RoadRegion)region).getDirection().getLeftOrRight(random);
        }

        throw new IllegalStateException("Unexpected region type");
    }

    private boolean checkConflict(AreaChunk chunk, Point startingPoint, CardinalDirection direction, int distance)
    {
        if(!chunk.inBounds(startingPoint))
        {
            this.logger.info(String.format("Conflict at %s with %s (distance: %s) due to out of bounds", startingPoint, direction, distance));
            return true;
        }

        if(chunk.regionTypeExists(startingPoint, List.of(AreaRegionType.MAIN_STREET, AreaRegionType.ROAD)))
        {
            this.logger.info(String.format("Conflict at %s with %s (distance: %s) due to existing ROAD/MAIN_STREET region", startingPoint, direction, distance));
            return true;
        }

        LineRegion leftTraversal = chunk.traverseDirection(startingPoint, direction.getLeft(), distance, false);

        if(leftTraversal.size() < distance)
        {
            this.logger.info(String.format("Conflict at %s with %s (distance: %s) due to left traversal size", startingPoint, direction, distance));
            return true;
        }

        for(Point point : leftTraversal.getPoints())
        {
            if(!chunk.inBounds(point))
            {
                this.logger.info(String.format("Conflict at %s with %s (distance: %s) due to left traversal being out of bounds", startingPoint, direction, distance));
                return true;
            }

            if(chunk.regionTypeExists(point, List.of(AreaRegionType.MAIN_STREET, AreaRegionType.ROAD)))
            {
                this.logger.info(String.format("Conflict at %s with %s (distance: %s) due to left traversal going through ROAD/MAIN_STREET region", startingPoint, direction, distance));
                return true;
            }
        }

        LineRegion rightTraversal = chunk.traverseDirection(startingPoint, direction.getRight(), distance, false);
        if(rightTraversal.size() < distance)
        {
            this.logger.info(String.format("Conflict at %s with %s (distance: %s) due to right traversal size", startingPoint, direction, distance));
            return true;
        }
        for(Point point : rightTraversal.getPoints())
        {
            if(!chunk.inBounds(point))
            {
                this.logger.info(String.format("Conflict at %s with %s (distance: %s) due to right traversal being out of bounds", startingPoint, direction, distance));
                return true;
            }

            if(chunk.regionTypeExists(point, List.of(AreaRegionType.MAIN_STREET, AreaRegionType.ROAD)))
            {
                this.logger.info(String.format("Conflict at %s with %s (distance: %s) due to right traversal going through ROAD/MAIN_STREET region", startingPoint, direction, distance));
                return true;
            }
        }

        return false;
    }
}