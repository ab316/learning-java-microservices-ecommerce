package com.learning.microservices.messaging;

import com.learning.microservices.messaging.config.RabbitMqConfig;
import com.learning.microservices.messaging.util.MonitorThread;
import com.learning.microservices.messaging.util.RejectedExecutionHandlerImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.DirectMessageListenerContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.util.concurrent.RejectedExecutionHandler;

@Component
@Slf4j
public class JobProcessor implements MessageListener {
    @Autowired
    private AmqpTemplate amqpTemplate;
    @Autowired
    private ConnectionFactory connectionFactory;
    @Autowired
    private Queue messagingInQueue;

    private final ThreadPoolTaskExecutor executor;

    public JobProcessor() {
        RejectedExecutionHandler rejectedExecutionHandler = new RejectedExecutionHandlerImpl();
        executor = new ThreadPoolTaskExecutor();
        executor.setRejectedExecutionHandler(rejectedExecutionHandler);
        executor.setMaxPoolSize(8);
        executor.setCorePoolSize(4);
        executor.setQueueCapacity(16);
        executor.setThreadNamePrefix("Processor");
        executor.setThreadGroupName("JobProcessor");
        executor.initialize();
        Thread monitorThread = new Thread(new MonitorThread(executor.getThreadPoolExecutor(), 30));
        monitorThread.start();
    }

    public void start() {
        var container = new DirectMessageListenerContainer(connectionFactory);
        container.setConsumersPerQueue(20);
        container.addQueues(messagingInQueue);
        container.setTaskExecutor(executor);
        container.setMessageListener(this);
        container.start();
    }

    @Override
    public void onMessage(Message message) {
        String body = new String(message.getBody());
        String correlationId = message.getMessageProperties().getCorrelationId();
        log.debug("Message received. Correlation id: [{}], Body: [{}]", correlationId, body);

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            log.error(e.toString());
        }

        MessageProperties messageProperties = new MessageProperties();
        messageProperties.setCorrelationId(correlationId);
        Message messageToSend = new Message(body.getBytes(), messageProperties);
        amqpTemplate.send(RabbitMqConfig.MESSAGING_OUT_ROUTING_KEY, messageToSend);
    }
}
