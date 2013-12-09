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
            else if (command.contains("search"));
        }
        commandFile.close();
    }


    /**
     * Processes a watcher add request
     *
     * @param watcher
     *            (the Watcher to be added)
     * @throws IOException
     */
    private static void watcherAddRequest(String line)
        throws IOException
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
            new Watcher(
                0,
                name,
                new Double(longitude).doubleValue(),
                new Double(latitude).doubleValue());

        // not in the BinTree
        if (watcherBinTree.find(watcher.getCoordinate()) == -1)
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
        }
    }


    /**
     * Processes a watcher delete request.
     *
     * @param watcher
     *            (the Watcher to be removed)
     * @throws IOException
     */
    private static void watcherDeleteRequest(String command)
        throws IOException
    {
        // parse the coordinate from the request, then removes the watcher
        // from the BinTree, and print the appropriate message
        int space1 = command.indexOf(" ") + 1;
        int space2 = command.indexOf(" ", space1);
        double x = Double.parseDouble(command.substring(space1, space2));
        space1 = space2 + 1;
        space2 = command.indexOf(" ", space1);
        double y = Double.parseDouble(command.substring(space1, space2));

        // create watcher and set its name
        Watcher watcher = new Watcher(-1, "", x, y);
        watcher.setName(handleToName(watcherBinTree.find(watcher
            .getCoordinate())));

        // remove watcher form the BinTree
        watcherBinTree.remove(watcher.getCoordinate());
        watcherBinTree.organize();

        System.out.println(watcher + " is removed from the bintree");

    }


    /**
     * Processes a debug request.
     */
    private static void debugRequest()
    {
        System.out.println(watcherBinTree); // toString()
    }


    /**
     * Returns the name of a Watcher that is represented at the given handle.
     *
     * @param handle
     *            the handle of this watcher
     * @return a String representing the Watcher's name
     * @throws IOException
     */
    private static String handleToName(int handle)
        throws IOException
    {
        byte[] byteArray = memoryManager.read(handle);
        char[] name = new char[(byteArray[0] << 8) + byteArray[1] - 18];
        for (int i = 0; i < name.length; i++)
        {
            name[i] = (char)byteArray[i + 18];
        }
        return new String(name);
    }
}
