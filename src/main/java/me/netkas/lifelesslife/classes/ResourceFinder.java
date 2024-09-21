package me.netkas.lifelesslife.classes;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import me.netkas.lifelesslife.enums.ResourceDefinitions;
import me.netkas.lifelesslife.objects.ResourceDefinition;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
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

    public static List<ResourceDefinition> getResourceDefinitions(ResourceDefinitions resourceDefinition)
    {
        if(resourceDefinition == null)
        {
            throw new NullPointerException("The resource definition cannot be null.");
        }

        final List<ResourceDefinition> resourceDefinitions = new ArrayList<>();
        try (InputStream input = getResource(resourceDefinition.getPath()))
        {
            if (input != null)
            {
                final ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
                final TypeReference<Map<String, List<ResourceDefinition>>> typeRef = new TypeReference<>() {};
                final Map<String, List<ResourceDefinition>> definitions = mapper.readValue(input, typeRef);

                for (Map.Entry<String, List<ResourceDefinition>> entry : definitions.entrySet())
                {
                    resourceDefinitions.addAll(entry.getValue());
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
