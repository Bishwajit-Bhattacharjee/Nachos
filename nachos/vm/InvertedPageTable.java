package nachos.vm;

import nachos.machine.Lib;
import nachos.machine.Machine;
import nachos.machine.Processor;
import nachos.machine.TranslationEntry;

import java.util.Hashtable;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

public class InvertedPageTable {

    InvertedPageTable(int numPhyPages) {
        table = new Hashtable<>();

        for (int i = 0; i < numPhyPages; i++) {
            Pair key = new Pair(i, i);
            TranslationEntry entry = new TranslationEntry(
                    i, i, false, false, false, false
            );
            table.put(key, entry);
        }
    }
    public void put(Pair key, TranslationEntry value){
        table.put(key, value);
    }

    public int evictPhysicalPageNumber (){

        for (Map.Entry<Pair, TranslationEntry> entry : table.entrySet()) {
            if (!entry.getValue().valid) {
                int ppn = entry.getValue().ppn;
                table.remove(entry.getKey());
                return ppn; // found a free ppn
            }
        }

        for (Map.Entry<Pair, TranslationEntry> entry : table.entrySet()) {

            int tmpPPN = entry.getValue().ppn;
            boolean hasFound = true;

            for (int i = 0; i < Machine.processor().getTLBSize(); i++) {
                TranslationEntry tlbEntry = Machine.processor().readTLBEntry(i);
                Lib.assertTrue(tlbEntry != null);

                if (tlbEntry.valid && tlbEntry.ppn == tmpPPN) {
                    hasFound = false;
                    break;
                }
            }
            if (hasFound) {
                // write to swap space if dirty

                TranslationEntry replacingEntry = entry.getValue();

                if (replacingEntry.dirty) {
                    writeBackToSwap(replacingEntry);
                }

                table.remove(entry.getKey());
                return tmpPPN; // found a free ppn
            }
        }
        Lib.assertNotReached();
        return -1;

    }

    public void writeBackToSwap (TranslationEntry entry) {
        int curProcessID = VMKernel.currentProcess().getProcessID();

        Integer swapFilePos = VMKernel.swapTracer.get(
                new Pair(entry.vpn, curProcessID)
        );

        if (swapFilePos == null) {
            // check whether swapFile has any hole
            if (!VMKernel.freePagesOFSwapFile.isEmpty()) {
                swapFilePos = VMKernel.freePagesOFSwapFile.poll();
            }
            else { // need a new page from swapFile
                swapFilePos = VMKernel.swapFile.length();
            }

            VMKernel.swapTracer.put(
                    new Pair(entry.vpn, curProcessID),
                    swapFilePos
            );
            Lib.assertTrue( swapFilePos % Processor.pageSize == 0);
        }

        // finally write it in the swap
        int writtenAmount = VMKernel.swapFile.write(swapFilePos,
                Machine.processor().getMemory(),
                entry.ppn * Processor.pageSize, Processor.pageSize);

        Lib.assertTrue(writtenAmount == Processor.pageSize);
    }

    public TranslationEntry get (Pair key) {
       TranslationEntry entry = table.get(key);

        if (entry == null || !entry.valid) return null;
        return entry;
    }

    @Override
    public String toString() {
        return "InvertedPageTable{" +
                "table=" + table +
                '}';
    }

    private Hashtable<Pair, TranslationEntry> table;
}

