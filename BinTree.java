import java.nio.ByteBuffer;
import java.io.IOException;

// -------------------------------------------------------------------------
/**
 * This class represents a BinTree. It is a binary tree with multi-dimensional
 * searching features.
 *
 * @author Connor J. Sullivan (csull)
 * @author Alex Kallam ( ?? )
 * @version 2013.12.08
 * @param <T>
 *            a generic object that implements the HasCoordinate interface
 */

public class BinTree<T extends HasCoordinateAndHandle>
{
    private BinNode       root;
    private BinLeafNode   emptyLeafNode;
    private MemoryManager memoryManager;


    /**
     * Create a BinTree object.
     *
     * @param memoryManager
     *            the memoryManger to be used with this BinTree
     * @throws IOException
     */
    public BinTree(MemoryManager memoryManager)
        throws IOException
    {
        this.root = null;
        this.memoryManager = memoryManager;
        this.emptyLeafNode = new BinLeafNode(-1, -1); // -1 for null data
    }


    /**
     * Gets the root of the tree.
     *
     * @return the root field
     */
    public BinNode getRoot()
    {
        return this.root;
    }


    /**
     * Clears or resets the tree to the empty state.
     */
    public void clear()
    {
        root = null;
    }


    /**
     * Gets the BinNode that corresponds to the given handle
     *
     * @param h
     *            the handle of the BinNode
     * @return a BinNode that is represented by h
     * @throws IOException
     */
    public BinNode handleToBinNode(int h)
        throws IOException
    {
        if (h == emptyLeafNode.getMyHandle())
        {
            return emptyLeafNode;
        }
        else
        {
            return deserializeBinNode(h, memoryManager.read(h));
        }
    }


    /**
     * Gets the Coordinate of the HsCoordinate that corresponds to the given
     * handle.
     *
     * @param h
     *            the handle of the HasCoordinate object
     * @return the Coordinate of the HasCoordinate that is represented by h
     * @throws IOException
     */
    public Coordinate handleToCoordinate(int h)
        throws IOException
    {
        return deserializeCoordinate(memoryManager.read(h));
    }


    /**
     * Inserts the given element with the given key into the tree.
     *
     * @param element
     *            the new element to be added
     * @throws IOException
     */
    public void insert(T element)
        throws IOException
    {
        writeNewElement(element);
        // make the new element the root if the root is null
        if (root == null)
        {
            root = new BinLeafNode(element.getHandle());
            writeNewBinNode(root);
        }
        else
        {
            // if the root is a leaf node, make it internal and sets the
            // children appropriately
            if (root.isLeafNode())
            {
                BinInternalNode newRoot = new BinInternalNode(-1);
                writeNewBinNode(newRoot);
                if (handleToCoordinate(((BinLeafNode)root).getElement())
                    .getLongitude() < 0.0)
                {
                    newRoot.setLeft(root);
                    newRoot.setRight(emptyLeafNode);
                }
                else
                {
                    newRoot.setRight(root);
                    newRoot.setLeft(emptyLeafNode);
                }
                writeBinNode(newRoot);
                root = newRoot;
            }
            // call the recursive method
            if (handleToCoordinate(element.getHandle()).getLongitude() < 0.0)
            {
                insert(
                    element.getHandle(),
                    handleToBinNode(((BinInternalNode)root).getLeft()),
                    (BinInternalNode)root,
                    1,
                    0.0,
                    180.0,
                    0.0,
                    180.0,
                    true);
            }
            else
            {
                insert(
                    element.getHandle(),
                    handleToBinNode(((BinInternalNode)root).getRight()),
                    (BinInternalNode)root,
                    1,
                    180.0,
                    360.0,
                    0.0,
                    180.0,
                    false);
            }
        }
    }


