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
     * Size of a block
     */
    private int                BLOCK_SIZE;
    private int                FILE_SIZE;
    private LinkedList<Buffer> pool;
    private Buffer[]           buffArr;
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
        // create linked list of buffers, open file, create pointer tempArray
        this.pool = new LinkedList<Buffer>();
        this.buffArr = new Buffer[numBuffers];
        this.file = new RandomAccessFile(fileName, "rw");
        this.tempArray = new byte[BLOCK_SIZE];

        // initialize all the statistics to zero
        cacheHits = cacheMisses = diskReads = diskWrites = 0;

        Buffer newBuf;
        // initialize the appropriate number of buffers
        for (int i = 0; i < numBuffers; i++)
        {
            file.seek(i * BLOCK_SIZE);
            file.read(tempArray);
            cacheMisses++;
            diskReads++;
            newBuf = new Buffer(tempArray, i);
            pool.append(newBuf);
            buffArr[i] = newBuf;
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
        FILE_SIZE = 0;

        // create linked list of buffers, open file, create pointer tempArray
        this.pool = new LinkedList<Buffer>();
        this.buffArr = new Buffer[numBuffers];
        this.file = new RandomAccessFile(fileName, "rw");
        this.tempArray = new byte[BLOCK_SIZE];

        expandFile();

        // initialize all the statistics to zero
        cacheHits = cacheMisses = diskReads = diskWrites = 0;

        Buffer newBuf;
        // initialize the appropriate number of buffers
        for (int i = 0; i < numBuffers; i++)
        {
            file.seek(i * BLOCK_SIZE);
            file.read(tempArray);
            cacheMisses++;
            diskReads++;
            newBuf = new Buffer(tempArray, i);
            pool.append(newBuf);
            buffArr[i] = newBuf;
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


    // ----------------------------------------------------------
    /**
     * Place a description of your method here.
     *
     * @param pos
     * @return
     * @throws IOException
     */
    private Buffer getBufferByPosition(int pos)
        throws IOException
    {
        return getBuffer(pos / BLOCK_SIZE);
    }


    // ----------------------------------------------------------
    /**
     * Get array of bytes from file
     *
     * @param pos
     *            position to start
     * @param size
     *            number of bytes to get
     * @return array of bytes
     * @throws IOException
     */
    public byte[] getData(int pos, int size)
        throws IOException
    {
        // data may need to be retrieved from multiple buffers
        byte[] b = new byte[size];
        Buffer buf;
        for (int i = 0; i < size; i++)
        {
            buf = getBufferByPosition(pos + i);
            b[i] = buf.getByte(pos + i);
// System.out.println("byte " + i +": "+ b[i]);
        }
        return b;
    }


    // ----------------------------------------------------------
    /**
     * Write data to buffer(s)
     *
     * @param b
     *            byte array to write
     * @param pos
     *            position to begin writing
     * @throws IOException
     */
    public void writeData(byte[] b, int pos)
        throws IOException
    {
        Buffer buf;
        for (int i = 0; i < b.length; i++)
        {
// System.out.println("byte " + i +": "+ b[i]);
            buf = getBufferByPosition(pos + i);
            buf.setByte(b[i], pos + i);
        }
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


    // ----------------------------------------------------------
    /**
     * Add enough 0 bytes for a new block at end of file
     *
     * @throws IOException
     */
    public void expandFile()
        throws IOException
    {
        file.seek(FILE_SIZE);
        for (int i = 0; i < BLOCK_SIZE; i++)
        {
            file.write(0);
        }
        FILE_SIZE += BLOCK_SIZE;
    }


    // ----------------------------------------------------------
    /**
     * Expand file and add extra size to free block at end of the file
     *
     * @param blocks
     *            queue of blocks
     * @throws IOException
     */
    public void expandFile(LinkedQueue<FreeBlock> blocks)
        throws IOException
    {
        if (blocks.getLength() != 0)
        {
            FreeBlock first = blocks.peek();
            FreeBlock block;
            do
            {
                block = blocks.peek();
                if (block.getSize() + block.getPosition() == FILE_SIZE)
                {
                    // last free block was at end of file and has been expanded
                    block.setSize(block.getSize() + BLOCK_SIZE);
                    break; // jump to file expansion
                }
                blocks.inqueue(blocks.dequeue());
            }
            while (blocks.peek() != first);
        }
        else
        {
            blocks.inqueue(new FreeBlock(FILE_SIZE, BLOCK_SIZE));
        }
        expandFile();
    }


    /**
     * Closes the file.
     *
     * @throws IOException
     */
    public void closeFile()
        throws IOException
    {
        this.flushBuffers();
        this.file.close();
    }


    // ----------------------------------------------------------
    /**
     * Output contents of all buffers to std out
     */
    public String toString()
    {
        String ret = "";
        for (int i = 0; i < buffArr.length; i++)
        {
            ret = ret + "Block ID of buffer" + i + " is "
                + buffArr[i].getBlockNumber() + "\n";
        }
        return ret;
    }

}
