package com.strikerkk.linkedin.connections_service.consumers;

import com.strikerkk.linkedin.connections_service.repository.PersonRepository;
import com.strikerkk.linkedin.user_service.event.UserCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserCreatedConsumer {

    private final PersonRepository personRepository;

    @KafkaListener(topics = "user-created-topic")
    public void handleCreateNewUserNode(UserCreatedEvent userCreatedEvent) {
        personRepository.createNewUserNode(userCreatedEvent.getId(), userCreatedEvent.getName());
    }
}
