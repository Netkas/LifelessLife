package me.netkas.lifelesslife.objects.area_region;

import me.netkas.lifelesslife.abstracts.AreaRegion;
import me.netkas.lifelesslife.classes.TextUtilities;
import me.netkas.lifelesslife.enums.AreaRegionType;
import me.netkas.lifelesslife.enums.CardinalDirection;
import me.netkas.lifelesslife.objects.point_region.LineRegion;
import me.netkas.lifelesslife.records.Point;

public class MainStreetRegion extends AreaRegion
{
    private final CardinalDirection direction;
    private final String name;

    public MainStreetRegion(Point start, Point end, CardinalDirection direction, String name)
    {
        super(new LineRegion(start, end));
        this.direction = direction;
        this.name = TextUtilities.capitalizeTitle(String.format("%s street", name));
    }

    @Override
    public AreaRegionType getType()
    {
        return AreaRegionType.MAIN_STREET;
    }

    public CardinalDirection getDirection()
    {
        return direction;
    }

    public String getName()
    {
        return name;
    }

    public String getFullName()
    {
        return TextUtilities.capitalizeTitle(String.format("%s %s", this.name, this.direction));
    }
}
