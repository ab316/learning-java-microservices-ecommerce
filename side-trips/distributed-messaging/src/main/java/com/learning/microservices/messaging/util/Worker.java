package com.learning.microservices.messaging.util;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Worker implements Runnable {
    private final Task task;

    public Worker(Task task) {
        this.task = task;
    }

    @Override
    public void run() {
        log.trace("Thread: {}-{} executing. Wrapper Object Ref: {}",
                Thread.currentThread().getId(), Thread.currentThread().getName(), this);
        task.execute();
    }
}
