package me.netkas.lifelesslife.objects.area_region;

import me.netkas.lifelesslife.abstracts.AreaRegion;
import me.netkas.lifelesslife.enums.AreaRegionType;
import me.netkas.lifelesslife.enums.CardinalDirection;
import me.netkas.lifelesslife.objects.point_region.LineRegion;
import me.netkas.lifelesslife.records.Point;

public class MainStreetRegion extends AreaRegion
{
    public MainStreetRegion(Point start, Point end, CardinalDirection direction)
    {
        super(new LineRegion(start, end));
    }

    @Override
    public AreaRegionType getType()
    {
        return AreaRegionType.MAIN_STREET;
    }
}
