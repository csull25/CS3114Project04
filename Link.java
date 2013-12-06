// -------------------------------------------------------------------------
/**
 * A Link represents a single section of a sequence. This link is
 * "doubly-linked" meaning that it has a pointer to the next and prev Links
 * in the sequence. Link Some of the ideas can be
 * contribute to the CS2114 Lab08-DoublyLinkedDeque during the Fall 2012
 * semester with Tony Allevato.
 *
 * @author Connor J. Sullivan
 * @version 2013.09.15
 * @param <T>
 *            a generic object type
 */

public class Link<T>
{
    private T       data;
    private Link<T> next;
    private Link<T> prev;


    // ----------------------------------------------------------
    /**
     * Create a new Link object.
     *
     * @param data
     *            data to be stored in the new Link
     */
    public Link(T data)
    {
        this.data = data;
    }


    // ----------------------------------------------------------
    /**
     * Create a new Link object.
     *
     * @param data
     *            the data of the new link
     * @param next
     *            the Link that follows this new Link
     */
    public Link(T data, Link<T> next)
    {
        this.data = data;
        this.next = next;
    }


    /**
     * Gets the data in the link.
     *
     * @return the data in the link
     */
    public T data()
    {
        return data;
    }


    /**
     * Sets the data in the link.
     *
     * @param newData
     *            the new data to put in the link
     */
    public void setData(T newData)
    {
        data = newData;
    }


    /**
     * Gets the link that follows this one in the sequence.
     *
     * @return the link that follows this one in the sequence
     */
    public Link<T> next()
    {
        return next;
    }


    /**
     * Gets the link that precedes this one in the sequence.
     *
     * @return the link that precedes this one in the sequence
     */
    public Link<T> prev()
    {
        return prev;
    }


    /**
     * Sets the given Link to be the next Link in the sequence
     *
     * @param next
     *            the Link to follow this one
     */
    public void setNext(Link<T> next)
    {
        this.next = next;
    }


    /**
     * Sets the given Link to be the prev Link in the sequence
     *
     * @param prev
     *            the Link to follow this one
     */
    public void setPrev(Link<T> prev)
    {
        this.prev = prev;
    }

    /**
     * Set up forward and backward links for cur and next node
     *
     * @param next_node
     *            node to follow current one
     */
    public void link(Link<T> next_node)
    {
        setNext(next_node);
        next_node.setPrev(this);
    }

}
