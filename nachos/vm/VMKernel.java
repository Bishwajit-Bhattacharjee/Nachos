package nachos.vm;

import nachos.machine.*;
import nachos.threads.*;
import nachos.userprog.*;
import nachos.vm.*;

import java.util.Hashtable;
import java.util.Objects;

/**
 * A kernel that can support multiple demand-paging user processes.
 */
public class VMKernel extends UserKernel {
    /**
     * Allocate a new VM kernel.
     */
    public VMKernel() {
        super();
        System.out.println("Hello from after super!");
        invertedPageTable = new InvertedPageTable(Machine
                .processor().getNumPhysPages());

        System.out.println("Hello from after invertedPageTable!");
        //pageTableLock = new Lock();
    }

    /**
     * Initialize this kernel.
     */
    public void initialize(String[] args) {
        super.initialize(args);
    }

    /**
     * Test this kernel.
     */
    public void selfTest() {
        super.selfTest();
    }

    /**
     * Start running user programs.
     */
    public void run() {
        super.run();
    }

    /**
     * Terminate this kernel. Never returns.
     */
    public void terminate() {
        super.terminate();
    }

    public static Integer evictPhysicalPageNumber() {

        return 0;
    }

    // dummy variables to make javac smarter
    private static VMProcess dummy1 = null;

    // static variables
    static InvertedPageTable invertedPageTable;
    static Lock pageTableLock;
    private static final char dbgVM = 'v';
}


