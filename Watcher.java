import realtimeweb.earthquakeservice.domain.Coordinate;

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
 */

public class Watcher
    implements Comparable<Watcher>, HasCoordinate
{
    /**
     * The name of the watcher
     */
    private String     name;
    private Coordinate coordinate;


    // ----------------------------------------------------------
    /**
     * Create a new Watcher object.
     *
     * @param name
     *            (the name of the Watcher)
     * @param longitude
     *            (the longitude coordinate)
     * @param latitude
     *            (the latitude coordinate)
     */
    public Watcher(String name, double longitude, double latitude)
    {
        this.name = name;
        this.coordinate = new Coordinate(longitude, latitude, 0);
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


    // ----------------------------------------------------------
    @Override
    /**
     * Overrides the compareTo method of the Comparable interface. This compares
     * only the names of the two watchers. It utilizes the String compareTo
     * method with regards to the names of the watchers.
     *
     * @return the String compareTo method with regards to the watcher's names
     */
    public int compareTo(Watcher other)
    {
        return this.name.compareTo(other.getName());
    }


    @Override
    /**
     * Overrides the toString method.
     *
     * @return the Watcher's name and coordinates
     */
    public String toString()
    {
        String longitude = "" + getLongitude();
        String latitude = "" + getLatitude();

        longitude = longitude.substring(0, longitude.indexOf('.') + 2);
        latitude = latitude.substring(0, latitude.indexOf('.') + 2);

        return getName() + " " + longitude + " " + latitude;
    }
}
