import junit.framework.TestCase;

// -------------------------------------------------------------------------
/**
 * Tests the BinInternalNode class.
 *
 * @author Connor J. Sullivan (csull)
 * @version 2013.10.04
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
        node = new BinInternalNode();
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
     * Test method for {@link BinInternalNode#setLeft(Handle)}.
     */
    public void testSetLeft()
    {
        Handle h = new Handle(13);
        node.setLeft(new BinLeafNode(h).getElement());
        assertEquals(h, node.getLeft());
    }


    /**
     * Test method for {@link BinInternalNode#setRight(Handle)}.
     */
    public void testSetRight()
    {
        Handle h = new Handle(31);
        node.setRight(h);
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
