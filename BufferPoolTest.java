import java.io.IOException;
import java.io.RandomAccessFile;
import junit.framework.TestCase;

// -------------------------------------------------------------------------
/**
 * Tests the BufferPool class.
 *
 * @author Connor J. Sullivan
 * @version 2013.11.06
 */

public class BufferPoolTest
    extends TestCase
{

    private BufferPool       pool;
    private RandomAccessFile file;


    // ----------------------------------------------------------
    protected void setUp()
        throws Exception
    {
        super.setUp();
        pool = new BufferPool("Test100_2.dat", 1);
        file = new RandomAccessFile("Test100_2.dat", "rw");
    }


    /**
     * Test method for {@link BufferPool#getFileSize()}.
     *
     * @throws IOException
     */
    public void testGetFileSize()
        throws IOException
    {
        assertEquals(file.length(), pool.getFileSize());
        pool.closeFile();
        file.close();
    }


    /**
     * Test method for {@link BufferPool#getBuffer(int)}.
     *
     * @throws IOException
     */
    public void testGetBuffer()
        throws IOException
    {
        byte[] arr = new byte[4096];
        file.read(arr);
        byte[] array = pool.getBuffer(0).readBlock();
        assertEquals(arr.length, array.length);
        for (int i = 0; i < arr.length; i++)
        {
            if (arr[i] != array[i])
            {
                fail("Test fails. Arrays are not equal at index " + i);
            }
        }
        pool.closeFile();
        file.close();
    }


    /**
     * Test method for {@link BufferPool#getKey(int)}.
     *
     * @throws IOException
     */
    public void testGetKey()
        throws IOException
    {
        assertEquals((32 << 8) + 77, pool.getKey(0));
        pool.closeFile();
        file.close();
    }


    /**
     * Test method for {@link BufferPool#swap(int, int)}.
     *
     * @throws IOException
     */
    public void testSwap()
        throws IOException
    {
        pool.swap(0, 1);
        assertEquals((32 << 8) + 77, pool.getKey(1));
        pool.flushBuffers();
        pool.closeFile();
        file.close();
        file = new RandomAccessFile("Test100_2.dat", "r");
        assertEquals((32 << 8) + 69, file.readShort());
        assertEquals(409600, file.length());
    }


    /**
     * Tests the get methods.
     *
     * @throws IOException
     */
    public void testGets()
        throws IOException
    {
        assertEquals(0, pool.getCacheHits());
        assertEquals(1, pool.getCacheMisses());
        assertEquals(1, pool.getDiskReads());
        assertEquals(0, pool.getDiskWrites());
        pool.closeFile();
        file.close();
    }

}
