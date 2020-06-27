package com.learning.microservices.messaging.util;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

@Slf4j
public class RejectedExecutionHandlerImpl implements RejectedExecutionHandler {
    @Override
    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
        log.warn("{} is rejected", r.toString());
        log.warn("[{}/{} Active: {}, Completed: {}, Task: {}, isShutdown: {}, isTerminated: {}]",
                executor.getPoolSize(),
                executor.getCorePoolSize(),
                executor.getActiveCount(),
                executor.getCompletedTaskCount(),
                executor.getTaskCount(),
                executor.isShutdown(),
                executor.isTerminated());
    }
}
