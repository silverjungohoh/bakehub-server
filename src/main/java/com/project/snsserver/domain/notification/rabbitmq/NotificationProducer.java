package com.project.snsserver.domain.notification.rabbitmq;

import com.project.snsserver.domain.notification.model.dto.NotificationMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationProducer {

    private static final String EXCHANGE_NAME = "notification.exchange";
    private static final String ROUTING_KEY = "notification.key";

    private final RabbitTemplate rabbitTemplate;

    public void produce(NotificationMessage message) {
        log.info(String.format("produce message >> %s", message));
        rabbitTemplate.convertAndSend(EXCHANGE_NAME, ROUTING_KEY, message);
    }
}
