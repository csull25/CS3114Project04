import junit.framework.TestCase;

// -------------------------------------------------------------------------
/**
 * Test class for the BinLeafNode class.
 *
 * @author Connor J. Sullivan (csull)
 * @author Shane Todd (rstodd13)
 * @version 2013.10.04
 */

public class BinLeafNodeTest
    extends TestCase
{

    private BinLeafNode<Watcher> node;


    // ----------------------------------------------------------
    @Override
    protected void setUp()
        throws Exception
    {
        node = new BinLeafNode<Watcher>(new Watcher("Test", 0, 1));
    }


    /**
     * Test method for {@link BinLeafNode#getElement()}.
     */
    public void testGetElement()
    {
        assertEquals("Test", node.getElement().getName());
        assertEquals(0.0, node.getElement().getLongitude(), .001);
        assertEquals(1, node.getElement().getLatitude(), .001);
    }


    /**
     * Test method for {@link BinLeafNode#isLeafNode()}.
     */
    public void testIsLeafNode()
    {
        assertTrue(node.isLeafNode());
    }

}
