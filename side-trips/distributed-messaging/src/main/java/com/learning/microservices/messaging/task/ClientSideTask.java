package com.learning.microservices.messaging.task;

import com.learning.microservices.messaging.Server;
import com.learning.microservices.messaging.util.Task;
import lombok.extern.slf4j.Slf4j;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

@Slf4j
public class ClientSideTask implements Task {
    private static final String MSG_SEED = "Hello #: ";
    private final long id;
    private final String serverHost;

    public ClientSideTask(String serverHost, long id) {
        this.serverHost = serverHost;
        this.id = id;
    }

    @Override
    public void execute() {
        try (
                var clientSocket = new Socket(serverHost, Server.PORT);
                var dataOutputStream = new DataOutputStream(clientSocket.getOutputStream());
                var dataInputStream = new DataInputStream(clientSocket.getInputStream());
        ) {
            String toServer = MSG_SEED + id;
            dataOutputStream.writeUTF(toServer);
            dataOutputStream.flush();
            String fromServer = dataInputStream.readUTF();
            log.info("Sent to server: [{}]. Received from server: [{}]", toServer, fromServer);

        } catch (UnknownHostException e) {
            log.error(e.getMessage());
        } catch (IOException e) {
            log.error("Socket exception: {}", e.toString());
        }
    }
}
