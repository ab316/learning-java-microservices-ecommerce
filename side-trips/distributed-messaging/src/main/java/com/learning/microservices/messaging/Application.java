package com.learning.microservices.messaging;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@Slf4j
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
        return new CommandLineRunner() {
            @Autowired
            private Server server;
            @Autowired
            private Client client;
            @Autowired
            private JobProcessor jobProcessor;

            @Override
            public void run(String... args) throws Exception {
                if (args.length < 1) {
                    log.error("Please provide one of the follow arguments: server/client/job");
                    return;
                }
                log.info("Application started");
                switch (args[0]) {
                    case "server":
                        log.info("Starting server");
                        server.start();
                        break;
                    case "client":
                        String host = args.length >= 2 ? args[1] : "localhost";
                        log.info("Starting client");
                        client.start(host);
                        break;
                    case "job":
                        log.info("Starting job processor");
                        jobProcessor.start();
                        break;
                    default:
                        throw new IllegalArgumentException("Invalid argument: " + args[0]);
                }
            }
        };
    }
}
