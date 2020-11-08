package nachos.threads;

import nachos.machine.*;

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

        speakerWaitingCond = new Condition(lock);
        hasMessageCond = new Condition(lock);
        wantToListenCond = new Condition(lock);
        hasSpokenCond = new Condition(lock);
        hasListenedCond = new Condition(lock);
        listenerWaitingCond = new Condition(lock);
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
    private Condition speakerWaitingCond;

    private boolean hasMessage;
    private Condition hasMessageCond;

    private boolean wantToListen;
    private Condition wantToListenCond;

    private boolean hasSpoken;
    private Condition hasSpokenCond;

    private boolean hasListened;
    private Condition hasListenedCond;

    private boolean isListenerWaiting;
    private Condition listenerWaitingCond;

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
}
