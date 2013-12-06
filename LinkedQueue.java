
// -------------------------------------------------------------------------
/**
 *  Queue comprised of linked Node objects
 *
 *  @author Alex Kallam
 *  @version Sep 9, 2013
 *  @param <T> Type of node
 */
public class LinkedQueue <T>
{
    /** Sentinel node at the front of the queue */
    private Link<T> f_sentinel;

    /** Sentinel node at the back of the queue */
    private Link<T> b_sentinel;

    /** length of queue */
    private int length;

    // ----------------------------------------------------------
    /**
     * Creates an empty LinkedQueue object.
     */
    public LinkedQueue() {
        f_sentinel = new Link<T>(null);
        b_sentinel = new Link<T>(null);
        f_sentinel.link(b_sentinel);
        length = 0;
    }

    // ----------------------------------------------------------
    /**
     * Return the length of the queue
     * @return Length of the queue
     */
    public int getLength() {
        return this.length;
    }

    // ----------------------------------------------------------
    /**
     * Add a Node to the end of the queue
     * @param newNode Node to be added to the queue
     */
    public void inqueue(Link<T> newNode) {
        backNode().link(newNode);
        newNode.link(b_sentinel);
        this.length++;
    }

 // ----------------------------------------------------------
    /**
     * Add a Node to the end of the queue
     * @param data Data for node to be added to the queue
     */
    public void inqueue(T data) {
        Link<T> newNode = new Link<T>(data);
        backNode().link(newNode);
        newNode.link(b_sentinel);
        this.length++;
    }

    /**
     * Remove and return node's data from beginning of queue
     * @return Node at the front of the queue
     */
    public T dequeue() {

        if (frontNode().data() != null) {
            Link<T> ret = frontNode();
            f_sentinel.link(ret.next());
            this.length--;
            return ret.data();
        }
        else {
            return null;
        }
    }

    // ----------------------------------------------------------
    /**
     * Look at data of front node
     * @return Data of front node
     */
    public T peek() {
        return frontNode().data();
    }

    // ----------------------------------------------------------
    /**
     * Get node at the front of the queue (exclude sentinel)
     * @return First queue node
     */
    public Link<T> frontNode() {
        return f_sentinel.next();
    }

    // ----------------------------------------------------------
    /**
     * Get node at the back of the queue (exclude sentinel)
     * @return Last queue node
     */
    public Link<T> backNode() {
        return b_sentinel.prev();
    }

}