    /**
     * Removes the node that contains the watcher that is associated with the
     * given coordinate.
     *
     * @param coordinate
     *            the coordinate being used to find the watcher to be removed
     * @return true if an object was successfully removed; false otherwise
     * @throws IOException
     */
    public boolean remove(Coordinate coordinate)
        throws IOException
    {
        // if the root is null, then nothing can be removed
        if (root == null)
        {
            return false;
        }
        // if the root is a leaf, check if the root contains the specified
        // coordinate. If it matches, then remove it; otherwise don't
        if (root.isLeafNode())
        {
            if (handleToCoordinate(((BinLeafNode)root).getElement()).equals(
                coordinate))
            {
                memoryManager.remove(((BinLeafNode)root).getElement());
                memoryManager.remove(root.getMyHandle());
                root = null;
                return true;
            }
            return false;
        }
        // if the root is internal, then check its children for the specified
        // coordinate and remove it if one matches.
        else
        {
            BinNode leftChild =
                handleToBinNode(((BinInternalNode)root).getLeft());
            BinNode rightChild =
                handleToBinNode(((BinInternalNode)root).getRight());
            if (leftChild.isLeafNode()
                && leftChild != emptyLeafNode
                && handleToCoordinate(((BinLeafNode)leftChild).getElement())
                    .equals(coordinate))
            {
                memoryManager
                    .remove(((BinLeafNode)handleToBinNode(((BinInternalNode)root)
                        .getLeft())).getElement());
                memoryManager.remove(((BinInternalNode)root).getLeft());
                ((BinInternalNode)root).setLeft(emptyLeafNode);
                writeBinNode(root);
                return true;
            }
            if (rightChild.isLeafNode()
                && rightChild != emptyLeafNode
                && handleToCoordinate(((BinLeafNode)rightChild).getElement())
                    .equals(coordinate))
            {
                memoryManager
                    .remove(((BinLeafNode)handleToBinNode(((BinInternalNode)root)
                        .getRight())).getElement());
                memoryManager.remove(((BinInternalNode)root).getRight());
                ((BinInternalNode)root).setRight(emptyLeafNode);
                writeBinNode(root);
                return true;
            }

            // call the recursive method based on the different grandparent and
            // grand-child combinations
            BinInternalNode rootInternal = (BinInternalNode)root;
            if (coordinate.getLongitude() < 0.0)
            {
                if (leftChild.isLeafNode())
                {
                    return false;
                }
                else
                {
                    if (coordinate.getLatitude() < 0.0)
                    {
                        return remove(
                            coordinate,
                            handleToBinNode(((BinInternalNode)handleToBinNode(rootInternal
                                .getLeft())).getLeft()),
                            rootInternal,
                            2,
                            0.0,
                            180.0,
                            0.0,
                            90.0,
                            true,
                            true);
                    }
                    else
                    {
                        return remove(
                            coordinate,
                            handleToBinNode(((BinInternalNode)handleToBinNode(rootInternal
                                .getLeft())).getRight()),
                            rootInternal,
                            2,
                            0.0,
                            180.0,
                            90.0,
                            180.0,
                            true,
                            false);
                    }
                }
            }
            else
            {
                if (rightChild.isLeafNode())
                {
                    return false;
                }
                else
                {
                    if (coordinate.getLatitude() < 0.0)
                    {
                        return remove(
                            coordinate,
                            handleToBinNode(((BinInternalNode)handleToBinNode(rootInternal
                                .getRight())).getLeft()),
                            rootInternal,
                            2,
                            180.0,
                            360.0,
                            0.0,
                            90.0,
                            false,
                            true);
                    }
                    else
                    {
                        return remove(
                            coordinate,
                            handleToBinNode(((BinInternalNode)handleToBinNode(rootInternal
                                .getRight())).getRight()),
                            rootInternal,
                            2,
                            180.0,
                            360.0,
                            90.0,
                            180.0,
                            false,
                            false);
                    }
                }
            }
        } // close of "else if (!root.isALeafNode)
    }


    /**
     * Finds and returns the object associated with the given coordinate.
     *
     * @param coordinate
     *            the key being used to find the object
     * @return the handle associated with the coordinate or -1 if no object was
     *         found
     * @throws IOException
     */
    public int find(Coordinate coordinate)
        throws IOException
    {
        // call the recursive method
        return find(coordinate, root, 0, 0.0, 360.0, 0.0, 180.0);
    }


    /**
     * Searches the given region in the bin tree. This method returns the number
     * of nodes visited during the search. The region is given by a point with a
     * radius.
     *
     * @param longitude
     *            the longitude of the center point
     * @param latitude
     *            the latitude of the center point
     * @param radius
     *            the radius of the region
     * @return number of nodes visited during the search
     * @throws IOException
     */
    public int regionSearch(double longitude, double latitude, double radius)
        throws IOException
    {
        // if the root is null then the tree is empty
        if (root == null)
        {
            return 0;
        }
        // call the recursive method
        return regionSearch(
            root,
            0,
            longitude - radius,
            longitude + radius,
            latitude - radius,
            latitude + radius,
            0.0,
            360.0,
            0.0,
            180.0);
    }


    /**
     * Organizes the bintree.
     *
     * @throws IOException
     */
    public void organize()
        throws IOException
    {
        // nothing needs to be organized if the root is null or a leaf
        if (root == null || root.isLeafNode())
        {
            return;
        }
        // if the root's children are both leaves with one of them being empty,
        // set the root to the non-empty child.
        BinNode leftChild = handleToBinNode(((BinInternalNode)root).getLeft());
        BinNode rightChild =
            handleToBinNode(((BinInternalNode)root).getRight());
        if (leftChild == emptyLeafNode && rightChild.isLeafNode())
        {
            memoryManager.remove(root.getMyHandle());
            root = rightChild;
            return;
        }
        else if (leftChild.isLeafNode() && rightChild == emptyLeafNode)
        {
            memoryManager.remove(root.getMyHandle());
            root = leftChild;
            return;
        }
        // While something was rearranged, continue to call the recursive
        // method.
        while (organize((BinInternalNode)root))
            ;
        leftChild = handleToBinNode(((BinInternalNode)root).getLeft());
        rightChild =
            handleToBinNode(((BinInternalNode)root).getRight());
        if (leftChild == emptyLeafNode && rightChild.isLeafNode())
        {
            memoryManager.remove(root.getMyHandle());
            root = rightChild;
            return;
        }
        else if (leftChild.isLeafNode() && rightChild == emptyLeafNode)
        {
            memoryManager.remove(root.getMyHandle());
            root = leftChild;
            return;
        }
    }


