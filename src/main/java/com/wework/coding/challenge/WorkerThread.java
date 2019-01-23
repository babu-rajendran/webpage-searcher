package com.wework.coding.challenge;

import java.util.concurrent.BlockingQueue;

/**
 * A {@link Thread} class which is part of the {@link CustomThreadPool} and executes the tasks from
 * the task queue.
 */
public class WorkerThread extends Thread {

    private BlockingQueue<Runnable> taskQueue;
    private CustomThreadPool customThreadPool;

    public WorkerThread(BlockingQueue<Runnable> queue,
        CustomThreadPool customThreadPool) {
        taskQueue = queue;
        this.customThreadPool = customThreadPool;
    }

    public void run() {
        try {
            //Runs the tasks in the task queue until there are some tasks left to be executed.
            while (true) {
                // Pick up the task from the queue.
                Runnable runnable = taskQueue.take();
                runnable.run();

                if ((this.taskQueue.size() == 0) && (this.customThreadPool.isShutdownTriggered())) {
                    this.interrupt();
                }
            }
        } catch (InterruptedException e) {
            System.out.println(Thread.currentThread().getName() + " is stopped.");
        }
    }
}

