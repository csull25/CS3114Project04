import java.io.IOException;

// -------------------------------------------------------------------------
/**
 * Main class of the project. The program will be invoked from the command-line
 * as java DiskBintree <command-file-name> <numb-buffers> <buffersize>. The
 * project represents a BinTree that is stored in a disk file. It takes commands
 * from the command file.
 *
 * @author Alex Kallam ( ?? )
 * @author Connor J. Sullivan (csull)
 * @version 2013.12.08
 */

// On my honor: I, Alex Kallam and Connor J Sullivan
//
// - I have not used source code obtained from another student,
// or any other unauthorized source, either modified or
// unmodified.
//
// - All source code and documentation used in my program is
// either my original work, or was derived by me from the
// source code published in the textbook for this course.
//
// - I have not discussed coding details about this project with
// anyone other than my partner (in the case of a joint
// submission), instructor, ACM/UPE tutors or the TAs assigned
// to this course. I understand that I may discuss the concepts
// of this program with other students, and that another student
// may help me debug my program so long as neither of us writes
// anything during the discussion or modifies any computer file
// during the discussion. I have violated neither the spirit nor
// letter of this restriction.

public class DiskBintree
{

    /**
     * The memory manager.
     */
    private static MemoryManager    memoryManager;
    /**
     * A BinTree of watchers.
     */
    private static BinTree<Watcher> watcherBinTree;


    /**
     * The method that is executed. This was run on Connor's Lenovo ThinkPad
     * X230 Tablet running Windows 7 and on Alex's Apple ????? running ????? on
     * ?????.
     *
     * @param args
     *            the command line arguments: <command-file-name> <numb-buffers>
     *            <buffersize>
     * @throws IOException
     */
    public static void main(String[] args) throws IOException
    {
        // check the number of arguments
        if (args.length != 3)
        {
            throw new IllegalArgumentException("Invalid number of arguments");
        }
        // instantiate fields
        memoryManager =
            new MemoryManager(new Integer(args[1]).intValue(), new Integer(
                args[2]).intValue());
        watcherBinTree = new BinTree<Watcher>(memoryManager);



    }

}
