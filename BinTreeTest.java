import java.io.IOException;
import junit.framework.TestCase;

// -------------------------------------------------------------------------
/**
 * Tests the BinTree class.
 *
 * @author Connor J. Sullivan (csull)
 * @version 2013.10.14
 * @version 2013.12.04 (minor update)
 */

public class BinTreeTest
    extends TestCase
{

    private BinTree<Watcher> tree;
    private MemoryManager    memoryManager;


    // ----------------------------------------------------------
    @Override
    protected void setUp()
        throws Exception
    {
        super.setUp();
        this.memoryManager = new MemoryManager(1, 4096);
        this.tree = new BinTree<Watcher>(memoryManager);
    }


    /**
     * Test method for {@link BinTree#getRoot()}.
     */
    public void testGetRoot()
    {
        assertNull(tree.getRoot());
    }


    /**
     * Test method for {@link BinTree#insert(HasCoordinateAndHandle)}.
     *
     * @throws IOException
     */
    public void testInsert()
        throws IOException
    {
        Watcher watcher1 = new Watcher(-1, "watcher1", -135, 45);
        Watcher watcher2 = new Watcher(-1, "watcher2", -45, 45);
        Watcher watcher3 = new Watcher(-1, "watcher3", 45, 0);
        Watcher watcher4 = new Watcher(-1, "watcher4", 135, -45);

        tree.insert(watcher1);

        assertEquals(
            watcher1.getHandle(),
            ((BinLeafNode)tree.getRoot()).getElement());

        // System.out.println(tree);

        tree.insert(watcher2);

        assertFalse(tree.getRoot().isLeafNode());

        tree.insert(watcher3);
        tree.insert(watcher4);

        assertEquals(
            watcher1.getHandle(),
            ((BinLeafNode)tree.handleToBinNode(((BinInternalNode)tree
                .handleToBinNode(((BinInternalNode)tree
                    .handleToBinNode(((BinInternalNode)tree.getRoot())
                        .getLeft())).getRight())).getLeft())).getElement());
        assertEquals(
            watcher2.getHandle(),
            ((BinLeafNode)tree.handleToBinNode(((BinInternalNode)tree
                .handleToBinNode(((BinInternalNode)tree
                    .handleToBinNode(((BinInternalNode)tree.getRoot())
                        .getLeft())).getRight())).getRight())).getElement());
        assertEquals(
            watcher3.getHandle(),
            ((BinLeafNode)tree.handleToBinNode(((BinInternalNode)tree
                .handleToBinNode(((BinInternalNode)tree.getRoot()).getRight()))
                .getRight())).getElement());
        assertEquals(
            watcher4.getHandle(),
            ((BinLeafNode)tree.handleToBinNode(((BinInternalNode)tree
                .handleToBinNode(((BinInternalNode)tree.getRoot()).getRight()))
                .getLeft())).getElement());
    }


    /**
     * Test method for {@link BinTree#remove(Coordinate)}.
     *
     * @throws IOException
     */
    public void testRemove()
        throws IOException
    {
        Watcher watcher1 = new Watcher(-1, "watcher1", -135, 45);
        Watcher watcher2 = new Watcher(-1, "watcher2", -45, 45);
        Watcher watcher3 = new Watcher(-1, "watcher3", 45, 0);
        Watcher watcher4 = new Watcher(-1, "watcher4", 135, -45);

        tree.insert(watcher1);
        tree.insert(watcher2);
        tree.insert(watcher3);
        tree.insert(watcher4);

        // System.out.println("\n\nremoving 1...\n\n");
        assertTrue(tree.remove(watcher1.getCoordinate()));
        // System.out.println(tree);

        // System.out.println("\n\nremoving 2...\n\n");
        assertTrue(tree.remove(watcher2.getCoordinate()));
        // System.out.println(tree);

        // System.out.println("\n\nremoving 3...\n\n");
        assertTrue(tree.remove(watcher3.getCoordinate()));
        // System.out.println(tree);

        // System.out.println("\n\nremoving 4...\n\n");
        assertTrue(tree.remove(watcher4.getCoordinate()));
        // System.out.println(tree);

        // System.out.println("SUCCESS");
    }


    /**
     * Test method for {@link BinTree#find(Coordinate)}.
     *
     * @throws IOException
     */
    public void testFind()
        throws IOException
    {
        Watcher watcher1 = new Watcher(-1, "watcher1", -90, -45);

        assertEquals(-1, tree.find(watcher1.getCoordinate()));

        tree.insert(watcher1);
        assertEquals(watcher1.getHandle(), tree.find(watcher1.getCoordinate()));

        Watcher watcher2 = new Watcher(-1, "watcher2", -90, -50);
        tree.insert(watcher2);
        Watcher watcher3 = new Watcher(-1, "watcher3", 90, 45);
        tree.insert(watcher3);

        assertEquals(watcher2.getHandle(), tree.find(watcher2.getCoordinate()));
        assertEquals(watcher3.getHandle(), tree.find(watcher3.getCoordinate()));

        tree.clear();

        Watcher watcherA = new Watcher(-1, "watcherA", -135, 45);
        Watcher watcherB = new Watcher(-1, "watcherB", -45, 45);
        Watcher watcherC = new Watcher(-1, "watcherC", 45, 0);
        Watcher watcherD = new Watcher(-1, "watcherD", 135, -45);

        tree.insert(watcherA);
        tree.insert(watcherB);
        tree.insert(watcherC);
        tree.insert(watcherD);

        assertEquals(watcherA.getHandle(), tree.find(watcherA.getCoordinate()));
        assertEquals(watcherB.getHandle(), tree.find(watcherB.getCoordinate()));
        assertEquals(watcherC.getHandle(), tree.find(watcherC.getCoordinate()));
        assertEquals(watcherD.getHandle(), tree.find(watcherD.getCoordinate()));
    }


    /**
     * Test method for
     * {@link BinTree#regionSearch(double, double, double, double)}.
     *
     * @throws IOException
     */
    public void testRegionSearch()
        throws IOException
    {
        Watcher watcher1 = new Watcher(-1, "watcher1", -135, 45);
        Watcher watcher2 = new Watcher(-1, "watcher2", -45, 45);
        Watcher watcher3 = new Watcher(-1, "watcher3", 45, 0);
        Watcher watcher4 = new Watcher(-1, "watcher4", 135, -45);

        tree.insert(watcher1);
        tree.insert(watcher2);
        tree.insert(watcher3);
        tree.insert(watcher4);

        System.out.println("\nBegin regionSearch test\n");
        assertEquals(9, tree.regionSearch(-180, 180, -90, 90));
        System.out.println("\n");
        assertEquals(6, tree.regionSearch(-180, -1, -90, 90));
        System.out.println("\n");
        assertEquals(7, tree.regionSearch(-180, 180, 0, 90));
        System.out.println("\n");
        assertEquals(4, tree.regionSearch(-140, -130, 40, 50));
        System.out.println("\nEnd regionSearch test\n"
            + "--------------------------------------------\n");

    }


    /**
     * Test method for {@link BinTree#toString()}.
     *
     * @throws IOException
     */
    public void testToString()
        throws IOException
    {
        assertEquals("E", tree.toString());

        Watcher watcherA = new Watcher(-1, "watcherA", -135, 45);
        Watcher watcherB = new Watcher(-1, "watcherB", -45, 45);

        tree.insert(watcherA);
        tree.insert(watcherB);

        assertEquals(
            "I\nI\nE\nI\nwatcherA -135.0 45.0\nwatcherB -45.0 45.0\nE",
            tree.toString());
    }


    /**
     * Tests the organize method.
     *
     * @throws IOException
     */
    public void testOrganize()
        throws IOException
    {
        Watcher riley = new Watcher(-1, "Riley", -105.7, -24.3);
        Watcher taylor = new Watcher(-1, "Taylor", 21.2, -38.6);
        Watcher nevaeh = new Watcher(-1, "Nevaeh", -11.0, 63.1);
        Watcher dominic = new Watcher(-1, "Dominic", -79.2, 37.3);
        Watcher tristan = new Watcher(-1, "Tristan", -117.1, 5.0);
        // del taylor
        Watcher alexa = new Watcher(-1, "Alexa", -50.2, 88.4);
        Watcher john = new Watcher(-1, "John", 10.7, -80.4);
        Watcher penny = new Watcher(-1, "Penny", 55.3, -80.3);
        // del john

        tree.insert(riley);
        tree.insert(taylor);
        tree.insert(nevaeh);
        tree.insert(dominic);
        tree.insert(tristan);
        tree.remove(taylor.getCoordinate());
        tree.insert(alexa);
        tree.insert(john);
        tree.insert(penny);
        assertTrue(tree.remove(john.getCoordinate()));

        tree.organize();
        assertEquals("I\nI\nRiley -105.7 -24.3\nI\nTristan -117.1 5.0\nI\n"
            + "Dominic -79.2 37.3\nI\nAlexa -50.2 88.4\n"
            + "Nevaeh -11.0 63.1\nPenny 55.3 -80.3", tree.toString());

        tree.remove(nevaeh.getCoordinate());
        tree.remove(riley.getCoordinate());
        tree.remove(dominic.getCoordinate());
        tree.organize();
        tree.remove(tristan.getCoordinate());
        tree.organize();

        tree.remove(alexa.getCoordinate());
        tree.organize();
        assertEquals("Penny 55.3 -80.3", tree.toString());
    }


    /**
     * Test method for {@link BinTree#handleToBinNode(int)}.
     *
     * @throws IOException
     */
    public void testHandleToBinNode()
        throws IOException
    {
        BinInternalNode node = new BinInternalNode(-1, 47, 32);
        node.setMyHandle(memoryManager.write(node.serialize()));
        BinNode actual = tree.handleToBinNode(node.getMyHandle());
        assertTrue(actual instanceof BinInternalNode);
        BinInternalNode internalActual = (BinInternalNode)actual;
        assertEquals(node.getLeft(), internalActual.getLeft());
        assertEquals(node.getRight(), internalActual.getRight());
        assertEquals(node.getMyHandle(), internalActual.getMyHandle());
    }


    /**
     * Test method for {@link BinTree#handleToCoordinate(int)}.
     *
     * @throws IOException
     */
    public void testHandleToCoordinate()
        throws IOException
    {
        Watcher w = new Watcher(-1, "name", -45.6, 60.3);
        w.setHandle(memoryManager.write(w.serialize()));
        Coordinate actual = tree.handleToCoordinate(w.getHandle());
        assertEquals(w.getCoordinate(), actual);
    }

}
