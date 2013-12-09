import java.io.File;
import java.nio.ByteBuffer;
import java.io.IOException;

// -------------------------------------------------------------------------
/**
 * Memory Manager that handles Buffer Pool
 *
 * @author Alex Kallam
 * @author Connor J. Sullivan
 * @version Dec 2, 2013
 */
public class MemoryManager
{

    private static String          FILENAME   = "p4bin.dat";
    /** BufferPool object */
    BufferPool                     pool;
    /** Last position looked at for placement of data */
    private FreeBlock              next_pos;
    private LinkedQueue<FreeBlock> free_blocks;
    /** Number of bytes representing data size */
    private static int             SIZE_BYTES = 2;


    // ----------------------------------------------------------
    /**
     * Create a new MemoryManager object.
     *
     * @param buffers
     *            number of buffers
     * @param buffer_size
     *            size in bytes of each buffer
     * @throws IOException
     */
    public MemoryManager(int buffers, int buffer_size)
        throws IOException
    {
        // get rid of old file
        new File(FILENAME).delete();

        pool = new BufferPool(FILENAME, buffers, buffer_size);
        free_blocks = new LinkedQueue<FreeBlock>();
        free_blocks.inqueue(new FreeBlock(0, buffer_size));
        next_pos = free_blocks.peek();
    }


    // ----------------------------------------------------------
    /**
     * Write bytes to write
     *
     * @param b
     *            bytes to write
     * @return Handle that points to written data
     * @throws IOException
     */
    public int write(byte[] b)
        throws IOException
    {
        // start at next_pos and find spot big enough
        int pos = findSpace(b.length);
        write(b, pos);
        return pos;
    }


    // ----------------------------------------------------------
    /**
     * Write data to handle's position
     *
     * @param b
     *            byte array to write
     * @param h
     *            handle for position
     * @throws IOException
     */
    public void write(byte[] b, int h)
        throws IOException
    {
        pool.writeData(b, h);
    }


    // ----------------------------------------------------------
    /**
     * Find space big enough for data
     *
     * @param size
     *            size of data
     * @return Position to write data
     * @throws IOException
     */
    private int findSpace(int size)
        throws IOException
    {
        FreeBlock block;
// System.out.println("free blocks: " + free_blocks.getLength());
        if (free_blocks.getLength() == 0)
        {
// System.out.println("no empty blocks");
            pool.expandFile(free_blocks);
            return findSpace(size);
        }

        next_pos = free_blocks.peek();
        do
        {
            block = free_blocks.dequeue();
            if (block.getSize() >= size)
            {
                // start writing - enough data
                if (block.getSize() == size)
                {
                    // free block will be completely filled
                    return block.getPosition();
                }
                // block is bigger than needed space
                int space = block.getPosition();
                block.setPosition(block.getPosition() + size);
                block.setSize(block.getSize() - size);
                free_blocks.inqueue(block);
                return space;
            }
            else
            {
                // jump this data and go to next
// System.out.println("size: " + block.getSize() + "\nsize needed: " + size);
                free_blocks.inqueue(block);
            }
        }
        while (next_pos != free_blocks.peek());
        // no room so add more to file size
// System.out.println("empty blocks are too small, expand file");
        pool.expandFile(free_blocks);
        return findSpace(size);
    }


    // ----------------------------------------------------------
    /**
     * Returns bytes for handle
     *
     * @param h
     *            handle for retrieval
     * @return bytes for handle
     * @throws IOException
     */
    public byte[] read(int h)
        throws IOException
    {
        // get size of data
        byte[] size_bytes = pool.getData(h, SIZE_BYTES);

// System.out.println(h + " size to get: " + bytesToShort(size_bytes));
        // get handle's data and return bytes
        return pool.getData(h, bytesToShort(size_bytes));
    }


    // ----------------------------------------------------------
    /**
     * Returns arbitrary number of bytes for handle
     *
     * @param h
     *            handle for retrieval
     * @param size
     *            number of bytes to retrieve
     * @return bytes for handle
     * @throws IOException
     */
    public byte[] read(int h, int size)
        throws IOException
    {
        return pool.getData(h, size);
    }


    // ----------------------------------------------------------
    /**
     * Remove data from memory file by adding it to free_block queue
     *
     * @param h
     *            handle of data to be removed
     * @throws IOException
     */
    public void remove(int h)
        throws IOException
    {
        FreeBlock b;
        FreeBlock p;
        int size = bytesToShort(pool.getData(h, SIZE_BYTES));

        // size of free block queue makes area of insertion meaningless
        if (free_blocks.getLength() == 0)
        {
            free_blocks.inqueue(new FreeBlock(h, size));
            return;
        }
        else if (free_blocks.getLength() == 1)
        {
            // check to see if blocks should be merged
            p = free_blocks.peek();
            if (p.getPosition() + p.getSize() == h)
            {
                // merge with previous block that is also free
                p.setSize(p.getSize() + size);
                return;
            }
            else if (h + size == p.getPosition())
            {
                // merge with next block that is also free
                p.setSize(p.getSize() + size);
                p.setPosition(p.getPosition() - size);
                return;
            }

            free_blocks.inqueue(new FreeBlock(h, size));
            return;
        }

        // insert free block into correct queue location

        while (true)
        {
            b = free_blocks.dequeue();
            p = free_blocks.peek();
            free_blocks.inqueue(b);
            if (((p.getPosition() > h) && (b.getPosition() < h))
                || ((h >= 0) && (h < p.getPosition()))
                || ((h > p.getPosition()) && (h > b.getPosition())))
            {
                // previous empty block is adjacent to newly freed
                if (b.getPosition() + b.getSize() == h)
                {
                    // System.out.println("pos: " +
// b.getPosition());............
                    // previous block should be merged with new
                    b.setSize(b.getSize() + size);
                    if (b.getPosition() + b.getSize() == p.getPosition())
                    {
                        b.setSize(b.getSize() + p.getSize());
                        free_blocks.dequeue(); // get rid of merged block ref
                    }
                    return;
                }
                if (h + size == p.getPosition())
                {
                    // next block should be merged with new
                    p.setSize(p.getSize() + size);
                    p.setPosition(p.getPosition() - size);
                    return;
                }
                else
                {
                    free_blocks.inqueue(new FreeBlock(h, size));
                    return;
                }
            }
        }
    }


    // ----------------------------------------------------------
    /**
     * Return short value of first 2 positions of byte array
     *
     * @param b
     *            byte array
     * @return short value of b[0] and b[1]
     */
    private short bytesToShort(byte[] b)
    {
        ByteBuffer bytes = ByteBuffer.wrap(b);
        return bytes.getShort();
    }


    // ----------------------------------------------------------
    /**
     * Close the storage file
     *
     * @throws IOException
     */
    public void closeFile()
        throws IOException
    {
        pool.closeFile();
    }

    public String toString() {
        return pool.toString();
    }
}
