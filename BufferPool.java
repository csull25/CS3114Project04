import java.io.IOException;
import java.io.RandomAccessFile;

// -------------------------------------------------------------------------
/**
 * Class that represents a buffer pool. Contains a linked list of buffers. Most
 * of the work is done in the buffer pool class.
 *
 * @author Connor J. Sullivan
 * @version 2013.11.06
 */

public class BufferPool
{
    /**
     * Size of a block: 4096 bytes
     */
    public int           BLOCK_SIZE; //       = 4096;
    /**
     * Size of a record: 4 bytes
     */
    public final int           SIZE_OF_RECORD    = 4;
    /**
     * Number of records per block: 1024 records
     */
    public int           RECORDS_PER_BLOCK; //= 1024;
    private LinkedList<Buffer> pool;
    private RandomAccessFile   file;
    private byte[]             tempArray;
    private int                cacheHits;
    private int                cacheMisses;
    private int                diskReads;
    private int                diskWrites;


    /**
     * Create a new ByteBufferPool object.
     *
     * @param fileName
     *            the name of the file
     * @param numBuffers
     *            number of buffers in the buffer pool
     * @throws IOException
     */
    public BufferPool(String fileName, int numBuffers)
        throws IOException
    {
        BLOCK_SIZE = 4096;
        RECORDS_PER_BLOCK = 1024;
        // create linked list of buffers, open file, create pointer tempArray
        this.pool = new LinkedList<Buffer>();
        this.file = new RandomAccessFile(fileName, "rw");
        this.tempArray = new byte[BLOCK_SIZE];

        // initialize all the statistics to zero
        cacheHits = cacheMisses = diskReads = diskWrites = 0;

        // initialize the appropriate number of buffers
        for (int i = 0; i < numBuffers; i++)
        {
            file.seek(i * BLOCK_SIZE);
            file.read(tempArray);
            cacheMisses++;
            diskReads++;
            pool.append(new Buffer(tempArray, i));
        }
    }


    /**
     * Create a new ByteBufferPool object.
     *
     * @param fileName
     *            the name of the file
     * @param numBuffers
     *            number of buffers in the buffer pool
     * @param bufferSize
     *            size of each buffer in bytes
     * @throws IOException
     */
    public BufferPool(String fileName, int numBuffers, int bufferSize)
        throws IOException
    {
        BLOCK_SIZE = bufferSize;

        // create linked list of buffers, open file, create pointer tempArray
        this.pool = new LinkedList<Buffer>();
        this.file = new RandomAccessFile(fileName, "rw");
        this.tempArray = new byte[BLOCK_SIZE];

        // initialize all the statistics to zero
        cacheHits = cacheMisses = diskReads = diskWrites = 0;

        // initialize the appropriate number of buffers
        for (int i = 0; i < numBuffers; i++)
        {
            file.seek(i * BLOCK_SIZE);
            file.read(tempArray);
            cacheMisses++;
            diskReads++;
            pool.append(new Buffer(tempArray, i));
        }
    }


    /**
     * @return the cacheHits
     */
    public int getCacheHits()
    {
        return cacheHits;
    }


    /**
     * @return the cacheMisses
     */
    public int getCacheMisses()
    {
        return cacheMisses;
    }


    /**
     * @return the diskReads
     */
    public int getDiskReads()
    {
        return diskReads;
    }


    /**
     * @return the diskWrites
     */
    public int getDiskWrites()
    {
        return diskWrites;
    }


    /**
     * Gets the size of the file.
     *
     * @return the fileSize field
     * @throws IOException
     */
    public long getFileSize()
        throws IOException
    {
        return file.length();
    }


    /**
     * Gets the specified buffer
     *
     * @param block
     *            identifies which buffer to get
     * @return the specified ByteBuffer
     * @throws IOException
     */
    public Buffer getBuffer(int block)
        throws IOException
    {
        // move to the front of the linked list
        pool.moveToStart();
        Buffer buffer;

        // while not at the end of the list, look at the current link's buffer.
        // If that is the target buffer, return it, otherwise go to the next
        // link.
        while (pool.getValue() != null)
        {
            if (pool.getValue().getBlockNumber() == block)
            {
                // place the buffer at the front of the list (most recently
                // used), increment the cacheHits, and return the buffer.
                buffer = pool.remove();
                pool.insertAtFront(buffer);
                cacheHits++;
                return buffer;
            }
            else
            {
                pool.current = pool.current.next();
            }
        }

        // increment cacheMisses, remove the last element (least recently used),
        // write its contents to the file if dirty (and increment disk writes),
        // read the new buffer in from the file, and return the buffer.
        cacheMisses++;
        buffer = pool.removeLastElement();
        if (buffer.isDirty())
        {
            file.seek(buffer.getBlockNumber() * BLOCK_SIZE);
            file.write(buffer.readBlock());
            diskWrites++;
        }
        file.seek(block * BLOCK_SIZE);
        file.read(buffer.readBlock());
        diskReads++;
        buffer.setBlockNumber(block);
        pool.insertAtFront(buffer);
        return buffer;
    }


