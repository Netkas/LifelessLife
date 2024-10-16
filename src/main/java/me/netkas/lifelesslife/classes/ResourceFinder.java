package me.netkas.lifelesslife.classes;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import me.netkas.lifelesslife.enums.ResourceDefinitions;
import me.netkas.lifelesslife.enums.resources.NameResources;
import me.netkas.lifelesslife.objects.ResourceNamedDefinition;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

public final class ResourceFinder {
    private static final ClassLoader classLoader = ResourceFinder.class.getClassLoader();
    private static final Map<NameResources, List<String>> cachedNameResources = new HashMap<>();
    private static final ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

    /**
     * Retrieves a resource as an InputStream given its path.
     *
     * @param path the path to the resource to be retrieved
     * @return an InputStream of the resource or null if the path is invalid, empty, or the resource does not exist
     */
    public static InputStream getResource(String path) {
        if (path == null || path.isEmpty()) {
            throw new NullPointerException("The path to the resource cannot be null or empty.");
        }

        InputStream resourceStream = classLoader.getResourceAsStream(path);
        if (resourceStream == null) {
            throw new RuntimeException("Resource not found: " + path);
        }
        return resourceStream;
    }

    /**
     * Reads the content of a resource file as a String.
     *
     * @param path the path to the resource file
     * @return the content of the resource file as a String
     */
    public static String readResource(String path) {
        try (InputStream input = getResource(path);
             BufferedReader reader = new BufferedReader(new InputStreamReader(input))) {

            return reader.lines().collect(Collectors.joining(System.lineSeparator()));
        } catch (IOException e) {
            throw new RuntimeException("Unable to read the resource file.", e);
        }
    }

    /**
     * Retrieves a list of resource definitions from a specified YAML file.
     *
     * @return a map of parsed {@link ResourceNamedDefinition} objects from the YAML file
     * @throws RuntimeException if there is an error reading or parsing the YAML file
     */
    public static Map<NameResources, ResourceNamedDefinition> getNamedResourceDefinitions() {
        final Map<NameResources, ResourceNamedDefinition> resourceDefinitions = new HashMap<>();
        try (InputStream input = getResource(ResourceDefinitions.NAMES.getPath())) {
            // Creating TypeReference for Map<String, Object>
            final TypeReference<Map<String, Object>> typeRef = new TypeReference<>() {};
            final Map<String, Object> definitions = mapper.readValue(input, typeRef);

            // Convert Map into List of ResourceDefinition
            for (Map.Entry<String, Object> entry : definitions.entrySet()) {
                if (entry.getValue() instanceof Map) {
                    ResourceNamedDefinition resourceDef = new ResourceNamedDefinition((Map<String, Object>) entry.getValue());
                    resourceDefinitions.put(NameResources.valueOf(entry.getKey()), resourceDef);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Unable to read and parse the resource definitions from the YML file.", e);
        }

        return Collections.unmodifiableMap(resourceDefinitions);
    }

    private static void cacheNameResource(NameResources name) {
        InputStream resourceStream = getNamedResourceDefinitions().get(name).getResource();
        if (resourceStream == null) {
            throw new RuntimeException("Resource not found for: " + name);
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(resourceStream))) {
            List<String> lines = reader.lines().collect(Collectors.toList());
            cachedNameResources.put(name, Collections.unmodifiableList(lines));
        } catch (IOException e) {
            throw new RuntimeException("Unable to read the resource file for caching.", e);
        }
    }

    public static String getRandomName(NameResources name, Random random) {
        if (name == null) {
            throw new IllegalArgumentException("The NameResources object cannot be null.");
        }

        if (!cachedNameResources.containsKey(name)) {
            cacheNameResource(name);
        }

        List<String> lines = cachedNameResources.get(name);

        if (lines.isEmpty()) {
            return null;
        }

        int randomLine = (random == null ? new Random() : random).nextInt(lines.size());
        return lines.get(randomLine);
    }
}