    // ----------------------------------------------------------
    @Override
    /**
     * Overrides the toString() method from Object for the BinTree
     *
     * @return a String representing how this BinTree would be printed
     */
    public String toString()
    {
        String s;
        try
        {
            s = preOrderTraversal(this.root);
        }
        catch (IOException e)
        {
            System.out.println("IOException at preOrderTraversal");
            e.printStackTrace();
            return "IOExcetpion at preOrderTraversal";
        }
        int index;
        // delete the double new lines.
        while (s.contains("\n\n"))
        {
            index = s.indexOf("\n\n");
            s = s.substring(0, index) + s.substring(index + 1);
        }
        // delete the hanging new line at the end of the string
        if (s.substring(s.length() - 1).equals("\n"))
        {
            s = s.substring(0, s.length() - 1);
        }
        // return the string without the extra new line at the front
        return s.substring(1);
    }


    /**
     * Helper method for the insert function. It is a recursive method that
     * traverses the BinTree until the given object is successfully inserted.
     *
     * @param elementHandle
     *            element being inserted
     * @param node
     *            node being looked at
     * @param depth
     *            current depth in the tree
     * @param minLongitude
     *            the minimum longitude for current bounding box
     * @param maxLongitude
     *            the maximum longitude for the current bounding box
     * @param minLatitude
     *            the minimum latitude for the current bounding box
     * @param maxLatitude
     *            the maximum latitude for the current bounding box
     * @throws IOException
     */
    private void insert(
        int elementHandle,
        BinNode node,
        BinInternalNode parent,
        int depth,
        double minLongitude,
        double maxLongitude,
        double minLatitude,
        double maxLatitude,
        boolean nodeIsALeftChild)
        throws IOException
    {
        if (node.isLeafNode())
        {
            // if the node is an empty leaf, make it a filled leaf with the
            // given element
            if (node == emptyLeafNode)
            {
                BinLeafNode newLeafNode = new BinLeafNode(elementHandle);
                writeNewBinNode(newLeafNode);
                if (nodeIsALeftChild)
                {
                    parent.setLeft(newLeafNode);
                }
                else
                {
                    parent.setRight(newLeafNode);
                }
                writeBinNode(parent);
                // return?
            }
            // if the node being looked at is a leaf, then replace it with a
            // new internal node with children being the current node and an
            // empty leaf node. The specifics of the setting are determined by
            // the depth of the tree (for longitude or latitude splitting).
            else
            {
                BinInternalNode newInternalNode = new BinInternalNode(-1);
                writeNewBinNode(newInternalNode);
                if (depth % 2 == 0)
                {
                    if (handleToCoordinate(((BinLeafNode)node).getElement())
                        .getLongitude() + 180 < (minLongitude + maxLongitude) / 2.0)
                    {
                        newInternalNode.setLeft(node);
                        newInternalNode.setRight(emptyLeafNode);
                    }
                    else
                    {
                        newInternalNode.setLeft(emptyLeafNode);
                        newInternalNode.setRight(node);
                    }
                }
                // else if (depth % 2 == 1)
                else
                {
                    if (handleToCoordinate(((BinLeafNode)node).getElement())
                        .getLatitude() + 90 < (minLatitude + maxLatitude) / 2.0)
                    {
                        newInternalNode.setLeft(node);
                        newInternalNode.setRight(emptyLeafNode);
                    }
                    else
                    {
                        newInternalNode.setLeft(emptyLeafNode);
                        newInternalNode.setRight(node);
                    }
                }
                writeBinNode(newInternalNode);
                if (nodeIsALeftChild)
                {
                    parent.setLeft(newInternalNode);
                }
                else
                {
                    parent.setRight(newInternalNode);
                }
                writeBinNode(parent);
                // call the recursive method
                this.insert(
                    elementHandle,
                    newInternalNode,
                    parent,
                    depth,
                    minLongitude,
                    maxLongitude,
                    minLatitude,
                    maxLatitude,
                    nodeIsALeftChild);
            }
        }
        // else if (!node.isLeafNode())
        // Call the recursive method with parameters depending on the depth
        // (for longitude or latitude splitting).
        else
        {
            if (depth % 2 == 0)
            {
                if (handleToCoordinate(elementHandle).getLongitude() + 180.0 < (minLongitude + maxLongitude) / 2.0)
                {
                    insert(
                        elementHandle,
                        handleToBinNode(((BinInternalNode)node).getLeft()),
                        (BinInternalNode)node,
                        depth + 1,
                        minLongitude,
                        (minLongitude + maxLongitude) / 2.0,
                        minLatitude,
                        maxLatitude,
                        true);
                }
                else
                {
                    insert(
                        elementHandle,
                        handleToBinNode(((BinInternalNode)node).getRight()),
                        (BinInternalNode)node,
                        depth + 1,
                        (minLongitude + maxLongitude) / 2.0,
                        maxLongitude,
                        minLatitude,
                        maxLatitude,
                        false);
                }
            }
            else
            {
                if (handleToCoordinate(elementHandle).getLatitude() + 90.0 < (minLatitude + maxLatitude) / 2.0)
                {
                    insert(
                        elementHandle,
                        handleToBinNode(((BinInternalNode)node).getLeft()),
                        (BinInternalNode)node,
                        depth + 1,
                        minLongitude,
                        maxLongitude,
                        minLatitude,
                        (minLatitude + maxLatitude) / 2.0,
                        true);
                }
                else
                {
                    insert(
                        elementHandle,
                        handleToBinNode(((BinInternalNode)node).getRight()),
                        (BinInternalNode)node,
                        depth + 1,
                        minLongitude,
                        maxLongitude,
                        (minLatitude + maxLatitude) / 2.0,
                        maxLatitude,
                        false);
                }
            }
        }
    }


