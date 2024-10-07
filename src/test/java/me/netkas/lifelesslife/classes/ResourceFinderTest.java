package me.netkas.lifelesslife.classes;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import me.netkas.lifelesslife.enums.ResourceDefinitions;
import me.netkas.lifelesslife.objects.ResourceDefinition;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ResourceFinderTest {

    /**
     * This class tests the behavior of the getResource and readResource methods of the ResourceFinder.
     */

    @Test
    public void testGetResourceValidPath() {
        InputStream resource = ResourceFinder.getResource("definitions_names.yml");
        assertNotNull(resource, "Resource should not be null if path is valid");
    }

    @Test
    public void testGetResourceInvalidPath() {
        InputStream resource = ResourceFinder.getResource("invalid_file.txt");
        assertNull(resource, "Resource should be null if path is invalid");
    }

    @Test
    public void testGetResourceEmptyPath() {
        assertThrows(IllegalArgumentException.class, () -> {
            ResourceFinder.getResource("");
        });
    }

    @Test
    public void testGetResourceNullPath() {
        // Exception expected
        assertThrows(NullPointerException.class, () -> {
            ResourceFinder.getResource(null);
        });
    }

    @Test
    public void testReadResourceValidPath() {
        String resourceContent = ResourceFinder.readResource("definitions_names.yml");
        assertFalse(resourceContent.isEmpty(), "Resource content should not be empty if path is valid");
        assertNotNull(resourceContent, "Resource content should not be null if path is valid");
    }

    @Test
    public void testReadResourceInvalidPath() {
        String resourceContent = ResourceFinder.readResource("invalid_file.txt");
        assertNull(resourceContent, "Resource content should be null if path is invalid");
    }

    @Test
    public void testReadResourceEmptyPath() {
        String resourceContent = ResourceFinder.readResource("");
        assertNull(resourceContent, "Resource content should be null if path is empty");
    }

    /**ecords
     * 
     * This method tests a valid instance of a resource definition.
     * A valid instance should result in a non-empty list return.
     */
    @Test
    public void testGetResourceDefinitionsValidResource() {
        List<ResourceDefinition> resourceDefinitions =
                ResourceFinder.getResourceDefinitions(ResourceDefinitions.NAMES);

        assertFalse(resourceDefinitions.isEmpty(),
                "Resource definitions should not be empty for valid resource");
    }

    /**
     * This method tests null as resource definition.
     * Null should result in an empty list return.
     */
    @Test
    public void testGetResourceDefinitionsNullResource() {
        // Exception expected
        assertThrows(NullPointerException.class, () -> {
            ResourceFinder.getResourceDefinitions(null);
        });
    }

    @Test
    public void testResourceRead() {
        List<ResourceDefinition> resourceDefinitions =
                ResourceFinder.getResourceDefinitions(ResourceDefinitions.NAMES);

        for(ResourceDefinition resource : resourceDefinitions)
        {
            System.out.println(resource.readResource());
        }
    }
}
