package com.strikerkk.linkedin.posts_service.service;

import com.strikerkk.linkedin.posts_service.auth.UserContextHolder;
import com.strikerkk.linkedin.posts_service.clients.ConnectionsClient;
import com.strikerkk.linkedin.posts_service.dto.PostCreateRequestDto;
import com.strikerkk.linkedin.posts_service.dto.PostDto;
import com.strikerkk.linkedin.posts_service.entity.Posts;
import com.strikerkk.linkedin.posts_service.events.PostCreatedEvent;
import com.strikerkk.linkedin.posts_service.exception.ResourceNotFoundException;
import com.strikerkk.linkedin.posts_service.repository.PostsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostsService {

    private final PostsRepository postsRepository;
    private final ConnectionsClient connectionsClient;
    private final ModelMapper modelMapper;

    private final KafkaTemplate<Long, PostCreatedEvent> kafkaTemplate;


    public PostDto createPost(PostCreateRequestDto postCreateRequestDto) {
        Long userId = UserContextHolder.getCurrentUserId();
        Posts posts = modelMapper.map(postCreateRequestDto, Posts.class);
        posts.setUserId(userId);

        Posts savedPost = postsRepository.save(posts);
        PostCreatedEvent postCreatedEvent = PostCreatedEvent.builder()
                .postId(posts.getId())
                .creatorId(userId)
                .content(posts.getContent())
                .build();

        kafkaTemplate.send("post-created-topic", postCreatedEvent);

        return modelMapper.map(savedPost, PostDto.class);
    }

    public PostDto getPostById(Long postId) {
        log.debug("Retrieving post with Id: {}", postId);

        Posts post =  postsRepository.findById(postId).orElseThrow(() ->
                new ResourceNotFoundException("Post not found with id: " + postId));

        return modelMapper.map(post, PostDto.class);
    }

    public List<PostDto> getAllPostOfUser(Long userId) {
        List<Posts> postsList = postsRepository.findByUserId(userId);

        return postsList
                .stream()
                .map((element) -> modelMapper.map(element, PostDto.class))
                .collect(Collectors.toList());
    }
}
