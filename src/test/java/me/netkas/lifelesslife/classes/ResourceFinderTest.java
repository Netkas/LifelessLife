package me.netkas.lifelesslife.classes;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import me.netkas.lifelesslife.enums.resources.NameResources;
import me.netkas.lifelesslife.objects.ResourceNamedDefinition;
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
    public void testGetResourceDefinitionsValidNamedResource() {
        Map<NameResources, ResourceNamedDefinition> resourceDefinitions =
                ResourceFinder.getNamedResourceDefinitions();

        assertFalse(resourceDefinitions.isEmpty(),
                "Resource definitions should not be empty for valid resource");
    }


    @Test
    public void testResourceRead() {
        Map<NameResources, ResourceNamedDefinition> resourceDefinitions =
                ResourceFinder.getNamedResourceDefinitions();

        for(Map.Entry<NameResources, ResourceNamedDefinition> entry : resourceDefinitions.entrySet()) {
            ResourceNamedDefinition resourceNamedDefinition = entry.getValue();
            assertNotNull(entry.getValue(), "Resource name should not be null");
            System.out.println("Resource: " + resourceNamedDefinition.getFolder());

            InputStream resource = resourceNamedDefinition.getResource();
            assertNotNull(resource, "Resource should not be null");

            try
            {
                while (resource.available() > 0) {
                    System.out.print((char) resource.read());
                }
            }
            catch(IOException e)
            {
                fail("IOException occurred while reading resource", e);
            }
        }
    }
}
