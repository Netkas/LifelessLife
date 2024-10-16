package me.netkas.lifelesslife.abstracts;

import me.netkas.lifelesslife.enums.DensityLevel;
import me.netkas.lifelesslife.objects.AreaChunk;

import java.util.Random;
import java.util.logging.Logger;

public abstract class LayerGenerator
{
    protected final Logger logger = Logger.getLogger(this.getClass().getName());

    public abstract void generateLayer(AreaChunk chunk, DensityLevel level, Random random);
}
