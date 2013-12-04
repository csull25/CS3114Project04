import junit.framework.TestCase;

// -------------------------------------------------------------------------
/**
 * Tests the BinTree class.
 *
 * @author Connor J. Sullivan (csull)
 * @author Shane Todd (rstodd13)
 * @version 2013.10.14
 */

public class BinTreeTest
    extends TestCase
{

    private BinTree<Watcher> tree;


    // ----------------------------------------------------------
    @Override
    protected void setUp()
        throws Exception
    {
        super.setUp();
        tree = new BinTree<Watcher>();
    }


    /**
     * Test method for {@link BinTree#getRoot()}.
     */
    public void testGetRoot()
    {
        assertNull(tree.getRoot());
    }


    /**
     * Test method for {@link BinTree#insert(HasCoordinate)}.
     */
    public void testInsert()
    {
        Watcher watcher1 = new Watcher("watcher1", -135, 45);
        Watcher watcher2 = new Watcher("watcher2", -45, 45);
        Watcher watcher3 = new Watcher("watcher3", 45, 0);
        Watcher watcher4 = new Watcher("watcher4", 135, -45);

        tree.insert(watcher1);

        assertEquals(watcher1, ((BinLeafNode<?>)tree.getRoot()).getElement());

        tree.insert(watcher2);

        assertFalse(tree.getRoot().isLeafNode());

        tree.insert(watcher3);
        tree.insert(watcher4);

        assertEquals(
            watcher1,
            ((BinLeafNode<?>)((BinInternalNode)((BinInternalNode)((BinInternalNode)tree
                .getRoot()).getLeft()).getRight()).getLeft()).getElement());
        assertEquals(
            watcher2,
            ((BinLeafNode<?>)((BinInternalNode)((BinInternalNode)((BinInternalNode)tree
                .getRoot()).getLeft()).getRight()).getRight()).getElement());
        assertEquals(
            watcher3,
            ((BinLeafNode<?>)((BinInternalNode)((BinInternalNode)tree.getRoot())
                .getRight()).getRight()).getElement());
        assertEquals(
            watcher4,
            ((BinLeafNode<?>)((BinInternalNode)((BinInternalNode)tree.getRoot())
                .getRight()).getLeft()).getElement());
    }


    /**
     * Test method for
     * {@link BinTree#remove(realtimeweb.earthquakeservice.domain.Coordinate)}.
     */
    public void testRemove()
    {
        Watcher watcher1 = new Watcher("watcher1", -135, 45);
        Watcher watcher2 = new Watcher("watcher2", -45, 45);
        Watcher watcher3 = new Watcher("watcher3", 45, 0);
        Watcher watcher4 = new Watcher("watcher4", 135, -45);

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
     * Test method for
     * {@link BinTree#find(realtimeweb.earthquakeservice.domain.Coordinate)}.
     */
    public void testFind()
    {
        Watcher watcher1 = new Watcher("watcher1", -90, -45);

        assertNull(tree.find(watcher1.getCoordinate()));

        tree.insert(watcher1);
        assertEquals(watcher1, tree.find(watcher1.getCoordinate()));

        Watcher watcher2 = new Watcher("watcher2", -90, -50);
        tree.insert(watcher2);
        Watcher watcher3 = new Watcher("watcher3", 90, 45);
        tree.insert(watcher3);

        assertEquals(watcher2, tree.find(watcher2.getCoordinate()));
        assertEquals(watcher3, tree.find(watcher3.getCoordinate()));

        tree.clear();

        Watcher watcherA = new Watcher("watcherA", -135, 45);
        Watcher watcherB = new Watcher("watcherB", -45, 45);
        Watcher watcherC = new Watcher("watcherC", 45, 0);
        Watcher watcherD = new Watcher("watcherD", 135, -45);

        tree.insert(watcherA);
        tree.insert(watcherB);
        tree.insert(watcherC);
        tree.insert(watcherD);

        assertEquals(watcherA, tree.find(watcherA.getCoordinate()));
        assertEquals(watcherB, tree.find(watcherB.getCoordinate()));
        assertEquals(watcherC, tree.find(watcherC.getCoordinate()));
        assertEquals(watcherD, tree.find(watcherD.getCoordinate()));
    }


    /**
     * Test method for
     * {@link BinTree#regionSearch(double, double, double, double)}.
     */
    public void testRegionSearch()
    {
        Watcher watcher1 = new Watcher("watcher1", -135, 45);
        Watcher watcher2 = new Watcher("watcher2", -45, 45);
        Watcher watcher3 = new Watcher("watcher3", 45, 0);
        Watcher watcher4 = new Watcher("watcher4", 135, -45);

        tree.insert(watcher1);
        tree.insert(watcher2);
        tree.insert(watcher3);
        tree.insert(watcher4);

        assertEquals(9, tree.regionSearch(-180, 180, -90, 90));

        System.out.println("\n");

        assertEquals(6, tree.regionSearch(-180, -1, -90, 90));

        System.out.println("\n");

        assertEquals(7, tree.regionSearch(-180, 180, 0, 90));

        System.out.println("\n");

        assertEquals(4, tree.regionSearch(-140, -130, 40, 50));
    }


    /**
     * Test method for {@link BinTree#toString()}.
     */
    public void testToString()
    {
        assertEquals("E", tree.toString());

        Watcher watcherA = new Watcher("watcherA", -135, 45);
        Watcher watcherB = new Watcher("watcherB", -45, 45);

        tree.insert(watcherA);
        tree.insert(watcherB);

        assertEquals(
            "I\nI\nE\nI\nwatcherA -135.0 45.0\nwatcherB -45.0 45.0\nE",
            tree.toString());
    }


    /**
     * Tests the organize method.
     */
    public void testOrganize()
    {
        Watcher riley = new Watcher("Riley", -105.7, -24.3);
        Watcher taylor = new Watcher("Taylor", 21.2, -38.6);
        Watcher nevaeh = new Watcher("Nevaeh", -11.0, 63.1);
        Watcher dominic = new Watcher("Dominic", -79.2, 37.3);
        Watcher tristan = new Watcher("Tristan", -117.1, 5.0);
        // del taylor
        Watcher alexa = new Watcher("Alexa", -50.2, 88.4);
        Watcher john = new Watcher("John", 10.7, -80.4);
        Watcher penny = new Watcher("Penny", 55.3, -80.3);
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
        tree.remove(john.getCoordinate());

        tree.organize();

        assertEquals(
            "I\nI\nRiley -105.7 -24.3\nI\nTristan -117.1 5.0\nI\nDominic -79.2 37.3\nI\nAlexa -50.2 88.4\nNevaeh -11.0 63.1\nPenny 55.3 -80.3",
            tree.toString());

        tree.remove(nevaeh.getCoordinate());
        tree.remove(riley.getCoordinate());
        tree.remove(dominic.getCoordinate());

        tree.organize();

        tree.remove(tristan.getCoordinate());

        tree.organize();

        System.out.println("-------------------------\n\n" + tree
            + "\n\n---------------------------\n");

        tree.remove(alexa.getCoordinate());

        System.out.println("-------------------------\n\n" + tree
            + "\n\n---------------------------\n");

    }

}
