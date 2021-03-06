import junit.framework.TestCase;

// -------------------------------------------------------------------------
/**
 * Tests the Watcher class. (CS 3114 Project 01)
 *
 * @author Connor J. Sullivan (csull)
 * @version 2013.09.14
 * @version 2013.10.04 (updated)
 * @version 2013.12.07 (updated)
 */

public class WatcherTest
    extends TestCase
{
    /**
     * Watcher used for testing.
     */
    Watcher watcher;


    // ----------------------------------------------------------
    @Override
    protected void setUp()
        throws Exception
    {
        super.setUp();
        watcher = new Watcher(32, "tester", 0, 1);
    }


    /**
     * Test method for {@link Watcher#getHandle()}.
     */
    public void testGetHandle()
    {
        assertEquals(32, watcher.getHandle());
    }


    /**
     * Test method for {@link Watcher#setHandle(int)}.
     */
    public void testSetHandle()
    {
        watcher.setHandle(2);
        assertEquals(2, watcher.getHandle());
    }


    /**
     * Test method for {@link Watcher#getName()}.
     */
    public void testGetName()
    {
        assertEquals("tester", watcher.getName());
    }

    /**
     * Test method for {@link Watcher#setName(String)}.
     */
    public void testSetName()
    {
        watcher.setName("test");
        assertEquals("test", watcher.getName());
    }

    /**
     * Test method for {@link Watcher#getLongitude()}.
     */
    public void testGetLongitude()
    {
        assertEquals(0, watcher.getLongitude(), .001);
    }


    /**
     * Test method for {@link Watcher#getLatitude()}.
     */
    public void testGetLatitude()
    {
        assertEquals(1, watcher.getLatitude(), .001);
    }


    /**
     * Test method for {@link Watcher#getCoordinate()}.
     */
    public void testGetCoordinate()
    {
        assertEquals(0, watcher.getCoordinate().getLongitude(), .001);
        assertEquals(1, watcher.getCoordinate().getLatitude(), .001);
    }


    /**
     * Test method for {@link Watcher#equals(java.lang.Object)}.
     */
    public void testEqualsObject()
    {
        assertFalse(watcher.equals(null));
        assertFalse(watcher.equals("string"));
        assertTrue(watcher.equals(new Watcher(65, "tester", 2, 3)));
        assertFalse(watcher.equals(new Watcher(65, "test", 2, 3)));
    }


    /**
     * Test method for {@link Watcher#toString()}.
     */
    public void testToString()
    {
        assertEquals("tester 0.0 1.0", watcher.toString());
    }


    /**
     * Test method for {@link Watcher#serialize()}.
     */
    public void testSerialize()
    {
        byte[] expected = new byte[24];
        expected[1] = 24;
        expected[10] = (byte)0x3f;
        expected[11] = (byte)0xf0;
        for (int i = 0; i < 6; i++)
        {
            expected[i + 18] = (byte)watcher.getName().charAt(i);
        }
        byte[] actual = watcher.serialize();
        assertEquals(expected.length, actual.length);
        for (int i = 0; i < expected.length; i++)
        {
            assertEquals(expected[i], actual[i]);
        }
    }

}
