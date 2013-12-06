// -------------------------------------------------------------------------
/**
 * Interface that defines a BinNode that will be used as a node in a BinTree.
 *
 * @author Connor J. Sullivan (csull)
 * @version 2013.10.03
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
     * 'Serializes' the node. It converts the node into a byte array.
     *
     * @return the byte array representing this node.
     */
    public byte[] serialize();

}
