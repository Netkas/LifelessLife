package me.netkas.lifelesslife.objects.area_region;

import me.netkas.lifelesslife.abstracts.AreaRegion;
import me.netkas.lifelesslife.classes.TextUtilities;
import me.netkas.lifelesslife.enums.AreaRegionType;
import me.netkas.lifelesslife.enums.CardinalDirection;
import me.netkas.lifelesslife.interfaces.RegionInterface;
import me.netkas.lifelesslife.objects.point_region.LineRegion;

public class RoadRegion extends AreaRegion
{
    private final CardinalDirection direction;
    private final String name;

    public RoadRegion(RegionInterface region, CardinalDirection direction, String name)
    {
        super(region);
        this.direction = direction;
        this.name = TextUtilities.capitalizeTitle(String.format("%s road", name));
    }


    @Override
    public AreaRegionType getType()
    {
        return AreaRegionType.ROAD;
    }

    public CardinalDirection getDirection()
    {
        return this.direction;
    }

    @Override
    public String getName()
    {
        return this.name;
    }

    @Override
    public String getFullName()
    {
        if(this.getRegion() instanceof LineRegion)
        {
            return TextUtilities.capitalizeTitle(String.format("%s %s", this.name, this.direction));
        }

        return this.name;
    }
}
