import java.nio.ByteBuffer;
import java.io.IOException;


// -------------------------------------------------------------------------
/**
 *  Memory Manager that handles Buffer Pool
 *
 *  @author Alex Kallam
 *  @author Connor J. Sullivan
 *  @version Dec 2, 2013
 */
public class MemoryManager {

    private static String FILENAME = "p4bin.dat";
    /** BufferPool object */
    BufferPool pool;
    /** Last position looked at for placement of data */
    int last_viewed;
    /** Number of bytes representing data size */
    private static int SIZE_BYTES = 2;

    // ----------------------------------------------------------
    /**
     * Create a new MemoryManager object.
     * @param buffers number of buffers
     * @param buffer_size size in bytes of each buffer
     * @throws IOException
     */
    public MemoryManager(int buffers, int buffer_size) throws IOException {
        pool = new BufferPool(FILENAME, buffers, buffer_size);
    }

    // ----------------------------------------------------------
    /**
     * Write bytes to write
     * @param b bytes to write
     * @return Handle that points to written data
     */
    public Handle write(byte[] b) {
        // start at last_viewed and find spot big enough

        // return handle
        return write(b, findSpace(b.length));
    }

    // ----------------------------------------------------------
    /**
     * Write bytes to given position
     * @param b bytes to write
     * @param pos position to write to
     * @return Handle that points to written data
     */
    private Handle write(byte[] b, int pos) {


        return null;
    }

    // ----------------------------------------------------------
    /**
     * Find space big enough for data
     * @param size size of data
     * @return Position to write data
     */
    private int findSpace(int size) {


        return 0;
    }

    // ----------------------------------------------------------
    /**
     * Returns bytes for handle
     * @param h handle for retrieval
     * @return bytes for handle
     * @throws IOException
     */
    public byte[] read(Handle h) throws IOException {
        // get size of data
        byte[] size_bytes = pool.getData(h.getPosition(), SIZE_BYTES);
        ByteBuffer bytes = ByteBuffer.wrap(size_bytes);
        int size = bytes.getShort();

        // get handle's data and return bytes

        return null;
    }


}
