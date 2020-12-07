package nachos.vm;

import nachos.machine.*;
import nachos.threads.*;
import nachos.userprog.*;
import nachos.vm.*;

import javax.crypto.Mac;
import java.util.Random;

/**
 * A <tt>UserProcess</tt> that supports demand-paging.
 */
public class VMProcess extends UserProcess {
    /**
     * Allocate a new process.
     */
    public VMProcess() {
        super();
    }

    /**
     * Save the state of this process in preparation for a context switch.
     * Called by <tt>UThread.saveState()</tt>.
     */
    public void saveState() {
        super.saveState();
    }

    /**
     * Restore the state of this process after a context switch. Called by
     * <tt>UThread.restoreState()</tt>.
     */
    public void restoreState() {
        super.restoreState();
    }

    /**
     * Initializes page tables for this process so that the executable can be
     * demand-paged.
     *
     * @return	<tt>true</tt> if successful.
     */
    protected boolean loadSections() {
        //return super.loadSections();
        return true;
    }

    /**
     * Release any resources allocated by <tt>loadSections()</tt>.
     */
    protected void unloadSections() {
        super.unloadSections();
    }

    protected void handleTLBMiss (int vaddr) {

        int vpn = Processor.pageFromAddress(vaddr);
        bringPage(vpn);
    }

    protected void replaceTLB (TranslationEntry entry) {

        for (int i = 0; i < Machine.processor().getTLBSize(); i++) {
            TranslationEntry tlbEntry = Machine.processor().readTLBEntry(i);
            Lib.assertTrue(tlbEntry != null);

            if (!tlbEntry.valid){
                Machine.processor().writeTLBEntry(i, entry);
                return;
            }
        }

        Machine.processor().writeTLBEntry(
                new Random().nextInt(Machine.processor().getTLBSize()), entry
            );

    }

    protected void loadPageFromDisk (int vpn, TranslationEntry entry) {

    }

    protected void bringPage (int vpn) {
        Pair key = new Pair(vpn, processID);

        VMKernel.pageTableLock.acquire();

        TranslationEntry alreadyFoundEntry = VMKernel.
                invertedPageTable.get(key);

        if (alreadyFoundEntry != null){ // Page Table Hit
            replaceTLB(alreadyFoundEntry);
        }
        else {     // Page Table miss

            Integer evictedPPN = VMKernel.invertedPageTable.
                    evictPhysicalPageNumber();

            // load vpn into this entry
            // insert into inverted page table
            // replaceTLB(entry)
        }

        VMKernel.pageTableLock.release();
    }
    /**
     * Handle a user exception. Called by
     * <tt>UserKernel.exceptionHandler()</tt>. The
     * <i>cause</i> argument identifies which exception occurred; see the
     * <tt>Processor.exceptionZZZ</tt> constants.
     *
     * @param	cause	the user exception that occurred.
     */
    public void handleException(int cause) {
        Processor processor = Machine.processor();

        switch (cause) {

            case Processor.exceptionTLBMiss:
                handleTLBMiss(processor.readRegister(Processor.regBadVAddr));
                break;
            default:
                super.handleException(cause);
                break;
        }
    }

    private static final int pageSize = Processor.pageSize;
    private static final char dbgProcess = 'a';
    private static final char dbgVM = 'v';
}
