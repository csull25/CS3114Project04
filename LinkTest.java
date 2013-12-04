import junit.framework.TestCase;

// -------------------------------------------------------------------------
/**
 * Tests the Link class.
 *
 * @author Connor J. Sullivan
 * @version 2013.09.15
 */

public class LinkTest
    extends TestCase
{
    /**
     * Link used for testing purposes.
     */
    Link<Integer> link;


    // ----------------------------------------------------------
    @Override
    protected void setUp()
        throws Exception
    {
        super.setUp();
        link = new Link<Integer>(new Integer(0));
    }


    /**
     * Test method for {@link Link#data()}.
     */
    public void testData()
    {
        assertEquals(0, link.data().intValue());
    }


    /**
     * Test method for {@link Link#setData(java.lang.Object)}.
     */
    public void testSetData()
    {
        link.setData(new Integer(1));
        assertEquals(1, link.data().intValue());
    }


    /**
     * Test method for {@link Link#next()}.
     */
    public void testNext()
    {
        assertNull(link.next());
        Link<Integer> link2 =
            new Link<Integer>(new Integer(2), new Link<Integer>(new Integer(3)));
        assertEquals(3, link2.next().data().intValue());
    }


    /**
     * Test method for {@link Link#setNext(Link)}.
     */
    public void testSetNext()
    {
        Link<Integer> link1 = new Link<Integer>(new Integer(1));
        link.setNext(link1);
        assertEquals(link1, link.next());
    }
}
