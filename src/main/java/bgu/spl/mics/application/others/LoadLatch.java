package bgu.spl.mics.application.others;

import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * this class was made for make sure that we start the TimeService after all the other threads
 * are ready to start the missions.
 * the fields contains list of threads that include all the subscribers, the thread of the TimeService
 * and a Countdown-latch that we use for that.
 */
public class LoadLatch {
    private List<Thread> threadList;
    private Thread timeService;
    private static CountDownLatch latchi;

    /**
     * public constructor of this class.
     */
    public LoadLatch(List<Thread> threadList, Thread timeService){
        this.threadList = threadList;
        this.timeService = timeService;
        latchi = new CountDownLatch(threadList.size());
    }

    /**
     * a method that we use for start all the subscribers threads and wait until everyone start together and
     * than TimeService start
     */
    public void start(){
        for (Thread thread: threadList){
            thread.start();
        }
        try {
            latchi.await();//wait until countdown get to 0
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        timeService.start();
    }

    /**
     * make a countdown in the latch
     */
    public static void CountdownLatch(){
        latchi.countDown();
    }
}
