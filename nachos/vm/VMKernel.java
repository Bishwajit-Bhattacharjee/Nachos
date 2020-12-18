package nachos.vm;

import nachos.machine.*;
import nachos.threads.*;
import nachos.userprog.*;
import nachos.vm.*;

import java.util.Arrays;
import java.util.Hashtable;
import java.util.LinkedList;
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

        //pageTableLock = new Lock();
    }

    /**
     * Initialize this kernel.
     */
    public void initialize(String[] args) {
        super.initialize(args);
        invertedPageTable = new InvertedPageTable(Machine
                .processor().getNumPhysPages());
        swapFile = Machine.stubFileSystem().open("swapFile", true);
        freePagesOFSwapFile = new LinkedList<>();
        swapTracer = new Hashtable<>();

        pageFault = 0;
        readWriteTLBMiss = 0;
        ppnMisses = new int[Machine.processor().getNumPhysPages()];
        Arrays.fill(ppnMisses, 0);

        kernelLock = new Lock();
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
        swapFile.close();
        Machine.stubFileSystem().remove("swapFile");

        System.out.println("\n\n........................Statistics...................................\n");
        System.out.println("Read Write TLB Misses : " + readWriteTLBMiss);
        System.out.println("Page Faults : " + pageFault);
        for (int i = 0; i < ppnMisses.length; i++) {
            System.out.println("Physical page no : " + i + " evicted count : " + ppnMisses[i]);
        }

        super.terminate();
    }

    public static Integer evictPhysicalPageNumber() {

        return 0;
    }

    // dummy variables to make javac smarter
    private static VMProcess dummy1 = null;

    // static variables
    static OpenFile swapFile;
    public static InvertedPageTable invertedPageTable;
    static Hashtable<Pair, Integer> swapTracer;
    static Lock pageTableLock;
    static LinkedList<Integer> freePagesOFSwapFile;
    private static final char dbgVM = 'v';

    static int pageFault;
    static int readWriteTLBMiss;
    static int[] ppnMisses;

    static Lock kernelLock;
}


