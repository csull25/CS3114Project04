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
     * Test method for {@link BinLeafNode#setMyHandle(int)}.
     */
    public void testSetMyHandle()
    {
        node.setMyHandle(11);
        assertEquals(11, node.getMyHandle());
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


    /**
     * Test method for {@link BinLeafNode#serialize()}.
     */
    public void testSerialize()
    {
        byte[] expected = new byte[7]; // sets values to 0 as default
        expected[1] = 7;
        expected[2] = 1;
        expected[6] = 47;
        byte[] actual = node.serialize();
        assertEquals(expected.length, actual.length);
        for (int i = 0; i < expected.length; i++)
        {
            assertEquals(expected[i], actual[i]);
        }

    }

}
