import realtimeweb.earthquakeservice.domain.Coordinate;

// -------------------------------------------------------------------------
/**
 * Interface that is used to generalize a class that has a coordinate.
 *
 * @author Connor J. Sullivan (csull)
 * @author Shane Todd (rstodd13)
 * @version 2013.10.14
 */

public interface HasCoordinate
{
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
}
