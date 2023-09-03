package com.project.snsserver.domain.notification.rabbitmq;

import com.project.snsserver.domain.notification.model.dto.NotificationMessage;
import com.project.snsserver.domain.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationConsumer {

    private final NotificationService notificationService;

    @RabbitListener(queues = "notification.queue")
    public void consumeNotification(NotificationMessage message) {
        log.info(String.format("consume message >> %s", message));
        notificationService.send(message);
    }
}