    /**
     * Helper method for the remove function. It is a recursive method that
     * traverses the BinTree until the given key is found, the object matching
     * that key is successfully removed, and the tree is reorganized.
     *
     * @param coordinate
     *            coordinate key associated with the object being removed
     * @param node
     *            node being looked at
     * @param grandparent
     *            the grandparent of the node being looked at
     * @param depth
     *            current node depth in the tree
     * @param minLongitude
     *            the minimum longitude for current bounding box
     * @param maxLongitude
     *            the maximum longitude for the current bounding box
     * @param minLatitude
     *            the minimum latitude for the current bounding box
     * @param maxLatitude
     *            the maximum latitude for the current bounding box
     * @param nodeHasALeftParent
     *            represents if the current node's parent is a left child of the
     *            node's grandparent
     * @param nodeIsALeftGrandchild
     *            represents if the current node is one of the two left
     *            grand-children
     * @throws IOException
     */
    private boolean remove(
        Coordinate coordinate,
        BinNode node,
        BinInternalNode grandparent,
        int depth,
        double minLongitude,
        double maxLongitude,
        double minLatitude,
        double maxLatitude,
        boolean nodeHasALeftParent,
        boolean nodeIsALeftGrandchild)
        throws IOException
    {
        if (node.isLeafNode() && node != emptyLeafNode)
        {
            // If the node being looked at matches the given coordinates, then
            // remove that node by replacing it with an empty leaf node. The
            // specifics of the replacing is determined by the ancestry of the
            // node being replaced.
            if (handleToCoordinate(((BinLeafNode)node).getElement()).equals(
                coordinate))
            {
                if (nodeHasALeftParent)
                {
                    if (nodeIsALeftGrandchild)
                    {
                        memoryManager
                            .remove(((BinLeafNode)handleToBinNode(((BinInternalNode)handleToBinNode(grandparent
                                .getLeft())).getLeft())).getElement());
                        memoryManager
                            .remove(((BinInternalNode)handleToBinNode(grandparent
                                .getLeft())).getLeft());
                        memoryManager.remove(grandparent.getLeft());
                        BinInternalNode parent =
                            (BinInternalNode)handleToBinNode(grandparent
                                .getLeft());
                        parent.setLeft(emptyLeafNode);
                        writeBinNode(parent);
// ((BinInternalNode)handleToBinNode(grandparent.getLeft()))
// .setLeft(emptyLeafNode);
// writeBinNode(handleToBinNode(grandparent.getLeft()));
                        return true;
                    }
                    else
                    {
                        memoryManager
                            .remove(((BinLeafNode)handleToBinNode(((BinInternalNode)handleToBinNode(grandparent
                                .getLeft())).getRight())).getElement());
                        memoryManager
                            .remove(((BinInternalNode)handleToBinNode(grandparent
                                .getLeft())).getRight());
                        memoryManager.remove(grandparent.getLeft());
                        BinInternalNode parent =
                            (BinInternalNode)handleToBinNode(grandparent
                                .getLeft());
                        parent.setRight(emptyLeafNode);
                        writeBinNode(parent);
                        return true;
                    }
                }
                else
                {
                    if (nodeIsALeftGrandchild)
                    {
                        memoryManager
                            .remove(((BinLeafNode)handleToBinNode(((BinInternalNode)handleToBinNode(grandparent
                                .getRight())).getLeft())).getElement());
                        memoryManager
                            .remove(((BinInternalNode)handleToBinNode(grandparent
                                .getRight())).getLeft());
                        memoryManager.remove(grandparent.getRight());
                        BinInternalNode parent =
                            (BinInternalNode)handleToBinNode(grandparent
                                .getRight());
                        parent.setLeft(emptyLeafNode);
                        writeBinNode(parent);
                        return true;
                    }
                    else
                    {
                        memoryManager
                            .remove(((BinLeafNode)handleToBinNode(((BinInternalNode)handleToBinNode(grandparent
                                .getRight())).getRight())).getElement());
                        memoryManager
                            .remove(((BinInternalNode)handleToBinNode(grandparent
                                .getRight())).getRight());
                        memoryManager.remove(grandparent.getRight());
                        BinInternalNode parent =
                            (BinInternalNode)handleToBinNode(grandparent
                                .getRight());
                        parent.setRight(emptyLeafNode);
                        writeBinNode(parent);
                        return true;
                    }
                }
            }
        }
        // if the current node is not a leaf node, then call the recursive
        // method. The parameters of the call i determined by the depth (for
        // longitude or latitude splitting) and whether the node's parent is
        // a left child or not.
        else if (!node.isLeafNode())
        {
            if (depth % 2 == 0)
            {
                if (coordinate.getLongitude() + 180.0 < (minLongitude + maxLongitude) / 2.0)
                {
                    if (nodeHasALeftParent)
                    {
                        return remove(
                            coordinate,
                            handleToBinNode(((BinInternalNode)node).getLeft()),
                            (BinInternalNode)handleToBinNode(grandparent
                                .getLeft()),
                            depth + 1,
                            minLongitude,
                            (minLongitude + maxLongitude) / 2.0,
                            minLatitude,
                            maxLatitude,
                            nodeIsALeftGrandchild,
                            true);
                    }
                    else
                    {
                        return remove(
                            coordinate,
                            handleToBinNode(((BinInternalNode)node).getLeft()),
                            (BinInternalNode)handleToBinNode(grandparent
                                .getRight()),
                            depth + 1,
                            minLongitude,
                            (minLongitude + maxLongitude) / 2.0,
                            minLatitude,
                            maxLatitude,
                            nodeIsALeftGrandchild,
                            true);
                    }
                }
                // else if (depth % 2 == 1)
                else
                {
                    if (nodeHasALeftParent)
                    {
                        return remove(
                            coordinate,
                            handleToBinNode(((BinInternalNode)node).getRight()),
                            (BinInternalNode)handleToBinNode(grandparent
                                .getLeft()),
                            depth + 1,
                            (minLongitude + maxLongitude) / 2.0,
                            maxLongitude,
                            minLatitude,
                            maxLatitude,
                            nodeIsALeftGrandchild,
                            false);
                    }
                    else
                    {
                        return remove(
                            coordinate,
                            handleToBinNode(((BinInternalNode)node).getRight()),
                            (BinInternalNode)handleToBinNode(grandparent
                                .getRight()),
                            depth + 1,
                            (minLongitude + maxLongitude) / 2.0,
                            maxLongitude,
                            minLatitude,
                            maxLatitude,
                            nodeIsALeftGrandchild,
                            false);
                    }
                }
            }
            else
            {
                if (coordinate.getLatitude() + 90.0 < (minLatitude + maxLatitude) / 2.0)
                {
                    if (nodeHasALeftParent)
                    {
                        return remove(
                            coordinate,
                            handleToBinNode(((BinInternalNode)node).getLeft()),
                            (BinInternalNode)handleToBinNode(grandparent
                                .getLeft()),
                            depth + 1,
                            minLongitude,
                            maxLongitude,
                            minLatitude,
                            (minLatitude + maxLatitude) / 2.0,
                            nodeIsALeftGrandchild,
                            true);
                    }
                    else
                    {
                        return remove(
                            coordinate,
                            handleToBinNode(((BinInternalNode)node).getLeft()),
                            (BinInternalNode)handleToBinNode(grandparent
                                .getRight()),
                            depth + 1,
                            minLongitude,
                            maxLongitude,
                            minLatitude,
                            (minLatitude + maxLatitude) / 2.0,
                            nodeIsALeftGrandchild,
                            true);
                    }
                }
                else
                {
                    if (nodeHasALeftParent)
                    {
                        return remove(
                            coordinate,
                            handleToBinNode(((BinInternalNode)node).getRight()),
                            (BinInternalNode)handleToBinNode(grandparent
                                .getLeft()),
                            depth + 1,
                            minLongitude,
                            maxLongitude,
                            (minLatitude + maxLatitude) / 2.0,
                            maxLatitude,
                            nodeIsALeftGrandchild,
                            false);
                    }
                    else
                    {
                        return remove(
                            coordinate,
                            handleToBinNode(((BinInternalNode)node).getRight()),
                            (BinInternalNode)handleToBinNode(grandparent
                                .getRight()),
                            depth + 1,
                            minLongitude,
                            maxLongitude,
                            (minLatitude + maxLatitude) / 2.0,
                            maxLatitude,
                            nodeIsALeftGrandchild,
                            false);
                    }
                }
            }
        }
        return false;
    }


