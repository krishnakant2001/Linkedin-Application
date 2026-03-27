package com.strikerkk.linkedin.notification_service.consumers;

import com.strikerkk.linkedin.notification_service.clients.ConnectionsClient;
import com.strikerkk.linkedin.notification_service.dto.PersonDto;
import com.strikerkk.linkedin.notification_service.entity.Notification;
import com.strikerkk.linkedin.notification_service.repository.NotificationRepository;
import com.strikerkk.linkedin.posts_service.events.PostCreatedEvent;
import com.strikerkk.linkedin.posts_service.events.PostLikedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class PostServiceConsumer {

    private final ConnectionsClient connectionsClient;
    private final NotificationRepository notificationRepository;

    @KafkaListener(topics = "post-created-topic")
    public void handlePostCreated(PostCreatedEvent postCreatedEvent) {
        log.info("Sending notifications: handlePostCreated: {}", postCreatedEvent);
        List<PersonDto> connections = connectionsClient.getFirstConnections(postCreatedEvent.getCreatorId());

        for(PersonDto connection: connections) {
            sendNotification(connection.getUserId(), "Your connection " + postCreatedEvent.getCreatorId() +
                    "a post, Check it out");
        }
    }

    @KafkaListener(topics = "post-liked-topic")
    public void handlePostLiked(PostLikedEvent postLikedEvent) {
        log.info("Sending notification: handlePostLiked: {}", postLikedEvent);
        String message = String.format("Your post, %d has been liked by %d,", postLikedEvent.getPostId(),
                postLikedEvent.getLikedByUserId());

        sendNotification(postLikedEvent.getCreatorId(), message);
    }

    public void sendNotification(Long userId, String message) {
        Notification notification = new Notification();
        notification.setMessage(message);
        notification.setUserId(userId);

        notificationRepository.save(notification);
    }
}
