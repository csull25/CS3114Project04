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
     * Test method for {@link BinInternalNode#setLeft(BinNode)}.
     */
    public void testSetLeft()
    {
        Watcher test01 = new Watcher("test", 0, 1);
        node.setLeft(new BinLeafNode<Watcher>(test01));
        assertEquals(test01, ((BinLeafNode<?>)(node.getLeft())).getElement());
    }


    /**
     * Test method for {@link BinInternalNode#setRight(BinNode)}.
     */
    public void testSetRight()
    {
        BinInternalNode otherNode = new BinInternalNode();
        node.setRight(otherNode);
        assertEquals(otherNode, node.getRight());
    }


    /**
     * Test method for {@link BinInternalNode#isLeafNode()}.
     */
    public void testIsLeafNode()
    {
        assertFalse(node.isLeafNode());
    }

}
