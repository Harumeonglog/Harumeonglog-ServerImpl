package com.example.harumeonglog.domain.post.converter;

import com.example.harumeonglog.domain.member.converter.MemberConverter;
import com.example.harumeonglog.domain.member.dto.response.MemberResponse;
import com.example.harumeonglog.domain.post.dto.response.PostResponse;
import com.example.harumeonglog.domain.post.entity.Post;

import java.util.List;

public class PostConverter {

    public static PostResponse.PostDetailResponse toPostDetailResponse(Post post, MemberResponse.MemberInfoResponse memberInfoResponse, List<String> imageList) {
        return PostResponse.PostDetailResponse.builder()
                .postId(post.getId())
                .postCategory(post.getCategory())
                .memberInfoResponse(memberInfoResponse)
                .likeNum(post.getPostLikeNum())
                .commentNum(post.getCommentNum())
                .postImageList(imageList)
                .build();
    }
}
