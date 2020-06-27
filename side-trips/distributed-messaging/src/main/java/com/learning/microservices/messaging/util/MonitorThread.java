package com.learning.microservices.messaging.util;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ThreadPoolExecutor;

@Slf4j
public class MonitorThread implements Runnable {
    private final ThreadPoolExecutor executor;
    private final int seconds;
    private boolean run;

    public MonitorThread(ThreadPoolExecutor executor, int seconds) {
        this.executor = executor;
        this.seconds = seconds;
        this.run = true;
    }

    public void shutdown() {
        run = false;
    }

    @Override
    public void run() {
        Thread.currentThread().setName("MonitorThread");
        while (run) {
            log.debug("[{}/{} Active: {}, Completed: {}, Task: {}, isShutdown: {}, isTerminated: {}]",
                    executor.getPoolSize(),
                    executor.getCorePoolSize(),
                    executor.getActiveCount(),
                    executor.getCompletedTaskCount(),
                    executor.getTaskCount(),
                    executor.isShutdown(),
                    executor.isTerminated());
            try {
                Thread.sleep(1000L * seconds);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
