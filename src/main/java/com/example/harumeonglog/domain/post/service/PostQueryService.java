package com.example.harumeonglog.domain.post.service;


import com.example.harumeonglog.domain.post.dto.response.PostResponse;
import com.example.harumeonglog.domain.post.entity.Post;
import org.springframework.data.domain.Slice;

public interface PostQueryService {
    Slice<Post> getPosts(Long cursor, Integer size);

    PostResponse.PostDetailResponse getPost(Long postId);

    Slice<Post> getMyPost(Long cursor, Integer size);

    Slice<Post> getMyLikePost(Long cursor, Integer size);
}
