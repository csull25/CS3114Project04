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
}
