import java.io.IOException;
import junit.framework.TestCase;

// -------------------------------------------------------------------------
/**
 *  Tests for the MemoryManager class
 *
 *  @author Alex Kallam
 *  @author Connor J. Sullivan
 *  @version Dec 6, 2013
 */
public class MemoryManagerTest extends TestCase
{
    /**  */
    MemoryManager man;

    // ----------------------------------------------------------
    /**
     * Setup small file with 1 buffer
     * @throws IOException
     */
    public void setup() throws IOException {
        man = new MemoryManager(1, 1024);
    }

    // ----------------------------------------------------------
    /**
     * Tests constructor with small file and 1 buffer
     * @throws IOException
     */
    public void testConstructor() throws IOException {
        setup();
    }

    // ----------------------------------------------------------
    /**
     * Simple read/write tests
     * @throws IOException
     */
    public void testSimpleReadWrite() throws IOException {
        setup();
        byte[] w = {0, 3, 3};
        man.write(w);
        byte[] b = man.read(0, 3);
        assertEquals(0, b[0]);
        assertEquals(3, b[2]);
    }
}
