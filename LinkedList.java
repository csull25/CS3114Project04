/**
 * Adapted by Connor Sullivan. Original is from Clifford A. Shaffer: Source code
 * example for "A Practical Introduction to Data Structures and Algorithm
 * Analysis, 3rd Edition (Java)" by Clifford A. Shaffer Copyright 2008-2011 by
 * Clifford A. Shaffer
 *
 * @author Connor J Sullivan
 * @version 2013.09.15
 * @version 2013.09.16 (commenting)
 * @version 2013.11.05 (modified)
 * @param <T>
 *            generic object type
 */

class LinkedList<T>
{
    /**
     * Pointer to the list header.
     */
    private Link<T>   head;
    /**
     * Pointer to the last element.
     */
    private Link<T>   tail;
    /**
     * Pointer to the current element.
     */
    protected Link<T> current;
    /**
     * The size of the list.
     */
    private int       size;


    /**
     * Constructors
     *
     * @param size
     *            the size of list
     */
    LinkedList(int size)
    {
        this();
    } // Constructor -- Ignore size


    /**
     * Create a new LList object.
     */
    LinkedList()
    {
        current = tail = head = new Link<T>(null); // Create header
        size = 0;
    }


    /** Remove all elements */
    public void clear()
    {
        head.setNext(null); // Drop access to links
        current = tail = head = new Link<T>(null); // Create header
        size = 0;
    }


    /**
     * Inserts a new link with the given value at the front of the list.
     *
     * @param value
     *            the value of the new link
     */
    public void insert(T value)
    {
        current.setNext(new Link<T>(value, current.next()));
        if (tail == current)
        {
            tail = current.next(); // New tail
        }
        size++;
    }


    /**
     * Appends a new link with the given value at the end of the list..
     *
     * @param value
     *            the value of the new link
     */
    public void append(T value)
    {
        // make a new link that follows tail and assign it the given value,
        // then move tail so that it points to the new 'tail' of the linked list
        tail.setNext(new Link<T>(value, null));
        tail = tail.next();
        size++;
    }


    /**
     * Inserts the given element at the front of the list.
     *
     * @param value
     *            given value
     */
    public void insertAtFront(T value)
    {
        current = head;
        current.setNext(new Link<T>(value, current.next()));
        size++;
    }


    /**
     * Removes the current element.
     *
     * @return the removed element
     */
    public T remove()
    {
        if (current.next() == null)
        {
            return null; // Nothing to remove
        }
        T it = current.next().data(); // Remember value
        if (tail == current.next())
        {
            tail = current; // Removed last
        }
        current.setNext(current.next().next()); // Remove from list
        size--; // Decrement count
        return it; // Return value
    }


    /**
     * Returns the last element in the list.
     *
     * @return the last element in the list or null if empty
     */
    public T removeLastElement()
    {
        moveToPosition(size - 1);
        return remove();

    }


    /**
     * Set current at list start
     */
    public void moveToStart()
    {
        current = head;
    }


    /**
     * Set current at list end
     */
    public void moveToEnd()
    {
        current = tail;
    }


    /**
     * * Move current one step left; no change if now at front
     */
    public void prev()
    {
        if (current == head)
        {
            return; // No previous element
        }
        Link<T> temp = head;
        // March down list until we find the previous element
        while (temp.next() != current)
        {
            temp = temp.next();
        }
        current = temp;
    }


    /**
     * Move current one step right; no change if now at end
     */
    public void next()
    {
        if (current != tail)
        {
            current = current.next();
        }
    }


    /**
     * Gets the size of the list.
     *
     * @return List length
     */
    public int getSize()
    {
        return size;
    }


    /**
     * Gets the position of the current element.
     *
     * @return The position of the current element
     */
    public int currentPosition()
    {
        // iterates through the array until temp (the pointer we are watching
        // and incrementing) points to the same link to which current points
        Link<T> temp = head;
        int i = 0;
        while (current != temp)
        {
            temp = temp.next();
            i++;
        }
        return i;
    }


    /**
     * Move down list to "pos" position
     *
     * @param pos
     *            the new position of current in the list
     */
    public void moveToPosition(int pos)
    {
        // check to make sure pos is in the bounds of the linked list
        if (pos < 0 || pos > size)
        {
            throw new IndexOutOfBoundsException(
                "The position that is trying to be reached is out of range.");
        }
        // set current to point to the front of the list and then iterate
        // through the list for the given number of positions
        current = head;
        for (int i = 0; i < pos; i++)
        {
            current = current.next();
        }
    }


    /**
     * Gets the value of the current element.
     *
     * @return Current element value
     */
    public T getValue()
    {
        if (current.next() == null)
        {
            return null;
        }
        return current.next().data();
    }


// Extra stuff not printed in the book.

    /**
     * Generate a human-readable representation of this list's contents that
     * looks something like this: < 1 2 3 | 4 5 6 >. The vertical bar represents
     * the current location of the fence. This method uses toString() on the
     * individual elements.
     *
     * @return The string representation of this list
     */
    @Override
    public String toString()
    {
        // Save the current position of the list
        int oldPos = currentPosition();
        int length = getSize();
        StringBuffer out = new StringBuffer((getSize() + 1) * 4);

        moveToStart();
        out.append("< ");
        for (int i = 0; i < oldPos; i++)
        {
            out.append(getValue());
            out.append(" ");
            next();
        }
        out.append("| ");
        for (int i = oldPos; i < length; i++)
        {
            out.append(getValue());
            out.append(" ");
            next();
        }
        out.append(">");
        moveToPosition(oldPos); // Reset the fence to its original position
        return out.toString();
    }
}
