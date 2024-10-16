package me.netkas.lifelesslife.enums;

public enum DensityLevel
{
    LOW(2),
    MEDIUM(3),
    HIGH(5);

    private final int mainStreetDensity;

    DensityLevel(int mainStreetDensity)
    {
        this.mainStreetDensity = mainStreetDensity;
    }

    public int getMainStreetDensity()
    {
        return mainStreetDensity;
    }
}
