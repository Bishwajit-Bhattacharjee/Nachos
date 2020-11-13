package nachos.proj1;

import nachos.machine.Machine;
import nachos.threads.KThread;
import nachos.threads.ThreadedKernel;

public class WaitUntilTest {

    public void performTest() {

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