    /**
     * Helper method for the find function. It is a recursive method that
     * traverses the BinTree until the given key is found, in which case the
     * object with which it is associated is returned, or until the function has
     * arrived at a leaf node that doesn't correspond to the given key, in which
     * case null is returned.
     *
     * @param coordinate
     *            coordinate key
     * @param node
     *            node being looked at
     * @param depth
     *            current depth in the tree
     * @param minLongitude
     *            the minimum longitude for current bounding box
     * @param maxLongitude
     *            the maximum longitude for the current bounding box
     * @param minLatitude
     *            the minimum latitude for the current bounding box
     * @param maxLatitude
     *            the maximum latitude for the current bounding box
     * @return the handle associated with the given key, or null if no key is
     *         found to match
     * @throws IOException
     */
    private int find(
        Coordinate coordinate,
        BinNode node,
        int depth,
        double minLongitude,
        double maxLongitude,
        double minLatitude,
        double maxLatitude)
        throws IOException
    {
        // If the node is null, then the coordinate wasn't found so return -1.
        if (node == null)
        {
            return -1;
        }
        // If the node is a leaf, then check the element's coordinates. If they
        // match, then return the element; otherwise, return -1.
        if (node.isLeafNode())
        {
            int elementHandle = ((BinLeafNode)node).getElement();
            if (node != emptyLeafNode
                && handleToCoordinate(elementHandle).equals(coordinate))
            {
                return elementHandle;
            }
            else
            {
                return -1;
            }
        }
        // Call the recursive method with parameters depending on the depth
        // (for longitude or latitude splitting).
        else
        {
            if (depth % 2 == 0)
            {
                if (coordinate.getLongitude() + 180.0 < (minLongitude + maxLongitude) / 2.0)
                {
                    return find(
                        coordinate,
                        handleToBinNode(((BinInternalNode)node).getLeft()),
                        depth + 1,
                        minLongitude,
                        (minLongitude + maxLongitude) / 2.0,
                        minLatitude,
                        maxLatitude);
                }
                else
                {
                    return find(
                        coordinate,
                        handleToBinNode(((BinInternalNode)node).getRight()),
                        depth + 1,
                        (minLongitude + maxLongitude) / 2.0,
                        maxLongitude,
                        minLatitude,
                        maxLatitude);
                }
            }
            else
            {
                if (coordinate.getLatitude() + 90.0 < (minLatitude + maxLatitude) / 2.0)
                {
                    return find(
                        coordinate,
                        handleToBinNode(((BinInternalNode)node).getLeft()),
                        depth + 1,
                        minLongitude,
                        maxLongitude,
                        minLatitude,
                        (minLatitude + maxLatitude) / 2.0);
                }
                else
                {
                    return find(
                        coordinate,
                        handleToBinNode(((BinInternalNode)node).getRight()),
                        depth + 1,
                        minLongitude,
                        maxLongitude,
                        (minLatitude + maxLatitude) / 2.0,
                        maxLatitude);
                }
            }
        } // close of the "not a leaf node" else
    }


