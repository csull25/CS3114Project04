// -------------------------------------------------------------------------
/**
 * Class that represents a leaf node for a BinTree. It contains only a single
 * data element, but has no pointers to other nodes. If the element is null,
 * then this class represents an empty leaf node.
 *
 * @author Connor J. Sullivan (csull)
 * @version 2013.10.04
 * @param <T>
 *            a generic object that implements the HasCoordinate interface
 */

public class BinLeafNode<T extends HasCoordinate>
    implements BinNode
{

    private T element;
    private int elementHandle;


    // ----------------------------------------------------------
    /**
     * Create a new BinLeafNode object.
     *
     * @param element
     *            the element stored in the BinLeafNode
     */
    public BinLeafNode(T element)
    {
        this.element = element;
    }


    /**
     * Gets the element contained in this node.
     *
     * @return the element field
     */
    public T getElement()
    {
        return this.element;
    }


    // ----------------------------------------------------------
    /**
     * Returns whether or not the node is a leaf node.
     *
     * @return true if it is a leaf node; false otherwise
     */
    public boolean isLeafNode()
    {
        return true;
    }


    // ----------------------------------------------------------
    /**
     * 'Serializes' the node. It converts the node into a byte array.
     *
     * @return the byte array representing this node.
     */
    public byte[] serialize()
    {
        byte[] byteArray = new byte[5];
        byteArray[0] = 0;
        for (int i = 0; i < 4; i++) {
            byteArray[1+i] = (byte)((elementHandle >> 24 - 8*i) & 0xFF);
        }
        return byteArray;
    }

}
