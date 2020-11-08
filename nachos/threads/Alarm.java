package nachos.threads;

import nachos.machine.*;

import java.util.PriorityQueue;

/**
 * Uses the hardware timer to provide preemption, and to allow threads to sleep
 * until a certain time.
 */
public class Alarm {
    /**
     * Allocate a new Alarm. Set the machine's timer interrupt handler to this
     * alarm's callback.
     *
     * <p><b>Note</b>: Nachos will not function correctly with more than one
     * alarm.
     */
    public Alarm() {
        waitingKThreads = new PriorityQueue<>();
        Machine.timer().setInterruptHandler(new Runnable() {
            public void run() {
                timerInterrupt();
            }
        });
    }

    /**
     * The timer interrupt handler. This is called by the machine's timer
     * periodically (approximately every 500 clock ticks). Causes the current
     * thread to yield, forcing a context switch if there is another thread
     * that should be run.
     */
    public void timerInterrupt() {


        while (!waitingKThreads.isEmpty() && waitingKThreads.peek().time <= Machine.timer().getTime())
        {
            PendingKThread pendingKThread = waitingKThreads.poll();
            Lib.assertTrue(pendingKThread.time <= Machine.timer().getTime());
            pendingKThread.kThread.ready();
        }


        KThread.currentThread().yield();
    }

    /**
     * Put the current thread to sleep for at least <i>x</i> ticks,
     * waking it up in the timer interrupt handler. The thread must be
     * woken up (placed in the scheduler ready set) during the first timer
     * interrupt where
     *
     * <p><blockquote>
     * (current time) >= (WaitUntil called time)+(x)
     * </blockquote>
     *
     * @param    x    the minimum number of clock ticks to wait.
     * @see    nachos.machine.Timer#getTime()
     */
    public void waitUntil(long x) {

        boolean intStatus = Machine.interrupt().disable();

        waitingKThreads.add(new PendingKThread(KThread.currentThread(),
                Machine.timer().getTime() + x)
        );
        KThread.sleep();

        Machine.interrupt().restore(intStatus);

//        long wakeTime = Machine.timer().getTime() + x;
//        while (wakeTime > Machine.timer().getTime())
//            KThread.yield();
    }

    private class PendingKThread implements Comparable {
        PendingKThread(KThread kThread, long time) {
            this.kThread = kThread;
            this.time = time;
        }

        public int compareTo(Object o) {
            PendingKThread pendingKThread = (PendingKThread) o;

            if (this.time < pendingKThread.time)
                return -1;
            else if (this.time > pendingKThread.time)
                return 1;
            else
                return this.kThread.compareTo(pendingKThread.kThread);
        }

        private long time;
        private KThread kThread;
    }


    private static class PingTest implements Runnable {
        PingTest(int which,long time) {
            this.which = which;
            this.time = time;
        }

        public void run()
        {
            System.out.println(which + " rings at " + Machine.timer().getTime());
            ThreadedKernel.alarm.waitUntil(time);
            System.out.println(which + " rings at " + Machine.timer().getTime());

        }

        private int which;
        private long time;
    }

    public static void selfTest() {
        new KThread(new PingTest(1,1000)).setName("1 Alarm thread").fork();
        new KThread(new Alarm.PingTest(2,5000)).setName("2 Alarm thread").fork();
        new KThread(new Alarm.PingTest(3,100000)).setName("3 Alarm thread").fork();
    }




    private PriorityQueue<PendingKThread> waitingKThreads;

}