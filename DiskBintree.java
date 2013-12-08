import java.io.RandomAccessFile;
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
    public static void main(String[] args)
        throws IOException
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
        // open command file
        RandomAccessFile commandFile = new RandomAccessFile(args[0], "r");
        String command;
        while ((command = commandFile.readLine()) != null)
        {
            if (command.contains("add"))
            {
                watcherAddRequest(command);
            }
            else if (command.contains("delete"))
            {
                watcherDeleteRequest(command);
            }
            else if (command.contains("debug"))
            {
                debugRequest();
            }
        }

    }


    /**
     * Processes a watcher add request
     *
     * @param watcher
     *            (the Watcher to be added)
     */
    private static void watcherAddRequest(String line)
    {
        // parse the add request for longitude, latitude, and name. Then create
        // that watcher, add the watcher to the linked list, and print the
        // appropriate message
        int index1 = line.indexOf("add") + 4;
        int index2 = line.indexOf(' ', index1);
        String longitude = line.substring(index1, index2);
        index1 = index2 + 1;
        index2 = line.indexOf(' ', index1);
        String latitude = line.substring(index1, index2);
        String name = line.substring(index2 + 1);
        Watcher watcher =
            new Watcher(name, new Double(longitude).doubleValue(), new Double(
                latitude).doubleValue());

        // not in BST
        if (watcherBinarySearchTree.find(watcher) == null)
        {
            watcherBinarySearchTree.insert(watcher);
            System.out.println(watcher + " is added to the BST");

            // not in the BinTree
            if (watcherBinTree.find(watcher.getCoordinate()) == null)
            {
                // add to BinTree
                watcherBinTree.insert(watcher);
                System.out.println(watcher + " is added to the bintree");
            }

            // coordinates already in BinTree
            else
            {
                System.out.println(longitude + " " + latitude
                    + " duplicates a watcher already in the bintree");
                // remove watcher from BST
                watcherBinarySearchTree.remove(watcher);
                System.out.println(name + " is removed from the BST");
            }
        }

        // already in BST
        else
        {
            System.out.println(name
                + " duplicates a watcher already in the BST");
        }
    }


    /**
     * Processes a watcher delete request.
     *
     * @param watcher
     *            (the Watcher to be removed)
     */
    private static void watcherDeleteRequest(String line)
    {
        // parse the watcher's name from the request, then remove the watcher
        // from the linked list, and print the appropriate message
        String name = line.substring(line.indexOf("delete") + 7);

        Watcher watcher = new Watcher(name, 0, 0);
        watcher = watcherBinarySearchTree.find(watcher);
        if (watcher != null) // in BST
        {
            // remove watcher from BST and update that watcher
            watcherBinarySearchTree.remove(watcher);
            System.out.println(watcher + " is removed from the BST");

            // remove watcher form the BinTree
            watcherBinTree.remove(watcher.getCoordinate());
            watcherBinTree.organize();

            System.out.println(watcher + " is removed from the bintree");
        }

        // not in BST
        else
        {
            System.out.println(name + " does not appear in the BST");
        }
    }


    /**
     * Processes a debug request.
     */
    private static void debugRequest()
    {
        if (!watcherBinarySearchTree.isEmpty())
        {
            System.out.println(watcherBinarySearchTree); // toString()
        }
        System.out.println(watcherBinTree); // toString()
    }

}
