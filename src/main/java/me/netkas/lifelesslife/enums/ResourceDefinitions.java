package me.netkas.lifelesslife.enums;

public enum ResourceDefinitions
{
    NAMES("definitions_names.yml");

    private final String path;

    ResourceDefinitions(String path)
    {
        this.path = path;
    }

    public String getPath()
    {
        return path;
    }
}
