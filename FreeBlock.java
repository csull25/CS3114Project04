
// -------------------------------------------------------------------------
/**
 *  Represents a free block in the memory manager
 *
 *  @author Alex Kallam
 *  @version Dec 6, 2013
 */
public class FreeBlock
{
    private int position;
    private int size;

    // ----------------------------------------------------------
    /**
     * Create a new FreeBlock object.
     * @param pos position of block
     * @param size size of block
     */
    public FreeBlock(int pos, int size) {
        this.position = pos;
        this.size = size;
    }

    // ----------------------------------------------------------
    /**
     * Get position of block
     * @return position of block
     */
    public int getPosition() {
        return position;
    }

    // ----------------------------------------------------------
    /**
     * Get size of block
     * @return size of block
     */
    public int getSize() {
        return size;
    }

    // ----------------------------------------------------------
    /**
     * Set the position of the block
     * @param pos position of the block
     */
    public void setPosition(int pos) {
        this.position = pos;
    }

    // ----------------------------------------------------------
    /**
     * Set the size of the block
     * @param size size of the block
     */
    public void setSize(int size) {
        this.size = size;
    }
}
