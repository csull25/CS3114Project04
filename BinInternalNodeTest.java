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
        node = new BinInternalNode(new Handle(5));
    }


    /**
     * Test method for {@link BinInternalNode#getMyHandle()}.
     */
    public void testMyHandle()
    {
        assertEquals(5, node.getMyHandle().getPosition());
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
        Handle h = new Handle(13);
        node.setLeft(new BinLeafNode(h, new Handle(6)));
        assertEquals(h, node.getLeft());
    }


    /**
     * Test method for {@link BinInternalNode#setRight(BinNode)}.
     */
    public void testSetRight()
    {
        Handle h = new Handle(31);
        node.setRight(new BinLeafNode(h, new Handle(7)));
        assertEquals(h, node.getRight());
    }


    /**
     * Test method for {@link BinInternalNode#isLeafNode()}.
     */
    public void testIsLeafNode()
    {
        assertFalse(node.isLeafNode());
    }

}
