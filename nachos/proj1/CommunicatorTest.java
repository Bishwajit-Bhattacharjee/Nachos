package nachos.proj1;
import nachos.threads.*;

class CommunicatorTest {
    public CommunicatorTest() {
        this.communicator = new Communicator();
    }

    public void performTest() {
        System.out.println("*************** Testing for task 2 and 4 *******************");

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
