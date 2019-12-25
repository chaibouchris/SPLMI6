package bgu.spl.mics.application.myClasses;

import java.util.List;
import java.util.concurrent.CountDownLatch;

public class LoadLatch {
    private List<Thread> threadList;
    private Thread timeService;
    private static CountDownLatch latchi;

    public LoadLatch(List<Thread> threadList, Thread timeService){
        this.threadList = threadList;
        this.timeService = timeService;
        latchi = new CountDownLatch(threadList.size());
    }

    public void start(){
        for (Thread thread: threadList){
            thread.start();
        }
        try {
            latchi.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        timeService.start();
    }

    public static void CountdownLatch(){
        latchi.countDown();
    }
}