    /**
     * Flushes the remaining buffers.
     *
     * @throws IOException
     */
    public void flushBuffers()
        throws IOException
    {
        pool.moveToStart();
        Buffer buffer;
        while (pool.getValue() != null)
        {
            buffer = pool.remove();
            if (buffer.isDirty())
            {
                file.seek(buffer.getBlockNumber() * BLOCK_SIZE);
                file.write(buffer.readBlock());
                diskWrites++;
            }
        }
    }


    /**
     * Closes the file.
     *
     * @throws IOException
     */
    public void closeFile()
        throws IOException
    {
        this.file.close();
    }


    /**
     * Compares the keys at the given index.
     *
     * @param index1
     *            the first index
     * @param index2
     *            the second index
     * @return a negative number if the first key is smaller, a positive number
     *         if the first key is larger, and zero if the keys are identical
     * @throws IOException
     *
                 public int compareKeys(int index1, int index2) throws
     *             IOException { int[] key1 = new int[2]; int[] key2 = new
     *             int[2]; tempArray = getBuffer(index1 /
     *             RECORDS_PER_BLOCK).readBlock(); key1[0] = tempArray[(index1 *
     *             SIZE_OF_RECORD) % BLOCK_SIZE] & 0xFF; key1[1] =
     *             tempArray[(index1 * SIZE_OF_RECORD) % BLOCK_SIZE + 1] & 0xFF;
     *             tempArray = getBuffer(index2 /
     *             RECORDS_PER_BLOCK).readBlock(); key2[0] = tempArray[(index2 *
     *             SIZE_OF_RECORD) % BLOCK_SIZE] & 0xFF; key2[1] =
     *             tempArray[(index2 * SIZE_OF_RECORD) % BLOCK_SIZE + 1] & 0xFF;
     *             if (key1[0] == key2[0]) { return (short)(key1[1] - key2[1]);
     *             } else { return (short)(key1[0] - key2[0]); } }
     */

    /**
     * Gets the key of the record at the given index.
     *
     * @param index
     *            the index
     * @return the key
     * @throws IOException
     */
    public int getKey(int index)
        throws IOException
    {
        byte[] key = new byte[2];
        tempArray = getBuffer(index / RECORDS_PER_BLOCK).readBlock();
        key[0] = tempArray[(index * SIZE_OF_RECORD) % BLOCK_SIZE];
        key[1] = tempArray[(index * SIZE_OF_RECORD) % BLOCK_SIZE + 1];
        return (key[0] << 8) | key[1] & 0xFF;
    }


    /**
     * Swaps the records at the given indices.
     *
     * @param index1
     *            index of first record
     * @param index2
     *            index of second record
     * @throws IOException
     */
    public void swap(int index1, int index2)
        throws IOException
    {
        // if the indices are in different blocks (sectors)
        if (index1 / RECORDS_PER_BLOCK != index2 / RECORDS_PER_BLOCK)
        {
            // get the record based on the first index
            Buffer buffer = getBuffer(index1 / RECORDS_PER_BLOCK);
            tempArray = buffer.readBlock();
            byte[] temp1 = new byte[4];
            for (int i = 0; i < 4; i++)
            {
                temp1[i] =
                    tempArray[(index1 * SIZE_OF_RECORD) % BLOCK_SIZE + i];
            }

            // get the record for the second index, followed by writing the
            // record based on the first index
            buffer = getBuffer(index2 / RECORDS_PER_BLOCK);
            tempArray = buffer.readBlock();
            byte[] temp2 = new byte[4];
            for (int i = 0; i < 4; i++)
            {
                temp2[i] =
                    tempArray[(index2 * SIZE_OF_RECORD) % BLOCK_SIZE + i];
                tempArray[(index2 * SIZE_OF_RECORD) % BLOCK_SIZE + i] =
                    temp1[i];
            }
            buffer.markDirty();

            // write the record based on the second index
            buffer = getBuffer(index1 / RECORDS_PER_BLOCK);
            tempArray = buffer.readBlock();
            for (int i = 0; i < 4; i++)
            {
                tempArray[(index1 * SIZE_OF_RECORD) % BLOCK_SIZE + i] =
                    temp2[i];
            }
            buffer.markDirty();
        }

        // index1 and index2 are in the same block (sector)
        else
        {
            // standard swap within the same buffer
            Buffer buffer = getBuffer(index1 / RECORDS_PER_BLOCK);
            tempArray = buffer.readBlock();
            byte temp;
            for (int i = 0; i < 4; i++)
            {
                temp = tempArray[(index1 * SIZE_OF_RECORD) % BLOCK_SIZE + i];
                tempArray[(index1 * SIZE_OF_RECORD) % BLOCK_SIZE + i] =
                    tempArray[(index2 * SIZE_OF_RECORD) % BLOCK_SIZE + i];
                tempArray[(index2 * SIZE_OF_RECORD) % BLOCK_SIZE + i] = temp;
            }
            buffer.markDirty();
        }
    }
}
