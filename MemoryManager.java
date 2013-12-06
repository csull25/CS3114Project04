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
    /** Size of file for storage */
    private int file_size;
    /** Last position looked at for placement of data */
    private int next_pos;
    private LinkedQueue<FreeBlock> free_blocks;
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
        file_size = buffer_size;
    }

    // ----------------------------------------------------------
    /**
     * Write bytes to write
     * @param b bytes to write
     * @return Handle that points to written data
     * @throws IOException
     */
    public Handle write(byte[] b) throws IOException {
        // start at next_pos and find spot big enough
        int pos = findSpace(b.length);
        write(b, pos);
        return new Handle(pos);
    }

    // ----------------------------------------------------------
    /**
     * Write data to handle's position
     * @param b byte array to write
     * @param h handle for position
     * @throws IOException
     */
    public void write(byte[] b, Handle h) throws IOException {
        write(b, h.getPosition());
    }

    // ----------------------------------------------------------
    /**
     * Write bytes to given position
     * @param b bytes to write
     * @param pos position to write to
     * @throws IOException
     */
    private void write(byte[] b, int pos) throws IOException {
        pool.writeData(b, pos);
    }

    // ----------------------------------------------------------
    /**
     * Find space big enough for data
     * @param size size of data
     * @return Position to write data
     * @throws IOException
     */
    private int findSpace(int size) throws IOException {
        int start = next_pos;
        int taken_size;
        do {
            taken_size = bytesToShort(pool.getData(next_pos, 2));
            if (taken_size == 0) {
                // start writing - no data previously written beyond this point
                nextPos(size);
                return next_pos;
            }
            else {
                // jump this data and go to next
                nextPos(taken_size);
            }
        } while (next_pos != start);


        return -1;
    }

    // ----------------------------------------------------------
    /**
     * Increment next_pos for a circular fit
     * @param n number to increase by
     */
    private void nextPos(int n) {
        next_pos = (next_pos + n) % file_size;
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

        // get handle's data and return bytes
        return pool.getData(h.getPosition(), bytesToShort(size_bytes));
    }

    // ----------------------------------------------------------
    /**
     * Return short value of first 2 positions of byte array
     * @param b byte array
     * @return short value of b[0] and b[1]
     */
    private short bytesToShort(byte[] b) {
        ByteBuffer bytes = ByteBuffer.wrap(b);
        return bytes.getShort();
    }


}
