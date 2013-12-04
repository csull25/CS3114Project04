import junit.framework.TestCase;

// -------------------------------------------------------------------------
/**
 * Tests for the LinkedList class.
 *
 * @author Connor J. Sullivan
 * @version 2013.09.15
 * @version 2013.11.05 (additions for testing new methods)
 */

public class LinkedListTest
    extends TestCase
{
    /**
     * Field representing a LinkedList; to be used for testing.
     */
    private LinkedList<Integer> list;


    // ----------------------------------------------------------
    @Override
    protected void setUp()
        throws Exception
    {
        super.setUp();
        list = new LinkedList<Integer>();
    }


    /**
     * Test method for {@link LinkedList#getSize()}.
     */
    public void testGetSize()
    {
        assertEquals(0, list.getSize());
    }


    /**
     * Test method for {@link LinkedList#clear()}.
     */
    public void testClear()
    {
        list.append(new Integer(1));
        list.clear();
        assertEquals(0, list.getSize());
        assertNull(list.getValue());
    }


    /**
     * Test method for {@link LinkedList#LinkedList(int)}.
     */
    public void testLinkedList()
    {
        LinkedList<Integer> list2 = new LinkedList<Integer>(1);
        assertEquals(0, list2.getSize());
    }


    /**
     * Test method for {@link LinkedList#append(java.lang.Object)}.
     */
    public void testAppend()
    {
        list.append(new Integer(1));
        Integer int2 = new Integer(2);
        list.append(int2);
        list.moveToEnd();
        list.prev();
        assertEquals(int2, list.getValue());
    }


    /**
     * Test method for {@link LinkedList#insert(java.lang.Object)}.
     */
    public void testInsert()
    {
        list.insert(new Integer(1));
        Integer int2 = new Integer(1);
        list.insert(int2);
        list.moveToStart();
        assertEquals(int2, list.getValue());
    }


    /**
     * Test method for {@link LinkedList#remove()}.
     */
    public void testRemove()
    {
        assertNull(list.remove());
        Integer int1 = new Integer(1);
        list.append(int1);
        list.moveToStart();
        assertEquals(int1, list.getValue());
        Integer int2 = new Integer(2);
        list.append(int2);
        list.moveToEnd();
        list.prev();
        assertEquals(int2, list.getValue());
        list.moveToStart();
        assertEquals(int1, list.remove());
        assertEquals(int2, list.getValue());
    }


    /**
     * Test method for {@link LinkedList#prev()}.
     */
    public void testPrev()
    {
        list.prev();
        list.append(new Integer(1));
        list.append(new Integer(2));
        list.prev();
        assertEquals(1, list.getValue().intValue());
    }


    /**
     * Test method for {@link LinkedList#next()}.
     */
    public void testNext()
    {
        list.next();
        list.insert(new Integer(1));
        list.insert(new Integer(2));
        list.next();
        assertEquals(1, list.getValue().intValue());
    }


    /**
     * Test method for {@link LinkedList#currentPosition()}.
     */
    public void testCurrentPosition()
    {
        assertEquals(0, list.currentPosition());
        list.append(new Integer(1));
        assertEquals(0, list.currentPosition());
        list.moveToEnd();
        assertEquals(1, list.currentPosition());
    }


    /**
     * Test method for {@link LinkedList#moveToPosition(int)}.
     */
    public void testMoveToPosition()
    {
        Exception ex = null;
        try
        {
            list.moveToPosition(-1);
        }
        catch (Exception e)
        {
            assertTrue(e instanceof IndexOutOfBoundsException);
            ex = e;
        }
        assertNotNull(ex);
        ex = null;
        try
        {
            list.moveToPosition(1);
        }
        catch (Exception e)
        {
            assertTrue(e instanceof IndexOutOfBoundsException);
            ex = e;
        }
        assertNotNull(ex);
        list.append(new Integer(1));
        list.append(new Integer(2));
        list.moveToPosition(0);
        assertEquals(0, list.currentPosition());
    }


    /**
     * Test method for {@link LinkedList#getValue()}.
     */
    public void testGetValue()
    {
        assertNull(list.getValue());
        Integer int1 = new Integer(1);
        list.insert(int1);
        assertEquals(int1, list.getValue());
    }


    /**
     * Test method for {@link LinkedList#toString()}.
     */
    public void testToString()
    {
        list.append(new Integer(1));
        list.append(new Integer(2));
        list.moveToPosition(1);
        assertEquals("< 1 | 2 >", list.toString());
    }


    /**
     * Test method for {@link LinkedList#insertAtFront(java.lang.Object)}.
     */
    public void testInsertAtFront()
    {
        list.insertAtFront(new Integer(1));
        list.insertAtFront(new Integer(2));
        list.moveToStart();
        assertEquals(2, list.getValue().intValue());
        assertEquals(2, list.getSize());
    }


    /**
     * Test method for {@link LinkedList#removeLastElement()}.
     */
    public void testRemoveLastElement()
    {
        list.insertAtFront(new Integer(1));
        list.insertAtFront(new Integer(2));
        assertEquals(1, list.removeLastElement().intValue());
        assertEquals(1, list.getSize());
    }
}
