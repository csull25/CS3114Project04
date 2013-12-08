// -------------------------------------------------------------------------
/**
 * Class that represents a leaf node for a BinTree. It contains only a single
 * handle to a data element, but has no handles to other nodes. If the handle
 * element is null, then this class represents an empty leaf node.
 *
 * @author Connor J. Sullivan (csull)
 * @version 2013.12.07
 */

public class BinLeafNode
    implements BinNode
{

    private int myHandle;
    private int element;


    // ----------------------------------------------------------
    /**
     * Create a new BinLeafNode object.
     *
     * @param myHandle
     *            the handle to this node
     * @param element
     *            the element stored in the BinLeafNode
     */
    public BinLeafNode(int myHandle, int element)
    {
        this.myHandle = myHandle;
        this.element = element;
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
     * Gets the element contained in this node.
     *
     * @return the element field
     */
    public int getElement()
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
        for (int i = 0; i < byteArray.length; i++)
        {
            byteArray[i + 1] =
                (byte)((element >> (24 - 8 * i)) & 0xFF);
        }
        return byteArray;
    }

}
