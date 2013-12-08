import java.nio.ByteBuffer;
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
     * Generate long array of bytes
     * @return array of bytes
     */
    public byte[] complexBytes() {
        byte[] w = new byte[2049];
        for (int i = 0; i < w.length; i++) {
            w[i] = (byte)(i % 128);
        }
        byte[] bytes = new byte[4];

        ByteBuffer.wrap(bytes).putInt(2049);
        w[0] = bytes[2];
        w[1] = bytes[3];
        return w;
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
        byte[] b = man.read(0);
        assertEquals(0, b[0]);
        assertEquals(3, b[2]);
    }

    // ----------------------------------------------------------
    /**
     * Read/Write that involves expanding file
     * @throws IOException
     */
    public void testComplexReadWrite() throws IOException {
        setup();
        byte[] w = complexBytes();

        byte[] b = man.read(man.write(w));
        assertEquals(2049, b.length);
        assertEquals(127, b[2047]);
    }

    // ----------------------------------------------------------
    /**
     * Simple remove test
     * @throws IOException
     */
    public void testSimpleRemove() throws IOException {
        setup();
        byte[] w = {0, 3, 3};
        int pos = man.write(w);
        man.remove(pos);
        assertEquals(pos, man.write(w));
    }

    // ----------------------------------------------------------
    /**
     * Complex remove test
     * @throws IOException
     */
    public void testComplexRemove() throws IOException {
        setup();
        byte[] w = {0, 3, 3};
        int pos = man.write(w);
        man.remove(pos);
        assertEquals(pos, man.write(w));
        assertEquals(pos + 3, man.write(w));
        man.remove(pos + 3);
        assertEquals(pos + 3, man.write(w));
        man.remove(pos);
        man.remove(pos + 3);
        assertEquals(pos, man.write(w));
        assertEquals(pos + 3, man.write(w));
    }

    // ----------------------------------------------------------
    /**
     * Complex removal with big byte array
     * @throws IOException
     */
    public void testComplexRemove2() throws IOException {
        setup();
        byte[] w = complexBytes();
        int pos = man.write(w);
        man.remove(pos);
        assertEquals(pos, man.write(w));
    }
}
