package nachos.vm;

import nachos.machine.Lib;
import nachos.machine.Machine;
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

                if (tlbEntry.ppn == tmpPPN) {
                    hasFound = false;
                    break;
                }
            }
            if (hasFound) {
                // write to swap space if dirty

                table.remove(entry.getKey());
                return tmpPPN; // found a free ppn
            }
        }

        return -1;
        // need to evict one
        // tlb entries must not be evicted
        // write into swap space if dirty

    }

    public TranslationEntry get (Pair key) {
        TranslationEntry entry = table.get(key);

        if (entry == null || !entry.valid) return null;
        return entry;
    }

    private Hashtable<Pair, TranslationEntry> table;
}

class Pair {
    public Pair(Integer vpn, Integer pid) {
        this.vpn = vpn;
        this.pid = pid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Pair)) return false;
        Pair pair = (Pair) o;
        return vpn.equals(pair.vpn) && pid.equals(pair.pid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(vpn, pid);
    }

    private Integer vpn;
    private Integer pid;
}
