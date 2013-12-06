import junit.framework.TestCase;

// -------------------------------------------------------------------------
/**
 * Test class for the BinLeafNode class.
 *
 * @author Connor J. Sullivan (csull)
 * @version 2013.12.05
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
        node = new BinLeafNode(new Handle(1));
    }


    /**
     * Test method for {@link BinLeafNode#getElement()}.
     */
    public void testGetElement()
    {
        assertEquals(1, node.getElement().getPosition());
    }


    /**
     * Test method for {@link BinLeafNode#isLeafNode()}.
     */
    public void testIsLeafNode()
    {
        assertTrue(node.isLeafNode());
    }

}
