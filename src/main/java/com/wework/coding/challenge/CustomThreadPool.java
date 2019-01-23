package com.wework.coding.challenge;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * A customized implementation of a thread pool using {@link LinkedBlockingQueue}
 */
public class CustomThreadPool {

    private BlockingQueue<Runnable> taskQueue;

    private boolean shutdownTriggered = false;

    public CustomThreadPool(int numberOfThreads) {
        taskQueue = new LinkedBlockingQueue<>(numberOfThreads);

        for (int i = 1; i <= numberOfThreads; i++) {
            WorkerThread workerThread = new WorkerThread(taskQueue, this);
            workerThread.start();
        }
    }

    /**
     * Executor for the given {@link Runnable} task
     *
     * @param task the task to be executed
     * @throws Exception if shutdown has been triggered
     */
    public synchronized void execute(Runnable task) throws Exception {
        if (this.shutdownTriggered) {
            System.out.println("Thread pool shutdown has been initiated");
            throw new Exception(
                "CustomThreadPool has been shutDown, no further tasks can be added");
        }
        this.taskQueue.put(task);
    }


    /**
     * Checks if the thread pool shutdown has been initiated.
     *
     * @return <code>true</code> If shutdown has been triggered, <code>false</code> otherwise
     */
    public boolean isShutdownTriggered() {
        return shutdownTriggered;
    }


    /**
     * Initiates shutdown of the thread pool.
     */
    public synchronized void shutdown() {
        this.shutdownTriggered = true;
    }
}

