package com.learning.microservices.messaging;

import com.learning.microservices.messaging.task.ClientSideTask;
import com.learning.microservices.messaging.util.MonitorThread;
import com.learning.microservices.messaging.util.RejectedExecutionHandlerImpl;
import com.learning.microservices.messaging.util.Worker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.concurrent.*;

@Component
@Slf4j
public class Client {
    private ThreadPoolExecutor executor;

    public void start(String serverHost) {
        init();
        connect(serverHost);
    }

    private void init() {
        var threadFactory = Executors.defaultThreadFactory();
        BlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<>(16);
        RejectedExecutionHandler rejectedExecutionHandler = new RejectedExecutionHandlerImpl();
        executor = new ThreadPoolExecutor(3, 16, 10, TimeUnit.SECONDS,
                workQueue, threadFactory, rejectedExecutionHandler);

        Thread monitorThread = new Thread(new MonitorThread(executor, 30));
        monitorThread.start();
    }

    private void connect(String serverHost) {
        long seed = 0;
        final int numTasks = 6;
        ClientSideTask[] tasks = new ClientSideTask[numTasks];
        while (true) {
            for (int i = 0; i < numTasks; i++) {
                tasks[i] = new ClientSideTask(serverHost, seed++);
            }
            Arrays.stream(tasks).forEach(task -> executor.execute(new Worker(task)));
            try {
                Thread.sleep(2_000);
            } catch (InterruptedException e) {
                log.error(e.getMessage());
            }
        }
    }
}
