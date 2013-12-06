// -------------------------------------------------------------------------
/**
 * Class that represents a buffer. Used by the BufferPool, where most of the
 * work is done.
 *
 * @author Connor J. Sullivan
 * @version 2013.11.04
 */

public class Buffer
{
    private byte[]  byteArray;
    private int     blockNumber;
    private boolean isDirty;


    /**
     * Create a new ByteBuffer object.
     */
    public Buffer()
    {
        this.byteArray = null;
        this.blockNumber = -1;
        this.isDirty = false;
    }


    /**
     * Create a new ByteBuffer object.
     *
     * @param byteArray
     *            the byteArray
     * @param block
     *            the block number
     */
    public Buffer(byte[] byteArray, int block)
    {
        this.byteArray = byteArray.clone();
        this.blockNumber = block;
        this.isDirty = false;
    }


    /**
     * Gets the block number.
     *
     * @return the blockNumber field
     */
    public int getBlockNumber()
    {
        return this.blockNumber;
    }


    /**
     * Sets the block number to the given value.
     *
     * @param block
     *            the new block number
     */
    public void setBlockNumber(int block)
    {
        this.blockNumber = block;
        this.isDirty = false;
    }


    /**
     * Read the associated block.
     *
     * @return byte array representing the block
     */
    public byte[] readBlock()
    {
        return byteArray;
    }

    // ----------------------------------------------------------
    /**
     * Get byte from buffer
     * @param pos position of byte in byte array
     * @return Byte from array
     */
    public byte getByte(int pos) {
        return byteArray[pos];
    }

    // ----------------------------------------------------------
    /**
     * Set byte value at given position
     * @param b byte value
     * @param pos position of byte
     */
    public void setByte(byte b, int pos) {
        byteArray[pos] = b;
    }


    /**
     * Marks the buffer as changed, so that when the block is flushed it knows
     * that it needs to be written
     */
    public void markDirty()
    {
        this.isDirty = true;
    }


    /**
     * Gets the value of isDirty.
     *
     * @return isDirty field
     */
    public boolean isDirty()
    {
        return this.isDirty;
    }

}
