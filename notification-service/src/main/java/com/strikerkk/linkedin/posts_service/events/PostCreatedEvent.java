package com.strikerkk.linkedin.posts_service.events;

import lombok.Data;

@Data
public class PostCreatedEvent {
    Long creatorId;
    Long postId;
    String content;
}
