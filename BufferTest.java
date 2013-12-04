import junit.framework.TestCase;

// -------------------------------------------------------------------------
/**
 * Tests the Buffer class.
 *
 * @author Connor J. Sullivan
 * @version 2013.11.05
 */

public class BufferTest
    extends TestCase
{

    private Buffer buffer;


    // ----------------------------------------------------------
    protected void setUp()
        throws Exception
    {
        super.setUp();
        buffer = new Buffer();
    }


    /**
     * Test method for {@link Buffer#getBlockNumber()}.
     */
    public void testGetBlockNumber()
    {
        assertEquals(-1, buffer.getBlockNumber());
    }


    /**
     * Test method for {@link Buffer#setBlockNumber(int)}.
     */
    public void testSetBlockNumber()
    {
        buffer.setBlockNumber(1);
        assertEquals(1, buffer.getBlockNumber());
    }


    /**
     * Test method for {@link Buffer#readBlock()}.
     */
    public void testReadBlock()
    {
        assertNull(buffer.readBlock());
    }


    /**
     * Test method for {@link Buffer#markDirty()}.
     */
    public void testMarkDirty()
    {
        buffer.markDirty();
        assertTrue(buffer.isDirty());
    }


    /**
     * Test method for {@link Buffer#isDirty()}.
     */
    public void testIsDirty()
    {
        assertFalse(buffer.isDirty());
    }


    /**
     * Tests non-default constructor.
     */
    public void testConstructor()
    {
        byte[] arr = { 1, 2, 3, 4 };
        Buffer buffer1 = new Buffer(arr, 2);
        for (int i = 0; i < arr.length; i++)
        {
            assertEquals(arr[i], buffer1.readBlock()[i]);
        }
        assertEquals(2, buffer1.getBlockNumber());
        assertFalse(buffer1.isDirty());
    }
}
