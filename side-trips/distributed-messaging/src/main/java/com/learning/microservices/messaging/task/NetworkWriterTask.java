package com.learning.microservices.messaging.task;

import com.learning.microservices.messaging.Server;
import com.learning.microservices.messaging.util.Task;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

@Slf4j
public class NetworkWriterTask implements Task {
    private final Message message;

    public NetworkWriterTask(Message message) {
        this.message = message;
    }

    @Override
    public void execute() {
        String body = new String(message.getBody());
        String correlationIdStr = message.getMessageProperties().getCorrelationId();
        Long correlationId = Long.parseLong(correlationIdStr);

        log.debug("AMQP Message received. Correlation id: [{}], Body: [{}]", correlationId, body);

        var maybeSocket = Server.getSocket(correlationId);
        if (maybeSocket.isEmpty()) {
            log.error("Socket not available for correlationId [{}]", correlationId);
            return;
        }
        Server.removeSocket(correlationId);

        Socket socket = maybeSocket.get();
        if (socket.isClosed()) {
            log.error("Socket already closed for correlation id [{}]", correlationId);
            return;
        }

        try (var dataOutputStream = new DataOutputStream(socket.getOutputStream())) {
            dataOutputStream.writeUTF(body);
            dataOutputStream.flush();

            log.debug("Responded to client");
        } catch (IOException e) {
            log.error(e.toString());
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                log.error(e.toString());
            }
        }
    }
}
