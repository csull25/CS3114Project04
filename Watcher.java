import java.nio.*;

// -------------------------------------------------------------------------
/**
 * This class represents a Watcher. A watcher has a name and a location. This
 * class implements the Comparable interface to compare its names. (CS 3114
 * Project 01)
 *
 * @author Connor J. Sullivan (csull)
 * @version 2013.09.09
 * @version 2013.09.16 (commenting)
 * @version 2013.10.03 (updates)
 * @version 2013.12.07 (updated)
 */

public class Watcher
    implements HasCoordinateAndHandle
{

    /**
     * The Handle of the watcher
     */
    private int        handle;
    /**
     * The name of the watcher
     */
    private String     name;
    /**
     * The Coordinate of the watcher
     */
    private Coordinate coordinate;


    // ----------------------------------------------------------
    /**
     * Create a new Watcher object.
     *
     * @param handle
     *            (the Handle of the Watcher)
     * @param name
     *            (the name of the Watcher)
     * @param longitude
     *            (the longitude coordinate)
     * @param latitude
     *            (the latitude coordinate)
     */
    public Watcher(int handle, String name, double longitude, double latitude)
    {
        this.handle = handle;
        this.name = name;
        this.coordinate = new Coordinate(longitude, latitude);
    }


    /**
     * @return the handle
     */
    public int getHandle()
    {
        return handle;
    }


    /**
     * @param handle
     *            the handle to be set
     */
    public void setHandle(int handle)
    {
        this.handle = handle;
    }


    /**
     * @return the name
     */
    public String getName()
    {
        return name;
    }


    /**
     * @return the longitude
     */
    public double getLongitude()
    {
        return this.coordinate.getLongitude();
    }


    /**
     * @return the latitude
     */
    public double getLatitude()
    {
        return this.coordinate.getLatitude();
    }


    /**
     * @return the coordinate
     */
    public Coordinate getCoordinate()
    {
        return this.coordinate;
    }


    @Override
    /**
     * Overrides the equals method form Object.
     *
     * @return true if the Watchers have equal names; false otherwise
     */
    public boolean equals(Object obj)
    {
        if (obj == null || !(obj instanceof Watcher))
        {
            return false;
        }
        Watcher other = (Watcher)(obj);
        return this.getName().equals(other.getName());
    }


    @Override
    /**
     * Overrides the toString method.
     *
     * @return the Watcher's name and coordinates
     */
    public String toString()
    {
        return getName() + " " + getCoordinate();
    }


    /**
     * Returns the byte array of the watcher
     */
    public byte[] serialize()
    {
        byte[] ret = new byte[18 + name.length()];
        ret[0] = (byte)((ret.length >> 8) & 0xFF);
        ret[1] = (byte)(ret.length & 0xFF);

        byte[] bytes = new byte[8];
        ByteBuffer.wrap(bytes).putDouble(getLongitude());

        int i;
        for (i = 0; i < 8; i++)
        {
            ret[i + 2] = bytes[i];
        }
        ByteBuffer.wrap(bytes).putDouble(getLatitude());
        for (i = 0; i < 8; i++)
        {
            ret[i + 10] = bytes[i];
        }
        // save name byte array
        for (i = 0; i < name.length(); i++)
        {
            ret[i + 18] = (byte)name.charAt(i);
        }
        return ret;
    }

}
