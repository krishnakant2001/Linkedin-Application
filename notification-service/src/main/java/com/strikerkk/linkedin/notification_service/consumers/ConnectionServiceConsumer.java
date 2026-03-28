package com.strikerkk.linkedin.notification_service.consumers;

import com.strikerkk.linkedin.connections_service.event.AcceptConnectionRequestEvent;
import com.strikerkk.linkedin.connections_service.event.SendConnectionRequestEvent;
import com.strikerkk.linkedin.notification_service.service.SendNotification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ConnectionServiceConsumer {

    private final SendNotification sendNotification;

    @KafkaListener(topics = "send-connection-request-topic")
    public void handleSendConnectionRequest(SendConnectionRequestEvent sendConnectionRequestEvent) {
        String message = String
                .format("You have received the connection request from user with Id: %d", sendConnectionRequestEvent.getSenderId());

        sendNotification.send(sendConnectionRequestEvent.getReceiverId(), message);
    }

    @KafkaListener(topics = "accept-connection-request-topic")
    public void handleAcceptConnectionRequest(AcceptConnectionRequestEvent acceptConnectionRequestEvent) {
        String message = String
                .format("Your connection request accepted by the user with Id: %d", acceptConnectionRequestEvent.getReceiverId());

        sendNotification.send(acceptConnectionRequestEvent.getSenderId(), message);
    }

}
