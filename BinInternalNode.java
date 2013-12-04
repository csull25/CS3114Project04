// -------------------------------------------------------------------------
/**
 * Class that represents an internal node of a BinTree. It contains no data
 * elements, but has pointers to its children.
 *
 * @author Connor J. Sullivan (csull)
 * @version 2013.10.04
 */

public class BinInternalNode
    implements BinNode
{

    private BinNode left;
    private BinNode right;


    // ----------------------------------------------------------
    /**
     * Create a new BinInternalNode object.
     */
    public BinInternalNode()
    {
        left = right = null;
    }


    /**
     * Gets the pointer to left.
     *
     * @return the left field
     */
    public BinNode getLeft()
    {
        return this.left;
    }


    /**
     * Gets the pointer to the right.
     *
     * @return the right field
     */
    public BinNode getRight()
    {
        return this.right;
    }


    /**
     * Sets the pointer left to the new value.
     *
     * @param newLeft
     *            the new left
     */
    public void setLeft(BinNode newLeft)
    {
        this.left = newLeft;
    }


    /**
     * Sets the pointer right to the new value.
     *
     * @param newRight
     *            the new right
     */
    public void setRight(BinNode newRight)
    {
        this.right = newRight;
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

}
