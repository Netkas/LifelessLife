package me.netkas.lifelesslife.abstracts;

import me.netkas.lifelesslife.enums.AreaRegionType;
import me.netkas.lifelesslife.interfaces.RegionInterface;

public abstract class AreaRegion extends IdentifiableObject
{
    private final RegionInterface region;

    public AreaRegion(RegionInterface region)
    {
        super();
        this.region = region;
    }

    public abstract AreaRegionType getType();

    public abstract String getName();

    public abstract String getFullName();

    public RegionInterface getRegion()
    {
        return this.region;
    }

    @Override
    public String toString()
    {
        return this.getFullName();
    }
}
