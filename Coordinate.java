// -------------------------------------------------------------------------
/**
 * A Coordinate represents a point in 2 dimensional space. It can be visualized
 * as an (x, y) or (longitude, latitude) ordered pair.
 *
 * @author Connor J. Sullivan (csull)
 * @version 2013.12.04
 */

public class Coordinate
{
    private double longitude;
    private double latitude;


    // ----------------------------------------------------------
    /**
     * Create a new Coordinate object.
     *
     * @param longitude
     *            the longitude
     * @param latitude
     *            the latitude
     */
    public Coordinate(double longitude, double latitude)
    {
        this.longitude = longitude;
        this.latitude = latitude;
    }


    /**
     * Gets the longitude.
     *
     * @return the longitude
     */
    public double getLongitude()
    {
        return longitude;
    }


    /**
     * Gets the latitude.
     *
     * @return the latitude
     */
    public double getLatitude()
    {
        return latitude;
    }


    /**
     * Sets the longitude.
     *
     * @param longitude
     *            the longitude to set
     */
    public void setLongitude(double longitude)
    {
        this.longitude = longitude;
    }


    /**
     * Sets the latitude.
     *
     * @param latitude
     *            the latitude to set
     */
    public void setLatitude(double latitude)
    {
        this.latitude = latitude;
    }


    /**
     * Overrides the equals method in Object. Returns whether or not this object
     * is equal to the given parameter.
     *
     * @param obj
     *            the Object being compared
     * @return true if this and obj are Coordinates with the same longitude and
     *         latitude values; false otherwise
     */
    public boolean equals(Object obj)
    {
        if (!(obj instanceof Coordinate))
        {
            return false;
        }
        Coordinate other = (Coordinate)obj;
        return this.getLongitude() == other.getLongitude()
            && this.getLatitude() == other.getLatitude();
    }


    /**
     * Overrides the toString method in Object. Returns the String
     * representation of the object,
     *
     * @return the String representation
     */
    public String toString()
    {
        return getLongitude() + " " + getLatitude();
    }

}
