package me.netkas.lifelesslife.abstracts;

public abstract class IdentifiableObject
{
    private final String uuid;

    /**
     * Constructs an IdentifiableObject with the specified UUID.
     *
     * @param uuid the unique identifier to assign to this object
     */
    public IdentifiableObject(String uuid)
    {
        this.uuid = uuid;
    }

    /**
     * Default constructor for IdentifiableObject.
     *
     * This constructor initializes the object with a unique identifier (UUID)
     * by generating a random UUID string.
     */
    public IdentifiableObject()
    {
        this.uuid = java.util.UUID.randomUUID().toString();
    }

    /**
     * Retrieves the universally unique identifier (UUID) of this IdentifiableObject.
     *
     * @return the UUID in String format
     */
    public String getUUID()
    {
        return this.uuid;
    }

    /**
     * Returns a string representation of the IdentifiableObject.
     *
     * @return the UUID of this IdentifiableObject in string format
     */
    @Override
    public String toString()
    {
        return this.uuid;
    }
}
