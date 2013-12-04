import junit.framework.TestCase;

// -------------------------------------------------------------------------
/**
 * Tests the Coordinate class.
 *
 * @author Connor J. Sullivan (csull)
 * @version 2013.12.04
 */

public class CoordinateTest
    extends TestCase
{
    private Coordinate c;


    // ----------------------------------------------------------
    protected void setUp()
        throws Exception
    {
        super.setUp();
        c = new Coordinate(0, 1);
    }


    /**
     * Test method for {@link Coordinate#getLongitude()}.
     */
    public void testGetLongitude()
    {
        assertEquals(0, c.getLongitude(), .001);
    }


    /**
     * Test method for {@link Coordinate#getLatitude()}.
     */
    public void testGetLatitude()
    {
        assertEquals(1, c.getLatitude(), .001);
    }


    /**
     * Test method for {@link Coordinate#setLongitude(double)}.
     */
    public void testSetLongitude()
    {
        c.setLongitude(2);
        assertEquals(2, c.getLongitude(), .001);
    }


    /**
     * Test method for {@link Coordinate#setLatitude(double)}.
     */
    public void testSetLatitude()
    {
        c.setLatitude(3);
        assertEquals(3, c.getLatitude(), .001);
    }


    /**
     * Test method for {@link Coordinate#equals(java.lang.Object)}.
     */
    public void testEqualsObject()
    {
        assertFalse(c.equals(null));
        assertFalse(c.equals(new Integer(1)));
        Coordinate other = new Coordinate(0, 0);
        assertFalse(c.equals(other));
        other.setLatitude(1);
        assertTrue(c.equals(other));
    }

    /**
     * Test method for {@link Coordinate#toString()}.
     */
    public void testToString()
    {
        assertEquals("0.0 1.0", c.toString());
    }

}
