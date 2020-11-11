package nachos.threads;

/**
 * A <i>communicator</i> allows threads to synchronously exchange 32-bit
 * messages. Multiple threads can be waiting to <i>speak</i>,
 * and multiple threads can be waiting to <i>listen</i>. But there should never
 * be a time when both a speaker and a listener are waiting, because the two
 * threads can be paired off at this point.
 */
public class Communicator {
    /**
     * Allocate a new communicator.
     */
    public Communicator() {
        this.lock = new Lock();

        isSpeakerWaiting = false;
        hasMessage = false;
        wantToListen = false;
        hasSpoken = false;
        hasListened = false;
        isListenerWaiting = false;

        speakerWaitingCond = new Condition2(lock);
        hasMessageCond = new Condition2(lock);
        wantToListenCond = new Condition2(lock);
        hasSpokenCond = new Condition2(lock);
        hasListenedCond = new Condition2(lock);
        listenerWaitingCond = new Condition2(lock);
    }

    /**
     * Wait for a thread to listen through this communicator, and then transfer
     * <i>word</i> to the listener.
     *
     * <p>
     * Does not return until this thread is paired up with a listening thread.
     * Exactly one listener should receive <i>word</i>.
     *
     * @param    word    the integer to transfer.
     */

    private Lock lock;

    private int message;

    private boolean isSpeakerWaiting;
    private Condition2 speakerWaitingCond;

    private boolean hasMessage;
    private Condition2 hasMessageCond;

    private boolean wantToListen;
    private Condition2 wantToListenCond;

    private boolean hasSpoken;
    private Condition2 hasSpokenCond;

    private boolean hasListened;
    private Condition2 hasListenedCond;

    private boolean isListenerWaiting;
    private Condition2 listenerWaitingCond;

    public void speak(int word) {

        lock.acquire();

        while(isSpeakerWaiting) speakerWaitingCond.sleep();
        isSpeakerWaiting = true;

        hasMessage = true;
        hasMessageCond.wake();

        while(!wantToListen) wantToListenCond.sleep();

        message = word;
        System.out.println(KThread.currentThread().getName()+" spoke "+word);

        hasMessage = false;

        hasSpoken = true;
        hasSpokenCond.wake();

        while(!hasListened) hasListenedCond.sleep();

        hasListened = false;

        isSpeakerWaiting = false;
        speakerWaitingCond.wake();

        lock.release();

    }

    /**
     * Wait for a thread to speak through this communicator, and then return
     * the <i>word</i> that thread passed to <tt>speak()</tt>.
     *
     * @return the integer transferred.
     */
    public int listen() {

        lock.acquire();

        while(isListenerWaiting) listenerWaitingCond.sleep();
        isListenerWaiting = true;

        while(!hasMessage) hasMessageCond.sleep();

        wantToListen = true;
        wantToListenCond.wake();

        while(!hasSpoken) hasSpokenCond.sleep();

        int retVal = message;
        System.out.println(KThread.currentThread().getName()+" listened "+message);

        wantToListen = false;

        hasListened = true;
        hasListenedCond.wake();

        hasSpoken = false;

        isListenerWaiting = false;
        listenerWaitingCond.wake();

        lock.release();

        return retVal;

    }

    private static class Speaker implements Runnable{

        Speaker(Communicator communicator)
        {
            this.communicator = communicator;
        }

        private Communicator communicator;

        @Override
        public void run() {
            for(int i = 0; i < 5; i++)
            {
              //  KThread.yield();
                this.communicator.speak(i);
              //  KThread.yield();
            }
        }
    }

    private static class Listener implements Runnable{

        Listener(Communicator communicator)
        {
            this.communicator = communicator;
        }

        private Communicator communicator;

        @Override
        public void run() {
            for(int i = 0; i < 5; i++)
            {
              //  KThread.yield();
                this.communicator.listen();
               // KThread.yield();
            }
        }
    }

    public static void selfTest()
    {
        Communicator communicator = new Communicator();

        KThread t0 = new KThread(new Speaker(communicator)).setName("speaker " + 0);
        KThread t1 = new KThread(new Speaker(communicator)).setName("speaker " + 1);
        KThread t2 = new KThread(new Speaker(communicator)).setName("speaker " + 2);
        KThread t3 = new KThread(new Speaker(communicator)).setName("speaker " + 3);
        KThread t4 = new KThread(new Speaker(communicator)).setName("speaker " + 4);


        KThread t00 = new KThread(new Listener(communicator)).setName("listener " + 0);
        KThread t11 = new KThread(new Listener(communicator)).setName("listener " + 1);
        KThread t22 = new KThread(new Listener(communicator)).setName("listener " + 2);
        KThread t33 = new KThread(new Listener(communicator)).setName("listener " + 3);
        KThread t44 = new KThread(new Listener(communicator)).setName("listener " + 4);

        t0.fork();
        t1.fork();
        t2.fork();
        t3.fork();
        t4.fork();

        t00.fork();
        t11.fork();
        t22.fork();
        t33.fork();
        t44.fork();

        t0.join();
        t1.join();
        t2.join();
        t3.join();
        t4.join();

        t00.join();
        t11.join();
        t22.join();
        t33.join();
        t44.join();

    }
}
