package me.netkas.lifelesslife.objects;

import me.netkas.lifelesslife.classes.ResourceFinder;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class ResourceDefinition
{
    private final String folder;
    private final String defaultLocale;
    private final List<String> locales;

    /**
     * Constructs a new ResourceDefinition.
     *
     * @param folder the folder where the resources are located
     * @param defaultLocale the default locale to be used for resources
     * @param locales the list of supported locales
     */
    public ResourceDefinition(String folder, String defaultLocale, List<String> locales)
    {
        this.folder = folder;
        this.defaultLocale = defaultLocale;
        this.locales = locales;
    }

    /**
     * Constructs a new ResourceDefinition instance using data from a provided map.
     *
     * @param data a map containing key-value pairs to initialize the resource definition. The map should have keys:
     *             "folder" (String) - path to the folder,
     *             "default_locale" (String) - default locale identifier,
     *             "locales" (List<String>) - list of supported locales.
     */
    @SuppressWarnings("unchecked")
    public ResourceDefinition(Map<String, Object> data)
    {
        this.folder = (String) data.getOrDefault("folder", null);
        this.defaultLocale = (String) data.getOrDefault("default_locale", null);
        this.locales = (List<String>) data.getOrDefault("locales", new ArrayList<>());
    }

    /**
     * Retrieves the folder associated with the resource definition.
     *
     * @return the folder path as a String
     */
    public String getFolder()
    {
        return folder;
    }

    /**
     * Retrieves the default locale.
     *
     * @return the default locale as a String
     */
    public String getDefaultLocale()
    {
        return defaultLocale;
    }

    /**
     * Retrieves the list of supported locales for this resource definition.
     *
     * @return a List of Strings representing the supported locales.
     */
    public List<String> getLocales()
    {
        return locales;
    }

    /**
     * Retrieves a resource file as an InputStream based on the specified locale.
     *
     * @param locale the locale for which the resource is to be fetched
     * @return an InputStream of the resource file
     * @throws IllegalArgumentException if the specified locale is not acceptable
     */
    public InputStream getResource(String locale)
    {
        if(!this.locales.contains(locale))
        {
            throw new IllegalArgumentException("Locale not acceptable");
        }

        return ResourceFinder.getResource(String.format("%s/%s.txt", this.folder, locale));
    }

    /**
     * Retrieves the default locale resource as an InputStream.
     *
     * @return an InputStream of the default locale resource, or null if the resource does not exist
     */
    public InputStream getResource()
    {
        return ResourceFinder.getResource(String.format("%s/%s.txt", this.folder, this.defaultLocale));
    }

    /**
     * Reads the content of a resource file for the given locale.
     *
     * @param locale the locale for which the resource content is to be read
     * @return the content of the resource file as a String
     * @throws IllegalArgumentException if the provided locale is not acceptable
     */
    public String readResource(String locale)
    {
        if(!this.locales.contains(locale))
        {
            throw new IllegalArgumentException("Locale not acceptable");
        }

        return ResourceFinder.readResource(String.format("%s/%s.txt", this.folder, locale));
    }

    /**
     * Reads the content of the resource file for the default locale.
     * The resource file is located in the specified folder and named according to the default locale.
     *
     * @return the content of the resource file as a String, or null if the resource could not be read
     */
    public String readResource()
    {
        return ResourceFinder.readResource(String.format("%s/%s.txt", this.folder, this.defaultLocale));
    }
}