    /**
     * Helper method for the regionSearch function. It is a recursive method
     * that traverses the regions of the tree that are within / touch the given
     * bounding box. It also returns the number of nodes that it visits.
     *
     * @param node
     *            the current node being looked at
     * @param depth
     *            the depth of the current node
     * @param minLongitude
     *            the minimum longitude
     * @param maxLongitude
     *            the maximum longitude
     * @param minLatitude
     *            the minimum latitude
     * @param maxLatitude
     *            the maximum latitude
     * @param currentMinLongitude
     *            the current minimum longitude
     * @param currentMaxLongitude
     *            the current maximum longitude
     * @param currentMinLatitude
     *            the current minimum latitude
     * @param currentMaxLatitude
     *            the current maximum latitude
     * @return the number of nodes that are visited
     * @throws IOException
     */
    private int regionSearch(
        BinNode node,
        int depth,
        double minLongitude,
        double maxLongitude,
        double minLatitude,
        double maxLatitude,
        double currentMinLongitude,
        double currentMaxLongitude,
        double currentMinLatitude,
        double currentMaxLatitude)
        throws IOException
    {
        // If the node is a filled leaf node, check that its element is within
        // the bounds of the biggest circle inscribed in the given bounding
        // box. If so, print that element.
        if (node.isLeafNode())
        {
            if (node != emptyLeafNode)
            {
                int element = ((BinLeafNode)node).getElement();
                if (Math.pow(handleToCoordinate(element).getLatitude()
                    - (minLatitude + maxLatitude) / 2.0, 2)
                    + Math.pow(handleToCoordinate(element).getLongitude()
                        - (minLongitude + maxLongitude) / 2.0, 2) < Math.pow(
                    (maxLongitude - minLongitude) / 2.0,
                    2))
                {
                    System.out.println(handleToName(element) + " "
                        + handleToCoordinate(element));
                }
            }
            return 1;
        }
        // else if (!node.isLeafNode())
        // Call the recursive method with parameters based on the depth (for
        // longitude or latitude splitting). The recursive call may call both
        // left and right subtrees if the bounding box covers both. This is
        // done through the additional checks.
        else
        {
            if (depth % 2 == 0)
            {
                if (minLongitude + 180 < (currentMinLongitude + currentMaxLongitude) / 2.0
                    && maxLongitude + 180 >= (currentMinLongitude + currentMaxLongitude) / 2.0)
                {
                    return 1
                        + regionSearch(
                            handleToBinNode(((BinInternalNode)node).getLeft()),
                            depth + 1,
                            minLongitude,
                            maxLongitude,
                            minLatitude,
                            maxLatitude,
                            currentMinLongitude,
                            (currentMinLongitude + currentMaxLongitude) / 2.0,
                            currentMinLatitude,
                            currentMaxLatitude)
                        + regionSearch(
                            handleToBinNode(((BinInternalNode)node).getRight()),
                            depth + 1,
                            minLongitude,
                            maxLongitude,
                            minLatitude,
                            maxLatitude,
                            (currentMinLongitude + currentMaxLongitude) / 2.0,
                            currentMaxLongitude,
                            currentMinLatitude,
                            currentMaxLatitude);
                }
                else if (minLongitude + 180 < (currentMinLongitude + currentMaxLongitude) / 2.0)
                {
                    return 1 + regionSearch(
                        handleToBinNode(((BinInternalNode)node).getLeft()),
                        depth + 1,
                        minLongitude,
                        maxLongitude,
                        minLatitude,
                        maxLatitude,
                        currentMinLongitude,
                        (currentMinLongitude + currentMaxLongitude) / 2.0,
                        currentMinLatitude,
                        currentMaxLatitude);
                }
                else if (maxLongitude + 180 >= (currentMinLongitude + currentMaxLongitude) / 2.0)
                {
                    return 1 + regionSearch(
                        handleToBinNode(((BinInternalNode)node).getRight()),
                        depth + 1,
                        minLongitude,
                        maxLongitude,
                        minLatitude,
                        maxLatitude,
                        (currentMinLongitude + currentMaxLongitude) / 2.0,
                        currentMaxLongitude,
                        currentMinLatitude,
                        currentMaxLatitude);
                }
            }
            else
            {
                if (minLatitude + 90 < (currentMinLatitude + currentMaxLatitude) / 2.0
                    && maxLatitude + 90 >= (currentMinLatitude + currentMaxLatitude) / 2.0)
                {
                    return 1
                        + regionSearch(
                            handleToBinNode(((BinInternalNode)node).getLeft()),
                            depth + 1,
                            minLongitude,
                            maxLongitude,
                            minLatitude,
                            maxLatitude,
                            currentMinLongitude,
                            currentMaxLongitude,
                            currentMinLatitude,
                            (currentMinLatitude + currentMaxLatitude) / 2.0)
                        + regionSearch(
                            handleToBinNode(((BinInternalNode)node).getRight()),
                            depth + 1,
                            minLongitude,
                            maxLongitude,
                            minLatitude,
                            maxLatitude,
                            currentMinLongitude,
                            currentMaxLongitude,
                            (currentMinLatitude + currentMaxLatitude) / 2.0,
                            currentMaxLatitude);
                }
                else if (minLatitude + 90 < (currentMinLatitude + currentMaxLatitude) / 2.0)
                {
                    return 1 + regionSearch(
                        handleToBinNode(((BinInternalNode)node).getLeft()),
                        depth + 1,
                        minLongitude,
                        maxLongitude,
                        minLatitude,
                        maxLatitude,
                        currentMinLongitude,
                        currentMaxLongitude,
                        currentMinLatitude,
                        (currentMinLatitude + currentMaxLatitude) / 2.0);
                }
                else if (maxLatitude + 90 >= (currentMinLatitude + currentMaxLatitude) / 2.0)
                {
                    return 1 + regionSearch(
                        handleToBinNode(((BinInternalNode)node).getRight()),
                        depth + 1,
                        minLongitude,
                        maxLongitude,
                        minLatitude,
                        maxLatitude,
                        currentMinLongitude,
                        currentMaxLongitude,
                        (currentMinLatitude + currentMaxLatitude) / 2.0,
                        currentMaxLatitude);
                }
            }
        }
        return 0;
    }


