package me.netkas.lifelesslife.abstracts;

import me.netkas.lifelesslife.enums.AreaRegionType;
import me.netkas.lifelesslife.interfaces.RegionInterface;

public abstract class AreaRegion
{
    private final RegionInterface region;

    public AreaRegion(RegionInterface region)
    {
        this.region = region;
    }

    public abstract AreaRegionType getType();

    public RegionInterface getRegion()
    {
        return this.region;
    }
}
