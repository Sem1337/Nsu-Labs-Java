package main.threadpool;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;

public class ThreadPool implements Executor {
    private final BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<>();

    public ThreadPool(int nThreads) {
        for (int i = 0; i < nThreads; i++) {
            new Thread(
                    () -> {
                        while(!Thread.currentThread().isInterrupted()) {
                            try {
                                Runnable nextTask = workQueue.take();
                                nextTask.run();
                            } catch (InterruptedException e) {
                                Thread.currentThread().interrupt();
                            }
                        }
                    }
            ).start();
        }
    }

    @Override
    public void execute(Runnable command) {
        try {
            workQueue.put(command);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

}
