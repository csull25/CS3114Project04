// -------------------------------------------------------------------------
/**
 * Interface that defines a BinNode that will be used as a node in a BinTree.
 *
 * @author Connor J. Sullivan (csull)
 * @version 2013.12.07
 */

public interface BinNode
{
    /**
     * Returns whether or not the node is a leaf node.
     *
     * @return true if it is a leaf node; false otherwise
     */
    public boolean isLeafNode();


    /**
     * Returns the handle of this BinNode.
     *
     * @return the myHandle field
     */
    public int getMyHandle();

    /**
     * Sets the handle of this BinNode.
     *
     * @param handle
     *            the handle to this node
     */
    public void setMyHandle(int handle);

    /**
     * 'Serializes' the node. It converts the node into a byte array.
     *
     * @return the byte array representing this node.
     */
    public byte[] serialize();

}
