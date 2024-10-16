package me.netkas.lifelesslife.classes;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import me.netkas.lifelesslife.enums.ResourceDefinitions;
import me.netkas.lifelesslife.enums.resources.NameResources;
import me.netkas.lifelesslife.objects.ResourceNamedDefinition;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public final class ResourceFinder
{
    private static final ClassLoader classLoader = ResourceFinder.class.getClassLoader();

    /**
     * Retrieves a resource as an InputStream given its path.
     *
     * @param path the path to the resource to be retrieved
     * @return an InputStream of the resource or null if the path is invalid, empty, or the resource does not exist
     */
    public static InputStream getResource(String path)
    {
        if (path == null)
        {
            throw new NullPointerException("The path to the resource cannot be null or empty.");
        }

        if(path.isEmpty())
        {
            throw new IllegalArgumentException("The path to the resource cannot be null or empty.");
        }

        if (classLoader.getResource(path) == null)
        {
            return null;
        }

        return classLoader.getResourceAsStream(path);
    }

    /**
     * Reads the content of a resource file given its path.
     *
     * @param path the path to the resource to be read
     * @return the content of the resource file as a String, or null if the path is null, empty,
     *         or the resource could not be read
     */
    public static String readResource(String path)
    {
        if (path == null || path.isEmpty())
        {
            return null;
        }

        InputStream resource = getResource(path);
        if (resource == null)
        {
            return null;
        }

        try
        {
            return new String(resource.readAllBytes());
        }
        catch (Exception e)
        {
            return null;
        }
    }

    /**
     * Retrieves a list of resource definitions from a specified YAML file.
     *
     * @return  a map of parsed {@link ResourceNamedDefinition} objects from the YAML file
     * @throws NullPointerException if the provided resource definition is null
     * @throws RuntimeException if there is an error reading or parsing the YAML file
     */
    @SuppressWarnings("unchecked")
    public static Map<NameResources, ResourceNamedDefinition> getNamedResourceDefinitions()
    {
        final Map<NameResources, ResourceNamedDefinition> resourceDefinitions = new HashMap<>();
        try (InputStream input = getResource(ResourceDefinitions.NAMES.getPath()))
        {
            if (input != null)
            {
                final ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
                // Creating TypeReference for Map<String, Object>
                final TypeReference<Map<String, Object>> typeRef = new TypeReference<>() {};
                final Map<String, Object> definitions = mapper.readValue(input, typeRef);

                // Convert Map into List of ResourceDefinition
                for (Map.Entry<String, Object> entry : definitions.entrySet())
                {
                    if (entry.getValue() instanceof Map)
                    {
                        ResourceNamedDefinition resourceDef = new ResourceNamedDefinition((Map<String, Object>) entry.getValue());
                        resourceDefinitions.put(NameResources.valueOf(entry.getKey()), resourceDef);
                    }
                }
            }
        }
        catch (Exception e)
        {
            throw new RuntimeException("Unable to read and parse the resource definitions from the YML file.", e);
        }

        return resourceDefinitions;
    }
}
