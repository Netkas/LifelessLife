package me.netkas.lifelesslife.enums;

public enum DensityLevel
{
    LOW(1, 9),
    MEDIUM(2, 14),
    HIGH(3, 16);

    private final int mainStreetDensity;
    private final int roadDensity;

    DensityLevel(int mainStreetDensity, int roadDensity)
    {
        this.mainStreetDensity = mainStreetDensity;
        this.roadDensity = roadDensity;
    }

    public int getMainStreetDensity()
    {
        return mainStreetDensity;
    }

    public int getRoadDensity()
    {
        return roadDensity;
    }
}
