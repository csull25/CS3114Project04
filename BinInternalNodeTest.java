import junit.framework.TestCase;

// -------------------------------------------------------------------------
/**
 * Tests the BinInternalNode class.
 *
 * @author Connor J. Sullivan (csull)
 * @version 2013.12.07
 */

public class BinInternalNodeTest
    extends TestCase
{
    private BinInternalNode node;


    // ----------------------------------------------------------
    @Override
    protected void setUp()
        throws Exception
    {
        node = new BinInternalNode(5);
    }


    /**
     * Test method for {@link BinInternalNode#getMyHandle()}.
     */
    public void testGetMyHandle()
    {
        assertEquals(5, node.getMyHandle());
    }


    /**
     * Test method for {@link BinInternalNode#setMyHandle(int)}.
     */
    public void testSetMyHandle()
    {
        node.setMyHandle(55);
        assertEquals(55, node.getMyHandle());
    }


    /**
     * Test method for {@link BinInternalNode#getLeft()}.
     */
    public void testGetLeft()
    {
        assertEquals(-1, node.getLeft());
    }


    /**
     * Test method for {@link BinInternalNode#getRight()}.
     */
    public void testGetRight()
    {
        assertEquals(-1, node.getRight());
    }


    /**
     * Test method for {@link BinInternalNode#setLeft(BinNode)}.
     */
    public void testSetLeft()
    {
        node.setLeft(new BinLeafNode(13, 6));
        assertEquals(13, node.getLeft());
    }


    /**
     * Test method for {@link BinInternalNode#setRight(BinNode)}.
     */
    public void testSetRight()
    {
        node.setRight(new BinLeafNode(31, 7));
        assertEquals(31, node.getRight());
    }


    /**
     * Test method for {@link BinInternalNode#isLeafNode()}.
     */
    public void testIsLeafNode()
    {
        assertFalse(node.isLeafNode());
    }


    /**
     * Test method for {@link BinInternalNode#serialize()}.
     */
    public void testSerialize()
    {
        node.setLeft(new BinLeafNode(23, 37));
        node.setRight(new BinLeafNode(9, 16));
        byte[] expected = new byte[11]; // sets values to 0 as default
        expected[1] = 11;
        expected[6] = 23;
        expected[10] = 9;
        byte[] actual = node.serialize();
        assertEquals(expected.length, actual.length);
        for (int i = 0; i < expected.length; i++)
        {
            assertEquals(expected[i], actual[i]);
        }
    }

}
