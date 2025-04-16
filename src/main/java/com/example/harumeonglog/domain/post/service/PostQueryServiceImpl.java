package com.example.harumeonglog.domain.post.service;


import com.example.harumeonglog.domain.member.converter.MemberConverter;
import com.example.harumeonglog.domain.member.entity.Member;
import com.example.harumeonglog.domain.post.converter.PostConverter;
import com.example.harumeonglog.domain.post.dto.response.PostResponse;
import com.example.harumeonglog.domain.post.entity.Post;
import com.example.harumeonglog.domain.post.entity.PostImage;
import com.example.harumeonglog.domain.post.repository.PostRepository;
import com.example.harumeonglog.global.error.code.PostErrorCode;
import com.example.harumeonglog.global.error.exception.PostException;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostQueryServiceImpl implements PostQueryService {

    private final PostRepository postRepository;

    @Override
    public Slice<Post> getPosts(Long cursor, Integer size) {
        return null;
    }

    @Override
    public PostResponse.PostDetailResponse getPost(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new PostException(PostErrorCode.NOT_FOUND));
        List<String> postImageList = post.getPostImageList().stream().map(PostImage::getPostImageKeyName).toList();
        return PostConverter.toPostDetailResponse(post, MemberConverter.toMemberInfoResponse(post.getMember()), postImageList);
    }

    @Override
    public Slice<Post> getMyPost(Long cursor, Integer size) {
        return null;
    }

    @Override
    public Slice<Post> getMyLikePost(Long cursor, Integer size) {
        return null;
    }
}
