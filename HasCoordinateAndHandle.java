// -------------------------------------------------------------------------
/**
 * Interface that is used to generalize a class that has a coordinate.
 *
 * @author Connor J. Sullivan (csull)
 * @version 2013.10.14
 * @version 2013.12.04 (updated)
 */

public interface HasCoordinateAndHandle
{

    /**
     * Gets the Handle of the object
     *
     * @return the handle
     */
    public int getHandle();


    /**
     * Sets the handle of this BinNode.
     *
     * @param handle
     *            the handle to this node
     */
    public void setHandle(int handle);


    /**
     * Gets the coordinate of the object.
     *
     * @return the coordinate
     */
    public Coordinate getCoordinate();


    /**
     * Gets the longitude of the object.
     *
     * @return the longitude
     */
    public double getLongitude();


    /**
     * Gets the latitude of the object.
     *
     * @return the latitude
     */
    public double getLatitude();


    // ----------------------------------------------------------
    /**
     * Get byte array of object
     *
     * @return byte array of object
     */
    public byte[] serialize();
}
