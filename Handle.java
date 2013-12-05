
// -------------------------------------------------------------------------
/**
 *  Pointer for data in Memory Manager. Allows data to be moved (which updates
 *  position value) without messing up top-level reference.
 *
 *  @author Alex Kallam
 *  @author Connor J. Sullivan
 *  @version Dec 4, 2013
 */
public class Handle
{
    /**  */
    int position;
    // ----------------------------------------------------------
    /**
     * Create a new Handle object.
     * @param pos byte position of object
     */
    public Handle(int pos) {
        this.position = pos;
    }

    // ----------------------------------------------------------
    /**
     * Set the position the handle points to
     * @param pos byte position of data
     */
    public void setPosition(int pos) {
        this.position = pos;
    }

    // ----------------------------------------------------------
    /**
     * Get the position of the handle
     * @return position handle points to
     */
    public int getPosition() {
        return this.position;
    }
}
