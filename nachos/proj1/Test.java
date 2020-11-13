package nachos.proj1;

import nachos.machine.Kernel;
import nachos.machine.Machine;
import nachos.threads.Communicator;
import nachos.threads.KThread;
import nachos.threads.ThreadedKernel;

public class Test {
    public static void test() {
        new WaitUntilTest().performTest();
        new JoinTest().performTest();
        new CommunicatorTest().performTest();
    }
}

class JoinTest {
    public void performTest() {

        System.out.println("\n***************************************** Join Test *****************************************\n");


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

class CommunicatorTest {
    public CommunicatorTest() {
        this.communicator = new Communicator();
    }

    public void performTest() {
        System.out.println("\n*************** Condition2 and Communicator Test *******************\n");

        KThread s1 = new KThread(new Speaker(communicator)).setName("Speaker thread 1");
        KThread s2 = new KThread(new Speaker(communicator)).setName("Speaker thread 2");
        KThread s3 = new KThread(new Speaker(communicator)).setName("Speaker thread 3");

        KThread l1 = new KThread(new Listener(communicator)).setName("Listener thread 1");
        KThread l2 = new KThread(new Listener(communicator)).setName("Listener thread 2");
        KThread l3 = new KThread(new Listener(communicator)).setName("Listener thread 3");

        s1.fork();
        s2.fork();
        s3.fork();

        l1.fork();
        l2.fork();

        ThreadedKernel.alarm.waitUntil(100000);

        l3.fork();

        s1.join();
        s2.join();
        s3.join();

        l1.join();
        l2.join();
        l3.join();

        System.out.println("\n\n");
    }

    private static class Speaker implements Runnable{

        Speaker(Communicator communicator) {
            this.communicator = communicator;
        }

        @Override
        public void run() {
            for(int i = 0; i < 2; i++) {
                KThread.yield();
                this.communicator.speak(i);
                KThread.yield();
            }
        }

        private Communicator communicator;
    }

    private static class Listener implements Runnable{

        Listener(Communicator communicator) {
            this.communicator = communicator;
        }

        @Override
        public void run() {
            for(int i = 0; i < 2; i++) {
                KThread.yield();
                this.communicator.listen();
                KThread.yield();
            }
        }

        private Communicator communicator;
    }

    private Communicator communicator;
}

class WaitUntilTest {

    public void performTest() {

        System.out.println("\n*************** Alarm Test *******************\n");

        KThread t1 = new KThread(new PingTest(1,0)).setName("1st Alarm thread");
        KThread t2 = new KThread(new PingTest(2,5000)).setName("2nd Alarm thread");
        KThread t3 = new KThread(new PingTest(3,1000000)).setName("3rd Alarm thread");

        t1.fork();
        t2.fork();
        t3.fork();

        t1.join();
        t2.join();
        t3.join();

    }

    private static class PingTest implements Runnable {
        PingTest(int which,long time) {
            this.which = which;
            this.time = time;
        }
        public void run()
        {
            System.out.println(which + " starts alarm at " + Machine.timer().getTime());
            ThreadedKernel.alarm.waitUntil(time);
            System.out.println(which + " ends alarm at " + Machine.timer().getTime());
        }

        private int which;
        private long time;
    }


}
