package nachos.proj1;

import nachos.machine.Kernel;
import nachos.machine.Machine;
import nachos.threads.KThread;
import nachos.threads.ThreadedKernel;

public class Test {
    public static void test() {
        new JoinTest().performTest();
    }

}

class JoinTest {
    public void performTest() {

        KThread D = new KThread( new JoinTestClass(null, 100000)).setName("D");
        KThread C = new KThread( new JoinTestClass(D, 0)).setName("C");
        KThread B = new KThread( new JoinTestClass(C, 0)).setName("B");
        KThread A = new KThread( new JoinTestClass(B, 0)).setName("A");

        A.fork();
        B.fork();
        C.fork();
        D.fork();

        A.join();
    }

    private static class JoinTestClass implements Runnable {

        JoinTestClass(KThread sleepOn, long sleepTime) {
            this.sleepOn = sleepOn;
            this.sleepTime = sleepTime;
        }
        @Override
        public void run() {
            System.out.println("Thread " + KThread.currentThread().getName()
                    + " want to sleep on " + ((sleepOn == null)?"None": sleepOn.getName()));
            System.out.println("Thread " + KThread.currentThread().getName() + " has started");

            if (sleepOn != null) {
                sleepOn.join();
            }

            ThreadedKernel.alarm.waitUntil(sleepTime);

            System.out.println("Thread " + KThread.currentThread().getName()
                    + " has ended at time " + Machine.timer().getTime());

        }

        private long sleepTime;
        private KThread sleepOn;

    }
}
