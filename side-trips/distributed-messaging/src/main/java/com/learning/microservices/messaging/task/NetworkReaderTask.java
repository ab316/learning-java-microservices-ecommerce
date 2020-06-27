package com.learning.microservices.messaging.task;

import com.learning.microservices.messaging.Server;
import com.learning.microservices.messaging.config.RabbitMqConfig;
import com.learning.microservices.messaging.util.Task;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Optional;

@Slf4j
public class NetworkReaderTask implements Task {
    private final AmqpTemplate amqpTemplate;
    private final long correlationId;

    public NetworkReaderTask(AmqpTemplate amqpTemplate, long correlationId) {
        this.amqpTemplate = amqpTemplate;
        this.correlationId = correlationId;
    }

    @Override
    public void execute() {
        Optional<Socket> maybeClientSocket = Server.getSocket(correlationId);
        if (maybeClientSocket.isEmpty()) {
            log.error("Socket not present for correlation id [{}]", correlationId);
            return;
        }

        Socket clientSocket = maybeClientSocket.get();
        if (clientSocket.isClosed()) {
            log.error("Socket already closed for correlation id [{}]", clientSocket);
            return;
        }

        try {
            // Must not close the input-stream here, otherwise the client will receive EOF and will stop waiting
            // for input
            var dataInputStream = new DataInputStream(clientSocket.getInputStream());
            String fromClient = dataInputStream.readUTF();
            log.debug("Message from client: {}", fromClient);

            MessageProperties messageProperties = new MessageProperties();
            messageProperties.setCorrelationId(Long.toString(correlationId));
            Message message = new Message(fromClient.getBytes(), messageProperties);
            amqpTemplate.send(RabbitMqConfig.MESSAGING_IN_ROUTING_KEY, message);

        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
