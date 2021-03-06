import java.nio.ByteBuffer;

// -------------------------------------------------------------------------
/**
 * Class that represents an internal node of a BinTree. It contains no data
 * elements, but has handles to its children.
 *
 * @author Connor J. Sullivan (csull)
 * @version 2013.12.07
 */

public class BinInternalNode
    implements BinNode
{

    private int myHandle;
    private int left;
    private int right;


    // ----------------------------------------------------------
    /**
     * Create a new BinInternalNode object.
     *
     * @param myHandle
     *            the handle to this node
     */
    public BinInternalNode(int myHandle)
    {
        this.myHandle = myHandle;
        left = right = -1;
    }


    // ----------------------------------------------------------
    /**
     * Create a new BinInternalNode object.
     *
     * @param myHandle
     *            the handle to this node
     * @param left
     *            the left Handle
     * @param right
     *            the right Handle
     */
    public BinInternalNode(int myHandle, int left, int right)
    {
        this.myHandle = myHandle;
        this.left = left;
        this.right = right;
    }


    /**
     * Returns the handle of this BinNode.
     *
     * @return the myHandle field
     */
    public int getMyHandle()
    {
        return this.myHandle;
    }


    /**
     * Sets the handle of this BinNode.
     *
     * @param handle
     *            the handle to this node
     */
    public void setMyHandle(int handle)
    {
        this.myHandle = handle;
    }


    /**
     * Gets the handle to left.
     *
     * @return the left field
     */
    public int getLeft()
    {
        return this.left;
    }


    /**
     * Gets the handle to the right.
     *
     * @return the right field
     */
    public int getRight()
    {
        return this.right;
    }


    /**
     * Sets the handle left to the new value.
     *
     * @param newLeft
     *            the new left
     */
    public void setLeft(BinNode newLeft)
    {
        this.left = newLeft.getMyHandle();
    }


    /**
     * Sets the pointer right to the new value.
     *
     * @param newRight
     *            the new right
     */
    public void setRight(BinNode newRight)
    {
        this.right = newRight.getMyHandle();
    }


    // ----------------------------------------------------------
    /**
     * Returns whether or not the node is a leaf node.
     *
     * @return true if it is a leaf node; false otherwise
     */
    public boolean isLeafNode()
    {
        return false;
    }


    // ----------------------------------------------------------
    /**
     * 'Serializes' the node. It converts the node into a byte array.
     *
     * @return the byte array representing this node.
     */
    public byte[] serialize()
    {
        // bytes 0,1 are size, 2 is leafNode indicator,
        // 2-5 are left handle, 6-9 are right handle
        byte[] byteArray = new byte[11];
        byteArray[0] = 0;
        byteArray[1] = 11;
        byteArray[2] = 0;

        byte[] leftArr = new byte[4];
        byte[] rightArr = new byte[4];

        ByteBuffer.wrap(leftArr).putInt(left);
        ByteBuffer.wrap(rightArr).putInt(right);

        for (int i = 0; i < 4; i++)
        {
            byteArray[i + 3] = leftArr[i];
            byteArray[i + 7] = rightArr[i];
        }
        return byteArray;
    }

}
