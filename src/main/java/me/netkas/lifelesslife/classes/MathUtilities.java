package me.netkas.lifelesslife.classes;

public final class MathUtilities
{

    public static int calculatePercentage(double currentProgress, double maxValue)
    {
        if(maxValue == 0)
        {
            throw new IllegalArgumentException("Max value cannot be zero");
        }

        return (int)((currentProgress / maxValue) * 100);
    }
}
