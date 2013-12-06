// -------------------------------------------------------------------------
/**
 * This class represents a BinTree. It is a binary tree with multi-dimensional
 * searching features.
 *
 * @author Connor J. Sullivan (csull)
 * @version 2013.12.05
 * @param <T>
 *            a generic object that implements the HasCoordinate interface
 */

public class BinTree<T extends HasCoordinate>
{
    private BinNode     root;
    private BinLeafNode emptyLeafNode;


    /**
     * Create a BinTree object.
     */
    public BinTree()
    {
        this.root = null;
        this.emptyLeafNode = new BinLeafNode(null);
    }


    /**
     * Create a BinTree object.
     *
     * @param root
     *            the root of the tree
     */
    // public BinTree(BinNode root)
    // {
    // this.root = root;
    // emptyLeafNode = new BinLeafNode(null);
    // }

    /**
     * 'De-serializes' the node. It converts the given byte array into a
     * binNode.
     *
     * @param byteArray
     *            the byteArray representation to be converted
     * @return the BinNode that was represented by the given byte array
     */
    public BinNode deserialize(byte[] byteArray)
    {
        if (byteArray.length == 5)
        {
            int handle = 0;
            for (int i = 0; i < 4; i++)
            {
                handle += byteArray[i + 1] << (24 - 8 * i);
            }
            return new BinLeafNode(new Handle(handle));
        }
        else if (byteArray.length == 9)
        {
            int handle1 = 0, handle2 = 0;
            for (int i = 0; i < 4; i++)
            {
                handle1 += byteArray[i + 1] << (24 - 8 * i);
                handle2 += byteArray[i + 5] << (24 - 8 * i);
            }
            return new BinInternalNode(new Handle(handle1), new Handle(handle2));
        }
        else
        {
            return null;
        }
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
     * Inserts the given element with the given key into the tree.
     *
     * @param element
     *            the new element to be added
     */
    public void insert(Handle element)
    {
        // make the new element the root if the root is null
        if (root == null)
        {
            root = new BinLeafNode(element);
        }
        else
        {
            // if the root is a leaf node, make it internal and sets the
            // children appropriately
            if (root.isLeafNode())
            {
                BinInternalNode newRoot = new BinInternalNode();

                if (((BinLeafNode)root).getElement().getLongitude() < 0.0)
                {
                    newRoot.setLeft(root);
                    newRoot.setRight(emptyLeafNode);
                }
                else
                {
                    newRoot.setRight(root);
                    newRoot.setLeft(emptyLeafNode);
                }
                root = newRoot;
            }
            // call the recursive method
            if (element.getCoordinate().getLongitude() < 0.0)
            {
                insert(
                    element,
                    ((BinInternalNode)root).getLeft(),
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
                    element,
                    ((BinInternalNode)root).getRight(),
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
     */
    public boolean remove(Coordinate coordinate)
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
            if (((BinLeafNode)root).getElement().getCoordinate().getLongitude() == coordinate
                .getLongitude()
                && ((BinLeafNode)root).getElement().getCoordinate()
                    .getLatitude() == coordinate.getLatitude())
            {
                root = null;
                return true;
            }
            return false;
        }
        // if the root is internal, then check its children for the specified
        // coordinate and remove it if one matches.
        else
        {
            BinNode leftChild = ((BinInternalNode)root).getLeft();
            BinNode rightChild = ((BinInternalNode)root).getRight();
            if (leftChild.isLeafNode()
                && leftChild != emptyLeafNode
                && ((BinLeafNode<?>)leftChild).getElement().getCoordinate()
                    .getLongitude() == coordinate.getLongitude()
                && ((BinLeafNode<?>)leftChild).getElement().getCoordinate()
                    .getLatitude() == coordinate.getLatitude())
            {
                ((BinInternalNode)root).setLeft(emptyLeafNode);
                return true;

            }
            if (rightChild.isLeafNode()
                && rightChild != emptyLeafNode
                && ((BinLeafNode)rightChild).getElement().getCoordinate()
                    .getLongitude() == coordinate.getLongitude()
                && ((BinLeafNode)rightChild).getElement().getCoordinate()
                    .getLatitude() == coordinate.getLatitude())
            {
                ((BinInternalNode)root).setRight(emptyLeafNode);
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
                            ((BinInternalNode)rootInternal.getLeft()).getLeft(),
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
                            ((BinInternalNode)rootInternal.getLeft())
                                .getRight(),
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
                            ((BinInternalNode)rootInternal.getRight())
                                .getLeft(),
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
                            ((BinInternalNode)rootInternal.getRight())
                                .getRight(),
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
     * @return the object associated with the coordinate or null if no object
     *         was found
     */
    public HasCoordinate find(Coordinate coordinate)
    {
        // call the recursive method
        return find(coordinate, root, 0, 0.0, 360.0, 0.0, 180.0);
    }


    /**
     * Searches the given region in the bin tree. This method returns the number
     * of nodes visited during the search.
     *
     * @param minLongitude
     *            the minimum longitude of the bounding box
     * @param maxLongitude
     *            the maximum longitude of the bounding box
     * @param minLatitude
     *            the minimum latitude of the bounding box
     * @param maxLatitude
     *            the maximum latitude of the bounding box
     * @return number of nodes visited during the search
     */
    public int regionSearch(
        double minLongitude,
        double maxLongitude,
        double minLatitude,
        double maxLatitude)
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
            minLongitude,
            maxLongitude,
            minLatitude,
            maxLatitude,
            0.0,
            360.0,
            0.0,
            180.0);
    }


    /**
     * Organizes the bintree.
     */
    public void organize()
    {
        // nothing needs to be organized if the root is null or a leaf
        if (root == null || root.isLeafNode())
        {
            return;
        }
        // if the root's children are both leaves with one of them being empty,
        // set the root to the non-empty child.
        BinNode leftChild = ((BinInternalNode)root).getLeft();
        BinNode rightChild = ((BinInternalNode)root).getRight();
        if (leftChild == emptyLeafNode && rightChild.isLeafNode())
        {
            root = rightChild;
            return;
        }
        else if (leftChild.isLeafNode() && rightChild == emptyLeafNode)
        {
            root = leftChild;
            return;
        }
        // While something was rearranged, continue to call the recursive
        // method.
        while (organize((BinInternalNode)root))
            ;
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
        String s = preOrderTraversal(this.root);
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
     * @param element
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
     */
    private void insert(
        Handle element,
        BinNode node,
        BinInternalNode parent,
        int depth,
        double minLongitude,
        double maxLongitude,
        double minLatitude,
        double maxLatitude,
        boolean nodeIsALeftChild)
    {
        if (node.isLeafNode())
        {
            // if the node is an empty leaf, make it a filled leaf with the
            // given element
            if (node == emptyLeafNode)
            {
                BinLeafNode newLeafNode = new BinLeafNode(element);
                if (nodeIsALeftChild)
                {
                    parent.setLeft(newLeafNode);
                }
                else
                {
                    parent.setRight(newLeafNode);
                }
            }
            // if the node being looked at is a leaf, then replace it with a
            // new internal node with children being the current node and an
            // empty leaf node. The specifics of the setting are determined by
            // the depth of the tree (for longitude or latitude splitting).
            else
            {
                BinInternalNode newInternalNode = new BinInternalNode();
                if (depth % 2 == 0)
                {
                    if (((BinLeafNode)node).getElement().getLongitude() + 180 < (minLongitude + maxLongitude) / 2.0)
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
                    if (((BinLeafNode)node).getElement().getLatitude() + 90 < (minLatitude + maxLatitude) / 2.0)
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
                if (nodeIsALeftChild)
                {
                    parent.setLeft(newInternalNode);
                }
                else
                {
                    parent.setRight(newInternalNode);
                }
                // call the recursive method
                this.insert(
                    element,
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
                if (element.getCoordinate().getLongitude() + 180.0 < (minLongitude + maxLongitude) / 2.0)
                {
                    insert(
                        element,
                        ((BinInternalNode)node).getLeft(),
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
                        element,
                        ((BinInternalNode)node).getRight(),
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
                if (element.getCoordinate().getLatitude() + 90.0 < (minLatitude + maxLatitude) / 2.0)
                {
                    insert(
                        element,
                        ((BinInternalNode)node).getLeft(),
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
                        element,
                        ((BinInternalNode)node).getRight(),
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
    {
        if (node.isLeafNode() && node != emptyLeafNode)
        {
            // If the node being looked at matches the given coordinates, then
            // remove that node by replacing it with an empty leaf node. The
            // specifics of the replacing is determined by the ancestry of the
            // node being replaced.
            if (((BinLeafNode)node).getElement().getCoordinate().getLongitude() == coordinate
                .getLongitude()
                && ((BinLeafNode)node).getElement().getCoordinate()
                    .getLatitude() == coordinate.getLatitude())
            {
                if (nodeHasALeftParent)
                {
                    if (nodeIsALeftGrandchild)
                    {
                        ((BinInternalNode)grandparent.getLeft())
                            .setLeft(emptyLeafNode);
                        return true;
                    }
                    else
                    {
                        ((BinInternalNode)grandparent.getLeft())
                            .setRight(emptyLeafNode);
                        return true;
                    }
                }
                else
                {
                    if (nodeIsALeftGrandchild)
                    {
                        ((BinInternalNode)grandparent.getRight())
                            .setLeft(emptyLeafNode);
                        return true;
                    }
                    else
                    {
                        ((BinInternalNode)grandparent.getRight())
                            .setRight(emptyLeafNode);
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
                            ((BinInternalNode)node).getLeft(),
                            (BinInternalNode)grandparent.getLeft(),
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
                            ((BinInternalNode)node).getLeft(),
                            (BinInternalNode)grandparent.getRight(),
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
                            ((BinInternalNode)node).getRight(),
                            (BinInternalNode)grandparent.getLeft(),
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
                            ((BinInternalNode)node).getRight(),
                            (BinInternalNode)grandparent.getRight(),
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
                            ((BinInternalNode)node).getLeft(),
                            (BinInternalNode)grandparent.getLeft(),
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
                            ((BinInternalNode)node).getLeft(),
                            (BinInternalNode)grandparent.getRight(),
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
                            ((BinInternalNode)node).getRight(),
                            (BinInternalNode)grandparent.getLeft(),
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
                            ((BinInternalNode)node).getRight(),
                            (BinInternalNode)grandparent.getRight(),
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
     * @return the object associated with the given key, or null if no key is
     *         found to match
     */
    private HasCoordinate find(
        Coordinate coordinate,
        BinNode node,
        int depth,
        double minLongitude,
        double maxLongitude,
        double minLatitude,
        double maxLatitude)
    {
        // If the node is null, then the coordinate wasn't found so return null.
        if (node == null)
        {
            return null;
        }
        // If the node is a leaf, then check the element's coordinates. If they
        // match, then return the element; otherwise, return null.
        if (node.isLeafNode())
        {
            HasCoordinate element = ((BinLeafNode<?>)node).getElement();
            if (element != null
                && element.getCoordinate().getLongitude() == coordinate
                    .getLongitude()
                && element.getCoordinate().getLatitude() == coordinate
                    .getLatitude())
            {
                return element;
            }
            else
            {
                return null;
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
                        ((BinInternalNode)node).getLeft(),
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
                        ((BinInternalNode)node).getRight(),
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
                        ((BinInternalNode)node).getLeft(),
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
                        ((BinInternalNode)node).getRight(),
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
    {
        // If the node is a filled leaf node, check that its element is within
        // the bounds of the biggest circle inscribed in the given bounding
        // box. If so, print that element.
        if (node.isLeafNode())
        {
            if (node != emptyLeafNode)
            {
                HasCoordinate element = ((BinLeafNode)node).getElement();

                if (Math.pow(element.getLatitude()
                    - (minLatitude + maxLatitude) / 2.0, 2)
                    + Math.pow(element.getLongitude()
                        - (minLongitude + maxLongitude) / 2.0, 2) < Math.pow(
                    (maxLongitude - minLongitude) / 2.0,
                    2))
                {
                    System.out.println(element);
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
                            ((BinInternalNode)node).getLeft(),
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
                            ((BinInternalNode)node).getRight(),
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
                        ((BinInternalNode)node).getLeft(),
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
                        ((BinInternalNode)node).getRight(),
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
                            ((BinInternalNode)node).getLeft(),
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
                            ((BinInternalNode)node).getRight(),
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
                        ((BinInternalNode)node).getLeft(),
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
                        ((BinInternalNode)node).getRight(),
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
     */
    private String preOrderTraversal(BinNode node)
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
                return "\n" + ((BinLeafNode)node).getElement() + "\n";
            }
        }
        // If the node is internal, print I and then call the recursive method
        // for its left subtree and right subtree.
        else
        {
            return "\nI" + preOrderTraversal(((BinInternalNode)node).getLeft())
                + preOrderTraversal(((BinInternalNode)node).getRight());
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
     */
    private boolean organize(BinInternalNode grandparent)
    {
        BinNode leftParent = grandparent.getLeft();
        BinNode rightParent = grandparent.getRight();
        BinNode leftChild, rightChild;
        if (!leftParent.isLeafNode())
        {
            leftChild = ((BinInternalNode)leftParent).getLeft();
            rightChild = ((BinInternalNode)leftParent).getRight();

            // If the leftParent has a filled leaf node and an empty leaf node,
            // replace the leftParent with its filled leaf node child.
            if (leftChild.isLeafNode() && rightChild.isLeafNode())
            {
                if (leftChild == emptyLeafNode && rightChild != emptyLeafNode)
                {
                    grandparent.setLeft(rightChild);
                    return true;
                }
                else if (leftChild != emptyLeafNode
                    && rightChild == emptyLeafNode)
                {
                    grandparent.setLeft(leftChild);
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
            leftChild = ((BinInternalNode)rightParent).getLeft();
            rightChild = ((BinInternalNode)rightParent).getRight();

            // If the rightParent has a filled leaf node and an empty leaf node,
            // replace the leftParent with its filled leaf node child.
            if (leftChild.isLeafNode() && rightChild.isLeafNode())
            {
                if (leftChild == emptyLeafNode && rightChild != emptyLeafNode)
                {
                    grandparent.setRight(rightChild);
                    return true;
                }
                else if (leftChild != emptyLeafNode
                    && rightChild == emptyLeafNode)
                {
                    grandparent.setRight(leftChild);
                    return true;
                }
            }
            else
            {
                // Call the recursive method. If the recursive call returned
// true,
                // then return true, but if the recursive call returned false,
                // continue through the method (without returning).
                if (organize((BinInternalNode)rightParent))
                {
                    return true;
                }
            }
        }
        return false;
    }
}
