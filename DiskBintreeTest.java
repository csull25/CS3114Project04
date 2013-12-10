import java.io.IOException;
import junit.framework.TestCase;


// -------------------------------------------------------------------------
/**
 *  Write a one-sentence summary of your class here.
 *  Follow it with additional details about its purpose, what abstraction
 *  it represents, and how to use it.
 *
 *  @author Alex Kallam
 *  @author Connor J. Sullivan
 *  @version Dec 9, 2013
 */
public class DiskBintreeTest extends TestCase
{
    // ----------------------------------------------------------
    /**
     * Run program with given command line arguments
     * @param args command line arguments for program
     * @throws IOException
     */
    private void runTest(String[] args) throws IOException {
        DiskBintree.main(args);
    }

    // ----------------------------------------------------------
    /**
     * Test with sample input
     * @throws IOException
     */
    public void test1() throws IOException {
        String[] args = new String[3];
        args[0] = "watcherP4.txt";  // watcher file name
        args[1] = "10";  // number of buffers
        args[2] = "10";  // size of buffer in bytes
        runTest(args);
        DiskBintree code_coverage = new DiskBintree();
        assertNotNull(code_coverage);
        String[] codeCoverage = {"arg"};
        Exception ex = null;
        try {
            runTest(codeCoverage);
        }
        catch (Exception e) {
            ex = e;
            assertTrue(e instanceof IllegalArgumentException);
        }
        assertNotNull(ex);
    }
}