    /**
     * Returns a String representing the pre-order traversal of the tree. The
     * formatting conforms to the specifications stated in the project. It is a
     * recursive method.
     *
     * @param node
     *            the current node that is being looked at
     * @return string representation of an in order traversal
     * @throws IOException
     */
    private String preOrderTraversal(BinNode node)
        throws IOException
    {
        // If the node is null (i.e. the root is null), then print E because it
        // could also be represented as an empty leaf node.
        if (node == null)
        {
            return "\nE";
        }
        if (node.isLeafNode())
        {
            // If the node is an empty leaf node, print E.
            if (node == emptyLeafNode)
            {
                return "\nE";
            }
            // If the node is a filled leaf node, print its element.
            else
            {
                return "\n" + handleToName(((BinLeafNode)node).getElement())
                    + " "
                    + handleToCoordinate(((BinLeafNode)node).getElement())
                    + "\n";
                // TO-DO
            }
        }
        // If the node is internal, print I and then call the recursive method
        // for its left subtree and right subtree.
        else
        {
            return "\nI"
                + preOrderTraversal(handleToBinNode(((BinInternalNode)node)
                    .getLeft()))
                + preOrderTraversal(handleToBinNode(((BinInternalNode)node)
                    .getRight()));
        }
    }


    /**
     * Organizes the bintree by pushing hanging filled leaf nodes (filled leaf
     * nodes whose ancestors are internal nods with an internal node child and
     * an empty leaf node child) 'up' the tree.
     *
     * @param grandparent
     *            a node with grand-child
     * @return true if the tree was altered; false otherwise
     * @throws IOException
     */
    private boolean organize(BinInternalNode grandparent)
        throws IOException
    {
        BinNode leftParent = handleToBinNode(grandparent.getLeft());
        BinNode rightParent = handleToBinNode(grandparent.getRight());
        BinNode leftChild, rightChild;
        if (!leftParent.isLeafNode())
        {
            leftChild =
                handleToBinNode(((BinInternalNode)leftParent).getLeft());
            rightChild =
                handleToBinNode(((BinInternalNode)leftParent).getRight());

            // If the leftParent has a filled leaf node and an empty leaf node,
            // replace the leftParent with its filled leaf node child.
            if (leftChild.isLeafNode() && rightChild.isLeafNode())
            {
                if (leftChild == emptyLeafNode && rightChild != emptyLeafNode)
                {
                    grandparent.setLeft(rightChild);
                    writeBinNode(grandparent);
                    memoryManager.remove(leftParent.getMyHandle());
                    return true;
                }
                else if (leftChild != emptyLeafNode
                    && rightChild == emptyLeafNode)
                {
                    grandparent.setLeft(leftChild);
                    writeBinNode(grandparent);
                    memoryManager.remove(leftParent.getMyHandle());
                    return true;
                }
            }
            // Call the recursive method. If the recursive call returned true,
            // then return true, but if the recursive call returned false,
            // continue through the method (without returning).
            else
            {
                if (organize((BinInternalNode)leftParent))
                {
                    return true;
                }
            }
        }
        if (!rightParent.isLeafNode())
        {
            leftChild =
                handleToBinNode(((BinInternalNode)rightParent).getLeft());
            rightChild =
                handleToBinNode(((BinInternalNode)rightParent).getRight());
            // If the rightParent has a filled leaf node and an empty leaf node,
            // replace the leftParent with its filled leaf node child.
            if (leftChild.isLeafNode() && rightChild.isLeafNode())
            {
                if (leftChild == emptyLeafNode && rightChild != emptyLeafNode)
                {
                    grandparent.setRight(rightChild);
                    writeBinNode(grandparent);
                    memoryManager.remove(rightParent.getMyHandle());
                    return true;
                }
                else if (leftChild != emptyLeafNode
                    && rightChild == emptyLeafNode)
                {
                    grandparent.setRight(leftChild);
                    writeBinNode(grandparent);
                    memoryManager.remove(rightParent.getMyHandle());
                    return true;
                }
            }
            else
            {
                // Call the recursive method. If the recursive call returned
                // true, then return true, but if the recursive call returned
                // false, continue through the method (without returning).
                if (organize((BinInternalNode)rightParent))
                {
                    return true;
                }
            }
        }
        return false;
    }


