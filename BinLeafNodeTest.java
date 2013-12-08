import junit.framework.TestCase;

// -------------------------------------------------------------------------
/**
 * Test class for the BinLeafNode class.
 *
 * @author Connor J. Sullivan (csull)
 * @version 2013.12.07
 */

public class BinLeafNodeTest
    extends TestCase
{

    private BinLeafNode node;


    // ----------------------------------------------------------
    @Override
    protected void setUp()
        throws Exception
    {
        node = new BinLeafNode(1, 47);
    }

    /**
     * Test method for {@link BinLeafNode#getMyHandle()}.
     */
    public void testGetMyHandle()
    {
        assertEquals(1, node.getMyHandle());
    }

    /**
     * Test method for {@link BinLeafNode#getElement()}.
     */
    public void testGetElement()
    {
        assertEquals(47, node.getElement());
    }


    /**
     * Test method for {@link BinLeafNode#isLeafNode()}.
     */
    public void testIsLeafNode()
    {
        assertTrue(node.isLeafNode());
    }

}
