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

    private Handle myHandle;
    private Handle left;
    private Handle right;


    // ----------------------------------------------------------
    /**
     * Create a new BinInternalNode object.
     *
     * @param myHandle
     *            the handle to this node
     */
    public BinInternalNode(Handle myHandle)
    {
        this.myHandle = myHandle;
        left = right = null;
    }


    // ----------------------------------------------------------
    /**
     * Create a new BinInternalNode object.
     *
     * @param myHandle the handle to this node
     * @param left
     *            the left Handle
     * @param right
     *            the right Handle
     */
    public BinInternalNode(Handle myHandle, Handle left, Handle right)
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
    public Handle getMyHandle() {
        return this.myHandle;
    }


    /**
     * Gets the handle to left.
     *
     * @return the left field
     */
    public Handle getLeft()
    {
        return this.left;
    }


    /**
     * Gets the handle to the right.
     *
     * @return the right field
     */
    public Handle getRight()
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
        byte[] byteArray = new byte[9];
        byteArray[0] = 0;
        for (int i = 0; i < 4; i++)
        {
            byteArray[i + 1] =
                (byte)((left.getPosition() >> (24 - 8 * i)) & 0xFF);
            byteArray[i + 5] =
                (byte)((right.getPosition() >> (24 - 8 * i)) & 0xFF);
        }
        return byteArray;
    }
}