    /**
     * 'De-serializes' the node. It converts the given byte array into a
     * binNode.
     *
     * @param myHandle
     *            the handle of this BinNode
     * @param byteArray
     *            the byteArray representation to be converted
     * @return the BinNode that was represented by the given byte array
     */
    private BinNode deserializeBinNode(int myHandle, byte[] byteArray)
    {
        if (byteArray[2] == 1) // it is a leaf node
        {
            return new BinLeafNode(myHandle, ByteBuffer.wrap(byteArray).getInt(
                3));
        }
        else
        {
            ByteBuffer bb = ByteBuffer.wrap(byteArray);
            return new BinInternalNode(myHandle, bb.getInt(3), bb.getInt(7));
        }
    }


    /**
     * 'De-serializes' the coordinate. It converts the given byte array into a
     * Coordinate.
     *
     * @param byteArray
     *            the byteArray representation to be converted
     * @return the Coordinate that was represented by the given byteArray
     */
    private Coordinate deserializeCoordinate(byte[] byteArray)
    {
        ByteBuffer bb = ByteBuffer.wrap(byteArray);
        return new Coordinate(bb.getDouble(2), bb.getDouble(10));
    }


    /**
     * Returns the name corresponding to the HasCoordinateAndHandle object
     * represented at the given handle.
     *
     * @param h
     *            the handle of this object
     * @return the name of the HasCoordinateAndHandle object (T)
     * @throws IOException
     */
    private String handleToName(int h)
        throws IOException
    {
        byte[] byteArray = memoryManager.read(h);
        char[] name = new char[(byteArray[0] << 8) + byteArray[1] - 18];
        for (int i = 0; i < name.length; i++)
        {
            name[i] = (char)byteArray[i + 18];
        }
        return new String(name);
    }


    /**
     * Writes a new element to memory file.
     *
     * @param element
     *            data to write
     * @throws IOException
     */
    private void writeNewElement(T element)
        throws IOException
    {
        if (element != null)
        {
            element.setHandle(memoryManager.write(element.serialize()));
        }
    }


    /**
     * Writes a new BinNode to memory file.
     *
     * @param node
     *            data to write
     * @throws IOException
     */
    private void writeNewBinNode(BinNode node)
        throws IOException
    {
        if (node != null)
        {
            node.setMyHandle(memoryManager.write(node.serialize()));
        }
    }


    /**
     * Writes a BinNode to memory file.
     *
     * @param node
     *            data to write
     * @throws IOException
     */
    private void writeBinNode(BinNode node)
        throws IOException
    {
        if (node != null)
        {
            memoryManager.write(node.serialize(), node.getMyHandle());
        }
    }

}
