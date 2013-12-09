import java.nio.ByteBuffer;

// -------------------------------------------------------------------------
/**
 * Class that represents a leaf node for a BinTree. It contains only a single
 * handle to a data element, but has no handles to other nodes. If the
 * element handle is -1, then this class represents an empty leaf node.
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
     * @param element
     *            the element stored in the BinLeafNode
     */
    public BinLeafNode(int element)
    {
        // this.myHandle = ???
        this.element = element;
    }


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
        byte[] byteArray = new byte[7];
        byteArray[0] = 0;
        byteArray[1] = 7;
        byteArray[2] = 1;

        byte[] elementArr = new byte[4];
        ByteBuffer.wrap(elementArr).putInt(element);

        for (int i = 0; i < 4; i++)
        {
            byteArray[i + 3] = elementArr[i];
        }
        return byteArray;
    }

}
