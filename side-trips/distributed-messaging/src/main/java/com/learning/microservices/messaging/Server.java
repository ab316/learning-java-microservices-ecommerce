package com.learning.microservices.messaging;

import com.learning.microservices.messaging.task.NetworkReaderTask;
import com.learning.microservices.messaging.task.NetworkWriterTask;
import com.learning.microservices.messaging.util.MonitorThread;
import com.learning.microservices.messaging.util.RejectedExecutionHandlerImpl;
import com.learning.microservices.messaging.util.Worker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.DirectMessageListenerContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.*;

@Component
@Slf4j
public class Server implements MessageListener {
    public static int PORT = 1100;
    private static final Map<Long, Socket> clientSockets = new ConcurrentHashMap<>();

    private final AmqpTemplate amqpTemplate;
    private final ConnectionFactory connectionFactory;
    private final Queue messagingOutQueue;

    private long count = 1000;
    private ThreadPoolExecutor executor;

    @Autowired
    public Server(AmqpTemplate amqpTemplate, ConnectionFactory connectionFactory, Queue messagingOutQueue) {
        this.amqpTemplate = amqpTemplate;
        this.connectionFactory = connectionFactory;
        this.messagingOutQueue = messagingOutQueue;
    }

    public void start() {
        init();
        listen();
    }

    private void init() {
        var threadFactory = Executors.defaultThreadFactory();
        BlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<>(16);
        RejectedExecutionHandler rejectedExecutionHandler = new RejectedExecutionHandlerImpl();
        executor = new ThreadPoolExecutor(3, 16, 10, TimeUnit.SECONDS,
                workQueue, threadFactory, rejectedExecutionHandler);

        Thread monitorThread = new Thread(new MonitorThread(executor, 30));
        monitorThread.start();

        initQueueListener();
    }

    private void initQueueListener() {
        var container = new DirectMessageListenerContainer(connectionFactory);
        container.setConsumersPerQueue(4);
        container.addQueues(messagingOutQueue);
        container.setMessageListener(this);
        container.start();
    }

    private void listen() {
        try (var serverSocket = new ServerSocket(PORT)) {
            log.info("Listening for new connections on port {}...", PORT);
            while (true) {
                Socket clientSocket = serverSocket.accept();
                log.debug("Receiving a new connection");

                long correlationId = getNextCorrelationId();
                clientSockets.put(correlationId, clientSocket);
                executor.execute(new Worker(new NetworkReaderTask(amqpTemplate, correlationId)));
            }
        } catch (IOException ex) {
            log.error(ex.getMessage());
        }
    }

    public static Optional<Socket> getSocket(Long correlationId) {
        return Optional.ofNullable(clientSockets.get(correlationId));
    }

    public static void removeSocket(Long correlationId) {
        clientSockets.remove(correlationId);
    }

    private synchronized long getNextCorrelationId() {
        return count++;
    }

    @Override
    public void onMessage(Message message) {
        NetworkWriterTask task = new NetworkWriterTask(message);
        executor.execute(new Worker(task));
    }
}
