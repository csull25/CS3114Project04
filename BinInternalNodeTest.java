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
    public void testMyHandle()
    {
        assertEquals(5, node.getMyHandle());
    }

    /**
     * Test method for {@link BinInternalNode#getLeft()}.
     */
    public void testGetLeft()
    {
        assertNull(node.getLeft());
    }


    /**
     * Test method for {@link BinInternalNode#getRight()}.
     */
    public void testGetRight()
    {
        assertNull(node.getRight());
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
        node.setRight(new BinLeafNode(31,7));
        assertEquals(31, node.getRight());
    }


    /**
     * Test method for {@link BinInternalNode#isLeafNode()}.
     */
    public void testIsLeafNode()
    {
        assertFalse(node.isLeafNode());
    }

}
