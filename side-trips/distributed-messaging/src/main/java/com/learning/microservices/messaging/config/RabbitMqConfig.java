package com.learning.microservices.messaging.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {
    public static final String MESSAGING_IN_ROUTING_KEY = "messaging.in";
    public static final String MESSAGING_OUT_ROUTING_KEY = "messaging.out";

    @Bean
    DirectExchange exchange() {
        return new DirectExchange("ecommerce.sidetrips");
    }

    @Bean
    Queue messagingInQueue() {
        return new Queue("messaging.in");
    }

    @Bean
    Queue messagingOutQueue() {
        return new Queue("messaging.out");
    }

    @Bean
    Binding messagingInBinding(Queue messagingInQueue, DirectExchange exchange) {
        return BindingBuilder
                .bind(messagingInQueue)
                .to(exchange)
                .with(MESSAGING_IN_ROUTING_KEY);
    }

    @Bean
    Binding messagingOutBinding(Queue messagingOutQueue, DirectExchange exchange) {
        return BindingBuilder
                .bind(messagingOutQueue)
                .to(exchange)
                .with(MESSAGING_OUT_ROUTING_KEY);
    }
}
