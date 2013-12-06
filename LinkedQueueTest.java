
// -------------------------------------------------------------------------
/**
 *  Tests for LinkedQueue class
 *
 *  @author Alex Kallam
 *  @version Sep 15, 2013
 */
public class LinkedQueueTest extends junit.framework.TestCase
{
    /**
     *
     */
    LinkedQueue<String> test1;
    /**
     *
     */
    String test_string = "hello";


    // ----------------------------------------------------------
    /**
     * Tests the constructor
     */
    public void testConstructor() {
        test1 = new LinkedQueue<String>();
        assertEquals(0, test1.getLength());
    }

    // ----------------------------------------------------------
    /**
     * Tests the inqueue and dequeue methods
     */
    public void testInDeQueue() {
        test1 = new LinkedQueue<String>();
        Link<String> newNode = new Link<String>(test_string);
        test1.inqueue(newNode);
        assertEquals(1, test1.getLength());
        test1.dequeue();
        assertEquals(0, test1.getLength());
        assertEquals(null, test1.dequeue());
        test1.inqueue(test_string);
        assertEquals(test_string, test1.peek());
    }

}
