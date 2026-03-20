package com.strikerkk.linkedin.posts_service.service;

import com.strikerkk.linkedin.posts_service.dto.PostCreateRequestDto;
import com.strikerkk.linkedin.posts_service.dto.PostDto;
import com.strikerkk.linkedin.posts_service.entity.Posts;
import com.strikerkk.linkedin.posts_service.exception.ResourceNotFoundException;
import com.strikerkk.linkedin.posts_service.repository.PostsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostsService {

    private final PostsRepository postsRepository;
    private final ModelMapper modelMapper;


    public PostDto createPost(PostCreateRequestDto postCreateRequestDto, Long userId) {
        Posts posts = modelMapper.map(postCreateRequestDto, Posts.class);
        posts.setUserId(userId);

        Posts savedPost = postsRepository.save(posts);
        return modelMapper.map(savedPost, PostDto.class);
    }

    public PostDto getPostById(Long postId) {
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
